package tsp.projects.competitor.laCrepeVoyageuse.voronoi2;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.display.DisplayGraphAWT;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune.BlockGraph;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune.DimBlock;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.GraphPoint;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.personalType.TypeDirection;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.utils.PileManager;

import java.util.ArrayList;

/**
 * class Voronoi
 * Cette classe est la classe principale qui permet de générer un diagramme de Voronoi
 */
public class Voronoi {

    // Variables

    // Variable sur la répartition des points en bloc
    private final int nbPointByBlock = 2;
    private int nbZone;
    private final double upGraphe = 0.05;

    // Informations du graphe
    private ArrayList<ArrayList<ArrayList<Point>>> blocks = new ArrayList<>();
    private ArrayList<GraphPoint> graphPoints = new ArrayList<>();
    private DimBlock dimGraph;
    private double xBlockSize;
    private double yBlockSize;
    private BlockGraph lastBlockGraph;


    // Fonctions principales de l'algorithme

    /**
     * 1. Initialisation
     * Transformation des données en objet Point et détermination des dimensions du futur graphe
     * @return Renvoie vrai si l'étape s'est bien passée
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

                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }

            // Aggrandissement du graphe
            minX = minX - (maxX - minX) * upGraphe;
            maxX = maxX + (maxX - minX) * upGraphe;
            minY = minY - (maxY - minY) * upGraphe;
            maxY = maxY + (maxY - minY) * upGraphe;


            // On sauvegarde les dimensions du graphe
            System.out.println("Dimensions : minX = " + minX + ", maxX = " + maxX + ", minY = " + minY + ", maxY = " + maxY);
            dimGraph = new DimBlock(new Point(minX, minY), new Point(maxX, maxY));


            // On détermine le découpage des blocs
            nbZone = (int) Math.ceil(data.length / (double) nbPointByBlock);

            // On incrémente s'il reste un reste à la division de data.length par nbPointByBlock
            if ((nbZone * nbPointByBlock) < data.length) {
                nbZone += 1;
            }

            xBlockSize = (maxX - minX) / nbZone;
            yBlockSize = (maxY - minY) / nbZone;

            System.out.println("Nbzone = " +  nbZone);
            System.out.println("Block size X = " + xBlockSize + ", block size Y = " + yBlockSize);

            // Initialisation de la variable block
            for (int i = 0; i < nbZone; i++) {
                blocks.add(new ArrayList<>());

                for (int j = 0; j < nbZone; j++) {
                    blocks.get(i).add(new ArrayList<>());
                }
            }


            // On transforme les données sous la forme d'une liste de voisins
            System.out.println("\nGénération des voisins du graphe");
            for (int i = 0; i < data.length; i++) {
                double x = data[i][0];
                double y = data[i][1];
                String id = Integer.toString(i);

                GraphPoint graphPoint = new GraphPoint(new Point(x, y, id));
                pile.add(graphPoint);
                graphPoints.add(graphPoint);
            }

            // On trie les points de la pilePoint dans l'ordre décroissant des coordonnées en Y
            ArrayList<Point> pilePoints = PileManager.sortPoints(pile);

            // On répartie les points dans les blocs
            for (Point point : pilePoints) {
                int indexX = (int) ((point.getX() - minX) / xBlockSize);
                int indexY = (int) ((point.getY() - minY) / yBlockSize);

                if (blocks.get(indexY) == null) blocks.set(indexY, new ArrayList<>());
                if (blocks.get(indexY).get(indexX) == null) blocks.get(indexY).set(indexX, new ArrayList<>());

                System.out.println("Block " + indexY + " " + indexX + " : " + point);
                blocks.get(indexY).get(indexX).add(point);
            }

            System.out.println("Initialisation du diagramme de Voronoi ... SUCCESS");
            return true;
        }
        catch (Exception e) {
            throw e;

//            System.err.println("Une erreur s'est produite lors de l'initialisation du diagramme de Voronoi : " + e.getMessage());
//            System.err.println("Initialisation du diagramme de Voronoi ... FAILED");
//            return false;
        }
    }



    /**
     * 2. Algorithme
     * Dans un premier, on crée des blocs où on applique l'algorithme de fortune dans chacun d'entre eux
     * Ensuite, on fusionne les blocs entre eux jusqu'à ce qu'il ne reste qu'un seul gros bloc
     * Enfin, on vérifie les derniers cercles restants et on renvoie la solution
     * @return Renvoie vrai si l'étape s'est bien passée
     */
    public boolean process() throws Exception {
        System.out.println("\nGénération du diagramme de voronoi");

        // Cette condition permet de ne pas bloquer le programme en cas d'échec
        try {

            // Traitement des blocs
            System.out.println("\nGénération des blocs");
            ArrayList<ArrayList<BlockGraph>> blockGenerate = new ArrayList<>();
            double minPointX = dimGraph.getMinPoint().getX();
            double minPointY = dimGraph.getMinPoint().getY();

            for (int i = 0; i < nbZone; i++) {
                blockGenerate.add(new ArrayList<>());

                for (int j = 0; j < nbZone; j++) {
                    BlockGraph blockGraph;

                    Point minPoint = new Point(j * xBlockSize + minPointX, i * yBlockSize + minPointY);
                    Point maxPoint = new Point((j+1) * xBlockSize + minPointX, (i+1) * yBlockSize + minPointY);
                    DimBlock dimBlock = new DimBlock(minPoint, maxPoint);

                    if (blocks.get(i) == null) {
                        blockGraph = new BlockGraph(new ArrayList<>(), dimBlock);
                    }
                    else if (blocks.get(i).get(j) == null) {
                        blockGraph = new BlockGraph(new ArrayList<>(), dimBlock);
                    }
                    else {
                        blockGraph = new BlockGraph(blocks.get(i).get(j), dimBlock);
                    }

                    blockGenerate.get(i).add(blockGraph);
                    System.out.println("\nAchevé : " + (i*nbZone+j+1) + " sur " + (nbZone*nbZone));
                    System.out.println(blockGraph.getNeutralBeachLine());
                    System.out.println(blockGraph.getReverseBeachLine());
                    System.out.println(blockGraph.getRightBeachLine());
                    System.out.println(blockGraph.getLeftBeachLine());

                    System.out.println(blockGraph.getNeutralSweepLine());
                    System.out.println(blockGraph.getReverseSweepLine());
                    System.out.println(blockGraph.getRightSweepLine());
                    System.out.println(blockGraph.getLeftSweepLine());

                    System.out.println(blockGraph.getNeutralPilePoints());
                    System.out.println(blockGraph.getRightPilePoints());
                }
            }

            // Fusion des blocs
            System.out.println("\nFusion des blocs");
            ArrayList<ArrayList<BlockGraph>> merge1 = blockGenerate;
            ArrayList<ArrayList<BlockGraph>> merge2 = new ArrayList<>();

            while (merge1.size() != 1 && merge1.get(0).size() != 1) {
                merge2.clear();
                for (int i = 0; i < merge1.size(); i++) {
                    merge2.add(new ArrayList<>());

                    for (int j = 0; j < ((int) Math.ceil(merge1.get(i).size() / 2.)); j++) {
                        if ((j + 1) < merge1.get(i).size()) {
                            merge2.get(i).add(new BlockGraph(merge1.get(i).get(j), merge1.get(i).get(j + 1), TypeDirection.LEFT));
                        }
                        else {
                            merge2.get(i).add(merge1.get(i).get(j));
                        }
                    }
                }
                System.out.println("Fusion de " + (merge1.size() * merge1.get(0).size()) + " blocs - left");

                merge1.clear();
                for (int i = 0; i < ((int) Math.ceil(merge2.size() / 2.)); i++) {
                    merge1.add(new ArrayList<>());

                    for (int j = 0; j < merge2.get(i).size(); j++) {
                        if ((i + 1) < merge2.size()) {
                            merge1.get(i).add(new BlockGraph(merge2.get(i).get(j), merge2.get(i+1).get(j), TypeDirection.BOTTOM));
                        }
                        else {
                            merge1.get(i).add(merge2.get(i).get(j));
                        }
                    }
                }
                System.out.println("Fusion de " + (merge2.get(0).size() * merge2.size()) + " blocs - bottom");
            }


            // Calcul des derniers cercles
            System.out.println("\nCalcul des derniers cercles");
            lastBlockGraph = merge1.get(0).get(0);
            lastBlockGraph.calculateLastCircles();

            // Création des liens entre les points
            for (Circle circle : lastBlockGraph.getTrueCircles()) {
                ArrayList<Point> points = circle.getPoints();

                for (int i = 0; i < points.size(); i++) {
                    for (int j = 0; j < points.size(); j++) {
                        if (i != j) {
                            graphPoints.get(Integer.parseInt(points.get(i).getId())).addNeighbour(graphPoints.get(Integer.parseInt(points.get(j).getId())));
                        }
                    }
                }
            }

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
            DisplayGraphAWT.showGraph(graphPoints, lastBlockGraph.getTrueCircles(), "Diagramme de voronoi");

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
