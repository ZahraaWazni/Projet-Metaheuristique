package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.fortune;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.utils.PileManager;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.personalType.TypeDirection;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.rotate.*;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.utils.StrUtils;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.utils.VoronoiUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class BlocGraph
 * Ce bloc est une partie du graphe
 * Il contient plusieurs points
 */
public class BlockGraph {

    // Variables

    // Rotation
    Rotate90DegToLeft rotate90DegToLeft = new Rotate90DegToLeft();
    Rotate90DegToRight rotate90DegToRight = new Rotate90DegToRight();
    Rotate180Deg rotate180Deg = new Rotate180Deg();
    Rotate0Deg rotate0Deg = new Rotate0Deg();

    // Informations du bloc
    private DimBlock dimBlock;
    private ArrayList<Point> listPoints = new ArrayList<>();
    private ArrayList<Point> rightPilePoints = new ArrayList<>();
    private ArrayList<Point> neutralPilePoints = new ArrayList<>();
    private ArrayList<Circle> trueCircles = new ArrayList<>();

    private BeachLine neutralBeachLine = new BeachLine();
    private BeachLine reverseBeachLine = new BeachLine();
    private BeachLine leftBeachLine = new BeachLine();
    private BeachLine rightBeachLine = new BeachLine();

    private SweepLine neutralSweepLine = new SweepLine();
    private SweepLine reverseSweepLine = new SweepLine();
    private SweepLine leftSweepLine = new SweepLine();
    private SweepLine rightSweepLine = new SweepLine();


    // Constructeur

    /**
     * Constructeur de la classe à partir d'une liste de points (initialisation
     * @param points Liste des points du bloc
     */
    public BlockGraph(ArrayList<Point> points, DimBlock dimBlock) throws Exception {

        // Sauvegarde des variables
        this.listPoints = points;
        this.dimBlock = dimBlock;

        // Lancement de l'algorithme
        neutralPilePoints = init(neutralBeachLine, neutralSweepLine, rotate0Deg, PileManager.sortPoints(rotate0Deg.translatePoints(points)));
        rightPilePoints = init(rightBeachLine, rightSweepLine, rotate90DegToRight, PileManager.sortPoints(rotate90DegToRight.translatePoints(points)));
        init(reverseBeachLine, reverseSweepLine, rotate180Deg, PileManager.sortPoints(rotate180Deg.translatePoints(points)));
        init(leftBeachLine, leftSweepLine, rotate90DegToLeft, PileManager.sortPoints(rotate90DegToLeft.translatePoints(points)));
    }

    /**
     * Constructeur de la classe en fusionnant deux graphes
     * @param blocGraph1 Bloc 1
     * @param blocGraph2 Bloc 2
     */
    public BlockGraph(BlockGraph blocGraph1, BlockGraph blocGraph2, TypeDirection direction) throws Exception {

        // Lancement de la fusion des deux blocs
        processMerge(blocGraph1, blocGraph2, direction);
    }


    // Getter

    // Getter - BeachLine

    /**
     * Renvoie la Beachline qui monte
     * @return Beachline
     */
    public BeachLine getNeutralBeachLine() {
        return neutralBeachLine;
    }

    /**
     * Renvoie la Beachline qui descend
     * @return Beachline
     */
    public BeachLine getReverseBeachLine() {
        return reverseBeachLine;
    }

    /**
     * Renvoie la Beachline qui va vers la gauche
     * @return Beachline
     */
    public BeachLine getLeftBeachLine() {
        return leftBeachLine;
    }

    /**
     * Renvoie la Beachline qui va vers la droite
     * @return Beachline
     */
    public BeachLine getRightBeachLine() {
        return rightBeachLine;
    }


    // Getter - SweepLine

    /**
     * Renvoie la SweepLine (top)
     * @return SweepLine
     */
    public SweepLine getNeutralSweepLine() {
        return neutralSweepLine;
    }

    /**
     * Renvoie la SweepLine (bottom)
     * @return SweepLine
     */
    public SweepLine getReverseSweepLine() {
        return reverseSweepLine;
    }

    /**
     * Renvoie la SweepLine (left)
     * @return SweepLine
     */
    public SweepLine getLeftSweepLine() {
        return leftSweepLine;
    }

    /**
     * Renvoie la SweepLine (right)
     * @return SweepLine
     */
    public SweepLine getRightSweepLine() {
        return rightSweepLine;
    }


    // Getter - PilePoints

    /**
     * Renvoie la liste des points restants dans la pile (bottom)
     * @return Liste de points
     */
    public ArrayList<Point> getNeutralPilePoints() {
        return neutralPilePoints;
    }

    /**
     * Renvoie la liste des points restants dans la pile (droite)
     * @return Liste de points
     */
    public ArrayList<Point> getRightPilePoints() {
        return rightPilePoints;
    }


    // Getter - Circles

    /**
     * Renvoie la liste des cercles
     * @return Liste des cercles
     */
    public ArrayList<Circle> getTrueCircles() {
        return trueCircles;
    }


    // Getter - DimBloc

    /**
     * Renvoie les dimensions du bloc
     * @return Dimensions du bloc
     */
    public DimBlock getDimBlock() {
        return dimBlock;
    }


    // Getter - DimBloc

    /**
     * Renvoie la liste des points du bloc
     * @return Liste des points
     */
    public ArrayList<Point> getListPoints() {
        return listPoints;
    }


    // Algorithme

    // Algorithme - Initialisation

    /**
     * Utilisation de l'algorithme de fortune pour trouver les liens entre les points du bloc
     * @param beachLine Beachline
     * @param sweepLine SweepLine
     * @param translate Translate
     * @param pilePoints Liste de points triée
     * @return Renvoie les points non traités
     * @throws Exception
     */
    private ArrayList<Point> init(BeachLine beachLine, SweepLine sweepLine, AbstractRotate translate, ArrayList<Point> pilePoints) throws Exception {
        if (pilePoints.isEmpty()) {
            return pilePoints;
        }
        else {
            return init(beachLine, sweepLine, translate, pilePoints, new ArrayList<>());
        }
    }


    /**
     * Recursive du process
     * @param beachLine Beachline
     * @param sweepLine SweepLine
     * @param translate Translate
     * @param pilePoints Liste de points triée
     * @param circles Liste de cercles
     * @return Renvoie les points non traités
     * @throws Exception
     */
    private ArrayList<Point> init(BeachLine beachLine, SweepLine sweepLine, AbstractRotate translate, ArrayList<Point> pilePoints, ArrayList<String> circles) throws Exception {

        // Initialisation des variables
        Point elt = pilePoints.get(0);
        pilePoints.remove(0);


        // 1. Si l'élément est un cercle
        if (elt instanceof Circle) {
            Circle circle = (Circle) elt;

            // 1.1. Déplacement de la SweepLine
            sweepLine.setPosition((circle).getTopCircle());

            // 1.2. Vérifier si le cercle est toujours d'actualité
            if (beachLine.checkValidCircle(circle)) {

                // 1.3. Sauvegarde de l'intersection
                trueCircles.add(circle);
            }
        }

        // 2. Si l'élément est un point
        else {

            // 2.1. Déplacement de la SweepLine
            sweepLine.setPosition(elt);

            // 2.2. Ajout du point sur la plage
            beachLine.addPointOnTheBeach(elt);
        }


        // 3. Mise à jour de la BeachLine
        beachLine.updateBeach(translate, sweepLine, dimBlock);

        // 4. Vérifier si de nouvelles liaisons sont apparues
        VoronoiUtils.searchAndSaveNewCircles(beachLine, sweepLine, pilePoints, circles);

        // 5. Relancer l'algorithme
        if (!pilePoints.isEmpty()) {
            return init(beachLine, sweepLine, translate, pilePoints, circles);
        }
        else {
            return pilePoints;
        }
    }


    // Algorithme - Fusion

    /**
     * Fusionne deux blocs pour sauvegarder le nouveau contenu dans le bloc courant
     * @param blocGraph1 Bloc 1
     * @param blocGraph2 Bloc 2
     */
    private void processMerge(BlockGraph blocGraph1, BlockGraph blocGraph2, TypeDirection direction) throws Exception {

        // Sauvegarde des données pour le nouveau bloc

        // Liste de points
        listPoints = new ArrayList<>();
        listPoints.addAll(blocGraph1.getListPoints());
        listPoints.addAll(blocGraph2.getListPoints());

        // Dimensions
        dimBlock = new DimBlock(blocGraph1.getDimBlock().getMinPoint(), blocGraph2.getDimBlock().getMaxPoint());

        // True circles
        trueCircles = new ArrayList<>();
        trueCircles.addAll(blocGraph1.getTrueCircles());
        trueCircles.addAll(blocGraph2.getTrueCircles());

        // Set default sweepLine
        neutralSweepLine.setDefaultPositionWithDimensions(rotate0Deg, dimBlock);
        reverseSweepLine.setDefaultPositionWithDimensions(rotate180Deg, dimBlock);
        leftSweepLine.setDefaultPositionWithDimensions(rotate90DegToLeft, dimBlock);
        rightSweepLine.setDefaultPositionWithDimensions(rotate90DegToRight, dimBlock);


        // Fusion des beachLines

        // Génération de la liste de points entre les beachlines
        BeachLine mergeBeachLine1 = new BeachLine();
        SweepLine mergeSweepLine1;
        BeachLine mergeBeachLine2 = new BeachLine();
        SweepLine mergeSweepLine2;
        ArrayList<Point> zones1;
        ArrayList<Point> zones2;
        ArrayList<Point> zones3 = new ArrayList<>();
        ArrayList<Point> zones4 = new ArrayList<>();
        AbstractRotate rotate;

        if (direction == TypeDirection.LEFT) {
            mergeSweepLine1 = new SweepLine();
            mergeSweepLine2 = new SweepLine();

            zones1 = rotate90DegToLeft.translatePoints(blocGraph1.getRightBeachLine().getZones());
            zones2 = rotate90DegToRight.translatePoints(blocGraph2.getLeftBeachLine().getZones());
            zones3.addAll(blocGraph1.getNeutralBeachLine().getZones());
            zones3.addAll(blocGraph2.getNeutralBeachLine().getZones());
            zones4.addAll(blocGraph1.getReverseBeachLine().getZones());
            zones4.addAll(blocGraph2.getReverseBeachLine().getZones());
            rotate = rotate0Deg;
        }
        else {
            mergeSweepLine1 = new SweepLine();
            mergeSweepLine2 = new SweepLine();

            zones1 = rotate180Deg.translatePoints(blocGraph1.getRightBeachLine().getZones());
            zones2 = blocGraph2.getLeftBeachLine().getZones();
            zones3.addAll(blocGraph1.getRightBeachLine().getZones());
            zones3.addAll(blocGraph2.getRightBeachLine().getZones());
            zones4.addAll(blocGraph1.getLeftBeachLine().getZones());
            zones4.addAll(blocGraph2.getLeftBeachLine().getZones());
            rotate = rotate90DegToLeft;
        }

        System.out.println("\n --- before --- ");

        System.out.println(mergeBeachLine1);
        System.out.println(mergeBeachLine2);
        System.out.println(mergeSweepLine1);
        System.out.println(mergeSweepLine2);
        System.out.println("\n");

        System.out.println(zones1);
        System.out.println(zones2);
        System.out.println(zones3);
        System.out.println(zones4);
        System.out.println("\n");


        // Création de la liste de point
        ArrayList<Point> points = PileManager.genPilePointWithZones(rotate, zones1, zones2, zones3);
        ArrayList<Point> points2 = PileManager.genPilePointWithZones(rotate, zones1, zones2, zones4);
        ArrayList<Point> reverse = new ArrayList<>();
        for (int i = (points2.size() - 1); i >= 0; i--) {
            reverse.add(points2.get(i));
        }
        System.out.println("points: " + points);
        System.out.println("reverse: " + rotate180Deg.translatePoints(reverse));


        // Remplissage du entre-deux blocs
        if (!points.isEmpty()) {
            fillBeachLine(mergeBeachLine1, mergeSweepLine1, rotate0Deg, points);
            fillBeachLine(mergeBeachLine2, mergeSweepLine2, rotate180Deg, rotate180Deg.translatePoints(reverse));
//            VoronoiUtils.searchAndSaveNewCircles(mergeBeachLine1, mergeSweepLine1, pilePoints, circles);

            // SweepLine & BeachLine - Part 1
            if (TypeDirection.LEFT == direction) {
                neutralBeachLine = mergeBeachLine1;
                neutralSweepLine = mergeSweepLine1;
//                neutralBeachLine.updateBeach(rotate0Deg, neutralSweepLine, dimBlock);

                reverseBeachLine = mergeBeachLine2;
                reverseSweepLine = mergeSweepLine2;
//                reverseBeachLine.updateBeach(rotate180Deg, reverseSweepLine, dimBlock);
            }
            else {
                rightBeachLine = mergeBeachLine1;
                rightSweepLine = mergeSweepLine1;
//                rightBeachLine.updateBeach(rotate90DegToRight, rightSweepLine, dimBlock);

                leftBeachLine = mergeBeachLine2;
                leftSweepLine = mergeSweepLine2;
//                leftBeachLine.updateBeach(rotate90DegToLeft, leftSweepLine, dimBlock);
            }
        }


        // Mise à jour de la pile de points

        // NeutralPilePoints && RightPilePoints
        if (TypeDirection.LEFT == direction) {
            neutralPilePoints = blocGraph1.getNeutralPilePoints();
            neutralPilePoints.addAll(blocGraph2.getNeutralPilePoints());
            rightPilePoints = blocGraph2.getRightPilePoints();
        }
        else {
            neutralPilePoints = blocGraph2.getNeutralPilePoints();
            rightPilePoints = blocGraph1.getRightPilePoints();
            rightPilePoints.addAll(blocGraph2.getRightPilePoints());
        }

        System.out.println("\n --- pending --- ");

        System.out.println(getNeutralBeachLine());
        System.out.println(getReverseBeachLine());
        System.out.println(getRightBeachLine());
        System.out.println(getLeftBeachLine());

        System.out.println(getNeutralSweepLine());
        System.out.println(getReverseSweepLine());
        System.out.println(getRightSweepLine());
        System.out.println(getLeftSweepLine());

        System.out.println(getNeutralPilePoints());
        System.out.println(getRightPilePoints());
        System.out.println("\n");

        System.out.println("zones 1 : " + zones1);
        System.out.println("zones 2 : " + zones2);
        System.out.println("\n");


        // Fusionner les deux blocs
        if (direction == TypeDirection.LEFT) {

            System.out.println(rotate90DegToRight.translatePoints(blocGraph2.getListPoints()));
            System.out.println(blocGraph1.getRightPilePoints());
            System.out.println(blocGraph1.getRightBeachLine());
            System.out.println(blocGraph1.getRightSweepLine());

            ArrayList<Point> allPoints1 = new ArrayList<>();
            allPoints1.addAll(rotate90DegToRight.translatePoints(blocGraph2.getListPoints()));
            allPoints1.addAll(blocGraph1.getRightPilePoints());
            mergeBlocks(blocGraph1.getRightBeachLine(), blocGraph1.getRightSweepLine(), rotate90DegToRight, allPoints1);
            rightBeachLine = blocGraph1.getRightBeachLine();
            rightSweepLine = blocGraph1.getRightSweepLine();

            System.out.println("\n");
            System.out.println(rotate90DegToLeft.translatePoints(blocGraph1.getListPoints()));
            System.out.println(blocGraph2.getLeftBeachLine());
            System.out.println(blocGraph2.getLeftSweepLine());

            ArrayList<Point> allPoints2 = rotate90DegToLeft.translatePoints(blocGraph1.getListPoints());
            mergeBlocks(blocGraph2.getLeftBeachLine(), blocGraph2.getLeftSweepLine(), rotate90DegToLeft, allPoints2);
            leftBeachLine = blocGraph2.getLeftBeachLine();
            leftSweepLine = blocGraph2.getLeftSweepLine();
        }
        else {
            ArrayList<Point> allPoints1 = rotate180Deg.translatePoints(blocGraph2.getListPoints());
            mergeBlocks(blocGraph1.getReverseBeachLine(), blocGraph1.getReverseSweepLine(), rotate180Deg, allPoints1);
            reverseBeachLine = blocGraph1.getReverseBeachLine();
            reverseSweepLine = blocGraph1.getReverseSweepLine();

            ArrayList<Point> allPoints2 = new ArrayList<>();
            allPoints2.addAll(rotate0Deg.translatePoints(blocGraph1.getListPoints()));
            allPoints2.addAll(blocGraph2.getNeutralPilePoints());
            mergeBlocks(blocGraph2.getNeutralBeachLine(), blocGraph2.getNeutralSweepLine(), rotate0Deg, allPoints2);
            neutralBeachLine = blocGraph2.getNeutralBeachLine();
            neutralSweepLine = blocGraph2.getNeutralSweepLine();
        }


        System.out.println("\n --- after --- ");

        System.out.println(getNeutralBeachLine());
        System.out.println(getReverseBeachLine());
        System.out.println(getRightBeachLine());
        System.out.println(getLeftBeachLine());

        System.out.println(getNeutralSweepLine());
        System.out.println(getReverseSweepLine());
        System.out.println(getRightSweepLine());
        System.out.println(getLeftSweepLine());

        System.out.println(getNeutralPilePoints());
        System.out.println(getRightPilePoints());
        System.out.println("\n");

        System.out.println("zones 1 : " + zones1);
        System.out.println("zones 2 : " + zones2);
        System.out.println("\n");

    }


    /**
     * Recursive du process
     * @param beachLine Beachline
     * @param sweepLine SweepLine
     * @param rotate Rotation
     * @param pilePoints Liste de points triée
     * @return Renvoie les points non traités
     * @throws Exception
     */
    private void fillBeachLine(BeachLine beachLine, SweepLine sweepLine, AbstractRotate rotate, ArrayList<Point> pilePoints) throws Exception {

        // Initialisation des variables
        Point elt = pilePoints.get(0);
        pilePoints.remove(0);

        // 1. Déplacement de la SweepLine
        sweepLine.setPosition(elt);

        // 2. Ajout du point sur la plage
        beachLine.addPointOnTheBeach(elt);

        // 3. Mise à jour de la BeachLine
        beachLine.updateBeach(rotate, sweepLine, dimBlock);

        // 4. Relancer l'algorithme
        if (!pilePoints.isEmpty()) {
            fillBeachLine(beachLine, sweepLine, rotate, pilePoints);
        }
    }


    /**
     * Finit la fusion des blocs
     * @param beachLine Beachline
     * @param sweepLine SweepLine
     * @param rotate Rotation
     * @param listPoints Liste de points du bloc
     * @throws Exception
     */
    private ArrayList<Point> mergeBlocks(BeachLine beachLine, SweepLine sweepLine, AbstractRotate rotate, ArrayList<Point> listPoints) throws Exception {

        // Initialisation des variables
        ArrayList<Point> originalZones = beachLine.getZones(); // TODO
        ArrayList<Point> points = PileManager.sortPoints(listPoints);

        if (!points.isEmpty()) {
            return init(beachLine, sweepLine, rotate, points, new ArrayList<>());
        }
        else {
            return new ArrayList<>();
        }
    }


    // Algorithme - Fin

    /**
     * Calcul les derniers cercles
     * @throws Exception
     */
    public void calculateLastCircles() throws Exception {
        if (!neutralPilePoints.isEmpty()) {
            init(neutralBeachLine, neutralSweepLine, new Rotate0Deg(), neutralPilePoints, StrUtils.circlesIdsToString(trueCircles));
        }
    }
}
