package com.uzio;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientConfiguration implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String clientId;
	@JsonIgnore
	private String clientSecret;
	private String issuer;
	private String audience;
	@JsonIgnore
	private String baseUrl;
	
}
