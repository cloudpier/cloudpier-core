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


package eu.cloud4soa.relational.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eu.cloud4soa.api.governance.monitoring.IMonitoringJob;
import javax.persistence.*;

/**
 * 
 * @author denisneuling <dn@cloudcontrol.de>
 */
@Entity
@Table(name = "Monitoringjob")
public class MonitoringJob extends AbstractModel<MonitoringJob> implements IMonitoringJob{
	private static final long serialVersionUID = 6348608521331335346L;
	
	@Id
	@GeneratedValue
	private Long id;
	
        //@Column		
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationInstance", nullable = true)   
	private ApplicationInstance applicationInstance;
			
	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date lastExecuted;
	
	@Column
	private boolean enabled;
		
	@Column
	private String checkurl;        

    @Column
    private String applicationInstanceUriId;
        
	public MonitoringJob(){
		enabled = true;
		lastExecuted = new Date();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "MonitoringJob [id=" + id + "]";
	}

    public ApplicationInstance getApplicationInstance() {
        return applicationInstance;
    }

    public void setApplicationInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    @Override
    public Date getLastExecuted() {
        return lastExecuted;
    }

    @Override
    public void setLastExecuted(Date lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCheckUrl() {
        return checkurl;
    }

    public void setCheckUrl(String checkurl) {
        this.checkurl = checkurl;
    }

    @Override
    public String getApiKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSecretKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getApplicationInstanceUriId() {
        return applicationInstanceUriId;
    }

    public void setApplicationInstanceUriId(String applicationInstanceUriId) {
        this.applicationInstanceUriId = applicationInstanceUriId;
    }
}