package com.uzio;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class AuthRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private AuthDetails authDetails;

	private ClientConfiguration clientConfiguration;

}
