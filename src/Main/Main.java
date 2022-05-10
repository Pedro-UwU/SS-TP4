package Main;

import Integrations.Integration;
import Integrations.Verlet;

public class Main {
    public static void main(String[] args) {
        double delta_t = 0.00000001;
        double k = 1e4;
        double b = 100;
        double mass = 70;
        double A = 1;
        double x0 = 1;
        double total_t = 5;
        int delta_to_print = 1000000;

        Oscillation.run(delta_to_print, 5, delta_t, k, b, A, x0, mass, new Verlet());
    }
}
