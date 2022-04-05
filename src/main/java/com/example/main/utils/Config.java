package com.example.main.utils;

import org.springframework.stereotype.Component;

import com.example.main.entities.Setting;
import com.example.main.services.SettingService;

import lombok.Data;

@Component
@Data
public class Config {
	
	private String gsmURL;
	private String transfertSimpleSyntaxe;
	private String secretCode;
	private String smsStorage;
	private String transfertsOnlineURL;
	private String soldeSyntaxe;

	public void updateConfig(Setting set) {
		
		String settingKey = set.getSettingKey();;
		
		switch (settingKey) {
		case "transfert_simple_syntaxe":
			this.transfertSimpleSyntaxe = set.getValue();
			break;
		case "secret_code":
			this.secretCode = set.getValue();
			break;
		case "transferts_online_url":
			this.secretCode = set.getValue();
			break;
		case "gsm_url":
			this.gsmURL = set.getValue();
			break;
		case "sms_storage":
			this.smsStorage = set.getValue();
			break;
		}
		
	}
	
}