package com.mariocairone.mule.anypoint.client;

import java.util.Map;

import com.jsoniter.any.Any;

import lombok.NonNull;

public interface AnypointClient {


	//############################################	
	//	 Cloudhub 
	//############################################	
	
	/**
	 * GET /cloudhub/api/v2/applications<br><br>
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment
	 * @param queryParams
	 * <br>
	 *	 - retrieveStatistics  (boolean) <br>
	 *   - period (number) <br>
	 *   - retrieveLogLevels (boolean)<br>
	 *   - retrieveTrackingSettings (boolean)<br>
	 *   - retrieveIpAddresses (boolean)<br>
	 * 
	 * @return Any
	 */
	public Any getCloudhubApplications(@NonNull String businessGroup,String environment,Map<String,Object> queryParams) ;	

	/**
	 * GET /cloudhub/api/v2/applications/:domain<br><br>
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment
	 * @param domain the Cloudhub application name
	 * @return Any
	 */
	public Any getCloudhubApplication(@NonNull String businessGroup,String environment,String domain) ;

	//############################################	
	//	 API Platform 
	//############################################	 	
	
	/**
	 * GET /accounts/api/organizations/:orgId/environments <br>
	 * @param businessGroup The name of the Business Group
	 * @param queryParams
	 * <br>
	 *	 - withCloudhubPermissions (boolean) <br>
	 *   - all (boolean) <br>
	 *   - query (string)<br>	  
	 * @return Any
	 */
	public Any getEnvironments(String businessGroup, Map<String,Object> queryParams) ;
	
	/**
	 * GET /apiplatform/repository/v2/organizations/:orgId/users<br><br>

	 * @param businessGroup The name of the Business Group
	 * @param queryParams
	 * <br>
	 *	 - search  (string) <br>
	 *   - includeCurrent (boolean) <br> 
	 * @return Any
	 */
	public Any getUsers(String businessGroup,Map<String,Object> queryParams);	
	
	/**
	 * GET /apiplatform/repository/v2/organizations/:orgId/applications<br><br>

	 * @param businessGroup The name of the Business Group
	 * @param queryParams
	 * <br>
	 *	 - includeContractsForApiVersion (boolean) <br>
	 *   - targetAdminSite  (boolen) <br>
	 *   - offset (integer, default 0)<br>
	 *   - limit (integer, default 100)<br>
	 *   - sort (string)<br>	
	 *   - ascending (boolean)<br>	
	 *   - query (string)<br>	 
	 * @return Any
	 */
	public Any getClientApplications(String businessGroup,Map<String,Object> queryParams);

	/**
	 * POST /apiplatform/repository/v2/organizations/:orgId/applications<br><br>
	 * @param businessGroup The name of the Business Group
	 * @param body The request body
	 * @return Any
	 */
	public Any createClientApplication(String businessGroup,Object body);
	
	/**
	 * DELETE /apiplatform/repository/v2/organizations/:orgId/applications/:applicationId<br><br>
	 * @param businessGroup The name of the Business Group
	 * @param applicationId The application id
	 * @return Any
	 */
	public Any deleteClientApplication(String businessGroup,Integer applicationId);

	/**
	 * GET /apiplatform/repository/v2/organizations/:orgId/applications/:applicationId<br><br>

	 * @param businessGroup The name of the Business Group
	 * @param applicationId the id of the client application 
	 * @param queryParams
	 * <br>
	 *	 - extended (boolean) <br>
	 *   - resolveTheme  (boolean) <br>
	 * @return Any
	 */
	public Any getClientApplication(String businessGroup,Integer applicationId,Map<String,Object> queryParams);

	/**
	 * PATCH /apiplatform/repository/v2/organizations/:orgId/applications/:applicationId<br><br>
	 * @param businessGroup The name of the Business Group
	 * @param applicationId the id of the client application to patch
	 * @param body The request body
	 * @return Any
	 */
	public Any patchClientApplication(String businessGroup,Integer applicationId,Object body);

	/**
	 * PUT /apiplatform/repository/v2/organizations/:orgId/applications/:applicationId<br><br>

	 * @param businessGroup The name of the Business Group
	 * @param applicationId the id of the client application to update
	 * @param body The request body
	 * @return Any
	 */
	public Any updateClientApplication(String businessGroup,Integer applicationId,Object body);

	/**
	 * POST /apiplatform/repository/v2/organizations/:orgId/applications/:applicationId/owners<br>
	 * @param businessGroup The name of the Business Group
	 * @param applicationId the id of the client application to update
	 * @param body The request body
	 * @return Any
	 */
	public Any addApplicationOwners(String businessGroup,Integer applicationId,Object body);
	
	//############################################	
	//	 API Manager 
	//############################################	
	
	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis<br>
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param queryParams
	 * <br>
	 *	 - autodiscoveryInstanceName (string) <br>
	 *   - assetId (string) <br>
	 *   - query (string)<br>
	 *   - productVersion(string)<br>
	 *   - groupId(string)<br>
	 *   - assetVersion(string) <br>
	 *   - instanceLabel(string)<br>
	 *   - offset(integer, default 0)<br>
	 *   - filters<br>
	 *   - ascending (boolean, default true) <br>
	 *   - sort (enum: name,createdDate,updatedDate , default name)<br>
	 *   - limit (integer, default 100) <br>
	 *   - autodiscoveryApiName (string)
	 * @return Any - A list of APIs for the Business Group in the environment
	 */
	public Any getApis(String businessGroup,String environment,Map<String,Object> queryParams);
	
	/**
	* GET https://anypoint.mulesoft.com/apimanager/api/v1/organizations/{organizationId}/environments/{environmentId}/apis/{environmentApiId}
	* <br>
	* Returns an API
	*
	* @param businessGroup The name of the Business Group
	* @param environment The environment name
	* @param apiId The api instance id
	* @param queryParams
	 * <br>
	 *	 - includeTlsContexts (boolean), default false <br>
	* @return Any
	*/
	public Any getApi(String businessGroup,String environment,Integer apiId,Map<String,Object> queryParams);
	
	/**
	 * PATCH https://anypoint.mulesoft.com/apimanager/api/v1/organizations/{organizationId}/environments/{environmentId}/apis/{environmentApiId}
	 * <br>
	 * Patch an API
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param body The request body
	 * @return Any
	 */
	public Any editApi(String businessGroup,String environment,Integer apiId, Object body);	

	/**
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @return Any
	 */
	public Any deleteApi(String businessGroup,String environment,Integer apiId);	
	
	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies	 

	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param queryParams
	 * <br>
	 *	 - standalone (boolean) <br>
	 *   - version (string) <br>
	 *   - fullinfo (boolean, default true)<br>
	 *   -  if-modified-since (number,Last modification date (EPOC))<br>
	 * @return Any - A list of policies for the API
	 */
	public Any getPolicies(String businessGroup,String environment,Integer apiId,Map<String,Object> queryParams);
	
	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies

	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param body The request body
	 * @return Any Any
	 */
	public Any applyPolicy(String businessGroup,String environment,Integer apiId, Object body);
	
	/**
	 *	PATCH /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies<br><br>
	 * 
	 * Currently, only updates in order field are supported.
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param body The request body - Example <br>
	 * <code>
	 * [
	 * 	{"id": 51,"order": 1},
	 * 	{"id": 63,"order": 2}
	 * ]
	 * </code>
	 * @return Any
	 */
	public Any patchPolicies(String businessGroup,String environment,Integer apiId, Object body);
	
	/**
	 *	PATCH /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies/:policyId <br>
	 *<br>	 
	 * Update the polciy configuration
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param polciyId The policy id
	 * @param body The request body
	 * @return Any
	 */
	public Any editPolicy(String businessGroup,String environment,Integer apiId,Integer polciyId, Object body);
	
	/**
	 *	DELETE /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies/:policyId	 
	 *<br>	 
	 * Unapply the poclicy 
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param polciyId The policy id
	 * @return Any
	 */
	public Any removePolicy(String businessGroup,String environment,Integer apiId, Integer polciyId);

	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/tiers<br>	 
	 * <br>
	 * Retrieves a list of tiers for the supplied API.
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param queryParams
	 * <br>
	 *   - query (string)<br>
	 *   - offset(integer, default 0)<br>
	 *   - ascending (boolean, default true) <br>
	 *   - sort (enum: name,createdDate,updatedDate , default name)<br>
	 *   - limit (integer, default 100) <br>
	 *   - active (boolean)
	 * @return Any
	 */
	public Any getTiers(String businessGroup,String environment,Integer apiId,Map<String,Object> queryParams);

	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/tiers<br>	 
	 * <br>
	 * Creates a sla tier for the API.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param body The request body
	 * @return Any
	 */
	public Any createTier(String businessGroup,String environment,Integer apiId, Object body);

	/**
	 *	PUT /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/tiers/:tierId<br>	 
	 * <br>	 
	 * Update a tier associated with an API.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param tierId The id of the Tier
	 * @param body The request body
	 * @return Any
	 */
	public Any editTier(String businessGroup,String environment,Integer apiId,Integer tierId, Object body);

	/**
	 *	DELETE /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/tiers/:tierId<br>	 
	 * <br>	 
	 * Delete a tier that has no active applications.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id
	 * @param tierId The id of the Tier
	 * @return Any
	 */
	public Any removeTier(String businessGroup,String environment,Integer apiId, Integer tierId);

	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts<br>	 
	 * <br>	 
	 * Delete a tier that has no active applications.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id	 
	 * @param queryParams
	 * <br>
	 *   - query (string)<br>
	 *   - offset(integer, default 0)<br>
	 *   - ascending (boolean, default true) <br>
	 *   - sort <br>
	 *   - limit (integer, default 100) <br>
	 *   - status (string) <br>
	 *   - coreServicesId (string) <br>
	 *   - includeExtraRedirection (boolean) <br>
	 *   - includeExtraApplicationData (boolean)
	 *   
	 * @return Any
	 */
	public Any getContracts(String businessGroup,String environment,Integer apiId, Map<String,Object> queryParams);

	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts<br>	 
	 * <br>	 
	 * Creates new contract between an API and the application.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id	 
	 * @param body The request body
	 * @return Any
	 */
	public Any createContract(String businessGroup,String environment,Integer apiId, Object body);

	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId<br>	 
	 * <br>	 
	 * Get the contract for the API with the provided id
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id	 
	 * @param contractId The id of the contract
	 * 
	 * @return Any	 
	 */	
	public Any getContract(String businessGroup,String environment,Integer apiId, Integer contractId);

	/**
	 *	PATCH /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId<br>	 
	 * <br>	 
	 * Patches contract conditions.
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id	 
	 * @param contractId The id of the contract
	 * @param body The request body
	 * @return Any
	 */
	public Any editContract(String businessGroup,String environment,Integer apiId,Integer contractId, Object body);

	/**
	 *	DELETE /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId<br>
	 * <br>
	 * Delete a contract 	 
	 * @param businessGroup The name of the Business Group
	 * @param environment The name of the environment 
	 * @param apiId The API instance id	 
	 * @param contractId The id of the contract
	 * @return Any
	 */
	public Any deleteContract(String businessGroup,String environment,Integer apiId, Integer contractId);

	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId/approve	 
	 * <br>
	 * Approve a Contract
	 * 		
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param contractId The id of the contract
	 * @return Any
	 */
	public Any approveContract(String businessGroup,String environment,Integer apiId, Integer contractId);

	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId/revoke	 
	 * <br>
	 * Revoke a contract
	 * 	
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param contractId The id of the contract
	 * @return Any
	 */
	public Any revokeContract(String businessGroup,String environment,Integer apiId, Integer contractId);

	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/contracts/:contractId/restore	 
	 * <br>
	 * Restore a contract
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param contractId The id of the contract
	 * @return Any
	 */
	public Any restoreContract(String businessGroup,String environment,Integer apiId, Integer contractId);
	
	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts	 
	 * <br>
	 * Get Alerts configured for the API
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @return Any
	 */
	public Any getAlerts(String businessGroup,String environment,Integer apiId);
	/**
	 *	POST /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts
	 * <br>
	 * Create an Alert for the API
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param body The request body
	 * @return Any
	 */
	
	public Any createAlert(String businessGroup,String environment,Integer apiId, Object body);
	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts/:alertId	 
	 * <br> 
	 * Get a Single alert
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param alertId The id of the alert
	 * @return Any
	 * */
	public Any getAlert(String businessGroup,String environment,Integer apiId, String alertId);

	/**
	 *	PATCH /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts/:alertId	 
	 * <br> 
	 * Update an Alert
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param alertId The id of the alert
	 * @param body The request body
	 * @return Any
	 */
	public Any editAlert(String businessGroup,String environment,Integer apiId,String alertId, Object body);

	/**
	 *	DELETE /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts/:alertId	 
	 * <br>
	 * Delete an Alert
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param alertId The id of the alert
	 * @return Any
	 */
	public Any deleteAlert(String businessGroup,String environment,Integer apiId, String alertId);

	/**
	 *	GET /apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/autodiscoveryProperties	 
	 * <br>
	 * Get Autodiscovery properties
	 * 
	 * @param businessGroup The name of the Business Group
	 * @param environment The environment name
	 * @param apiId The api instance id
	 * @param queryParams
	 * <br>
	 *	 - gatewayVersion (string) <br>
	 * @return Any
	 */
	public Any getAutodiscovery(String businessGroup,String environment,Integer apiId, Map<String,Object> queryParams);
	
	
}
