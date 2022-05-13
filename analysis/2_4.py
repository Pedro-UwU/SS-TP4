from cProfile import label
import json
import math
import statistics
import numpy as np
from turtle import color
from matplotlib import markers
import matplotlib.pylab as plt

def main():
    velocities = [5000, 16250, 27500, 38750, 50000]
    runs = 5
    means = []
    stdevs = []
    lengthByVel= []
    for v in velocities:
        mean = 0
        lengths = []
        for i in range(runs):
            file = open(f'../results/Radiation/ej2_2-{v}_run{i}.json/dynamic.json')
            json_data = json.load(file)
            file.close()
            if(json_data['end'][0] == 'absorbed'):
                positions = zip(json_data["posX"], json_data["posY"])
                length = calculate_path_len(positions)
                lengths.append(length)
        lengthByVel.append((v , lengths))
        #mean /= runs
        #stdev = statistics.stdev(lengths)
        #means.append(mean)
        #stdevs.append(stdev)
    print(lengthByVel)
    bars , binEdges = np.histogram(lengthByVel[0][1] , bins=10)
    
    plt.hist(bars, bins=binEdges)
    plt.grid()
    plt.xlabel("Recorrido")
    plt.ylabel("Frecuencia")
    plt.show()

def calculate_path_len(positions) -> float:
    prev_pos = None
    total_dist = 0
    for pos in positions:
        if prev_pos is None:
            prev_pos = pos
            continue
        else:
            dist = math.dist(pos, prev_pos)
            prev_pos = pos
            total_dist += dist
    return total_dist
    

if __name__ == '__main__':
    main()