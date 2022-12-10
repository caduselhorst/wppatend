package br.com.wppatend.wpprequest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebWppMessageId {

	private Boolean fromMe;
	private String remote;
	private String id;
	private String _serialized;
	
}
