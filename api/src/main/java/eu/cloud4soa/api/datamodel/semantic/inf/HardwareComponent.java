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

@RDFBean("http://www.cloud4soa.eu/v0.1/infrastructural-domain#Hardware_Component")
public class HardwareComponent extends TechnologyComponent {

	private java.lang.String version;
        private String description;
        private String title;
	private HardwareCategory relatedhwcategory;

	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/infrastructural-domain#")
	public String getUriId() {
		return super.getUriId();
	}
	
    @RDF("http://www.enterprise-architecture.org/essential-metamodel.owl#realisation_of_technology_capability")
	public HardwareCategory getRelatedhwcategory() {
		return relatedhwcategory;
	}
	
	public void setRelatedhwcategory( HardwareCategory relatedhwcategory ) {
		this.relatedhwcategory = relatedhwcategory;
	}
        
         @RDF("http://purl.org/dc/terms/hasVersion")
	public java.lang.String getVersion() {
		return version;
	}
	
	public void setVersion( java.lang.String version ) {
		this.version = version;
	}
        
         @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return description;
	}
	
	public void setDescription( java.lang.String description ) {
		this.description = description;
	}
        @RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String title ) {
		this.title = title;
	}
}
