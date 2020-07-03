package br.com.wppatend.wpprequest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	
	private String id;
	private String body;
	private boolean fromMe;
	private String self;
	private boolean isFoward;
	private String author;
	private long time;
	private String chatId;
	private String type;
	private String senderName;
	private String caption;
	private String quotedMsgBody;
	private String chatName;
	private boolean isBusiness;
	private boolean isEnterprise;
	private boolean isMyContact;
	private boolean isWAContact;
	private ProfilePicThumbObj profilePicThumbObj;
	private long lat;
	private long lng;
	private long loc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean isFromMe() {
		return fromMe;
	}
	public void setFromMe(boolean fromMe) {
		this.fromMe = fromMe;
	}
	public String getSelf() {
		return self;
	}
	public void setSelf(String self) {
		this.self = self;
	}
	public boolean isFoward() {
		return isFoward;
	}
	public void setFoward(boolean isFoward) {
		this.isFoward = isFoward;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getQuotedMsgBody() {
		return quotedMsgBody;
	}
	public void setQuotedMsgBody(String quotedMsgBody) {
		this.quotedMsgBody = quotedMsgBody;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public boolean isBusiness() {
		return isBusiness;
	}
	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	public boolean isEnterprise() {
		return isEnterprise;
	}
	public void setEnterprise(boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}
	public boolean isMyContact() {
		return isMyContact;
	}
	public void setMyContact(boolean isMyContact) {
		this.isMyContact = isMyContact;
	}
	public boolean isWAContact() {
		return isWAContact;
	}
	public void setWAContact(boolean isWAContact) {
		this.isWAContact = isWAContact;
	}
	public ProfilePicThumbObj getProfilePicThumbObj() {
		return profilePicThumbObj;
	}
	public void setProfilePicThumbObj(ProfilePicThumbObj profilePicThumbObj) {
		this.profilePicThumbObj = profilePicThumbObj;
	}
	public long getLat() {
		return lat;
	}
	public void setLat(long lat) {
		this.lat = lat;
	}
	public long getLng() {
		return lng;
	}
	public void setLng(long lng) {
		this.lng = lng;
	}
	public long getLoc() {
		return loc;
	}
	public void setLoc(long loc) {
		this.loc = loc;
	}

}
