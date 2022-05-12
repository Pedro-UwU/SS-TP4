package FileManager;

import Main.Charge;
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
        if(name == null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
            LocalDateTime time = LocalDateTime.now();
            this.name = dtf.format(time);
        }else {
            this.name = name;
        }
        dynamic_data = new JSONObject();
        positionsX = new JSONArray();
        positionsY = new JSONArray();
        velocitiesX = new JSONArray();
        velocitiesY = new JSONArray();
        times = new JSONArray();
        dynamic_data.append("posX", positionsX);
        dynamic_data.append("posY", positionsY);
        dynamic_data.append("velX", velocitiesX);
        dynamic_data.append("velY", velocitiesY);
        dynamic_data.append("t", times);
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

    public void saveGrid(Charge[] charges){
        String filePath = "board.json";
        File dir2 = new File(filePath);
        JSONObject board = new JSONObject();
        for( Charge c : charges) {
            board.append("charges" , c.charge);
            board.append("posx" , c.pos.x);
            board.append("posy" , c.pos.y);
        }
        try {
            if (dir2.createNewFile()) {
                System.out.println("File created: " + dir2.getName());
                FileWriter boardWrite = new FileWriter(filePath);
                boardWrite.write(board.toString());
                boardWrite.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
