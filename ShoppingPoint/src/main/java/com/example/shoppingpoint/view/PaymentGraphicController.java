package com.example.shoppingpoint.view;

import com.example.shoppingpoint.ShoppingPointApplication;
import com.example.shoppingpoint.adapter.GenericProduct;
import com.example.shoppingpoint.bean.PaymentBean;
import com.example.shoppingpoint.controller.PaymentController;
import com.example.shoppingpoint.model.LoyaltyCard;
import com.example.shoppingpoint.model.Store;
import com.example.shoppingpoint.model.user.Client;
import com.example.shoppingpoint.singleton.LoggedInUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class PaymentGraphicController {
    private GenericProduct product;
    private Store store;
    private LoyaltyCard card;
    private float total;

    @FXML
    private Text productNameText;

    @FXML
    private Label quantityLabel;

    @FXML
    private Text priceText;

    @FXML
    private Text totalText;

    @FXML
    private CheckBox checkLoyaltyCard;

    @FXML
    private Label maxQuantityText;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Text pointText;

    private static final String DECIMAL_FORMAT = "%.02f€";

    public void initData(GenericProduct product, Store store, LoyaltyCard card) {
        this.product = product;
        this.store = store;
        this.card = card;

        if (product.getQuantity() == 1) {
            addButton.setVisible(false);
        }
        productNameText.setText(product.getName());
        String formattedPrice = String.format(DECIMAL_FORMAT, product.getDiscountedPrice()); // Price with 2 decimal points
        priceText.setText(formattedPrice);
        total = Integer.parseInt(quantityLabel.getText()) * product.getDiscountedPrice();
        String formattedTotal = String.format(DECIMAL_FORMAT, total);
        totalText.setText(formattedTotal);
        if (card != null) {
            checkLoyaltyCard.setVisible(true);
            pointText.setVisible(true);
            pointText.setText("You have " + card.getPoints() + " points");
        }
        maxQuantityText.setText(maxQuantityText.getText() + product.getQuantity());
    }

    @FXML
    public void buy(ActionEvent actionEvent) throws Exception {
        PaymentController controller = new PaymentController();
        PaymentBean bean = new PaymentBean(quantityLabel.getText(), checkLoyaltyCard.isSelected());
        controller.buy(bean, card, LoggedInUser.getInstance().getUser().getUsername(), store, product);
        FXMLLoader loader = new FXMLLoader(ShoppingPointApplication.class.getResource("payment_completed.fxml"));
        Parent node = loader.load();
        ((Node) actionEvent.getSource()).getScene().setRoot(node);
        PaymentCompletedGraphicController paymentCompletedGraphicController = loader.getController();
        paymentCompletedGraphicController.initData(store);
    }

    @FXML
    public void loyaltyCardCheck(ActionEvent actionEvent) {
        if (checkLoyaltyCard.isSelected()) {
            total = Integer.parseInt(quantityLabel.getText()) * product.getDiscountedPrice() - (float) card.getPoints() / store.getPointsInEuro();
            if (total < 0) {
                total = 0f;
            }
            String formattedTotal = String.format(DECIMAL_FORMAT, total);
            totalText.setText(formattedTotal);
        } else {
            total = Integer.parseInt(quantityLabel.getText()) * product.getDiscountedPrice();
            String formattedTotal = String.format(DECIMAL_FORMAT, total);
            totalText.setText(formattedTotal);
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("store.fxml"));
        Parent node = fxmlLoader.load();
        ((Node) event.getSource()).getScene().setRoot(node);
        StoreGraphicController storeGraphicController = fxmlLoader.getController();
        storeGraphicController.initData(store);
    }

    @FXML
    public void addQuantity(ActionEvent event) {
        quantityLabel.setText(String.valueOf(Integer.parseInt(quantityLabel.getText()) + 1));
        if (Integer.parseInt(quantityLabel.getText()) == product.getQuantity()) {
            addButton.setVisible(false);
        }
        if (Integer.parseInt(quantityLabel.getText()) == 2) {
            removeButton.setVisible(true);
        }
        total = total + product.getDiscountedPrice();
        String formattedTotal = String.format(DECIMAL_FORMAT, total);
        totalText.setText(formattedTotal);

    }

    @FXML
    public void removeQuantity(ActionEvent event) {
        quantityLabel.setText(String.valueOf(Integer.parseInt(quantityLabel.getText()) - 1));
        if (Integer.parseInt(quantityLabel.getText()) == 1) {
            removeButton.setVisible(false);
        }
        if (Integer.parseInt(quantityLabel.getText()) == product.getQuantity() - 1) {
            addButton.setVisible(true);
        }
        total = total - product.getDiscountedPrice();
        String formattedTotal = String.format(DECIMAL_FORMAT, total);
        totalText.setText(formattedTotal);
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        LoggedInUser.getInstance().setUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }
}
