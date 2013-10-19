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

import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareCategory;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "softwareComponentInstance", namespace="eu.cloud4soa.api.datamodel.core.utilBeans")
@XmlSeeAlso({DBStorageComponentInstance.class})
public class SoftwareComponentInstance {
    protected SoftwareComponent softwareComponent;

    public SoftwareComponentInstance() {
        this.softwareComponent=new SoftwareComponent();
    }

    public SoftwareComponentInstance(SoftwareComponent softwareComponent) {
        this.softwareComponent = softwareComponent;
    }
    
    public SoftwareComponentInstance(String title, String description, String version, String licensetype) {
        this();
        softwareComponent.setTitle(title);
        softwareComponent.setDescription(description);
        softwareComponent.setVersion(version);
        softwareComponent.setLicensetype(licensetype);
    }

    public SoftwareComponent getSoftwareComponent() {
        return softwareComponent;
    }

    public void setSoftwareComponent(SoftwareComponent softwareComponent) {
        this.softwareComponent = softwareComponent;
    }
    
    public String getUriId(){
        return softwareComponent.getUriId();
    }
    
    public void setUriId(String uriId){
       softwareComponent.setUriId(uriId);
    }
    
    public String getTitle(){
        return softwareComponent.getTitle();
    }
    
    public void setTitle(String title){
       softwareComponent.setTitle(title);
    }
    
    public String getDescription(){
        return softwareComponent.getDescription();
    }
    
    public void setDescription(String description){
        softwareComponent.setDescription(description);
    }
    
    public String getLicensetype(){
        return softwareComponent.getLicensetype();
    }
    
    public void setLicensetype(String licenseType){
       softwareComponent.setLicensetype(licenseType);
    }
    
    public String getVersion(){
        return softwareComponent.getVersion();
    }
    
    public void setVersion(String version){
       softwareComponent.setVersion(version);
    }
    
//    Second Level of Indirection  
//---    Remember: catch nullPointerException... ---
//---    Remember: add a getUri for every object obtained by indirection... ---
//---    Remember: add a Designed getter from every object obtained by indirection... ---
    /*---------- swcategory ----------*/	
   
    private SoftwareCategory getRelatedswcategory(){
        return softwareComponent.getRelatedswcategory();
    }
    
//    private void setRelatedswcategory(SoftwareCategory relatedswcategory){
//        softwareComponent.setRelatedswcategory(relatedswcategory);
//    }
    
    private String getRelatedswcategoryTitle(){
        return getRelatedswcategory().getTitle();
    }
    
    private void setRelatedswcategoryTitle(String title){
        getRelatedswcategory().setTitle(title);
    }
    
    public SoftwareCategoryInstance getSoftwareCategoryInstance(){
        return new SoftwareCategoryInstance(softwareComponent.getRelatedswcategory());
    }
    
    public void setSoftwareCategoryInstance(SoftwareCategoryInstance softwareCategoryInstance){
        this.softwareComponent.setRelatedswcategory(softwareCategoryInstance.getSoftwareCategory());        
    }

}
