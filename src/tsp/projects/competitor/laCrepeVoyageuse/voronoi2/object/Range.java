package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object;

/**
 * Class Range
 * Cette classe représente un intervalle pour la BeachLine
 */
public class Range {

    // Variables

    // Limite de l'intervalle
    private double startRange;
    private double endRange;


    // Constructeur

    /**
     * Constructeur de la classe Range
     * @param startRange Début de l'intervalle
     * @param endRange Fin de l'intervalle
     */
    public Range(double startRange, double endRange) {
        this.startRange = startRange;
        this.endRange = endRange;
    }


    // Setter

    /**
     * Modifie l'intervalle
     * @param startRange Début de l'intervalle
     * @param endRange Fin de l'intervalle
     */
    public void setRange(double startRange, double endRange) {
        setStartRange(startRange);
        setEndRange(endRange);
    }

    /**
     * Modifie le début de l'intervalle
     * @param startRange Début de l'intervalle
     */
    public void setStartRange(double startRange) {
        this.startRange = startRange;
    }

    /**
     * Modifie la fin de l'intervalle
     * @param endRange Fin de l'intervalle
     */
    public void setEndRange(double endRange) {
        this.endRange = endRange;
    }


    // Getter

    /**
     * Renvoie le début de l'intervalle
     * @return Nombre flottant
     */
    public double getStartRange() {
        return startRange;
    }

    /**
     * Renvoie la fin de l'intervalle
     * @return Nombre flottant
     */
    public double getEndRange() {
        return endRange;
    }


    // Affichage dans la console de l'objet

    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "Range [start=" + startRange + ", end=" + endRange + "]";
    }
}
