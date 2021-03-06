package com.shoppingpoint.cli.view;

import com.shoppingpoint.adapter.GenericProduct;
import com.shoppingpoint.cli.utils.CLIReader;
import com.shoppingpoint.model.LoyaltyCard;
import com.shoppingpoint.model.Store;
import com.shoppingpoint.singleton.LoggedInUser;
import com.shoppingpoint.utils.Triple;

import java.io.IOException;
import java.util.List;

public class StoreViewCLI {
    public void showStoreInfo(Store store) {
        System.out.println("--------------------");
        System.out.println(store.getName());
        System.out.printf("Email: %s%n", LoggedInUser.getInstance().getUser().getEmail());
    }

    public void showLoyaltyCardInfo(Store store, LoyaltyCard card) {
        if (store.getPointsInEuro() != 0 && store.getEuroInPoints() != 0) {
            System.out.printf("Every %d points used, you will have a discount of 1€%n", store.getPointsInEuro());
            System.out.printf("Every %d€ spent, you will receive 1 point%n", store.getEuroInPoints());
            if (card == null) System.out.println("Loyalty card not activated.");
            else System.out.printf("You have %d points%n", card.getPoints());
        }
    }

    public void showProducts(List<Triple<GenericProduct, Float, Float>> products) {
        System.out.println("Products");
        for (Triple<GenericProduct, Float, Float> productWithValues : products) {
            GenericProduct product = productWithValues.getLeft();
            float review = productWithValues.getMiddle();
            float discount = productWithValues.getRight();
            System.out.printf("ID: %d - %s%n", product.getId(), product.getName());
            System.out.printf("Price: %.2f€%n", product.getPrice());
            System.out.printf("Discounted Price: %.2f€ (%.2f%%)%n", product.getDiscountedPrice(), discount);
            System.out.printf("Status: %s%n", product.getStatus());
            System.out.printf("Description: %s%n", product.getDescription());
            System.out.printf("Review: %.1f/5%n", review);
            System.out.println("---------------");
        }
    }

    public int getStoreAction() throws IOException {
        System.out.println("What do you want to do?");
        return CLIReader.multiChoice(List.of(
                "Buy product",
                "Orders",
                "Activate loyalty card",
                "Send email to store",
                "Go back"
        ));
    }

    public void showSuccessActivation() {
        System.out.println("Successfully activated Loyalty Card for this store");
        System.out.println("You will now be able to use it while paying for a product");
    }

    public int getProduct() throws IOException {
        System.out.print("Insert product (specify ID): ");
        return Integer.parseInt(CLIReader.readline());
    }
}
