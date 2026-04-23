package com.example.recommendation_api.controller;

import com.example.recommendation_api.configuration.DatabaseConfig;
;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/health")



public class HelloSwati {
    private final DatabaseConfig databaseConfig;

    public HelloSwati(DatabaseConfig databaseConfig){
        this.databaseConfig = databaseConfig;
    }

    @GetMapping(
            value="/check",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getHealthCheck() throws SQLException {
        Connection connection = databaseConfig.getConnection();
        return null;
    }
}
