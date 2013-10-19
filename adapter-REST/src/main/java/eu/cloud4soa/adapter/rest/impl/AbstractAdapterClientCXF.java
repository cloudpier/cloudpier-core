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


package eu.cloud4soa.adapter.rest.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.UnknownHostException;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.Version;
import eu.cloud4soa.adapter.rest.auth.AuthenticationPropertyExclusionProvider;
import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.common.HttpStatus;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.response.Response;
import eu.cloud4soa.adapter.rest.struct.AbstractSerializer;
import eu.cloud4soa.adapter.rest.util.ClassUtil;
import eu.cloud4soa.adapter.rest.util.EncryptionUtil;
import eu.cloud4soa.adapter.rest.util.RequestUtil;
import eu.cloud4soa.adapter.rest.util.Timer;
import java.net.URI;

/**
 * TODO implement authentication at the adapter use {@link #getCredentials()} to
 * access credentials (public/private key)
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public abstract class AbstractAdapterClientCXF extends AbstractSerializer {
	private final Logger log =  LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings({ "unchecked" })
	protected <T> T request(Request<T> request, Credentials credentials) throws AdapterClientException, UnknownHostException {
		request = verify(request, credentials);
		return ((T) invokeByRequestDefinition(request));
	}

	private <T> Request<T> verify(Request<T> request, Credentials credentials) throws AdapterClientException {
		try {
			return EncryptionUtil.encipher(request, credentials);
		} catch (Exception e) {
			throw new AdapterClientException(e.getMessage());
		}
	}

	private <T> Response<T> invokeByRequestDefinition(Request<T> t) throws AdapterClientException, UnknownHostException {

		try {
			HttpMethod method = getHttpMethod(t);
			switch (method) {
			case POST:
				return POST(t);
			case PUT:
				return PUT(t);
			case GET:
				return GET(t);
			case DELETE:
				return DELETE(t);
			default:
				throw new AdapterClientException("Method:{" + method + "} not implemented.");
			}
		} catch (UnknownHostException uhe) {
                        uhe.printStackTrace();
                        throw uhe;
                    //throw new AdapterClientException(uhe.getMessage());
		} catch (org.apache.cxf.jaxrs.client.ClientWebApplicationException cwae) {
			throw new AdapterClientException(cwae.getMessage());
		} catch (IOException ioe) {
			throw new AdapterClientException(ioe.getMessage());
		}
		}

	/**
	 * HTTP1.1 get method invocation
	 * 
	 * @param t
	 *            the request to send
	 * @return Response the response
	 * @throws IOException
	 */
	private <T> Response<T> GET(Request<T> t) throws IOException {
		String targetUrl = inquireUrl(t);
		WebClient client = createCXFInstance(targetUrl);
		client = setParams(client, t);
		client = setHeaders(client, t);
		
		Timer timer = Timer.tic();
		javax.ws.rs.core.Response cxfResponse = client.get();
                timer.toc();

		log.debug(getHttpMethod(t) + " " + inquireUrl(t) + " " + cxfResponse.getStatus() + " (took " + timer.getDifference() + " ms)");

		String serializedResponse = IOUtils.readStringFromStream((InputStream) cxfResponse.getEntity());

		Response<T> response = deserialize(serializedResponse, t);
		response.setResponseTime(timer.getDifference());
		response.setStatusCode(HttpStatus.getStatus(cxfResponse.getStatus()));
                if(cxfResponse.getStatus()<200 || cxfResponse.getStatus()>300){
                throw new IOException(serializedResponse);  
                }
                
		return response;
	}

	/**
	 * HTTP1.1 post method invocation
	 * 
	 * @param t
	 *            the request to send
	 * @return Response the response
	 * @throws IOException
	 */
	private <T> Response<T> POST(Request<T> t) throws IOException {
		String targetUrl = inquireUrl(t);
		WebClient client = setHeaders(createCXFInstance(targetUrl), t);
                
		String serialized = serialize(t);

		Timer timer = Timer.tic();
		
		javax.ws.rs.core.Response cxfResponse = client.post(serialized);
		timer.toc();

		log.debug(getHttpMethod(t) + " " + inquireUrl(t) + " " + cxfResponse.getStatus() + " (took " + timer.getDifference() + " ms)");

		String serializedResponse = IOUtils.readStringFromStream((InputStream) cxfResponse.getEntity());

		Response<T> response = deserialize(serializedResponse, t);

		response.setResponseTime(timer.getDifference());
		response.setStatusCode(HttpStatus.getStatus(cxfResponse.getStatus()));
                if(cxfResponse.getStatus()<200 || cxfResponse.getStatus()>300){
                 throw new IOException(serializedResponse);  
                }
                
		return response;
	}

	/**
	 * HTTP1.1 put method invocation
	 * 
	 * @param t
	 *            the request to send
	 * @return Response the response
	 * @throws IOException
	 */
	private <T> Response<T> PUT(Request<T> t) throws IOException {
		String targetUrl = inquireUrl(t);
		WebClient client = setHeaders(createCXFInstance(targetUrl), t);

		String serialized = serialize(t);

		Timer timer = Timer.tic();
		javax.ws.rs.core.Response cxfResponse = client.put(serialized);
		timer.toc();

		log.debug(getHttpMethod(t) + " " + inquireUrl(t) + " " + cxfResponse.getStatus() + " (took " + timer.getDifference() + " ms)");

		String serializedResponse = IOUtils.readStringFromStream((InputStream) cxfResponse.getEntity());

		Response<T> response = deserialize(serializedResponse, t);

		response.setResponseTime(timer.getDifference());
		response.setStatusCode(HttpStatus.getStatus(cxfResponse.getStatus()));
                if(cxfResponse.getStatus()<200 || cxfResponse.getStatus()>300){
                 throw new IOException(serializedResponse);  
                }
		return response;
	}

	/**
	 * HTTP1.1 delete method invocation
	 * 
	 * @param t
	 *            the request to send
	 * @return Response the response
	 * @throws IOException
	 */
	private <T> Response<T> DELETE(Request<T> t) throws IOException {
		String targetUrl = inquireUrl(t);
		WebClient client = createCXFInstance(targetUrl);
		client = setParams(client, t);
		client = setHeaders(client, t);
		
		Timer timer = Timer.tic();
		
		javax.ws.rs.core.Response cxfResponse = client.delete();
		timer.toc();

		log.debug(getHttpMethod(t) + " " + inquireUrl(t) + " " + cxfResponse.getStatus() + " (took " + timer.getDifference() + " ms)");

		String serializedResponse = IOUtils.readStringFromStream((InputStream) cxfResponse.getEntity());

		Response<T> response = deserialize(serializedResponse, t);

		response.setResponseTime(timer.getDifference());
		response.setStatusCode(HttpStatus.getStatus(cxfResponse.getStatus()));
                if(cxfResponse.getStatus()<200 || cxfResponse.getStatus()>300){
                 throw new IOException(serializedResponse);  
                }
		return response;
	}

	private WebClient createCXFInstance(String baseAddress) {
		WebClient client = WebClient.create(baseAddress).type("application/json").accept(MediaType.TEXT_PLAIN).accept(MediaType.APPLICATION_JSON);
        
        HTTPConduit conduit = WebClient.getConfig(client).getHttpConduit();
        conduit.getClient().setReceiveTimeout(600000);
        conduit.getClient().setConnectionTimeout(600000);                 
                
        return client;
        }

	private <T> WebClient setHeaders(WebClient client, Request<T> request) {
		if(client == null){
			throw new IllegalArgumentException("WebClient cannot be null.");
		}
		if(request != null){
			client.header("apiKey", request.getApiKey());
			client.header("apiVersion", ClassUtil.getClassAnnotationValue(request.getClass(), Version.class, "value", String.class));
			client.header("hash", request.getHash());
		}
		return client;
	}
	
	private <T> WebClient setParams(WebClient client, Request<T> request) {
		if(client == null){
			throw new IllegalArgumentException("WebClient cannot be null.");
		}
		if(request != null){
			Field[] fields = ClassUtil.getAllDeclaredFields(request.getClass(), AuthenticationPropertyExclusionProvider.EXCLUSIONS);
			for(Field field : fields){
				Object value = ClassUtil.getValueOfField(field, request);
				if(value != null){
					client.header(field.getName(), value);
				}
			}
		}
		return client;
	}

	private <T> String getUrlPath(Request<T> t) {
		StringBuffer urlBuffer = new StringBuffer();
		String adapterRoot = getAdapterRootPath();
		String urlPath = RequestUtil.resolveResourcePath(t);

		if (adapterRoot != null) {
			urlBuffer.append(adapterRoot);
		}
		if (urlPath != null) {
			urlBuffer.append(urlPath);
		}
		return urlBuffer.toString();
	}

	private <T> String inquireUrl(Request<T> request) {
		StringBuffer stringAppender = new StringBuffer();

		String baseUrl = interpolateProtocol(request.getBaseUrl());
		stringAppender.append(baseUrl);
		String path = getUrlPath(request);
		stringAppender.append(path);

		return stringAppender.toString();
	}

	private <T> String interpolateProtocol(String baseUrl) {
		if (baseUrl.startsWith("http://") || baseUrl.startsWith("https://")) {
			return baseUrl;
		}
		return "http://" + baseUrl;
	}

	private <T> HttpMethod getHttpMethod(Request<T> t) {
		HttpMethod method = ClassUtil.getClassAnnotationValue(t.getClass(), Method.class, "value", Method.HttpMethod.class);
		return method;
	}

	/**
	 * The (*nix) path on what the adapter can be found.
	 * 
	 * @return String the path on what the adapter lies
	 */
	public abstract String getAdapterRootPath();

	/**
	 * Validates the request.
	 * 
	 * @param request
	 *            the request to validate
	 * @return bool true if all fields annotated with <code>@NotNull</code> are
	 *         not null or not empty false otherwise
	 */
	public abstract <T> boolean validateAndInfixDefaultsIfPossible(Request<T> request);
}
