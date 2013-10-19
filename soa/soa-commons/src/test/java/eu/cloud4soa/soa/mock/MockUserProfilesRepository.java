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

import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.repository.UserProfilesRepository;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author vins
 */
public class MockUserProfilesRepository implements UserProfilesRepository{
    Map<String, UserInstance> map;

	public MockUserProfilesRepository() {
		UserInstance	defaultUser;
		String			defaultUserId;
		
		this.map	= new HashMap<String, UserInstance>();
		
		defaultUserId	= UUID.randomUUID().toString();
		defaultUser		= new UserInstance();
		defaultUser.setAccountname("cloud4soa");
		defaultUser.setBirthday( Calendar.getInstance().getTime() );
		defaultUser.setUriId(defaultUserId);
		defaultUser.setFamilyname( "familyName");
		defaultUser.setFirstName( "firstName");
		defaultUser.setGeekcode( "geekCode" );
		defaultUser.setPersonalmailbox( "userEMail");
		defaultUser.setSurname( "surname" );
		defaultUser.setUriId( defaultUserId );
		
		this.map.put( defaultUserId, defaultUser);
		
	}
	
	
    
    @Override
    public void createUserInstance(UserInstance userInstance) {
        if(userInstance.getUriId()==null){
            // Creating a random UUID (Universally unique identifier)
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            userInstance.setUriId(randomUUIDString);
        }
        if(userInstance.getCloud4SoaAccountUriId()==null){
            // Creating a random UUID (Universally unique identifier)
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            userInstance.setCloud4SoaAccountUriId(randomUUIDString);
        }
        map.put(userInstance.getUriId(), userInstance);
    }

    @Override
    public UserInstance getUserInstance(String userInstanceUriId) {
        return map.get(userInstanceUriId);
    }

    @Override
    public void storeUserInstance(UserInstance userInstance) throws IllegalArgumentException {
        map.remove(userInstance.getUriId());
        map.put(userInstance.getUriId(), userInstance);
    }

	@Override
	public UserInstance getUserInstanceFromAccountName(String accountName) {
		UserInstance	userInstance;
		
		userInstance	= null;
		for( UserInstance oneUser: map.values() ) {
			if ( oneUser.getAccountname()!= null && oneUser.getAccountname().equals( accountName) ) {
				userInstance	= oneUser;
			}
		}
		
		return userInstance;
	}

	
	@Override
	public void close() {
		map.clear();
	}

    @Override
    public void updateUserInstance(UserInstance userInstance) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	
    
}
