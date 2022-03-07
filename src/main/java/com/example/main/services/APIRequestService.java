package com.example.main.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIRequestService {
	
	
	private String url;
	HttpRequest httpRequest;
	HttpClient httpClient;
	
	public APIRequestService() {}
	
	public APIRequestService(String url) {
		this.url = url;
	}
	
	
	public HttpResponse<String> send() {
		httpClient = HttpClient.newHttpClient();
		httpRequest = HttpRequest.newBuilder().uri(URI.create(this.url)).build();	
		
		HttpResponse<String> response = null;
		
		try {
			response =  httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}
