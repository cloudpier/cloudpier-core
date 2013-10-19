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

'''
Created on 31.01.2013

@author: Denis Neuling (dn@cloudcontrol.de)
'''

app = Flask(__name__)

@app.route('/c4s/ems/application/<application>/deployment/<deployment>/database/<database>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def database(application, deployment, database):
    handler = request_handler.Database_Handler()
    handler.authenticate(request.headers["Apikey"])
    response = {
        'POST': handler.create_database,
        'PUT' : handler.update_database,
        'GET': handler.database,
        'DELETE': handler.delete_database
    }[request.method](application, deployment, database, request.data)
    return response


@app.route('/c4s/ems/application/<application>/deployment/<deployment>/database', methods=['GET'])
def list_database(application, deployment):
    handler = request_handler.Database_Handler()
    handler.authenticate(request.headers["Apikey"])
    response = handler.list_database(application, deployment, request.data)
    return response


@app.route('/c4s/ems/application/<application>/deployment/<deployment>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def deployment(application, deployment):
    handler = request_handler.Deployment_Handler()
    handler.authenticate(request.headers["Apikey"])
    response = {
        'POST': handler.create_dep,
        'PUT' : handler.update_dep,
        'GET': handler.deployment,
        'DELETE': handler.delete_dep
    }[request.method](application, deployment, request.data)
    return response


@app.route('/c4s/ems/application/<application>/deployment', methods=['GET'])
def list_deployment(application):
    handler = request_handler.Deployment_Handler()
    handler.authenticate(request.headers['Apikey'])
    response = handler.list_deployment(application, request.data)
    return response.status_code if type(response) is requests.models.Response else response

@app.route('/c4s/ems/application/<application>/operation/<operation>', methods=['POST'])
def operation(application, operation):
    handler = request_handler.Operation_Handler()
    handler.authenticate(request.headers["Apikey"])
    response = handler.operation(application, operation, request.data)
    return response


@app.route('/c4s/ems/application/<application>', methods=['GET', 'POST', 'PUT', 'DELETE'])
def application(application):
    handler = request_handler.Application_Handler()
    handler.authenticate(request.headers['Apikey'])
    response = {
        'POST': handler.create_application,
        'PUT' : handler.update_application,
        'GET': handler.application,
        'DELETE': handler.delete_application
    }[request.method](application, RequestBody(request.data))
    return response


@app.route('/c4s/ems/application', methods=['GET'])
def list_application():
    handler = request_handler.Application_Handler()
    handler.authenticate(request.headers['Apikey'])
    response = handler.list_application(RequestBody(request.data))
    return response


@app.route('/c4s/ems/sshkey', methods=['DELETE'])
def delete_sshkey():
    handler = request_handler.SSHKey_Handler()
    handler.authenticate(fromRequestHeaders('Apikey', request))
    requestBody = RequestBody(request.data)
    requestBody.put("sshKey", fromRequestHeaders('SshKey', request))
    requestBody.put("applicationName", fromRequestHeaders('Applicationname', request))
    return handler.delete_sshkey(requestBody)


@app.route('/c4s/ems/sshkey', methods=['GET', 'POST'])
def sshkey():
    handler = request_handler.SSHKey_Handler()
    handler.authenticate(fromRequestHeaders('Apikey',request))
    requestBody = {
        'GET' : RequestBody(), 
        'POST' : RequestBody(request.data)
    }[request.method]
    requestBody.put("applicationName", fromRequestHeaders('Applicationname', request))
    response = {
        'GET': handler.list_sshkey,
        'POST': handler.create_sshkey,
    }[request.method](requestBody)
    return response


@app.route('/c4s/ems', methods=['GET'])
def ems():
    handler = request_handler.Common_Handler()
    response = handler.ems(RequestBody(request.data))
    return response


@app.route('/c4s/monitor/detail', methods=['GET'])
def monitor_detail():
    handler = request_handler.Monitor_Handler()
    response =  handler.detail()
    return response


@app.route('/c4s/monitor', methods=['GET'])
def monitor():
    handler = request_handler.Common_Handler()
    response = handler.monitor(RequestBody(request.data))
    return response


@app.route('/c4s', methods=['GET'])
def c4s():
    handler = request_handler.Common_Handler()
    response = handler.c4s(RequestBody(request.data))
    return response


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

