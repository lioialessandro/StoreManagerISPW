package com.example.shoppingpoint.cli.graphic_controller;

import com.example.shoppingpoint.adapter.GenericProduct;
import com.example.shoppingpoint.bean.NewRequestBean;
import com.example.shoppingpoint.cli.view.NewRequestViewCLI;
import com.example.shoppingpoint.controller.NewRequestController;
import com.example.shoppingpoint.exception.BeanException;
import com.example.shoppingpoint.exception.ControllerException;

import java.io.IOException;

public class NewRequestGraphicControllerCLI {
    public void initialize(GenericProduct product) throws IOException {
        try {
            NewRequestViewCLI newRequestViewCLI = new NewRequestViewCLI();
            NewRequestBean newRequestBean = newRequestViewCLI.newRequestInput();
            NewRequestController controller = new NewRequestController();
            controller.saveRequest(newRequestBean, product.getId());
        } catch (ControllerException e) {
            System.out.println("[ERR] " + e.getMessage());
        } catch (BeanException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
