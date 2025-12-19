package com.onemoney.recgardecardservice.endpoints;

import com.onemoney.recgardecardservice.repository.CardRepository;

import com.onemoney.rechargeservice.entity.ListCardsRequest;
import com.onemoney.rechargeservice.entity.ListCardsResponse;
import com.onemoney.rechargeservice.entity.DeleteCardResponse;
import com.onemoney.rechargeservice.entity.DeleteCardRequest;
import com.onemoney.rechargeservice.entity.UpdateCardResponse;
import com.onemoney.rechargeservice.entity.UpdateCardRequest;
import com.onemoney.rechargeservice.entity.CreateCardRequest;
import com.onemoney.rechargeservice.entity.CreateCardResponse;
import com.onemoney.rechargeservice.entity.GetCardRequest;
import com.onemoney.rechargeservice.entity.GetCardResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.onemoney.recgardecardservice.domain.Card;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

//Indique a Spring-WS que cette classe contient des methodes qui vont traiter des requetes SOAP Cest l’equivalent dun @RestController pour SOAP
@Endpoint
public class CardEndPoint {

    private static final String NAMESPACE = "http://onemoney.com/card";

    @Autowired
    private CardRepository cardRepository;

    //Lie une méthode specifique à un type de requête SOAP
    @PayloadRoot(namespace = NAMESPACE, localPart = "createCardRequest")
    //ResponsePayload lobjet retourne (CreateCardResponse) sera converti automatiquement en XML SOAP pour être renvoyé au client.
    //RequestPayload Spring-WS convertit automatiquement le XML SOAP reçu en un objet Java
    @ResponsePayload
    public CreateCardResponse createCard(@RequestPayload CreateCardRequest request) {
        CreateCardResponse response = new CreateCardResponse();
        try {
            Card card = new Card();
            card.setCardNumber(request.getCardNumber());
            card.setCardType(request.getCardType());
            card.setBalance(request.getBalance());
            card.setStatus(request.isStatus());
            card.setAccountId(request.getAccountId());

            cardRepository.save(card);
            // On force l'écriture immédiate pour voir si ça plante ici
            cardRepository.flush();

            response.setStatus("Card created successfully");
        } catch (Exception e) {
            response.setStatus("Error: " + e.getMessage());
            e.printStackTrace(); // Pour voir l'erreur exacte dans tes logs
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getCardRequest")
    @ResponsePayload
    public GetCardResponse getcard(@RequestPayload GetCardRequest request){
        GetCardResponse response = new GetCardResponse();

        // Récupération entité JPA
        Card entity = cardRepository.findById(request.getId()).orElse(null);

        if (entity != null) {
            // Conversion entité JPA -> objet SOAP
            com.onemoney.rechargeservice.entity.Card soapCard = mapToSoap(entity);
            response.setCard(soapCard);
        } else {
            response.setCard(null);
        }

        return response;
    }

    private com.onemoney.rechargeservice.entity.Card mapToSoap(Card card) {
        com.onemoney.rechargeservice.entity.Card soapCard =
            new com.onemoney.rechargeservice.entity.Card();

        soapCard.setId(card.getId());
        soapCard.setAccountId(card.getAccountId());
        soapCard.setCardNumber(card.getCardNumber());
        soapCard.setCardType(card.getCardType());
        soapCard.setBalance(card.getBalance());
        soapCard.setStatus(card.isStatus());

        return soapCard;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "listCardsRequest")
    @ResponsePayload
    public ListCardsResponse getAllCard(@RequestPayload ListCardsRequest request) {
        ListCardsResponse response = new ListCardsResponse();
        Collection<Card> cards = cardRepository.findAll();
        for (Card card : cards ){
            response.getCards().add(mapToSoap(card));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "updateCardRequest")
    @ResponsePayload
    public UpdateCardResponse updateCard(@RequestPayload UpdateCardRequest request) {

        Card card = cardRepository.findById(request.getId()).orElse(null);
        if(card !=null){
            card.setAccountId(request.getAccountId());
            if (request.getCardNumber() != null) card.setCardNumber(request.getCardNumber());
            if (request.getCardType() != null) card.setCardType(request.getCardType());
            if (request.getBalance() != null) card.setBalance(request.getBalance());
            card.setStatus(request.isStatus());
        }
        cardRepository.save(card);
        UpdateCardResponse response = new UpdateCardResponse();
        com.onemoney.rechargeservice.entity.Card soapCard = mapToSoap(card);
        response.setCard(soapCard);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "DeleteCardRequest")
    @ResponsePayload
    public DeleteCardResponse deleteCard(@RequestPayload DeleteCardRequest request) {
        boolean delete = cardRepository.existsById(request.getId());
        if (delete) cardRepository.deleteById(request.getId());

        DeleteCardResponse response = new DeleteCardResponse();
        response.setDeleted(delete);
        return response;
    }






}
