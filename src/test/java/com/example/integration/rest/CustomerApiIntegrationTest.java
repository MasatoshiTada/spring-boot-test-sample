package com.example.integration.rest;

import com.example.annotation.ApiIntegrationTest;
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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ApiIntegrationTest
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
public class CustomerApiIntegrationTest {
    RestClient restClient;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach(@Autowired RestClient.Builder restClientBuilder, @LocalServerPort int port) {
        restClient = restClientBuilder
                .baseUrl("http://localhost:" + port)
                .defaultStatusHandler(status -> true, (request, response) -> {
                    // ステータスコードが4xx・5xxであっても何もしない
                })
                .build();
    }

    /**
     * ログインします。
     * @return セッションID（"JSESSIONID=xxxxxxxx"形式）とCSRFトークンを持つSessionTokenPair
     */
    SessionTokenPair login() {
        // 初回CSRFトークン取得
        SessionTokenPair pair1 = getCsrfToken(null);

        // ADMIN権限でログイン
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("/login")
                .body("username=admin@example.com&password=admin&_csrf=" + pair1.csrfToken())  // CSRFトークンをリクエストパラメーターに指定
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Cookie", pair1.sessionId())
                .retrieve()
                .toBodilessEntity();
        String newSessionCookie = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).getFirst();
        String newSessionId = Arrays.stream(newSessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();

        // 2回目のCSRFトークン取得（ログイン成功時にCSRFトークンが変更されているため）
        // See https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/csrf/CsrfAuthenticationStrategy.java#L70
        SessionTokenPair pair2 = getCsrfToken(newSessionId);
        return pair2;
    }

    /**
     * セッションIDとCSRFトークンを取得します。
     * @param sessionId セッションID。未作成（初回アクセス）の場合はnull
     * @return セッションIDとCSRFトークンを持つSessionTokenPair
     */
    SessionTokenPair getCsrfToken(String sessionId) {
        // CSRFトークンを取得
        ResponseEntity<DefaultCsrfToken> csrfTokenResponse = restClient.get()
                .uri("/api/csrf")
                .header("Cookie", sessionId != null ? sessionId : "foo=bar" /* セッションがまだ無い場合は適当なCookieを設定 */)
                .retrieve()
                .toEntity(DefaultCsrfToken.class);
        String csrfToken = csrfTokenResponse.getBody().getToken();
        // Set-Cookieヘッダーを取得
        List<String> setCookieHeader = csrfTokenResponse.getHeaders().getOrEmpty("Set-Cookie");
        // Set-Cookieヘッダーが無い場合は引数のsessionIdを返す
        if (setCookieHeader.isEmpty()) {
            return new SessionTokenPair(sessionId, csrfToken);
        }
        // Set-Cookieヘッダーがある場合は新しいセッションIDを返す
        String sessionCookie = setCookieHeader.getFirst();
        String newSessionId = Arrays.stream(sessionCookie.split(";"))
                .filter(element -> element.startsWith("JSESSIONID="))
                .findFirst()
                .get();
        return new SessionTokenPair(newSessionId, csrfToken);
    }

    @Nested
    @DisplayName("全顧客の取得")
    class GetCustomers {
        @Test
        @DisplayName("ADMIN権限で全顧客を取得できる")
        void success() {
            SessionTokenPair sessionTokenPair = login();
            ResponseEntity<String> responseEntity = restClient.get()
                    .uri("/api/customers")
                    .header("Cookie", sessionTokenPair.sessionId())
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
        void success() {
            SessionTokenPair sessionTokenPair = login();
            ResponseEntity<Void> responseEntity = restClient.post()
                    .uri("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Cookie", sessionTokenPair.sessionId())
                    .header("X-CSRF-TOKEN", sessionTokenPair.csrfToken())
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
                    () -> assertEquals(URI.create("/api/customers/3"), responseEntity.getHeaders().getLocation()),
                    () -> assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "customer", """
                            id = 3
                            AND first_name = '天'
                            AND last_name = '山﨑'
                            AND mail_address = 'tyamasaki@sakura.com'
                            AND birthday = '2005-09-28'
                            """))
            );
        }
    }
}
