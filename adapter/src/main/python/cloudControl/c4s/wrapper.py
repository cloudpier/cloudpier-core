import json
from types import DictType
from types import StringType
from types import UnicodeType
from pycclib.cclib import ConnectionException, TokenRequiredError, BadRequestError, UnauthorizedError, ForbiddenError, NotFoundError, ConflictDuplicateError, GoneError, InternalServerError, NotImplementedError
from requests.exceptions import HTTPError 
import functools
import traceback

'''
Created on 31.01.2013

@author: Denis Neuling (dn@cloudcontrol.de)
'''

def wrapResponseMessage(dictionary):
    type(dictionary)
    if type(dictionary) is StringType or type(dictionary) is UnicodeType:
        return json.dumps(dict({'message':dictionary}))
    elif type(dictionary) is ProviderException:
        return json.dumps(dict({'message':dictionary.message})), dictionary.status
    elif issubclass(dictionary.__class__, BaseException):
        return wrapResponseMessage(ProviderException(dictionary))
    else:
        try:
            return json.dumps(dictionary)
        except Exception, e:
            print str(e)
            print traceback.format_exc()
            return wrapResponseMessage(ProviderException())


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


def failsafe():
    def failsafe_decorator(function):
        @functools.wraps(function)
        def wrapper(*args, **kwargs):
                try:
                    return function(*args, **kwargs)
                except Exception as exception:
                    return wrapResponseMessage(ProviderException(exception));
        return wrapper
    return failsafe_decorator


class ProviderException(Exception):
    
    status = 500
    message = 'Internal Server Error' 
    
    def __init__(self, exception=None):
        if not exception is None:
            err_map = {
                UnauthorizedError          : {'status':401, 'message': 'Unauthorized'},
                TokenRequiredError         : {'status':401, 'message': 'Unauthorized'},
                ForbiddenError             : {'status':403, 'message': 'Forbidden'},
                NotFoundError              : {'status':404, 'message': 'Not Found'},
                ConflictDuplicateError     : {'status':409, 'message': 'Conflict'},
                GoneError                  : {'status':410, 'message': 'Gone'},
                
                InternalServerError        : {'status':500, 'message': 'Internal Server Error'},
                NotImplementedError        : {'status':501, 'message': 'Not Implemented'},
                ConnectionException        : {'status':503, 'message': 'Service Unavailable'},
                
                HTTPError                  : {'status'  : exception.response.status_code if hasattr(exception, 'response') and hasattr(exception.response, 'status_code') else 503, 
                                              'message'     : exception.response.text if hasattr(exception, 'response') and hasattr(exception.response, 'text') else 'Service Unavailable' }
            }
            if type(exception) in err_map:
                self.status = err_map[type(exception)]['status']
                self.message = err_map[type(exception)]['message']
            elif type(exception) is BadRequestError:
                errors = exception.msgs.itervalues()
                self.message = ' '.join(errors)
                self.status = 400
            else:
                print str(exception)
                print traceback.format_exc()

