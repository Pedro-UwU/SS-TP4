package Main;

<<<<<<< Updated upstream
import FileManager.OscillationOutputManager;
=======
import FileManager.Config;
>>>>>>> Stashed changes
import FileManager.OutputManager;
import Integrations.*;

public class Main {
    public static void main(String[] args) {
<<<<<<< Updated upstream

        double delta_t = 1E-3;
        double k = 10000;
        double b = 100;
        double mass = 70;
        double A = 1;
        double x0 = 1;
        double total_t = 5;
        int delta_to_print = 1;


        Oscillation.run(delta_to_print, total_t, delta_t, k, b, A, x0, mass, new Gear(), new OscillationOutputManager("Test"));


//        double k = 1E10;
//        double q = 1E-19;
//        double m = 1E-27;
//        double d = 1E-8;
//        Vector2D v0 = new Vector2D( 5E3 ,0);
//        double minV0 = 5E4;
//        double maxV0 = 5E4;
//        double total_t = 5;
//        int delta_to_print = 100;
//        double delta_t = 1E-16;
//        int n = 16;
//        Radiation.run(delta_to_print, total_t, delta_t, n, d, q, m, k, minV0 , maxV0, new GearNoVel(), null, new OutputManager(null));
=======
        Config.readConfig();
        if(Config.SIM == 0 ) {
            double delta_t = 1E-3;
            double k = 10000;
            double b = 100;
            double mass = 70;
            double A = 1;
            double x0 = 1;
            double total_t = 5;
            int delta_to_print = 1;
            Oscillation.run(Config.DELTA_T2_FACTOR, Config.TOTAL_T, Config.DELTA_T, Config.K, Config.B, Config.A, Config.X0, Config.MASS, Config.getIntegration());
            return;
        }

        double k = 1E10;
        double q = 1E-19;
        double m = 1E-27;
        double d = 1E-8;
        Vector2D v0 = new Vector2D( 5E3 ,0);
        double minV0 = 5E4;
        double maxV0 = 5E4;
        double total_t = 5;
        int delta_to_print = 100;
        double delta_t = 1E-16;
        int n = 16;
        Radiation.run(Config.DELTA_T2_FACTOR, Config.TOTAL_T, Config.DELTA_T, Config.N,
                Config.D, Config.Q, Config.MASS, Config.K, Config.MIN_V0 ,Config.MAX_V0, Config.getIntegration(), null, new OutputManager(null));
>>>>>>> Stashed changes

    }

}
