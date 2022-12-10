package br.com.wppatend.wpprequest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebWppMessageData {
	
	private WebWppMessageId id;
	private String body;
	private String type;
	private Long t;
	private String notifyName;
	private String from;
	private String to;
	private String author;
	private String self;
	private Integer ack;
	private Boolean isNewMsg;
	private Boolean star;
	private Boolean kicNotified;
	private Boolean recvFresh;
	private Boolean isFromTemplate;
	private Integer richPreviewType;
	private Boolean pollInvalidated;
	private Boolean broadcast;
	private WebWppQuoteMessage quotedMsg;
	private String quotedStanzaID;
	private String quotedParticipant;
	private String[] mentionedJidList;
	private Boolean isVcardOverMmsDocument;
	private Boolean hasReaction;
	private String inviteGrpType;
	private Boolean productHeaderImageRejected;
	private Integer lastPlaybackProgress;
	private Boolean isDynamicReplyButtonsMsg;
	private Boolean isMdHistoryMsg;
	private Integer stickerSentTs;
	private Boolean requiresDirectConnection;
	private Boolean pttForwardedFeaturesEnabled;
	private Boolean isEphemeral;
	private Boolean isStatusV3;

}
