import json
import math
import statistics
import matplotlib.pylab as plt

def main():
    plt.rcParams.update({'font.size': 20})

    velocities = [5000, 16250, 27500, 38750, 50000]
    runs = 200
    means = []
    stdevs = []
    for v in velocities:
        mean = 0
        lengths = []
        for i in range(runs):
            file = open(f'../SS-TP4/results/Radiation/a_ej2_2-{v}_run{i}/dynamic.json')
            json_data = json.load(file)
            file.close()
            
            positions = zip(json_data["posX"], json_data["posY"])
            length = calculate_path_len(positions)
            lengths.append(length)
            mean += length
        mean /= runs
        stdev = statistics.stdev(lengths)
        means.append(mean)
        stdevs.append(stdev)
    
    plt.figure(figsize=(8, 6))
    plt.plot(velocities, means, color='r')
    plt.errorbar(velocities, means, yerr=stdevs, fmt='o', color='r', ecolor='lightcoral', elinewidth=3, capsize=0)
    plt.xticks(velocities)
    plt.grid()
    plt.xlabel("Velocidades inciales (m/s)")
    plt.ylabel("Promedio de recorrido (m)")
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