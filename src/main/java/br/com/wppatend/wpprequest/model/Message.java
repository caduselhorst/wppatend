package br.com.wppatend.wpprequest.model;

public class Message {
	
	private String id;
	private String body;
	private Boolean fromMe;
	private Integer self;
	private Boolean isFoward;
	private String author;
	private Long time;
	private String chatId;
	private String type;
	private String senderName;
	private String caption;
	private String quotedMsgBody;
	private String chatName;
	private Boolean isBusiness;
	private Boolean isEnterprise;
	private Boolean isMyContact;
	private Boolean isWAContact;
	private ProfilePicThumbObj profilePicThumbObj;
	private Long lat;
	private Long lng;
	private Long loc;
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
	public Boolean getFromMe() {
		return fromMe;
	}
	public void setFromMe(Boolean fromMe) {
		this.fromMe = fromMe;
	}
	public Integer getSelf() {
		return self;
	}
	public void setSelf(Integer self) {
		this.self = self;
	}
	public Boolean getIsFoward() {
		return isFoward;
	}
	public void setIsFoward(Boolean isFoward) {
		this.isFoward = isFoward;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
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
	public Boolean getIsBusiness() {
		return isBusiness;
	}
	public void setIsBusiness(Boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	public Boolean getIsEnterprise() {
		return isEnterprise;
	}
	public void setIsEnterprise(Boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}
	public Boolean getIsMyContact() {
		return isMyContact;
	}
	public void setIsMyContact(Boolean isMyContact) {
		this.isMyContact = isMyContact;
	}
	public Boolean getIsWAContact() {
		return isWAContact;
	}
	public void setIsWAContact(Boolean isWAContact) {
		this.isWAContact = isWAContact;
	}
	public ProfilePicThumbObj getProfilePicThumbObj() {
		return profilePicThumbObj;
	}
	public void setProfilePicThumbObj(ProfilePicThumbObj profilePicThumbObj) {
		this.profilePicThumbObj = profilePicThumbObj;
	}
	public Long getLat() {
		return lat;
	}
	public void setLat(Long lat) {
		this.lat = lat;
	}
	public Long getLng() {
		return lng;
	}
	public void setLng(Long lng) {
		this.lng = lng;
	}
	public Long getLoc() {
		return loc;
	}
	public void setLoc(Long loc) {
		this.loc = loc;
	}

}
