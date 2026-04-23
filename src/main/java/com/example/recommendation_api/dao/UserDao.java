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
    // Insert
    // It takes users data and insert it to db
    public void saveUser (UserDto user) {
        //create sql query
        String sql = " insert into dolly.users (first_name, last_name, email, password) values " + "('" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getEmail() + "', '" + user.getPassword() + "')";
        //connect to db
        try (Connection conn = databaseConfig.getConnection();
             // create statement object to send sql query to db
             Statement statement = conn.createStatement())
        {
            //execute query
              statement.executeUpdate(sql);
              System.out.println("User created Successfully");
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    //Login POST call
    //
    public UserDto loginUser(LoginModel loginModel) {

        String sql = "SELECT * FROM dolly.users WHERE email = '"
                + loginModel.getEmail() + "' AND password = '" + loginModel.getPassword() + "'";

        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            //move cursor to first row of result
            if (rs.next()) {
                // create an empty object to store user data
                UserDto user = new UserDto();
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                //user.setPassword(rs.getString("password"));

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