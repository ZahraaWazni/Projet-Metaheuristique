package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

import java.util.ArrayList;

/**
 * Class Circle
 * Représente un cercle du graphe
 * Un cercle est créé à partir de 3 points du graphes
 */
public class Circle extends Point {

    // Variables

    // Informations du cercle
    private double radius = 0;
    private ArrayList<Point> points = new ArrayList<>();


    // Constructeur

    /**
     * Constructeur de la classe Circle
     * @param points Liste des points qui composent le cercle (il faut obligatoirement que le cercle soit composé de 3 points)
     */
    public Circle(ArrayList<Point> points) throws Exception {
        if (points.size() != 3) {
            throw new Exception("Le nombre de points pour faire un cercle doit être égale à 3");
        }
        this.points = points;

        // Génération des propriétés du cercle
        setId(generateId(points.get(0), points.get(1), points.get(2)));
        setXY(calculateCenterOfCircle());
        radius = calculateRadius();
    }


    // Getter

    /**
     * Renvoie le rayon du cercle
     * @return Nombre flottant
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Renvoie la liste des points qui composent le cercle
     * @return Liste d'object Point
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Renvoie le point le plus bas du cercle
     * @return Objet Point
     */
    public Point getTopCircle() {
        return new Point(getX(), getY() - radius);
    }


    // Autres fonctions

    /**
     * Génère un identifiant à partir de 3 points
     * @param point1 point 1
     * @param point2 point 2
     * @param point3 point 3
     * @return Identifiant au format String
     */
    public static String generateId(Point point1, Point point2, Point point3) {
        return point1.getId() + "-" + point2.getId() + "-" + point3.getId();
    }

    /**
     * Renvoie le centre du cercle circonscrit à partir des points qui le composent
     * @return Centre du cercle sous la forme d'un objet point
     */
    private Point calculateCenterOfCircle() throws Exception {

        // Récupération des coordonnées des trois points
        double x1 = points.get(0).getX();
        double y1 = points.get(0).getY();
        double x2 = points.get(1).getX();
        double y2 = points.get(1).getY();
        double x3 = points.get(2).getX();
        double y3 = points.get(2).getY();

        // Calcul des carrés des coordonées
        double p1pow2 = Math.pow(x1, 2) + Math.pow(y1, 2);
        double p2pow2 = Math.pow(x2, 2) + Math.pow(y2, 2);
        double p3pow2 = Math.pow(x3, 2) + Math.pow(y3, 2);

        // Calcul du déterminant
        double det = 2 * (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));

        // Si le déterminant est égal à 0, on arrête le programme, car sinon, cela implique une division par 0
        if (det == 0) {
            throw new Exception("Les trois points sont alignés, pas de cercle circonscrit possible");
        }

        // Calcul des coordonnées du centre du cercle circonscrit
        double x = ((p1pow2 * (y2 - y3)) + (p2pow2 * (y3 - y1)) + (p3pow2 * (y1 - y2))) / det;
        double y = ((p1pow2 * (x3 - x2)) + (p2pow2 * (x1 - x3)) + (p3pow2 * (x2 - x1))) / det;

        return new Point(x, y);
    }

    private double calculateRadius() {

        // Récupération des coordonnées d'un point
        double x1 = points.get(0).getX();
        double y1 = points.get(0).getY();

        // Calcul de la distance entre ce point et le centre du cercle
        return Math.sqrt(Math.pow((getX() - x1), 2) + Math.pow((getY() - y1), 2));
    }


    // Affichage dans la console de l'objet

    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "Circle - " + super.toString();
    }
}
