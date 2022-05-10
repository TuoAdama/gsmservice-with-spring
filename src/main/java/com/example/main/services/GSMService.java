package com.example.main.services;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entities.Solde;
import com.example.main.entities.Transfert;
import com.example.main.utils.Config;
import com.example.main.utils.LogMessage;

@Service
@Slf4j
public class GSMService {

	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	public static final String MESSAGE = "Message";
	public static final String RESPONSE = "Response";
	@Autowired SoldeService soldeService;
	@Autowired Config config;
	
	private APIRequestService apiRequest = new APIRequestService();

	public HashMap<String, String> makeTransfert(Transfert transfert) {
		
		String transfertSyntaxe = this.formatTranfertSyntaxe(transfert.getNumero(), transfert.getMontant());
		
		System.out.println(this.config.getGsmURL()+transfertSyntaxe);
		
		Solde previousSoldes = this.getSolde();
		
		log.info("validation du transfert...");
		
		HashMap<String, String> responses = makeUSSD(this.config.getGsmURL()+transfertSyntaxe);
		
		if(responses.get(MESSAGE).contains("souhaitez-vous continuer @@ cette vente")) {
			
			responses = makeUSSD(this.config.getGsmURL()+"1");
			
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
				log.info("Transfert validé !\n\n");
				return responses;
			}
			
			return this.failedTransfert(responses);
		}
		
		if(soldesExists && !soldeService.soldeIschange(previousSoldes, currentSoldes)) {
			return this.failedTransfert(responses);
		}
		
		log.info("Transfert validé !\n\n");
		
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
		
		String url = this.config.getGsmURL()+ this.getSoldeSyntaxe();
		
		log.info("Recupération du solde...");
		
		HashMap<String, String> responses = makeUSSD(url);
		int essaie = 1;
		
		while(responses.get(RESPONSE).equals(ERROR) && essaie < 10) {
			log.info("Recupération echouée ...");
			responses = makeUSSD(url);
			essaie++;
		}
		
		if(!responses.get(RESPONSE).equals(ERROR)) {
			
			String[] results = this.getSoldeByMessage(responses.get(MESSAGE));
			
			Solde solde = Solde.builder()
								.solde(Long.parseLong(results[0]))
								.bonus(Long.parseLong(results[1]))
								.build();
			
			log.info(solde.toString());
			
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
		log.info("Transfert echoué :)\n\n");
		responses.put(RESPONSE, ERROR);
		responses.put(MESSAGE, null);
		return responses;
	}
	
	public String encodeSyntaxe(String syntaxe) {
		return syntaxe
				.replace("*","%2A")
				.replace("#", "%23");
	}
	
	public String formatTranfertSyntaxe(String numero, Long montant) {
		String format = this.config.getTransfertSimpleSyntaxe().replaceAll("NUMERO", numero)
		.replaceAll("MONTANT", String.valueOf(montant));
		return this.encodeSyntaxe(format);
	}
	
	public String getSoldeSyntaxe() {
		return this.encodeSyntaxe(this.config.getSoldeSyntaxe());
	}
}
