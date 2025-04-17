package tsp.projects.competitor.laCrepeVoyageuse.voronoi;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Neighbour
 * Cette classe représente un point avec des voisins
 */
public class Neighbour extends Point {

    // Variables

    // Liste des voisins du point
    private HashMap<String, Neighbour> neighbours = new HashMap<>();


    // Constructeur

    /**
     * Constructeur de la class Neighbour (via un point uniquement)
     * @param point Object Point
     */
    public Neighbour(Point point) {
        super(point);
    }


    // Getter

    /**
     * Renvoie la liste des voisins du point
     * @return Liste des points sous la forme d'objet de Neighbour
     */
    public ArrayList<Neighbour> getNeighbours() {
        return new ArrayList<>(neighbours.values());
    }


    // Autres fonctions

    /**
     * Ajoute un nouveau voisin au point
     * @param neighbour Objet Neighbour
     */
    public void addNeighbour(Neighbour neighbour) {
        if (!neighbours.containsKey(neighbour.getId())) {
            neighbours.put(neighbour.getId(), neighbour);
        }
    }


    // Affichage dans la console de l'objet

    /**
     * Affiche l'objet sous la forme d'une chaine de caractère
     */
    @Override
    public String toString() {
        return "Neighbour - " + super.toString();
    }
}
