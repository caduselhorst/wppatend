package br.com.wppatend.services;

import org.springframework.data.domain.Page;

import br.com.wppatend.entities.Chat;

public interface ChatService {
	
	public Page<Chat> findChatByProtocoloId(Long protocoloId, Integer pageNumber, Integer totalP);
	public Chat save(Chat chat);

}
