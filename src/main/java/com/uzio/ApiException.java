package com.uzio;

public class ApiException extends Exception {
   
	private static final long serialVersionUID = 1L;
	
	private String  errorCode;
	
	private String  errorMessage;

    public ApiException() {}

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String message) {
        super(message);
        this.errorMessage=message;
    }

    public ApiException(String message, Throwable throwable, String code) {
        super(message, throwable);
        this.errorCode = code;
        this.errorMessage=message;
    }
    
    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorMessage=message;
    }


    public ApiException(String code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMessage=message;
    }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ApiException [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}

  

}