package br.com.wppatend.wpprequest.model;

import lombok.Data;

@Data
public class Message {
	
	private String id;
	private String type;
	private Boolean isGroup;
	private String body;
	private Long time;
	private Boolean fromMe;
	private String author;
	private String chatId;
	private String messageId;
	private SenderName senderName;
	private String profilePicThumbObj;
	private String status;
	private String filename;
	private String caption;
	private String selectedButtonId;
	private String selectedDisplayText;
	

}
