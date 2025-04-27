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
    private double Q = 200;
    private int numAnts;
    private Random random = new Random();

    // Adaptive parameters
    private double minPheromone = 0.001;
    private double maxPheromone = 10.0;
    private int stagnationCounter = 0;
    private final int MAX_STAGNATION = 30;
    private double bestDistance = Double.MAX_VALUE;

    // Local search parameters
    private final boolean USE_2OPT = true;
    private final int TWO_OPT_FREQUENCY = 5;

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
        Path bestIterationPath = null;
        double bestIterationDistance = Double.MAX_VALUE;

        // Step 1: Each ant builds a tour
        for (int k = 0; k < numAnts; k++) {
            Path path = buildAntPath(k);

            // Apply local search occasionally
            if (USE_2OPT && k % TWO_OPT_FREQUENCY == 0) {
                path = apply2Opt(path);
            }

            double distance = evaluation.evaluate(path);

            // Track best iteration solution
            if (distance < bestIterationDistance) {
                bestIterationDistance = distance;
                bestIterationPath = path;
            }
        }

        // Step 2: Update pheromones
        evaporatePheromones();

        // Deposit pheromones for best iteration path (elitist strategy)
        if (bestIterationPath != null) {
            depositPheromones(bestIterationPath, bestIterationDistance);

            // Adaptive parameter adjustment
            if (bestIterationDistance < bestDistance) {
                bestDistance = bestIterationDistance;
                stagnationCounter = 0;
            } else {
                stagnationCounter++;
                if (stagnationCounter > MAX_STAGNATION) {
                    adjustParameters();
                    stagnationCounter = 0;
                }
            }
        }
    }
    // methode pour construire le chemin de la fourmi a partir de la matrice des phéromones
    private Path buildAntPath(int antIndex) {
        int n = problem.getLength();
        boolean[] visited = new boolean[n];
        int[] path = new int[n];

        // Point de départ différent pour chaque fourmi
        int startCity = antIndex % n;
        int currentCity = startCity;
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

        // Calcul des probabilités basées sur les phéromones et l'heuristique
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], alpha) *
                        Math.pow(heuristicMatrix[currentCity][i], beta);
                sum += probabilities[i];
            }
        }

        // Sélection probabiliste
        while (true) {
            int candidate = random.nextInt(n);
            if (!visited[candidate] && (sum == 0 || random.nextDouble() < probabilities[candidate] / sum)) {
                return candidate;
            }
        }
    }

    // Evaporation of pheromones permet de diminuer la quantité de phéromones sur les arêtes
    private void evaporatePheromones() {
        int n = pheromones.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] = Math.max(minPheromone, pheromones[i][j] * (1 - evaporation));
            }
        }
    }

    // Dépôt de phéromones sur les arêtes du chemin trouvé par la fourmi
    private void depositPheromones(Path path, double distance) {
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
    }
//--------------------------------------------------------------------------------------------------------------------
    // Local search using 2-opt pour optimiser le chemin, il faut que le paramètre USE_2OPT soit à true
    private Path apply2Opt(Path path) {
        int[] tour = path.getPath().clone();
        int n = tour.length;
        boolean improved = true;
        int maxIterations = 1000;
        int iterations = 0;

        while (improved && iterations < maxIterations) {
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
}
