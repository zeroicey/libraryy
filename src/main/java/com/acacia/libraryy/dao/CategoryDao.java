package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.Category;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");

    private static final String INSERT_CATEGORY = "INSERT INTO categories(name) VALUES(?)";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM categories";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id = ?";
    private static final String SELECT_CATEGORIES_BY_BOOK_ID = "SELECT c.* FROM categories c JOIN book_categories bc ON c.id = bc.category_id WHERE bc.book_id = ?";

    public boolean insertCategory(Category category) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getName());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error inserting category: " + e.getMessage());
            return false;
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_CATEGORIES);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all categories: " + e.getMessage());
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        Category category = null;
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    category = mapRowToCategory(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching category by ID: " + e.getMessage());
        }
        return category;
    }

    public boolean updateCategory(Category category) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_CATEGORY)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategoryById(int id) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    public List<Category> getCategoriesByBookId(int bookId) {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_CATEGORIES_BY_BOOK_ID)) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapRowToCategory(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories by book ID: " + e.getMessage());
        }
        return categories;
    }

    private Category mapRowToCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}