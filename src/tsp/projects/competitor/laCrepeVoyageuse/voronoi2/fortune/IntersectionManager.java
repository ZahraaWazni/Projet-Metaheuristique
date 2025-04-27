package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Class IntersectionManager
 * Cette classe permet de déterminer une intersection à partir de 2 points
 */
public class IntersectionManager {

    // Renvoies des intersections

    /**
     * Renvoie les intersections à partir de deux points
     * Chacun des points retournés à la même distance que ceux passés en paramètres
     * On définit la zone d'influence d'un point comme la zone où tous les points qui sont à l'intérieur sont plus proche de ce point plutôt qu'un autre
     * Cette zone dépend de la SweepLine qui est une ligne de balayage
     * À partir cette ligne, on établit le fait qu'un point sur la parabole est à égal de distance du point référent de la zone d'influence et de la ligne de balayage
     * Cette fonction permet de trouver les points d'intersection deux points à partir de leur zone d'influence et la ligne de balayage
     * @param sweepLine SweepLine
     * @param point1 1er point
     * @param point2 2ème point
     * @return Liste de points représentants les points d'intersection
     */
    public static ArrayList<Point> getIntersections(SweepLine sweepLine, Point point1, Point point2) throws Exception {

        /**
         * On effectue le calcul des points deux fois avec les points dans le désordre
         * En effet, il se peut qu'il y ait une différence entre les deux résultats à cause de la complexité du calcul
         */
        ArrayList<Point> listSolutions1 = IntersectionManager.getSolutionsWithDelta(sweepLine, point1, point2);
        ArrayList<Point> listSolutions2 = IntersectionManager.getSolutionsWithDelta(sweepLine, point2, point1);

        /**
         * On effectue la moyenne des solutions afin d'obtenir le résultat le plus précis possible
         * En inversant les points dans le calcul, les solutions sont retournées dans le sens inverse
         */

        if (listSolutions1.size() == listSolutions2.size()) {
            ArrayList<Point> newSolutions = new ArrayList<>();

            for (int i = 0; i < listSolutions1.size(); i++) {
                Point solution1 = listSolutions1.get(i);
                Point solution2 = listSolutions2.get(listSolutions2.size() - i - 1);

                // Calcul de la moyenne des coordonnées des 2 points
                double xNew = (solution1.getX() + solution2.getX()) / 2;
                double yNew = (solution1.getY() + solution2.getY()) / 2;
                newSolutions.add(new Point(xNew, yNew));
            }

            return newSolutions;
        }
        else {
            return listSolutions1;
        }
    }


    /**
     * Déterminer les points d'intersection des zones des 2 points à partir d'une équation du second degré (calcul du delta)
     * @param sweepLine SweepLine
     * @param point1 1er point
     * @param point2 2ème point
     * @return Liste de points représentants les points d'intersection
     */
    private static ArrayList<Point> getSolutionsWithDelta(SweepLine sweepLine, Point point1, Point point2) throws Exception {

        // Si une erreur survient lors du traitement du point, on renvoie simplement un tableau vide suivi d'une alerte dans la console
        try {
            // Initialisation des variables
            Point translatePoint1 = sweepLine.translateToSweepLine(point1);
            Point translatePoint2 = sweepLine.translateToSweepLine(point2);

            double x1 = translatePoint1.getX();
            double y1 = translatePoint1.getY();
            double x2 = translatePoint2.getX();
            double y2 = translatePoint2.getY();
            double x1pow2 = x1 * x1;
            double x2pow2 = x2 * x2;


            /**
             * Si une des valeurs sur l'axe des Y est égal à 0, alors on transpose simplement l'abscisse sur l'autre parabole
             * Sinon, on calcule le delta afin de déterminer la position
             */

            // Si les deux éléments sont sur la SweepLine, il est impossible de trouver une solution
            if (y1 == 0 && y2 == 0) {
                return new ArrayList<>();
            }

            // Si seulement le 1er point est sur la SweepLine, alors le transpose sur la zone d'influence du 2ème
            else if (y1 == 0) {
                double y = (Math.pow((x1 - x2), 2) / (2*y2)) + (y2/2);
                return new ArrayList<>(List.of(
                    sweepLine.translateToGraph(new Point(x1, y))
                ));
            }

            // Si seulement le 2ème point est sur la SweepLine, alors le transpose sur la zone d'influence du 1er
            else if (y2 == 0) {
                double y = (Math.pow((x2 - x1), 2) / (2*y1)) + (y1/2);
                return new ArrayList<>(List.of(
                    sweepLine.translateToGraph(new Point(x2, y))
                ));
            }

            // Sinon, on effectue le calcul avec le delta
            else {

                // Création d'un polynome du second degré
                double alpha = (1/(2*y1)) - (1/(2*y2));
                double beta = -(x1/y1) + (x2/y2);
                double gamma = (x1pow2/(2*y1)) - (x2pow2/(2*y2))+ ((y1-y2)/2);
                double delta = Math.pow(beta, 2) - 4 * alpha * gamma;

                // Déterminer les coordonées en X des intersections
                ArrayList<Double> tabX = new ArrayList<>();

                /*
                 * Si alpha est égal à 0, cela signifie que les deux points ont la même ordonnée
                 * Dans ce cas, le point d'intersection par rapport à l'axe des abscisses correspond à la moyenne des abscisses des points
                 */
                if (alpha == 0) {
                    double xIntersectionAlpha = (x1 + x2) / 2;
                    tabX.add(xIntersectionAlpha);
                }

                // Si le delta est égal à 0, alors il n'a qu'un seul point d'intersection entre les deux points
                else if (delta == 0) {
                    double xIntersectionDelta = (-beta) / (2*alpha);
                    tabX.add(xIntersectionDelta);
                }

                // Sinon, on calcul les coordonnées des deux points d'intersection
                else {
                    double xIntersectionDelta1 = (-beta - Math.sqrt(delta)) / (2*alpha);
                    double xIntersectionDelta2 = (-beta + Math.sqrt(delta)) / (2*alpha);
                    tabX.add(xIntersectionDelta1);
                    tabX.add(xIntersectionDelta2);
                }

                // Génération des coordonnées des points à partir des coordonnées en X
                ArrayList<Point> tabPoints = new ArrayList<>();

                for (double xGen : tabX) {
                    double yGen1 = (Math.pow((xGen - x1), 2) / (2 * y1)) + (y1 / 2);
                    double yGen2 = (Math.pow((xGen - x2), 2) / (2 * y2)) + (y2 / 2);
                    double YNewGen = (yGen1 + yGen2) / 2;

                    tabPoints.add(sweepLine.translateToGraph(new Point(xGen, YNewGen)));
                }

                return tabPoints;
            }
        }
        catch (Exception e) {

            // Affiche de l'erreur et on met fin au traitement
            throw new Exception("Un problème est survenu lors du traitement du calcul de l'intersection avec le delta : " + e.getMessage());
        }
    }


    // Sélectionne une intersection

    /**
     * Sélectionne un point parmi les points d'intersection
     * L'objectif est de pouvoir déterminer entre nos deux points lequel appartient à l'intervalle recherché
     * @param point1 1er point
     * @param point2 2ème point
     * @param intersections Liste des points d'intersection
     * @return Point sélectionné
     */
    public static Point selectMainPoint(Point point1, Point point2, ArrayList<Point> intersections) {

        // Initialisation des données
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();

        double mainX = x1;
        boolean isFirstPoint = true;

        /*
         * Pour sélectionner le bon, il faut trouver le point le plus bas parmi les deux points
         * Le point le plus bas à la zone d'influence la plus importante
         * Par conséquent, on sait que les zones d'intersection se trouvent de part et d'autre du point
         */
        if (y1 > y2) {
            mainX = x2;
            isFirstPoint = false;
        }

        // Recherche du point parmi les solutions
        for (Point intersection : intersections) {
            double xIntersection = intersection.getX();

            // Si c'est le 1er point qui est plus bas que le 2nd, alors le point d'intersection qu'on cherche est le 1er avec une abscisse plus grande que le point le plus bas
            if (xIntersection >= mainX && isFirstPoint) {
                return intersection;
            }

            // Si c'est le 2nd point qui est plus bas que le 1er, alors le point d'intersection qu'on cherche est le 1er avec une abscisse plus petite que le point le plus bas
            if (xIntersection <= mainX && !isFirstPoint) {
                return intersection;
            }
        }

        // Si on ne trouve pas d'intersection valide, on renvoie null
        return null;
    }
}
