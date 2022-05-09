package com.example.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entities.Setting;
import com.example.main.repositories.SettingRepository;

@Service
public class SettingService {

	@Autowired
	SettingRepository settingRepository;
	
	public Setting updateOrSave(Setting setting) {
		return settingRepository.save(setting);
	}
	
	public List<Setting> allSettings() {
		return settingRepository.findAll();
	}
	
	public Setting findById(Long id) {
		return settingRepository.findById(id).get();
	}
	
	public Setting findByKey(String name) {
		return settingRepository.findByKey(name);
	}

	public void update(String key, String value) {
		Setting s = this.findByKey(key);
		s.setValue(value);
		this.updateOrSave(s);
	}
	
}
