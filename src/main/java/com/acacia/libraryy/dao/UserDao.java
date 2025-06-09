package com.acacia.libraryy.dao;

import com.acacia.libraryy.model.User;
import com.acacia.libraryy.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "123456");
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE email = ?";

    public boolean insertUser(String username, String password, String email) {
        try {
            Connection conn = dbUtil.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        Connection conn = dbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_USERS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                users.add(new User(id, username, password, email));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User getUserById(int id) {
        User user = null;
        Connection conn = dbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            user = getUser(user, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private User getUser(User user, PreparedStatement preparedStatement) throws SQLException {
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            user = new User(id, username, password, email);
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = null;
        Connection conn = dbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String password = rs.getString("password");
                String email = rs.getString("email");
                user = new User(id, username, password, email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = null;
        Connection conn = dbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                user = new User(id, username, password, email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User getUserByName(String name) {
        User user = null;
        Connection conn = dbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_USER_BY_NAME);
            preparedStatement.setString(1, name);
            user = getUser(user, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public boolean deleteUserById(int id) {
        try {
            Connection conn = dbUtil.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
