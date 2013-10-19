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
package eu.cloud4soa.soa.mock;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.repository.ApplicationProfilesRepository;
import eu.cloud4soa.api.util.exception.repository.RepositoryException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author vins
 */
public class MockApplicationProfilesRepository implements ApplicationProfilesRepository{
    Map<String, ApplicationInstance> map = new HashMap<String, ApplicationInstance>();
    Map<String, List<ApplicationInstance>> appListMap = new HashMap<String, List<ApplicationInstance>>();
    
    private void storeApplication(String userInstanceUriId, ApplicationInstance applicationInstance){
        System.out.println("Storing: "+applicationInstance.getUriId()+" "+applicationInstance);
        map.put(applicationInstance.getUriId(), applicationInstance);
        if(!appListMap.containsKey(userInstanceUriId)){
            List<ApplicationInstance> list = new ArrayList<ApplicationInstance>();
            list.add(applicationInstance);                    
            appListMap.put(userInstanceUriId, list);
        }
        else{
            if(!appListMap.get(userInstanceUriId).contains(applicationInstance)){
                appListMap.get(userInstanceUriId).add(applicationInstance);
            }
//            Overwrite
        }
    }
    
    @Override
    public ApplicationInstance getApplicationInstance(String applicationInstanceUriId) {
        return map.get(applicationInstanceUriId);
    }

    @Override
    public void storeApplicationInstance(ApplicationInstance applicationInstance) {
        if(applicationInstance.getUriId()==null){
            // Creating a random UUID (Universally unique identifier)
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            applicationInstance.setUriId(randomUUIDString);
            storeApplication(applicationInstance.getOwnerUriId(), applicationInstance);
        }else{
            System.out.println("Try to store a different uriId: "+applicationInstance.getUriId());
        }
        
//        map.put(applicationInstance.getUriId(), applicationInstance);
    }

    @Override
    public void updateApplicationInstance(ApplicationInstance applicationInstance) {
        map.remove(applicationInstance.getUriId());
        storeApplication(applicationInstance.getOwnerUriId(), applicationInstance);
    }

    @Override
    public List<ApplicationInstance> retrieveAllApplicationProfile(String userInstanceUriId) {
        return appListMap.get(userInstanceUriId);
    }

    @Override
    public void removeApplicationProfile(String applicationInstanceUriId) {
        //        Obtain first uri id
        ApplicationInstance applicationInstance = map.get(applicationInstanceUriId);
        System.out.println(map);
        map.remove(applicationInstanceUriId);
        
        appListMap.get(applicationInstance.getOwnerUriId()).remove(applicationInstance);
    }
    
    public boolean hasApplicationProfile(ApplicationInstance applicationInstance, String userInstanceUriId){
        if(appListMap.containsKey(userInstanceUriId)){
            List<ApplicationInstance> appInstances = appListMap.get(userInstanceUriId);
            for (ApplicationInstance storedAppInstance : appInstances) {
                return checkAppEquals(applicationInstance, storedAppInstance);
            }
        }
        return false;
    }
    
    public ApplicationInstance getApplicationInstance(String applicationInstanceUriId, String userInstanceUriId){
        if(appListMap.containsKey(userInstanceUriId)){
            List<ApplicationInstance> appInstances = appListMap.get(userInstanceUriId);
            for (ApplicationInstance storedAppInstance : appInstances) {
                if(storedAppInstance.getUriId().equalsIgnoreCase(applicationInstanceUriId) )
                    return storedAppInstance;
            }
        }
        return null;
    }
    
    public Collection<ApplicationInstance> getAllStoredApplicationInstance(){
        return map.values();
    }
    
    private boolean checkAppEquals(ApplicationInstance applicationInstance1, ApplicationInstance applicationInstance2){
        return applicationInstance1.getApplication()==applicationInstance2.getApplication();
    }
    
    public void cleanUp(){
        map.clear();
        appListMap.clear();
    }

    @Override
    public List<ApplicationInstance> retrieveAllApplicationProfileDeployedOnPaaS(String paasInstanceUserId) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
