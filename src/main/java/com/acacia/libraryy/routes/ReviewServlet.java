package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.ReviewDao;
import com.acacia.libraryy.model.Review;
import com.acacia.libraryy.utils.JwtUtil;
import com.acacia.libraryy.utils.Responder;
import com.alibaba.fastjson2.JSON;
// import com.alibaba.fastjson2.JSONObject; // Not used
// import io.jsonwebtoken.Claims; // Switched to direct JwtUtil.getUserId call
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@WebServlet("/api/reviews/*")
public class ReviewServlet extends HttpServlet {
    private final ReviewDao reviewDao = new ReviewDao();

    // GET /api/reviews/book/{bookId} -> get all reviews for a book
    // GET /api/reviews/user/{userId} -> get all reviews by a user (potentially admin/self only)
    // GET /api/reviews/{reviewId} -> get a specific review by its ID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            Responder.fail("Specific review resource required (e.g., /book/{bookId}, /user/{userId}, or /{reviewId})").setStatusCode(400).build(response);
            return;
        }

        if (pathInfo.startsWith("/book/")) {
            try {
                int bookId = Integer.parseInt(pathInfo.substring("/book/".length()));
                List<Review> reviews = reviewDao.getReviewsByBookId(bookId);
                Responder.success(reviews).build(response);
            } catch (NumberFormatException e) {
                Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
            }
        } else if (pathInfo.startsWith("/user/")) {
            try {
                int userId = Integer.parseInt(pathInfo.substring("/user/".length()));
                // Add authorization check here if needed (e.g., only admin or the user themselves can see their reviews)
                List<Review> reviews = reviewDao.getReviewsByUserId(userId);
                Responder.success(reviews).build(response);
            } catch (NumberFormatException e) {
                Responder.fail("Invalid user ID format").setStatusCode(400).build(response);
            }
        } else if (pathInfo.matches("^/\\d+$")) { // Matches /reviewId
            try {
                int reviewId = Integer.parseInt(pathInfo.substring(1));
                Review review = reviewDao.getReviewById(reviewId);
                if (review != null) {
                    Responder.success(review).build(response);
                } else {
                    Responder.fail("Review not found").setStatusCode(404).build(response);
                }
            } catch (NumberFormatException e) {
                Responder.fail("Invalid review ID format").setStatusCode(400).build(response);
            }
        } else {
            Responder.fail("Invalid endpoint for reviews").setStatusCode(404).build(response);
        }
    }

    // POST /api/reviews -> create a new review
    // Body: {"bookId": 1, "rating": 5, "comment": "Great book!"}
    // Requires authentication
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Responder.fail("Authorization header missing or invalid").setStatusCode(401).build(response);
            return;
        }
        String token = authHeader.substring(7);
        int userId;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            System.err.println("Token validation/parsing failed in doPost: " + e.getMessage());
            Responder.fail("Invalid or expired token.").setStatusCode(401).build(response);
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String jsonString = jsonBuilder.toString();
        Review review = JSON.parseObject(jsonString, Review.class);
        review.setUserId(userId);

        if (review.getBookId() <= 0 || review.getRating() < 1 || review.getRating() > 5) {
            Responder.fail("Invalid review data: bookId must be positive, rating must be between 1 and 5.").setStatusCode(400).build(response);
            return;
        }

        if (reviewDao.insertReview(review)) {
            Responder.success(review).setStatusCode(201).build(response);
        } else {
            Responder.fail("Failed to create review. You may have already reviewed this book, or an internal error occurred.").setStatusCode(400).build(response);
        }
    }

    // PUT /api/reviews/{reviewId} -> update an existing review
    // Body: {"rating": 4, "comment": "Still good."}
    // Requires authentication, user must own the review
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("^/\\d+$")) {
            Responder.fail("Review ID is required for update (e.g., /api/reviews/1)").setStatusCode(400).build(response);
            return;
        }
        int reviewId;
        try {
            reviewId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            Responder.fail("Invalid review ID format").setStatusCode(400).build(response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Responder.fail("Authorization header missing or invalid").setStatusCode(401).build(response);
            return;
        }
        String token = authHeader.substring(7);
        int userId;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            System.err.println("Token validation/parsing failed in doPut: " + e.getMessage());
            Responder.fail("Invalid or expired token.").setStatusCode(401).build(response);
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String jsonString = jsonBuilder.toString();
        Review reviewUpdates = JSON.parseObject(jsonString, Review.class);
        reviewUpdates.setId(reviewId);
        reviewUpdates.setUserId(userId); // For DAO to verify ownership

        if (reviewUpdates.getRating() < 1 || reviewUpdates.getRating() > 5) {
            Responder.fail("Invalid review data: rating must be between 1 and 5.").setStatusCode(400).build(response);
            return;
        }

        // Check if the review exists and belongs to the user before attempting update
        Review existingReview = reviewDao.getReviewById(reviewId);
        if (existingReview == null) {
            Responder.fail("Review not found").setStatusCode(404).build(response);
            return;
        }
        if (existingReview.getUserId() != userId) {
            Responder.fail("You are not authorized to update this review").setStatusCode(403).build(response);
            return;
        }

        if (reviewDao.updateReview(reviewUpdates)) {
            // Fetch the updated review to return it
            Review updatedReview = reviewDao.getReviewById(reviewId);
            Responder.success(updatedReview).build(response);
        } else {
            Responder.fail("Failed to update review").setStatusCode(500).build(response);
        }
    }

    // DELETE /api/reviews/{reviewId} -> delete a review
    // Requires authentication, user must own the review
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("^/\\d+$")) {
            Responder.fail("Review ID is required for deletion (e.g., /api/reviews/1)").setStatusCode(400).build(response);
            return;
        }
        int reviewId;
        try {
            reviewId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            Responder.fail("Invalid review ID format").setStatusCode(400).build(response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Responder.fail("Authorization header missing or invalid").setStatusCode(401).build(response);
            return;
        }
        String token = authHeader.substring(7);
        int userId;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            System.err.println("Token validation/parsing failed in doDelete: " + e.getMessage());
            Responder.fail("Invalid or expired token.").setStatusCode(401).build(response);
            return;
        }

        // Check if the review exists and belongs to the user before attempting deletion
        Review existingReview = reviewDao.getReviewById(reviewId);
        if (existingReview == null) {
            Responder.fail("Review not found").setStatusCode(404).build(response);
            return;
        }
        if (existingReview.getUserId() != userId) {
            Responder.fail("You are not authorized to delete this review").setStatusCode(403).build(response);
            return;
        }

        if (reviewDao.deleteReview(reviewId, userId)) {
            Responder.success("Review deleted successfully").build(response);
        } else {
            Responder.fail("Failed to delete review").setStatusCode(500).build(response);
        }
    }
}