package com.example.main.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.main.entities.Etat;
import com.example.main.entities.Message;
import com.example.main.entities.Transfert;
import com.example.main.repositories.TransfertRepository;
import com.example.main.utils.LogMessage;

@Service
public class TransfertService {

	@Autowired
	TransfertRepository transfertRepository;
	@Autowired
	EtatService etatService;
	@Autowired
	GSMService gsmService;
	@Autowired
	Validator validator;
	@Autowired
	SoldeService soldeService;
	
	@Autowired
	LogMessage logMessage;

	@Value("${appOnlineURl}")
	private String appOnlineURl;

	private HttpClient httpClient = HttpClient.newHttpClient();
	private HttpRequest httpRequest;

	public JSONArray getTransfertOnline() {

		JSONArray responseToJSon = new JSONArray();

		httpRequest = HttpRequest.newBuilder().uri(URI.create(appOnlineURl)).build();

		logMessage.showLog("Recuperation en ligne des transferts...");
		try {
			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			responseToJSon = new JSONArray(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return responseToJSon;
	}

	/*
	 * Récupère les transferts depuis l'application distance et les stockent dans la
	 * base de données pour qu'on puisse les récupérer et les traiter
	 */
//	@Scheduled(cron = "*/10 * * * * *")
	public void storeTransferts() {
		
		JSONArray transferts = this.getTransfertOnline();
		logMessage.showLog(transferts.toString()+"\n");
		

		logMessage.showLog("Enregistrement des transferts dans la base de données...\n");

		Transfert transfert;

		for (int i = 0; i < transferts.length(); i++) {

			JSONObject item = transferts.getJSONObject(i);

			transfert = Transfert.builder()
					.id(item.getLong("id"))
					.numero(item.getString("numero"))
					.montant(item.getLong("montant"))
					.etat(etatService.getEtatByName(Etat.EN_COURS))
					.build();
			
			logMessage.showLog("Enregistrement du transfert : "+transfert.toString());

			Set<ConstraintViolation<Transfert>> violations = validator.validate(transfert);
		
			if (!violations.isEmpty()) {
				
				violations.stream().forEach(violation -> {
					String value = violation.getInvalidValue().toString();
					String message = violation.getMessage();
					logMessage.showLog(message + ": " + value+"\n");
				});
				continue;
			}
			
			Optional<Transfert> Optionaltransfert = transfertRepository.findById(transfert.getId());
			String message = "transfert déjà enregistré\n";
			
			if (!Optionaltransfert.isPresent()) {
				transfertRepository.save(transfert);
				message = "transfert enregistré\n";
			}
			
			logMessage.showLog(message);
		}

		logMessage.showLog("Enregistrements terminés");

	}

	/*
	 * Recupère les transferts stockés dans la base de données et les valides
	 */

//	@PostConstruct
	public void makeTransfert() {

		List<Transfert> transferts = this.getFailedOrLoadingTransferts();

		transferts.forEach(transfert -> {
			
			logMessage.showLog(transfert.toString());
			
			String ETAT_STATUS = Etat.ECHOUE;

			HashMap<String, String> responses = gsmService.makeTransfert(transfert);

			if (responses.get(GSMService.RESPONSE).equals(GSMService.SUCCESS)) {

				ETAT_STATUS = Etat.EXECUTE;
				
				String sms = responses.get(GSMService.MESSAGE);
				Message message = Message.builder().sms(sms).build();
				
				transfert.setMessage(message);
				transfert.setReference(sms != null ? gsmService.getReference(sms) : null);
			}

			transfert.setEtat(etatService.getEtatByName(ETAT_STATUS));
			transfertRepository.save(transfert);

		});
	}

	public List<Transfert> getFailedOrLoadingTransferts() {
		
		logMessage.showLog("Récupération des tranferts EN_COURS et ECHOUE\n");
		
		List<Transfert> transferts = new ArrayList<>();

		etatService.getEtatLoadingOrFailed().forEach(etat -> {
			transferts.addAll(etat.getTransferts());
		});
		
		logMessage.showLog(transferts.toString()+"\n");
		
		return transferts;
	}

}
