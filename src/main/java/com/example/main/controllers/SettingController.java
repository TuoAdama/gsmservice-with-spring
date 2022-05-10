package com.example.main.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.main.entities.Setting;
import com.example.main.services.SettingService;
import com.example.main.services.SoldeService;
import com.example.main.utils.Config;

@Controller
@Slf4j
public class SettingController {
	
	
	@Autowired SettingService settingService;
	@Autowired Config config;
	@Autowired SoldeService soldeService;
	
	@GetMapping("/index")
	private String index(Model model){
		model.addAttribute("soldes", soldeService.findAll());
		return "index";
	}
	
	@GetMapping("/")
	private String allSettings(Model settings){
		settings.addAttribute("settings", this.settingService.allSettings());
		return "config";
	}
	
	@GetMapping("/key/{key}")
	public Setting settingByKey(@PathVariable String key) {
		return settingService.findByKey(key);
	}
	
	@GetMapping("/{id}")
	private Setting settingById(@PathVariable Long id) {
		return settingService.findById(id);
	}

	@PostMapping("settings/update")
	public String updateSetting(@RequestParam Map<String, String> params){
		String key = params.get("key");
		String value = params.get("value");
		this.updateConfig(key, value);
		return "redirect:/";
	}

	@PostMapping("/add")
	private Setting addSetting(@RequestBody @Valid Setting setting) {
		return settingService.updateOrSave(setting);
	}

	private void updateConfig(String key, String value){
		key = key.substring(0, 1).toUpperCase()+ key.substring(1);
		Method methodName;
		try {
			methodName = config.getClass().getMethod("set"+key, String.class);
			methodName.invoke(config, value);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		settingService.update(key,value);
	}
}
