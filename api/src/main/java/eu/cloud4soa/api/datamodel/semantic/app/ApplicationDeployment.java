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
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;

/**
 *
 * @author vincenzo
 */
@RDFBean("http://www.enterprise-architecture.org/essential-metamodel.owl#Application_Deployment")
public class ApplicationDeployment extends ApplicationPhysical {

    private java.lang.String IP;
    private java.lang.String adapterUrl;
    private PaaSOffering paasOffering;
    private ApplicationDeployment migratedFrom;
    private String SLAid;

    @Override
    @RDFSubject(prefix = "http://www.enterprise-architecture.org/essential-metamodel.owl#Application_Deployment/")
    public String getUriId() {
        return super.getUriId();
    }

    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#hasIP")
    public String getIP() {
        return IP;
    }

    public void setIP(String ip) {
        this.IP = ip;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#hasAdapterUrl")
    public String getAdapterURL() {
        return adapterUrl;
    }

    public void setAdapterURL(String url) {
        this.adapterUrl = url;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#deploying_location")
    public PaaSOffering getDeployingLocation() {
        return paasOffering;
    }

    public void setDeployingLocation(PaaSOffering paasOffering) {
        this.paasOffering = paasOffering;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#migratedFrom")
    public ApplicationDeployment getMigratedFrom() {
        return migratedFrom;
    }

    public void setMigratedFrom(ApplicationDeployment migratedFrom) {
        this.migratedFrom = migratedFrom;
    }
    
    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#SLAid")
    public String getSLAcontractID() {
        return SLAid;
    }

    public void setSLAcontractID(String SLAid) {
        this.SLAid = SLAid;
    }
}
