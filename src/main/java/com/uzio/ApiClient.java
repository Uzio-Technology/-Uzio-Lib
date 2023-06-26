package com.uzio;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ApiClient is required to connect to UZIO APIs
 * Initialize this client with ClientConfiguration object with details shared by UZIO team
 *
 */
public class ApiClient {

	private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
	
	private RestTemplate restTemplate;
	
	private ClientConfiguration clientConfiguration;
	
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param clientConfiguration details to be shared by UZIO team
	 */
	public ApiClient(ClientConfiguration clientConfiguration) {
		restTemplate = new RestTemplate();
		this.clientConfiguration = clientConfiguration;
	}
	
	/**
	 * clientConfiguration should be set explicitly
	 */
	public ApiClient() {
		super();
		restTemplate = new RestTemplate();		
	}
	
	/**
	 * @param authDetails
	 * @return UZIO SSO URL to be used for 302 http redirect
	 * @throws ApiException
	 */
	public String getRedirectUrl(AuthDetails authDetails) throws ApiException {
		if(clientConfiguration==null || clientConfiguration.getAudience()==null ||
				clientConfiguration.getBaseUrl()==null ||
				clientConfiguration.getClientId()==null ||
				clientConfiguration.getClientSecret()==null ||
				clientConfiguration.getIssuer()==null) {
			throw new ApiException("clientConfiguration_missing", "Please provide all fields in ClientConfiguration.");
		}
		String authToken = getToken(authDetails);
		return getRedirectUrl(authToken, authDetails.getUserType().name());		
	}
	
	/**
	 * @param authToken 
	 * @param userType
	 * @return UZIO SSO URL to be used for 302 http redirect
	 * @throws ApiException
	 */
	private String getRedirectUrl(String authToken, String userType) throws ApiException{
		
		String state = clientConfiguration.getBaseUrl()+"/"+userType.toLowerCase();
		try {
			String encodedState = Base64.getEncoder().encodeToString(state.getBytes("UTF-8"));
			return clientConfiguration.getBaseUrl()+AuthConstants.APP_INTEGRATIONS_CALLBACK_PATH+"?code="+authToken+"&state="+encodedState;
		}
		catch (UnsupportedEncodingException ex) {
			throw new ApiException("UTF-8 encoding not supported", ex, "encoding_nunsupported");
		}

	} 
	
	/**
	 * @param authDetails
	 * @return SSO Token from UZIO Auth app
	 * @throws ApiException
	 */
	private String getToken(AuthDetails authDetails) throws ApiException {

		if (authDetails == null || authDetails.getUserType()==null || (authDetails.getBrokerIdentifier()==null &&
				authDetails.getEmployeeIdentifier()==null &&
				authDetails.getEmployerIdentifier()==null &&
				authDetails.getUserId()==null &&
				authDetails.getUsername()==null &&
				authDetails.getExternalIdentifier()==null)) {
			throw new ApiException("AuthDetails are required. Please provide userType and atleast one user identifier - userId, username, "
					+ "externalIdentifier, employerIdentifier, employerIdentifier, or brokerIdentifier.");
		}

		ResponseEntity<AuthResponse> responseEntity = null;

		try {

			Algorithm algorithm = Algorithm.HMAC256(clientConfiguration.getClientSecret());
			String token = JWT.create().withIssuer(clientConfiguration.getIssuer())
					.withAudience(clientConfiguration.getAudience()).withIssuedAt(new Date())
					.withExpiresAt(new Date(System.currentTimeMillis() + AuthConstants.CLIENT_JWT_EXPIRATION * 1000))
					.withJWTId(UUID.randomUUID().toString()).sign(algorithm);

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			AuthRequest authRequest =AuthRequest.builder().authDetails(authDetails).clientConfiguration(clientConfiguration).build();

			HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(authRequest, headers);

			responseEntity = restTemplate.exchange(
					authRequest.getClientConfiguration().getBaseUrl()+AuthConstants.APP_INTEGRATIONS_TOKEN_GEN_PATH, HttpMethod.POST, requestEntity,
					AuthResponse.class);
			
		} catch (HttpClientErrorException ex) {
			logger.error("AuthRequest could not be processed. Failed to generate token.", ex);
			String response = ex.getResponseBodyAsString();
			if(StringUtils.hasText(response)) {
				AuthResponse authResponse = null;
				try {
					authResponse = mapper.readValue(response, AuthResponse.class);
				} catch (Exception e) {
					logger.warn("Failed to parse error response.", e);
				} 
				if(authResponse!=null && authResponse.getErrorMessage()!=null) {
					throw new ApiException(authResponse.getErrorCode(),
							authResponse.getErrorMessage());
				}					

			}			
			throw new ApiException("AuthRequest could not be processed. Failed to generate token", ex, "token_generation_failed");
		} catch (RestClientException ex) {
			logger.error("AuthRequest could not be processed. Failed to generate token.", ex);
			throw new ApiException("AuthRequest could not be processed. Failed to generate token", ex, "token_generation_failed");
		}

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			return responseEntity.getBody().getToken();
		} else {
			throw new ApiException(
					 responseEntity.getBody().getErrorCode(),
					 responseEntity.getBody().getErrorMessage());
		}


	}

}
