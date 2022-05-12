package Main;

import Integrations.Integration;

import java.util.Random;

public class Radiation {

    //Grilla
    private final int N ;
    private final double D,L;
    //magnitudes fisicas
    private final double Q , M,  k ;
    private final Vector2D v0;
    private NetForce force;
    private Charge[] charges;
    private Particle particle;
    private Integration integrationMethod;



    public void Update() {
        if (this.integrationMethod == null) {
            throw new RuntimeException("ERROR: Integration method is null");
        }
        this.integrationMethod.update();
    }

    public Radiation(int n , double d, double q, double m, double k, Vector2D v0 , Integration integrationMethod) {
        D = d;
        N = n;
        L = (N -1) * D;//tengo una particula que va al borde
        Q = q;
        M = m;
        this.k = k;
        this.v0 = v0;
        this.integrationMethod = integrationMethod;
        charges = new Charge[N * N];
        createCharges();
        /*
        force = new NetForce() {
            @Override
            public Vector2D getNetForce(Vector2D pos, Vector2D vel) {
                Vector2D acum = new Vector2D(0,0);
                for( Charge c : charges){
                    acum = acum.add( interactionPC(pos , c));
                }
                return  acum.mul(k * Q);
            }
        };*/

        this.particle = new Particle( getInitialParticlePos() , v0 , 0 , M , this::getNetForce);

    }

    public Radiation(int n , double d, double q, double m, double k, Vector2D v0 , Integration integrationMethod , Vector2D initialPos) {
        D = d;
        N = n;
        L = (N -1) * D;//tengo una particula que va al borde
        Q = q;
        M = m;
        this.k = k;
        this.v0 = v0;
        this.integrationMethod = integrationMethod;
        charges = new Charge[N * N];
        createCharges();
        this.particle = new Particle( initialPos , v0 , 0 , M , this::getNetForce);

    }

    //Auxiliares al constructor
    private Vector2D getInitialParticlePos(){
        double distance = new Random(System.currentTimeMillis()).nextDouble() * 2 - 1;
        return new Vector2D(0,L / 2 + (distance * D));
    }
    private void createCharges(){
        for( int i = 0 ; i < N ; i++){
            for(int j = 0 ; j < N ; j++){
                double charge = (i+j % 2 == 0)? Q : -Q;
                Vector2D position = new Vector2D( i * D , j * D);
                charges[i * N + j] = new Charge(position , charge);
            }
        }
    }

    public void setIntegrationMethod(Integration method) {
        this.integrationMethod = method;
    }


    //Calculos fuerza
    public Vector2D getNetForce(Vector2D pos, Vector2D vel) {
        Vector2D acum = new Vector2D(0,0);
        for( Charge c : charges){
            acum = acum.add( interactionPC(pos , c));
        }
        return  acum.mul(k * Q);
    }
    private Vector2D interactionPC( Vector2D pos , Charge c){
        //Optimizacion (se simplifica la raiz)
        double distSquared = Math.pow(pos.x - c.pos.x , 2 ) + Math.pow(pos.y - c.pos.y , 2 );
        return pos.sub(c.pos).normalize().mul(c.charge/distSquared);
    }

    //Calculos energia
    private double getElectrostaticEnergy(){
        double energy = 0;
        for( Charge c : charges){
            energy += particle.pos.distance(c.pos) * c.charge;
        }
        return energy * Q * k;
    }

    private double geKineticEnergy(){
        return 0.5 * particle.mass * Math.pow(particle.vel.magnitude() , 2);
    }

    //Checkeos del sistema

    private boolean checkParticleAbsorbed(){
        //Recupero cuales eran los valores de i y j cuando cree las particulas
        int i = (int) Math.round(particle.pos.x / D);
        int j = (int) Math.round(particle.pos.y / D);
        if( i <0 || i>N || j < 0 || j > N)
            return false;
        Charge close = charges[i*N +j];
        return close.pos.distance(particle.pos) < 0.01 * D;
    }

    private boolean checkParticleInside(){
        return particle.pos.x >= 0 && particle.pos.x <= L && particle.pos.y>=0 && particle.pos.y<=L;
    }

    public static void run(int delta_to_print, double total_time, double delta_t, int n , double d, double q, double m, double k, Vector2D v0 , Integration integrationMethod , Vector2D initialPos ) {
        Radiation r;
        if(initialPos == null){
            r = new Radiation(n , d , q , m , k , v0  , integrationMethod);
        }else {
            r = new Radiation( n , d,  q,  m,  k,v0 , integrationMethod , initialPos);
        }

        for (double t = 0; t < total_time && !r.checkParticleAbsorbed() && r.checkParticleInside(); t+=delta_t, n++) {
            Particle particle = r.particle;
            Vector2D particlePos = particle.pos;
            r.Update();
        }
    }




}
