package tsp.projects.competitor.laCrepeVoyageuse.voronoi2;

import java.util.Random;

public class Test {
    public static void main(String[] args) throws Exception {

        // Génération de 100 points aléatoires
        double[][] data = new double[10][2];
//        Random rand = new Random();
//
//        for (int i = 0; i < 2048; i++) {
//            data[i][0] = rand.nextDouble() * 800; // X
//            data[i][1] = rand.nextDouble() * 800; // Y
//        }

        data[0][0] = 3.208426534683928;
        data[0][1] = 83.83826319168757;
        data[1][0] = 48.168816337807904;
        data[1][1] = 74.77003770313677;
        data[2][0] = 93.88999125243811;
        data[2][1] = 74.10984338789713;
        data[3][0] = 32.528172084463435;
        data[3][1] = 67.26139860150381;
        data[4][0] = 54.87188575919464;
        data[4][1] = 63.78701668054364;
        data[5][0] = 33.72420145883389;
        data[5][1] = 43.637325637171976;
        data[6][0] = 66.61235273758605;
        data[6][1] = 23.962876528404898;
        data[7][0] = 6.754059692567294;
        data[7][1] = 23.79949609928438;
        data[8][0] = 40.58853772977267;
        data[8][1] = 23.57412039132494;
        data[9][0] = 42.22577250387041;
        data[9][1] = 10.73617517055544;


        // Génération du diagramme de Voronoi
        System.out.println("Diagramme de Voronoi");
        boolean displayGraph = true;

        Voronoi voronoi = new Voronoi();
        boolean step1 = voronoi.init(data);

        if (step1) {
            boolean step2 = voronoi.process();

            if (step2) {
                if (displayGraph) {
                    boolean step3 = voronoi.display();

                    if (step3) {
                        System.out.println("Diagramme de Voronoi ... SUCCESS");
                    }
                    else {
                        System.err.println("Diagramme de Voronoi ... FAILED");
                    }
                }
                else {
                    System.out.println("Diagramme de Voronoi ... SUCCESS");
                }
            }
            else {
                System.err.println("Diagramme de Voronoi ... FAILED");
            }
        }
        else {
            System.err.println("Diagramme de Voronoi ... FAILED");
        }
    }
}