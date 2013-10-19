import json
from flask import abort
import requests
from wrapper import *
from pycclib.cclib import Request, API
from request_body import RequestBody
import urllib

'''
Created on 31.01.2013

@author: Denis Neuling (dn@cloudcontrol.de)
'''

'''
    Handler base class
'''
class Adapter(API, object):

    def __init__(self):
        (Adapter, self).__init__()
        return

    def authenticate(self, b64creds=None):
        try:
            self.create_token(b64creds)
        except UnauthorizedError as unauthorizedError:
            abort(401)

    def create_token(self, base64):
        headers = {'authorization' : 'Basic {}'.format(base64)}
        request = Request()
        content = request.request(resource='/token/', method='POST', headers=headers)
        self.set_token(json.loads(content))
        return True

'''
    uncategorized stuff
'''
class Common_Handler(Adapter):

    def __init__(self):
        super(Common_Handler, self).__init__()

    @failsafe()
    def ems(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                 'description':'cloud4soa execution & management',
                 'version':'1.0',
                 'moduleName':'ems'
            }
        }), 200

    @failsafe()
    def monitor(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                'description':'cloud4soa monitoring',
                'version':'1.0',
                'moduleName':'monitor'
            }
        }), 200

    @failsafe()
    def c4s(self, requestBody=RequestBody()):
        return wrapResponseMessage({
            'module': {
                 'description':'cloud4soa',
                 'version':'1.0',
                 'moduleName':''
             }
        }), 200

'''
    Monitoring relating stuff
'''
class Monitor_Handler(Adapter):

    def __init__(self):
        Adapter.__init__(self)

    @failsafe()
    def detail(self):
        return wrapResponseMessage({'message':'everything is fine'}), 200

'''
    Operation relating stuff
'''
class Operation_Handler(Adapter):
    valid_operations = ["nop", "start", "stop"]

    def __init__(self):
        Adapter.__init__(self)

    @failsafe()
    def operation(self, applicationName=None, operation=None, requestBody=RequestBody()):
        return wrapResponseMessage({'message':'501 Not implemented'}), 501

'''
    Database relating stuff
'''
class Database_Handler(Adapter):

    def __init__(self):
        Adapter.__init__(self)

    @failsafe()
    def list_database(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        addonName = 'mysqls.free'
        mysqls_instance = self.read_addon(applicationName, deploymentName, addonName)
        databases = []
        if not mysqls_instance is None:
            databases.append({
                'database': {
                     'databaseName' : mysqls_instance['settings']['MYSQLS_DATABASE'],
                     'userName': mysqls_instance['settings']['MYSQLS_USERNAME'],
                     'password': mysqls_instance['settings']['MYSQLS_PASSWORD'],
                     'host': mysqls_instance['settings']['MYSQLS_HOSTNAME'],
                     'port': mysqls_instance['settings']['MYSQLS_PORT']
                }
            })
        return wrapResponseMessage(dict(databases=databases)), 200

    @failsafe()
    def database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        addonName = 'mysqls.free'
        mysqls_instance = self.read_addon(applicationName, deploymentName, addonName)
        return wrapResponseMessage({
            'database': {
                 'databaseName' : mysqls_instance['settings']['MYSQLS_DATABASE'],
                 'userName': mysqls_instance['settings']['MYSQLS_USERNAME'],
                 'password': mysqls_instance['settings']['MYSQLS_PASSWORD'],
                 'host': mysqls_instance['settings']['MYSQLS_HOSTNAME'],
                 'port': mysqls_instance['settings']['MYSQLS_PORT']
            }
        }), 200

    @failsafe()
    def create_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        addonName = 'mysqls.free'
        mysqls_instance = self.create_addon(applicationName, deploymentName, addonName)
        return wrapResponseMessage({
            'database': {
                 'databaseName' : mysqls_instance['settings']['MYSQLS_DATABASE'],
                 'userName': mysqls_instance['settings']['MYSQLS_USERNAME'],
                 'password': mysqls_instance['settings']['MYSQLS_PASSWORD'],
                 'host': mysqls_instance['settings']['MYSQLS_HOSTNAME'],
                 'port': mysqls_instance['settings']['MYSQLS_PORT']
            }
        }), 201

    @failsafe()
    def update_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        return wrapResponseMessage({'message':'501 Not implemented'}), 501

    @failsafe()
    def delete_database(self, applicationName=None, deploymentName=None, databaseName=None, requestBody=RequestBody()):
        addonName = 'mysqls.free'
        mysqls_instance = self.read_addon(applicationName, deploymentName, addonName)
        self.delete_addon(applicationName, deploymentName, addonName)
        return wrapResponseMessage({
            'database': {
                 'databaseName' : mysqls_instance['settings']['MYSQLS_DATABASE'],
                 'userName': mysqls_instance['settings']['MYSQLS_USERNAME'],
                 'password': mysqls_instance['settings']['MYSQLS_PASSWORD'],
                 'host': mysqls_instance['settings']['MYSQLS_HOSTNAME'],
                 'port': mysqls_instance['settings']['MYSQLS_PORT']
            },
            'message': 'success'
        }), 204


'''
    Deployment relating stuff
'''
class Deployment_Handler(Adapter):

    def __init__(self):
        Adapter.__init__(self)

    @failsafe()
    def list_deployment(self, applicationName=None, requestBody=RequestBody()):
        app = self.read_app(applicationName)
        deploymentList = []
        for dep in app['deployments']:
            deployment = self.read_deployment(applicationName, dep)
            deployment = dict({
                 'applicationName' : applicationName,
                 'deploymentName': deployment['name'],
                 'subdomain': deployment['default_subdomain'],
                 'state' : deployment['state']
            })
            deploymentList.append(deployment)
        return wrapResponseMessage(dict(deployments=deploymentList)), 200

    @failsafe()
    def deployment(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        deployment = self.read_deployment(applicationName, deploymentName)
        return wrapResponseMessage({
            'deployment': {
                 'applicationName' : applicationName,
                 'deploymentName': deploymentName,
                 'subdomain': deployment['default_subdomain'],
                 'state' : deployment['state']
            }
        }), 200

    @failsafe()
    def create_dep(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        self.create_deployment(applicationName, deploymentName)
        deployment = self.read_deployment(applicationName, deploymentName)
        return wrapResponseMessage({
            'deployment': {
                 'applicationName' : applicationName,
                 'deploymentName': deploymentName,
                 'subdomain': deployment['default_subdomain'],
                 'state' : deployment['state']
            }
        }), 201

    @failsafe()
    def update_dep(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        self.update_deployment(app_name=applicationName, deployment_name=deploymentName)
        deployment = self.read_deployment(applicationName, deploymentName)
        return wrapResponseMessage({
            'deployment': {
                 'applicationName' : applicationName,
                 'deploymentName': deploymentName,
                 'subdomain': deployment['default_subdomain'],
                 'state' : deployment['state']
            }
        }), 200

    @failsafe()
    def delete_dep(self, applicationName=None, deploymentName=None, requestBody=RequestBody()):
        deployment = self.read_deployment(applicationName, deploymentName)
        deleted = self.delete_deployment(applicationName, deploymentName)
        return wrapResponseMessage({
            'deployment': {
                 'applicationName' : applicationName,
                 'deploymentName': deploymentName,
                 'subdomain': deployment['default_subdomain'],
                 'state' : 'not deploymend' if deleted else 'deployed'
            },
            'message' : 'success'
        }), 204

'''
    Application relating stuff
'''
class Application_Handler(Adapter):

    def __init__(self):
        super(Application_Handler, self).__init__()

    @failsafe()
    def list_application(self, requestBody=RequestBody()):
        apps = self.read_apps()
        applicationList = []
        for application in apps:
            application = self.read_app(application['name'])
            applicationModel = dict({
               'applicationName': application['name'],
               'created': application['date_created'],
               'url' : '{}.cloudcontrolled.com'.format(application['name']),
               'modified' : application['date_modified'],
               'repository': application['repository'],
               'language': application['type']['name']
            })
            applicationList.append(applicationModel)
        return wrapResponseMessage(dict(applications=applicationList)), 200

    @failsafe()
    def application(self, applicationName=None, requestBody=RequestBody()):
        application = self.read_app(applicationName);
        app = dict({
           'applicationName': application['name'],
           'created': application['date_created'],
           'modified' : application['date_modified'],
           'repository': application['repository'],
           'language': application['type']['name']
        })
        return wrapResponseMessage({'application':app}), 200

    @failsafe()
    def create_application(self, applicationName=None, requestBody=RequestBody()):
        app = self.create_app(applicationName, requestBody.get('language'), 'git')
        return wrapResponseMessage(
            dict(
                 application={
                    'applicationName': app['name'],
                    'created': app['date_created'],
                    'url' : '{}.cloudcontrolled.com'.format(app['name']),
                    'modified' : app['date_modified'],
                    'repository': app['repository'],
                    'language': app['type']['name']
                 }
            )
        ), 201

    @failsafe()
    def update_application(self, applicationName=None, requestBody=RequestBody()):
        return wrapResponseMessage({'message':'501 Not implemented'}), 501

    @failsafe()
    def delete_application(self, applicationName=None, requestBody=RequestBody()):
        response = self.delete_app(applicationName)
        return wrapResponseMessage({'message':response}), 204


class SSHKey_Handler(Adapter):

    def __init__(self):
        super(SSHKey_Handler, self).__init__()

    @failsafe()
    def list_sshkey(self, requestBody=RequestBody()):
        applicationName = requestBody.get('applicationName')
        if applicationName:
            app = self.read_app(applicationName)
            users = app['users']
            user = users[0]
            keys = self.read_user_keys(user['username'])
            sshKeysList = []
            for key in keys:
                affectedKey = self.read_user_key(user['username'], key['key_id'])
                sshKey = dict({
                   'sshKey': affectedKey['key'],
                   'owner': user['email']
                })
                sshKeysList.append(sshKey)
            return wrapResponseMessage(dict(sshKeys=sshKeysList)), 200
        else:
            return wrapResponseMessage("Bad Request"), 400

    @failsafe()
    def create_sshkey(self, requestBody=RequestBody()):
        sshKey = requestBody.get('sshKey')
        if sshKey:
            user = self.read_users()[0]
            self.create_user_key(user['username'], sshKey)
            sshKey = dict({
               'sshKey': sshKey,
               'owner': user['email']
            })
            return wrapResponseMessage(dict(sshKey=sshKey)), 201
        else:
            return wrapResponseMessage("Bad Request"), 400

    @failsafe()
    def delete_sshkey(self, requestBody=RequestBody()):
        applicationName = requestBody.get('applicationName')
        sshKey = requestBody.get('sshKey')
        if applicationName and  sshKey:
            app = self.read_app(applicationName)
            username = app['users'][0]['username']
            keys = self.read_user_keys(username)
            deleted = False
            for key_id_ob in keys:
                key_object = self.read_user_key(username, key_id_ob['key_id'])
                affectedKey = key_object['key']
                if affectedKey == sshKey:
                    deleted = self.delete_user_key(username, key_id_ob['key_id'])
                    break
            if deleted:
                sshKey = dict({'sshKey': sshKey, 'owner': app['users'][0]['email']})
                return wrapResponseMessage(dict(sshKey=sshKey)), 204
            else:
                raise GoneError()
        else:
            return wrapResponseMessage("Bad Request"), 400

