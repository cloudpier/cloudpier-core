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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudadapter;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class DatabaseObject {

    public static final String MYSQL = "MySQL";
    public static final String ORACLE = "Oracle";
    public static final String REDIS = "Redis";
    
    
    
    private String dbname;
    private String dbidentifier;
    private String dbhost;
    private String dbtype=MYSQL;
    private Integer port=3306;

    /**
     * @return the dbname
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * @param dbname the dbname to set
     */
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    /**
     * @return the dbidentifier
     */
    public String getDbidentifier() {
        return dbidentifier;
    }

    /**
     * @param dbidentifier the dbidentifier to set
     */
    public void setDbidentifier(String dbidentifier) {
        this.dbidentifier = dbidentifier;
    }

    /**
     * @return the dbhost
     */
    public String getDbhost() {
        return dbhost;
    }

    /**
     * @param dbhost the dbhost to set
     */
    public void setDbhost(String dbhost) {
        this.dbhost = dbhost;
    }

    /**
     * @return the dbtype
     */
    public String getDbtype() {
        return dbtype;
    }

    /**
     * @param dbtype the dbtype to set
     */
    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }


    
}
