package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.Book;
import com.acacia.libraryy.model.BorrowRecord;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");
    private BookDao bookDao = new BookDao(); // For updating book status and count

    private static final String INSERT_BORROW_RECORD = "INSERT INTO borrow_records(user_id, book_id, borrow_date, returned) VALUES(?,?,?,?)";
    private static final String SELECT_BORROW_RECORDS_BY_USER_ID = "SELECT * FROM borrow_records WHERE user_id = ?";
    private static final String SELECT_BORROW_RECORDS_BY_BOOK_ID = "SELECT * FROM borrow_records WHERE book_id = ? AND returned = false";
    private static final String UPDATE_RETURN_RECORD = "UPDATE borrow_records SET return_date = ?, returned = ? WHERE id = ?";
    private static final String SELECT_BORROW_RECORD_BY_ID = "SELECT * FROM borrow_records WHERE id = ?";


    public boolean borrowBook(int userId, int bookId) {
        Connection conn = null;
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check if book is available and rest_number > 0
            Book book = bookDao.getBookById(bookId);
            if (book == null || book.getRestNumber() <= 0 || "borrowed".equals(book.getStatus())) {
                if (book != null && book.getRestNumber() <= 0){
                    // if no books left, update status to borrowed
                    book.setStatus("borrowed");
                    bookDao.updateBook(book);
                }
                conn.rollback();
                return false; // Book not available or does not exist
            }

            // 2. Insert borrow record
            try (PreparedStatement psInsert = conn.prepareStatement(INSERT_BORROW_RECORD)) {
                psInsert.setInt(1, userId);
                psInsert.setInt(2, bookId);
                psInsert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                psInsert.setBoolean(4, false);
                psInsert.executeUpdate();
            }

            // 3. Update book's rest_number and status if necessary
            book.setRestNumber(book.getRestNumber() - 1);
            if (book.getRestNumber() == 0) {
                book.setStatus("borrowed");
            }
            // bookDao.updateBook needs a connection that is not part of this transaction or use the same connection
            // For simplicity, we assume bookDao.updateBook handles its own connection or is adapted.
            // A better approach would be to pass the connection to bookDao.updateBook
            try (PreparedStatement psUpdateBook = conn.prepareStatement("UPDATE books SET rest_number = ?, status = ? WHERE id = ?")) {
                psUpdateBook.setInt(1, book.getRestNumber());
                psUpdateBook.setString(2, book.getStatus());
                psUpdateBook.setInt(3, book.getId());
                psUpdateBook.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.out.println("Borrow book transaction failed: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    public boolean returnBook(int borrowRecordId) {
        Connection conn = null;
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Get borrow record
            BorrowRecord record = getBorrowRecordById(borrowRecordId, conn); // Pass connection
            if (record == null || record.isReturned()) {
                conn.rollback();
                return false; // Record not found or already returned
            }

            // 2. Update borrow record
            try (PreparedStatement psUpdateRecord = conn.prepareStatement(UPDATE_RETURN_RECORD)) {
                psUpdateRecord.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                psUpdateRecord.setBoolean(2, true);
                psUpdateRecord.setInt(3, borrowRecordId);
                psUpdateRecord.executeUpdate();
            }

            // 3. Update book's rest_number and status
            Book book = bookDao.getBookById(record.getBookId()); // bookDao should handle its own connection or be passed one
            if (book != null) {
                book.setRestNumber(book.getRestNumber() + 1);
                book.setStatus("available"); // Always set to available when a book is returned
                // bookDao.updateBook(book);
                 try (PreparedStatement psUpdateBook = conn.prepareStatement("UPDATE books SET rest_number = ?, status = ? WHERE id = ?")) {
                    psUpdateBook.setInt(1, book.getRestNumber());
                    psUpdateBook.setString(2, book.getStatus());
                    psUpdateBook.setInt(3, book.getId());
                    psUpdateBook.executeUpdate();
                }
            } else {
                 conn.rollback(); // Book not found, something is wrong
                 return false;
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.out.println("Return book transaction failed: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    public List<BorrowRecord> getBorrowRecordsByUserId(int userId) {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BORROW_RECORDS_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRowToBorrowRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public List<BorrowRecord> getBorrowRecordsByBookId(int bookId) {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BORROW_RECORDS_BY_BOOK_ID)) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRowToBorrowRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    private BorrowRecord getBorrowRecordById(int id, Connection conn) throws SQLException {
        BorrowRecord record = null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BORROW_RECORD_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    record = mapRowToBorrowRecord(rs);
                }
            }
        }
        return record;
    }

    private BorrowRecord mapRowToBorrowRecord(ResultSet rs) throws SQLException {
        return new BorrowRecord(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getTimestamp("borrow_date"),
                rs.getTimestamp("return_date"),
                rs.getBoolean("returned")
        );
    }
}