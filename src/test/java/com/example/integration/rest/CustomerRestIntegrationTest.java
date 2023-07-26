package com.example.integration.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerRestIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("顧客検索")
    class GetCustomersTest {
        @Test
        @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
        @DisplayName("USER権限で全顧客を検索できる")
        void success() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("user@example.com", "user");
            RequestEntity<Void> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, URI.create("/api/customers"));
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            String actualJson = responseEntity.getBody();
            assertAll(
                    () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                    () -> JSONAssert.assertEquals("""
                       [
                           {
                               "id": 1,
                               "firstName": "友香",
                               "lastName": "菅井",
                               "email": "ysugai@sakura.com",
                               "birthday": "1995-11-29"
                           },
                           {
                               "id": 2,
                               "firstName": "久美",
                               "lastName": "佐々木",
                               "email": "ksasaki@hinata.com",
                               "birthday": "1996-01-22"
                           }
                       ]
                        """, actualJson, false)
            );
        }
    }

    @Nested
    @DisplayName("顧客登録")
    class PostCustomerTest {
        @Test
        @DisplayName("ADMIN権限で顧客登録できる")
        @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
        void success() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("admin@example.com", "admin");
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            String postJson = """
                    {
                        "firstName":"天",
                        "lastName":"山﨑",
                        "email":"tyamasaki@sakura.com",
                        "birthday":"2005-09-28"
                    }
                    """;
            RequestEntity<String> requestEntity = new RequestEntity<>(postJson, httpHeaders, HttpMethod.POST, URI.create("/api/customers"));
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                    () -> assertEquals(URI.create("http://localhost:" + port + "/api/customers/3"), responseEntity.getHeaders().getLocation())
            );
        }
    }
}
