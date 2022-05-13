package Main;

import FileManager.OscillationOutputManager;
import Integrations.Integration;

public class Oscillation {
    public double k; //Coeficiente restitucion
    public double b; //Coeficiente friccion
    public Particle particle;
    private Integration integrationMethod;
    private final double initialX;

    private final double w;
    private final double beta;

    public NetForce force;

    public Oscillation(double k, double b, double A, double initialX, double mass) {
        this.k = k;
        this.b = b;
        this.initialX = initialX;
        double w0 = Math.sqrt(k / mass);
        this.beta = b/(2* mass);
        this.w = Math.sqrt(Math.pow(w0, 2) - Math.pow(this.beta, 2));


        if (beta >= w0) {
            throw new RuntimeException("ERROR: Only Under-damped Oscillator Accepted");
        }

        force = (pos, vel) -> new Vector2D(-k*pos.x -b*vel.x, 0);
        this.particle = new Particle(new Vector2D(initialX, 0), new Vector2D(-A*(b/(2*mass)), 0), 0, mass, this.force);
    }

    public void setIntegrationMethod(Integration method) {
        this.integrationMethod = method;
    }

    public void Update() {
        if (this.integrationMethod == null) {
            throw new RuntimeException("ERROR: Integration method is null");
        }
        this.integrationMethod.update();
    }

    public Vector2D analyticSolution(double t) {
        double posX = initialX * Math.exp(-beta*t) * Math.cos(this.w*t);
        return new Vector2D(posX, 0);
    }

    public static void run(int delta_to_print, double total_time, double delta_t, double k, double b, double A, double initialX, double mass, Integration integrationMethod, OscillationOutputManager outputManager, boolean saveAnalytic) {
        Oscillation osc = new Oscillation(k, b, A, initialX, mass);
        integrationMethod.setParticle(osc.particle);
        integrationMethod.setDelta_t(delta_t);
        osc.setIntegrationMethod(integrationMethod);
        int n = 0;
        double error = 0;
        outputManager.setStatic(k, b, mass, A);
        for (double t = 0; t < total_time; t+=delta_t, n++) {
            Particle particle = osc.particle;
            Vector2D analytic = osc.analyticSolution(t);
            Vector2D particlePos = particle.pos;
            double diff = analytic.x - particlePos.x;
            error += Math.pow(diff, 2);
            if (n % delta_to_print == 0) {
                System.out.println("t: " + String.format("%.3f", t) + " - Analytic: " + String.format("%.3f", analytic.x) + " - Integration: " + String.format("%.3f", particlePos.x) + " - Diff: " + String.format("%.9f", Math.abs(diff)));
            }
            if (saveAnalytic) {
                Particle aux = new Particle(analytic, new Vector2D(0, 0), 1, 1, osc.particle.force);
                outputManager.saveSnapshot(aux, t);
            } else {
                outputManager.saveSnapshot(particle, t);
            }
            osc.Update();
        }
        outputManager.dumpDynamic();
        outputManager.dumpStatic();
        error /= n;
        System.out.println("Error: " + error);
    }


}
