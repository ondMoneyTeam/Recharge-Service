package com.onemoney.recgardecardservice.endpoints;

import com.onemoney.recgardecardservice.domain.Card;
import com.onemoney.recgardecardservice.domain.Recharge;
import com.onemoney.recgardecardservice.repository.CardRepository;
import com.onemoney.recgardecardservice.repository.RechargeRepository;
import com.onemoney.rechargeservice.entity.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;
import org.springframework.ws.soap.SoapFaultException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Endpoint
public class RechargeEndPoint {

    private static final String NAMESPACE = "http://onemoney.com/card";

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private CardRepository cardRepository;

    //Lier une methode a une requete SOAP
    @PayloadRoot(namespace = NAMESPACE,localPart = "createRechargeRequest")
    //ResponsePayload: lobjet retourne CreateCardResponse => XML SOAP
    //RequestPayload: Spring ws XML SOAP => JAVA
    @ResponsePayload
    @Transactional
    public CreateRechargeResponse createCard(@RequestPayload CreateRechargeRequest request) {
        System.out.println("=== DEBUG: Starting createRecharge ===");
        System.out.println("Request data: " + request);

        CreateRechargeResponse response = new CreateRechargeResponse();

        try {
            // 1. Créer d'abord une carte si elle n'existe pas
            Card card = cardRepository.findById(request.getCardId()).orElse(null);
            if (card == null) {
                System.out.println("=== DEBUG: Creating test card ===");
                card = new Card();
                card.setId(request.getCardId());
                card.setAccountId(1000L);
                card.setCardNumber("TEST-" + request.getCardId());
                card.setStatus(true);
                cardRepository.save(card);
                cardRepository.flush();
                System.out.println("=== DEBUG: Card created: " + card.getId());
            }

            // 2. Créer la recharge avec des données simples
            System.out.println("=== DEBUG: Creating recharge ===");
            Recharge recharge = new Recharge();
            recharge.setAccoundId(request.getAccoundId());
            recharge.setAmount(request.getAmount());
            recharge.setStatus("PENDING");  // Statut fixe pour test
            recharge.setCard(card);
            recharge.setCreatedDate(LocalDate.now());  // Date actuelle pour éviter les problèmes de parsing

            // 3. Sauvegarder
            Recharge saved = rechargeRepository.save(recharge);
            rechargeRepository.flush();  // Force l'insertion

            System.out.println("=== DEBUG: Recharge saved with ID: " + saved.getId());

            response.setStatus("SUCCESS - Recharge ID: " + saved.getId());

        } catch (Exception e) {
            System.err.println("=== ERROR in createRecharge ===");
            e.printStackTrace();
            response.setStatus("ERROR: " + e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "getRechargeRequest")
    @ResponsePayload
    public GetRechargeResponse getRecharge(@RequestPayload GetRechargeRequest request)
    {
        GetRechargeResponse response = new GetRechargeResponse();
        Recharge recharge = rechargeRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Card not found"));
        com.onemoney.rechargeservice.entity.Recharge rechargeSOAP=mapToSOAP(recharge);
        response.setRecharge(rechargeSOAP);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "updateRechargeRequest")
    @ResponsePayload
    public UpdateRechargeResponse updateRecharge(@RequestPayload UpdateRechargeRequest request){
        UpdateRechargeResponse response = new UpdateRechargeResponse();

        Recharge recharge = rechargeRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Card not found"));
        recharge.setAccoundId(request.getAccoundId());
        recharge.setAmount(request.getAmount());
        recharge.setStatus(request.getStatus());
        Card cardEntity= cardRepository.findById(request.getCardId()).orElseThrow(() -> new RuntimeException("Card not found"));
        recharge.setCard(cardEntity);
        recharge.setCreatedDate(LocalDate.parse(request.getCreatedDate()));
        rechargeRepository.save(recharge);
        com.onemoney.rechargeservice.entity.Recharge rechargeSOAP = mapToSOAP(recharge);
        response.setRecharge(rechargeSOAP);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "deleteRechargeRequest")
    @ResponsePayload
    public DeleteRechargeResponse deleteRecharge(@RequestPayload DeleteRechargeRequest request){
        boolean delete = rechargeRepository.existsById(request.getId());
        if (delete){
            rechargeRepository.deleteById(request.getId());
        }
        DeleteRechargeResponse response = new DeleteRechargeResponse();
        response.setDeleted(delete);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "listRechargeRequest")
    @ResponsePayload
    public ListRechargeResponse getAll(@RequestPayload ListRechargeRequest request){
       List<Recharge> listRecharge =  rechargeRepository.findAll();
        ListRechargeResponse response = new ListRechargeResponse();
        for(Recharge recharge:listRecharge){
            response.getRecharges().add(mapToSOAP(recharge));
        }
        return response;
    }


    private com.onemoney.rechargeservice.entity.Recharge  mapToSOAP(Recharge recharge){
        com.onemoney.rechargeservice.entity.Recharge rechargeSOAP = new com.onemoney.rechargeservice.entity.Recharge();
        rechargeSOAP.setAmount(recharge.getAmount());
        rechargeSOAP.setId(recharge.getId());
        rechargeSOAP.setStatus(recharge.getStatus());
        rechargeSOAP.setCreatedDate(recharge.getCreatedDate().toString());
        rechargeSOAP.setAccoundId(recharge.getAccoundId());
       rechargeSOAP.setCardId(recharge.getCard().getId());
       return rechargeSOAP;
    }
}
