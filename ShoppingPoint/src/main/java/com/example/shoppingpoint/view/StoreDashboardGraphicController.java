package com.example.shoppingpoint.view;

import com.example.shoppingpoint.ShoppingPointApplication;
import com.example.shoppingpoint.adapter.GenericProduct;
import com.example.shoppingpoint.bean.store_dashboard.LoyaltyCardBean;
import com.example.shoppingpoint.controller.StoreDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;


import com.example.shoppingpoint.model.user.*;
import com.example.shoppingpoint.model.Store;
import javafx.scene.text.Font;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.List;

public class StoreDashboardGraphicController {

    @FXML
    private Label labelStoreName;

    private final StoreDashboardController controller;
    private StoreOwner storeOwner;

    @FXML
    private FlowPane productsPane;

    public StoreDashboardGraphicController() {
        controller = new StoreDashboardController();
    }

    public void initData(StoreOwner owner) throws Exception {
        setStoreOwner(owner);
        labelStoreName.setText(storeOwner.getStore().getName() + " - Shopping Point");
        createProductsView(storeOwner.getStore());
    }

    @FXML
    public void goToClientList(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("client_list.fxml"));
        Parent node = fxmlLoader.load();
        ((Node) event.getSource()).getScene().setRoot(node);
        ClientListGraphicController clientListGraphicController = fxmlLoader.getController();
        clientListGraphicController.initData(storeOwner);
    }

    public void goToRequest(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("request.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    private void setStoreOwner(StoreOwner storeOwnerName) throws Exception {
        this.storeOwner = storeOwnerName;
        if (storeOwner.getStore() == null) {
            Store store = controller.getStoreFromStoreOwnerName(storeOwner.getUsername());
            storeOwner.setStore(store);
        }
    }

    private void createProductsView(Store store) throws Exception {
        productsPane.getChildren().clear();
        List<GenericProduct> products = controller.getProductsFromStore(store);

        for (GenericProduct product : products) {
            FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("reusable/store_dashboard_product_pane.fxml"));
            AnchorPane pane = fxmlLoader.load();
//            Set product data in the View
            ((Label) pane.lookup("#name")).setText(product.getName());
            String formattedPrice = String.format("%.02f€", product.getPrice()); // Price with 2 decimal points
            ((Label) pane.lookup("#price")).setText(formattedPrice);
            String formattedDiscountedPrice = String.format("%.02f€", product.getDiscountedPrice()); // Price with 2 decimal points
            ((Label) pane.lookup("#discountedPrice")).setText(formattedDiscountedPrice);
            ((Label) pane.lookup("#status")).setText(product.getStatus());
            ((Label) pane.lookup("#description")).setText(product.getDescription());
            ((Button) pane.lookup("#descriptionButtonOfLabel")).setOnAction((ActionEvent event) -> {
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setMaxWidth(400.0);
                scrollPane.setMaxHeight(400.0);
                scrollPane.setPadding(new Insets(16));
                Label label = new Label();
                label.setText(product.getDescription());
                label.setStyle("-fx-font-size: 16px");
                label.setMaxWidth(350.0);
                label.setWrapText(true);
                scrollPane.setContent(label);
                PopOver popOver = new PopOver();
                Node node = (Node) event.getSource();
                popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
                popOver.setContentNode(scrollPane);
                popOver.setCornerRadius(16);
                popOver.show(node);
            });
            ((Button) pane.lookup("#editButton")).setOnAction((ActionEvent event) -> {
                pane.lookup("#name").setVisible(false);
                pane.lookup("#price").setVisible(false);
                pane.lookup("#discountedPrice").setVisible(false);
                pane.lookup("#status").setVisible(false);
                pane.lookup("#editButton").setVisible(false);
                pane.lookup("#requestButton").setVisible(false);
                pane.lookup("#descriptionHbox").setVisible(false);

                pane.lookup("#nameTextField").setVisible(true);
                pane.lookup("#priceTextField").setVisible(true);
                pane.lookup("#discountedPriceTextField").setVisible(true);
                pane.lookup("#statusTextField").setVisible(true);
                pane.lookup("#descriptionTextField").setVisible(true);
                pane.lookup("#saveButton").setVisible((true));


                ((TextField) pane.lookup("#nameTextField")).setText(product.getName());
                String formattedPriceTextField = String.format("%.02f€", product.getPrice()); // Price with 2 decimal points
                ((TextField) pane.lookup("#priceTextField")).setText(formattedPriceTextField);
                String formattedDiscountedPriceTextField = String.format("%.02f€", product.getDiscountedPrice()); // Price with 2 decimal points
                ((TextField) pane.lookup("#discountedPriceTextField")).setText(formattedDiscountedPriceTextField);
                ((TextField) pane.lookup("#statusTextField")).setText(product.getStatus());
                ((TextField) pane.lookup("#descriptionTextField")).setText(product.getDescription());
                ((Button) pane.lookup("#saveButton")).setOnAction((ActionEvent actionEvent) -> {
                    pane.lookup("#name").setVisible(true);
                    pane.lookup("#price").setVisible(true);
                    pane.lookup("#discountedPrice").setVisible(true);
                    pane.lookup("#status").setVisible(true);
                    pane.lookup("#editButton").setVisible(true);
                    pane.lookup("#requestButton").setVisible(true);
                    pane.lookup("#descriptionHbox").setVisible(true);

                    pane.lookup("#nameTextField").setVisible(false);
                    pane.lookup("#priceTextField").setVisible(false);
                    pane.lookup("#discountedPriceTextField").setVisible(false);
                    pane.lookup("#statusTextField").setVisible(false);
                    pane.lookup("#descriptionTextField").setVisible(false);
                    pane.lookup("#saveButton").setVisible(false);
                });

            });
//            Add product to the view
            productsPane.getChildren().add(pane);
        }
    }

    @FXML
    public void openEditCard(ActionEvent actionEvent) {
        PopOver popOver = new PopOver();

        Font font = new Font(18);
        VBox vbox = new VBox(16);
        vbox.setPadding(new Insets(16));
        CheckBox activeBox = new CheckBox("Active");
        activeBox.setAlignment(Pos.CENTER);
        activeBox.setFont(font);
        activeBox.setSelected(storeOwner.getStore().getPointsInEuro() != 0 && storeOwner.getStore().getEuroInPoints() != 0);

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        TextField pointInEuroTextField = new TextField(storeOwner.getStore().getPointsInEuro().toString());
        pointInEuroTextField.setPrefSize(64, 32);
        pointInEuroTextField.setDisable(!activeBox.isSelected());
        TextField euroInPointsTextField = new TextField(storeOwner.getStore().getEuroInPoints().toString());
        euroInPointsTextField.setPrefSize(64, 32);
        euroInPointsTextField.setDisable(!activeBox.isSelected());
        Label text1 = new Label(" points spent = 1€ discount");
        text1.setAlignment(Pos.CENTER);
        text1.setFont(font);
        Label text2 = new Label(" € spent = 1 point earned");
        text2.setAlignment(Pos.CENTER);
        text2.setFont(font);
        hbox1.getChildren().addAll(pointInEuroTextField, text1);
        hbox2.getChildren().addAll(euroInPointsTextField, text2);

        activeBox.setOnAction(event -> {
            boolean selected = ((CheckBox) event.getSource()).isSelected();
            pointInEuroTextField.setDisable(!selected);
            euroInPointsTextField.setDisable(!selected);
        });

        Button updateButton = new Button("Update");
        updateButton.setAlignment(Pos.CENTER);
        updateButton.setStyle("-fx-background-color: #6EC6FF; -fx-background-radius: 16;");
        updateButton.setEffect(new DropShadow());
        updateButton.setPrefSize(120, 48);
        updateButton.setOnAction(event -> {
            try {
                LoyaltyCardBean bean = new LoyaltyCardBean(activeBox.isSelected(), pointInEuroTextField.getText(), euroInPointsTextField.getText());
                controller.updateLoyaltyCard(bean, storeOwner.getStore());
                storeOwner.getStore().setPointsInEuro(bean.getPointsInEuro());
                storeOwner.getStore().setEuroInPoints(bean.getEuroInPoints());
                popOver.hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(activeBox, hbox1, hbox2, updateButton);

        Node node = (Node) actionEvent.getSource();
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setContentNode(vbox);
        popOver.setCornerRadius(16);
        popOver.show(node);
    }
}
