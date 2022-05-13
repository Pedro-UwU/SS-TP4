package FileManager;

import Main.Particle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class OscillationOutputManager {
    static String DIRECTORY = "./results/Oscillation";

    String name;

    JSONObject dynamic_output;
    JSONObject static_output;

    JSONArray pos, ts;

    public OscillationOutputManager(String name) {
        this.name = name;
        dynamic_output = new JSONObject();
        pos = new JSONArray();
        ts = new JSONArray();
        dynamic_output.put("pos", pos);
        dynamic_output.put("t", ts);
        static_output = new JSONObject();
        File dir = new File(DIRECTORY);
        dir.mkdir();
    }

    public void saveSnapshot(Particle p, double t) {
        double x = p.pos.x;
        pos.put(x);
        ts.put(t);
    }

    public void setStatic(double k, double b, double mass, double A) {
        static_output.put("k", k);
        static_output.put("b", b);
        static_output.put("mass", mass);
        static_output.put("A", A);
    }

    public void dumpStatic() {
        File dir = new File(DIRECTORY + "/" + this.name);
        dir.mkdir();
        String filePath = DIRECTORY + "/" + this.name + "/" + "static.json";
        File dir2 = new File(filePath);
        FileWriter writer;
        try {
            if (dir2.createNewFile()) {
                System.out.println("File created: " + dir2.getName());
                writer = new FileWriter(filePath);
                writer.write(static_output.toString());
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void dumpDynamic() {
        File dir = new File(DIRECTORY + "/" + this.name);
        dir.mkdir();
        String filePath = DIRECTORY + "/" + this.name + "/" + "dynamic.json";
        File dir2 = new File(filePath);
        FileWriter writer;
        try {
            if (dir2.createNewFile()) {
                System.out.println("File created: " + dir2.getName());
                writer = new FileWriter(filePath);
                writer.write(dynamic_output.toString());
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }




}
