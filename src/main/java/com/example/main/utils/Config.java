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
	
	public Config(SettingService settingService) {
		this.gsmURL = settingService.getByKey("gsm_url").getValue();
		this.transfertSimpleSyntaxe = settingService.getByKey("transfert_simple_syntaxe").getValue();
		this.transfertsOnlineURL = settingService.getByKey("transferts_online_url").getValue();
		this.smsStorage = settingService.getByKey("sms_storage").getValue();
		this.secretCode = settingService.getByKey("secret_code").getValue();
		this.soldeSyntaxe = settingService.getByKey("consultation_solde_syntaxe").getValue();
	}

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