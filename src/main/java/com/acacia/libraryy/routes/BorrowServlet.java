package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.BorrowRecordDao;
import com.acacia.libraryy.model.BorrowRecord;
import com.acacia.libraryy.utils.JwtUtil;
import com.acacia.libraryy.utils.Responder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@WebServlet("/api/borrow/*")
public class BorrowServlet extends HttpServlet {
    private final BorrowRecordDao borrowRecordDao = new BorrowRecordDao();

    // POST /api/borrow/ - Borrow a book
    // Body: {"bookId": 1}
    // Requires Authorization header with JWT token
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization");
        Integer userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = JwtUtil.getUserId(token);
        }

        if (userId == null) {
            Responder.fail("Unauthorized").setStatusCode(401).build(response);
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
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Integer bookId = jsonObject.getInteger("bookId");

        if (bookId == null) {
            Responder.fail("bookId is required").setStatusCode(400).build(response);
            return;
        }

        if (borrowRecordDao.borrowBook(userId, bookId)) {
            Responder.success("Book borrowed successfully").build(response);
        } else {
            Responder.fail("Failed to borrow book. It might be unavailable or already borrowed by you.").setStatusCode(400).build(response);
        }
    }

    // PUT /api/borrow/return - Return a book
    // Body: {"borrowRecordId": 1}
    // Requires Authorization header with JWT token (optional, could be admin only or user specific)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.equals("/return")) {
            Responder.fail("Invalid endpoint for returning a book. Use /api/borrow/return").setStatusCode(400).build(response);
            return;
        }

        // Optional: Add token validation if needed for user-specific returns or admin actions
        // String token = request.getHeader("Authorization");
        // Integer userId = null;
        // if (token != null && token.startsWith("Bearer ")) {
        //     token = token.substring(7);
        //     userId = JwtUtil.getUserIdFromToken(token);
        // }
        // if (userId == null) {
        //     Responder.fail("Unauthorized").setStatusCode(401).build(response);
        //     return;
        // }

        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String jsonString = jsonBuilder.toString();
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Integer borrowRecordId = jsonObject.getInteger("borrowRecordId");

        if (borrowRecordId == null) {
            Responder.fail("borrowRecordId is required").setStatusCode(400).build(response);
            return;
        }

        if (borrowRecordDao.returnBook(borrowRecordId)) {
            Responder.success("Book returned successfully").build(response);
        } else {
            Responder.fail("Failed to return book. Invalid record ID or book already returned.").setStatusCode(400).build(response);
        }
    }

    // GET /api/borrow/user - Get all borrow records for the logged-in user
    // GET /api/borrow/book/{bookId} - Get borrow records for a specific book (current borrows)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/user")) {
            String token = request.getHeader("Authorization");
            Integer userId = null;
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                userId = JwtUtil.getUserId(token);
            }

            if (userId == null) {
                Responder.fail("Unauthorized").setStatusCode(401).build(response);
                return;
            }
            List<BorrowRecord> records = borrowRecordDao.getBorrowRecordsByUserId(userId);
            Responder.success(records).build(response);

        } else if (pathInfo != null && pathInfo.startsWith("/book/")) {
            try {
                int bookId = Integer.parseInt(pathInfo.substring("/book/".length()));
                List<BorrowRecord> records = borrowRecordDao.getBorrowRecordsByBookId(bookId);
                Responder.success(records).build(response);
            } catch (NumberFormatException e) {
                Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
            }
        } else {
            Responder.fail("Invalid endpoint. Available: /user, /book/{bookId}").setStatusCode(404).build(response);
        }
    }
}