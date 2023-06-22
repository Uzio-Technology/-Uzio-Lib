package com.uzio;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private UserType userType;
	private String externalIdentifier;
	private String userId;
}
