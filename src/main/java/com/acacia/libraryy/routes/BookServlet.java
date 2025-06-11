package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.BookDao;
import com.acacia.libraryy.model.Book;
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

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {
    private final BookDao bookDao = new BookDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all books
            List<Book> books = bookDao.getAllBooks();
            Responder.success(books).build(response);
        } else {
            // Get book by ID
            try {
                int bookId = Integer.parseInt(pathInfo.substring(1));
                Book book = bookDao.getBookById(bookId);
                if (book != null) {
                    Responder.success(book).build(response);
                } else {
                    Responder.fail("Book not found").setStatusCode(404).build(response);
                }
            } catch (NumberFormatException e) {
                Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String jsonString = jsonBuilder.toString();
        Book book = JSON.parseObject(jsonString, Book.class);

        if (bookDao.insertBook(book)) {
            Responder.success("Book added successfully").setStatusCode(201).build(response);
        } else {
            Responder.fail("Failed to add book").setStatusCode(500).build(response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            Responder.fail("Book ID is required for update").setStatusCode(400).build(response);
            return;
        }

        try {
            int bookId = Integer.parseInt(pathInfo.substring(1));
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();
            Book book = JSON.parseObject(jsonString, Book.class);
            book.setId(bookId);

            if (bookDao.updateBook(book)) {
                Responder.success("Book updated successfully").build(response);
            } else {
                Responder.fail("Failed to update book or book not found").setStatusCode(500).build(response);
            }
        } catch (NumberFormatException e) {
            Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            Responder.fail("Book ID is required for deletion").setStatusCode(400).build(response);
            return;
        }

        try {
            int bookId = Integer.parseInt(pathInfo.substring(1));
            if (bookDao.deleteBookById(bookId)) {
                Responder.success("Book deleted successfully").build(response);
            } else {
                Responder.fail("Failed to delete book or book not found").setStatusCode(500).build(response);
            }
        } catch (NumberFormatException e) {
            Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
        }
    }
}