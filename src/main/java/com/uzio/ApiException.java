package com.uzio;

public class ApiException extends Exception {
   
	private static final long serialVersionUID = 1L;
	
	private int code = 0;

    public ApiException() {}

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable throwable, int code) {
        super(message, throwable);
        this.code = code;
    }
    
    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiException(String message, int code) {
        this(message, (Throwable) null, code);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}