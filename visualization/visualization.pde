final String CONFIG_LOCATION = "../config_visualization.json";
final String BOARD_LOCATION = "../board.json";
final Integer BOARD_SIZE = 16;

String SIM_NAME;
int radius = 15;
int pan = 40;

float space_width;
float space_height;
int n = 0;
float D;
boolean see_path;
boolean save;

ArrayList<PVector> path;
JSONObject dynamic_data;
JSONArray posX, posY, t;

JSONObject static_data;

PVector[] charges;


int screen_w, screen_h;


void setup() {
  size(592, 555, P2D);
  ellipseMode(CENTER);
  JSONObject config = loadJSONObject(CONFIG_LOCATION);
  see_path = config.getBoolean("View_path");
  save = config.getBoolean("save");
  SIM_NAME = config.getString("saveName");
  String dynamic_path = config.getString("Name");
  dynamic_data = loadJSONObject(dynamic_path);
  
  D = 1E-8;
  space_width = BOARD_SIZE*D;
  space_height = (BOARD_SIZE-1)*D;
  
  path = new ArrayList<>();
  
  posX = dynamic_data.getJSONArray("posX");
  posY = dynamic_data.getJSONArray("posY");
  t = dynamic_data.getJSONArray("t");
  
  charges = new PVector[BOARD_SIZE*BOARD_SIZE];
  JSONObject board_info = loadJSONObject(BOARD_LOCATION);
  JSONArray bX = board_info.getJSONArray("posx");
  JSONArray bY = board_info.getJSONArray("posy");
  JSONArray ch = board_info.getJSONArray("charges");
  
  for (int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++) {
    float x = bX.getFloat(i);
    float y = bY.getFloat(i);
    int aux_charge = ch.getFloat(i) > 0 ? 1 : 0;
    charges[i] = new PVector(x, y,  aux_charge);
  }
  
  frameRate(240);
  
}

void draw() {
  if (n >= posX.size()) {
    noLoop();
    return;
  }
  background(51);
  drawBoard();
  
  float x = posX.getFloat(n);
  float y = posY.getFloat(n);
  float pixelX = map(x, 0, space_width, pan, width-pan);
  float pixelY = map(y, 0, space_height, pan, height-pan);
  
  if (see_path) {
    if (frameCount % 60 == 0) {
      path.add(new PVector(pixelX, pixelY));
    }
    stroke(255);
    strokeWeight(2);
    noFill();
    for (int i = 1; i < path.size(); i++) {
      PVector prev = path.get(i-1);
      PVector curr = path.get(i);
      line(prev.x, prev.y, curr.x, curr.y);
    }
  }
  
  fill(255, 0, 0);
  noStroke();
  ellipse(pixelX, pixelY, radius, radius);
  n++;
  println("X: " + x + " - Y: " + y);
  text("t: " + t.getFloat(n), 30, 30);
  
  if (save) {
    saveFrame("./data/" + SIM_NAME + "/#####.png");
  }
}

void drawBoard() {
  for (int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++) {
    PVector charge = charges[i];
    float x = map(charge.x, 2*D, 16*D, 37 + pan, width - pan);
    float y = map(charge.y, D, 15*D, 0 + pan, height - pan);
    if (charge.z > 0) {
      fill(255, 0, 0);
    } else {
      fill(0, 0, 255);
    }
    ellipse(x, y, radius, radius);
  }
  
}
