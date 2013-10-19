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


package org.cloudfoundry.client.lib.util;

import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.rest.LoggingRestTemplate;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

/**
 * Some helper utilities for creating classes used for the REST support.
 *
 * @author: Thomas Risberg
 */
public class RestUtil {

	public RestTemplate createRestTemplate(HttpProxyConfiguration httpProxyConfiguration) {
		RestTemplate restTemplate = new LoggingRestTemplate();
		restTemplate.setRequestFactory(createRequestFactory(httpProxyConfiguration));
		return restTemplate;
	}

	public ClientHttpRequestFactory createRequestFactory(HttpProxyConfiguration httpProxyConfiguration) {
		CommonsClientHttpRequestFactory requestFactory = new CommonsClientHttpRequestFactory();
		if (httpProxyConfiguration != null) {
			requestFactory.getHttpClient().getHostConfiguration().setProxy(httpProxyConfiguration.getProxyHost(),
					httpProxyConfiguration.getProxyPort());
		}
                requestFactory.setConnectTimeout(Integer.MAX_VALUE);
                requestFactory.setReadTimeout(Integer.MAX_VALUE);
		return requestFactory;
	}

	public OauthClient createOauthClient(URL authorizationUrl, HttpProxyConfiguration httpProxyConfiguration) {
		return new OauthClient(authorizationUrl, createRestTemplate(httpProxyConfiguration));
	}
}
