package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.display;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.GraphPoint;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Point;

import java.awt.*;
import java.util.ArrayList;


/**
 * Class DrawAWT
 * Cette classe dessine des formes dans le graphe
 */
public class DrawAWT {

    // Dessin

    /**
     * Dessine des points dans le graphe à partir d'une liste de points
     * @param graphics2D Graphique
     * @param points Liste de points
     * @param color Couleur des points
     */
    public static <T extends Point> void drawPoints(Graphics2D graphics2D, ArrayList<T> points, Color color) {

        // Affecte une couleur aux points
        graphics2D.setColor(color);

        // On ajoute tous les points dans le graphe
        for (Point p : points) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            graphics2D.fillOval(x - 3, y - 3, 6, 6);
        }
    }

    /**
     * Dessine les relations entre les voisins à partir d'une liste de voisins
     * @param graphics2D Graphique
     * @param graphPoints Liste de voisins
     * @param color Couleur des liens entre les voisins
     */
    public static void drawLinks(Graphics2D graphics2D, ArrayList<GraphPoint> graphPoints, Color color) {

        // Affecte une couleur aux relations
        graphics2D.setColor(color);

        // Pour tous les voisins, on les lie avec leurs voisins directs
        for (GraphPoint n : graphPoints) {
            int x1 = (int) n.getX();
            int y1 = (int) n.getY();

            for (GraphPoint other : n.getNeighbours()) {
                int x2 = (int) other.getX();
                int y2 = (int) other.getY();
                graphics2D.drawLine(x1, y1, x2, y2);
            }
        }
    }


    /**
     * Dessine des cercles dans le graphe
     * @param graphics2D Graphique
     * @param circles Liste de cercles
     * @param color Couleur des cercles
     */
    public static void drawCircles(Graphics2D graphics2D, ArrayList<Circle> circles, Color color) {

        // Affecte une couleur aux cercles
        graphics2D.setColor(color);

        // On affiche tous les cercles avec leur rayon
        for (Circle circle : circles) {
            int x = (int) circle.getX();
            int y = (int) circle.getY();
            int radius = (int) circle.getRadius();
            graphics2D.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

            Point topCircle = circle.getTopCircle();
            graphics2D.drawLine(x, y, (int) topCircle.getX(), (int) topCircle.getY());
        }
    }
}
