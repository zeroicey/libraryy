package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.BookCategoryDao;
import com.acacia.libraryy.dao.CategoryDao;
import com.acacia.libraryy.model.Category;
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

@WebServlet("/api/categories/*")
public class CategoryServlet extends HttpServlet {
    private final CategoryDao categoryDao = new CategoryDao();
    private final BookCategoryDao bookCategoryDao = new BookCategoryDao();

    // GET /api/categories -> get all categories
    // GET /api/categories/{id} -> get category by id
    // GET /api/categories/book/{bookId} -> get categories for a book
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Category> categories = categoryDao.getAllCategories();
            Responder.success(categories).build(response);
        } else if (pathInfo.matches("^/\\d+$")) { // Matches /id
            try {
                int categoryId = Integer.parseInt(pathInfo.substring(1));
                Category category = categoryDao.getCategoryById(categoryId);
                if (category != null) {
                    Responder.success(category).build(response);
                } else {
                    Responder.fail("Category not found").setStatusCode(404).build(response);
                }
            } catch (NumberFormatException e) {
                Responder.fail("Invalid category ID format").setStatusCode(400).build(response);
            }
        } else if (pathInfo.startsWith("/book/")) {
            try {
                int bookId = Integer.parseInt(pathInfo.substring("/book/".length()));
                List<Category> categories = categoryDao.getCategoriesByBookId(bookId);
                Responder.success(categories).build(response);
            } catch (NumberFormatException e) {
                Responder.fail("Invalid book ID format").setStatusCode(400).build(response);
            }
        } else {
            Responder.fail("Invalid endpoint for categories").setStatusCode(404).build(response);
        }
    }

    // POST /api/categories -> create a new category
    // Body: {"name": "Fiction"}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        // POST /api/categories/{categoryId}/books/{bookId} -> assign category to book
        if (pathInfo != null && pathInfo.matches("^/\\d+/books/\\d+$")) {
            try {
                String[] parts = pathInfo.substring(1).split("/books/");
                int categoryId = Integer.parseInt(parts[0]);
                int bookId = Integer.parseInt(parts[1]);

                if (bookCategoryDao.addBookToCategory(bookId, categoryId)) {
                    Responder.success("Book added to category successfully").build(response);
                } else {
                    Responder.fail("Failed to add book to category, or relationship already exists.").setStatusCode(400).build(response);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Responder.fail("Invalid category ID or book ID format in path").setStatusCode(400).build(response);
            }
            return;
        }

        // POST /api/categories -> create a new category
        if (pathInfo == null || pathInfo.equals("/")) {
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();
            Category category = JSON.parseObject(jsonString, Category.class);

            if (category.getName() == null || category.getName().trim().isEmpty()){
                Responder.fail("Category name is required").setStatusCode(400).build(response);
                return;
            }

            if (categoryDao.insertCategory(category)) {
                Responder.success(category).setStatusCode(201).build(response);
            } else {
                Responder.fail("Failed to create category, it might already exist or an internal error occurred.").setStatusCode(500).build(response);
            }
        } else {
            Responder.fail("Invalid endpoint for POST request. Use /api/categories to create or /api/categories/{catId}/books/{bookId} to assign.").setStatusCode(400).build(response);
        }
    }

    // PUT /api/categories/{id} -> update category
    // Body: {"name": "Science Fiction"}
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || !pathInfo.matches("^/\\d+$")) {
            Responder.fail("Category ID is required for update and must be in the path (e.g., /api/categories/1)").setStatusCode(400).build(response);
            return;
        }

        try {
            int categoryId = Integer.parseInt(pathInfo.substring(1));
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonString = jsonBuilder.toString();
            Category category = JSON.parseObject(jsonString, Category.class);
            category.setId(categoryId);

            if (category.getName() == null || category.getName().trim().isEmpty()){
                Responder.fail("Category name is required for update").setStatusCode(400).build(response);
                return;
            }

            if (categoryDao.updateCategory(category)) {
                Responder.success("Category updated successfully").build(response);
            } else {
                Responder.fail("Failed to update category or category not found").setStatusCode(500).build(response);
            }
        } catch (NumberFormatException e) {
            Responder.fail("Invalid category ID format").setStatusCode(400).build(response);
        }
    }

    // DELETE /api/categories/{id} -> delete category by id
    // DELETE /api/categories/{categoryId}/books/{bookId} -> remove category from book
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            Responder.fail("Category ID or book-category relation is required for deletion.").setStatusCode(400).build(response);
            return;
        }

        // DELETE /api/categories/{categoryId}/books/{bookId}
        if (pathInfo.matches("^/\\d+/books/\\d+$")) {
            try {
                String[] parts = pathInfo.substring(1).split("/books/");
                int categoryId = Integer.parseInt(parts[0]);
                int bookId = Integer.parseInt(parts[1]);

                if (bookCategoryDao.removeBookFromCategory(bookId, categoryId)) {
                    Responder.success("Book removed from category successfully").build(response);
                } else {
                    Responder.fail("Failed to remove book from category or relationship not found.").setStatusCode(400).build(response);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Responder.fail("Invalid category ID or book ID format in path").setStatusCode(400).build(response);
            }
            return;
        }

        // DELETE /api/categories/{id}
        if (pathInfo.matches("^/\\d+$")) {
            try {
                int categoryId = Integer.parseInt(pathInfo.substring(1));
                // Optional: Consider implications. Deleting a category might require deleting related book_categories entries.
                // The current SQL schema for book_categories has ON DELETE CASCADE for category_id, so this should be handled by DB.
                // If not, you'd call bookCategoryDao.removeAllBooksFromCategory(categoryId) first.
                if (categoryDao.deleteCategoryById(categoryId)) {
                    Responder.success("Category deleted successfully").build(response);
                } else {
                    Responder.fail("Failed to delete category or category not found").setStatusCode(500).build(response);
                }
            } catch (NumberFormatException e) {
                Responder.fail("Invalid category ID format").setStatusCode(400).build(response);
            }
            return;
        }

        Responder.fail("Invalid endpoint for DELETE. Use /api/categories/{id} or /api/categories/{catId}/books/{bookId}").setStatusCode(400).build(response);
    }
}