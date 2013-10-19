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


package eu.cloud4soa.relational.persistence;

import eu.cloud4soa.relational.datamodel.ApplicationInstance;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author pgouvas
 *
 */
@Repository
public class ApplicationInstanceRepository extends AbstractHbmDao<ApplicationInstance> implements IApplicationInstanceRepository {

	@Autowired
	public ApplicationInstanceRepository(SessionFactory sessionFactory) {
		super(sessionFactory, ApplicationInstance.class);
	}
	
	public void store(ApplicationInstance appinstance) {
		this.saveOrUpdate(appinstance);
	}

	public List<ApplicationInstance> retrieveAll() {
		return this.findAll();
	}
        
    public List<ApplicationInstance> findByUriId(String uriId) {
        List<ApplicationInstance> appList = this.find("uriID = ?", uriId);
        if ( appList != null && appList.size() > 1 ) {
            throw new IndexOutOfBoundsException("Found more then one ApplicationInstance with the same uriId");
        }
        return appList;
    }
        
    public List<ApplicationInstance> findByUriIdNoCheck(String uriId) {
        List<ApplicationInstance> appList = this.find("uriID = ?", uriId);
        return appList;
    }
        
    public List<ApplicationInstance> findByUriIDAndAccountId(String uriId, Long accountId ) {
    List<ApplicationInstance> appList = this.find("uriID = ? AND account.id = ?", uriId, accountId);
      
        return appList;

    }
    
    //updates the start runing time of an application
    public boolean updateApplicationStartRunningTime(String uriId) {
        boolean updated=false;
    
        return updated;

    }
    
    //calculates and updates the total runing time of an application
    public boolean updateApplicationTotalRunningTime(String uriId) {
        boolean updated=false;
        List<ApplicationInstance> appList = this.find("uriID = ?", uriId);
        if ( appList != null && appList.size() > 1 ) {
            throw new IndexOutOfBoundsException("Found more then one ApplicationInstance with the same uriId");
        }else if( appList != null && appList.size() > 0 ) {
        ApplicationInstance appInstance = appList.get(0);
        Long latestStarTime=appInstance.getLatestStart();       
        Long totalRunningTime=appInstance.getRuntime();    
        Long currentTime=System.currentTimeMillis();
        
        totalRunningTime = totalRunningTime + currentTime - latestStarTime;
        appInstance.setRuntime(totalRunningTime);
        store(appInstance);
        }
    return updated;
        
    }
    
    //calculates and updates the total runing time of an application
    public Long updateAndGetApplicationTotalRunningTime(String uriId) {
        //update total running time
        //updateApplicationTotalRunningTime(uriId);
        
        
        Long totalRunningTime=getApplicationTotalRunningTimeNew(uriId);
        
        return totalRunningTime;
    }  
    
    //returns total runing time calculated for application, by providing application uri    
    public Long getApplicationTotalRunningTime(String uriId) {
        Long totalRunningTime =0L;
        List<ApplicationInstance> appList = this.find("uriID = ?", uriId);
        if ( appList != null && appList.size() > 1 ) {
            throw new IndexOutOfBoundsException("Found more then one ApplicationInstance with the same uriId");
        }else if( appList != null && appList.size() > 0 ) {

        
        totalRunningTime=appList.get(0).getRuntime();
        }
        return totalRunningTime;
    }    
    
    //returns total runing time calculated for application, by providing application uri    
    public Long getApplicationTotalRunningTimeNew(String uriId) {
        Long totalRunningTime =0L;
        List<ApplicationInstance> appList = this.find("uriID = ?", uriId);
        if ( appList != null && appList.size() > 1 ) {
            throw new IndexOutOfBoundsException("Found more then one ApplicationInstance with the same uriId");
        }else if( appList != null && appList.size() > 0 ) {
        ApplicationInstance appInstance = appList.get(0);
        Long latestStartTime=appInstance.getLatestStart();       
        Long storedRunningTime=appInstance.getRuntime();    
        //Long currentTime=System.currentTimeMillis();
        java.util.Date today = new java.util.Date();           
        //java.util.Date latestStart = new java.util.Date(appInstance.getLatestStart());
        //Long newRunTime=today.getTime() - latestStart.getTime();      
        totalRunningTime = storedRunningTime + (today.getTime() - latestStartTime);   ;
        }
        return totalRunningTime;
    }    
    

        

}