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

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: pgouvas
 * Date: 8/3/12
 * Time: 7:45 AM
 */
@Entity(name = "Gitrepo")
public class GitRepo  extends AbstractModel<GitRepo> implements java.io.Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long  id;

    @Column(name = "giturl")
    private String  giturl;

    @Column(name = "gitrepo")
    private String  gitrepo;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;


    @ManyToOne
    @JoinColumn(name = "paasid")
    private Paas paas;
    
    @ManyToOne
    @JoinColumn(name = "appid")
    private ApplicationInstance app;
    
    public GitRepo() {
    }  
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long gitrepoid) {
        this.id = gitrepoid;
    }

    public Paas getPaas() {
        return paas;
    }

    public void setPaas(Paas paas) {
        this.paas = paas;
    }

    public String getGiturl() {
        return giturl;
    }

    public void setGiturl(String giturl) {
        this.giturl = giturl;
    }

    public String getGitrepo() {
        return gitrepo;
    }

    public void setGitrepo(String gitrepo) {
        this.gitrepo = gitrepo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApplicationInstance getApp() {
        return app;
    }

    public void setApp(ApplicationInstance app) {
        this.app = app;
    }        
}
