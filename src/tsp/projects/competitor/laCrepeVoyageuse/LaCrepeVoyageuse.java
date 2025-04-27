package tsp.projects.competitor.laCrepeVoyageuse;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Random;

public class LaCrepeVoyageuse extends CompetitorProject {

    // ACO Parameters
    private double[][] pheromones;
    private double[][] heuristicMatrix;
    private double[][] distanceMatrix;
    private double alpha = 1.0;
    private double beta = 3.0;
    private double evaporation = 0.4;
    private int numAnts;
    private Random random = new Random();

    // Adaptive parameters
    private int stagnationCounter = 0;
    private final int MAX_STAGNATION = 30;
    private double bestDistance = Double.MAX_VALUE;

    // Local search parameters
    private final boolean USE_2OPT = true;
    private final int TWO_OPT_FREQUENCY = 5;
    private int totalStagnationCounter = 0; // Counter for big stagnation

    public LaCrepeVoyageuse(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        setMethodName("La Crêpe Voyageuse");
        setAuthors("Zahraa WAZNI", "Antoine FEISTHAUER");
    }

    @Override
    public void initialization() {
        // Nombre de villes
        int n = problem.getLength();

        // Nombre de fourmis limité à 50 max car sinon trop long
        numAnts = Math.min(50, n);

        // Initialisation de la matrice des distances entre villes qui va servir à la construction de la solution
        distanceMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distanceMatrix[i][j] = problem.getCoordinates(i).distance(problem.getCoordinates(j));
            }
        }

        // Matrice heuristique (1 / distance) permettra de favoriser les villes proches
        heuristicMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                heuristicMatrix[i][j] = 1.0 / Math.max(1, distanceMatrix[i][j]);
            }
        }

        // Initialisation des phéromones avec une valeur basée sur le voisin le plus proche
        double initialPheromone = 1.0 / (n * calculateNearestNeighborDistance());
        pheromones = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = i == j ? 0 : initialPheromone;
            }
        }
    }

    @Override
    public void loop() {
        int n = problem.getLength();

        Path[] topPaths = new Path[3];
        double[] topDistances = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};

        for (int k = 0; k < numAnts; k++) {
            Path path = buildAntPath(k);

            if (USE_2OPT && k % TWO_OPT_FREQUENCY == 0) {
                path = apply2Opt(path, 50); // Limiting 2-opt to 50ms
            }

            double distance = evaluation.evaluate(path);

            // Update top 3 best paths
            for (int i = 0; i < 3; i++) {
                if (distance < topDistances[i]) {
                    for (int j = 2; j > i; j--) {
                        topDistances[j] = topDistances[j - 1];
                        topPaths[j] = topPaths[j - 1];
                    }
                    topDistances[i] = distance;
                    topPaths[i] = path;
                    break;
                }
            }
        }

        evaporatePheromones();

        for (int i = 0; i < 3; i++) {
            if (topPaths[i] != null) {
                if (USE_2OPT) {
                    topPaths[i] = apply2Opt(topPaths[i], 50); // 2-Opt again with 50ms limit
                }
                depositPheromones(topPaths[i], topDistances[i]);
            }
        }

        // Update best global distance
        if (topDistances[0] < bestDistance) {
            bestDistance = topDistances[0];
            stagnationCounter = 0;
            totalStagnationCounter = 0; // Reset big stagnation counter too
        } else {
            stagnationCounter++;
            totalStagnationCounter++;

            if (stagnationCounter > MAX_STAGNATION) {
                adjustParameters();
                stagnationCounter = 0;
            }

            if (totalStagnationCounter > 100) {
                restartPheromones();
                totalStagnationCounter = 0;
            }
        }
    }
    // methode pour construire le chemin de la fourmi a partir de la matrice des phéromones
    private Path buildAntPath(int antIndex) {
        int n = problem.getLength();
        boolean[] visited = new boolean[n];
        int[] path = new int[n];

        // Point de départ différent pour chaque fourmi
        int currentCity = antIndex % n;
        path[0] = currentCity;
        visited[currentCity] = true;

        // Construction du chemin
        for (int i = 1; i < n; i++) {
            int nextCity = selectNextCity(currentCity, visited);
            path[i] = nextCity;
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        return new Path(path);
    }

    // methode pour choisir la prochaine ville a partir de la matrice des phéromones
    private int selectNextCity(int currentCity, boolean[] visited) {
        int n = problem.getLength();
        double sum = 0.0;
        double[] probabilities = new double[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], alpha) *
                        Math.pow(heuristicMatrix[currentCity][i], beta);
                sum += probabilities[i];
            }
        }
        // 5% d'exploration totalement aléatoire
        if (random.nextDouble() < 0.05) {
            int candidate;
            do {
                candidate = random.nextInt(n);
            } while (visited[candidate]);
            return candidate;
        }
        while (true) {
            int candidate = random.nextInt(n);
            if (!visited[candidate] && (sum == 0 || random.nextDouble() < probabilities[candidate] / sum)) {
                return candidate;
            }
        }
    }

    // Evaporation of pheromones permet de diminuer la quantité de phéromones sur les arêtes
    private void evaporatePheromones() {
        double minPheromone = 0.001;
        double maxPheromone = 10.0;
        int n = pheromones.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = Math.max(minPheromone, pheromones[i][j] * (1 - evaporation));
            }
        }
    }

    // Dépôt de phéromones sur les arêtes du chemin trouvé par la fourmi
    private void depositPheromones(Path path, double distance) {
        double maxPheromone = 10.0;
        double Q = 200;
        int[] tour = path.getPath();
        double depositAmount = Q / distance;

        for (int i = 0; i < tour.length; i++) {
            int from = tour[i];
            int to = tour[(i + 1) % tour.length];
            pheromones[from][to] = Math.min(maxPheromone, pheromones[from][to] + depositAmount);
            pheromones[to][from] = pheromones[from][to]; // symmetric TSP
        }
    }

    // Calcul de la distance du voisin le plus proche pour initialiser les phéromones
    private double calculateNearestNeighborDistance() {
        int n = problem.getLength();
        double total = 0;

        for (int i = 0; i < n; i++) {
            double minDist = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (i != j && distanceMatrix[i][j] < minDist) {
                    minDist = distanceMatrix[i][j];
                }
            }
            total += minDist;
        }

        return total / n;
    }

    // Adjust parameters dynamically based on stagnation
    private void adjustParameters() {
        // Dynamically adjust parameters to escape local optima
        evaporation = Math.min(0.5, evaporation * 1.1);
        alpha = Math.max(0.5, alpha * 0.95);
        beta = Math.min(5.0, beta * 1.05);

        // reduce number of ants to explore less ( min 10 ants)
        numAnts = Math.max(10, (int)(numAnts * 0.9));
    }
//--------------------------------------------------------------------------------------------------------------------
    // Local search using 2-opt pour optimiser le chemin, il faut que le paramètre USE_2OPT soit à true
    private Path apply2Opt(Path path, long maxMillis) {
        int[] tour = path.getPath().clone();
        int n = tour.length;
        boolean improved = true;
        int maxIterations = 1000;
        int iterations = 0;
        long startTime = System.currentTimeMillis();

        while (improved && iterations < maxIterations && (System.currentTimeMillis() - startTime) < maxMillis) {
            improved = false;
            iterations++;

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int a = tour[i];
                    int b = tour[(i + 1) % n];
                    int c = tour[j];
                    int d = tour[(j + 1) % n];

                    double currentDistance = distanceMatrix[a][b] + distanceMatrix[c][d];
                    double newDistance = distanceMatrix[a][c] + distanceMatrix[b][d];

                    if (newDistance < currentDistance) {
                        reverseSubTour(tour, (i + 1) % n, j);
                        improved = true;
                    }
                    if ((System.currentTimeMillis() - startTime) >= maxMillis) {
                        break;
                    }
                }
                if ((System.currentTimeMillis() - startTime) >= maxMillis) {
                    break;
                }
            }
        }
        return new Path(tour);
    }

    // Reverse the sub-tour between two indices in the tour
    private void reverseSubTour(int[] tour, int start, int end) {
        int n = tour.length;
        while (start != end && start != (end + 1) % n) {
            int temp = tour[start];
            tour[start] = tour[end];
            tour[end] = temp;
            start = (start + 1) % n;
            if (start == end) break;
            end = (end - 1 + n) % n;
        }
    }

    // Restart pheromones if stagnation occurs
    private void restartPheromones() {
        int n = problem.getLength();
        double initialPheromone = 1.0 / (n * calculateNearestNeighborDistance());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = (i == j) ? 0 : initialPheromone;
            }
        }
    }
}
