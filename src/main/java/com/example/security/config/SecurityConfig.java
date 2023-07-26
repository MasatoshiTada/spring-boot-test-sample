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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginPage("/login")
                .permitAll()
        ).httpBasic(basic -> basic
                .realmName("Spring")
        ).authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/insert*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("ADMIN")
                .anyRequest().authenticated()
        ).logout(logout -> logout
                .invalidateHttpSession(true)
                .permitAll()
        ).csrf(csrf -> csrf
                // 簡略化のため、Web APIへのCSRF対策を無効化しています。
                // 実際に無効化するかどうかは慎重に検討してください。
                .ignoringRequestMatchers("/api/**")
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
