package Integrations;

import Main.Particle;
import Main.Vector2D;

public abstract class Integration {
    double delta_t;
    Particle particle;

    public void setDelta_t(double delta_t) {
        this.delta_t = delta_t;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public abstract Particle update();

    public static Vector2D euler(Particle p, double delta_t, Vector2D forceVector) {
        return p.pos
                .add(p.vel.mul(delta_t))
                .add(forceVector.mul(Math.pow(delta_t, 2)/(2*p.mass)));
    }
}
