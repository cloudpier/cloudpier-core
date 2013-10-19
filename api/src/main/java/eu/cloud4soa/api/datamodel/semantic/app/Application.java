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


package eu.cloud4soa.api.datamodel.semantic.app;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.other.ProgrammingLanguage;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
//import eu.cloud4soa.api.datamodel.semantic.measure.Capacity;
import eu.cloud4soa.api.datamodel.semantic.measure.NumericRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageUnit;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.enterprise-architecture.org/essential-metamodel.owl#Application")
public class Application extends ApplicationLogical {

	private java.lang.String applicationcode;
	private java.lang.String version;
	private java.lang.String termsTitle;
	private java.lang.String digest;
	private ApplicationDeployment applicationDeployment;
	private ApplicationStatus status;
	private java.lang.String licensetype;
	private ApplicationArchive releatedApplicationArchive;
	private List<HardwareComponent> requiresHardwareComponent = new ArrayList<HardwareComponent>();
	private List<SoftwareComponent> requiresSoftwareComponent = new ArrayList<SoftwareComponent>();
	private StorageUnit size;
	// private Capacity size;
	private ProgrammingLanguage useProgrammingLanguage;
	private Developer owner;
	// inferred
	private java.lang.String description;
	private java.lang.String alternative;
        //QOS
        private List<Technology_Service_Quality> requiresServiceQuality = new ArrayList<Technology_Service_Quality>();
        
        private NumericRange requireWebScalingFactor;
        private NumericRange requireComputeScalingFactor;
        
	@Override
	@RDFSubject(prefix = "http://www.enterprise-architecture.org/essential-metamodel.owl#")
	public String getUriId() {
		return super.getUriId();
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#application_code")
	public java.lang.String getApplicationcode() {
		return applicationcode;
	}

	public void setApplicationcode(java.lang.String applicationcode) {
		this.applicationcode = applicationcode;
	}

	@RDF("http://purl.org/dc/terms/hasVersion")
	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	@RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTermsTitle() {
		return termsTitle;
	}

	public void setTermsTitle(java.lang.String termsTitle) {
		this.termsTitle = termsTitle;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#digest")
	public java.lang.String getDigest() {
		return digest;
	}

	public void setDigest(java.lang.String digest) {
		this.digest = digest;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#hasDeployment")
	public ApplicationDeployment getDeployment() {
		return applicationDeployment;
	}

	public void setDeployment(ApplicationDeployment applicationDeployment) {
		this.applicationDeployment = applicationDeployment;

	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#hasStatus")
	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#license_type")
	public java.lang.String getLicensetype() {
		return licensetype;
	}

	public void setLicensetype(java.lang.String licensetype) {
		this.licensetype = licensetype;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#releatedApplicationArchive")
	public ApplicationArchive getReleatedApplicationArchive() {
		return releatedApplicationArchive;
	}

	public void setReleatedApplicationArchive(
			ApplicationArchive releatedApplicationArchive) {
		this.releatedApplicationArchive = releatedApplicationArchive;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#requiresHardwareComponent")
	public List<HardwareComponent> getRequiresResource() {
		return requiresHardwareComponent;
	}

	public void setRequiresResource(
			List<HardwareComponent> requiresHardwareComponent) {

		this.requiresHardwareComponent = requiresHardwareComponent;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#requiresSoftwareComponent")
	public List<SoftwareComponent> getRequiresSoftwareComponent() {
		return requiresSoftwareComponent;
	}

	public void setRequiresSoftwareComponent(
			List<SoftwareComponent> requiresSoftwareComponent) {
		this.requiresSoftwareComponent = requiresSoftwareComponent;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#size")
	public StorageUnit getSize() {
		return size;
	}

	public void setSize(StorageUnit size) {
		this.size = size;
	}

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#hasOwner")
	public Developer getOwner() {
		return owner;
	}

	public void setOwner(Developer owner) {
		this.owner = owner;
	}

	/*
	 * @RDF("http://www.cloud4soa.eu/v0.1/application-domain#size") public
	 * Capacity getSize() { return size; }
	 * 
	 * public void setSize( Capacity size ) { this.size = size; }
	 */

	@RDF("http://www.cloud4soa.eu/v0.1/application-domain#useProgrammingLanguage")
	public ProgrammingLanguage getUseProgrammingLanguage() {
		return useProgrammingLanguage;
	}

	public void setUseProgrammingLanguage(
			ProgrammingLanguage useProgrammingLanguage) {
		this.useProgrammingLanguage = useProgrammingLanguage;
	}

	/**** inferred ****/

	@RDF("http://www.enterprise-architecture.org/essential-metamodel.owl#description")
	public java.lang.String getdescription() {
		return description;
	}

	public void setdescription(java.lang.String description) {
		this.description = description;
	}

	@RDF("http://purl.org/dc/terms/alternative")
	public java.lang.String getAlternative() {
		return alternative;
	}

	public void setAlternative(java.lang.String alternative) {
		this.alternative = alternative;
	}
        
        //Qos
        @RDF("http://www.cloud4soa.eu/v0.1/application-domain#requiresServiceQuality")
        public List<Technology_Service_Quality> getRequiresServiceQuality() {
            return requiresServiceQuality;
        }

        public void setRequiresServiceQuality(List<Technology_Service_Quality> requiresServiceQuality) {
            this.requiresServiceQuality = requiresServiceQuality;
        }

        @RDF("http://www.cloud4soa.eu/v0.1/application-domain#requireWebScalingFactor")
        public NumericRange getRequireWebScalingFactor() {
            return requireWebScalingFactor;
        }

        public void setRequireWebScalingFactor(NumericRange requireWebScalingFactor) {
            this.requireWebScalingFactor = requireWebScalingFactor;
        }

        @RDF("http://www.cloud4soa.eu/v0.1/application-domain#requireComputeScalingFactor")
        public NumericRange getRequireComputeScalingFactor() {
            return requireComputeScalingFactor;
        }

        public void setRequireComputeScalingFactor(NumericRange requireComputeScalingFactor) {
            this.requireComputeScalingFactor = requireComputeScalingFactor;
        }
        
}