package br.com.wppatend.wpprequest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebWppQuoteMessage {
	
	private String type;
	private String deprecatedMms3Url;
	private String directPath;
	private String staticUrl;
	private String mimetype;
	private String filehash;
	private String encFilehash;
	private Long size;
	private Long height;
	private Long width;
	private String mediaKey;
	private Long mediaKeyTimestamp;
	private String body;

}
