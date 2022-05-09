package com.example.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import com.example.main.entities.Etat;
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
		this.initEtat();
		this.initSettings();
		this.initConfiguration();
		//this.transfertService.makeTransfert();
		//this.transfertService.storeTransferts();
//		this.transfertService.makeTransfert();
		//this.transfertService.storeTransferts();
		//this.transfertService.makeTransfert();
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
			
			Setting setting = settingService.findByKey(obj.getString("setting_key"));
			
			if (setting == null) {
				
				setting = new Setting();
				setting.setDisplay(obj.getString("display_name"));
				setting.setKey(obj.getString("setting_key"));
				setting.setValue(obj.getString("value"));
				
				settingService.updateOrSave(setting);
			}
			
		}
		
	}
	
	private void initEtat() {
		
		String[] etats = {"EN COURS", "EXECUTE", "ECHOUE"};
		
		for (String etat : etats) {
			if(etatService.getEtatByName(etat) == null) {
				etatService.updateOrSave(Etat.builder().name(etat).build());
			}
		}
		
	}
	
	private void initSettings() {
		
		String[][] settingsValue = {
				{"gsmURL", "http://192.168.5.150/cgi/WebCGI?1500102=account=apiuser&password=apipass&port=1&content=", "GSM URL"},
				{"smsStorage", "http://localhost:8000/api/AddTransfertAndroid", "SMS STORAGE"},
				{"appOnlineURL", "http://localhost:8000/api/gsmlist", "APP ONLINE URL"},
				{"syntaxeSoldeURL", "http://localhost:8000/api/soldeSyntaxe", "Get syntaxe URL"},
				{"syntaxeTransfertURL","http://localhost:8000/api/transfertCabineSyntaxe","Transfert syntaxe URL"},
		};
		
		Setting s;

		for (String[] item : settingsValue) {
			
			String key = item[0];
			if(settingService.findByKey(key) == null) {
				s = Setting.builder()
						.key(key)
						.value(item[1])
						.display(item[2])
						.build();
				settingService.updateOrSave(s);
			}
		}
		
	}
	
	private void initConfiguration(){
		List<Setting> settings = settingService.allSettings();
		String key;
		String value;
		
		for(Setting item : settings) {
			key = item.getKey();
			value = item.getValue();
			String methodName = "set"+key.substring(0, 1).toUpperCase()+key.substring(1);
			Method method;
			try {
				method = config.getClass().getMethod(methodName, String.class);
				method.invoke(config, value);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		config.initConfig();
		
	}

}
