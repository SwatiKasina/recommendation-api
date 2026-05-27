package com.example.recommendation_api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/health")



public class HelloSwati {


    @GetMapping(
            value="/check",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getHealthCheck() throws SQLException {

        return null;
    }
}
