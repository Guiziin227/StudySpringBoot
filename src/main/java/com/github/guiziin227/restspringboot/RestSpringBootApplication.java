package com.github.guiziin227.restspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RestSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestSpringBootApplication.class, args);


        generateHashedPassword();
    }

    private static void generateHashedPassword() {
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2",encoders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

        var pass1 = encoder.encode("password");
        var pass2 = encoder.encode("admin123");

        System.out.println(pass1);
        System.out.println(pass2);
    }

}
