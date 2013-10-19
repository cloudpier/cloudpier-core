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

import eu.cloud4soa.api.governance.monitoring.IMonitoringStatistic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Statistic object with metrics
 *
 * @author denisneuling (dn@cloudcontrol.de)
 */
@Entity
@Table(name = "Monitoringstatistic")
public class MonitoringStatistic extends AbstractModel<MonitoringStatistic> implements IMonitoringStatistic, Serializable {

    private static final long serialVersionUID = -3343340281569961762L;
    @Id
    @GeneratedValue
    private Long id;
    
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "monitoringJob", unique = true, nullable = false)
    //private MonitoringJob monitoringJob;
    
    @Column
    private long monitoringJobId;    
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Column
    private long responseTime;
    
    @Column
    private int responseCode;
    
    @Column
    private String message;

    @Override
    public String toString() {
        return "MonitoringStatistic [id=" + id + ", monitoringJobId=" + getMonitoringJobId() + ", date=" + date + ", responseTime=" + responseTime
                + ", responseCode=" + responseCode + ", message=" + message + "]";
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
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getMonitoringJobId() {
        return monitoringJobId;
    }

    public void setMonitoringJobId(long monitoringJobId) {
        this.monitoringJobId = monitoringJobId;
    }



}
