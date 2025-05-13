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
    DBUtil dbUtil = new DBUtil("jdbc:mysql://localhost:3306/libraryy", "root", "kxboons");
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE email = ?";

    public boolean insertUser(User user) {
        try {
            Connection conn = dbUtil.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
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
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                users.add(new User(username, password, email));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
