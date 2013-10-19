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

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.datamodel.semantic.user.Cloud4SoaAccount;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "developerInstance", namespace="eu.cloud4soa.api.datamodel.core")
public class DeveloperInstance extends UserInstance{

    public DeveloperInstance(Developer developer) {
        super(developer);
        if(developer.getHoldsaccount() != null)
            setHoldsaccount(new Cloud4SoaAccountInstance((Cloud4SoaAccount)developer.getHoldsaccount()));
        else 
            setHoldsaccount(new Cloud4SoaAccountInstance(new Cloud4SoaAccount()));
    }
    
    public DeveloperInstance() {
        this(new Developer());
//        Developer developer = new Developer();
//        Cloud4SoaAccount cloud4SoaAccount = new Cloud4SoaAccount();
//        developer.setHoldsaccount(cloud4SoaAccount);
//        
//        setHoldsaccount(new Cloud4SoaAccountInstance());
//        setBirthday(Calendar.getInstance());
    }
    
    private Developer getDeveloper(){
        return (Developer)this.getUser();
    }
    
    //First Level of Indirection  
    @SemanticRelation(semanticClass=Developer.class, methodName="getApplication")
    public List<ApplicationInstance> getApplications() {
        //closableIterator instead of List? -> to force the removing/adding process 
        //through the methods exposed by this class
        List<ApplicationInstance> applicationInstances = new ArrayList<ApplicationInstance>();
        for (Application application : getDeveloper().getApplication()) {
            ApplicationInstance appInstance = new ApplicationInstance(application);
            applicationInstances.add(appInstance);
        }
        return applicationInstances;       
    }
    
    public void setApplications(List<ApplicationInstance> applicationInstances) {
        List<Application> applications = new ArrayList<Application>();
        for (ApplicationInstance applicationInstance : applicationInstances) {
            Application application = applicationInstance.getApplication();
            applications.add(application);
        }
        getDeveloper().setApplication(applications);
    }
    
    public void addApplication(ApplicationInstance applicationInstance) {
        Application application = applicationInstance.getApplication();
        if(!getDeveloper().getApplication().contains(application))
            getDeveloper().getApplication().add(application);
    }

    public void removeApplication(ApplicationInstance applicationInstance) {
        Application application = applicationInstance.getApplication();
        if(getDeveloper().getApplication().contains(application))
            getDeveloper().getApplication().add(application);        
    }
    
}
