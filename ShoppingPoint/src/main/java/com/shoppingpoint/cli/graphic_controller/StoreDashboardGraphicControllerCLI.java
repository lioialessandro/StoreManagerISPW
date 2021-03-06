package com.shoppingpoint.cli.graphic_controller;

import com.shoppingpoint.adapter.GenericProduct;
import com.shoppingpoint.cli.view.StoreDashboardViewCLI;
import com.shoppingpoint.controller.ReviewController;
import com.shoppingpoint.controller.ViewProductsController;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.model.Store;
import com.shoppingpoint.model.user.StoreOwner;
import com.shoppingpoint.singleton.LoggedInUser;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoreDashboardGraphicControllerCLI {
    public void initialize() throws IOException {
        try {
            StoreDashboardViewCLI storeDashboardViewCLI = new StoreDashboardViewCLI();
            List<GenericProduct> productList = getProductList();
            List<Pair<GenericProduct, Float>> productsWithReview = getProductsWithReview(productList);
            storeDashboardViewCLI.createProductView(productsWithReview);
            int choice;
            boolean exit = false;
            while (!exit) {
                choice = storeDashboardViewCLI.getChoiceStoreOwner();
                switch (choice) {
                    case 1 -> { // Add product
                        AddProductGraphicControllerCLI addProductGraphicControllerCLI = new AddProductGraphicControllerCLI();
                        addProductGraphicControllerCLI.initialize();
                    }
                    case 2 -> { // Select a product
                        int productId = storeDashboardViewCLI.getProduct();
                        productList = getProductList();
                        GenericProduct product = productList.stream().filter(el -> el.getId().equals(productId)).findFirst().orElse(null);
                        if (product == null) {
                            System.out.println("Invalid product ID");
                            continue;
                        }
                        ProductGraphicControllerCLI productGraphicControllerCLI = new ProductGraphicControllerCLI();
                        productGraphicControllerCLI.initialize(product);
                    }
                    case 3 -> { // Loyalty Card
                        LoyaltyCardGraphicControllerCLI loyaltyCardGraphicControllerCLI = new LoyaltyCardGraphicControllerCLI();
                        loyaltyCardGraphicControllerCLI.initialize();
                    }
                    case 4 -> { // Client List
                        ClientListGraphicControllerCLI clientListGraphicControllerCLI = new ClientListGraphicControllerCLI();
                        clientListGraphicControllerCLI.initialize();
                    }
                    case 5 -> { // Sold Products
                        SummaryGraphicControllerCLI summaryGraphicControllerCLI = new SummaryGraphicControllerCLI();
                        summaryGraphicControllerCLI.initialize();
                    }
                    case 6 -> { // View products
                        productList = getProductList();
                        productsWithReview = getProductsWithReview(productList);
                        storeDashboardViewCLI.createProductView(productsWithReview);
                    }
                    default -> exit = true;
                }
            }
        } catch (ControllerException e) {
            System.out.println("[ERR] " + e.getMessage());
        }
    }

    private List<GenericProduct> getProductList() throws ControllerException {
        ViewProductsController viewProductsController = new ViewProductsController();
        Store store = ((StoreOwner) LoggedInUser.getInstance().getUser()).getStore();
        return viewProductsController.getProductsFromStore(store);
    }

    private List<Pair<GenericProduct, Float>> getProductsWithReview(List<GenericProduct> productList) throws ControllerException {
        List<Pair<GenericProduct, Float>> productsWithReview = new ArrayList<>();
        for (GenericProduct product : productList) {
            ReviewController controller = new ReviewController();
            float review = controller.getAverageReviewOfProduct(product.getId());
            productsWithReview.add(new Pair<>(product, review));
        }
        return productsWithReview;
    }
}
