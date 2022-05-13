from cProfile import label
from distutils.log import error
import enum
import math
import numpy as np
from tracemalloc import Snapshot
import matplotlib.pyplot as plt
import json
from resultReader import ResultReader 
def main():
    plt.rcParams.update({'font.size': 20})
    exponentes = [ 15 , 16 , 17 , 18 ]
    runs = dict()
    boardFile = open("../board.json")
    boardJSON = json.load(boardFile)
    boardPos=tuple(zip(boardJSON['posx'] , boardJSON['posy']))
    board = tuple(zip(boardPos , boardJSON['charges']))
    print(board[0])
    print(board[1])
    for e in exponentes:
        runs[e] = ResultReader.parse_ej2_1(f"../results/Radiation" , f"ej2_1-t{e}")

    promedioPorDt = []
    for t in runs:
        diferencias = []
        for r in runs[t]:
            k = r['k']
            q = r['Q']
            mass = r['M']
            p = r['snapshots']
            initialEnergy = getEnergy(p[0] , k , q , mass , board )
            deltaEnergy = abs(getEnergy(p[-1] , k , q , mass , board ) - initialEnergy)
            diferencias.append(deltaEnergy/initialEnergy)
        promedioPorDt.append(diferencias)
    
    deltas = [ 10**-x for x in exponentes]
    errors = [ np.std(p)/math.sqrt(len(p)) for p in promedioPorDt]
    promedios = [np.mean(p) for p in promedioPorDt]
    print(deltas)
    
    plt.errorbar( deltas, promedios , fmt='-o' , yerr=errors , ecolor = "m" , capsize=5)
    plt.xlabel('Delta(s)')
    plt.ylabel('Diferencia relativa')
    plt.xscale('log')
    plt.yscale('log')
    plt.grid(alpha=0.25)
    plt.show()
    

    return 0

def getEnergy(particle , k , q , mass , board):

    return pE(board , particle , k , q) + kE(particle , mass)
def getInitialEnergy(run  , board):
    print(len(run['snapshots']))
    particle = run['snapshots'][0]
    k = run['k']
    q = run['Q']
    mass = run['M']
    return pE(board , particle , k , q) + kE(particle , mass)

def pE(charges , p , q , k ):
    energy = 0
    for c in charges:
        cx= c[0][0]
        cy= c[0][1]
        px = p[0][0]
        py = p[0][1]
        distance = math.sqrt(((cx - px)**2 + (cy - py)**2 ))
        energy += c[1] / distance
    return energy * k * q

def kE( p , m ):
    vx = p[1][0]
    vy = p[1][1]
    v = math.sqrt(vx**2 + vy**2)
    return 0.5 * m * v *v

if __name__ == "__main__":
    main()