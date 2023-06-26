package com.uzio;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * AuthDetails are required to identify user in UZIO app
 *
 */
@Data
@Builder
public class AuthDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * username: Username/emailId of the user
	 */
	private String username;
	
	/**
	 * userType: Identifies UZIO portal : EMPLOYER, EMPLOYEE or BROKER	 * 
	 */
	private UserType userType;
	/**
	 * externalIdentifier: User identifier of your system maintained in UZIO app via API integration
	 */
	private String externalIdentifier;
	/**
	 * userId:  User identity in Uzio shared via UZIO API integration
	 */
	private String userId;
	/**
	 * employeeIdentifier: employee's identifier in UZIO system shared via API integration
	 */
	private String employeeIdentifier;
	/**
	 * employerIdentifier: employer's identifier in UZIO system shared via API integration
	 */
	private String employerIdentifier;
	/**
	 * brokerIdentifier: broker's identifier in UZIO system shared via API integration
	 */
	private String brokerIdentifier;
}
