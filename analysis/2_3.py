import json
import math
import matplotlib.pyplot as plt

def main():
    plt.rcParams.update({'font.size': 20})
    
    velocities = [5000, 16250, 27500, 38750, 50000]
    runs = 200
    absorbed = 0
    BOARD_SIZE = 16
    wall_per_vel = {}
    for v in velocities:
        walls = {
            "Arriba": 0,
            "Abajo": 0,
            "Izquierda": 0,
            "Derecha": 0,
            "Absorbido": 0
        }
        for i in range(runs):
            file = open(f'../SS-TP4/results/Radiation/a_ej2_2-{v}_run{i}/dynamic.json')
            json_data = json.load(file)
            file.close()
            
            

            positions = list(zip(json_data["posX"], json_data["posY"]))
            if (json_data["end"][0] == 'absorbed'):
                walls["Absorbido"] += 1
                continue
            else:
                pos = positions[-1]
                static_file = open(f'../SS-TP4/results/Radiation/a_ej2_2-{v}_run{i}/static.json')
                json_static = json.load(static_file)
                D = json_static["D"]
                w = D * BOARD_SIZE
                h = D * (BOARD_SIZE-1)
                wallDiff, wall = checkWall(pos, w, h)
                walls[wall] += 1
        wall_per_vel[v] = walls
    # print(wall_per_vel)
    wall_per_vel_relative = {}
    for v in wall_per_vel:
        print(f'v: {v}')
        wall_per_vel_relative[v] = {}
        for k in wall_per_vel[v]:
            wall_per_vel_relative[v][k] = wall_per_vel[v][k]/runs
    
    for v in wall_per_vel_relative:
        plt.figure(figsize=(10, 5))
        ax = plt.gca()
        ax.set_ylim([0, 1])
        col = ['lightcoral', 'cornflowerblue', 'orange', 'mediumseagreen', 'paleturquoise']
        bars = plt.bar(wall_per_vel_relative[v].keys(), wall_per_vel_relative[v].values(), color=col)
        ax.bar_label(bars)
        plt.show()


def checkWall(pos, w, h):
    topDiff = math.fabs(pos[1] - h)
    botDiff = math.fabs(pos[1])

    verticalDiff = 0
    verticallWall = ''

    if (topDiff < botDiff):
        verticalDiff = topDiff
        verticallWall = 'Arriba'
    else:
        verticalDiff = botDiff
        verticallWall = 'Abajo'

    rightDiff = math.fabs(pos[0] - w)
    leftDiff = math.fabs(pos[0])

    horizonalDiff = 0
    horizonalWall = ''

    if (rightDiff < leftDiff):
        horizonalDiff = rightDiff
        horizonalWall = 'Derecha'
    else:
        horizonalDiff = leftDiff
        horizonalWall = 'Izquierda'

    if (horizonalDiff < verticalDiff):
        return horizonalDiff, horizonalWall
    return verticalDiff, verticallWall

if __name__ == "__main__":
    main()