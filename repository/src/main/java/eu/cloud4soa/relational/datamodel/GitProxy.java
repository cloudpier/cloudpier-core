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
 * Date: 8/28/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity(name = "Gitproxy")
public class GitProxy extends AbstractModel<GitProxy> implements java.io.Serializable {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long  id;


    @Column(name = "proxyname")
    private String  proxyname;

    @ManyToOne //(cascade =CascadeType.ALL )
    @JoinColumn(name = "repoid")
    private GitRepo repo;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    
    public GitProxy() {
    }
       
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProxyname() {
        return proxyname;
    }

    public void setProxyname(String proxyname) {
        this.proxyname = proxyname;
    }

    public GitRepo getRepo() {
        return repo;
    }

    public void setRepo(GitRepo repo) {
        this.repo = repo;
    }

}
