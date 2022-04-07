package com.example.main.services;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.main.entities.Solde;
import com.example.main.entities.Transfert;
import com.example.main.utils.Config;
import com.example.main.utils.LogMessage;

@Service
public class GSMService {

	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	public static final String MESSAGE = "Message";
	public static final String RESPONSE = "Response";
	
	
	@Value("${validateurURL}")
	private String validateurURL;
		
	@Autowired
	LogMessage logMessage;
	
	@Autowired
	SoldeService soldeService;
	@Autowired Config config;
	
	private APIRequestService apiRequest = new APIRequestService();

	public HashMap<String, String> makeTransfert(Transfert transfert) {
		
		String transfertSyntaxe = this.encodeSyntaxe(transfert.getSyntaxe());
		System.out.println(this.validateurURL+transfertSyntaxe);
		
		Solde previousSoldes = this.getSolde();
		
		logMessage.showLog("validation du transfert...");
		
		HashMap<String, String> responses = makeUSSD(this.validateurURL+transfertSyntaxe);
		
		if(responses.get(MESSAGE).contains("souhaitez-vous continuer @@ cette vente")) {
			
			responses = makeUSSD(this.validateurURL+"1");
			
			if(responses.get(RESPONSE).equals(ERROR)) {
				return this.failedTransfert(responses);
			}
		}
		
		Solde currentSoldes = this.getSolde();
		
		boolean soldesExists = previousSoldes != null && currentSoldes != null;
		
		if (responses.get(RESPONSE).equals(ERROR)) {
			
			if(soldesExists && soldeService.soldeIschange(previousSoldes, currentSoldes)) {
				responses.put(MESSAGE, null);
				responses.put(RESPONSE, SUCCESS);
				logMessage.showLog("Transfert validé !\n\n");
				return responses;
			}
			
			return this.failedTransfert(responses);
		}
		
		if(soldesExists && !soldeService.soldeIschange(previousSoldes, currentSoldes)) {
			return this.failedTransfert(responses);
		}
		
		logMessage.showLog("Transfert validé !\n\n");
		
		return responses;
	}

	public HashMap<String, String> formatResponseBody(String responseBody) {

		Iterator<String> lines = responseBody.lines().iterator();

		lines.next();
		lines.next();
		String response = lines.next().split("Response: ")[1].trim();
		String message = lines.next().split("Message: ")[1].trim();

		HashMap<String, String> responses = new HashMap<>();

		responses.put("Response", response);
		responses.put("Message", message);

		return responses;
	}

	public String getReference(String message) {
		int index = message.indexOf(".Ref");
		return message.substring(index + ".Ref ".length());
	}
	
	private String[] getSoldeByMessage(String message) {
		
		message = message.replaceAll("[^0-9]+", "&").substring(1);
		String[] soldes = message.split("&");
		
		return soldes;
	}
	
	
	
	public Solde getSolde() {
		
		
		String soldeSyntaxe = this.getSoldeSyntaxe();
		
		String url = this.validateurURL+soldeSyntaxe;
		
		logMessage.showLog("Recupération du solde...");
		
		HashMap<String, String> responses = makeUSSD(url);
		int essaie = 1;
		
		while(responses.get(RESPONSE).equals(ERROR) && essaie < 10) {
			logMessage.showLog("Recupération echouée ...");
			responses = makeUSSD(url);
			essaie++;
		}
		
		if(!responses.get(RESPONSE).equals(ERROR)) {
			
			String[] results = this.getSoldeByMessage(responses.get(MESSAGE));
			
			Solde solde = Solde.builder()
								.solde(Long.parseLong(results[0]))
								.bonus(Long.parseLong(results[1]))
								.build();
			
			logMessage.showLog(solde.toString());
			
			return solde;
		}
		
		return null;
	}
	
	private HashMap<String, String> makeUSSD(String url) {
		apiRequest.setUrl(url);
		HttpResponse<String> httpResponse = apiRequest.send();
		HashMap<String, String> responses = formatResponseBody(httpResponse.body());
		return responses;
	}
	
	private HashMap<String, String>  failedTransfert(HashMap<String, String> responses){
		logMessage.showLog("Transfert echoué :)\n\n");
		responses.put(RESPONSE, ERROR);
		responses.put(MESSAGE, null);
		return responses;
	}
	
	public String encodeSyntaxe(String syntaxe) {
		return syntaxe
				.replace("*","%2A")
				.replace("#", "%23");
	}
	
	public String getSoldeSyntaxe() {
		
		APIRequestService req = new APIRequestService("http://localhost:8000/api/soldeSyntaxe");
		JSONObject obj = new JSONObject(req.send().body());
		
		return this.encodeSyntaxe(obj.getString("syntaxe"));
	}
}
