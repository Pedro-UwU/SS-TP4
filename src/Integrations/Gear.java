package Integrations;

import Main.Particle;
import Main.Vector2D;

public class Gear extends Integration {
    public Gear(){super();}
    public final double[] alpha = { 3.0/16 , 251.0/360 , 1.0 , 11.0/18 , 1.0/6 , 1.0/60 };
    public double[] pCoefficients = null;
    private Vector2D[] r = null;



    private Vector2D[] predict(){
        Vector2D[] rp = new Vector2D[6];
        for(int i = 0 ; i < 6 ; i++){
            Vector2D aux = new Vector2D(0,0);
            for( int j = i+1 ; j < 6 ; j++ ){
                int dif = j - i;
                aux = aux.add(r[j].mul(pCoefficients[dif]));
            }
            rp[i] = r[i].add(aux);
        }
        return rp;
    }

    private Vector2D[] correct( Vector2D[] rp , Vector2D dR2){
        Vector2D[] rc = new Vector2D[6];
        for(int i = 0 ; i < 6 ; i ++ ){
            rc[i] = rp[i].add(dR2.mul(alpha[i]/pCoefficients[i]));
        }
        return rc;
    }
    @Override
    public Particle update() {
        if(pCoefficients == null) {
            pCoefficients = new double[]{1.0, delta_t, Math.pow(delta_t, 2) / 2, Math.pow(delta_t, 3) / 6, Math.pow(delta_t, 4) / 24, Math.pow(delta_t, 5) / (120)};
        }
        if(r == null){
            r = new Vector2D[6];
            r[0] = new Vector2D(particle.pos.x , particle.pos.y);
            r[1] = new Vector2D(particle.vel.x , particle.vel.y);
            r[2] = particle.getNetForce().mul(1/ particle.mass);
            r[3] = new Vector2D(0,0);
            r[4] = new Vector2D(0,0);
            r[5] = new Vector2D(0,0);
        }
        //Predict
        Vector2D[] rp = predict();
        Vector2D force = particle.force.getNetForce(rp[0] , rp[1]);
        Vector2D acc = force.mul(1/ particle.mass);
        Vector2D dA = acc.sub(rp[2]);
        Vector2D dR2 = dA.mul(delta_t*delta_t/2);

        Vector2D[] rc = correct(rp , dR2);

        particle.pos = rc[0];
        particle.vel = rc[1];
        r = rc;
        return particle;
    }
}
