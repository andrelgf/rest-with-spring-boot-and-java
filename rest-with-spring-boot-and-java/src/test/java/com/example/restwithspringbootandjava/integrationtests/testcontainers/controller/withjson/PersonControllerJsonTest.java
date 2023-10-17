package com.example.restwithspringbootandjava.integrationtests.testcontainers.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import com.example.restwithspringbootandjava.integrationtests.testcontainers.dto.security.AccountCredentialsDTO;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.dto.security.TokenDTO;
import com.example.restwithspringbootandjava.configs.TestConfigs;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import com.example.restwithspringbootandjava.integrationtests.testcontainers.dto.PersonDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

        private static RequestSpecification specification;
        private static ObjectMapper objectMapper;
        private static PersonDTO personDTO;

        @BeforeAll
        public static void setup() {
                objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                personDTO = new PersonDTO();
        }

        @Test
        @Order(0)
        public void authorization() throws JsonMappingException, JsonProcessingException {
                AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");

                var accessToken = given()
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
                                .as(TokenDTO.class)
                                .getAccessToken();

                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                                .setBasePath("/api/person/v1")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();
        }

        @Test
        @Order(1)
        public void testCreate() throws JsonMappingException, JsonProcessingException {
                mockPerson();

                var content = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST_8080)
                                .body(personDTO)
                                .when()
                                .post()
                                .then()
                                .statusCode(200)
                                .extract()
                                .body()
                                .asString();

                PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
                personDTO = createdPerson;

                assertNotNull(createdPerson);
                assertNotNull(createdPerson.getId());
                assertNotNull(createdPerson.getFirstName());
                assertNotNull(createdPerson.getLastName());
                assertNotNull(createdPerson.getAddress());
                assertNotNull(createdPerson.getGender());

                assertEquals("Richard", createdPerson.getFirstName());
                assertEquals("Stallman", createdPerson.getLastName());
                assertEquals("New York", createdPerson.getAddress());
                assertEquals("Male", createdPerson.getGender());
        }

        @Test
        @Order(2)
        public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
                mockPerson();

                var content = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NO_CORS)
                                .body(personDTO)
                                .when()
                                .post()
                                .then()
                                .statusCode(403)
                                .extract()
                                .body()
                                .asString();

                assertNotNull(content);

                assertEquals("Invalid CORS request", content);
        }

        @Test
        @Order(3)
        public void testFindById() throws JsonMappingException, JsonProcessingException {
                mockPerson();

                var content = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST_8080)
                                .pathParam("id", personDTO.getId())
                                .when()
                                .get("{id}")
                                .then()
                                .statusCode(200)
                                .extract()
                                .body()
                                .asString();

                PersonDTO persistedPersonDTO = objectMapper.readValue(content, PersonDTO.class);
                personDTO = persistedPersonDTO;

                assertNotNull(persistedPersonDTO);
                assertNotNull(persistedPersonDTO.getId());
                assertNotNull(persistedPersonDTO.getFirstName());
                assertNotNull(persistedPersonDTO.getLastName());
                assertNotNull(persistedPersonDTO.getAddress());
                assertNotNull(persistedPersonDTO.getGender());

                assertEquals("Richard", persistedPersonDTO.getFirstName());
                assertEquals("Stallman", persistedPersonDTO.getLastName());
                assertEquals("New York", persistedPersonDTO.getAddress());
                assertEquals("Male", persistedPersonDTO.getGender());
        }

        @Test
        @Order(4)
        public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
                mockPerson();

                var content = given().spec(specification)
                                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NO_CORS)
                                .pathParam("id", personDTO.getId())
                                .when()
                                .get("{id}")
                                .then()
                                .statusCode(403)
                                .extract()
                                .body()
                                .asString();

                assertNotNull(content);

                assertEquals("Invalid CORS request", content);
        }

        private void mockPerson() {
                personDTO.setFirstName("Richard");
                personDTO.setLastName("Stallman");
                personDTO.setAddress("New York");
                personDTO.setGender("Male");
        }

}
