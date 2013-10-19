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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;

/**
 *
 * @author pgouvas
 */
@Entity
@Table(name = "Applicationinstance")
public class ApplicationInstance extends AbstractModel<ApplicationInstance> implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "appname", nullable = false)
    private String appname;
    
    @Column(name = "appurl", nullable = true)
    private String appurl;
    
    @Column(name = "adapterurl", nullable = true)
    private String adapterurl;    
    
    @Column(name = "version", nullable = true)
    private String version;    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account", nullable = false)    
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = true)    
    private Status status;    
    
    @Column(name = "uriID", nullable = false)    
    private String uriID;     
    
    @Column(name = "runtime", nullable = true)
    private Long runtime = 0L;    
    
    @Column(name = "latestStart", nullable = true)
    private Long latestStart = 0L;    
    
    public ApplicationInstance() {
    }



    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    
    public String toString() {
        return "Paas [ id=" + id + " ]";
    }

    public String getName() {
        return appname;
    }

    public void setName(String name) {
        this.appname = name;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getAdapterurl() {
        return adapterurl;
    }

    public void setAdapterurl(String adapterurl) {
        this.adapterurl = adapterurl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUriID() {
        return uriID;
    }

    public void setUriID(String uriID) {
        this.uriID = uriID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the runtime
     */
    public Long getRuntime() {
        return runtime;
    }

    /**
     * @param runtime the runtime to set
     */
    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    /**
     * @return the latestStart
     */
    public Long getLatestStart() {
        return latestStart;
    }

    /**
     * @param latestStart the latestStart to set
     */
    public void setLatestStart(Long latestStart) {
        this.latestStart = latestStart;
    }

    
}