package tsp.projects.competitor.laCrepeVoyageuse.voronoi.display;

import tsp.projects.competitor.laCrepeVoyageuse.voronoi.Neighbour;
import tsp.projects.competitor.laCrepeVoyageuse.voronoi.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


/**
 * Class DisplayGraphAWT
 * Cette classe affiche le graphe sur un onglet
 */
public class DisplayGraphAWT extends JPanel {

    // Variables

    // Informations sur le graphe
    ArrayList<Neighbour> neighbours;
    ArrayList<Circle> circles;


    // Constructeur

    /**
     * Constructeur de la classe
     * @param neighbours Liste des voisins
     * @param circles Liste des cercles
     */
    public DisplayGraphAWT(ArrayList<Neighbour> neighbours, ArrayList<Circle> circles) {
        this.neighbours = neighbours;
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

        DrawAWT.drawLinks(graphics2D, neighbours, Color.GREEN);
        DrawAWT.drawPoints(graphics2D, neighbours, Color.BLUE);
         DrawAWT.drawPoints(graphics2D, circles, Color.RED);
         DrawAWT.drawCircles(graphics2D, circles, Color.YELLOW);
    }


    /**
     * Affiche le graphe dans un onglet
     * @param neighbours Liste des voisins
     * @param circles Liste des cercles
     */
    public static void showGraph(ArrayList<Neighbour> neighbours, ArrayList<Circle> circles, String title) {

        // Paramètres du graphe
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new DisplayGraphAWT(neighbours, circles));

        // Affichage
        frame.setVisible(true);
    }
}