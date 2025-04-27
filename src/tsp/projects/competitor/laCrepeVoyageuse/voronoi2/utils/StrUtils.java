package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.utils;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;

import java.util.ArrayList;

/**
 * Class StrUtils
 * Cette classe effectue des traitements sur un String
 */
public class StrUtils {

    /**
     * Décalle une lettre vers la droite dans une chaine de caractères
     * @param str Chaine à traiter
     * @return Chaine modifiée
     */
    public static String shiftRight(String str) {

        // Si la chaine ne contient qu'un seul élément ou est vide, alors on renvoie simplement la chaine
        if (str == null || str.length() <= 1) {
            return str;
        }

        // Sinon, on déplace la chaine d'un cran vers la droite en la coupant de 2
        char lastLetter = str.charAt(str.length() - 1);
        String startStr = str.substring(0, str.length() - 1);

        // On met la dernière lettre devant
        return lastLetter + startStr;
    }


    /**
     * Retourne une chaine de caractères
     * @param str Chaine à traiter
     * @return Chaine modifiée
     */
    public static String reverse(String str) {

        // Si la chaine ne contient qu'un seul élément ou est vide, alors on renvoie simplement la chaine
        if (str == null) {
            return null;
        }

        // Retourne la chaine
        return new StringBuilder(str).reverse().toString();
    }


    /**
     * Renvoie la liste des coordonnées des cercles
     * @param circles Liste de cercles
     * @return Liste d'identifiant
     */
    public static ArrayList<String> circlesIdsToString (ArrayList<Circle> circles) {
        ArrayList<String> strings = new ArrayList<>();

        // Sauvegarde des identifiants dans un nouveau tableau
        for (Circle circle : circles) {
            strings.add(circle.getId());
        }

        return strings;
    }
}
