package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

/**
 * Class Data
 * Cette classe est un singleton qui contient les données sur les dimensions du graphe
 */
public class DataGraph {

    // Variables

    // Dimensions du graphe
    private Point minPoint = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE);
    private Point maxPoint = new Point(Double.MAX_VALUE, Double.MAX_VALUE);

    // Instance de classe
    private static DataGraph instance = null;


    // Constructeur

    /**
     * Constructeur de la classe en privé pour empêcher les autres programmes de l'instancier
     */
    private DataGraph() {}


    // Instance de la classe

    /**
     * Renvoie une instance de la classe DataGraph
     * Toutes les instances de cette classe sont similaires les unes des autres !
     * @return Object DataGraph
     */
    public static DataGraph getInstance() {
        if (instance == null) {
            instance = new DataGraph();
        }
        return instance;
    }


    // Setter

    /**
     * Sauvegarde le point le plus petit du graphe
     * Ce point correspond à la dimension minimum du graphe
     * @param minPoint Plus petit point
     */
    public void setMinPoint(Point minPoint) {
        this.minPoint = minPoint;
    }

    /**
     * Sauvegarde le point le plus grand du graphe
     * Ce point correspond à la dimension maximum du graphe
     * @param maxPoint Plus grand point
     */
    public void setMaxPoint(Point maxPoint) {
        this.maxPoint = maxPoint;
    }


    // Getter

    /**
     * Renvoie les coordonnées du point le plus petit possible dans le graphe
     * Ce point correspond à la dimension minimum du graphe
     * @return Objet Point
     */
    public Point getMinPoint() {
        return minPoint;
    }

    /**
     * Renvoie les coordonnées du point le plus grand possible dans le graphe
     * Ce point correspond à la dimension maximum du graphe
     * @return Objet Point
     */
    public Point getMaxPoint() {
        return maxPoint;
    }
}
