package Main;


import FileManager.Config;
import FileManager.OutputManager;

public class Ejercicio2_1 {
    private final static int RUNS_EACH = 10;
    public static void main(String[] args) {
        double v0 = 5e4;
        Config.readConfig();
        double dt[] = {1E-16, 1E-17, 1E-18, 1E-19, 1E-20, 1E-21 , 1E-22};
        double L = (Config.N -1) * Config.D;
        for (int i = 0; i < 5; i++) {
            Vector2D initPos = new Vector2D(0 , L / 2 + Config.D * (0.25 * i) );
            for(int j = 0 ; j < dt.length ; j++){
                double delta_t = dt[dt.length -1 - j];
                for(int k = 0 ; k < RUNS_EACH ; k++ ){
                    String filename = "ej2.1-" + i + "_" +j + "_" + k;
                    Radiation.run( (int)(1/(delta_t-1)) , Config.TOTAL_T , Config.DELTA_T, Config.N, Config.D, Config.Q, Config.MASS, Config.K, v0, v0, Config.getIntegration(), initPos, new OutputManager(filename)  );
                }
            }
            // Radiation.run(Config.DELTA_T2_FACTOR, Config.TOTAL_T, Config.DELTA_T, Config.N, Config.D, Config.Q, Config.MASS, Config.K, Config.MIN_V0, Config.MAX_V0, Config.getIntegration(), null, new OutputManager(null));
        }
    }
}
