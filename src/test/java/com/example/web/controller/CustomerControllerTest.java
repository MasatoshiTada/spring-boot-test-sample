package com.example.web.controller;

import com.example.annotation.ControllerTest;
import com.example.web.annotation.TestWithAdmin;
import com.example.web.annotation.TestWithAnonymous;
import com.example.web.annotation.TestWithUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest
public class CustomerControllerTest {

    @Autowired
    MockMvc mvc;

    @Nested
    @DisplayName("トップ画面へのアクセス")
    class TopPage {
        final MockHttpServletRequestBuilder request = get("/")
                .accept(MediaType.TEXT_HTML);

        @TestWithUser
        @DisplayName("userはOK")
        void userOk() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAdmin
        @DisplayName("adminはOK")
        void adminOk() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAnonymous
        @DisplayName("匿名はログイン画面にリダイレクトされる")
        void anonymousNg() throws Exception {
            mvc.perform(request)
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    @DisplayName("追加画面へのアクセス")
    class SaveMain {
        final MockHttpServletRequestBuilder request = get("/saveMain")
                .accept(MediaType.TEXT_HTML);

        @TestWithUser
        @DisplayName("userはNG")
        void userNg() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        @DisplayName("adminはOK")
        void adminOk() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("saveMain"));
        }

        @TestWithAnonymous
        @DisplayName("匿名はログイン画面にリダイレクトされる")
        void anonymousNg() throws Exception {
            mvc.perform(request)
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    @DisplayName("追加の実行")
    class SaveComplete {
        final MultiValueMap<String, String> validData =
                new LinkedMultiValueMap<>() {{
                    add("firstName", "天");
                    add("lastName", "山﨑");
                    add("mailAddress", "tyamasaki@sakura.com");
                    add("birthday", "2005-09-28");
                }};

        MockHttpServletRequestBuilder createRequest(MultiValueMap<String, String> formData) {
            return post("/saveComplete")
                    .params(formData)
                    .with(csrf())
                    .accept(MediaType.TEXT_HTML);
        }

        @TestWithUser
        @DisplayName("userはNG")
        void userNg() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        @DisplayName("adminはOK")
        void adminOk() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }

        @TestWithAdmin
        @DisplayName("adminで不正なデータを登録しようとすると、バリデーションエラーで入力画面に戻る")
        void adminInvalid() throws Exception {
            MultiValueMap<String, String> invalidData =
                    new LinkedMultiValueMap<>() {{
                        add("firstName", "");
                        add("lastName", "");
                        add("mailAddress", "");
                        add("birthday", "");
                    }};
            mvc.perform(createRequest(invalidData))
                    .andExpect(status().isOk())
                    .andExpect(view().name("saveMain"));
        }

        @TestWithAnonymous
        @DisplayName("匿名はログイン画面にリダイレクトされる")
        void anonymousNg() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }
}
