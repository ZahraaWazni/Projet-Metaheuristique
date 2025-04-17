package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class PileManager
 * Cette classe est un singleton et gère la pile de point qui est utilisée par l'algorithme
 */
public class PileManager {

    // Trie par interclassement

    /**
     * Trie les points en fonction de leurs coordonnées (Y puis X)
     * Pour cela, on utilise un trie par interclassement
     * Le principe de ce trie est de diviser les données en des plus petits ensembles, puis de les fusionner afin de les trier au faire et à mesure
     * @param points Tableau de points à trier
     * @return Tableau de points trié
     */
    public static ArrayList<Point> sortPoints(ArrayList<Point> points) {
        return PileManager.spliSort(points);
    }

    /**
     * Trie par interclassement
     * 1. Division des données
     * @param points Tableau de points à trier
     * @return Tableau de points trié
     */
    private static ArrayList<Point> spliSort(ArrayList<Point> points) {

        // Si la liste ne contient qu'un seul élément, on l'a renvoie tel quel
        if (points.size() <= 1) {
            return points;
        }

        // Sinon, on divise la liste en deux et on répète l'opération jusqu'à ce qu'il ne reste qu'un seul élément
        else {
            int mid = points.size() / 2;
            ArrayList<Point> left = spliSort(new ArrayList<>(points.subList(0, mid)));
            ArrayList<Point> right = spliSort(new ArrayList<>(points.subList(mid, points.size())));

            return PileManager.mergeSort(left, right);
        }
    }

    /**
     * Trie par interclassement
     * 2. Fusion des ensembles
     * @param left Partie gauche d'un tableau trié
     * @param right Partie droite d'un tableau trié
     * @return Tableau de points trié
     */
    private static ArrayList<Point> mergeSort(ArrayList<Point> left, ArrayList<Point> right) {

        // Fusion des 2 listes de points
        ArrayList<Point> sortedList = new ArrayList<>();
        int i = 0, j = 0;

        /**
         * On boucle sur les listes tant qu'il reste des éléments dans au moins une des listes
         * Chaque liste a déjà été triée par le programme. Il suffit donc de parcourir les éléments dans l'ordre et de sélectionner le plus petit à chaque fois entre les 2 listes de points
         */
        while (i < left.size() && j < right.size()) {

            // Coordonées des points
            double xLeft = left.get(i).getX();
            double yLeft = left.get(i).getY();
            double xRight = right.get(j).getX();
            double yRight = right.get(j).getY();

            /**
             * Pour la comparaison, on met en avant le trie sur l'axe des Y
             * S'il y a une égalité, on départage avec l'axe des X
             * Le trie ce fait de manière décroissante sur l'axe des Y et croissante sur l'axe des X
             */
            if (yLeft > yRight) {
                sortedList.add(left.get(i));
                i = i + 1;
            }
            else if (yLeft == yRight && xLeft < xRight) {
                sortedList.add(left.get(i));
                i = i + 1;
            }
            else {
                sortedList.add(right.get(j));
                j = j + 1;
            }
        }

        // S'il reste des éléments dans une des listes, on les concatène à la liste triée
        sortedList.addAll(left.subList(i, left.size()));
        sortedList.addAll(right.subList(j, right.size()));

        return sortedList;
    }


    // Recherche d'index

    /**
     * Recherche d'un index dans la pile de points
     * Cet index correspond à la position du cercle à ajouter dans la pile
     */
    public static int getAddIndexInPile(Point point, ArrayList<Point> pile) {

        // Initialisation des variables
        int index = 0;
        boolean found = false;
        double xPoint = point.getX();
        double yPoint = point.getY();

        // Boucle while qui parcourt tous les éléments de la liste (lorsqu'il y a trop de points, une récursive n'est plus possible)
        while (!found && index < pile.size()) {
            double xPile = pile.get(index).getX();
            double yPile = pile.get(index).getY();

            if (pile.get(index) instanceof Circle) {
                xPile = ((Circle) pile.get(index)).getTopCircle().getX();
                yPile = ((Circle) pile.get(index)).getTopCircle().getY();
            }

            if (yPoint > yPile) {
                found = true;
            }
            else if (yPoint == yPile && xPoint < xPile) {
                found = true;
            }
            else if (yPoint == yPile && xPoint == xPile && pile.get(index) instanceof Neighbour) {
                found = true;
            }

            if (!found) {
                index += 1;
            }
        }

        // Si la position a été trouvée, on l'a renvoie
        if (found) {
            return index;
        }

        // Si le cercle doit être ajouté à la fin du tableau, on renvoie -1
        return -1;
    }
}
