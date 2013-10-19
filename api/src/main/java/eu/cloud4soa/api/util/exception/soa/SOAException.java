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
package eu.cloud4soa.api.util.exception.soa;

import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author vins
 */
public class SOAException extends Exception {

    Response.Status     responseStatus;

    public SOAException( Response.Status responseStatus, String message ) {
        super(message);
        this.responseStatus     = responseStatus;
    }
    
    
    
    public SOAException( Cloud4SoaException exception) {
        super();
        
        this.responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
        this.initCause( exception.getCause() );
        
    }

    
    
    public Status getResponseStatus() {
        return responseStatus;
    }

}

