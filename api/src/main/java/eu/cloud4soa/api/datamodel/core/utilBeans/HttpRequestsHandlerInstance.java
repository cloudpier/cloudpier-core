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

import eu.cloud4soa.api.datamodel.semantic.inf.HttpRequestsHandler;
import eu.cloud4soa.api.datamodel.semantic.measure.NumericRange;
import java.util.List;

/**
 *
 * @author vins
 */
public class HttpRequestsHandlerInstance extends HardwareComponentInstance {
//    private Box box;

    public HttpRequestsHandlerInstance() {
        this.hardwareComponent = new HttpRequestsHandler();
    }

    public HttpRequestsHandlerInstance(HttpRequestsHandler httpRequestsHandler) {
        this.hardwareComponent = httpRequestsHandler;
    }

    private HttpRequestsHandler getHttpRequestsHandler() {
        return (HttpRequestsHandler) this.hardwareComponent;
    }

    public String getUriId() {
        return hardwareComponent.getUriId();
    }

    public void setUriId(String uriId) {
        hardwareComponent.setUriId(uriId);
    }

    public String getTitle() {
        return getHttpRequestsHandler().getTitle();
    }

    public void setTitle(String title) {
        getHttpRequestsHandler().setTitle(title);
    }

    public String getDescription() {
        return getHttpRequestsHandler().getDescription();
    }

    public void setDescription(String description) {
        getHttpRequestsHandler().setDescription(description);
    }

    public NumericRange getHTTPRequestsRange() {
        return getHttpRequestsHandler().getHTTPRequests();
    }

    public void setHTTPRequestsRange(NumericRange httprequests) {
        getHttpRequestsHandler().setHTTPRequests(httprequests);
    }

    public Float getMaxHTTPRequests() {
        return getHttpRequestsHandler().getHTTPRequests().getMax();
    }

    public void setMaxHTTPRequests(Float httprequests) {
        if (getHttpRequestsHandler().getHTTPRequests() == null) {
            getHttpRequestsHandler().setHTTPRequests(new NumericRange());
        }
        getHttpRequestsHandler().getHTTPRequests().setMax(httprequests);
    }

    public Float getMinHTTPRequests() {
        return getHttpRequestsHandler().getHTTPRequests().getMin();
    }

    public void setMinHTTPRequests(Float httprequests) {
        if (getHttpRequestsHandler().getHTTPRequests() == null) {
            getHttpRequestsHandler().setHTTPRequests(new NumericRange());
        }
        getHttpRequestsHandler().getHTTPRequests().setMin(httprequests);
    }

    public Float getStepHTTPRequests() {
        return getHttpRequestsHandler().getHTTPRequests().getStep();
    }

    public void setStepHTTPRequests(Float httprequests) {
        if (getHttpRequestsHandler().getHTTPRequests() == null) {
            getHttpRequestsHandler().setHTTPRequests(new NumericRange());
        }
        getHttpRequestsHandler().getHTTPRequests().setStep(httprequests);
    }

    public List<Float> getHTTPRequestsValues() {
        if (getHttpRequestsHandler().getHTTPRequests() != null) {
            return getHttpRequestsHandler().getHTTPRequests().getOfferedNumericValues();
        }
        return null;
    }

    public void setHTTPRequestsValues(List<Float> httpRequestsValues) {
        if (getHttpRequestsHandler().getHTTPRequests() == null) {
            getHttpRequestsHandler().setHTTPRequests(new NumericRange());
        }
        getHttpRequestsHandler().getHTTPRequests().setOfferedNumericValues(httpRequestsValues);
    }

    public void addCoreValue(Float httpRequestsValue) {
        if (getHttpRequestsHandler().getHTTPRequests() == null) {
            getHttpRequestsHandler().setHTTPRequests(new NumericRange());
        }
        getHttpRequestsHandler().getHTTPRequests().getOfferedNumericValues().add(httpRequestsValue);
    }
}
