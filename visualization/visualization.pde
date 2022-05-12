final String CONFIG_LOCATION = "../config_visualization.json";
final String BOARD_LOCATION = "../board.json";
final Integer BOARD_SIZE = 16;

String SIM_NAME;
int radius = 15;

float space_width;
float space_height;
int n = 0;
float D;

JSONObject dynamic_data;
JSONArray posX, posY, t;

JSONObject static_data;

PVector[] charges;


int screen_w, screen_h;


void setup() {
  size(592, 555, P2D);
  
  JSONObject config = loadJSONObject(CONFIG_LOCATION);
  String dynamic_path = config.getString("Name");
  dynamic_data = loadJSONObject(dynamic_path);
  
  D = 1E-8;
  space_width = BOARD_SIZE*D;
  space_height = (BOARD_SIZE-1)*D;
  
  posX = dynamic_data.getJSONArray("posX");
  posY = dynamic_data.getJSONArray("posY");
  t = dynamic_data.getJSONArray("t");
  
  charges = new PVector[BOARD_SIZE];
  JSONObject board_info = loadJSONObject(BOARD_LOCATION);
  JSONArray bX = board_info.getJSONArray("posx");
  JSONArray bY = board_info.getJSONArray("posy");
  JSONArray ch = board_info.getJSONArray("charges");
  
  for (int i = 0; i < BOARD_SIZE; i++) {
    float x = bX.getFloat(i);
    float y = bY.getFloat(i);
    int aux_charge = ch.getFloat(i) > 0 ? 1 : 0;
    charges[i] = new PVector(x, y,  aux_charge);
  }
  
  frameRate(120);
  
}

void draw() {
  if (n >= posX.size()) {
    noLoop();
    return;
  }
  background(51);
  fill(255, 0, 0);
  noStroke();
  float x = posX.getFloat(n);
  float y = posY.getFloat(n);
  float pixelX = map(x, 0, space_width, 0, width);
  float pixelY = map(y, 0, space_height, 0, height);
  ellipse(pixelX, pixelY, radius, radius);
  n++;
  println("X: " + x + " - Y: " + y);
}

void drawBoard() {
  
  
}
