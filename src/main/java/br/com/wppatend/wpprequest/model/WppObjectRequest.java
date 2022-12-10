package br.com.wppatend.wpprequest.model;

import java.util.List;

import lombok.Data;

@Data
public class WppObjectRequest {
	
	private List<Option> options;
	private List<Message> messages;
	private List<Info> info;

}
