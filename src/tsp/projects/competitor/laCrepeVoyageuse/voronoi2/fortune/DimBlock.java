package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

/**
 * Class Data
 * Cette classe est un singleton qui contient les données sur les dimensions du graphe
 */
public class DimBlock {

    // Variables

    // Dimensions du graphe
    private Point minPoint = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE);
    private Point maxPoint = new Point(Double.MAX_VALUE, Double.MAX_VALUE);


    // Constructeur

    /**
     * Constructeur de la classe en privé pour empêcher les autres programmes de l'instancier
     */
    public DimBlock(Point minPoint, Point maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
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


    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "DimBlock [minPoint=" + minPoint + ", maxPoint=" + maxPoint + "]";
    }
}
