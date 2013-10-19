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

import eu.cloud4soa.api.datamodel.semantic.paas.Parameter;

/**
 *
 * @author vins
 */
public class ParameterInstance {
    private Parameter parameter;

    public ParameterInstance() {
        this.parameter=new Parameter();
    }

    public ParameterInstance(Parameter parameter) {
        this.parameter = parameter;
    }
    
    public ParameterInstance(String title, String description, Boolean isOptional, String value) {
        this();
        parameter.setTitle(title);
        parameter.setDescription(description);
        parameter.setIsOptional(isOptional);
        parameter.setValue(value);
    }

    public Parameter getParameter() {
        return parameter;
    }
    
    public String getUriId(){
        return parameter.getUriId();
    }
    
    public void setUriId(String uriId){
       parameter.setUriId(uriId);
    }
    
    public Boolean getIsOptional() {
        return parameter.getIsOptional();
    }
	
    public void setIsOptional( java.lang.Boolean isOptional ) {
        parameter.setIsOptional(isOptional);
    }
    
    public java.lang.String getValue() {
        return parameter.getValue();
    }

    public void setValue( java.lang.String value ) {
        parameter.setValue(value);
    }
    
    public java.lang.String getDescription() {
        return parameter.getDescription();
    }

    public void setDescription( java.lang.String description ) {
        parameter.setDescription(description);
    }

    public java.lang.String getTitle() {
        return parameter.getTitle();
    }

    public void setTitle( java.lang.String name ) {
        parameter.setTitle(name);
    }
}
