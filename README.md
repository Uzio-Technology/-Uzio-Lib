# Java library to support SSO into UZIO applications. This library is needed to obtain SSO JWT token from UZIO to github.

- Configure the following application properties in the client web application and create an instance of ClientConfiguration using these properties.
‘clientId’: Identifies a BT client in Uzio (to be shared by the Uzio team).
‘clientSecret’: BT-specific secret string required for signing the bearer token (to be shared by the Uzio team).
‘issuer’: Identifies the app that issued the bearer token (shared by the BT team with Uzio).
‘audience’: Recipient of the bearer token (to be shared by the Uzio team).
‘baseUrl’: Uzio app URL (to be shared by the Uzio team).
- Initialize ApiClient by passing ClientConfiguration instance.
- When an authenticated user in the client application clicks on the Uzio link, the client application should obtain the Uzio app redirect URL for that user. This can be achieved by making a call to com.uzio.ApiClient.getRedirectUrl(AuthDetails). The AuthDetails object is necessary to identify the user in the Uzio system.
- Perform a 302 HTTP redirect using this URL. The URL should lead the user to the Uzio application.


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

