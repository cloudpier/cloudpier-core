import os
import sys
from flask import Flask
from flask import request
import request_handler
import requests
import json
from types import DictType
from wrapper import *
from request_body import RequestBody


app = Flask(__name__)

@app.route('/c4s/ems/application/<application>/deployment/<deployment>/database/<database>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def database(application, deployment, database):
    try:
        handler = request_handler.Database_Handler()
        handler.authenticate(request.headers["Apikey"])
        
        response = {
            'POST': handler.create_database,
            'PUT' : handler.update_database,
            'GET': handler.database,
            'DELETE': handler.delete_database
        }[request.method](application, deployment, database, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/ems/application/<application>/deployment/<deployment>/database', methods=['GET'])
def list_database(application, deployment):
    try:
        handler = request_handler.Database_Handler()
        handler.authenticate(request.headers["Apikey"])
        
        response = handler.list_database(application, deployment, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/ems/application/<application>/deployment/<deployment>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def deployment(application, deployment):
    try:
        handler = request_handler.Deployment_Handler()
        handler.authenticate(request.headers["Apikey"])
        
        response = {
            'POST': handler.create_deployment,
            'PUT' : handler.update_deployment,
            'GET': handler.deployment,
            'DELETE': handler.delete_deployment
        }[request.method](application, deployment, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/ems/application/<application>/deployment', methods=['GET'])
def list_deployment(application):
    try:
        handler = request_handler.Deployment_Handler()
        handler.authenticate(request.headers['Apikey'])
        
        response = handler.list_deployment(application, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)

@app.route('/c4s/ems/application/<application>/operation/<operation>', methods=['POST'])
def operation(application, operation):
    
    try:
        handler = request_handler.Operation_Handler()
        handler.authenticate(request.headers["Apikey"])
        
        response = handler.operation(application, operation, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/ems/application/<application>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def application(application):
    try:
        handler = request_handler.Application_Handler()
        handler.authenticate(request.headers['Apikey'])
        
        response = {
            'POST': handler.create_application,
            'PUT' : handler.update_application,
            'GET': handler.application,
            'DELETE': handler.delete_application
        }[request.method](application, request.data)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/ems/application', methods=['GET'])
def list_application():
    try:
        handler = request_handler.Application_Handler()
        handler.authenticate(request.headers['Apikey'])
        
        response = handler.list_application(RequestBody(request.data))
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)

@app.route('/c4s/ems/sshkey', methods=['DELETE'])
def delete_sshkey():
    try:
        handler = request_handler.SSHKey_Handler()
        handler.authenticate(fromRequestHeaders('Apikey', request))
        
        requestBody = RequestBody()
        requestBody.put("sshKey", fromRequestHeaders('SshKey', request))
        
        return handler.delete_sshkey(requestBody)
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)    
        
    
@app.route('/c4s/ems/sshkey', methods=['GET', 'POST'])
def sshkey():
    try:
        handler = request_handler.SSHKey_Handler()
        handler.authenticate(fromRequestHeaders('Apikey',request))
        
        requestBody = {
            'GET' : RequestBody(), 
            'POST' : RequestBody(request.data)
        }[request.method]
        
        response = {
            'GET': handler.list_sshkey,
            'POST': handler.create_sshkey,
        }[request.method](requestBody)
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)

@app.route('/c4s/ems', methods=['GET'])
def ems():
    try:
        handler = request_handler.Common_Handler()
        
        '''
            If we need authentication here, we should enable this, otherwise not
        '''
        #handler.authenticate(request.headers["Apikey"])
        
        response = handler.ems(RequestBody(request.data))
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/monitor/detail', methods=['GET'])
def monitor_detail():
    try:
        handler = request_handler.Monitor_Handler()
        
        '''
            If we need authentication here, we should enable this, otherwise not
        '''
        #handler.authenticate(request.headers["Apikey"])
        
        response =  handler.detail()
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s/monitor', methods=['GET'])
def monitor():
    try:
        handler = request_handler.Common_Handler()
        
        '''
            If we need authentication here, we should enable this, otherwise not
        '''
        #handler.authenticate(request.headers["Apikey"])
        
        response = handler.monitor(RequestBody(request.data))
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


@app.route('/c4s', methods=['GET'])
def c4s():
    try:
        handler = request_handler.Common_Handler()
        
        '''
            If we need authentication here, we should enable this, otherwise not
        '''
        #handler.authenticate(request.headers["Apikey"])
        
        response = handler.c4s(RequestBody(request.data))
        return response.status_code if type(response) is requests.models.Response else response
    except Exception, e:
        return wrapResponseMessage(e), extractStatusCode(e)


# get rid of 404 favicon not found
@app.route('/favicon.ico', methods=['GET'])
def favicon():
    return ""
    
@app.errorhandler(404)
def resource_not_mapped(err):
    return wrapResponseMessage(err.message), 404

@app.errorhandler(500)
def internal_server_error(err):
    return wrapResponseMessage(err.message), 500
    
    
if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(host='0.0.0.0', port=port)
    
