package com.example.main.entities;

import lombok.Data;

@Data
public class ConfigItem {
	private String key;
	private String value;
	private String display;
	
	public ConfigItem(String key, String value) {
		this.key = key;
		this.value = value;
		this.display = key;
	}
	
	public ConfigItem(String key, String value, String display) {
		this.key = key;
		this.value = value;
		this.display = display;
	}
	
}
