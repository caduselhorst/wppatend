package br.com.wppatend.services;

public interface SecurityService {
	
	public String findLoggedInUserName();
	public void autoLogin(String username, String password);

}
