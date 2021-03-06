package com.shoppingpoint.cli.graphic_controller;

import com.shoppingpoint.adapter.GenericProduct;
import com.shoppingpoint.bean.PaymentBean;
import com.shoppingpoint.cli.view.PaymentViewCLI;
import com.shoppingpoint.controller.PaymentController;
import com.shoppingpoint.exception.BeanException;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.model.LoyaltyCard;
import com.shoppingpoint.model.Store;
import com.shoppingpoint.singleton.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PaymentGraphicControllerCLI {
    public void initialize(Store store, GenericProduct product) throws IOException {
        PaymentController controller = new PaymentController();
        LoyaltyCard card = null;
        try {
            card = controller.getLoyaltyCard(LoggedInUser.getInstance().getUser().getUsername(), product.getStoreName());
        } catch (ControllerException e) {
            System.out.println("Loyalty card not available");
        } finally { // Loyalty card can be null, so it should print an error message but the application can go on
            PaymentViewCLI view = new PaymentViewCLI();
            int quantity = 1;
            boolean loyaltyCardCheck = false;
            boolean exit = false;
            while (!exit) {
                view.showPaymentInformation(product, card, store, quantity, loyaltyCardCheck);
                int action = view.selectAction();
                switch (action) {
                    // Use loyalty card
                    case 1 -> loyaltyCardCheck = !loyaltyCardCheck;
                    // Increase quantity
                    case 2 -> {
                        if (quantity < product.getQuantity()) quantity++;
                    }
                    // Decrease quantity
                    case 3 -> {
                        if (quantity > 1) quantity--;
                    }
                    // Buy
                    case 4 -> {
                        try {
                            controller.buy(new PaymentBean(quantity, loyaltyCardCheck), card, LoggedInUser.getInstance().getUser().getUsername(), store, product);
                        } catch (BeanException e) {
                            System.out.println("Incorrect input: " + e.getMessage());
                            continue;
                        } catch (ControllerException e) {
                            System.out.println("[ERR] " + e.getMessage());
                            exit = true;
                            break;
                        }
                        view.showPaymentCompleted();
                        try {
                            TimeUnit.SECONDS.sleep(2); // wait for 2 seconds
                        } catch (InterruptedException e) {
                            System.out.printf("Unable to wait (reason: %s)\nYou will be redirected now%n", e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                        exit = true;
                    }
                    // Go back
                    case 5 -> exit = true;
                }
            }
        }
    }
}
