package tsp.projects.competitor.laCrepeVoyageuse;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Coordinates;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class LaCrepeVoyageuse extends CompetitorProject {

    private Path currentSolution;
    private double temperature;
    private final double COOLING_RATE = 0.995;
    private final double INITIAL_TEMP = 2000;                           // i think thi is the best value for the initial temperature

    // setting up the constructor
    public LaCrepeVoyageuse(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);                                                  // Call the constructor of the parent class to initialize the bot
        setMethodName("La Crêpe Voyageuse");                                // The name of our bot
        setAuthors("Zahraa WAZNI", "Antoine FEISTHAUER ");
    }


    @Override
    public void initialization() {
        // We will implement the Simulated Annealing algorithm here
        currentSolution = new Path(problem.getLength());                    // Initialize the solution with a random path : get number of cities
        evaluation.evaluate(currentSolution);                               // Evaluate the solution to get the cost

        // 2. Initialize SA parameters
        temperature = INITIAL_TEMP;                                                 // Adjust based on problem scale
    }

    @Override
    public void loop() {

        // Generate neighbor solution (small perturbation)
        Path newSolution = generateNeighbor(currentSolution);

        // Evaluate both solutions
        double currentEnergy = evaluation.quickEvaluate(currentSolution);
        double newEnergy = evaluation.quickEvaluate(newSolution);
        double delta = newEnergy - currentEnergy;

        // Simulated Annealing acceptance
        if (acceptSolution(delta, temperature)) {
            currentSolution = newSolution;
            evaluation.evaluate(currentSolution); // Updates best solution if improved
        }

        // Periodically apply 2-Opt optimization (every ~100 iterations)
        if (Math.random() < 0.01) {
            currentSolution = twoOptOptimize(currentSolution);
            evaluation.evaluate(currentSolution);
        }

        // Cool down temperature
        temperature *= COOLING_RATE;
    }
    // Helper Methods --------------------------------------------------------

    // method that generates a neighbor solution by swapping two random cities
    private Path generateNeighbor(Path path) {
        // Create a small perturbation (swap two random cities)
        int[] newPath = path.getCopyPath();
        int i = (int) (Math.random() * newPath.length);
        int j = (int) (Math.random() * newPath.length);

        // Swap cities
        int temp = newPath[i];
        newPath[i] = newPath[j];
        newPath[j] = temp;

        return new Path(newPath);
    }
    // method that accepts a new solution based on the temperature and the energy difference
    private boolean acceptSolution(double delta, double temp) {
        // Always accept better solutions
        if (delta < 0) return true;
        // Sometimes accept worse solutions (based on temperature)
        return Math.exp(-delta / temp) > Math.random();
    }

    // method that applies the 2-opt optimization to a path
    private Path twoOptOptimize(Path path) {
        int[] currentPath = path.getCopyPath();
        boolean improved = true;

        while (improved) {
            improved = false;
            for (int i = 0; i < currentPath.length - 1; i++) {
                for (int j = i + 2; j < currentPath.length; j++) {
                    double before = calculateSegmentLength(currentPath, i, j);
                    // Perform 2-opt swap
                    reverseArraySegment(currentPath, i + 1, j);
                    double after = calculateSegmentLength(currentPath, i, j);

                    if (after < before) {
                        improved = true;
                    } else {
                        // Revert if no improvement
                        reverseArraySegment(currentPath, i + 1, j);
                    }
                }
            }
        }
        return new Path(currentPath);
    }
    // method that calculates the length of a segment in a path
    private double calculateSegmentLength(int[] path, int i, int j) {
        // Calculate distance between city i and i+1 + city j-1 and j
        Coordinates c1 = problem.getCoordinates(path[i]);
        Coordinates c2 = problem.getCoordinates(path[i + 1]);
        Coordinates c3 = problem.getCoordinates(path[j - 1]);
        Coordinates c4 = problem.getCoordinates(path[j]);

        return c1.distance(c2) + c3.distance(c4);
    }

    // method that reverses a segment in a path
    private void reverseArraySegment(int[] array, int start, int end) {
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }
}
