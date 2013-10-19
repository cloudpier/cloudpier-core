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

import eu.cloud4soa.api.datamodel.semantic.ent.IaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.ea.Technology_Service_Quality;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import eu.cloud4soa.api.datamodel.semantic.other.ProgrammingLanguage;
import eu.cloud4soa.api.datamodel.semantic.inf.SoftwareComponent;
import eu.cloud4soa.api.datamodel.semantic.measure.NumericRange;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/paas-model#PaaSOffering")
public class PaaSOffering extends PlatformLayer {

	private List<Channel> communicationChannels=new ArrayList<Channel>();
	private List<PricingPolicy> pricingPolicies = new ArrayList<PricingPolicy>();
	private List<Application> hostsApplication=new ArrayList<Application>();
	private List<HardwareComponent> offeredHardwareComponents=new ArrayList<HardwareComponent>();
	private List<SoftwareComponent> offeredSoftware=new ArrayList<SoftwareComponent>();
	private List<Rating> rating =new ArrayList<Rating>();
	private java.lang.String status;
	private ProgrammingLanguage supportedLanguage;
	private java.lang.String uRL;
	private IaaSProvider usedInfrastructure;
	private java.lang.String title;
        private PaaSProvider paasProvider;
	private java.lang.String description;

        private NumericRange offerWebScalingFactor;
        private NumericRange offerComputeScalingFactor;
        private String slaId;
        private Boolean hasAdapet;
        
        //QoS
        private List<Technology_Service_Quality> providesServiceQuality = new ArrayList<Technology_Service_Quality>();


	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/paas-model#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#communicateThrough")
	public List<Channel> getCommunicationChannels() {
		return communicationChannels;
	}
	
	public void setCommunicationChannels( List<Channel> communicationChannels ) {
		this.communicationChannels = communicationChannels;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasPricingPolicy")
	public List<PricingPolicy> getPricingPolicies() {
		return pricingPolicies;
	}
	
	public void setPricingPolicies( List<PricingPolicy> pricingPolicies ) {
		this.pricingPolicies = pricingPolicies;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hostsApplication")
	public List<Application> getHostsApplication() {
		return hostsApplication;
	}
	
	public void setHostsApplication( List<Application> hostsApplication ) {
		this.hostsApplication = hostsApplication;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#offerHardwareComponent")
	public List<HardwareComponent> getOfferedHardwareComponents() {
		return offeredHardwareComponents;
	}
	
	public void setOfferedHardwareComponents( List<HardwareComponent> offeredHardwareComponents ) {
		this.offeredHardwareComponents = offeredHardwareComponents;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#offerSoftware")
	public List<SoftwareComponent> getOfferedSoftware() {
		return offeredSoftware;
	}
	
	public void setOfferedSoftware( List<SoftwareComponent> offeredSoftware ) {
		this.offeredSoftware = offeredSoftware;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasRating")
	public List<Rating> getRating() {
		return rating;
	}
	
	public void setRating( List<Rating> rating ) {
		this.rating = rating;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasStatus")
	public java.lang.String getStatus() {
		return status;
	}
	
	public void setStatus( java.lang.String status ) {
		this.status = status;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#supportLanguage")
	public ProgrammingLanguage getSupportedLanguage() {
		return supportedLanguage;
	}
	
	public void setSupportedLanguage( ProgrammingLanguage supportedLanguage ) {
		this.supportedLanguage = supportedLanguage;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasURL")
	public java.lang.String getURL() {
		return uRL;
	}
	
	public void setURL( java.lang.String uRL ) {
		this.uRL = uRL;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#useInfrastructure")
	public IaaSProvider getUsedInfrastructure() {
		return usedInfrastructure;
	}
	
	public void setUsedInfrastructure( IaaSProvider usedInfrastructure ) {
		this.usedInfrastructure = usedInfrastructure;
	}
    @RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String Name ) {
		this.title = Name;
	}
        
        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#providedByPaaSProvider")
	public PaaSProvider getPaaSProvider() {
		return paasProvider;
	}
	
	public void setPaaSProvider(PaaSProvider paasProvider ) {
		this.paasProvider = paasProvider;
	}

    @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return description;
	}
	
	public void setDescription( java.lang.String description ) {
		this.description = description;
	}

        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasSlaId")
	public java.lang.String getSlaId() {
		return slaId;
	}
	
	public void setSlaId( java.lang.String slaId ) {
		this.slaId = slaId;
	}
        
        //QoS
        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#providesServiceQuality")
        public List<Technology_Service_Quality> getProvidesServiceQuality() {
            return providesServiceQuality;
        }

        public void setProvidesServiceQuality(List<Technology_Service_Quality> providesServiceQuality) {
            this.providesServiceQuality = providesServiceQuality;
        }

        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#offerComputeScalingFactor")
        public NumericRange getOfferComputeScalingFactor() {
            return offerComputeScalingFactor;
        }

        public void setOfferComputeScalingFactor(NumericRange offerComputeScalingFactor) {
            this.offerComputeScalingFactor = offerComputeScalingFactor;
        }

        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#offerWebScalingFactor")
        public NumericRange getOfferWebScalingFactor() {
            return offerWebScalingFactor;
        }

        public void setOfferWebScalingFactor(NumericRange offerWebScalingFactor) {
            this.offerWebScalingFactor = offerWebScalingFactor;
        }
        
        @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasAdapter")
        public Boolean gethasAdapter() {
            return hasAdapet;
        }

        public void sethasAdapter(Boolean hasAdapter) {
            this.hasAdapet = hasAdapter;
        }

        
                
        
}
