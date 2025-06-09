package com.example.dijkstra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.allowed-hosts}")
    private List<String> allowedHosts;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsFilter()))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(conf -> conf.requestMatchers("/",
                                                                "/register",
                                                                "/api/**",
                                                                "/restorePassword/**",
                                                                "/public/css/**",
                                                                "/user/assets/**",
                                                                "/user/webjars/**",
                                                                "/assets/**",
                                                                "/panel/**").permitAll().anyRequest().authenticated())
            .formLogin(formLogin -> formLogin.loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/", true).permitAll())
            .rememberMe(conf -> conf.tokenValiditySeconds(5 * 24 * 60 * 60).key("AbcdefghiJklmNoPqRstUvXyz"))
            .logout(conf -> conf.logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID", "remember-me"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private CorsConfigurationSource corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedHosts);
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        configuration.setExposedHeaders(List.of(CONTENT_DISPOSITION));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
