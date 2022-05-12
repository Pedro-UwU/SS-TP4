package FileManager;

import Main.Particle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class OutputManager {
    private final static String DIRECTORY = "./results";
    private static FileWriter SNAPSHOT_WRITER;
    JSONObject dynamic_data;
    JSONObject static_data;
    JSONArray positionsX;
    JSONArray positionsY;
    JSONArray velocitiesX;
    JSONArray velocitiesY;
    JSONArray times;
    String name;


    public OutputManager(String name) {
        this.name = name;
        dynamic_data = new JSONObject();
        positionsX = new JSONArray();
        positionsY = new JSONArray();
        velocitiesX = new JSONArray();
        velocitiesY = new JSONArray();
        times = new JSONArray();
        dynamic_data.put("posX", positionsX);
        dynamic_data.put("posY", positionsY);
        dynamic_data.put("velX", velocitiesX);
        dynamic_data.put("velY", velocitiesY);
        dynamic_data.put("t", times);
        static_data = new JSONObject();
    }


    public void setStatic(HashMap<String, Object> objects) {

    }

    public void saveSnapshot(Particle p, double t) {
        positionsX.put(p.pos.x);
        positionsY.put(p.pos.y);
        velocitiesX.put(p.vel.x);
        velocitiesY.put(p.vel.y);
        times.put(t);
    }

    public void saveEndCondition(String endCondition) {
        dynamic_data.append("end", endCondition);
    }

    public void saveDynamic() {
        File dir = new File(DIRECTORY);
        dir.mkdir();
        if(name == null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
            LocalDateTime time = LocalDateTime.now();
            name = dtf.format(time);
        }
        String filePath = DIRECTORY + "/" + name + ".json";
        File dir2 = new File(filePath);
        try {
            if (dir2.createNewFile()) {
                System.out.println("File created: " + dir2.getName());
                SNAPSHOT_WRITER = new FileWriter(filePath);
                SNAPSHOT_WRITER.write(dynamic_data.toString());
                SNAPSHOT_WRITER.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
