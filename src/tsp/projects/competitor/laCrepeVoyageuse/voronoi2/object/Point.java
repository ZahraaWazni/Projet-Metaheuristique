package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object;

/**
 * Class Point
 * Cette classe représente un point du graphe
 */
public class Point {

    // Variables

    // Identifiant du point
    private String id = "";

    // Coordonnées du point
    private double x = Double.MAX_VALUE;
    private double y = Double.MAX_VALUE;


    // Constructeur

    /**
     * Constructeur de la class Point (via un point)
     * @param point Object point
     */
    public Point(Point point) {
        this.id = point.getId();
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Constructeur de la class Point (via des coordonnées et un identifiant)
     * @param x Coordonnées en X
     * @param y Coordonnées en Y
     * @param id identifiant
     */
    public Point(double x, double y, String id) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur de la class Point (via des coordonnées uniquement)
     * @param x double Coordonnée en X
     * @param y double Coordonnée en Y
     */
    public Point(double x, double y) {
        this.id = "-1";
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur de la class Point (point vide)
     */
    public Point() {}


    // Setter

    /**
     * Modifie l'identifiant du point
     * @param id Nouvel identifiant
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Modifie les coordonneés du point à partir des coordonnées X et Y
     * @param x Coordonnée en X
     * @param y Coordonnée en Y
     */
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Modifie les coordonneés du point à partir d'un autre point
     * @param point Objet point
     */
    public void setXY(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }


    // Getter

    /**
     * Renvoie l'identifiant
     * @return Identifiant sous la forme d'une chaine de caractères
     */
    public String getId() {
        return id;
    }

    /**
     * Renvoie la coordonée en X
     * @return Coordonée sous la forme d'un nombre flottant
     */
    public double getX() {
        return x;
    }

    /**
     * Renvoie la coordonée en Y
     * @return Coordonée sous la forme d'un nombre flottant
     */
    public double getY() {
        return y;
    }


    // Affichage dans la console de l'objet

    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "Point [id=" + id + ", x=" + x + ", y=" + y + "]";
    }
}
