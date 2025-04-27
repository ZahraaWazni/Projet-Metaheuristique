package tsp.projects.competitor.laCrepeVoyageuse.voronoi2.display;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.Circle;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi2.object.GraphPoint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Class DisplayGraphAWT
 * Cette classe affiche le graphe sur un onglet
 */
public class DisplayGraphAWT extends JPanel {

    // Variables

    // Informations sur le graphe
    ArrayList<GraphPoint> graphPoints;
    ArrayList<Circle> circles;


    // Constructeur

    /**
     * Constructeur de la classe
     * @param graphPoints Liste des voisins
     * @param circles Liste des cercles
     */
    public DisplayGraphAWT(ArrayList<GraphPoint> graphPoints, ArrayList<Circle> circles) {
        this.graphPoints = graphPoints;
        this.circles = circles;
    }


    /**
     * Dessine un graphe
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Pour des formes plus avancées
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DrawAWT.drawLinks(graphics2D, graphPoints, Color.GREEN);
        DrawAWT.drawPoints(graphics2D, graphPoints, Color.BLUE);
//        DrawAWT.drawPoints(graphics2D, circles, Color.RED);
//        DrawAWT.drawCircles(graphics2D, circles, Color.YELLOW);
    }


    /**
     * Affiche le graphe dans un onglet
     * @param graphPoints Liste des voisins
     * @param circles Liste des cercles
     */
    public static void showGraph(ArrayList<GraphPoint> graphPoints, ArrayList<Circle> circles, String title) {

        // Paramètres du graphe
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100, 100);
        frame.add(new DisplayGraphAWT(graphPoints, circles));

        // Affichage
        frame.setVisible(true);
    }
}