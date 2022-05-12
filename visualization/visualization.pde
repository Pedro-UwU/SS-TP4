final String CONFIG_LOCATION = "../config_visualization.json";
String SIM_NAME;
int radius = 15;


float space_width;
float space_height;
int n = 0;
float D;

JSONObject dynamic_data;
JSONArray posX, posY, t;

JSONObject static_data;

void setup() {
  size(600, 600);
  
  JSONObject config = loadJSONObject(CONFIG_LOCATION);
  String dynamic_path = config.getString("Name");
  dynamic_data = loadJSONObject(dynamic_path);
  
  D = 1E-8;
  space_width = 16*D;
  space_height = 15*D;
  
  posX = dynamic_data.getJSONArray("posX");
  posY = dynamic_data.getJSONArray("posY");
  t = dynamic_data.getJSONArray("t");
  
  
}

void draw() {
  if (n >= 10) {
    noLoop();
    return;
  }
  background(51);
  fill(255, 0, 0);
  noStroke();
  float x = posX.getFloat(n);
  float y = posY.getFloat(n);
  ellipse(x, y, radius, radius);
  n++;
}
