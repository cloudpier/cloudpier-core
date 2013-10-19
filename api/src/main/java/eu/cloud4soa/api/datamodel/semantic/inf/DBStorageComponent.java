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


package eu.cloud4soa.api.datamodel.semantic.inf;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

@RDFBean("http://www.cloud4soa.eu/v0.1/infrastructural-domain#DBStorageComponent")
public class DBStorageComponent extends SoftwareComponent {

    private String dbname;
    private String dbuser;
    private String dbpassword;
    private String dbtype;

    private DBDeployment dbDeployment;
    private DBConfiguration dbConfiguration;
    
    @Override
    @RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/infrastructural-domain#")
    public String getUriId() {
            return super.getUriId();
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBname")
    public String getDbname() {
        return dbname;
    }
    
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }


    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBpassword")
    public String getDbpassword() {
        return dbpassword;
    }

    
    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBtype")
    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBuser")
    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }
    
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBdeployment")
    public DBDeployment getDBdeployment() {
        return dbDeployment;
    }

    public void setDBdeployment(DBDeployment dbDeployment) {
        this.dbDeployment = dbDeployment;
    }

    
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasDBconfiguration")
    public DBConfiguration getDBconfiguration() {
        return dbConfiguration;
    }

    public void setDBconfiguration(DBConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }
}
