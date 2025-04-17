package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

/**
 * Class SweepLine
 * Cette classe est un singleton et représente la ligne balayement qui génère le graphe de voronoi
 */
public class SweepLine {

    // Variables

    // Position courante de la sweepLine
    private Point position = new Point();

    // Instance de classe
    private static SweepLine instance = null;


    // Constructeur

    /**
     * Constructeur de la classe en privé pour empêcher les autres programmes de l'instancier
     */
    private SweepLine() {}


    // Instance de la classe

    /**
     * Renvoie une instance de la classe DataGraph
     * Toutes les instances de cette classe sont similaires les unes des autres !
     * @return Object DataGraph
     */
    public static SweepLine getInstance() {
        if (instance == null) {
            instance = new SweepLine();
        }
        return instance;
    }


    // Setter

    /**
     * Modifie la position de la SweepLine
     * @param position Nouvelle position de la SweepLine
     */
    public void setPosition(Point position) {
        this.position = position;
    }


    // Getter

    /**
     * Renvoie la position de la SweepLine
     * @return Objet Point
     */
    public Point getPosition() {
        return position;
    }


    // Autres fonctions

    /**
     * Transpose un point par rapport à la SweepLine
     * L'idée est de modifier le repère du point pour que la référence en ordonnée soit par rapport à la SweepLine et non le graphe
     * @param point Point à transposer
     * @return objet Point
     */
    public Point translateToSweepLine(Point point) throws Exception {

        // Récupération des coordonnées du point et de la SweepLine
        double x = point.getX();
        double y = point.getY();
        double sweepY = position.getY();

        // Si le point est en-dessous de la SweepLine, il est considéré comme invalide
        if (sweepY > y) {
            throw new Exception("La position du point à transposer doit être au-dessus de la sweepLine");
        }
        return new Point(x, y - sweepY);
    }

    /**
     * Transpose un point par rapport au graphe
     * @param point Point à transposer
     * @return objet Point
     */
    public Point translateToGraph(Point point) {

        // Récupération des coordonnées du point et de la SweepLine
        double x = point.getX();
        double y = point.getY();
        double sweepY = position.getY();

        return new Point(x, y + sweepY);
    }
}
