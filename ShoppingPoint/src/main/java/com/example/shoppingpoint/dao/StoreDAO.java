package com.example.shoppingpoint.dao;

import com.example.shoppingpoint.model.Store;
import com.example.shoppingpoint.utils.StoreType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    private StoreDAO() {
        throw new IllegalStateException();
    }

    public static List<Store> getAllStores() throws Exception {
        Statement statement = null;
        Connection connection = null;
        ArrayList<Store> stores = new ArrayList<>();

        try {
            // Create Connection
            connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS);
            // Create statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Execute query
            ResultSet rs = statement.executeQuery("SELECT * FROM Store");
            while (rs.next()) {
                String name = rs.getString("Name");
                String address = rs.getString("Address");
                Integer pointsInEuro = rs.getInt("PointsInEuro");
                Integer euroInPoints = rs.getInt("EuroInPoints");
                StoreType type = StoreType.valueOf(rs.getString("Type"));
                Store store = new Store(name, address, pointsInEuro, euroInPoints, type);
                stores.add(store);
            }

            rs.close();
        } finally {
            // Clean-up dell'ambiente
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return stores;
    }

    public static Store getStoreByName(String name) throws Exception {
        Statement statement = null;
        Connection connection = null;
        Store store = null;

        try {
            // Create Connection
            connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS);
            // Create statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Get user with specified username
            String sql = String.format("SELECT * FROM Store WHERE Name='%s'", name);
            // Execute query
            ResultSet rs = statement.executeQuery(sql);
            // Empty result
            if (!rs.first()) {
                throw new Exception("No store found with name: " + name);
            }
            rs.first();
            String address = rs.getString("Address");
            Integer pointsInEuro = rs.getInt("PointsInEuro");
            Integer euroInPoints = rs.getInt("EuroInPoints");
            StoreType type = StoreType.valueOf(rs.getString("Type"));
            store = new Store(name, address, pointsInEuro, euroInPoints, type);

            rs.close();
        } finally {
            // Clean-up dell'ambiente
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return store;
    }

    public static Store getStoreByStoreOwnerUsername(String username) throws Exception {
        Statement statement = null;
        Connection connection = null;
        Store store = null;

        try {
            // Create Connection
            connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS);
            // Create statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Get store with specified store owner
            String sql = String.format("SELECT * FROM Store WHERE StoreOwner='%s'", username);
            // Execute query
            ResultSet rs = statement.executeQuery(sql);
            // Empty result
            if (!rs.first()) {
                throw new Exception("Not found " + username +"'store");
            }
            rs.first();
            String name = rs.getString("Name");
            String address = rs.getString("Address");
            Integer pointsInEuro = rs.getInt("PointsInEuro");
            Integer euroInPoints = rs.getInt("EuroInPoints");
            StoreType type = StoreType.valueOf(rs.getString("Type"));
            store = new Store(name, address, pointsInEuro, euroInPoints, type);

            rs.close();
        } finally {
            // Clean-up dell'ambiente
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return store;
    }

    public static void saveStore(String name, String address, StoreType type, String storeOwner) throws Exception {
        Statement statement = null;
        Connection connection = null;

        try {
            // Create Connection
            connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS);
            // Create statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Get user with specified username
            // TODO passare null a punti
            String sql = String.format("INSERT INTO Store (Name, Address, Type, PointsInEuro, StoreOwner) VALUES ('%s', '%s', '%s', '%d', '%s')", name, address, type, 0, storeOwner);
            // Execute query
            statement.executeUpdate(sql);
        } finally {
            // Clean-up dell'ambiente
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
