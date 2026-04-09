package com.example.recommendation_api.dao;


import com.example.recommendation_api.configuration.DatabaseConfig;
import com.example.recommendation_api.dto.UserDto;
import com.example.recommendation_api.model.LoginModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final DatabaseConfig databaseConfig;

    // INSERT USER
    public void saveUser(UserDto user) {
        String sql = "INSERT INTO dolly.users (first_name, last_name, email, password) VALUES ('"
                + user.getFirstName() + "', '"
                + user.getLastName() + "', '"
                + user.getEmail() + "', '"
                + user.getPassword() + "')";

        try (Connection conn = databaseConfig.getConnection();
             Statement statement = conn.createStatement()) {

            statement.executeUpdate(sql);
            System.out.println("User inserted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public UserDto loginUser(String email, String password) {

        String sql = "SELECT * FROM dolly.users WHERE email = '"
                + email + "' AND password = '" + password + "'";

        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                UserDto user = new UserDto();
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                System.out.println("Login successful");
                return user;
            } else {
                System.out.println("Invalid email or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

  //Find User BY ID
    public UserDto getUserById(UUID id) {

        //sql query creation
        String sql = "SELECT * FROM dolly.users WHERE id = '" + id + "'";
        //connect to db and create sql executor
        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
           // execute query
            ResultSet rs = stmt.executeQuery(sql);
            // if row exists true else false
            if (rs.next()) {
                // create UserDto object
                UserDto user = new UserDto();
                // convert db to java object
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                // return user
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
      // if No user with that ID exists give null
        return null;
    }
}