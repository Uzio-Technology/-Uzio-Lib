package com.uzio;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;
	
	private String errorMessage;
	
	private String errorCode;
}
