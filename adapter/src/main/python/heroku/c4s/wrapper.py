import json
#from types import 
from types import DictType
from types import StringType
from types import UnicodeType


def wrapResponseMessage(dictionary):
    if type(dictionary) is StringType or type(dictionary) is UnicodeType:
        return json.dumps(dict({'message':dictionary}))
    elif not type(dictionary) is DictType and issubclass(dictionary.__class__, Exception):
        return json.dumps(dict({'message':dictionary.message}))
    else:
        try:
            dir(dictionary)
            return json.dumps(dictionary)
        except Exception, e:
            wrapResponseMessage(e)

def extractStatusCode(exception):
    statusCode = 500
    if hasattr(exception, 'status') and not exception.status is None:
        statusCode = exception.status

    return statusCode


def fromRequestHeaders(key = None, request = None):
    if request is None or request.headers is None or key is None:
        return None
    else:
        try:
            return request.headers[key]
        except KeyError:
            return None
