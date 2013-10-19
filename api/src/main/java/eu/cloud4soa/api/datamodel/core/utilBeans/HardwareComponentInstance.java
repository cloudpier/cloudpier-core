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

import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "hardwareComponentInstance", namespace="eu.cloud4soa.api.datamodel.core")
@XmlSeeAlso({StorageComponentInstance.class, ComputeInstance.class, HttpRequestsHandlerInstance.class, NetworkResourceInstance.class})
public class HardwareComponentInstance {
    protected HardwareComponent hardwareComponent;
    private HardwareCategoryInstance hardwareCategoryInstance;
    
    public HardwareComponentInstance() {
        this.hardwareComponent=new HardwareComponent();
    }

    public HardwareComponentInstance(HardwareComponent hardwareComponent) {
        this.hardwareComponent = hardwareComponent;
    }
        
    public String getUriId(){
        return hardwareComponent.getUriId();
    }
    
    public void setUriId(String uriId){
       hardwareComponent.setUriId(uriId);
    }
    
    public String getTitle(){
        return hardwareComponent.getTitle();
    }
    
    public void setTitle(String title){
       hardwareComponent.setTitle(title);
    }
    
    public String getDescription(){
        return hardwareComponent.getDescription();
    }
    
    public void setDescription(String description){
       hardwareComponent.setDescription(description);
    }
    
    public String getVersion(){
        return hardwareComponent.getVersion();
    }
    
    public void setVersion(String version){
       hardwareComponent.setVersion(version);
    }
    
//    private HardwareCategory getRelatedhwcategory(){
//        return hardwareComponent.getRelatedhwcategory();
//    }
    
    public HardwareCategoryInstance getRelatedhwcategoryInstance(){
        return hardwareCategoryInstance;
    }
    
    public void setRelatedhwcategoryInstance(HardwareCategoryInstance hardwareCategoryInstance){
        this.hardwareCategoryInstance=hardwareCategoryInstance;
        this.hardwareComponent.setRelatedhwcategory(hardwareCategoryInstance.getHardwareCategory());
    }

    public HardwareComponent getHardwareComponent() {
        return hardwareComponent;
    }
    
    
//    public void accept(PrinterVisitor visitor) {
//        visitor.visit(this);
//    }

    public void setHardwareComponent(HardwareComponent hardwareComponent) {
        this.hardwareComponent = hardwareComponent;
    }
    
}
