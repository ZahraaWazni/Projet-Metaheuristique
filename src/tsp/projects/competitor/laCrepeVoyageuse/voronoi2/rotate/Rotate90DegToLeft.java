package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

/**
 * Rotate90DegToLeft
 * Cette classe tourne un point vers la gauche
 */
public class Rotate90DegToLeft extends AbstractRotate {

    /**
     * Modifie la direction d'un point (point -> left)
     * @param point Point à transposer
     * @return Point transposé
     */
    @Override
    public Point translate(Point point) {
        return new Point(-point.getY(), point.getX(), point.getId());
    }
}
