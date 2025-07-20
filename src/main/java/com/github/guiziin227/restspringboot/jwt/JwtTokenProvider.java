package com.github.guiziin227.restspringboot.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.github.guiziin227.restspringboot.dto.security.TokenDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validy = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = getAccessToken(username, roles, now, validy);
        String refresh = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validy, accessToken, refresh);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        return "";
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validy) {
        return "";
    }
}
