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

import eu.cloud4soa.relational.datamodel.GitProxy;
import eu.cloud4soa.relational.datamodel.User;

import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: pgouvas
 * Date: 8/28/12
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GitProxyRepository extends AbstractHbmDao<GitProxy> implements IGitProxyRepository {

    @Autowired
    public GitProxyRepository(SessionFactory sessionFactory) {
        super(sessionFactory, GitProxy.class);
    }

    public void store(GitProxy girProxy) {
        this.saveOrUpdate(girProxy);
    }

    public List<GitProxy> findByProxyname(String proxyname){
        List<GitProxy> gitProxyList = this.find("proxyname = ?", proxyname);
        return gitProxyList;
    }

    public List<GitProxy> findByUser(User user){
        List<GitProxy> gitProxyList = this.find("user.id = ?", user.getId());
        return gitProxyList;
    }

    public List<GitProxy> findByUserAndGitproxyid(User user, Long proxyid){
        List<GitProxy> gitProxyList = this.find("id = ? AND user.id = ?", proxyid, user.getId());
        return gitProxyList;
    }

}
