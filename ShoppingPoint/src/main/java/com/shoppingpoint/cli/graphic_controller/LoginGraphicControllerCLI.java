package com.shoppingpoint.cli.graphic_controller;

import com.shoppingpoint.bean.LoginBean;
import com.shoppingpoint.cli.view.LoginViewCLI;
import com.shoppingpoint.controller.LoginController;
import com.shoppingpoint.exception.BeanException;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.model.user.Client;
import com.shoppingpoint.model.user.StoreOwner;
import com.shoppingpoint.model.user.Supplier;
import com.shoppingpoint.model.user.User;
import com.shoppingpoint.singleton.LoggedInUser;

import java.io.IOException;

public class LoginGraphicControllerCLI {
    private final LoginViewCLI loginView;

    public LoginGraphicControllerCLI() {
        loginView = new LoginViewCLI();
    }

    public void login() throws IOException {
        User user;
        while (true) {
            try {
                LoginBean bean = loginView.getLoginInformation();
                LoginController controller = new LoginController();
                user = controller.login(bean);
                LoggedInUser.getInstance().setUser(user);
                break;
            } catch (ControllerException | BeanException e) {
                System.out.println("[ERR] " + e.getMessage());
                System.out.println("Please retry.");
            }
        }
        if (user instanceof Client) {
            SearchStoreGraphicControllerCLI searchStoreGraphicControllerCLI = new SearchStoreGraphicControllerCLI();
            searchStoreGraphicControllerCLI.initialize();
        }
        if (user instanceof StoreOwner) {
            StoreDashboardGraphicControllerCLI storeDashboardGraphicControllerCLI = new StoreDashboardGraphicControllerCLI();
            storeDashboardGraphicControllerCLI.initialize();
        }
        if (user instanceof Supplier) {
            RequestListGraphicControllerCLI requestListCli = new RequestListGraphicControllerCLI();
            requestListCli.initialize();
        }
    }
}
