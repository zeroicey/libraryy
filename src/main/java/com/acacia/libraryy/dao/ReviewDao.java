package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.Review;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");

    private static final String INSERT_REVIEW = "INSERT INTO reviews(user_id, book_id, rating, comment) VALUES(?,?,?,?)";
    private static final String SELECT_REVIEW_BY_ID = "SELECT id, user_id, book_id, rating, comment, created_at, updated_at FROM reviews WHERE id = ?";
    private static final String SELECT_REVIEWS_BY_BOOK_ID = "SELECT id, user_id, book_id, rating, comment, created_at, updated_at FROM reviews WHERE book_id = ? ORDER BY created_at DESC";
    private static final String SELECT_REVIEWS_BY_USER_ID = "SELECT id, user_id, book_id, rating, comment, created_at, updated_at FROM reviews WHERE user_id = ? ORDER BY created_at DESC";
    private static final String UPDATE_REVIEW = "UPDATE reviews SET rating = ?, comment = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND user_id = ?";
    private static final String DELETE_REVIEW = "DELETE FROM reviews WHERE id = ? AND user_id = ?"; // User can only delete their own review
    private static final String CHECK_REVIEW_EXISTS = "SELECT id FROM reviews WHERE user_id = ? AND book_id = ?";

    public boolean insertReview(Review review) {
        // Check if the user has already reviewed this book
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(CHECK_REVIEW_EXISTS)) {
            checkStmt.setInt(1, review.getUserId());
            checkStmt.setInt(2, review.getBookId());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User has already reviewed this book.");
                    return false; // User has already reviewed this book
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking existing review: " + e.getMessage());
            return false;
        }

        // Insert new review
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_REVIEW, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, review.getUserId());
            preparedStatement.setInt(2, review.getBookId());
            preparedStatement.setInt(3, review.getRating());
            preparedStatement.setString(4, review.getComment());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        review.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error inserting review: " + e.getMessage());
            return false;
        }
    }

    public Review getReviewById(int reviewId) {
        Review review = null;
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_REVIEW_BY_ID)) {
            preparedStatement.setInt(1, reviewId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                review = mapRowToReview(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting review by ID: " + e.getMessage());
        }
        return review;
    }

    public List<Review> getReviewsByBookId(int bookId) {
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_REVIEWS_BY_BOOK_ID)) {
            preparedStatement.setInt(1, bookId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                reviews.add(mapRowToReview(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews by book ID: " + e.getMessage());
        }
        return reviews;
    }

    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_REVIEWS_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                reviews.add(mapRowToReview(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews by user ID: " + e.getMessage());
        }
        return reviews;
    }

    public boolean updateReview(Review review) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_REVIEW)) {
            preparedStatement.setInt(1, review.getRating());
            preparedStatement.setString(2, review.getComment());
            preparedStatement.setInt(3, review.getId());
            preparedStatement.setInt(4, review.getUserId()); // Ensure user owns the review
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating review: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReview(int reviewId, int userId) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_REVIEW)) {
            preparedStatement.setInt(1, reviewId);
            preparedStatement.setInt(2, userId); // Ensure user owns the review
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }

    private Review mapRowToReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getInt("rating"),
                rs.getString("comment"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}