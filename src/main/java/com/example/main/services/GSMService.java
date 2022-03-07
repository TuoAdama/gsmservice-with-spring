package com.example.main.services;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.main.entities.Transfert;
import com.example.main.utils.LogMessage;

@Service
public class GSMService {

	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	public static final String MESSAGE = "Message";
	public static final String RESPONSE = "Response";

	@Value("${syntaxe.transfert}")
	private String syntaxe;
	@Value("${syntaxe.solde}")
	private String soldeSyntaxe;
	@Value("${validateurURL}")
	private String validatorUrl;
	
	@Autowired
	LogMessage logMessage;
	
	private APIRequestService apiRequest = new APIRequestService();

	public HashMap<String, String> makeTransfert(Transfert transfert) {
		
		this.syntaxe = this.syntaxe.replaceFirst("NUMERO", transfert.getNumero());
		this.syntaxe = this.syntaxe.replaceFirst("MONTANT", String.valueOf(transfert.getMontant()));
		
		//1.Recuperer le premier solde
		
		HashMap<String, Long> previousSoldes = this.getSoldes();
		
		logMessage.showLog("validation du transfert...");
		
		HashMap<String, String> responses = makeUSSD(this.validatorUrl + this.syntaxe);
		
		HashMap<String, Long> currentSoldes = this.getSoldes();
		
		boolean soldesExists = previousSoldes != null && currentSoldes != null;
		
		if (responses.get(RESPONSE).equals(ERROR)) {
			
			if(soldesExists && soldeIschange(previousSoldes, currentSoldes)) {
				responses.put(MESSAGE, null);
				responses.put(RESPONSE, SUCCESS);
				logMessage.showLog("Transfert validé !\n");
				return responses;
			}
			
			responses.put(RESPONSE, ERROR);
			logMessage.showLog("Transfert echoué :)\n");
			return responses;		
		}
		
		if(soldesExists && !soldeIschange(previousSoldes, currentSoldes)) {
			logMessage.showLog("Transfert echoué :)\n");	
			responses.put(RESPONSE, ERROR);
			responses.put(MESSAGE, null);
			return responses;	
		}
		
		logMessage.showLog("Transfert validé !");
		
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
	
	private boolean soldeIschange(HashMap<String, Long> previousSolde, HashMap<String, Long> currentSolde) {
		return previousSolde.get("solde") != currentSolde.get("solde") ||
				previousSolde.get("bonus") != currentSolde.get("bonus");
	}
	
	
	
	public HashMap<String, Long> getSoldes() {
		logMessage.showLog("Recupération du solde...");
		
		HashMap<String, String> responses = makeUSSD(this.validatorUrl + this.soldeSyntaxe);
		int essaie = 1;
		
		while(responses.get(RESPONSE).equals(ERROR) && essaie < 10) {
			logMessage.showLog("Recupération echouée ...");
			responses = makeUSSD(this.validatorUrl + this.soldeSyntaxe);
			essaie++;
		}
		
		if(!responses.get(RESPONSE).equals(ERROR)) {
			String[] results = this.getSoldeByMessage(responses.get(MESSAGE));
			
			HashMap<String, Long> soldes = new HashMap<>();
			
			soldes.put("solde", Long.parseLong(results[0]));
			soldes.put("bonus", Long.parseLong(results[1]));
			soldes.put("total", Long.parseLong(results[2]));
			
			return soldes;
		}
		
		return null;
	}
	
	private HashMap<String, String> makeUSSD(String syntaxe) {
		apiRequest.setUrl(syntaxe);
		HttpResponse<String> httpResponse = apiRequest.send();
		HashMap<String, String> responses = formatResponseBody(httpResponse.body());
		return responses;
	}

}
