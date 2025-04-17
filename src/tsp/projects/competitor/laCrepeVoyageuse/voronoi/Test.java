package tsp.projects.competitor.laCrepeVoyageuse.voronoi;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws Exception {

        // Génération de 100 points aléatoires
        double[][] data = new double[2048][2];
        Random rand = new Random();

        for (int i = 0; i < 2048; i++) {
            data[i][0] = rand.nextDouble() * 1200; // X
            data[i][1] = rand.nextDouble() * 1200; // Y
        }
//        x: 579.5417508214458, y: 463.43999285087875
//        x: 90.54899153275224, y: 294.9926265088746
//        x: 160.71663623589743, y: 629.6639706122883
//        x: 410.9111385037034, y: 654.3836092728476
//        x: 579.6473800883098, y: 455.1093094731759
//        x: 86.91336632924794, y: 389.18662169563845
//
//        data[0][0] = 579.5417508214458;
//        data[0][1] = 463.43999285087875;
//        data[1][0] = 90.54899153275224;
//        data[1][1] = 294.9926265088746;
//        data[2][0] = 160.71663623589743;
//        data[2][1] = 629.6639706122883;
//        data[3][0] = 410.9111385037034;
//        data[3][1] = 654.3836092728476;
//        data[4][0] = 579.6473800883098;
//        data[4][1] = 455.1093094731759;
//        data[5][0] = 86.91336632924794;
//        data[5][1] = 389.18662169563845;


//        x: 49.35572095615966, y: 471.7726992224068
//        x: 212.56142782350668, y: 645.0606450752601
//        x: 625.1916336504319, y: 449.64306401970975
//        x: 440.8335424612656, y: 679.9075704464077
//        x: 131.12088465983697, y: 473.1901258559311
//        x: 491.82279315352105, y: 40.87628159568499
//        x: 219.69140724144304, y: 547.2966287062437
//        x: 636.0088174540125, y: 416.868556439071
//        x: 361.8290260466586, y: 583.6068607608214
//        x: 80.57800301492036, y: 624.9844425852114

//        data[0][0] = 49.35572095615966;
//        data[0][1] = 471.7726992224068;
//        data[1][0] = 212.56142782350668;
//        data[1][1] = 645.0606450752601;
//        data[2][0] = 625.1916336504319;
//        data[2][1] = 449.64306401970975;
//        data[3][0] = 440.8335424612656;
//        data[3][1] = 679.9075704464077;
//        data[4][0] = 131.12088465983697;
//        data[4][1] = 473.1901258559311;
//        data[5][0] = 491.82279315352105;
//        data[5][1] = 40.87628159568499;
//        data[6][0] = 219.69140724144304;
//        data[6][1] = 547.2966287062437;
//        data[7][0] = 636.0088174540125;
//        data[7][1] = 416.868556439071;
//        data[8][0] = 361.8290260466586;
//        data[8][1] = 583.6068607608214;
//        data[9][0] = 80.57800301492036;
//        data[9][1] = 624.9844425852114;


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