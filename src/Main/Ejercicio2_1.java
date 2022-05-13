package Main;


import FileManager.Config;
import FileManager.OutputManager;

public class Ejercicio2_1 {
    public static void main(String[] args) {
        double v0 = 5e4;
        Config.readConfig();
        double dt[] = { 1E-15 , 1E-16 , 1E-17 , 1E-18 };
        double L = (Config.N -1) * Config.D;
        for(int j = 0 ; j < dt.length ; j++){
            double delta_t = dt[dt.length -1 - j];
            for (int i = 0; i < 5; i++) {
                    Vector2D initPos = new Vector2D(0 , L / 2 + Config.D * (0.25 * i) );
                    String filename = "ej2_1-t" + (int)Math.round(-Math.log10(delta_t)) + "_" + String.format("%.0f" , 0.25 * i *100);
                    Radiation.run(   (int)Math.round(dt[0]/delta_t), Config.TOTAL_T , delta_t, Config.N, Config.D, Config.Q, Config.MASS, Config.K, v0, v0, Config.getIntegration(), initPos, new OutputManager(filename)  );
                }
            }
    }
}

