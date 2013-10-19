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

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.OneToMany;
import java.util.HashSet;
import javax.persistence.CascadeType;

/**
 * @author pgouvas
 */
@Entity
@Table(name = "C4sUser")
public class User extends AbstractModel<User> implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usertype", nullable = false)    
    private Usertype usertype;
    
    @Column(name = "fullname",     nullable = false)    
    private String fullname;
    
    @Column(name = "username",     nullable = false)    
    private String username;
    
    @Column(name = "password",     nullable = false)    
    private String password;
    
    @Column(name = "c4spublickey", nullable = true)    
    private String c4spublickey;

    @Column(name = "uriID", nullable = false)    
    private String uriID;    
    
    public User() {
    }

    public User(Usertype usertype, String fullname, String username, String password, String c4spublickey) {
        this.usertype = usertype;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.c4spublickey = c4spublickey;
    }

    @Override
    public Long getId() {
        return this.id;

    }
    
    @Override
    public void setId(Long id) {
        this.id = id;
    }


    public Usertype getUsertype() {
        return this.usertype;
    }

    public void setUsertype(Usertype usertype) {
        this.usertype = usertype;
    }


    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getC4spublickey() {
        return c4spublickey;
    }

    public void setC4spublickey(String c4spublickey) {
        this.c4spublickey = c4spublickey;
    }


    public String toString() {
        return "User [ id=" + id + " ]";
    }

    public String getUriID() {
        return uriID;
    }

    public void setUriID(String uriID) {
        this.uriID = uriID;
    }

}