package br.com.wppatend.wpprequest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiQRCode {
	
	private String accountStatus;
	private String state;
	private String qrCode;
	private String message;

}
