package com.github.guiziin227.restspringboot.config;

import com.github.guiziin227.restspringboot.jwt.JwtTokenFilter;
import com.github.guiziin227.restspringboot.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2",encoders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
        return encoder;
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
        //@formatter:off

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter,UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(
                                                "/auth/signin",
                                                "/auth/refresh/**",
                                                "/auth/createUser",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**").permitAll()
                                        .requestMatchers("/api/**").authenticated()
                                        .requestMatchers("/users").denyAll()
                )
                .cors(cors -> {})
                .build();
        //@formatter:on
    }

}
