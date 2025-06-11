package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.Book;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");

    private static final String INSERT_BOOK = "INSERT INTO books(title, author, tag, description, cover_image, isbn, sum_number, rest_number, status) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String SELECT_ALL_BOOKS = "SELECT * FROM books";
    private static final String SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE id = ?";
    private static final String UPDATE_BOOK = "UPDATE books SET title=?, author=?, tag=?, description=?, cover_image=?, isbn=?, sum_number=?, rest_number=?, status=? WHERE id = ?";
    private static final String DELETE_BOOK_BY_ID = "DELETE FROM books WHERE id = ?";
    private static final String UPDATE_BOOK_STATUS_AND_REST_NUMBER = "UPDATE books SET status = ?, rest_number = ? WHERE id = ?";

    public boolean insertBook(Book book) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTag());
            preparedStatement.setString(4, book.getDescription());
            preparedStatement.setString(5, book.getCoverImage());
            preparedStatement.setString(6, book.getIsbn());
            preparedStatement.setInt(7, book.getSumNumber());
            preparedStatement.setInt(8, book.getRestNumber());
            preparedStatement.setString(9, book.getStatus() != null ? book.getStatus() : "available");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_BOOKS);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    public Book getBookById(int id) {
        Book book = null;
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BOOK_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    book = mapRowToBook(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public boolean updateBook(Book book) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTag());
            preparedStatement.setString(4, book.getDescription());
            preparedStatement.setString(5, book.getCoverImage());
            preparedStatement.setString(6, book.getIsbn());
            preparedStatement.setInt(7, book.getSumNumber());
            preparedStatement.setInt(8, book.getRestNumber());
            preparedStatement.setString(9, book.getStatus());
            preparedStatement.setInt(10, book.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteBookById(int id) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_BOOK_BY_ID)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Helper method to map ResultSet row to Book object
    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("tag"),
                rs.getString("status"),
                rs.getString("description"),
                rs.getString("cover_image"),
                rs.getString("isbn"),
                rs.getInt("sum_number"),
                rs.getInt("rest_number"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}