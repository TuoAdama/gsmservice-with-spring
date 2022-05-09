package com.example.main.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.main.entities.ConfigItem;
import com.example.main.entities.Setting;
import com.example.main.services.SettingService;
import com.example.main.utils.Config;

@Controller
public class SettingController {
	
	
	@Autowired SettingService settingService;
	@Autowired Config config;
	
	@GetMapping("/settings")
	private String index(){
		return "index";
	}
	
	@GetMapping("/")
	private String allSettings(Model settings){
		
		settings.addAttribute("settings", this.getConfigItems());
		
		return "config";
	}
	
	@GetMapping("/key/{key}")
	public Setting settingByKey(@PathVariable String key) {
		return settingService.getByKey(key);
	}
	
	@GetMapping("/{id}")
	private Setting settingById(@PathVariable Long id) {
		return settingService.findById(id);
	}

	@PostMapping("settings/update")
	public String updateSetting(@RequestParam Map<String, String> params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String key = params.get("key");
		String value = params.get("value");
		key = key.substring(0, 1).toUpperCase()+ key.substring(1);
		
		System.out.println("set"+key);
		
		Method setMethodName = config.getClass().getMethod("set"+key, String.class);
		
		setMethodName.invoke(config, value);
		
		return "redirect:/";
	}

	@PostMapping("/add")
	private Setting addSetting(@RequestBody @Valid Setting setting) {
		return settingService.updateOrSave(setting);
	}
	
	private List<ConfigItem> getConfigItems(){
		List<ConfigItem> configItems = new ArrayList<>();
		configItems.add(new ConfigItem("gsmURL", config.getGsmURL(), "GSM URL"));
		configItems.add(new ConfigItem("smsStorage", config.getSmsStorage(), "SMS URL"));
		configItems.add(new ConfigItem("appOnlineURL", config.getAppOnlineURL(), "APP ONLINE URL"));
		configItems.add(new ConfigItem("syntaxeSoldeURL", config.getSyntaxeSoldeURL(), "Syntaxe Solde URL"));
		configItems.add(new ConfigItem("syntaxeTransfertURL", config.getSmsStorage(), "Transfert URL"));
		configItems.add(new ConfigItem("syntaxeTransfertURL", config.getSmsStorage(), "Transfert URL"));
		
		return configItems;
	}
}
