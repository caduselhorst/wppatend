package br.com.wppatend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.wppatend.entities.Chat;
import br.com.wppatend.repositories.ChatRepository;
import br.com.wppatend.services.ChatService;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	private ChatRepository repository;

	@Override
	public Page<Chat> findChatByProtocoloId(Long protocoloId, Integer pageNumber, Integer totalP) {
		
		PageRequest pageRequest =
                PageRequest.of(pageNumber - 1, totalP, Sort.Direction.ASC, "idchat");
		
		
		return repository.findByprotocolo(protocoloId, pageRequest);
	}

	@Override
	public Chat save(Chat chat) {
		return repository.save(chat);
	}

}
