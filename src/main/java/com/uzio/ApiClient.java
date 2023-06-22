package com.uzio;

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
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class ApiClient {

	private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
	
	private RestTemplate restTemplate;
	
	private ClientConfiguration clientConfiguration;

	public ApiClient(ClientConfiguration clientConfiguration) {
		restTemplate = new RestTemplate();
		this.clientConfiguration = clientConfiguration;
	}
	
	public ApiClient() {
		super();
		restTemplate = new RestTemplate();		
	}
	
	public String getRedirectUrl(AuthDetails authDetails) throws Exception {
		String authToken = getToken(authDetails);
		return getRedirectUrl(authToken);		
	}
	
	private String getRedirectUrl(String authToken) throws ApiException {
		return clientConfiguration.getBaseUrl()+AuthConstants.APP_INTEGRATIONS_CALLBACK_PATH+"?code="+authToken;
	} 
	
	private String getToken(AuthDetails authDetails) throws ApiException {

		if (authDetails == null) {
			throw new ApiException(
					"Incomplete AuthRequest. Please provide all the required fields: clientConfiguration and userDetails.");
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

		} catch (Exception ex) {
			logger.error("AuthRequest could not be processed. Failed to generate token.", ex);
			throw new ApiException("AuthRequest could not be processed. Failed to generate token", ex);
		}

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			return responseEntity.getBody().getToken();
		} else {
			throw new ApiException(
					responseEntity.getBody().getErrorMessage(), responseEntity.getBody().getErrorCode());
		}


	}

}
