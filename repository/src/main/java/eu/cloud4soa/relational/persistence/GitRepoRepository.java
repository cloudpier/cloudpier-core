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
import eu.cloud4soa.relational.datamodel.GitProxy;
import eu.cloud4soa.relational.datamodel.GitRepo;
import eu.cloud4soa.relational.datamodel.Paas;
import eu.cloud4soa.relational.datamodel.User;

import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: pgouvas
 * Date: 8/3/12
 * Time: 7:55 AM
 */
@Repository
public class GitRepoRepository extends AbstractHbmDao<GitRepo> implements IGitRepoRepository {

    @Autowired
    public GitRepoRepository (SessionFactory sessionFactory) {
        super(sessionFactory, GitRepo.class);
    }

    public List<GitRepo> findByUser(User user) {
        List<GitRepo> gitRepoList = this.find("user.id = ?", user.getId());
        return gitRepoList;
    }

    public List<GitRepo> findByGitrepo(String gitrepo) {
        List<GitRepo> gitProxyList = this.find("gitrepo = ?", gitrepo);
        return gitProxyList;
    }

    public List<GitRepo> findByGitrepoAndGitUrl(String gitrepo, String giturl) {
        List<GitRepo> gitProxyList = this.find("gitrepo = ? AND giturl = ?", gitrepo, giturl);
        return gitProxyList;
    }

    public List<GitRepo> findByUserAndGitrepoid(User user, Long gitrepoid) {
        List<GitRepo> gitProxyList = this.find("id = ? AND user.id = ?", gitrepoid, user.getId());
        return gitProxyList;
    }
    
    public List<GitRepo> findByAppAndUserAndPaaS(ApplicationInstance app, User user, Paas paas) {
        List<GitRepo> gitProxyList = this.find("app.id = ? AND user.id = ? AND paas.id = ?", app.getId(), user.getId(), paas.getId());
        return gitProxyList;
    }
}