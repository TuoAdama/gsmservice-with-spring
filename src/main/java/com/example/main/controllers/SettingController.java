package com.example.main.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entities.Setting;
import com.example.main.services.SettingService;
import com.example.main.utils.Config;

@RestController
@RequestMapping("/settings")
public class SettingController {
	
	
	@Autowired SettingService settingService;
	@Autowired Config config;
	
	@GetMapping
	private List<Setting> allSettings(){
		return settingService.allSettings();
	}
	
	@GetMapping("/key/{key}")
	public Setting settingByKey(@PathVariable String key) {
		return settingService.getByKey(key);
	}
	
	@GetMapping("/{id}")
	private Setting settingById(@PathVariable Long id) {
		return settingService.findById(id);
	}

	@PostMapping("/update")
	public Setting updateSetting(@RequestBody @Valid Setting setting) {
		Setting set = settingService.updateOrSave(setting);
		config.updateConfig(set);
		return set;
	}

	@PostMapping("/add")
	private Setting addSetting(@RequestBody @Valid Setting setting) {
		return settingService.updateOrSave(setting);
	}
}
