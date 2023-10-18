package com.example.restwithspringbootandjava.integrationtests.testcontainers.controller.withjson;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restwithspringbootandjava.configs.TestConfigs;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.dto.security.AccountCredentialsDTO;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.dto.security.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {
    private static TokenDTO tokenDTO;

    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException  {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {

        var newTokenDTO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(newTokenDTO.getAccessToken());
        assertNotNull(newTokenDTO.getRefreshToken());
    }
}