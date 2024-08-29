package com.example.web.controller;

import com.example.annotation.ControllerTest;
import com.example.web.annotation.TestWithAnonymous;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest
public class LoginControllerTest {

    @Autowired
    MockMvc mvc;

    @Nested
    @DisplayName("ログイン画面へのアクセス")
    class LoginPage {
        @TestWithAnonymous
        @DisplayName("/loginにアクセスするとログイン画面が返される")
        void success() throws Exception {
            mvc.perform(get("/login").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(view().name("login"));
        }
    }

    @Nested
    @DisplayName("ログインの実行")
    class Login {
        @Test
        @DisplayName("存在するユーザーでログインできる")
        void success() throws Exception {
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
        @DisplayName("存在しないユーザーでログインできない")
        void failure() throws Exception {
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
}
