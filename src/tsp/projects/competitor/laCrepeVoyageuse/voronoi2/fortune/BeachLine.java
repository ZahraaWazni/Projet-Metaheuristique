package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Range;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate.AbstractRotate;

import java.util.ArrayList;

/**
 * Class BeachLine
 * Cette classe est un singleton et gère la zone d'influence des points lors de la génération du diagramme de Voronoi
 */
public class BeachLine {

    // Variables

    // Informations de la BeachLine
    private ArrayList<Point> zones = new ArrayList<>();
    private ArrayList<Range> ranges = new ArrayList<>();


    // Getter

    /**
     * Renvoie la liste des points de la BeachLine
     * @return Tableau d'objet Point
     */
    public ArrayList<Point> getZones() {
        return zones;
    }

    /**
     * Renvoie la liste des intervalles de la BeachLine
     * @return Tableau d'objet Range
     */
    public ArrayList<Range> getRanges() {
        return ranges;
    }


    // Check

    /**
     * Vérifie si un cercle est toujours "d'actualité"
     * On considère commme actuel, si tous les points qui le composent sont toujours présent dans la BeachLine et dans le bon ordre
     * @param circle Cercle à vérifier
     * @return Renvoie vrai si le cercle est valide
     */
    public boolean checkValidCircle(Circle circle) {
        ArrayList<Point> points = circle.getPoints();

        for (int i = 0; i < zones.size(); i++) {
            if (checkSeq(points, i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie de manière récursive si la séquence de point apparaît dans la plage
     * @param points Liste de points
     * @param startIndex Index de départ
     * @return Résultat de la vérification (renvoie vrai si la séquence de points apparaît dans la zone)
     */
    private boolean checkSeq(ArrayList<Point> points, int startIndex) {
        return recCheckSeq(points, startIndex, 0, 0);
    }

    /**
     * Vérifie de manière récursive si la séquence de point apparaît dans la plage
     * @param points Liste de points
     * @param startIndex Index de départ
     * @param indexPoint Index courant sur la table de points
     * @param indexZone Index courant sur la zone
     * @return Résultat de la vérification (renvoie vrai si la séquence de points apparaît dans la zone)
     */
    private boolean recCheckSeq(ArrayList<Point> points, int startIndex, int indexPoint, int indexZone) {

        // Si on arrive au bout de la recherche, alors la séquence est validée
        if (indexPoint >= points.size()) {
            return true;
        }

        // Si on arrive à la fin de la chaine, alors c'est que la chaine n'est pas présente
        else if (startIndex + indexPoint >= zones.size()) {
            return false;
        }

        // Si les points sont similaires, alors on passe à l'index suivant
        else if (points.get(indexPoint).getId().equals(zones.get(startIndex + indexZone).getId())) {
            indexPoint += 1;
            indexZone += 1;
            return recCheckSeq(points, startIndex, indexPoint, indexZone);
        }

        // Sinon, si les points sont différents, on vérifie si le point n'a pas déjà été traité en réalité auparavant
        else {
            boolean stillValid = false;

            for (int i = 0; i < indexPoint; i++) {
                if (points.get(i).getId().equals(zones.get(startIndex + indexZone).getId())) {
                    stillValid = true;
                }
            }

            if (stillValid) {
                indexZone += 1;
                return recCheckSeq(points, startIndex, indexPoint, indexZone);
            }
            else {
                return false;
            }
        }
    }


    // Add

    /**
     * Ajout d'un nouveau point sur la BeachLine
     * On renvoie l'index sur lequel on a ajouté le point
     * @param point Point à ajouter
     * @return Index où l'élément a été ajouté
     */
    public int addPointOnTheBeach (Point point) {
        return addPointOnTheBeach(point, 0);
    }

    /**
     * Ajout d'un nouveau point sur la BeachLine
     * On renvoie l'index sur lequel on a ajouté le point
     * @param point Point à ajouter
     * @return Index où l'élément a été ajouté
     */
    private int addPointOnTheBeach (Point point, int index) {

        // Initialisation des variables (point)
        double xPoint = point.getX();

        // Si l'index arrive à la fin du tableau, alors on met fin à la récursive
        if (index >= zones.size()) {
            zones.add(point);
            ranges.add(new Range(xPoint, xPoint));
            return -1;
        }

        // Initialisation des variables (intervalle)
        double startRange = ranges.get(index).getStartRange();
        double endRange = ranges.get(index).getEndRange();

        // Si la coordonée en X correspond à l'intervalle de départ, alors on ajoute le point à l'index actuel
        if (xPoint == startRange) {
            zones.add(index, point);
            ranges.add(index, new Range(xPoint, xPoint));
            return index;
        }

        // Si la coordonnée en X est comprise dans un intervalle, alors on coupe l'intervalle en deux et on ajoute le point au milieu
        else if (startRange < xPoint && xPoint < endRange) {

            // Mise à jour de l'intervalle précédent
            ranges.set(index, new Range(startRange, xPoint));

            // Ajout du nouveau point
            if (index + 1 < zones.size()) {
                zones.add(index + 1, point);
                ranges.add(index + 1, new Range(xPoint, xPoint));
            }
            else {
                zones.add(point);
                ranges.add(new Range(xPoint, xPoint));
            }

            // Ajout d'un nouvel intervalle pour l'ancien point
            if (index + 2 < zones.size()) {
                zones.add(index + 2, zones.get(index));
                ranges.add(index + 2, new Range(xPoint, endRange));
            }
            else {
                zones.add(zones.get(index));
                ranges.add(new Range(xPoint, endRange));
            }

            return index + 1;
        }

        // Sinon, on passe à l'index suivant
        else {
            index += 1;
            return addPointOnTheBeach(point, index);
        }
    }


    // Update

    /**
     * Mise à jour de la plage pour un instant t de la génération du graphe
     * Cette fonction met à jour les intervalles entre les valeurs et supprime les points lorsqu'ils sont totalement recouvert
     * On estime qu'un point est recouvert lorsque son minimum et son maximum s'inverse
     * @param translate translate du point
     * @param sweepLine SweepLine
     */
    public void updateBeach(AbstractRotate translate, SweepLine sweepLine, DimBlock dimBlock) throws Exception {

        // S'il y a au moins 2 éléments sur notre plage, on peut effectuer la mise à jouer
        if (zones.size() >= 2) {

            // Initialisation des données (on récupère les dimensions du graphe)
            double xMinGraph = translate.translateMinX(dimBlock.getMinPoint(), dimBlock.getMaxPoint());
            double xMaxGraph = translate.translateMaxX(dimBlock.getMinPoint(), dimBlock.getMaxPoint());

            // Pour chaque point de la zone, on calcul le d'intersection de la zone d'influence de 2 points consécutifs dans notre beachLine pour mettre à jour les intervalles
            for (int i = 0; i < zones.size() - 1; i++) {
                ArrayList<Point> intersections = IntersectionManager.getIntersections(sweepLine, zones.get(i), zones.get(i + 1));
                Point intersection = IntersectionManager.selectMainPoint(zones.get(i), zones.get(i + 1), intersections);

                // Si on a trouvé une solution, on met à jour les valeurs
                if (intersection != null) {
                    double xIntersection = intersection.getX();

                    // Modification de l'intersection
                    ranges.get(i).setEndRange(xIntersection);
                    ranges.get(i+1).setStartRange(xIntersection);
                }
            }

            // Maintenant que les données sont à jour, on supprime les zones de la page qui ont été recouvertes
            for (int i = ranges.size() - 1; i >= 0; i--) {
                double xStartRange = ranges.get(i).getStartRange();
                double xEndRange = ranges.get(i).getEndRange();

                if (xStartRange > xEndRange) {
                    zones.remove(i);
                    ranges.remove(i);
                }
            }

            // Réinitialiser le début et la fin de la BeachLine pour qu'elle corresponde aux dimensions du graphe
            ranges.get(0).setStartRange(xMinGraph);
            ranges.get(ranges.size() - 1).setEndRange(xMaxGraph);
        }
        else if(ranges.size() == 1) {

            // Initialisation des données (on récupère les dimensions du graphe)
            double xMinGraph = translate.translateMinX(dimBlock.getMinPoint(), dimBlock.getMaxPoint());
            double xMaxGraph = translate.translateMaxX(dimBlock.getMinPoint(), dimBlock.getMaxPoint());

            ranges.get(0).setStartRange(xMinGraph);
            ranges.get(0).setEndRange(xMaxGraph);
        }
    }


    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "BeachLine [zones=" + zones + ", ranges=" + ranges + "]";
    }
}