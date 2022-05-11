package Integrations;

import Main.Particle;
import Main.Vector2D;

public class Verlet extends Integration{
    public Verlet() {
        super();
    }

    @Override
    public Particle update() {
        Vector2D force = particle.getNetForce();
        if (particle.prevPos == null) {
            particle.prevPos = Integration.euler(particle, -delta_t, force);
        }
        Vector2D currentPos = particle.pos;
        particle.pos = particle.pos.mul(2.0)
                .sub(particle.prevPos)
                .add(force.mul(Math.pow(delta_t, 2)/particle.mass));

        particle.vel = particle.pos.sub(particle.prevPos).mul(1/(2*delta_t));
        particle.prevPos = currentPos;
        return particle;
    }
}
