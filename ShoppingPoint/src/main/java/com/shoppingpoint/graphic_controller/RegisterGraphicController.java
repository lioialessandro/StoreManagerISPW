package com.shoppingpoint.graphic_controller;

import com.shoppingpoint.ShoppingPointApplication;
import com.shoppingpoint.bean.RegisterBean;
import com.shoppingpoint.controller.RegisterController;
import com.shoppingpoint.exception.BeanException;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.model.user.Client;
import com.shoppingpoint.model.user.Supplier;
import com.shoppingpoint.model.user.User;
import com.shoppingpoint.singleton.LoggedInUser;
import com.shoppingpoint.utils.ExceptionHandler;
import com.shoppingpoint.utils.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterGraphicController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField verifyPasswordTextField;
    @FXML
    private ComboBox<String> userTypeField;

    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    @FXML
    protected void register(ActionEvent actionEvent) throws IOException {
        try {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String verifyPassword = verifyPasswordTextField.getText();
            String email = emailTextField.getText();
            String type = userTypeField.getValue();
            RegisterBean bean = new RegisterBean(email, username, type, password, verifyPassword);

            if (bean.getUserType() == UserType.STOREOWNER) {
                FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("new_store.fxml"));
                Parent node = fxmlLoader.load();
                NewStoreGraphicController newStoreController = fxmlLoader.getController();
                newStoreController.initialize(bean);
                ((Node) actionEvent.getSource()).getScene().setRoot(node);
            } else {
                RegisterController controller = new RegisterController();
                User user = controller.register(bean);
                LoggedInUser.getInstance().setUser(user);
                if (user instanceof Client) {
                    FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("searchstore.fxml"));
                    ((Node) actionEvent.getSource()).getScene().setRoot(fxmlLoader.load());
                }
                if (user instanceof Supplier) {
                    FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("request_list.fxml"));
                    Parent node = fxmlLoader.load();
                    ((Node) actionEvent.getSource()).getScene().setRoot(node);
                    RequestListGraphicController requestListGraphicControllerController = fxmlLoader.getController();
                    requestListGraphicControllerController.initialize();
                }
            }
        } catch (BeanException e) {
            ExceptionHandler.handleException(ExceptionHandler.BEAN_HEADER_TEXT, e.getMessage());
        } catch (ControllerException e) {
            ExceptionHandler.handleException(ExceptionHandler.CONTROLLER_HEADER_TEXT, e.getMessage());
        }
    }
}
