package com.example.shoppingpoint.cli.graphic_controller;

import com.example.shoppingpoint.bean.store_dashboard.EditProductBean;
import com.example.shoppingpoint.cli.view.EditProductViewCLI;
import com.example.shoppingpoint.controller.EditProductController;
import com.example.shoppingpoint.exception.BeanException;
import com.example.shoppingpoint.exception.ControllerException;

import java.io.IOException;

public class EditProductGraphicControllerCLI {
    public void initialize() throws IOException, BeanException , ControllerException {
        EditProductViewCLI editProductViewCLI = new EditProductViewCLI();
        int productId = editProductViewCLI.editProductID();
        EditProductBean editProductBean = editProductViewCLI.editProductInput();
        EditProductController editProductController = new EditProductController();
        editProductController.editProduct(productId,editProductBean);

    }
}
