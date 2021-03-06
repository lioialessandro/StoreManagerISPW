package com.shoppingpoint.cli.graphic_controller;

import com.shoppingpoint.adapter.GenericProduct;
import com.shoppingpoint.cli.view.AcceptOfferViewCLI;
import com.shoppingpoint.controller.AcceptOfferController;
import com.shoppingpoint.controller.SendEmailController;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.exception.EmailException;
import com.shoppingpoint.model.Offer;
import com.shoppingpoint.model.Request;

import java.io.IOException;
import java.util.List;

public class AcceptOfferGraphicControllerCLI {
    public void initialize(GenericProduct product) throws IOException {
        AcceptOfferViewCLI acceptOfferViewCLI = new AcceptOfferViewCLI();
        AcceptOfferController acceptOfferController = new AcceptOfferController();
        try {
            List<Request> requestsList = acceptOfferController.getRequestsOfProduct(product.getId());
            acceptOfferViewCLI.viewRequestsOfProduct(requestsList);
            boolean exit = false;
            while (!exit) exit = acceptOfferAction(requestsList);
        } catch (ControllerException e) {
            System.out.println("[ERR] " + e.getMessage());
        } catch (EmailException e) {
            System.out.println("[EMAIL] " + e.getMessage());
        }
    }

    private boolean acceptOfferAction(List<Request> requestsList) throws IOException, ControllerException, EmailException {
        AcceptOfferViewCLI acceptOfferViewCLI = new AcceptOfferViewCLI();
        AcceptOfferController acceptOfferController = new AcceptOfferController();
        boolean offerSelection = acceptOfferViewCLI.getChoice();
        if (offerSelection) {
            int requestId = acceptOfferViewCLI.getOfferRequestId();
            Request request = requestsList.stream().filter(el -> el.getRequestId() == requestId).findFirst().orElse(null);
            if (request == null) {
                System.out.println("Invalid input");
                return false;
            }
            if (request.isAccepted()) {
                Offer offer = acceptOfferController.getAcceptedOffer(request.getRequestId());
                acceptOfferViewCLI.viewAcceptedOfferOfRequest(offer);
                if (acceptOfferViewCLI.getChoiceEmail()) {
                    SendEmailController sendEmailController = new SendEmailController();
                    sendEmailController.sendEmail(offer.getSupplierUsername());
                }
                return true;
            }
//            Show offers
            List<Offer> offers = acceptOfferController.getOffersOfRequest(request.getRequestId());
            acceptOfferViewCLI.viewOffersOfProduct(offers);
            int acceptOffer = acceptOfferViewCLI.getOfferChoice();
            if (acceptOffer == 1) {
                int offerId = acceptOfferViewCLI.acceptOffer();
                Offer offer = offers.stream().filter(el -> el.getOfferId() == offerId).findFirst().orElse(null);
                if (offer == null) {
                    System.out.println("Invalid input");
                    return false;
                }
                acceptOfferController.acceptOffer(request.getRequestId(), offer.getOfferId());
                System.out.println("Offer accepted");
            }
            if (acceptOffer == 2) {
                sendEmail(offers, acceptOfferViewCLI);

            }
        }
        return true;
    }

    private void sendEmail(List<Offer> offers, AcceptOfferViewCLI acceptOfferViewCLI) throws IOException, ControllerException, EmailException {
        SendEmailController controller = new SendEmailController();
        String supplier = acceptOfferViewCLI.getSupplier();
        Offer offer = offers.stream().filter(el -> el.getSupplierUsername().equals(supplier)).findFirst().orElse(null);
        if (offer == null) System.out.println("Invalid input: supplier offer's not found");
        else controller.sendEmail(supplier);
    }
}
