package com.example.shoppingpoint.cli.view;

import com.example.shoppingpoint.adapter.GenericProduct;
import com.example.shoppingpoint.cli.utils.CLIReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StoreDashboardViewCLI {
    public int getChoiceStoreOwner() throws IOException {
        System.out.println("What do you want to do?");
        return CLIReader.multiChoice(List.of(
                "Add product",
                "Select a product",
                "Change your loyalty card details",
                "View client list",
                "View sold products",
                "Quit"
        ));
    }

    public int getProduct() throws IOException {
        System.out.println("Select a product (insert product  ID): ");
        return Integer.parseInt(CLIReader.readline());
    }

    public void createProductView(List<GenericProduct> productList) {
        System.out.println("\nProduct list\n");

        for (GenericProduct product : productList) {
            System.out.printf("Product ID: %d\n", product.getId());
            System.out.printf("Name: %s\n", product.getName());
            System.out.printf("Price: %.2f\n", product.getPrice());
            System.out.printf("Discounted price: %.2f\n", product.getDiscountedPrice());
            System.out.printf("Status: %s\n", product.getStatus());
            System.out.printf("Quantity: %d\n", product.getQuantity());
            System.out.printf("Description: %s\n", product.getDescription());
            System.out.println("---------------");
        }
    }
}
