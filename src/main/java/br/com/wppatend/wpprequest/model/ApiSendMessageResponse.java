package br.com.wppatend.wpprequest.model;

import lombok.Data;

@Data
public class ApiSendMessageResponse {
	
	private String accountStatus;
	private String chatID;
	private String messageID;
	private String message;

}
