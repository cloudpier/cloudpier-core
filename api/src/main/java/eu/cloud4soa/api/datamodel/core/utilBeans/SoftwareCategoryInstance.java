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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "softwareCategoryInstance", namespace="eu.cloud4soa.api.datamodel.core.utilBeans")
@XmlSeeAlso({DBStorageComponentInstance.class})
public class SoftwareCategoryInstance {
    private SoftwareCategory softwareCategory;

    public SoftwareCategoryInstance() {
        this.softwareCategory=new SoftwareCategory();
    }

    public SoftwareCategoryInstance(SoftwareCategory softwareCategory) {
        this.softwareCategory = softwareCategory;
    }
    
    public SoftwareCategoryInstance(String title, String description) {
        this();
        softwareCategory.setTitle(title);
        softwareCategory.setDescription(description);
    }
    
    public String getUriId(){
        return softwareCategory.getUriId();
    }
    
    public void setUriId(String uriId){
       softwareCategory.setUriId(uriId);
    }
    
    public String getTitle(){
        return softwareCategory.getTitle();
    }
    
    public void setTitle(String title){
       softwareCategory.setTitle(title);
    }
    
    public String getDescription(){
        return softwareCategory.getDescription();
    }
    
    public void setDescription(String description){
       softwareCategory.setDescription(description);
    }

    protected void setSoftwareCategory(SoftwareCategory softwareCategory) {
        this.softwareCategory = softwareCategory;
    }
    
    public SoftwareCategory getSoftwareCategory() {
        return softwareCategory;
    }
    
}
