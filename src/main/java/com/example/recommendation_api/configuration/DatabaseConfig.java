package com.example.recommendation_api.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${test.swati.value}")
    private String testSwatiValue;


    public Connection getConnection() throws SQLException {
        System.out.println("USER : "+username);
        System.out.println("URL : "+url);
        System.out.println("PASSWORD : "+password);
        System.out.println("testSwatiValue : "+testSwatiValue);
        return DriverManager.getConnection(url, username, password);
    }
}

