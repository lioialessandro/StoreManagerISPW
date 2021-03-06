package com.shoppingpoint.graphic_controller;

import com.shoppingpoint.ShoppingPointApplication;
import com.shoppingpoint.bean.add_product.AddProductCommonBean;
import com.shoppingpoint.controller.UploadImageController;
import com.shoppingpoint.exception.BeanException;
import com.shoppingpoint.exception.ImageException;
import com.shoppingpoint.model.user.StoreOwner;
import com.shoppingpoint.singleton.LoggedInUser;
import com.shoppingpoint.utils.ExceptionHandler;
import com.shoppingpoint.utils.ProductType;
import com.shoppingpoint.utils.StoreType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AddProductGraphicController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField discountedPriceTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> typeComboBox;

    private InputStream image;

    @FXML
    public void initialize() {
        switch (((StoreOwner) LoggedInUser.getInstance().getUser()).getStore().getType()) {
            case CLOTHES -> typeComboBox.getItems().addAll("Clothes", "Shoes");
            case BOOKS -> typeComboBox.getItems().addAll("Book", "Comics");
            case VIDEOGAMES -> typeComboBox.getItems().addAll("Video Game", "Game Console");
            case ELECTRONICS -> typeComboBox.getItems().addAll("Home Appliances", "Computer");
            default -> throw new IllegalStateException("Unexpected value: " + ((StoreOwner) LoggedInUser.getInstance().getUser()).getStore().getType());
        }
    }

    @FXML
    public void goNext(ActionEvent actionEvent) throws IOException {
        try {
            AddProductCommonBean bean = new AddProductCommonBean(nameTextField.getText(), priceTextField.getText(), discountedPriceTextField.getText(), quantityTextField.getText(), statusComboBox.getValue());
            bean.setImage(image);
            ProductType type = ProductType.valueOf(typeComboBox.getValue().toUpperCase().replace(" ", ""));
            FXMLLoader loader = new FXMLLoader(ShoppingPointApplication.class.getResource("add_product_continue.fxml"));
            Parent node = loader.load();
            ((Node) actionEvent.getSource()).getScene().setRoot(node);
            AddProductContinueGraphicController addProductContinueGraphicController = loader.getController();
            addProductContinueGraphicController.initialize(bean, type);
        } catch (BeanException e) {
            ExceptionHandler.handleException(ExceptionHandler.BEAN_HEADER_TEXT, e.getMessage());
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(ShoppingPointApplication.class.getResource("store_dashboard.fxml"));
        ((Node) actionEvent.getSource()).getScene().setRoot(loader.load());
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        LoggedInUser.getInstance().setUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    @FXML
    public void uploadImage() {
        try {
            UploadImageController controller = new UploadImageController();
            File img = controller.chooseImage();
            image = controller.validateImage(img);
        } catch (ImageException e) {
            ExceptionHandler.handleException("Image", e.getMessage());
        }
    }
}
