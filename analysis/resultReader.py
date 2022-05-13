
import json
from os import listdir
from os.path import isfile, join
from tracemalloc import Snapshot
from typing import Dict
import matplotlib.pyplot as pl
import re


class ResultReader:
    def __init__(self):
        print("Reader ready")


    @staticmethod
    def parse_ej2_1(path , regex = '.*'):
        folders = [ folder for folder in listdir(path) if re.match(regex  , folder )]
        #folders = listdir(path)
        print(folders)
        runs = []
        
        for foldername in folders:
            run = dict()
            with open(f'{path}/{foldername}/static.json') as json_file:
                data = json.load(json_file)
                run['Q'] = data['Q']
                run['D'] = data['D']
                run['k'] = data['k']
                run['delta_t'] = data['delta_t']
                run['M'] = data['M']
                json_file.close()
                #print(run)
                
            with open(f'{path}/{foldername}/dynamic.json') as json_file:

                data = json.load(json_file)
                pos = tuple(zip(data['posX'],data['posY']))
                vel = tuple(zip(data['velX'],data['velY']))
                snapshots = tuple(zip(pos,vel,data['t']))
                run['snapshots'] = snapshots
                runs.append(run)
                json_file.close()
        return runs
