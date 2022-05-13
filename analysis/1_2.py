from cProfile import label
from distutils.log import error
import enum
import matplotlib.pyplot as plt
import json

def main():
    verlet = open('../SS-TP4/results/Oscillation/Verlet/dynamic.json')
    beeman = open('../SS-TP4/results/Oscillation/Beeman/dynamic.json')
    gear = open('../SS-TP4/results/Oscillation/Gear/dynamic.json')
    analytic = open('../SS-TP4/results/Oscillation/Analytic/dynamic.json')

    data_verlet = json.load(verlet)
    data_beeman = json.load(beeman)
    data_gear = json.load(gear)
    data_analytic = json.load(analytic)

    t = data_analytic["t"]
    pos_verlet = data_verlet["pos"]
    pos_beeman = data_beeman["pos"]
    pos_gear = data_gear["pos"]
    pos_analytic = data_analytic["pos"]

    plt.plot(t, pos_verlet, label='Verlet')
    plt.plot(t, pos_beeman, label='Beeman')
    plt.plot(t, pos_gear, label='Gear')
    plt.plot(t, pos_analytic, label='Analytic')
    plt.legend()
    plt.show()

    errors = {}
    errors['Verlet'] = calculate_error(pos_verlet, pos_analytic)
    errors['Beeman'] = calculate_error(pos_beeman, pos_analytic)
    errors['Gear'] = calculate_error(pos_gear, pos_analytic)


    fig, ax = plt.subplots()
    x = 3
    names = errors.keys()
    values = errors.values()

    plt.bar(names, values)
    ax.set_yscale('log')
    plt.show()
    print(errors)


def calculate_error(data, analytic):
    error = 0
    n = 0
    for i, value in enumerate(data):
        real = analytic[i]
        current_error = (real - value) ** 2
        error += current_error
        n += 1
    return error/n

if __name__ == "__main__":
    main()