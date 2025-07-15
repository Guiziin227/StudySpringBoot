package com.github.guiziin227.restspringboot.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.mail")
public class EmailConfig {

    private String host;
    private int port;
    private String username;
    private String password;

    private String from;
    private boolean ssl;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmailConfig that = (EmailConfig) o;
        return port == that.port && ssl == that.ssl && Objects.equals(host, that.host) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, password, from, ssl);
    }
}
