package com.mariocairone.mule.anypoint.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.mariocairone.mule.anypoint.client.exception.AnypointClientException;
import lombok.NonNull;
import java.util.logging.Logger;

public class AnypointClientImpl implements AnypointClient {

	private static final String  DEBUG_PROPERTY_NAME = "anypoint.client.debug";
    
	public static String HTTPS_ANYPOINT_MULESOFT_COM = "https://anypoint.mulesoft.com";

	private PassiveExpiringMap<String, String> cache = new PassiveExpiringMap<>(30, TimeUnit.MINUTES);

	private Client restClient;

	private String username;

	private String password;

	public AnypointClientImpl(String username, String password) {
		super();
		this.restClient = ClientBuilder.newClient();
	
		configureClient(restClient);

		this.username = username;
		this.password = password;
	}


	private void configureClient(Client client) {
		 String debug = System.getProperty(DEBUG_PROPERTY_NAME);
		 boolean enabled = debug == null || !debug.equals("true")? false : true;
		 if(enabled) {
			 Logger clientLogger = Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME);
			 client.register(new LoggingFeature(clientLogger,
				        Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
		 }
		 client.register(MultiPartFeature.class);
	}
	
	@Override
	public Any getEnvironments(@NonNull String businessGroup, Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("accounts/api/organizations")
				.path(getOrgId(businessGroup)).path("environments");

		processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);

	}

	@Override
	public Any getCloudhubApplication(@NonNull String businessGroup, @NonNull String environment,
			@NonNull String domain) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("cloudhub/api/v2/applications")
				.path(domain);

		Response response = target.request().header("Authorization", getAuthorization())
				.header("X-ANYPNT-ENV-ID", getEnvId(businessGroup, environment)).accept(MediaType.APPLICATION_JSON)
				.get();

		return parseResponse(response);

	}

	@Override
	public Any getCloudhubApplications(@NonNull String businessGroup, @NonNull String environment,
			Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("cloudhub/api/v2/applications");

		processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.header("X-ANYPNT-ENV-ID", getEnvId(businessGroup, environment)).accept(MediaType.APPLICATION_JSON)
				.get();

		return parseResponse(response);

	}


	@Override
	public Any getClientApplications(@NonNull String businessGroup, Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any createClientApplication(@NonNull String businessGroup, @NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any getPolicies(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("policies");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any getApis(@NonNull String businessGroup, @NonNull String environment, Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}
	@Override
	public Any editApi(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId, @NonNull Object body) {
		
		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString());

		String payload = parseBody(body);
		
		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.build("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
		
		return parseResponse(response);
	}


	@Override
	public Any deleteApi(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId) {
		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString());

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);
	}


	@Override
	public Any getApi(@NonNull  String businessGroup, @NonNull String environment, @NonNull Integer apiId,Map<String, Object> queryParams) {
		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString());

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}
	
	@Override
	public Any applyPolicy(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("policies");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	@Override
	public Any patchPolicies(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("policies");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.build("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();

		return parseResponse(response);
	}

	@Override
	public Any editPolicy(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer polciyId, @NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("policies").path(polciyId.toString());

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.build("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();

		return parseResponse(response);

	}

	@Override
	public Any removePolicy(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer polciyId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("policies").path(polciyId.toString());

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);

	}

	@Override
	public Any getTiers(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("tiers");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any createTier(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("tiers");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	@Override
	public Any editTier(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer tierId, @NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("tiers").path(tierId.toString());

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).put(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	@Override
	public Any removeTier(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer tierId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("tiers").path(tierId.toString());

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);

	}

	@Override
	public Any getContracts(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any createContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any getContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString());

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);

	}

	@Override
	public Any editContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId, @NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString());

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.build("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();

		return parseResponse(response);

	}

	@Override
	public Any deleteContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString());

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);

	}

	@Override
	public Any getAlerts(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("alerts");

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any createAlert(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("alerts");

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	@Override
	public Any getAlert(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull String alertId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("alerts").path(alertId);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any editAlert(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull String alertId, @NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("alerts").path(alertId);

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.build("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();

		return parseResponse(response);
	}

	@Override
	public Any deleteAlert(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull String alertId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("alerts").path(alertId);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);
	}

	@Override
	public Any getAutodiscovery(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/api/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("autodiscoveryProperties");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any approveContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/xapi/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString()).path("approve");

		String payload = "{}";

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	@Override
	public Any revokeContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/xapi/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString()).path("revoke");

		String payload = "{}";

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any restoreContract(@NonNull String businessGroup, @NonNull String environment, @NonNull Integer apiId,
			@NonNull Integer contractId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("/apimanager/xapi/v1/organizations")
				.path(getOrgId(businessGroup)).path("environments").path(getEnvId(businessGroup, environment))
				.path("apis").path(apiId.toString()).path("contracts").path(contractId.toString()).path("restore");

		String payload = "{}";

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any deleteClientApplication(@NonNull String businessGroup, @NonNull Integer applicationId) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications")
				.path(applicationId.toString());

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).delete();

		return parseResponse(response);

	}

	@Override
	public Any getClientApplication(@NonNull String businessGroup, @NonNull Integer applicationId,Map<String,Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications")
				.path(applicationId.toString());

		target = processQueryParams(target, queryParams);
		
		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any patchClientApplication(String businessGroup, Integer applicationId, Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications")
				.path(applicationId.toString());

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization())
				.method("PATCH", Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any updateClientApplication(String businessGroup, Integer applicationId, Object body) {
		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications")
				.path(applicationId.toString());

		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).put(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	@Override
	public Any getUsers(@NonNull String businessGroup, Map<String, Object> queryParams) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("users");

		target = processQueryParams(target, queryParams);

		Response response = target.request().header("Authorization", getAuthorization())
				.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);
	}

	@Override
	public Any addApplicationOwners(@NonNull String businessGroup, @NonNull Integer applicationId,
			@NonNull Object body) {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM)
				.path("apiplatform/repository/v2/organizations").path(getOrgId(businessGroup)).path("applications")
				.path(applicationId.toString()).path("owners");

		
		String payload = parseBody(body);

		Response response = target.request().accept(MediaType.APPLICATION_JSON)
				.header("Authorization", getAuthorization()).post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);
	}

	private Any parseResponse(Response response) {

		String body = response.readEntity(String.class);
		
		Any any = body == null || body.isEmpty() ? Any.wrapNull():JsonIterator.deserialize(body);
		Response.StatusType statusInfo = response.getStatusInfo();
		if (statusInfo.getFamily() != Family.SUCCESSFUL) {		
			throw new AnypointClientException(statusInfo.getStatusCode(), statusInfo.getReasonPhrase());
		}

		
		return any;

	}

	private WebTarget processQueryParams(WebTarget target, Map<String, Object> queryParams) {

		if (queryParams == null)
			return target;

		for (String paramName : queryParams.keySet()) {
			target = target.queryParam(paramName, queryParams.get(paramName));
		}

		return target;
	}

	private String parseBody(Object body) {

		if (body instanceof String)
			return (String) body;

		return JsonStream.serialize(body);

	}

	private String getAuthorization() {

		if (cache.containsKey("access_token"))
			return cache.get("access_token");

		Any loginResponse = login(username, password);
		String token = "Bearer " + loginResponse.get("access_token").as(String.class);
		cache.put("access_token", token);
		return token;

	}

	private String getOrgId(String orgName) {

		String cacheKey = "bg." + orgName;

		if (cache.containsKey(cacheKey))
			return cache.get(cacheKey);

		Any me = me();

		Optional<Any> organization = me.get("user").get("contributorOfOrganizations").asList().stream()
				.filter(org -> org.get("name").as(String.class).equals(orgName)).findFirst();

		if (!organization.isPresent())
			throw new IllegalArgumentException(
					String.format("Business Group %s not found for the user %s", orgName, username));

		String orgId = organization.get().get("id").as(String.class);

		cache.put(cacheKey, orgId);

		return orgId;
	}

	private String getEnvId(String orgName, String envName) {

		String cacheKey = "env." + envName;

		if (cache.containsKey(cacheKey))
			return cache.get(cacheKey);

		Any environments = getEnvironments(orgName, null);

		Optional<Any> environment = environments.get("data").asList().stream()
				.filter(env -> env.get("name").as(String.class).equals(envName)).findFirst();

		if (!environment.isPresent())
			throw new IllegalArgumentException(
					String.format("Environment %s not found in Business Group %s", envName, orgName));

		String envId = environment.get().get("id").as(String.class);

		cache.put(cacheKey, envId);

		return envId;
	}

	private Any login(@NonNull String username, @NonNull String password) {

		LinkedHashMap<String, Object> loginValues = new LinkedHashMap<String, Object>();
		loginValues.put("username", username);
		loginValues.put("password", password);

		String payload = parseBody(loginValues);

		WebTarget target = restClient
								.target(HTTPS_ANYPOINT_MULESOFT_COM)
									.path("accounts/login");

		Response response = target
							.request()
								.accept(MediaType.APPLICATION_JSON)
							.post(Entity.entity(payload, MediaType.APPLICATION_JSON));

		return parseResponse(response);

	}

	
	private Any me() {

		WebTarget target = restClient.target(HTTPS_ANYPOINT_MULESOFT_COM).path("accounts/api/me");

		Response response = target
							.request()
								.header("Authorization", getAuthorization())
								.accept(MediaType.APPLICATION_JSON).get();

		return parseResponse(response);

	}





}
