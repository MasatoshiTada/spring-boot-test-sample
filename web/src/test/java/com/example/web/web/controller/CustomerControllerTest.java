package com.example.web.web.controller;

import com.example.web.security.details.AccountDetailsService;
import com.example.web.service.CustomerService;
import com.example.web.web.annotation.TestWithAdmin;
import com.example.web.web.annotation.TestWithAnonymous;
import com.example.web.web.annotation.TestWithUser;
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
public class CustomerControllerTest {

    @Autowired
    MockMvc mvc;

    // CustomerServiceをMockitoでモック化する
    @MockBean
    CustomerService customerService;

    @Nested
    class トップ画面へのアクセス {
        final MockHttpServletRequestBuilder request = get("/")
                .accept(MediaType.TEXT_HTML);

        @TestWithUser
        void userはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(request)
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    class 追加画面へのアクセス {
        final MockHttpServletRequestBuilder request = get("/insertMain")
                .accept(MediaType.TEXT_HTML);

        @TestWithUser
        void userはNG() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(view().name("insertMain"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(request)
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    class 追加の実行 {
        final MultiValueMap<String, String> validData =
                new LinkedMultiValueMap<>() {{
                    add("firstName", "天");
                    add("lastName", "山﨑");
                    add("email", "tyamasaki@sakura.com");
                    add("birthday", "2005-09-28");
                }};

        MockHttpServletRequestBuilder createRequest(MultiValueMap<String, String> formData) {
            return post("/insertComplete")
                    .params(formData)
                    .with(csrf())
                    .accept(MediaType.TEXT_HTML);
        }

        @TestWithUser
        void userはNG() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().isForbidden());
        }

        @TestWithAdmin
        void adminはOK() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }

        @TestWithAdmin
        void 不正なデータを登録しようとするとバリデーションエラーで入力画面に戻る() throws Exception {
            MultiValueMap<String, String> invalidData =
                    new LinkedMultiValueMap<>() {{
                        add("firstName", "");
                        add("lastName", "");
                        add("email", "");
                        add("birthday", "");
                    }};
            mvc.perform(createRequest(invalidData))
                    .andExpect(status().isOk())
                    .andExpect(view().name("insertMain"));
        }

        @TestWithAnonymous
        void 匿名はNG_ログイン画面にリダイレクトされる() throws Exception {
            mvc.perform(createRequest(validData))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }
}
