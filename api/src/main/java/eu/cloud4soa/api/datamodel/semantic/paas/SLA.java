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

import eu.cloud4soa.api.datamodel.semantic.user.Developer;
import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.Thing;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/paas-model#SLA")
public class SLA extends PlatformLayer {

	private Developer involvesDeveloper;
	private PaaSOffering involvesPaaSOffering;
	private java.lang.String obligation;
	private List<PricingPolicy> relatedPricingPolicy=new ArrayList<PricingPolicy>();
	private java.lang.String validityPeriod;





	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/paas-model#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#involvesDeveloper")
	public Developer getInvolvesDeveloper() {
		return involvesDeveloper;
	}
	
	public void setInvolvesDeveloper( Developer involvesDeveloper ) {
		this.involvesDeveloper = involvesDeveloper;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#involvesPaaSOffering")
	public PaaSOffering getInvolvesPaaSOffering() {
		return involvesPaaSOffering;
	}
	
	public void setInvolvesPaaSOffering( PaaSOffering involvesPaaSOffering ) {
		this.involvesPaaSOffering = involvesPaaSOffering;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#obligation")
	public java.lang.String getObligation() {
		return obligation;
	}
	
	public void setObligation( java.lang.String obligation ) {
		this.obligation = obligation;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#relatedPricingPolicy")
	public List<PricingPolicy> getRelatedPricingPolicy() {
		return relatedPricingPolicy;
	}
	
	public void setRelatedPricingPolicy( List<PricingPolicy> relatedPricingPolicy ) {
		this.relatedPricingPolicy = relatedPricingPolicy;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#validityPeriod")
	public java.lang.String getValidityPeriod() {
		return validityPeriod;
	}
	
	public void setValidityPeriod( java.lang.String validityPeriod ) {
		this.validityPeriod = validityPeriod;
	}

}
