package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

import java.util.ArrayList;

/**
 * Interface AbstractRotate
 * Cette interface permet d'effectuer une rotation sur un point par rapport au centre du repère
 */
public abstract class AbstractRotate {

    /**
     * Modifie l'orientation d'un point
     * @param point Point à transposer
     * @return Point transposé
     */
    public abstract <T extends Point> T translate(T point);


    /**
     * Retourne les coordonées d'une liste des points
     * @param points Liste des points
     * @return Liste des points modifiés
     */
    public <T extends Point> ArrayList<T> translatePoints(ArrayList<T> points) {
        ArrayList<T> newPoints = new ArrayList<>();

        for (T point : points) {
            newPoints.add(translate(point));
        }

        return newPoints;
    }


    /**
     * Renvoie les coordonnées minimum en X
     * @param point1 Point 1
     * @param point2 Point 3
     * @return Coordonées en X minimum
     */
    public double translateMinX(Point point1, Point point2) {
        Point translatePoint1 = translate(point1);
        Point translatePoint2 = translate(point2);
        return Math.min(translatePoint1.getX(), translatePoint2.getX());
    }


    /**
     * Renvoie les coordonnées maximum en X
     * @param point1 Point 1
     * @param point2 Point 3
     * @return Coordonées en X maximum
     */
    public double translateMaxX(Point point1, Point point2) {
        Point translatePoint1 = translate(point1);
        Point translatePoint2 = translate(point2);
        return Math.max(translatePoint1.getX(), translatePoint2.getX());
    }


    /**
     * Renvoie les coordonnées minimum en Y
     * @param point1 Point 1
     * @param point2 Point 3
     * @return Coordonées en Y minimum
     */
    public double translateMinY(Point point1, Point point2) {
        Point translatePoint1 = translate(point1);
        Point translatePoint2 = translate(point2);
        return Math.min(translatePoint1.getY(), translatePoint2.getY());
    }


    /**
     * Renvoie les coordonnées maximum en Y
     * @param point1 Point 1
     * @param point2 Point 3
     * @return Coordonées en Y maximum
     */
    public double translateMaxY(Point point1, Point point2) {
        Point translatePoint1 = translate(point1);
        Point translatePoint2 = translate(point2);
        return Math.max(translatePoint1.getY(), translatePoint2.getY());
    }
}
