package com.example.main.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class APIRequestService {
	
	
	private String url;
	HttpRequest httpRequest;
	HttpClient httpClient = HttpClient.newHttpClient();
	
	public APIRequestService() {}
	
	public APIRequestService(String url) {
		this.url = url;
	}
	
	
	public HttpResponse<String> send() {
		this.httpRequest = HttpRequest.newBuilder().uri(URI.create(this.url)).build();	
		
		HttpResponse<String> response = null;
		
		try {
			response =  httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public HttpResponse<String> send(HashMap<String, String> body){
		
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody;
		try {
			requestBody = objectMapper.writeValueAsString(body);
			this.httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(this.url))
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();
			return this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
				
		return null;
	}
		
	public void setUrl(String url) {
		this.url = url;
	}
	
}
