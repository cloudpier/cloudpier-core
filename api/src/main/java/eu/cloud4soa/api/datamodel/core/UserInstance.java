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

package eu.cloud4soa.api.datamodel.core;

import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.core.utilBeans.Cloud4SoaAccountInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.DeveloperInstance;
import eu.cloud4soa.api.datamodel.core.utilBeans.PaaSUserInstance;
import eu.cloud4soa.api.datamodel.semantic.foaf.FoafAgent;
import eu.cloud4soa.api.datamodel.semantic.user.Cloud4SoaAccount;
import eu.cloud4soa.api.datamodel.semantic.user.User;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "userInstance", namespace="eu.cloud4soa.api.datamodel.core")
@XmlSeeAlso({DeveloperInstance.class, PaaSUserInstance.class})
public class UserInstance{
    protected User user;
    protected Cloud4SoaAccountInstance cloud4SoaAccountInstance;

    public UserInstance() {
        user = new User();
        cloud4SoaAccountInstance = new Cloud4SoaAccountInstance();
        user.setHoldsaccount(cloud4SoaAccountInstance.getCloud4SoaAccount());
//        user.setBirthday(Calendar.getInstance());
    }
    
    public UserInstance(User user) {
        this.user = user;
        cloud4SoaAccountInstance = new Cloud4SoaAccountInstance((Cloud4SoaAccount)user.getHoldsaccount());
    }
    
    public User getUser() {
            return user;
    }
    
    //First Level of Indirection  
    @SemanticRelation(semanticClass=User.class, methodName="getUriId")
    public String getUriId() {
            return user.getUriId();
    }
    
    public void setUriId(String uriId) {
            user.setUriId(uriId);
    }

    /*---------- Inherited from Person ----------*/	 
    @SemanticRelation(semanticClass=User.class, methodName="getFamilyname")
    public java.lang.String getFamilyname() {
            return user.getFamilyname();
    }

    public void setFamilyname( java.lang.String familyname ) {
            user.setFamilyname(familyname);
    }
	
    @SemanticRelation(semanticClass=User.class, methodName="getFirstName")
    public java.lang.String getFirstName() {
            return user.getFirstName();
    }

    public void setFirstName( java.lang.String firstName ) {
            user.setFirstName(firstName);
    }
	
    @SemanticRelation(semanticClass=User.class, methodName="getGeekcode")
    public java.lang.String getGeekcode() {
            return user.getGeekcode();
    }
	
    public void setGeekcode( java.lang.String geekcode ) {
            user.setGeekcode(geekcode);
    }

	
    @SemanticRelation(semanticClass=User.class, methodName="getSurname")
    public java.lang.String getSurname() {
            return user.getSurname();
    }

    public void setSurname( java.lang.String surname ) {
            user.setSurname(surname);
    }
    
    @SemanticRelation(semanticClass=FoafAgent.class, methodName="getPersonalmailbox")
    public java.lang.String getPersonalmailbox() {
            return user.getPersonalmailbox();
    }

    public void setPersonalmailbox( java.lang.String mailbox ) {
            user.setPersonalmailbox(mailbox);
    }
        
    /*---------- Inherited from Agent ----------*/	 
    @SemanticRelation(semanticClass=User.class, methodName="getBirthday")
    public Date getBirthday() {
            return user.getBirthday();
    }

    public void setBirthday( Date birthday ) {
            user.setBirthday(birthday);
    }
    /*    Missing:
        Document openid;
	Thing personalmailbox;
	java.lang.String sha1sumofapersonalmailboxURIname;
     */

    //Second Level of Indirection  
    //---    Remember: catch nullPointerException... ---
    //---    Remember: add a getUri for every object obtained by indirection... ---
    //---    Remember: add a Designed getter from every object obtained by indirection... ---
    
    /*---------- Cloud4SoaAccount ----------*/	 
    public Cloud4SoaAccountInstance getHoldsaccount() {
            return cloud4SoaAccountInstance;
    }
    
    public void setHoldsaccount(Cloud4SoaAccountInstance cloud4SoaAccountInstance) {
        this.cloud4SoaAccountInstance = cloud4SoaAccountInstance;
        user.setHoldsaccount(cloud4SoaAccountInstance.getCloud4SoaAccount());
    }
    
    @SemanticRelation(semanticClass=Cloud4SoaAccount.class, methodName="getUriId")
    public String getCloud4SoaAccountUriId() {
            return cloud4SoaAccountInstance.getUriId();
    }
    
    public void setCloud4SoaAccountUriId( String uri ) {
            cloud4SoaAccountInstance.setUriId(uri);
    }
    
    @SemanticRelation(semanticClass=Cloud4SoaAccount.class, methodName="getAccountname")
    public java.lang.String getAccountname() {
            return cloud4SoaAccountInstance.getAccountname();
    }

    public void setAccountname( java.lang.String accountname ) {
            cloud4SoaAccountInstance.setAccountname(accountname);
    }

}
