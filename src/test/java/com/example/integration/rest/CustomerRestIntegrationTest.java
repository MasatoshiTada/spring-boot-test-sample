package com.example.integration.rest;

import com.example.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class CustomerRestIntegrationTest {
    RestClient restClient;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach(@Autowired RestClient.Builder restClientBuilder, @LocalServerPort int port) {
        restClient = restClientBuilder
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * ログインします。
     *
     * @return セッションID（"JSESSIONID=xxxxxxxx"形式）とCSRFトークンを持つLoginResult
     */
    LoginResult login() {
        // CSRFトークン取得
        ResponseEntity<DefaultCsrfToken> csrfTokenResponse = restClient.get()
                .uri("/api/csrf")
                .retrieve()
                .toEntity(DefaultCsrfToken.class);
        String csrfToken = csrfTokenResponse.getBody().getToken();
        String sessionCookie = csrfTokenResponse.getHeaders().get("Set-Cookie").getFirst();
        String sessionId = Arrays.stream(sessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();

        // ADMIN権限でログイン
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("/login")
                .body("username=admin@example.com&password=admin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", sessionId)
                .header("X-CSRF-TOKEN", csrfToken)
                .retrieve()
                .toBodilessEntity();
        String newSessionCookie = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).getFirst();
        String newSessionId = Arrays.stream(newSessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();
        return new LoginResult(newSessionId, csrfToken);
    }


    @Nested
    @DisplayName("全顧客の取得")
    class GetCustomers {
        @Test
        @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
        @DisplayName("ADMIN権限で全顧客を取得できる")
        void success() {
            LoginResult loginResult = login();
            ResponseEntity<String> responseEntity = restClient.get()
                    .uri("/api/customers")
                    .header("Cookie", loginResult.sessionId())
                    .retrieve()
                    .toEntity(String.class);
            assertAll(
                    () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                    () -> JSONAssert.assertEquals("""
                            [
                                {
                                    "id": 1,
                                    "firstName": "友香",
                                    "lastName": "菅井",
                                    "mailAddress": "ysugai@sakura.com",
                                    "birthday": "1995-11-29"
                                },
                                {
                                    "id": 2,
                                    "firstName": "久美",
                                    "lastName": "佐々木",
                                    "mailAddress": "ksasaki@hinata.com",
                                    "birthday": "1996-01-22"
                                }
                            ]
                            """, responseEntity.getBody(), false)
            );
        }
    }

    @Nested
    @DisplayName("顧客の登録")
    class PostCustomer {
        @Test
        @DisplayName("ADMIN権限で顧客登録できる")
        @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
        void success() {
            LoginResult loginResult = login();
            ResponseEntity<Void> responseEntity = restClient.post()
                    .uri("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Cookie", loginResult.sessionId())
                    .header("X-CSRF-TOKEN", loginResult.csrfToken())
                    .body("""
                            {
                                "firstName":"天",
                                "lastName":"山﨑",
                                "mailAddress":"tyamasaki@sakura.com",
                                "birthday":"2005-09-28"
                            }
                            """)
                    .retrieve()
                    .toBodilessEntity();
            assertAll(
                    () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                    () -> assertEquals(URI.create("/api/customers/3"), responseEntity.getHeaders().getLocation())
            );
        }
    }
}
