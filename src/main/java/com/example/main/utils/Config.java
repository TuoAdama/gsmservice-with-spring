package com.example.main.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.example.main.services.APIRequestService;

import lombok.Data;

@Component
@Data
public class Config {
	
	private String gsmURL;
	private String smsStorage;
	private String appOnlineURL;
	private String syntaxeSoldeURL;
	private String syntaxeTransfertURL;
	private String soldeSyntaxe;
	private String transfertSimpleSyntaxe;

	private String executionTimeMakeTransfert = "3000";
	private String executionTimeStoreTransfert = "4000";
	
	public void initConfig() {
		this.soldeSyntaxe = this.getSyntaxeSoldeURLOnline();
		this.transfertSimpleSyntaxe = this.getSyntaxeTransfertURLOnline();
	}
	
	public String getSyntaxeSoldeURLOnline() {
		return this.getSyntaxeOnline(this.syntaxeSoldeURL);
	}
	
	public String getSyntaxeTransfertURLOnline() {
		return this.getSyntaxeOnline(this.syntaxeTransfertURL);	
	}
	
	private String getSyntaxeOnline(String url) {
		APIRequestService apiRequest = new APIRequestService();
		apiRequest.setUrl(url);
		JSONObject ob = new JSONObject(apiRequest.send().body());
		return ob.getString("syntaxe");
	}
}