from exceptions import KeyError
import json
'''
Created on 09.10.2012

@author: Denis Neuling (dn@cloudcontrol.de)
'''

class RequestBody():
    
    __content = None
    __asdict = dict()
    
    def __init__(self, content = None):
        self.__content = content
        if not self.__content is None:
            try:
                self.__asdict = json.loads(self.__content)
            except Exception, e:
                pass
        
    def get(self, key = None):
        if not key is None:
            try:
                return self.__asdict[key]
            except KeyError, k:
                return None
        else:
            return self.__content
        
    def put(self, key=None, value=None):
        if not key is None:
            self.__asdict[key]=value
        return self