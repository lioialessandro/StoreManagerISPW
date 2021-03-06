package com.shoppingpoint.graphic_controller;

import com.shoppingpoint.ShoppingPointApplication;
import com.shoppingpoint.adapter.GenericProduct;
import com.shoppingpoint.bean.NewRequestBean;
import com.shoppingpoint.controller.NewRequestController;
import com.shoppingpoint.exception.BeanException;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.singleton.LoggedInUser;
import com.shoppingpoint.utils.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class NewRequestGraphicController {
    private GenericProduct product;

    @FXML
    private Label productNameLabel;
    @FXML
    private TextField maxPriceTextField;
    @FXML
    private TextField quantityTextField;

    public void initialize(GenericProduct product) {
        this.product = product;
        productNameLabel.setText(product.getName());
    }

    @FXML
    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(ShoppingPointApplication.class.getResource("store_dashboard.fxml"));
        ((Node) actionEvent.getSource()).getScene().setRoot(loader.load());
    }

    @FXML
    public void save(ActionEvent actionEvent) throws IOException {
        try {
            NewRequestBean bean = new NewRequestBean(maxPriceTextField.getText(), quantityTextField.getText());
            NewRequestController controller = new NewRequestController();
            controller.saveRequest(bean, product.getId());
            FXMLLoader loader = new FXMLLoader(ShoppingPointApplication.class.getResource("offers.fxml"));
            Parent node = loader.load();
            ((Node) actionEvent.getSource()).getScene().setRoot(node);
            OffersGraphicController offersGraphicController = loader.getController();
            offersGraphicController.initialize(product);
        } catch (BeanException e) {
            ExceptionHandler.handleException(ExceptionHandler.BEAN_HEADER_TEXT, e.getMessage());
        } catch (ControllerException e) {
            ExceptionHandler.handleException(ExceptionHandler.CONTROLLER_HEADER_TEXT, e.getMessage());
        }
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        LoggedInUser.getInstance().setUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }
}
