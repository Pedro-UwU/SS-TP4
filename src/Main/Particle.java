package Main;

import java.util.Objects;

public class Particle {
    static private int GLOBAL_ID = 1;

    public Vector2D pos;
    public Vector2D vel;
    public double radius;
    public double mass;
    public int id;
    public NetForce force;
    public Vector2D prevPos = null;

    public Particle(double x, double y, double vx, double vy, double radius, double mass, NetForce force) {
        this.pos = new Vector2D(x, y);
        this.vel = new Vector2D(vx, vy);
        this.radius = radius;
        this.mass = mass;
        this.id = Particle.GLOBAL_ID;
        this.force = force;
        GLOBAL_ID = GLOBAL_ID + 1;
    }

    public Particle(Vector2D pos, Vector2D vel, double radius, double mass, NetForce force) {
        this.pos = new Vector2D(pos.x, pos.y);
        this.vel = new Vector2D(vel.x, vel.y);
        this.radius = radius;
        this.mass = mass;
        this.id = Particle.GLOBAL_ID;
        this.force = force;
        GLOBAL_ID = GLOBAL_ID + 1;
    }

    void update(double delta_t) {
        Vector2D displacement = vel.mul(delta_t);
        this.pos = this.pos.add(displacement);
    }

    @Override
    public String toString() {
        return "Main.Particle: pos = " + pos.toString() + ", vel = " + vel.toString() + ", radius = " + radius + ", mass = " + mass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Vector2D getNetForce() {
        return this.force.getNetForce(this.pos, this.vel);
    }

    public Particle copy() {
        return new Particle(this.pos, this.vel, this.radius, this.mass, this.force);
    }

}