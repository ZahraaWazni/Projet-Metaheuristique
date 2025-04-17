package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi.display.DisplayGraphAWT;

import javax.sound.sampled.Line;
import java.util.ArrayList;

/**
 * class Voronoi
 * Cette classe est la classe principale qui permet de générer un diagramme de Voronoi
 */
public class Voronoi {

    // Variables

    // Informations du diagramme
    private ArrayList<Point> pilePoints = new ArrayList<>();
    private ArrayList<Neighbour> neighbours = new ArrayList<>();
    private ArrayList<Circle> circles = new ArrayList<>();
    private ArrayList<Circle> trueCricles = new ArrayList<>();
    private boolean rotate = false;

    // Informations finales du diagramme (après retournement des données)


    // Fonctions principales de l'algorithme

    /**
     * 1. Initialisation
     * Transformation des données en objet Point et détermination des dimensions du futur graphe
     * @return Renvoie vrai si l'étape, c'est bien passé
     */
    public boolean init(double[][] data) {
        System.out.println("\nInitialisation du diagramme de Voronoi");

        // Cette condition permet de ne pas bloquer le programme en cas d'échec
        try {

            // Initialisation des variables
            double minX = Double.MAX_VALUE;
            double maxX = -Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxY = -Double.MAX_VALUE;
            ArrayList<Point> pile = new ArrayList<>();

            // On cherche les dimensions du graphe
            System.out.println("Liste des points du graphe");
            for (double[] dataPoint : data) {
                double x = dataPoint[0];
                double y = dataPoint[1];
//                System.out.println("x: " + x + ", y: " + y);

                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }

            // On inverse l'axe des X et des Y si le graphe est plus grand sur l'axe des X
            rotate = (maxX - minX) > (maxY - minY);
            System.out.println("\nInformations complémentaires");
            System.out.println("minX: " + minX + ", maxX: " + maxX + ", minY: " + minY + ", maxY: " + maxY);
            System.out.println("rotate: " + rotate);

            if (rotate) {
                double tmpMinX = minX;
                double tmpMaxX = maxX;
                double tmpMinY = minY;
                double tmpMaxY = maxY;

                minX = tmpMinY;
                maxX = tmpMaxY;
                minY = tmpMinX;
                maxY = tmpMaxX;

                System.out.println("Mise à jour des dimensions - minX: " + minX + ", maxX: " + maxX + ", minY: " + minY + ", maxY: " + maxY);
            }

            // On transforme les données sous la forme d'une liste de voisins
            System.out.println("\nGénération des voisins du graphe");
            for (int i = 0; i < data.length; i++) {
                double x = data[i][0];
                double y = data[i][1];
                String id = Integer.toString(i);

                Neighbour neighbour = (rotate ? new Neighbour(new Point(y, x, id)) : new Neighbour(new Point(x, y, id)));
//                System.out.println("neighbour: " + neighbour);
                pile.add(neighbour);
                neighbours.add(neighbour);
            }

            // On sauvegarde les dimensions du graphe
            DataGraph dataGraph = DataGraph.getInstance();
            dataGraph.setMinPoint(new Point(minX, minY));
            dataGraph.setMaxPoint(new Point(maxX, maxY));

            // On trie les points de la pilePoint dans l'ordre décroissant des coordonnées en Y
            pilePoints = PileManager.sortPoints(pile);

            // Liste des points triés
            System.out.println("\nListe des points dans la pile - les points sont triès par ordre décroissant sur l'axe Y");
//            for (int i = 0; i < pilePoints.size(); i++) {
//                System.out.println(pilePoints.get(i).toString());
//            }

            System.out.println("Initialisation du diagramme de Voronoi ... SUCCESS");
            return true;
        }
        catch (Exception e) {
            System.err.println("Une erreur s'est produite lors de l'initialisation du diagramme de Voronoi : " + e.getMessage());
            System.err.println("Initialisation du diagramme de Voronoi ... FAILED");
            return false;
        }
    }


    /**
     * 2. Algorithme
     * On récupère la liste des points et en fonction de leur nature, on effectue le traitement suivant :
     * - Si c'est un point de notre graphe, on l'ajoute à notre BeachLine, on la met à jour et on vérifie s'il est possible de définir de nouveaux cercles (les nouveaux cercles étant à ajouter à notre liste de points)
     * - Si c'est un cercle et qu'il est encore "d'actualité", on ajoute l'intersection à notre liste et comme pour l'autre, on met à jour la BeachLine et on fait attention à l'apparition de nouveaux cercles
     */
    public boolean process() throws Exception {
        System.out.println("\nGénération du diagramme de voronoi");

        // Cette condition permet de ne pas bloquer le programme en cas d'échec
        try {

            /**
             * Variable qui permet de boucler sur l'algorithme
             * L'algorithme doit être en itératif pour pallier aux problèmes de limite de boucle récursif
             */
            boolean continueProcessing = true;
            int itr = 0;

            // L'algorithme continue tant qu'il y a des éléments dans la pile
            while (continueProcessing) {

                // Initialisation des variables
                BeachLine beachLine = BeachLine.getInstance();
                SweepLine sweepLine = SweepLine.getInstance();

                Point elt = pilePoints.get(0);
                pilePoints.remove(0);
//                System.out.println("\nItération : " + itr);
//                System.out.println("Traitement du point : " + elt.toString());
//                System.out.println("BeachLine : " + beachLine.getZones());

                // 1. Si l'élément est un point
                if (elt instanceof Neighbour) {
                    Neighbour neighbour = (Neighbour) elt;

                    // 1.1. Déplacement de la SweepLine
                    sweepLine.setPosition(neighbour);

                    // 1.2. Ajout du point sur la plage
                    beachLine.addPointOnTheBeach(neighbour);
                }


                // 2. Si l'élément est un cercle
                else if (elt instanceof Circle) {
                    Circle circle = (Circle) elt;

                    // 2.1. Déplacement de la SweepLine
                    sweepLine.setPosition((circle).getTopCircle());

                    // 2.2. Vérifier si le cercle est toujours d'actualité
                    if (beachLine.checkValidCircle(circle)) {

                        // 2.3. Sauvegarde de l'intersection
                        trueCricles.add(circle);

                        // 2.4. Sauvegarder les voisins du cercle
                        ArrayList<Point> circlePoints = circle.getPoints();
                        for (int i = 0; i < circlePoints.size(); i++) {
                            for (int j = 0; j < circlePoints.size(); j++) {
                                if (i != j) {
                                    neighbours.get(Integer.parseInt(circlePoints.get(i).getId())).addNeighbour(neighbours.get(Integer.parseInt(circlePoints.get(j).getId())));
                                }
                            }
                        }
                    }
                }
//                System.out.println("BeachLine 2 : " + beachLine.getZones());


                // 3. Mise à jour de la BeachLine
                beachLine.updateBeach();

                // 4. Vérifier si de nouvelles liaisons sont apparues
                VoronoiUtils.searchAndSaveNewCircles(pilePoints, circles);

                // 5. Sauvegarde des voisins
//                ArrayList<Point> zones = beachLine.getZones();
//                for (int i = 0; i < zones.size(); i++) {
//                    ArrayList<Point> points = VoronoiUtils.getNextPoints(zones, i, 2);
//
//                    if (points.size() == 2) {
//                        neighbours.get(Integer.parseInt(points.get(0).getId())).addNeighbour(neighbours.get(Integer.parseInt(points.get(1).getId())));
//                        neighbours.get(Integer.parseInt(points.get(1).getId())).addNeighbour(neighbours.get(Integer.parseInt(points.get(0).getId())));
//                    }
//                }

                /**
                 * 6. Relancer l'algorithme
                 * On arrête l'algorithme si la pile n'a plus de point
                 */
                if (pilePoints.isEmpty()) {
                    continueProcessing = false;
                }

//                System.out.println("sweepline y : " + sweepLine.getPosition().getY());

//                System.out.println("BeachLine 3 : " + beachLine.getZones());

//                ArrayList<Neighbour> cpNeighbours = new ArrayList<>();
//                for (Neighbour neighbour : neighbours) {
//                    Neighbour cpNeighbour = new Neighbour(neighbour);
//                    for (Neighbour otherNeighbour : neighbour.getNeighbours()) {
//                        cpNeighbour.addNeighbour(otherNeighbour);
//                    }
//                    cpNeighbours.add(cpNeighbour);
//                }
//                ArrayList<Circle> cpCircle = new ArrayList<>(circles);
//
//                DisplayGraphAWT.showGraph(cpNeighbours, cpCircle, String.valueOf(itr));
                itr += 1;
            }

            // TODO : Reverse result

            System.out.println("Génération du diagramme de voronoi ... SUCCESS");
            return true;
        }
        catch (Exception e) {
            throw e;

//            System.err.println("Une erreur est survenue lors de la génération du diagramme de voronoi : " + e.getMessage());
//            System.err.println("Génération du diagramme de voronoi ... FAILED");
//            return false;
        }
    }


    /**
     * 3. Affichage du résultat sur un onglet à part
     */
    public boolean display() throws Exception {
        System.out.println("\nAffichage du diagramme de voronoi");

        try {
            DisplayGraphAWT.showGraph(neighbours, trueCricles, "Diagramme de voronoi");

            System.out.println("Affichage du diagramme de voronoi ... SUCCESS");
            return true;
        }
        catch (Exception e) {
            throw e;
//            System.err.println("Une erreur est survenue lors de l'affichage du diagramme de voronoi : " + e.getMessage());
//            System.err.println("Affichage du diagramme de voronoi ... FAILED");
//            return false;
        }
    }
}
