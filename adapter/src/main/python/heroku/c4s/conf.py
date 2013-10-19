import os

def conf():
    c4sherokuconf = dict()
    iniPath = '{0}/c4sheroku.ini'.format(os.getcwd())
    if os.path.exists(iniPath) :
        c4sherokuini_file = open(iniPath)
        for line in c4sherokuini_file.readlines():
            if line and not line.startswith("#"):
                key, value = line.split('=')
                c4sherokuconf[key]=value.rstrip()
                
        return c4sherokuconf
    else:
        raise Exception("Credentials file not found")
