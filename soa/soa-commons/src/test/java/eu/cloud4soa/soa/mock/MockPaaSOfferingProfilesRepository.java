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
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSRating;
import eu.cloud4soa.api.repository.PaaSOfferingProfilesRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Yosu Gorroñogoitia
 */
public class MockPaaSOfferingProfilesRepository implements PaaSOfferingProfilesRepository{
    Map<String, PaaSInstance> map = new HashMap<String, PaaSInstance>();
    Map<String, List<PaaSInstance>> paaSListMap = new HashMap<String, List<PaaSInstance>>();
    
    private void storePaaSInstance(String userInstanceUriId, PaaSInstance paaSInstance){
        System.out.println("Storing: "+paaSInstance.getUriId()+" "+paaSInstance);
        map.put(paaSInstance.getUriId(), paaSInstance);
        if(!paaSListMap.containsKey(userInstanceUriId)){
            List<PaaSInstance> list = new ArrayList<PaaSInstance>();
            list.add(paaSInstance);                    
            paaSListMap.put(userInstanceUriId, list);
        }
        else{
            if(!paaSListMap.get(userInstanceUriId).contains(paaSInstance)){
                paaSListMap.get(userInstanceUriId).add(paaSInstance);
            }
//            Overwrite
        }
    }

	@Override
	public List<PaaSInstance> getPaaSInstances(
			ApplicationInstance applicationInstance) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PaaSInstance getPaaSInstance(String paaSInstanceUriId) {
		return map.get(paaSInstanceUriId);
	}

	@Override
	public void updatePaaSInstance(PaaSInstance paaSInstance,
			PaaSRating paaSRating) {
		map.remove(paaSInstance.getUriId());
            storePaaSInstance(paaSInstance.getUriId(), paaSInstance);
	}

	@Override
	public String storePaaSInstance(PaaSInstance paaSInstance) {
            if(paaSInstance.getUriId()==null){
                // Creating a random UUID (Universally unique identifier)
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                paaSInstance.setUriId(randomUUIDString);
                storePaaSInstance(paaSInstance.getUriId(), paaSInstance);
            }else{
                System.out.println("Try to store a different uriId: "+paaSInstance.getUriId());
            }
            return paaSInstance.getUriId();
	}

	@Override
	public List<PaaSInstance> getAllAvailablePaaSInstances() {
		List<PaaSInstance> result = new ArrayList<PaaSInstance>();
		result.addAll(map.values());
		return result;
	}

	@Override
	public void removePaaSInstance(String paasInstanceUriId) {
		PaaSInstance paaSInstance = map.get(paasInstanceUriId);
        System.out.println(map);
        map.remove(paasInstanceUriId);
        
        paaSListMap.get(paaSInstance.getUriId()).remove(paaSInstance);
	}

	@Override
	public List<PaaSInstance> retrieveAllPaaSInstances(String userInstanceUriId) {
		return paaSListMap.get(userInstanceUriId);
	}
    
    public void cleanUp(){
        map.clear();
        paaSListMap.clear();
    }
}
