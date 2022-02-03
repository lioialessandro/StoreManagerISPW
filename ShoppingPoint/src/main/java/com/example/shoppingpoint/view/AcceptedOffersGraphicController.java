package com.example.shoppingpoint.view;

import com.example.shoppingpoint.ShoppingPointApplication;
import com.example.shoppingpoint.adapter.GenericProduct;
import com.example.shoppingpoint.adapter.ProductAdapter;
import com.example.shoppingpoint.controller.AcceptedOfferController;
import com.example.shoppingpoint.controller.RequestListController;
import com.example.shoppingpoint.controller.SendEmailController;
import com.example.shoppingpoint.exception.ControllerException;
import com.example.shoppingpoint.model.Offer;
import com.example.shoppingpoint.model.Request;
import com.example.shoppingpoint.singleton.LoggedInUser;
import com.example.shoppingpoint.utils.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.List;

public class AcceptedOffersGraphicController {

    @FXML
    FlowPane acceptedOfferPane;

    @FXML
    public void initialize() throws IOException {
        createAcceptedOfferPaneView();
    }

    private void createAcceptedOfferPaneView() throws IOException {
        try {
            acceptedOfferPane.getChildren().clear();
            AcceptedOfferController controller = new AcceptedOfferController();
            List<Offer> acceptedOffersList = controller.getAcceptedOffersOfSupplier();
            for (Offer acceptedOffer : acceptedOffersList) {
                FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("reusable/request_product_pane.fxml"));
                AnchorPane node = fxmlLoader.load();
                int requestId = acceptedOffer.getRequestId();
                Request request = controller.getRequestById(requestId);
                RequestListController controller2 = new RequestListController();
                GenericProduct product = new ProductAdapter(controller2.getProduct(request.getProductId()));
                ((Text) node.lookup("#ProductName")).setText(product.getName());
                ((Text) node.lookup("#quantity")).setText(String.format("Quantity: %d", request.getQuantity()));
                ((Text) node.lookup("#maxPrice")).setText(String.format("Offer Price:%.2f", acceptedOffer.getOfferPrice()));
                ((Label) node.lookup("#description")).setText(product.getDescription());
                ((Text) node.lookup("#store")).setText("Store: " + product.getStoreName());
                ((Button) node.lookup("#descriptionButton")).setOnAction((ActionEvent event) -> {
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
                    Node pane = (Node) event.getSource();
                    popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
                    popOver.setContentNode(scrollPane);
                    popOver.setCornerRadius(16);
                    popOver.show(pane);
                });
                Text sendEmail = (Text) node.lookup("#sendEmail");
                sendEmail.setOnMouseClicked(event -> {
                    try {
                        SendEmailController emailController = new SendEmailController();
                        String username = emailController.getUsernameOfStore(product.getStoreName());
                        emailController.sendEmail(username);
                    } catch (ControllerException e) {
                        ExceptionHandler.handleException("Controller Error", e.getMessage());
                    }
                });
                if (!controller.IsOfferChecked(acceptedOffer.getOfferId())) {
                    (node.lookup("#newAcceptedOffer")).setVisible(true);
                }
                (node.lookup("#offerButton")).setVisible(false);
                (node.lookup("#choosePriceHBox")).setVisible(false);
                acceptedOfferPane.getChildren().add(node);

            }
        } catch (ControllerException e) {
            ExceptionHandler.handleException("Controller Error", e.getMessage());
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("request_list.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LoggedInUser.getInstance().setUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(ShoppingPointApplication.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }
}
