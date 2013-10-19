/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.api.datamodel.core.utilBeans;

import eu.cloud4soa.api.datamodel.semantic.paas.Operation;
import eu.cloud4soa.api.datamodel.semantic.paas.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import eu.cloud4soa.api.datamodel.semantic.paas.Exception;
import eu.cloud4soa.api.datamodel.semantic.paas.OperationType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "operationInstance")
public class OperationInstance {
    private Operation operation;

    public OperationInstance() {
        this.operation=new Operation();
        operation.setOperationType(new OperationType());
    }

    public OperationInstance(Operation operation) {
        this.operation = operation;
    }
    
    public OperationInstance(String title, String description, String command, String informationReturned) {
        this();
        operation.setTitle(title);
        operation.setDescription(description);
        operation.setOperationCommand(command);
        operation.setInformationReturned(informationReturned);
        operation.setOperationType(new OperationType());
    }

    public Operation getOperation() {
        return operation;
    }
        
    public String getUriId(){
        return operation.getUriId();
    }
    
    public void setUriId(String uriId){
       operation.setUriId(uriId);
    }
    
    public java.lang.String getInformationReturned() {
        return operation.getInformationReturned();
    }

    public void setInformationReturned( java.lang.String informationReturned ) {
        operation.setInformationReturned(informationReturned);
    }

    public java.lang.String getOperationCommand() {
        return operation.getOperationCommand();
    }

    public void setOperationCommand( java.lang.String operationCommand ) {
        operation.setOperationCommand(operationCommand);
    }

    public String getOperationTypeName() {
        return operation.getOperationType().getTitle();
    }

    public void setOperationTypeName( String operationTypeName ) {
            operation.getOperationType().setTitle(operationTypeName);
    }

    public List<ExceptionInstance> getThrownException() {
        List<Exception> thrownException = operation.getThrownException();
        List<ExceptionInstance> exceptionInstances = new ArrayList<ExceptionInstance>();
        for (Exception exception : thrownException) {
            exceptionInstances.add(new ExceptionInstance(exception));
        }
        return exceptionInstances;
    }
    
    public void setThrownException(List<ExceptionInstance> exceptionInstances) {
        for (ExceptionInstance exceptionInstance : exceptionInstances) {
            operation.getThrownException().add(exceptionInstance.getException());
        }
    }
    
    public void addThrownException(ExceptionInstance exceptionInstance){
        operation.getThrownException().add(exceptionInstance.getException());
    }

    public boolean removeThrownException(ExceptionInstance exceptionInstance) {
        return operation.getThrownException().remove(exceptionInstance.getException());
    }

    public java.lang.String getDescription() {
            return operation.getDescription();
    }

    public void setDescription( String description ) {
        operation.setDescription(description);
    }
    public java.lang.String getTitle() {
        return operation.getTitle();
    }

    public void setTitle( String title ) {
        operation.setTitle(title);
    }
    
    public Iterator<ParameterInstance> getRequiredParameters() {
        List<Parameter> requiredParameters = operation.getRequiredParameters();
        List<ParameterInstance> requiredParametersInstance = new ArrayList<ParameterInstance>();
        for (Parameter parameter : requiredParameters) {
            requiredParametersInstance.add(new ParameterInstance(parameter));
        }
        return requiredParametersInstance.iterator();
    }
    
    public void addRequiredParameter(ParameterInstance parameter){
        operation.getRequiredParameters().add(parameter.getParameter());
    }
    
    public boolean removeRequiredParameter(ParameterInstance parameter){
        return operation.getRequiredParameters().remove(parameter.getParameter());
    }
}
