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


package eu.cloud4soa.api.datamodel.semantic.paas;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.Thing;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/paas-model#PricingPolicy")
public class PricingPolicy extends PlatformLayer {

	private List<HardwareComponent> relatedHardwareComponent= new ArrayList<HardwareComponent>();
	private List<SoftwareComponent> relatedSoftware=new ArrayList<SoftwareComponent>();
	private java.lang.String description;
	private java.lang.String title;





	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/paas-model#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#relatedHardwareComponent")
	public List<HardwareComponent> getRelatedHardwareComponent() {
		return relatedHardwareComponent;
	}
	
	public void setRelatedHardwareComponent( List<HardwareComponent> relatedHardwareComponent ) {
		this.relatedHardwareComponent = relatedHardwareComponent;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#relatedSoftware")
	public List<SoftwareComponent> getRelatedSoftware() {
		return relatedSoftware;
	}
	
	public void setRelatedSoftware( List<SoftwareComponent> relatedSoftware ) {
		this.relatedSoftware = relatedSoftware;
	}
    @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return description;
	}
	
	public void setDescription( java.lang.String Description ) {
		this.description = Description;
	}
    @RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String title ) {
		this.title = title;
	}

}
