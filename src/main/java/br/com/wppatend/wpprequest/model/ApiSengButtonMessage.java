package br.com.wppatend.wpprequest.model;

import lombok.Data;

@Data
public class ApiSengButtonMessage {

	private String phone;
	private String title;
	private String description;
	private String type;
	private String[] buttons;
	
}
