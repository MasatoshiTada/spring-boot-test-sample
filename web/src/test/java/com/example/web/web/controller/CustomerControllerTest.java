package com.example.web.web.controller;

import com.example.web.security.details.AccountDetailsService;
import com.example.web.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {AccountDetailsService.class}
))
@AutoConfigureMybatis
public class CustomerControllerTest {

    @Autowired
    MockMvc mvc;

    // CustomerServiceをMockitoでモック化する
    @MockBean
    CustomerService customerService;

    @Nested
    class トップ画面へのアクセス {
        @TestWithUser
        void userはOK() throws Exception {
            mvc.perform(get("/")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(get("/")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(get("/")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    class 追加画面へのアクセス {
        @TestWithUser
        void userはNG() throws Exception {
            mvc.perform(get("/insertMain")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().isForbidden());
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(get("/insertMain")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().isOk())
                    .andExpect(view().name("insertMain"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(get("/")
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    class 追加の実行 {

        final MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        @BeforeEach
        void setUp() {
            formData.add("firstName", "天");
            formData.add("lastName", "山﨑");
            formData.add("email", "tyamasaki@sakura.com");
            formData.add("birthday", "2005-09-28");
        }

        @TestWithUser
        void userはNG() throws Exception {
            mvc.perform(post("/insertComplete")
                    .params(formData)
                    .with(csrf())
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().isForbidden());
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(post("/insertComplete")
                    .params(formData)
                    .with(csrf())
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(post("/insertComplete")
                    .params(formData)
                    .with(csrf())
                    .accept(MediaType.TEXT_HTML)
            ).andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Test
    @WithUserDetails(userDetailsServiceBeanName = "accountDetailsService",
            value = "user@example.com")
    @interface TestWithUser {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Test
    @WithUserDetails(userDetailsServiceBeanName = "accountDetailsService",
            value = "admin@example.com")
    @interface TestWithAdmin {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Test
    @WithAnonymousUser
    @interface TestWithAnonymous {}
}
