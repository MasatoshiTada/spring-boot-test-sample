package com.example.web.rest;

import com.example.security.config.SecurityConfig;
import com.example.web.annotation.TestWithAdmin;
import com.example.web.annotation.TestWithAnonymous;
import com.example.persistence.entity.Customer;
import com.example.security.details.AccountDetailsService;
import com.example.service.CustomerService;
import com.example.web.annotation.TestWithUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {AccountDetailsService.class, SecurityConfig.class}
))
@AutoConfigureMybatis
public class CustomerRestControllerTest {

    @Autowired
    MockMvc mvc;

    // CustomerServiceをMockitoでモック化する
    @MockBean
    CustomerService customerService;

    @Nested
    class 全顧客の取得 {
        final MockHttpServletRequestBuilder request = get("/api/customers")
                .accept(MediaType.APPLICATION_JSON);
        final String expectedJson = """
                [
                    {
                        "id": 1,
                        "firstName": "友香",
                        "lastName": "菅井",
                        "email": "ysugai@sakura.com",
                        "birthday": "1995-11-29"
                    }
                ]
                """;

        @BeforeEach
        void setUp() {
            when(customerService.findAll()).thenReturn(List.of(
                    new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29))
                    )
            );
        }

        @TestWithUser
        void userはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @TestWithAnonymous
        void 匿名はNG() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 顧客の登録 {
        final MockHttpServletRequestBuilder request = post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName":"天",
                            "lastName":"山﨑",
                            "email":"tyamasaki@sakura.com",
                            "birthday":"2005-09-28"
                        }
                        """);

        @BeforeEach
        void setUp() {
            doAnswer(invocation -> {
                Customer customer = invocation.getArgument(0);
                customer.setId(999);
                return null;
            }).when(customerService).save(any(Customer.class));
        }

        @TestWithUser
        void userはNG() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", "http://localhost/api/customers/999"));
        }

        @TestWithAnonymous
        void 匿名はNG() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isUnauthorized());
        }
    }
}
