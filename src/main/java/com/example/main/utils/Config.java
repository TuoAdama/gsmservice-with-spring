package com.example.main.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.example.main.services.APIRequestService;

import lombok.Data;

@Component
@Data
public class Config {
	
	private String gsmURL = "http://192.168.5.150/cgi/WebCGI?1500102=account=apiuser&password=apipass&port=1&content=";
	private String smsStorage = "http://localhost:8000/api/AddTransfertAndroid";
	private String appOnlineURL = "http://localhost:8000/api/gsmlist";
	private String syntaxeSoldeURL = "http://localhost:8000/api/soldeSyntaxe";
	private String syntaxeTransfertURL = "http://localhost:8000/api/simpleTransfertSyntaxe";
	private String soldeSyntaxe;
	private String transfertSimpleSyntaxe;
	
	public Config(){
		this.initConfig();
	}
	
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