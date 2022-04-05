package com.example.main;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.main.repositories.EtatRepository;
import com.example.main.repositories.MessageRepository;
import com.example.main.repositories.SoldeRepository;
import com.example.main.repositories.TransfertRepository;
import com.example.main.services.EtatService;
import com.example.main.services.GSMService;
import com.example.main.services.MessageService;
import com.example.main.services.SettingService;
import com.example.main.services.SoldeService;
import com.example.main.services.TransfertService;
import com.example.main.utils.Config;
import com.example.main.utils.LogMessage;

@SpringBootApplication
@EnableScheduling
public class OnemartJobCronAppApplication implements CommandLineRunner {

	@Autowired
	EtatRepository etatRepo;
	@Autowired
	Validator validator;
	@Autowired
	EtatService etatService;
	@Autowired
	SoldeRepository soldeRepository;
	@Autowired
	GSMService gsmService;

	@Autowired
	TransfertService transfertService;

	@Autowired
	MessageService messageService;
	@Autowired SettingService settingService;

	@Autowired
	LogMessage logMessage;
	
	@Autowired
	TransfertRepository transfertRepository;
	@Autowired
	MessageRepository messageRepository;

	
	@Autowired
	SoldeService soldeService;
	
	@Autowired Config config;

	public static void main(String[] args) {
		SpringApplication.run(OnemartJobCronAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		transfertService.makeTransfert();
	}

}
