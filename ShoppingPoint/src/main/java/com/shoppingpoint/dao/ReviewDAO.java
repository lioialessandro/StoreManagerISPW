package com.shoppingpoint.dao;

import com.shoppingpoint.exception.DatabaseException;
import com.shoppingpoint.model.Review;
import com.shoppingpoint.model.SoldProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private ReviewDAO() {
        throw new IllegalStateException();
    }

    public static List<Review> getReviewsOfProduct(int productId) throws SQLException, DatabaseException {
        ArrayList<Review> reviews = new ArrayList<>();

        // Create Connection
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS)) {
            // Create statement
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                // Execute query
                ResultSet rs = statement.executeQuery(String.format("SELECT * FROM Review WHERE ProductId = %d", productId));
                while (rs.next()) {
                    Review review = getReview(rs);
                    reviews.add(review);
                }

                rs.close();
            }
        }
        return reviews;
    }

    public static Review getReviewFromClientAndSoldProductId(String client, int soldProductId) throws SQLException, DatabaseException {
        Review review;

        // Create Connection
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS)) {
            // Create statement
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                // Get user with specified username
                String sql = String.format("SELECT * FROM Review WHERE Client='%s' AND soldProductId = %d", client, soldProductId);
                // Execute query
                ResultSet rs = statement.executeQuery(sql);
                // Empty result
                if (!rs.first())
                    throw new DatabaseException("review");

                rs.first();
                review = getReview(rs);
                rs.close();
            }
        }
        return review;
    }

    public static void addReview(float value, String client, int soldProductId, int productId) throws SQLException {
        // Create Connection
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS)) {
            // Create statement
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                String sql = String.format(java.util.Locale.US, "INSERT INTO Review (Value, Client, SoldProductId, ProductId) VALUES (%f, '%s', %d, %d)", value, client, soldProductId, productId);
                // Execute query
                statement.executeUpdate(sql);
            }
        }
    }

    public static void updateReview(int reviewId, float value, String client, int soldProductId) throws SQLException {
        // Create Connection
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.USER, Database.PASS)) {
            // Create statement
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                // Get user with specified username
                String sql = String.format(java.util.Locale.US, "UPDATE Review SET Value = %s WHERE ReviewId = %d AND Client = '%s' AND SoldProductId = %d", value, reviewId, client, soldProductId);
                // Execute query
                statement.executeUpdate(sql);
            }
        }
    }

    private static Review getReview(ResultSet rs) throws SQLException, DatabaseException {
        int id = rs.getInt("ReviewId");
        int productId = rs.getInt("ProductId");
        int soldProductId = rs.getInt("SoldProductId");
        SoldProduct soldProduct = SoldProductDAO.getSoldProductById(soldProductId);
        String client = rs.getString("Client");
        float value = rs.getInt("Value");
        return new Review(id, value, client,soldProduct, productId);
    }
}
