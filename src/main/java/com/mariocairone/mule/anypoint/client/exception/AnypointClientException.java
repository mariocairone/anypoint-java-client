package com.mariocairone.mule.anypoint.client.exception;

public class AnypointClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6740138080402626148L;
	
	private int statusCode;
	private String reason;

	public AnypointClientException(int statusCode, String reason) {
		super( statusCode + " - " + reason + ". Please set system property anypoint.client.debug=true for full details");
		this.statusCode = statusCode;
		this.reason = reason;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}

	public String getReason() {
		return this.reason;
	}

}
