package com.shoppingpoint.controller;

import com.shoppingpoint.dao.LoyaltyCardDAO;
import com.shoppingpoint.exception.ControllerException;
import com.shoppingpoint.model.LoyaltyCard;

import java.sql.SQLException;

public class LoyaltyCardController {
    public LoyaltyCard createLoyaltyCard(String client, String storeName) throws ControllerException {
        try {
            LoyaltyCardDAO.saveLoyaltyCard(client, storeName, 0);
        } catch (SQLException e) {
            throw new ControllerException("SQL", e);
        }
        return new LoyaltyCard(0, storeName);
    }
}
