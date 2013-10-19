import json
from datetime import datetime
from types import DictType
from heroku.models import *
import heroku
import requests
from wrapper import wrapResponseMessage
from requests.exceptions import HTTPError
from request_body import RequestBody
import urllib 

'''
    Handler base class
'''
class Adapter(heroku.api.Heroku):
    
    _configuration = None
    _apiKey = None
     
    def __init__(self):
        super(Adapter, self).__init__()
        return
#        self.connect()

    def authenticate(self, apiKey=None):
        self._apiKey = apiKey
        self.use_api_key()
        return

    def use_api_key(self):
        try:
            verified = super(Adapter, self).authenticate(self._apiKey)
            if not verified:
                self.raise_Error()    
        except HTTPError, he:
            self.raise_Error(he)
        
    def raise_Error(self, http_error=None):
        e = Exception('Authentication Failure')
        if(http_error is None):
            # apply status_code
            e.status = 401
            raise e
        else:
            if hasattr(http_error.response, 'status_code'):
                e.status = http_error.response.status_code
            raise e

'''
    uncategorized stuff
'''
class Common_Handler(Adapter):
    
    #__adapter = adapter.Adapter()
    
    def __init__(self):
        super(Common_Handler, self).__init__()

    def ems(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                 'description':'cloud4soa execution & management',
                 'version':'1.0',
                 'moduleName':'ems'
            }
        })
    
    def monitor(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                'description':'cloud4soa monitoring',
                'version':'1.0',
                'moduleName':'monitor'
            }
        })
    
    def c4s(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                 'description':'cloud4soa',
                 'version':'1.0',
                 'moduleName':''
             }
        })

'''
    Monitoring relating stuff
'''
class Monitor_Handler(Adapter):
    
    def __init__(self):
        Adapter.__init__(self)
    
    def detail(self):
        return wrapResponseMessage({'message':'everything is fine'});

'''
    Operation relating stuff
'''
class Operation_Handler(Adapter):
    
    valid_operations = ["nop", "start", "stop"]
    
    def __init__(self):
        Adapter.__init__(self)

    def operation(self, applicationName=None, operation=None, requestBody=RequestBody()):
        try:
            if operation in self.valid_operations:
                message = 'success'
                if not operation is "nop":
                    state = {
                        'start': "0",
                        'stop' : "1",
                    }[operation]
                    response = self._http_resource(method='POST', resource=('apps', applicationName ,'server/maintenance'), params={'app': applicationName ,'maintenance_mode': state}, data={'app': applicationName ,'maintenance_mode': state})
                    if not response.ok:
                        message = response.text
                
                return wrapResponseMessage({'message':message})
            else:
                return wrapResponseMessage({'message':'Bad Request'}), 400
        except Exception, e:
            return wrapResponseMessage(e);

'''
    Database relating stuff
'''
class Database_Handler(Adapter):
    
    def __init__(self):
        Adapter.__init__(self) 
    
    def list_database(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        try:
            #redistogo:nano
            response = self._get_resource(resource=('apps', applicationName), obj=heroku.models.App)
            redis = response.addons.get('redistogo:nano')
            
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        redistogo = 'redistogo:nano'
        try:
            response = self._http_resource(method='GET', resource=('apps', applicationName, 'addons', redistogo))
            
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def create_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        redistogo = 'redistogo:nano'
        
        try:
            response = self._http_resource(method='POST', resource=('apps', applicationName, 'addons', redistogo))
            
            if response.ok:
                return wrapResponseMessage({
                    'database': {
                        'databaseName':redistogo,
                        'userName':'',
                        'password':'',
                        'host':'',
                        'port':'',
                    }
                })
            else:
                return wrapResponseMessage({'message':response.reason}), 500

        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e
    
    def update_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def delete_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        redistogo = 'redistogo:nano'
        
        try:
            response = self._http_resource(method='DELETE', resource=('apps', applicationName, 'addons', redistogo))

            if response.ok:
                return wrapResponseMessage({
                    'database': {
                        'databaseName':redistogo,
                        'userName':'',
                        'password':'',
                        'host':'',
                        'port':'',
                    },
                    'message': 'success'
                })
            else:
                return wrapResponseMessage({'message':response.reason}), 500
            
        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e

'''
    Deployment relating stuff
'''
class Deployment_Handler(Adapter):
    
    def __init__(self):
        Adapter.__init__(self) 
    
    def list_deployment(self, applicationName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def deployment(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def create_deployment(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);
    
    def update_deployment(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);

    def delete_deployment(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        try:
            return wrapResponseMessage({'message':'501 Not implemented'}), 501
        except Exception, e:
            return wrapResponseMessage(e);

'''
    Application relating stuff
'''
class Application_Handler(Adapter):
    
    def __init__(self):
        super(Application_Handler, self).__init__()
        
    def list_application(self, requestBody=RequestBody()):
        try:
            herokuApps = self.apps
        
            applicationList = []
            for application in herokuApps:
                applicationModel = dict({
                   'applicationName': application.name,
                   'created': application.created_at.isoformat(),
                   'modified' : None,
                   'repository': None,
                   'language': None
                })
                applicationList.append(applicationModel)
            return wrapResponseMessage(dict(applications=applicationList))
        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e
    
    def application(self, applicationName=None, requestBody=RequestBody()):
        try:
            response = self._http_resource(method='GET',resource=('apps', applicationName))
            
            if response.ok:
                response = response.json
                
                application = dict({
                   'applicationName':   response['name']                            if 'name' in response                             else None,
                   'created':           response['created_at']                      if 'created_at' in response                       else None,
                   'repository' :       response['git_url']                         if 'git_url' in response                          else None,
                   'modified' :         response['updated_at']                      if 'updated_at' in response                       else None,
                   'language' :         response['buildpack_provided_description']  if 'buildpack_provided_description' in response   else None
                })
                
                return wrapResponseMessage({'application':application})
            else:
                return wrapResponseMessage({'message':response.reason}), 500
        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e
        except Exception, e :
            return wrapResponseMessage(e)
    
    def create_application(self, applicationName=None, requestBody=RequestBody()):
        try:
            response = self._http_resource(method='POST', resource=('apps'), data={'app[name]':applicationName, 'app[stack]':'cedar'})
            response = response.json

            return wrapResponseMessage(
                dict(
                     application={
                                 'applicationName': response['name']                            if 'name' in response                             else None,
                                 'created':         response['created_at']                      if 'created_at' in response                       else None,
                                 'url' :            response['web_url']                         if 'web_url' in response                          else None,
                                 'modified':        response['updated_at']                      if 'updated_at' in response                       else None,
                                 'repository':      response['git_url']                         if 'git_url' in response                          else None,
                                 'language' :       response['buildpack_provided_description']  if 'buildpack_provided_description' in response   else None  
                                 }
                    )
            )
        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e                                           
        except Exception, e:
            return wrapResponseMessage(e);

    
    def update_application(self, applicationName=None, requestBody=RequestBody()):
        return wrapResponseMessage({'message':'501 Not implemented'}), 501
    
    def delete_application(self, applicationName=None, requestBody=RequestBody()):
        try:
            herokuApps = self.apps
            for application in herokuApps:
                if application.name == applicationName:
                    try:
                        response = application.destroy()
                        return wrapResponseMessage({'message':response}) 
                    except Exception, e:
                        return wrapResponseMessage(e);
    
            return wrapResponseMessage({'message':'Not found.'}), 404
        except requests.exceptions.HTTPError, he:
            e = Exception(he.response.text)
            if hasattr(he.response, 'status_code'):
                e.status = he.response.status_code
            raise e


class SSHKey_Handler(Adapter):
    
    def __init__(self):
        super(SSHKey_Handler, self).__init__()
        
    def list_sshkey(self, requestBody=RequestBody()):
        response = self._http_resource(method='GET', resource=('user', 'keys'))
        if response.ok:
            sshKeys = response.json
            sshKeysList = []
            for sshKey in sshKeys:
                sshKey = dict({
                   'sshKey': sshKey['contents'],
                   'owner': sshKey['email']
                })
                sshKeysList.append(sshKey)
            return wrapResponseMessage(dict(sshKeys=sshKeysList))
        else:
            return wrapResponseMessage({'message':response.reason}), 500

    def create_sshkey(self, requestBody=RequestBody()):
        sshKey = requestBody.get('sshKey')

        if not sshKey is None:
            newKey = heroku.models.Key()
            newKey._h = self
            newKey = newKey.new(sshKey)

            sshKey = dict({
               'sshKey': newKey.contents,
               'owner': newKey.email
            })
            return wrapResponseMessage(dict(sshKey=sshKey))
        else:
            return wrapResponseMessage(Exception('Bad Request')), 400

    def delete_sshkey(self, requestBody=RequestBody()):
        sshKey = requestBody.get('sshKey')

        if not sshKey is None:
            key_id = sshKey.split()[-1]

            affectedKey = self.keys.get(key_id);
            
            if not affectedKey is None:
                affectedKey.delete();
                
                sshKey = dict({
                   'sshKey': affectedKey.contents,
                   'owner': affectedKey.email
                })
                return wrapResponseMessage(dict(sshKey=sshKey))
            else: 
                return wrapResponseMessage(Exception('Gone')), 410
        else:
            return wrapResponseMessage(Exception('Bad Request')), 400
