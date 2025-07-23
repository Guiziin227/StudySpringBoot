package com.github.guiziin227.restspringboot.service;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.dto.security.AccountCredentialsDTO;
import com.github.guiziin227.restspringboot.dto.security.TokenDTO;
import com.github.guiziin227.restspringboot.exception.RequiredObjectIsNullException;
import com.github.guiziin227.restspringboot.jwt.JwtTokenProvider;
import com.github.guiziin227.restspringboot.model.Person;
import com.github.guiziin227.restspringboot.model.User;
import com.github.guiziin227.restspringboot.repository.UserRepository;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.github.guiziin227.restspringboot.dto.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    @Transactional
    public AccountCredentialsDTO create(AccountCredentialsDTO user) {
        if (user == null) throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");

        logger.info("Creating one new USER!");
        var entity = new User();
        entity.setUsername(user.getUsername());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setFullname(user.getFullName());
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = userRepository.save(entity);
        return new AccountCredentialsDTO(dto.getUsername(),
                dto.getPassword(),
                dto.getFullname());
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("pbkdf2",encoders);

        encoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

        return encoder.encode(password);
    }

}
