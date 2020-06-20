package br.com.wppatend.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiReturn {
	
	private boolean error;
	private String message;
	private Object returns;
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getReturns() {
		return returns;
	}
	public void setReturns(Object returns) {
		this.returns = returns;
	}
	
}
