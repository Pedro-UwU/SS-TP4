package FileManager;

import Main.Particle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class OutputManager {
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

    }
}
