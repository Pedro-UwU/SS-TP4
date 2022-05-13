package Main;

import FileManager.Config;
import FileManager.OutputManager;

import java.util.Random;

public class Ejercicio2_2 {
    public static void main(String[] args) {
        //valores v0 = 5000 16250 27500 38750 50000
        int[] velocities = {5000, 16250, 27500, 38750, 50000};
        Config.readConfig();
        final int TOTAL_RUNS = 200;
        Random rndGenerator = new Random();
        double L = (Config.N -1.0) * Config.D;
        for (int i = 0; i < velocities.length; i++) {
            for (int j = 0; j < TOTAL_RUNS; j++) {
                System.out.println("RUN: " + j + " - VEL: " + velocities[i]);
                double displacement = rndGenerator.nextDouble() * 2 - 1;
                double initialY = L / 2;
                initialY += Config.D * displacement;
                Vector2D initPos = new Vector2D(0, initialY);
                String runName = "a_ej2_2-" + velocities[i] + "_run" + j;
                Radiation.run(Config.DELTA_T2_FACTOR, Config.TOTAL_T, Config.DELTA_T, Config.N, Config.D, Config.Q, Config.MASS, Config.K, velocities[i], velocities[i], Config.getIntegration(), initPos, new OutputManager(runName));
            }
        }
    }
}
