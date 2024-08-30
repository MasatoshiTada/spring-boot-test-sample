package com.example.web.rest;

import com.example.annotation.RestControllerTest;
import com.example.service.CustomerService;
import com.example.web.annotation.TestWithAdmin;
import com.example.web.annotation.TestWithAnonymous;
import com.example.persistence.entity.Customer;
import com.example.web.annotation.TestWithUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RestControllerTest
public class CustomerRestControllerTest {

    @Autowired
    MockMvc mvc;

    // モックのCustomerServiceをDI
    @Autowired
    CustomerService customerService;

    @Nested
    @DisplayName("全顧客の取得")
    class GetCustomers {
        final MockHttpServletRequestBuilder request = get("/api/customers")
                .accept(MediaType.APPLICATION_JSON);
        final String expectedJson = """
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
                """;

        @BeforeEach
        void beforeEach() {
            when(customerService.findAll()).thenReturn(List.of(
                    new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29)),
                    new Customer(2, "久美", "佐々木", "ksasaki@hinata.com", LocalDate.of(1996, 1, 22))
            ));
        }

        @TestWithUser
        @DisplayName("userはOK")
        void userOk() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @TestWithAdmin
        @DisplayName("adminはOK")
        void adminOk() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @TestWithAnonymous
        @DisplayName("匿名はNG")
        void anonymousNg() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("顧客の登録")
    class PostCustomer {
        final String validJson = """
                        {
                            "firstName":"天",
                            "lastName":"山﨑",
                            "mailAddress":"tyamasaki@sakura.com",
                            "birthday":"2005-09-28"
                        }
                        """;

        MockHttpServletRequestBuilder createRequest(String json) {
            return post("/api/customers")
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json);
        }

        @BeforeEach
        void beforeEach() {
            doAnswer(invocation -> {
                Customer customer = invocation.getArgument(0);
                customer.setId(999);
                return null;
            }).when(customerService).save(any(Customer.class));
        }

        @TestWithUser
        @DisplayName("userはNG")
        void userNg() throws Exception {
            mvc.perform(createRequest(validJson))
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        @DisplayName("adminはOK")
        void adminOk() throws Exception {
            mvc.perform(createRequest(validJson))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", "/api/customers/999"));
        }

        @TestWithAdmin
        @DisplayName("adminで不正なデータを登録しようとすると、400 Bad Request")
        void adminInvalid() throws Exception {
            mvc.perform(createRequest("""
                        {
                            "firstName":"",
                            "lastName":"",
                            "mailAddress":"",
                            "birthday":""
                        }
                        """))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                            {
                                "type":"about:blank",
                                "title":"バリデーションエラー",
                                "status":400,
                                "detail":"不正な入力です",
                                "instance":"/api/customers",
                                "messages": [
                                    "名は1文字以上32文字以下です",
                                    "姓は1文字以上32文字以下です",
                                    "メールアドレスはxxx@xxx形式です",
                                    "誕生日はyyyy-MM-dd形式です"
                                ]
                            }
                            """));
        }

        @TestWithAnonymous
        @DisplayName("匿名はNG")
        void anonymousNg() throws Exception {
            mvc.perform(createRequest(validJson))
                    .andExpect(status().isUnauthorized());
        }
    }
}
