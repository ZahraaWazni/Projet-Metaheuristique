package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * class VoronoiUtils
 * Cette classe stockent les fonctions annexes utilisées par le diagramme de Voronoi
 */
public class VoronoiUtils {

    // Variables

    // Cette variable permet d'indiquer si une donnée est sautable ou non
    private static double skipRange = 5 * Math.pow(10, -13);


    // Recherche et sauvegarde de nouveaux cercles

    /**
     * Rechercher les nouveaux cercles à partir des zones
     * Un cercle est composé de 3 points consécutifs sur la BeachLine
     * On estime que des points trop éloignés ne peuvent pas se croiser sur le diagramme
     */
    public static void searchAndSaveNewCircles(ArrayList<Point> pilePoints, ArrayList<Circle> circles) throws Exception {

        // Initialisation des données
        BeachLine beachLine = BeachLine.getInstance();
        SweepLine sweepLine = SweepLine.getInstance();
        ArrayList<Point> zones = beachLine.getZones();

        // Pour créer un cercle circonscrit, il faut au minimum 3 points
        if (zones.size() >= 3) {

            /**
             * Pour chaque triplet (3 points sucessifs sur la BeachLine), on crée un nouveau cercle
             * Si le cercle n'a pas encore été généré, alors on l'instancie
             * Un cercle est identifiable par un id correspond aux 3 sommets qui le composent
             */

            // Génération de tous les nouveaux cercles
            for (int i = 0; i < zones.size() - 2; i++) {
                ArrayList<Point> nextPoints = VoronoiUtils.getNextPoints(zones, i, 3);

                // Si on a pu sélectionner 3 points depuis la BeachLine, alors on crée un nouveau identifiant
                if (nextPoints.size() == 3) {
                    String newCircleId = Circle.generateId(nextPoints.get(0), nextPoints.get(1), nextPoints.get(2));

                    // Si le cercle n'a pas déjà été généré auparavant, alors on crée le cercle
                    if (VoronoiUtils.checkCircleDoesntExist(newCircleId, circles)) {

                        // Si une erreur survient lors de la création du cercle, alors on l'abandonne
                        try {
                            Circle circle = new Circle(nextPoints);

                            // Filter les cercles où le point le plus haut ne dépasse pas la SweepLine
                            double maxCircleY = circle.getTopCircle().getY();
                            double sweepY = sweepLine.getPosition().getY();

                            if (maxCircleY <= sweepY) {

                                // Ajout du cercle dans la pile de point à traiter et dans la liste de cercles traitée
                                int index = PileManager.getAddIndexInPile(circle.getTopCircle(), pilePoints);

//                                System.out.println(" --- param --- ");
//                                System.out.println(circle.getTopCircle());
//                                System.out.println(pilePoints);
//                                System.out.println(index);

                                circles.add(circle);

                                if (index == -1) {
                                    pilePoints.add(circle);
                                }
                                else {
                                    pilePoints.add(index, circle);
                                }
                            }
                        }
                        catch (Exception e) {
                            throw new Exception("Une erreur est survenue lors de la création du cercle : " + e.getMessage());
                        }
                    }
                }
            }
        }
    }


    // Récupère une liste de points

    /**
     * Renvoie les X prochains points à partir d'un index, d'une suite de point
     * S'il n'est pas possible de renvoyer le bon nombre de points, alors on renvoie un tableau vide
     * @param zones liste des points
     * @param index Index de départ dans la liste
     * @param nbPoint Nombre de points à sélectionner
     * @return Liste des points sélectionnés
     */
    public static ArrayList<Point> getNextPoints(ArrayList<Point> zones, int index, int nbPoint) {
        return VoronoiUtils.recGetNextPoints(zones, index, nbPoint, new ArrayList<>());
    }

    /**
     * Récursive pour sélectionner les points
     * @param zones liste des points
     * @param index Index de départ dans la liste
     * @param nbPoint Nombre de points à sélectionner
     * @param genTab Tableau en cours de génération
     * @return Liste des points sélectionnés
     */
    private static ArrayList<Point> recGetNextPoints(ArrayList<Point> zones, int index, int nbPoint, ArrayList<Point> genTab) {

        // Si la table générée est de bonne taille, alors on renvoie les points
        if (genTab.size() >= nbPoint) {
            return genTab;
        }

        // Si on arrive à la fin de la chaine, alors on estime qu'il n'y a pas de combinaisons possibles
        else if (index >= zones.size()) {
            return new ArrayList<>();
        }

        // Sinon, on passe à l'index suivant
        else {
            Point point = zones.get(index);
            boolean addPoint = true;
            index += 1;

            /**
             * Si le point n'appartient pas encore à la séquence, alors on l'ajoute
             * Enfin, on effectue une nouvelle itération jusqu'à tomber sur une condition d'arrêt
             */
            for (Point p : genTab) {
                if (p.getId().equals(point.getId())) {
                    addPoint = false;
                    break;
                }
            }

            if (addPoint) {
                genTab.add(point);
            }

            return recGetNextPoints(zones, index, nbPoint, genTab);
        }
    }


    // Comparer des cercles

    /**
     * Comparer le nouveau cercle aux anciens
     * L'objectif est d'éviter de recréer un cercle déjà créer
     * @param newId Identifiant du cercle à tester
     * @param circles Liste de cercles déjà générée
     * @return Résultat de la vérification
     */
    private static boolean checkCircleDoesntExist(String newId, ArrayList<Circle> circles) {
        return VoronoiUtils.recCheckCircleDoesntExist(newId, circles, 0);
    }

    /**
     * Récursive qui permet vérifier si un cercle n'existe pas déjà
     * @param newId Identifiant du cercle à tester
     * @param circles Liste de cercles déjà générée
     * @param index Index du traitement
     * @return Résultat de la vérification
     */
    private static boolean recCheckCircleDoesntExist(String newId, ArrayList<Circle> circles, int index) {

        // Si on arrive au bout de la recherche, alors l'id est unique
        if (index >= circles.size()) {
            return true;
        }

        // Si un autre cercle a ou a eu le même identifiant, alors on renvoie faux
        else if (VoronoiUtils.sameId(newId, circles.get(index).getId())) {
            return false;
        }

        // Sinon, on passe à l'index suivant
        else {
            index += 1;
            return VoronoiUtils.recCheckCircleDoesntExist(newId, circles, index);
        }
    }


    // Vérification de l'identifiant

    /**
     * Vérifie si deux index sont les mêmes
     * @param newId Ancienne identifiant
     * @param otherId Autre identifiant
     * @return Résultat de la vérification
     */
    private static boolean sameId(String newId, String otherId) {

        /**
         * Pour vérifier si deux chaines sont similaires, il ne faut pas prendre en compte l'ordre des caractères
         * Pour cela, on retourne et on décalle les caractères pour gérer le plus de cas
         */
        String tmpId = otherId;

        for (int i = 0; i < newId.length(); i++) {
            if (newId.equals(tmpId) || newId.equals(StrUtils.reverse(tmpId))) {
                return true;
            }

            tmpId = StrUtils.shiftRight(tmpId);
        }

        return false;
    }


    // Retourne les coordonées

    /**
     * Inverse les coordonnées x et y des résultats
     * @param points Liste des points à retourner
     * @return Liste de points retournés
     * @param <T> Objet étant dans la classe Point
     */
    public static <T extends Point> ArrayList<T> reversePositionResult(ArrayList<T> points) {
        return points;
    }
}
