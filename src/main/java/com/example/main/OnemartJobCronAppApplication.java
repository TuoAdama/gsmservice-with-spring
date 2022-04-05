package com.example.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Validator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.main.entities.Setting;
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
		this.storeSettings();
		this.initConfig();
	}
	
	public void storeSettings() throws IOException {
		
		Path path = Paths.get("settings.json");
		
		if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			throw new FileNotFoundException("Fichiers settings non trouvé !");
		}
		
		List<String> lines = Files.readAllLines(path);
		String jsonString = String.join("", lines);
		
		JSONArray jsonArray = new JSONArray(jsonString);
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			
			Setting setting = settingService.getByKey(obj.getString("setting_key"));
			
			if (setting == null) {
				
				setting = new Setting();
				setting.setDetails(obj.getString("details"));
				setting.setDisplayName(obj.getString("display_name"));
				setting.setSettingKey(obj.getString("setting_key"));
				setting.setValue(obj.getString("value"));
				
				settingService.updateOrSave(setting);
			}
			
		}
		
	}
	
	private void initConfig() {
		this.config.setGsmURL(settingService.getByKey("gsm_url").getValue());
		this.config.setTransfertSimpleSyntaxe(settingService.getByKey("transfert_simple_syntaxe").getValue());
		this.config.setTransfertsOnlineURL(settingService.getByKey("transferts_online_url").getValue());
		this.config.setSmsStorage(settingService.getByKey("sms_storage").getValue());
		this.config.setSecretCode(settingService.getByKey("secret_code").getValue());
		this.config.setSoldeSyntaxe(settingService.getByKey("consultation_solde_syntaxe").getValue());		
	}

}
