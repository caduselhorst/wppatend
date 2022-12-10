package br.com.wppatend.wpprequest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class WebWppMessage {

	private WebWppMessageData _data;
	private WebWppMessageId id;
	private Integer ack;
	private Boolean hasMedia;
	private String body;
	private String type;
	private Long timestamp;
	private String from;
	private String to;
	private String author;
	private String deviceType;
	private String forwardingScore;
	private Boolean isStatus;
	private Boolean isStarred;
	private Boolean broadcast;
	private Boolean fromMe;
	private Boolean hasQuotedMessage;
	private String[] vCards;
	private String[] mentionedIds;
	private Boolean isGif;
	private Boolean isEphemeral;
	
}
