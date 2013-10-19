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

import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.semantic.user.Cloud4SoaAccount;

/**
 *
 * @author vins
 */
public class Cloud4SoaAccountInstance {
    private Cloud4SoaAccount cloud4SoaAccount;

    public Cloud4SoaAccountInstance(Cloud4SoaAccount cloud4SoaAccount) {
        this.cloud4SoaAccount = cloud4SoaAccount;
    }

    public Cloud4SoaAccountInstance() {
        this(new Cloud4SoaAccount());
    }

    //First Level of Indirection  
    /*---------- Cloud4SoaAccount ----------*/
    public Cloud4SoaAccount getCloud4SoaAccount() {
        return cloud4SoaAccount;
    }
    
    @SemanticRelation(semanticClass = Cloud4SoaAccount.class, methodName = "getAccountname")
    public String getAccountname(){
        return cloud4SoaAccount.getAccountname();
    }

    public void setAccountname(String  accountname) {
        this.cloud4SoaAccount.setAccountname(accountname);
    }
    
    @SemanticRelation(semanticClass = Cloud4SoaAccount.class, methodName = "getUriId")
    public String getUriId(){
        return cloud4SoaAccount.getUriId();
    }

    public void setUriId(String  uriId) {
        this.cloud4SoaAccount.setUriId(uriId);
    }
    
    
}
