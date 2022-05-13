from cProfile import label
import json
import math
import statistics
import numpy as np
from turtle import color
from matplotlib import markers
import matplotlib.pylab as plt

def main():
    plt.rcParams.update({'font.size': 20})

    velocities = [5000, 16250, 27500, 38750, 50000]
    runs = 200
    lengthByVel= []
    for v in velocities:
        mean = 0
        lengths = []
        for i in range(runs):
            file = open(f'../SS-TP4/results/Radiation/ej2_2-{v}_run{i}.json/dynamic.json')
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
    print(lengthByVel[0])
    
    total_bins = 20
    for vels in lengthByVel:
        histogram, bins = np.histogram(vels[1], bins=total_bins, range=(min(vels[1]), max(vels[1])))
        plt.plot(bins[1:], histogram/(len(vels[1])), label=f'Vel = {vels[0]}')
    
    # plt.hist(lengthByVel[0][1], bins=total_bins)
    plt.grid()
    plt.xlabel("Recorrido (m)")
    plt.ylabel("Probabilidad")
    plt.legend()
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