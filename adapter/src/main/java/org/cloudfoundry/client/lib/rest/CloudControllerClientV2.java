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
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib.rest;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.UploadStatusCallback;
import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.archive.DirectoryApplicationArchive;
import org.cloudfoundry.client.lib.archive.ZipApplicationArchive;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudResource;
import org.cloudfoundry.client.lib.domain.CloudResources;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudServicePlan;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstanceState;
import org.cloudfoundry.client.lib.domain.InstanceStats;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.ServiceConfiguration;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.domain.UploadApplicationPayload;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.util.CloudEntityResourceMapper;
import org.cloudfoundry.client.lib.util.CloudUtil;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.cloudfoundry.client.lib.util.RestUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Empty implementation for cloud controller v2 REST API
 *
 * @author Thomas Risberg
 */
public class CloudControllerClientV2 extends AbstractCloudControllerClient {

	// This map only contains framework/runtime mapping for frameworks that we actively support
	private static Map<String, String[]> FRAMEWORK_RUNTIME_PATTERNS = new HashMap<String, String[]>() {{
		put("spring", new String[] {"java.*"});
		put("lift", new String[] {"java.*"});
		put("grails", new String[] {"java.*"});
		put("java_web", new String[] {"java.*"});
	}};

	private OauthClient oauthClient;

	private CloudSpace sessionSpace;

	private CloudEntityResourceMapper resourceMapper = new CloudEntityResourceMapper();

	private Map<String, UUID> runtimeIdCache = new HashMap<String, UUID>();

	private Map<String, UUID> frameworkIdCache = new HashMap<String, UUID>();

	public CloudControllerClientV2(URL cloudControllerUrl,
								   RestUtil restUtil,
								   CloudCredentials cloudCredentials,
								   URL authorizationEndpoint,
								   CloudSpace sessionSpace,
								   HttpProxyConfiguration httpProxyConfiguration) {
		super(cloudControllerUrl, restUtil, cloudCredentials, authorizationEndpoint, httpProxyConfiguration);
		this.oauthClient = restUtil.createOauthClient(authorizationEndpoint, httpProxyConfiguration);
		this.sessionSpace = sessionSpace;
	}

	@SuppressWarnings("unchecked")
	public CloudInfo getInfo() {
		String infoJson = getRestTemplate().getForObject(getUrl("/v2/info"), String.class);
		Map<String, Object> infoMap = JsonUtil.convertJsonToMap(infoJson);

		Map<String, Object> userMap = getUserInfo((String) infoMap.get("user"));

		//TODO: replace with v2 api call once, or if, they become available
		String infoV1Json = getRestTemplate().getForObject(getUrl("/info"), String.class);
		Map<String, Object> infoV1Map = JsonUtil.convertJsonToMap(infoV1Json);
		Map<String, Object> limitMap = (Map<String, Object>) infoV1Map.get("limits");
		Map<String, Object> usageMap = (Map<String, Object>) infoV1Map.get("usage");

		String name = CloudUtil.parse(String.class, infoMap.get("name"));
		String support = CloudUtil.parse(String.class, infoMap.get("support"));
		String authorizationEndpoint = CloudUtil.parse(String.class, infoMap.get("authorization_endpoint"));
		int build = CloudUtil.parse(Integer.class, infoMap.get("build"));
		String version = "" + CloudUtil.parse(Number.class, infoMap.get("version"));
		String description = CloudUtil.parse(String.class, infoMap.get("description"));

		CloudInfo.Limits limits = null;
		CloudInfo.Usage usage = null;
		boolean debug = false;
		Map<String, CloudInfo.Runtime> runtimes = null;
		Collection<CloudInfo.Framework> frameworks = null;
		if (token != null) {
			limits = new CloudInfo.Limits(limitMap);
			usage = new CloudInfo.Usage(usageMap);
			debug = CloudUtil.parse(Boolean.class, infoV1Map.get("allow_debug"));
			runtimes = getInfoForRuntimes();
			frameworks = getInfoForFrameworks(runtimes);
		}

		return new CloudInfo(name, support, authorizationEndpoint, build, version, (String)userMap.get("user_name"),
				description, limits, usage, debug, frameworks, runtimes);
	}

	public boolean supportsSpaces() {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CloudSpace> getSpaces() {
		String urlPath = "/v2/spaces?inline-relations-depth=1";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, null);
		List<CloudSpace> spaces = new ArrayList<CloudSpace>();
		for (Map<String, Object> resource : resourceList) {
			spaces.add(resourceMapper.mapResource(resource, CloudSpace.class));
		}
		return spaces;
	}

	public String login() {
		OAuth2AccessToken token = oauthClient.getToken(cloudCredentials.getEmail(),
				cloudCredentials.getPassword());
		this.token = token.getTokenType() + " " + token.getValue();
		return this.token;
	}

	public void logout() {
		token = null;
	}

	public void register(String email, String password) {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		oauthClient.changePassword(token, credentials.getPassword(), newPassword);
		CloudCredentials newCloudCredentials = new CloudCredentials(credentials.getEmail(), newPassword);
		if (cloudCredentials.getProxyUser() != null) {
			cloudCredentials = newCloudCredentials.proxyForUser(cloudCredentials.getProxyUser());
		} else {
			cloudCredentials = newCloudCredentials;
		}
	}

	public void unregister() {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	@SuppressWarnings("unchecked")
	public List<CloudService> getServices() {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlPath = urlPath + "/service_instances?inline-relations-depth={depth}";
		urlVars.put("depth", 2);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		List<CloudService> services = new ArrayList<CloudService>();
		for (Map<String, Object> resource : resourceList) {
			services.add(resourceMapper.mapResource(resource, CloudService.class));
		}
		return services;
	}

	public void createService(CloudService service) {
		Assert.notNull(sessionSpace, "Unable to create service without specifying space to use.");
		Assert.notNull(service, "Service must not be null");
		Assert.notNull(service.getName(), "Service name must not be null");
		Assert.notNull(service.getLabel(), "Service label must not be null");

		// until we have defaults - the version and plan are required
		Assert.notNull(service.getVersion(), "Service version must not be null");
		Assert.notNull(service.getPlan(), "Service plan must not be null");

		List<CloudServiceOffering> offerings = getServiceOfferings(service.getLabel());
		CloudServicePlan cloudServicePlan = null;
		for (CloudServiceOffering offering : offerings) {
			if (service.getVersion() != null || service.getVersion().equals(offering.getVersion())) {
				for (CloudServicePlan plan : offering.getCloudServicePlans()) {
					if (service.getPlan() != null && service.getPlan().equals(plan.getName())) {
						cloudServicePlan = plan;
						break;
					}
				}
			}
			if (cloudServicePlan != null) {
				break;
			}
		}
		Assert.notNull(cloudServicePlan, "Service Plan not found.");
		HashMap<String, Object> serviceRequest = new HashMap<String, Object>();
		serviceRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		serviceRequest.put("name", service.getName());
		serviceRequest.put("service_plan_guid", cloudServicePlan.getMeta().getGuid());
		getRestTemplate().postForObject(getUrl("/v2/service_instances"), serviceRequest, String.class);
	}

	@SuppressWarnings("unchecked")
	public CloudService getService(String serviceName) {
		String urlPath = "/v2";
		Map<String, Object> urlVars = new HashMap<String, Object>();
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlVars.put("q", "name:" + serviceName);
		urlPath = urlPath + "/service_instances?inline-relations-depth=2&q={q}";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		CloudService cloudService = null;
		if (resourceList.size() > 0) {
			cloudService = resourceMapper.mapResource(resourceList.get(0), CloudService.class);
		}
		return cloudService;
	}

	public void deleteService(String serviceName) {
		CloudService cloudService = getService(serviceName);
		doDeleteService(cloudService);
	}

	public void deleteAllServices() {
		List<CloudService> cloudServices = getServices();
		for (CloudService cloudService : cloudServices) {
			doDeleteService(cloudService);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServiceConfiguration> getServiceConfigurations() {
		String urlPath = "/v2/services?inline-relations-depth=1";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, null);
		List<ServiceConfiguration> serviceConfigurations = new ArrayList<ServiceConfiguration>();
		for (Map<String, Object> resource : resourceList) {
			CloudServiceOffering serviceOffering = resourceMapper.mapResource(resource, CloudServiceOffering.class);
			serviceConfigurations.add(new ServiceConfiguration(serviceOffering));
		}
		return serviceConfigurations;
	}

	@SuppressWarnings("unchecked")
	public List<CloudApplication> getApplications() {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlPath = urlPath + "/apps?inline-relations-depth={depth}";
		urlVars.put("depth", 2);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		List<CloudApplication> apps = new ArrayList<CloudApplication>();
		for (Map<String, Object> resource : resourceList) {
			apps.add(mapCloudApplication(resource));
		}
		return apps;
	}

	@SuppressWarnings("unchecked")
	public CloudApplication getApplication(String appName) {
		Map<String, Object> resource = findApplicationResource(appName, 2);
		if (resource == null) {
			throw new CloudFoundryException(HttpStatus.NOT_FOUND, "Not Found", "Application not found");
		}
		CloudApplication cloudApp = mapCloudApplication(resource);
		return cloudApp;
	}

	private CloudApplication mapCloudApplication(Map<String, Object> resource) {
		UUID appId = resourceMapper.getGuidOfResource(resource);
		CloudApplication cloudApp = null;
		if (resource != null) {
			int running = getRunningInstances(appId,
					CloudApplication.AppState.valueOf(
							CloudEntityResourceMapper.getEntityAttribute(resource, "state", String.class)));
			((Map<String, Object>)resource.get("entity")).put("running_instances", running);
			cloudApp = resourceMapper.mapResource(resource, CloudApplication.class);
			cloudApp.setUris(findApplicationUris(cloudApp.getMeta().getGuid()));
		}
		return cloudApp;
	}

	private int getRunningInstances(UUID appId, CloudApplication.AppState appState) {
		int running = 0;
		ApplicationStats appStats = doGetApplicationStats(appId, appState);
		if (appStats != null && appStats.getRecords() != null) {
			for (InstanceStats inst : appStats.getRecords()) {
				if (InstanceState.RUNNING == inst.getState()){
					running++;
				}
			}
		}
		return running;
	}

	public ApplicationStats getApplicationStats(String appName) {
		UUID appId = getAppId(appName);
		CloudApplication app = getApplication(appName);
		return doGetApplicationStats(appId, app.getState());

	}

	private ApplicationStats doGetApplicationStats(UUID appId, CloudApplication.AppState appState) {
		List<InstanceStats> instanceList = new ArrayList<InstanceStats>();
		if (appState.equals(CloudApplication.AppState.STARTED)) {
			Map<String, Object> respMap = getInstanceInfoForApp(appId, "stats");
			for (String instanceId : respMap.keySet()) {
				InstanceStats instanceStats =
						new InstanceStats(instanceId, (Map<String, Object>) respMap.get(instanceId));
				instanceList.add(instanceStats);
			}
		}
		return new ApplicationStats(instanceList);
	}

	private Map<String, Object> getInstanceInfoForApp(UUID appId, String path) {
		String url = getUrl("/v2/apps/{guid}/" + path);
		Map<String, Object> urlVars = new HashMap<String, Object>();
		urlVars.put("guid", appId);
		String resp = getRestTemplate().getForObject(url, String.class, urlVars);
		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		return respMap;
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris,
								  List<String> serviceNames, boolean checkExists) {
		if (checkExists) {
			try {
				getAppId(appName);
				return;
			} catch (HttpClientErrorException e) {
				if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
					throw e;
				}
			}
		}

		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		appRequest.put("name", appName);
		appRequest.put("framework_guid", getFrameworkId(staging.getFramework()));
		appRequest.put("runtime_guid", getRuntimeId(staging.getRuntime()));
		appRequest.put("memory", memory);
		appRequest.put("instances", 1);
		if (staging.getCommand() != null) {
			appRequest.put("command", staging.getCommand());
		}
		appRequest.put("state", CloudApplication.AppState.STOPPED);
		String appResp = getRestTemplate().postForObject(getUrl("/v2/apps"), appRequest, String.class);
		Map<String, Object> appEntity = JsonUtil.convertJsonToMap(appResp);
		UUID newAppGuid = CloudEntityResourceMapper.getMeta(appEntity).getGuid();

		if (serviceNames != null && serviceNames.size() > 0) {
			updateApplicationServices(appName, serviceNames);
		}

		if (uris != null && uris.size() > 0) {
			addUris(uris, newAppGuid);
		}

	}

	private List<Map<String, Object>> getAllResources(String urlPath, Map<String, Object> urlVars) {
		List<Map<String, Object>> allResources = new ArrayList<Map<String, Object>>();
		String resp;
		if (urlVars != null) {
			resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);
		} else {
			resp = getRestTemplate().getForObject(getUrl(urlPath), String.class);
		}
		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
		if (newResources != null && newResources.size() > 0) {
			allResources.addAll(newResources);
			for (Map<String, Object> res : newResources) {
				Map<String, Object> ent = (Map<String, Object>) res.get("entity");
			}
		}
		String nextUrl = (String) respMap.get("next_url");
		while (nextUrl != null && nextUrl.length() > 0) {
			nextUrl = addPageOfResources(nextUrl, allResources);
		}
		return allResources;
	}

	private String addPageOfResources(String nextUrl, List<Map<String, Object>> allResources) {
		String resp = getRestTemplate().getForObject(getUrl(nextUrl), String.class);
		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
		if (newResources != null && newResources.size() > 0) {
			allResources.addAll(newResources);
			for (Map<String, Object> res : newResources) {
				Map<String, Object> ent = (Map<String, Object>) res.get("entity");
			}
		}
		return (String) respMap.get("next_url");
	}

	private void addUris(List<String> uris, UUID appGuid) {
		Map<String, UUID> domains = getDomains();
		for (String uri : uris) {
			UUID domainGuid = null;
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			domainGuid = domains.get(uriInfo.get("domainName"));
			bindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	private void removeUris(List<String> uris, UUID appGuid) {
		Map<String, UUID> domains = getDomains();
		for (String uri : uris) {
			UUID domainGuid = null;
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			domainGuid = domains.get(uriInfo.get("domainName"));
			unbindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	private void extractUriInfo(Map<String, UUID> domains, String uri, Map<String, String> uriInfo) {
		URI newUri = URI.create(uri);
		String authority = newUri.getScheme() != null ? newUri.getAuthority(): newUri.getPath();
		for (String domain : domains.keySet()) {
			if (authority != null && authority.endsWith(domain)) {
				uriInfo.put("domainName", domain);
				if (domain.length() < authority.length()) {
					uriInfo.put("host", authority.substring(0, authority.indexOf(domain) - 1));
				}
				break;
			}
		}
		if (uriInfo.get("domainName") == null) {
			throw new IllegalArgumentException("Domain not found for URI " + uri);
		}
		if (uriInfo.get("host") == null) {
			throw new IllegalArgumentException("Invalid URI " + uri +
					" -- host not specified for domain " + uriInfo.get("domainName"));
		}
	}

	private Map<String, UUID> getDomains() {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		String domainPath = urlPath + "/domains?inline-relations-depth={depth}";
		urlVars.put("depth", 1);
		List<Map<String, Object>> resourceList = getAllResources(domainPath, urlVars);
		Map<String, UUID> domains = new HashMap<String, UUID>(resourceList.size());
		for (Map<String, Object> d : resourceList) {
			domains.put(
					CloudEntityResourceMapper.getEntityAttribute(d, "name", String.class),
					CloudEntityResourceMapper.getMeta(d).getGuid());
		}
		return domains;
	}

	private void bindRoute(String host, UUID domainGuid, UUID appGuid) {
		UUID routeGuid = getRouteGuid(host);
		if (routeGuid == null) {
			routeGuid = addRoute(host, domainGuid);
		}
		String bindPath = "/v2/apps/{app}/routes/{route}";
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("app", appGuid);
		bindVars.put("route", routeGuid);
		HashMap<String, Object> bindRequest = new HashMap<String, Object>();
		getRestTemplate().put(getUrl(bindPath), bindRequest, bindVars);
	}

	private void unbindRoute(String host, UUID domainGuid, UUID appGuid) {
		UUID routeGuid = getRouteGuid(host);
		if (routeGuid != null) {
			String bindPath = "/v2/apps/{app}/routes/{route}";
			Map<String, Object> bindVars = new HashMap<String, Object>();
			bindVars.put("app", appGuid);
			bindVars.put("route", routeGuid);
			getRestTemplate().delete(getUrl(bindPath), bindVars);
		}
	}

	private UUID getRouteGuid(String host) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String routePath = "/v2/routes?inline-relations-depth={depth}&q=host:{host}";
		urlVars.put("depth", 0);
		urlVars.put("host", host);
		List<Map<String, Object>> routes = getAllResources(routePath, urlVars);
		UUID routeGuid = null;
		if (routes.size() > 0) {
			Map<String, Object> r  = routes.get(0);
			routeGuid = CloudEntityResourceMapper.getMeta(r).getGuid();
		}
		return routeGuid;
	}

	private UUID addRoute(String host, UUID domainGuid) {
		Assert.notNull(sessionSpace, "Unable to add route without specifying space to use.");

		HashMap<String, Object> routeRequest = new HashMap<String, Object>();
		routeRequest.put("host", host);
		routeRequest.put("domain_guid", domainGuid);
		routeRequest.put("organization_guid", sessionSpace.getOrganization().getMeta().getGuid());
		String routeResp = getRestTemplate().postForObject(getUrl("/v2/routes"), routeRequest, String.class);
		Map<String, Object> routeEntity = JsonUtil.convertJsonToMap(routeResp);
		UUID newRouteGuid = CloudEntityResourceMapper.getMeta(routeEntity).getGuid();
		return newRouteGuid;
	}

	public void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException {
		Assert.notNull(file, "File must not be null");
		if (file.isDirectory()) {
			ApplicationArchive archive = new DirectoryApplicationArchive(file);
			uploadApplication(appName, archive, callback);
		} else {
			ZipFile zipFile = new ZipFile(file);
			try {
				ApplicationArchive archive = new ZipApplicationArchive(zipFile);
				uploadApplication(appName, archive, callback);
			} finally {
				zipFile.close();
			}
		}
	}

	public void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback)
			throws IOException {
		Assert.notNull(appName, "AppName must not be null");
		Assert.notNull(archive, "Archive must not be null");
		UUID appId = getAppId(appName);

		if (callback == null) {
			callback = UploadStatusCallback.NONE;
		}
		CloudResources knownRemoteResources = getKnownRemoteResources(archive);
		callback.onCheckResources();
		callback.onMatchedFileNames(knownRemoteResources.getFilenames());
		UploadApplicationPayload payload = new UploadApplicationPayload(archive, knownRemoteResources);
		callback.onProcessMatchedResources(payload.getTotalUncompressedSize());
		HttpEntity<?> entity = generatePartialResourceRequest(payload, knownRemoteResources);
		String url = getUrl("/v2/apps/{guid}/bits");
		getRestTemplate().put(url, entity, appId);
	}

	private CloudResources getKnownRemoteResources(ApplicationArchive archive) throws IOException {
		CloudResources archiveResources = new CloudResources(archive);
		String json = JsonUtil.convertToJson(archiveResources);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(JSON_MEDIA_TYPE);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		ResponseEntity<String> responseEntity =
			getRestTemplate().exchange(getUrl("/v2/resource_match"), HttpMethod.PUT, requestEntity, String.class);
		List<CloudResource> cloudResources = JsonUtil.convertJsonToCloudResourceList(responseEntity.getBody());
		return new CloudResources(cloudResources);
	}

	private HttpEntity<MultiValueMap<String, ?>> generatePartialResourceRequest(UploadApplicationPayload application,
			CloudResources knownRemoteResources) throws IOException {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(2);
		body.add("application", application);
		ObjectMapper mapper = new ObjectMapper();
		String knownRemoteResourcesPayload = mapper.writeValueAsString(knownRemoteResources);
		body.add("resources", knownRemoteResourcesPayload);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		return new HttpEntity<MultiValueMap<String, ?>>(body, headers);
	}

	public void startApplication(String appName) {
		CloudApplication app = getApplication(appName);
		if (app.getState() != CloudApplication.AppState.STARTED) {
			HashMap<String, Object> appRequest = new HashMap<String, Object>();
			appRequest.put("state", CloudApplication.AppState.STARTED);
			getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, app.getMeta().getGuid());
		}
	}

	public void debugApplication(String appName, CloudApplication.DebugMode mode) {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public void stopApplication(String appName) {
		CloudApplication app = getApplication(appName);
		if (app.getState() != CloudApplication.AppState.STOPPED) {
			HashMap<String, Object> appRequest = new HashMap<String, Object>();
			appRequest.put("state", CloudApplication.AppState.STOPPED);
			getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, app.getMeta().getGuid());
		}
	}

	public void restartApplication(String appName) {
		stopApplication(appName);
		startApplication(appName);
	}

	public void deleteApplication(String appName) {
		UUID appId = getAppId(appName);
		doDeleteApplication(appId);
	}

	public void deleteAllApplications() {
		List<CloudApplication> cloudApps = getApplications();
		for (CloudApplication cloudApp : cloudApps) {
			doDeleteApplication(cloudApp.getMeta().getGuid());
		}
	}

	public void updateApplicationMemory(String appName, int memory) {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("memory", memory);
		getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, appId);
	}

	public void updateApplicationInstances(String appName, int instances) {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("instances", instances);
		getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, appId);
	}

	public void updateApplicationServices(String appName, List<String> services) {
		CloudApplication app = getApplication(appName);
		List<UUID> addServices = new ArrayList<UUID>();
		List<UUID> deleteServices = new ArrayList<UUID>();
		// services to add
		for (String serviceName : services) {
			if (!app.getServices().contains(serviceName)) {
				CloudService cloudService = getService(serviceName);
				if (cloudService != null) {
					addServices.add(cloudService.getMeta().getGuid());
				}
				else {
					throw new CloudFoundryException(HttpStatus.NOT_FOUND, "Service with name " + serviceName +
							" not found in current space " + sessionSpace.getName());
				}
			}
		}
		// services to delete
		for (String serviceName : app.getServices()) {
			if (!services.contains(serviceName)) {
				CloudService cloudService = getService(serviceName);
				if (cloudService != null) {
					deleteServices.add(cloudService.getMeta().getGuid());
				}
			}
		}
		for (UUID serviceId : addServices) {
			doBindService(app.getMeta().getGuid(), serviceId);
		}
		for (UUID serviceId : deleteServices) {
			doUnbindService(app.getMeta().getGuid(), serviceId);
		}
	}

	private void doBindService(UUID appId, UUID serviceId) {
		HashMap<String, Object> serviceRequest = new HashMap<String, Object>();
		serviceRequest.put("service_instance_guid", serviceId);
		serviceRequest.put("app_guid", appId);
		getRestTemplate().postForObject(getUrl("/v2/service_bindings"), serviceRequest, String.class);
	}

	private void doUnbindService(UUID appId, UUID serviceId) {
		UUID serviceBindingId = getServiceBindingId(appId, serviceId);
		getRestTemplate().delete(getUrl("/v2/service_bindings/{guid}"), serviceBindingId);
	}

	public void updateApplicationStaging(String appName, Staging staging) {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		if (staging.getCommand() != null) {
			appRequest.put("command", staging.getCommand());
		}
		appRequest.put("framework_guid", getFrameworkId(staging.getFramework()));
		appRequest.put("runtime_guid", getRuntimeId(staging.getRuntime()));
		getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, appId);
	}

	public void updateApplicationUris(String appName, List<String> uris) {
		CloudApplication app = getApplication(appName);
		List<String> newUris = new ArrayList<String>(uris);
		newUris.removeAll(app.getUris());
		List<String> removeUris = app.getUris();
		removeUris.removeAll(uris);
		addUris(newUris, app.getMeta().getGuid());
		removeUris(removeUris, app.getMeta().getGuid());
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("environment_json", env);
		getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, appId);
	}

	public void updateApplicationEnv(String appName, List<String> env) {
		Map<String, String> envHash = new HashMap<String, String>();
		for (String s : env) {
			if (!s.contains("=")) {
				throw new IllegalArgumentException("Environment setting without '=' is invalid: " + s);
			}
			String key = s.substring(0, s.indexOf('=')).trim();
			String value = s.substring(s.indexOf('=') + 1).trim();
			envHash.put(key, value);
		}
		updateApplicationEnv(appName, envHash);
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition) {
		UUID appId = getAppId(appName);
		String urlPath = "/v2/apps/{appId}/instances/{instanceIndex}/files/{filePath}";
		return doGetFile(urlPath, appId, instanceIndex, filePath, startPosition, endPosition);
	}

	public void bindService(String appName, String serviceName) {
		CloudService cloudService = getService(serviceName);
		UUID appId = getAppId(appName);
		doBindService(appId, cloudService.getMeta().getGuid());
	}

	public void unbindService(String appName, String serviceName) {
		CloudService cloudService = getService(serviceName);
		UUID appId = getAppId(appName);
		doUnbindService(appId, cloudService.getMeta().getGuid());
	}

	public InstancesInfo getApplicationInstances(String appName) {
		UUID appId = getAppId(appName);
		CloudApplication app = getApplication(appName);
		List<Map<String, Object>> instanceList = new ArrayList<Map<String, Object>>();
		if (app.getState().equals(CloudApplication.AppState.STARTED)) {
			Map<String, Object> respMap = getInstanceInfoForApp(appId, "instances");
			List<String> keys = new ArrayList<String>(respMap.keySet());
			java.util.Collections.sort(keys);
			for (String instanceId : keys) {
				Integer index;
				try {
					index = Integer.valueOf(instanceId);
				} catch (NumberFormatException e) {
					index = -1;
				}
				Map<String, Object> instanceMap = (Map<String, Object>) respMap.get(instanceId);
				instanceMap.put("index", index);
				instanceList.add(instanceMap);
			}
		}
		return new InstancesInfo(instanceList);
	}

	public CrashesInfo getCrashes(String appName) {
		throw new UnsupportedOperationException("Feature is not yet implemented.");
	}

	public void rename(String appName, String newName) {
		UUID appId = getAppId(appName);
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("name", newName);
		getRestTemplate().put(getUrl("/v2/apps/{guid}"), appRequest, appId);
	}

	private void doDeleteService(CloudService cloudService) {
		List<UUID> appIds = getAppsBoundToService(cloudService);
		if (appIds.size() > 0) {
			for (UUID appId : appIds) {
				doUnbindService(appId, cloudService.getMeta().getGuid());
			}
		}
		getRestTemplate().delete(getUrl("/v2/service_instances/{guid}"), cloudService.getMeta().getGuid());
	}

	private List<UUID> getAppsBoundToService(CloudService cloudService) {
		List<UUID> appGuids = new ArrayList<UUID>();
		String urlPath = "/v2";
		Map<String, Object> urlVars = new HashMap<String, Object>();
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlVars.put("q", "name:" + cloudService.getName());
		urlPath = urlPath + "/service_instances?inline-relations-depth=2&q={q}";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		for (Map<String, Object> resource : resourceList) {
			List<Map<String, Object>> bindings =
					CloudEntityResourceMapper.getEntityAttribute(resource, "service_bindings", List.class);
			for (Map<String, Object> binding : bindings) {
				String appId = CloudEntityResourceMapper.getEntityAttribute(binding, "app_guid", String.class);
				if (appId != null) {
					appGuids.add(UUID.fromString(appId));
				}
			}
		}
		return appGuids;
	}

	private void doDeleteApplication(UUID appId) {
		getRestTemplate().delete(getUrl("/v2/apps/{guid}"), appId);
	}

	@SuppressWarnings("unchecked")
	private Collection<CloudInfo.Framework> getInfoForFrameworks(Map<String, CloudInfo.Runtime> runtimes) {
		List<Map<String, Object>> resourceList = getAllResources("/v2/frameworks", null);
		Collection<CloudInfo.Framework> frameworks = new ArrayList<CloudInfo.Framework>();
		for (Map<String, Object> frameworkMap : resourceList) {
			Map<String, Object> frameworkEntity = (Map<String, Object>) frameworkMap.get("entity");
			String frameworkName = String.valueOf(frameworkEntity.get("name"));
			String[] runtimePatterns = FRAMEWORK_RUNTIME_PATTERNS.get(frameworkName);
			List<CloudInfo.Runtime> runtimeList = new ArrayList<CloudInfo.Runtime>();
			if (runtimePatterns != null) {
				for (String runtimePattern : runtimePatterns) {
					Pattern pattern = Pattern.compile(runtimePattern);
					for (String runtimeName : runtimes.keySet()) {
						Matcher matcher = pattern.matcher(runtimeName);
						if (matcher.find()) {
							runtimeList.add(runtimes.get(runtimeName));
						}
					}
				}
			}
			CloudInfo.Framework framework = new CloudInfo.Framework(frameworkEntity, runtimeList);
			frameworks.add(framework);
		}
		return frameworks;
	}

	@SuppressWarnings("unchecked")
	private Map<String, CloudInfo.Runtime> getInfoForRuntimes() {
		List<Map<String, Object>> runtimesList = getAllResources("/v2/runtimes", null);
		Map<String, CloudInfo.Runtime> runtimes = new HashMap<String, CloudInfo.Runtime>();
		for (Map<String, Object> runtimeMap : runtimesList) {
			Map<String, Object> runtimeEntity = (Map<String, Object>) runtimeMap.get("entity");
			CloudInfo.Runtime runtime = new CloudInfo.Runtime(runtimeEntity);
			runtimes.put(runtime.getName(), runtime);
		}
		return runtimes;
	}

	@SuppressWarnings("unchecked")
	private List<CloudServiceOffering> getServiceOfferings(String label) {
		Assert.notNull(label, "Service label must not be null");
		List<Map<String, Object>> resourceList = getAllResources("/v2/services?inline-relations-depth=2", null);
		List<CloudServiceOffering> results = new ArrayList<CloudServiceOffering>();
		for (Map<String, Object> resource : resourceList) {
			CloudServiceOffering cloudServiceOffering =
					resourceMapper.mapResource(resource, CloudServiceOffering.class);
			if (cloudServiceOffering.getLabel() != null && label.equals(cloudServiceOffering.getLabel())) {
				results.add(cloudServiceOffering);
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private UUID getServiceBindingId(UUID appId, UUID serviceId ) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		urlVars.put("guid", appId);
		List<Map<String, Object>> resourceList = getAllResources("/v2/apps/{guid}/service_bindings", urlVars);
		UUID serviceBindingId = null;
		if (resourceList != null && resourceList.size() > 0) {
			for (Map<String, Object> resource : resourceList) {
				Map<String, Object> bindingMeta = (Map<String, Object>) resource.get("metadata");
				Map<String, Object> bindingEntity = (Map<String, Object>) resource.get("entity");
				String serviceInstanceGuid = (String) bindingEntity.get("service_instance_guid");
				if (serviceInstanceGuid != null && serviceInstanceGuid.equals(serviceId.toString())) {
					String bindingGuid = (String) bindingMeta.get("guid");
					serviceBindingId = UUID.fromString(bindingGuid);
					break;
				}
			}
		}
		return serviceBindingId;
	}

	@SuppressWarnings("unchecked")
	private UUID getAppId(String appName) {
		Map<String, Object> resource = findApplicationResource(appName, 1);
		UUID guid = null;
		if (resource != null) {
			Map<String, Object> appMeta = (Map<String, Object>) resource.get("metadata");
			guid = UUID.fromString(String.valueOf(appMeta.get("guid")));
		}
		return guid;
	}

	private Map<String, Object> findApplicationResource(String appName, int depth) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlVars.put("q", "name:" + appName);
		urlPath = urlPath + "/apps?inline-relations-depth={depth}&q={q}";
		urlVars.put("depth", depth);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		if (resourceList.size() > 0) {
			return resourceList.get(0);
		}
		else {
			return null;
		}
	}

	private List<String> findApplicationUris(UUID appGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/apps/{app}/routes?inline-relations-depth={depth}";
		urlVars.put("app", appGuid);
		urlVars.put("depth", 2);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		List<String> uris =  new ArrayList<String>();
		for (Map<String, Object> resource : resourceList) {
			Map<String, Object> domainResource = CloudEntityResourceMapper.getEmbeddedResource(resource, "domain");
			String uri = CloudEntityResourceMapper.getEntityAttribute(resource, "host", String.class) + "." +
					CloudEntityResourceMapper.getEntityAttribute(domainResource, "name", String.class);
			uris.add(uri);
		}
		return uris;
	}

	@SuppressWarnings("unchecked")
	private UUID getFrameworkId(String framework) {
		if (!frameworkIdCache.containsKey(framework)) {
			List<Map<String, Object>> resourceList = getAllResources("/v2/frameworks", null);
			for (Map<String, Object> resource : resourceList) {
				String name = resourceMapper.getNameOfResource(resource);
				UUID guid = resourceMapper.getGuidOfResource(resource);
				frameworkIdCache.put(name, guid);
			}
		}
		return frameworkIdCache.get(framework);
	}

	@SuppressWarnings("unchecked")
	private UUID getRuntimeId(String runtime) {
		if (!runtimeIdCache.containsKey(runtime)) {
			List<Map<String, Object>> resourceList = getAllResources("/v2/runtimes", null);
			for (Map<String, Object> resource : resourceList) {
				String name = resourceMapper.getNameOfResource(resource);
				UUID guid = resourceMapper.getGuidOfResource(resource);
				runtimeIdCache.put(name, guid);
			}
		}
		return runtimeIdCache.get(runtime);
	}

	@SuppressWarnings("unused")
	private Map<String, Object> getUserInfo(String user) {
//		String userJson = getRestTemplate().getForObject(getUrl("/v2/users/{guid}"), String.class, user);
//		Map<String, Object> userInfo = (Map<String, Object>) JsonUtil.convertJsonToMap(userJson);
//		return userInfo();
		//TODO: remove this temporary hack once the /v2/users/ uri can be accessed by mere mortals
		String userJson = "{}";
		if (token != null) {
			int x = token.indexOf('.');
			int y = token.indexOf('.', x + 1);
			String encodedString = token.substring(x + 1, y);
			try {
				byte[] decodedBytes = new sun.misc.BASE64Decoder().decodeBuffer(encodedString);
				userJson = new String(decodedBytes, 0, decodedBytes.length, "UTF-8");
			} catch (IOException e) {}
		}
		return(JsonUtil.convertJsonToMap(userJson));
	}

}
