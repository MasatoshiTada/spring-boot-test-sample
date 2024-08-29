package com.example.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Securityの設定クラスです。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * 認証認可などの設定を行います。
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 例外（滅多に発生しません）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginPage("/login")
                .permitAll()
        ).httpBasic(basic -> basic
                .realmName("Spring")
        ).authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/save*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/csrf").permitAll()
                .anyRequest().authenticated()
        ).logout(logout -> logout
                .invalidateHttpSession(true)
                .permitAll()
        );
        return http.build();
    }

    /**
     * パスワードエンコーダーのBeanを定義します。
     * @return パスワードエンコーダー
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
