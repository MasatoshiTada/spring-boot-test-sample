package com.example.web.web.controller;

import com.example.web.security.details.AccountDetailsService;
import com.example.web.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {AccountDetailsService.class}
))
@AutoConfigureMybatis
public class LoginControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CustomerService customerService;

    @Test
    @DisplayName("/loginにアクセスするとログイン画面が返される")
    void loginにアクセスするとログイン画面が返される() throws Exception {
        mvc.perform(get("/login").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void userでログインできる() throws Exception {
        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>() {{
                    add("username", "user@example.com");
                    add("password", "user");
                }};
        mvc.perform(post("/login")
                .params(formData)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void hogeでログインできない() throws Exception {
        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>() {{
                    add("username", "hoge@example.com");
                    add("password", "hoge");
                }};
        mvc.perform(post("/login")
                .params(formData)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}
