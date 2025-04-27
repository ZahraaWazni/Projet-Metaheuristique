package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

/**
 * Rotate180Deg
 * Cette classe modifie l'orientation d'un point vers le bas
 */
public class Rotate180Deg extends AbstractRotate {

    /**
     * Retourne un point
     * @param point Point à transposer
     * @return Point transposé
     */
    @Override
    public Point translate(Point point) {
        return new Point(-point.getX(), -point.getY(), point.getId());
    }
}