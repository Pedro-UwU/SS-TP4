package Main;

import FileManager.OutputManager;
import Integrations.Integration;

import java.util.Random;

public class Radiation {

    //Grilla
    private final int N ;
    private final double D,L;
    //magnitudes fisicas
    private double Q;
    private final double M;
    private final double k ;
    private NetForce force;
    private Charge[] charges;
    public Particle particle;
    private Integration integrationMethod;



    public void Update() {
        if (this.integrationMethod == null) {
            throw new RuntimeException("ERROR: Integration method is null");
        }
        this.integrationMethod.update();
    }

    public Charge[] getCharges(){
        return this.charges;
    }

    public Radiation(int n , double d, double q, double m, double k, double minV0 , double maxV0 , Integration integrationMethod) {
        D = d;
        N = n;
        L = (N -1) * D;//tengo una particula que va al borde
        Q = q;
        M = m;
        this.k = k;
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

        this.particle = new Particle( getInitialParticlePos() , getV0(minV0, maxV0) , 0 , M , this::getNetForce);

    }


    private Vector2D getV0(double minV0 , double maxV0){
        double randomV0 = new Random(System.currentTimeMillis()).nextDouble() * (maxV0 - minV0)  + minV0 ;
        return new Vector2D(randomV0 , 0 );
    }

    public Radiation(int n , double d, double q, double m, double k, double minV0 , double maxV0 , Integration integrationMethod , Vector2D initialPos) {
        D = d;
        N = n;
        L = (N -1) * D;//tengo una particula que va al borde
        Q = q;
        M = m;
        this.k = k;
        this.integrationMethod = integrationMethod;
        charges = new Charge[N * N];
        createCharges();
        this.particle = new Particle( initialPos , getV0(minV0, maxV0) , 0 , M , this::getNetForce);
        System.out.println("ready");
    }

    //Auxiliares al constructor
    private Vector2D getInitialParticlePos(){
        double distance = new Random(System.currentTimeMillis()).nextDouble() * 2 - 1;
        return new Vector2D(0,L / 2 + (distance * D));
    }

    private void createCharges(){
        for( int i = 0 ; i < N ; i++){
            for(int j = 0 ; j < N ; j++){
                double charge = Q;
                this.Q = -Q;
                Vector2D position = new Vector2D( (i+1) * D , j * D);
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
            energy += c.charge / particle.pos.distance(c.pos) ;
        }
        return energy * Q * k;
    }

    private double getKineticEnergy(){
        return 0.5 * particle.mass * Math.pow(particle.vel.magnitude() , 2);
    }

    //Checkeos del sistema

    private boolean checkParticleAbsorbed(){
        //Recupero cuales eran los valores de i y j cuando cree las particulas
        int i = (int) Math.round(particle.pos.x / D);
        int j = (int) Math.round(particle.pos.y / D);
        if( i <0 || i> (N-1) || j < 0 || j > N)
            return false;
        Charge close = charges[i*N +j];
        return close.pos.distance(particle.pos) < 0.01 * D;
    }

    private boolean checkParticleInside(){
        return particle.pos.x >= 0 && particle.pos.x <= (L+D) && particle.pos.y>=0 && particle.pos.y<=(L);
    }

    public static void run(int delta_to_print, double total_time, double delta_t, int n , double d, double q, double m, double k, double minV0 , double maxV0 , Integration integrationMethod , Vector2D initialPos, OutputManager outputManager) {
        Radiation r;
        if(initialPos == null){
            r = new Radiation(n , d , q , m , k , minV0 , maxV0  , integrationMethod);
        }else {
            r = new Radiation( n , d,  q,  m,  k, minV0 , maxV0 , integrationMethod , initialPos);
        }
        outputManager.saveGrid(r.charges);
        integrationMethod.setDelta_t(delta_t);
        integrationMethod.setParticle(r.particle);
        int i = 0;
        double initialEnergy = r.getKineticEnergy() + r.getElectrostaticEnergy();
        double ke = r.getKineticEnergy();
        double ee = r.getElectrostaticEnergy();
        double diff = initialEnergy - ke - ee;
        Particle particle = r.particle;
        System.out.println(diff);
        int temp_n = 0;
        for (double t = 0; t < total_time && !r.checkParticleAbsorbed() && r.checkParticleInside(); t+=delta_t, i++) {
            if (i % delta_to_print == 0) {
                outputManager.saveSnapshot(particle, t);
            }

           // if (n % delta_to_print == 0) {
                ke = r.getKineticEnergy();
                ee = r.getElectrostaticEnergy();
                diff = initialEnergy - ke - ee;
                System.out.println("temp_n: " + temp_n + " t: " + String.format("%g", t) + " - Kinetic: " + String.format("%g", ke) + " - Potential: " + String.format("%g", ee) + " - Diff: " + String.format("%g", Math.abs(diff)));
           // }
            r.Update();
            temp_n++;
        }
        if(r.checkParticleAbsorbed()){
            outputManager.saveEndCondition("absorbed");
            System.out.println("Absorbed");
        }
        else if(!r.checkParticleInside()) {
            outputManager.saveEndCondition("outside");
            System.out.println("Outside");
        }
        System.out.println(i);
        outputManager.saveDynamic();

    }



}
