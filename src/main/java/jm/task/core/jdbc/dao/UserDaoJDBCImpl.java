package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {

    }


    public void createUsersTable() {
        Connection connection = Util.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS User (id INT NOT NULL AUTO_INCREMENT," +
                "name VARCHAR(255)," +
                "lastName VARCHAR(255)," +
                "age INT NOT NULL, PRIMARY KEY (id))";
        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.execute();
            connection.commit();
            System.out.println("Table created");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error rollback");
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException exe) {
                    exe.printStackTrace();
                }
            }
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void dropUsersTable() {

        Connection connection = Util.getConnection();

        try (
                Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {

        Connection connection = Util.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, last_name, age) VALUES(?,?,?) ")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("User " + name + " " + lastName + " added!");


    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public List<User> getAllUsers() {

        Connection connection = Util.getConnection();

        List<User> users = new ArrayList<>();

        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
