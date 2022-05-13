package FileManager;

import Integrations.*;
import Main.Vector2D;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Config {

    public static double K , B , MASS , A , X0 , TOTAL_T , DELTA_T;
    public static double Q , D , MIN_V0 , MAX_V0;
    public static int DELTA_T2_FACTOR , N ;
    private static final String config_path = "../config.json";
    private static String INTEGRATION;
    public static int SIM;
    public static String NAME;
    /*
    double k = 1E10;
    double q = 1E-19;
    double m = 1E-27;
    double d = 1E-8;
    Vector2D v0 = new Vector2D( 5E3 ,0);
    double minV0 = 5E4;
    double maxV0 = 5E4;
    double total_t = 5;
    int delta_to_print = 100;
    double delta_t = 1E-16;
    int n = 16;
    */

    public static void readConfig(){
        StringBuilder str_builder = new StringBuilder();
        try {
            Stream<String> stream = Files.lines(Paths.get(Config.config_path));
            stream.forEach(s -> str_builder.append(s).append('\n'));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        String content = str_builder.toString();
        JSONObject root = new JSONObject(content);
        SIM = root.getInt("simulation");
        if(SIM == 0 ){
            readConfigOscillation(root);
        }else{
            readConfigRadiation(root);
        }
    }


        /*
        double delta_t = 1E-3;
        double k = 10000;
        double b = 100;
        double mass = 70;
        double A = 1;
        double x0 = 1;
        double total_t = 5;
        int delta_to_print = 1;
        */

    public static void readConfigOscillation(JSONObject o){
        JSONObject sim = o.getJSONObject("config");
        DELTA_T = sim.getDouble("delta_t");
        TOTAL_T = sim.getDouble("total_t");
        DELTA_T2_FACTOR = sim.getInt("delta_t2_factor");
        K = sim.getDouble("k");
        MASS = sim.getDouble("m");
        A = sim.getDouble("a");
        X0 = sim.getDouble("x0");
        B = sim.getDouble("b");
        INTEGRATION = sim.getString("integration");
        NAME = sim.getString("name");
    }


    public static Integration getIntegration(){
        switch (INTEGRATION) {
            case "beeman":
                if(SIM == 1)
                    System.out.println("Beeman is aproximating for forces affected by velocity");
                return new Beeman();
            case "gear":
                if(SIM == 1)
                    return new GearNoVel();
                return new Gear();
            default:
                return new Verlet();
        }

    }
    /*
   double k = 1E10;
   double q = 1E-19;
   double m = 1E-27;
   double d = 1E-8;
   Vector2D v0 = new Vector2D( 5E3 ,0);
   double minV0 = 5E4;
   double maxV0 = 5E4;
   double total_t = 5;
   int delta_to_print = 100;
   double delta_t = 1E-16;
   int n = 16;
   */
    public static void  readConfigRadiation(JSONObject o){
        JSONObject sim = o.getJSONObject("config");
        DELTA_T = sim.getDouble("delta_t");
        TOTAL_T = sim.getDouble("total_t");
        DELTA_T2_FACTOR = sim.getInt("delta_t2_factor");
        K = sim.getDouble("k");
        MASS = sim.getDouble("m");
        Q = sim.getDouble("q");
        D = sim.getDouble("d");
        N = sim.getInt("n");
        MIN_V0 = sim.getDouble("min_v0");
        MAX_V0 = sim.getDouble("max_v0");
        NAME = sim.getString("name");
        INTEGRATION = sim.getString("integration");
    }
}
