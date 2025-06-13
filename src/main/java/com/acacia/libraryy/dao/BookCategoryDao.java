package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.BookCategory;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookCategoryDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");

    private static final String INSERT_BOOK_CATEGORY = "INSERT INTO book_categories(book_id, category_id) VALUES(?,?)";
    private static final String DELETE_BOOK_CATEGORY = "DELETE FROM book_categories WHERE book_id = ? AND category_id = ?";
    private static final String DELETE_BOOK_CATEGORIES_BY_BOOK_ID = "DELETE FROM book_categories WHERE book_id = ?";
    private static final String DELETE_BOOK_CATEGORIES_BY_CATEGORY_ID = "DELETE FROM book_categories WHERE category_id = ?";

    public boolean addBookToCategory(int bookId, int categoryId) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_BOOK_CATEGORY)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, categoryId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding book to category: " + e.getMessage());
            return false;
        }
    }

    public boolean removeBookFromCategory(int bookId, int categoryId) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_BOOK_CATEGORY)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, categoryId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error removing book from category: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAllCategoriesFromBook(int bookId) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_BOOK_CATEGORIES_BY_BOOK_ID)) {
            preparedStatement.setInt(1, bookId);
            return preparedStatement.executeUpdate() >= 0; // Return true if no rows affected or rows deleted
        } catch (SQLException e) {
            System.out.println("Error removing all categories from book: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAllBooksFromCategory(int categoryId) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_BOOK_CATEGORIES_BY_CATEGORY_ID)) {
            preparedStatement.setInt(1, categoryId);
            return preparedStatement.executeUpdate() >= 0; // Return true if no rows affected or rows deleted
        } catch (SQLException e) {
            System.out.println("Error removing all books from category: " + e.getMessage());
            return false;
        }
    }
}