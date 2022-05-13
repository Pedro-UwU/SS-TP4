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
    topLong = []
    topShort = []
    for v in velocities:
        mean = 0
        lengths = []
        longestFilename = ''
        longest = 0
        shortestFilename = ''
        shortest = 100
        for i in range(runs):
            file = open(f'../SS-TP4/results/Radiation/a_ej2_2-{v}_run{i}/dynamic.json')
            json_data = json.load(file)
            file.close()
            if(json_data['end'][0] == 'absorbed'):
                positions = zip(json_data["posX"], json_data["posY"])
                length = calculate_path_len(positions)
                if length > longest:
                    longest = length
                    longestFilename = f'ej2_2-{v}_run{i}.json/dynamic.json'
                if shortest > length:
                    shortest = length
                    shortestFilename = f'ej2_2-{v}_run{i}.json/dynamic.json'
                lengths.append(length)
        lengthByVel.append((v , lengths))
        topLong.append((longest , longestFilename))
        topShort.append((shortest , shortestFilename))
        #mean /= runs
        #stdev = statistics.stdev(lengths)
        #means.append(mean)
        #stdevs.append(stdev)
    print(lengthByVel[0])
    print(topLong)
    print(topShort)
    total_bins = 7
    for vels in lengthByVel:
        histogram, bins = np.histogram(vels[1], bins=total_bins, range=(min(vels[1]), max(vels[1])))
        plt.plot(bins[1:], histogram/(len(vels[1])), label=f'Vel = {vels[0]}m/s')
    
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