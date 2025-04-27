package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

/**
 * Rotate0Deg
 * Cette classe ne change pas l'orientation du point
 */
public class Rotate0Deg extends AbstractRotate {

    /**
     * Modifie la direction d'un point (point -> top)
     * @param point Point à transposer
     * @return Point transposé
     */
    @Override
    public Point translate(Point point) {
        return point;
    }
}
