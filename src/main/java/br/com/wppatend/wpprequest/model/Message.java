package br.com.wppatend.wpprequest.model;

public class Message {
	
	private String id;
	private String type;
	private Boolean isGroup;
	private String body;
	private Long time;
	private Boolean fromMe;
	private String author;
	private String chatId;
	private SenderName senderName;
	private String profilePicThumbObj;
	private String status;
	private String filename;
	private String caption;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public SenderName getSenderName() {
		return senderName;
	}
	public void setSenderName(SenderName senderName) {
		this.senderName = senderName;
	}
	public String getProfilePicThumbObj() {
		return profilePicThumbObj;
	}
	public void setProfilePicThumbObj(String profilePicThumbObj) {
		this.profilePicThumbObj = profilePicThumbObj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getFromMe() {
		return fromMe;
	}
	public void setFromMe(Boolean fromMe) {
		this.fromMe = fromMe;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

}
