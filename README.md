# Java library to support SSO into UZIO applications. This library is needed to obtain SSO JWT token from UZIO to github.

# Create com.uzio.ApiClient instance using ClientConfiguration details as shared by UZIO team.
# Obtain UZIO app redirect URL for a user by calling com.uzio.ApiClient.getRedirectUrl(AuthDetails). AuthDetails are required to identify the user in UZIO system.

Examples: 

# Initializing ApiClient:
		ClientConfiguration cc = ClientConfiguration.builder().clientId(clientId)
				.audience(audience)
				.issuer(issuer)
				.clientSecret(clientSecret)
				.baseUrl(baseUrl).build();
		ApiClient apiClient = new ApiClient(cc);
		
# Obtaining UZIO app redirect URL for a user:		
		AuthDetails authDetails = AuthDetails.builder().username(username)
    			.userType(user.getUserType()).build();
		try {
			String redirectUrl  = apiClient.getRedirectUrl(authDetails);
			return new RedirectView(redirectUrl);
		} catch (ApiException ex) {
			log.error("error_code: "+e.getErrorCode() +" , error_message: "+ e.getErrorMessage(),ex);
		}

