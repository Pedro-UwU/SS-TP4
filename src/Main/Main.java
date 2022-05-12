package Main;

import Integrations.Beeman;
import Integrations.Gear;
import Integrations.Integration;
import Integrations.Verlet;

public class Main {
    public static void main(String[] args) {
        double delta_t = 0.0001;
        double k = 10000;
        double b = 100;
        double mass = 70;
        double A = 1;
        double x0 = 1;
        double total_t = 5;
        int delta_to_print = 1;

        Oscillation.run(delta_to_print, total_t, delta_t, k, b, A, x0, mass, new Beeman());
    }
}
