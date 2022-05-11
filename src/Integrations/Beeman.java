package Integrations;

import Main.Particle;
import Main.Vector2D;

public class Beeman extends Integration{
    public Beeman(){super();}
    @Override
    public Particle update() {
        Vector2D force = particle.getNetForce();
        Vector2D acc = force.mul(1 / particle.mass);
        if (particle.prevForce == null) {

            System.out.println("new prev pos");
            particle.prevPos = Integration.euler(particle, -delta_t, force);
            Vector2D prevVel = Integration.eulerVel(particle , -delta_t , force);
            particle.prevForce = particle.force.getNetForce(particle.prevPos , prevVel);
           //  particle.prevForce = force;
        }
        Vector2D prevAcc = particle.prevForce.mul(1/ particle.mass);
        double delta_t_square = Math.pow(delta_t , 2);
        Vector2D currentPos = particle.pos;

                    //x(t)
        Vector2D nextPos = particle.pos
                //v(t)*dt
                .add(particle.vel.mul(delta_t))
                //2/3*a(t)*dt^2
                .add(acc.mul(2/3 * delta_t_square))
                //1/6*a(t-dt)*dt^2
                .sub(prevAcc.mul(1/6 * delta_t_square));
        // v(t) + 3/2 a(t) * dt - 1/2 a(t-dt)*dt
        Vector2D predictedVel = particle.vel
                .add(acc.mul(3/2 * delta_t ))
                .sub(prevAcc.mul(  1/2 * delta_t ));

        Vector2D nextForce = particle.force.getNetForce(nextPos , predictedVel);
        Vector2D nextAcc = nextForce.mul(1/ particle.mass);
        particle.vel = particle.vel
                .add( nextAcc.mul(1/3 * delta_t) )
                .add( acc.mul(5/6 * delta_t ) )
                .sub( prevAcc.mul(1/6 * delta_t ));
        particle.pos = nextPos;
        particle.prevForce = force;
        particle.prevPos = currentPos;
        return particle;
    }
}
