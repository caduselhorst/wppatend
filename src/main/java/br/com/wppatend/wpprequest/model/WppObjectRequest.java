package br.com.wppatend.wpprequest.model;

import java.util.List;

import com.google.gson.Gson;

public class WppObjectRequest {
	
	private List<Option> options;
	private List<Message> messages;
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}

}
