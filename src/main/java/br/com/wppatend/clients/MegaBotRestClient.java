package br.com.wppatend.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import br.com.wppatend.services.ParametroService;
import br.com.wppatend.wpprequest.model.ApiBase64Message;
import br.com.wppatend.wpprequest.model.ApiStatus;
import br.com.wppatend.wpprequest.model.ApiTextMessage;

@Service
public class MegaBotRestClient {
		
	@Autowired
	private ParametroService parametroService;
	
	private static final Logger logger = LoggerFactory.getLogger(MegaBotRestClient.class);
	
	public void sendMessage(String phone, String body) {
		
		ApiTextMessage msg = new ApiTextMessage();
		msg.setBody(body);
		msg.setPhone(phone);
		String msgBody = new Gson().toJson(msg);
		String url = parametroService.getUrlApiMegaBot() + "/sendMessage?token="+parametroService.getTokenApiMegaBot();
		
		RestTemplate rest = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Cache-Control", "no-cache");
		
		
		HttpEntity<String> entity = new HttpEntity<String>(msgBody, headers);
		
		if(parametroService.isApiSendMsg()) {
			String response = rest.postForObject(url, entity, String.class);
			logger.info("API Service response: " + response);
		} else {
			logger.info(String.format("ApiURL: %1$s MsgBody: %2$s", url, msgBody));
		}
	}
	
	public void sendBase64File(String phone, String body, String filename, String caption) {
		
		ApiBase64Message msg = new ApiBase64Message();
		msg.setBody(body);
		msg.setCaption(caption);
		msg.setFilename(filename);
		msg.setPhone(phone);
		String msgBody = new Gson().toJson(msg);
		String url = parametroService.getUrlApiMegaBot() + "/sendfilebase64?token="+parametroService.getTokenApiMegaBot();
		
		RestTemplate rest = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Cache-Control", "no-cache");
		
		
		HttpEntity<String> entity = new HttpEntity<String>(msgBody, headers);
		
		if(parametroService.isApiSendMsg()) {
			String response = rest.postForObject(url, entity, String.class);
			logger.info("API Service response: " + response);
		} else {
			logger.info(String.format("ApiURL: %1$s MsgBody: %2$s", url, msgBody.substring(0, 60) + "..."));
		}
		
	}
	
	public ApiStatus getApiStatus() {
		String url = parametroService.getUrlApiMegaBot() + "/status?token="+parametroService.getTokenApiMegaBot();
		RestTemplate rest = new RestTemplate();
		return rest.getForObject(url, ApiStatus.class);
	}

}
