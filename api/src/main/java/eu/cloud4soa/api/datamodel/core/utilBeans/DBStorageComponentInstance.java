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
package eu.cloud4soa.api.datamodel.core.utilBeans;

import eu.cloud4soa.api.datamodel.semantic.inf.DB;
import eu.cloud4soa.api.datamodel.semantic.inf.DBDeployment;
import eu.cloud4soa.api.datamodel.semantic.inf.DBStorageComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.NoSQLDB;
import eu.cloud4soa.api.datamodel.semantic.inf.SQLDB;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "dbStorageComponentInstance")
public class DBStorageComponentInstance extends SoftwareComponentInstance {

    public DBStorageComponentInstance() {
        super(new DBStorageComponent());
        setRelatedhwcategory();
    }

    public DBStorageComponentInstance(DBStorageComponent dbStorageComponent) {
        super(dbStorageComponent);
        setRelatedhwcategory((DB)dbStorageComponent.getRelatedswcategory());
    }
    
    public DBCategoryInstance getRelatedhwcategoryInstance() {
        if(getRelatedhwcategory() instanceof SQLDB)
            return new SqlDbCategoryInstance((SQLDB)getRelatedhwcategory());
        else if(getRelatedhwcategory() instanceof SQLDB)
            return new NoSqlDbCategoryInstance((NoSQLDB)getRelatedhwcategory());
        else return new DBCategoryInstance((DB)getRelatedhwcategory());
    }
    
    public void setRelatedhwcategoryInstance(DBCategoryInstance dbCategoryInstance) {
        setRelatedhwcategory((DB)dbCategoryInstance.getSoftwareCategory());
    }
    
    private DB getRelatedhwcategory() {
        return (DB)super.softwareComponent.getRelatedswcategory();
    }
    
    private void setRelatedhwcategory(DB db) {
        super.softwareComponent.setRelatedswcategory(db);
    }
    
    private void setRelatedhwcategory() {
        DBStorageComponent dBStorageComponent = getDBStorageComponent();
        DB db = new DB();
        dBStorageComponent.setRelatedswcategory(db);
    }
    
    public DBStorageComponent getDBStorageComponent(){
        return (DBStorageComponent)super.getSoftwareComponent();
    }
    
    public void setDBStorageComponent(DBStorageComponent dbStorageComponent){
        super.setSoftwareComponent(dbStorageComponent);
    }
    
    public String getDbuser(){
        return getDBStorageComponent().getDbuser();
    }
    
    public void setDbuser(String dbUser){
       getDBStorageComponent().setDbuser(dbUser);
    }
    
    public String getDbpassword(){
        return getDBStorageComponent().getDbpassword();
    }
    
    public void setDbpassword(String dbPassword){
       getDBStorageComponent().setDbpassword(dbPassword);
    }
    
    public String getDbname(){
        return getDBStorageComponent().getDbname();
    }
    
    public void setDbname(String dbName){
       getDBStorageComponent().setDbname(dbName);
    }
    
    public String getDbtype(){
        return getDBStorageComponent().getDbtype();
    }
    
    public void setDbtype(String dbType){
       getDBStorageComponent().setDbtype(dbType);
    }
    
    public String getDeploymentLocationUriId(){
        DBDeployment dBdeployment = getDBStorageComponent().getDBdeployment();
        if(dBdeployment != null && dBdeployment.getDeploymentLocation() != null)
            return dBdeployment.getDeploymentLocation().getUriId();
        return null;
    }
//    
//    public void setDeploymentLocation(PaaSInstance paaSInstance){
//        DBDeployment dBdeployment = getDBStorageComponent().getDBdeployment();
//        if(dBdeployment == null){
//            dBdeployment=new DBDeployment();
//            getDBStorageComponent().setDBdeployment(dBdeployment);
//        }
//        if(dBdeployment.getDeploymentLocation() == null){
//            dBdeployment.setDeploymentLocation(paaSInstance.getPaaSOffering());
//        }
//    }
//    
//    public PaaSInstance getDeploymentLocation(){
//        DBDeployment dBdeployment = getDBStorageComponent().getDBdeployment();
//        PaaSOffering deploymentLocation = null;
//        PaaSInstance paaSInstance = null;
//        if(dBdeployment != null && dBdeployment.getDeploymentLocation() != null){
//            deploymentLocation = dBdeployment.getDeploymentLocation();
//            paaSInstance = new PaaSInstance(deploymentLocation);
//        }
//        return paaSInstance;
//    }
    
    public String getUrl(){
        if(getDBStorageComponent().getDBdeployment() != null)
            return getDBStorageComponent().getDBdeployment().getUrl();
        return null;
    }
    
    public void setUrl(String url){
        if(getDBStorageComponent().getDBdeployment() != null)
            getDBStorageComponent().getDBdeployment().setUrl(url);
    }
}
