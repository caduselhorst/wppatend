package br.com.wppatend.clients;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.ApiAcoesResponse;
import br.com.wppatend.wpprequest.model.ApiBase64Message;
import br.com.wppatend.wpprequest.model.ApiQRCode;
import br.com.wppatend.wpprequest.model.ApiSendMessageResponse;
import br.com.wppatend.wpprequest.model.ApiSengButtonMessage;
import br.com.wppatend.wpprequest.model.ApiSetWebHook;
import br.com.wppatend.wpprequest.model.ApiSetWebHookResponse;
import br.com.wppatend.wpprequest.model.ApiStatus;
import br.com.wppatend.wpprequest.model.ApiTextMessage;

@Service
public class MegaBotRestClient {
	
	
	@Autowired
	private ParametroService parametroService;
	
	private static final String API_LOGOUT_METHOD = "logout";
	private static final String API_TAKEOVER_METHOD = "takeover";
	private static final String API_SETWEBHOOK_METHOD = "setwebhook";
	private static final String API_GET_QRCODE_METHOD = "qrcode";
	private static final String API_STATUS_METHOD = "status";
	private static final String API_SEND_FILE_BASE64_METHOD = "sendfilebase64";
	private static final String API_SEND_MESSAGE_METHOD = "sendMessage";
	private static final String API_SEND_BUTTONS_MESSAGE_METHOD = "sendbuttonmessage";
	
	private final RestTemplate rest = new RestTemplate();
	
	private static final Logger logger = LoggerFactory.getLogger(MegaBotRestClient.class);
	
	public ApiSendMessageResponse sendMessage(String phone, String body) {
		
		ApiTextMessage msg = new ApiTextMessage();
		msg.setBody(body);
		msg.setPhone(phone);
		String msgBody = new Gson().toJson(msg);

		String url = getUrl(API_SEND_MESSAGE_METHOD);
		
		HttpHeaders headers = getHttpHeaders();
		
		
		HttpEntity<String> entity = new HttpEntity<String>(msgBody, headers);
		
		if(parametroService.isApiSendMsg()) {
			ApiSendMessageResponse response = rest.postForObject(url, entity, ApiSendMessageResponse.class);
			logger.info(response.toString());
			return response;
		} else {
			logger.info(String.format("ApiURL: %1$s MsgBody: %2$s", url, msgBody));
			ApiSendMessageResponse response = new ApiSendMessageResponse();
			response.setAccountStatus("true");
			response.setChatID("fasdsdfasdf");
			response.setMessage("Teste");
			response.setMessageID("dfasdfasdfasdf");
			return response;
		}
	}

	
	public ApiSendMessageResponse sendMessageButtons(String phone, String body, String description, List<String> buttons) {
		ApiSengButtonMessage message = new ApiSengButtonMessage();
		message.setButtons(buttons.toArray(new String[] {}));
		message.setDescription(description);
		message.setPhone(phone);
		message.setTitle(body);
		message.setType("text");
		
		String msgbody = new Gson().toJson(message);
		String url = getUrl(API_SEND_BUTTONS_MESSAGE_METHOD);
		HttpHeaders headers = getHttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(msgbody, headers);
		
		if(parametroService.isApiSendMsg()) {
			ApiSendMessageResponse response = rest.postForObject(url, entity, ApiSendMessageResponse.class);
			logger.info(response.toString());
			return response;
		} else {
			logger.info(String.format("ApiURL: %1$s MsgBody: %2$s", url, msgbody));
			return null;
		}
	}
	
	public void sendBase64File(String phone, String body, String filename, String caption) {
		
		ApiBase64Message msg = new ApiBase64Message();
		msg.setBody(body);
		msg.setCaption(caption);
		msg.setFilename(filename);
		msg.setPhone(phone);
		String msgBody = new Gson().toJson(msg);

		String url = getUrl(API_SEND_FILE_BASE64_METHOD);
		
		HttpHeaders headers = getHttpHeaders();
		
		HttpEntity<String> entity = new HttpEntity<String>(msgBody, headers);
		
		if(parametroService.isApiSendMsg()) {
			String response = rest.postForObject(url, entity, String.class);
			logger.info("API Service response: " + response);
		} else {
			logger.info(String.format("ApiURL: %1$s MsgBody: %2$s", url, msgBody.substring(0, 60) + "..."));
		}
		
	}
	
	public ApiStatus getApiStatus() {
		String url = getUrl(API_STATUS_METHOD);
		return rest.getForObject(url, ApiStatus.class);
	}
	
	public ApiQRCode getApiQRCode() {
		String url = getUrl(API_GET_QRCODE_METHOD);
		return rest.getForObject(url, ApiQRCode.class);
	}
	
	public ApiSetWebHookResponse setApiWebHook(ApiSetWebHook webhook) {
		
		String url = getUrl(API_SETWEBHOOK_METHOD);
		ApiSetWebHookResponse response = rest.postForObject(url, webhook, ApiSetWebHookResponse.class);
		
		return response;
		
	}
	
	public 	ApiAcoesResponse resetaApi() {
		String url = getUrl(API_TAKEOVER_METHOD);
		return rest.getForObject(url, ApiAcoesResponse.class);
	}
	
	public ApiAcoesResponse logout() {
		String url =  getUrl(API_LOGOUT_METHOD);
		return rest.getForObject(url, ApiAcoesResponse.class);
	}
	
	
	private String getUrl(String apiMethod) {
		return String.format("%s/%s?token=%s", parametroService.getUrlApiMegaBot(), apiMethod, parametroService.getTokenApiMegaBot());
	}
	 
	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Cache-Control", "no-cache");
		return headers;
	}

}
