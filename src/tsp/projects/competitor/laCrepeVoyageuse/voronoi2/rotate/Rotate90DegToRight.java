package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

/**
 * Rotate90DegToRight
 * Cette classe tourne un point vers la droite
 */
public class Rotate90DegToRight extends AbstractRotate {

    /**
     * Modifie la direction d'un point (point -> right)
     * @param point Point à transposer
     * @return Point transposé
     */
    @Override
    public Point translate(Point point) {
        return new Point(point.getY(), -point.getX(), point.getId());
    }
}