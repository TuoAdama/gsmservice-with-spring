package com.example.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entities.Message;
import com.example.main.repositories.MessageRepository;

@Service
public class MessageService {

	@Autowired
	MessageRepository messageRepository;
	
	public Message store(Message message){
		return messageRepository.save(message);
	}
	
}
