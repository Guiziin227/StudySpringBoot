package com.github.guiziin227.restspringboot.service;

import com.github.guiziin227.restspringboot.dto.security.AccountCredentialsDTO;
import com.github.guiziin227.restspringboot.dto.security.TokenDTO;
import com.github.guiziin227.restspringboot.jwt.JwtTokenProvider;
import com.github.guiziin227.restspringboot.repository.UserRepository;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = userRepository.findByUsername(credentials.getUsername());

        if (user == null) throw new UsernameNotFoundException("User not found " + credentials.getUsername());

        var tokenResponse = jwtTokenProvider.createAccessToken(
                user.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(tokenResponse);
    }


    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = userRepository.findByUsername(username);
        TokenDTO tokenDTO;

        if (user != null){
            tokenDTO = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("User not found " + username);
        }

        return ResponseEntity.ok(tokenDTO);
    }
}
