package com.example.main.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class LogMessage {
	
	LocalDateTime dateTime;
	DateTimeFormatter format;
	
	public void showLog(String message){
		dateTime = LocalDateTime.now();
		format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		System.out.println(format.format(dateTime)+" : "+message);
	}

}
