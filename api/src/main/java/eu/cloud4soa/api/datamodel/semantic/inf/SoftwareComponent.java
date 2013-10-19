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
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationLogical;

@RDFBean("http://www.enterprise-architecture.org/essential-metamodel.owl#Software_Component")
public class SoftwareComponent extends ApplicationLogical {

	private java.lang.String licensetype;
	private SoftwareCategory relatedswcategory;
	private java.lang.String version;
        private java.lang.String description;
	private java.lang.String title;
	private java.lang.Boolean isRequired;


	@Override
	@RDFSubject(prefix="http://www.enterprise-architecture.org/essential-metamodel.owl#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#license_type")
	public java.lang.String getLicensetype() {
		return licensetype;
	}
	
	public void setLicensetype( java.lang.String licensetype ) {
		this.licensetype = licensetype;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#related_sw_category")
	public SoftwareCategory getRelatedswcategory() {
		return relatedswcategory;
	}
	
	public void setRelatedswcategory( SoftwareCategory relatedswcategory ) {
		this.relatedswcategory = relatedswcategory;
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

         @RDF("http://purl.org/dc/terms/hasVersion")
	public java.lang.String getVersion() {
		return version;
	}
	
	public void setVersion( java.lang.String version ) {
		this.version = version;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#isRequired")
	public java.lang.Boolean getIsRequired() {
		return isRequired;
	}
	
	public void setIsRequired( java.lang.Boolean isRequired ) {
		this.isRequired = isRequired;
	}
}
