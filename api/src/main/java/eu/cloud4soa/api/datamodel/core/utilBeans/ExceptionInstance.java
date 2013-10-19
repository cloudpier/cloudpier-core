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

import eu.cloud4soa.api.datamodel.semantic.paas.Exception;
/**
 *
 * @author vins
 */
public class ExceptionInstance {
    private Exception exception;
    
    public ExceptionInstance() {
        this.exception = new Exception();
    }

    public ExceptionInstance(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
    
    public ExceptionInstance(String title, String condition, String termsDescription) {
        this.exception = new Exception();
        exception.setCondition(condition);
        exception.setDescription(termsDescription);
        exception.setTitle(title);
    }
    
    public String getUriId(){
        return exception.getUriId();
    }
    
    public void setUriId(String uriId){
       exception.setUriId(uriId);
    }
    
    public java.lang.String getCondition() {
        return exception.getCondition();
    }

    public void setCondition( java.lang.String condition ) {
        exception.setCondition(condition);
    }

    public java.lang.String getDescription() {
        return exception.getDescription();
    }

    public void setDescription( java.lang.String description ) {
        exception.setDescription(description);
    }

    public java.lang.String getTitle() {
        return exception.getTitle();
    }

    public void setTitle( java.lang.String title ) {
        exception.setTitle(title);
    }
}
