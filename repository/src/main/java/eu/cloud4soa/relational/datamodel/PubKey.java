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

import org.hibernate.annotations.Table;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: pgouvas
 * Date: 8/2/12
 * Time: 12:30 PM
 */

@Entity(name = "Pubkey")
public class PubKey extends AbstractModel<PubKey> implements java.io.Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "pubkey",length = 500)
    private String pubkey;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public PubKey() {
    }
       
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long pubkeyid) {
        this.id = pubkeyid;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PubKey{" +
                "pubkeyid=" + id +
                ", pubkey='" + pubkey + '\'' +
                ", user=" + user +
                '}';
    }
}
