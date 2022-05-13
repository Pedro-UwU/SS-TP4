from cProfile import label
import matplotlib.pyplot as plt
import json

def main():
    plt.rcParams.update({'font.size': 20})
    errors = {}
    integrations = ['verlet', 'gear', 'beeman']
    steps = [1, 2, 3, 4, 5, 6]
    for integration in integrations:
        errors[integration] = []
        for i in steps:
            analytic = open(f'../SS-TP4/results/Oscillation/sim_e{i}_analytic/dynamic.json')
            data = open(f'../SS-TP4/results/Oscillation/sim_e{i}_{integration}/dynamic.json')

            pos_a = json.load(analytic)["pos"]
            data_json = json.load(data)
            data.close()
            analytic.close()
            t = data_json["t"]
            pos = data_json["pos"]

            error = calculate_error(pos, pos_a)
            errors[integration].append(error)
    print(errors)

    fig, ax = plt.subplots()
    steps = [-x for x in steps]
    for integration in integrations:
        plt.plot(steps, errors[integration], label=integration)
    
    ax.set_yscale('log')
    ax.invert_xaxis()
    plt.xlabel("Exponente del delta t")
    plt.ylabel("Error cuadrático medio")
    plt.legend()
    
    plt.show()
            
def calculate_error(data, analytic):
    error = 0
    n = 0
    for i, value in enumerate(data):
        real = analytic[i]
        current_error = (real - value) ** 2
        error += current_error
        n += 1
    return error/n

if __name__ == '__main__':
    main()