package com.github.guiziin227.restspringboot.controller;

import com.github.guiziin227.restspringboot.controller.docs.AuthControllerDocs;
import com.github.guiziin227.restspringboot.dto.security.AccountCredentialsDTO;
import com.github.guiziin227.restspringboot.dto.security.TokenDTO;
import com.github.guiziin227.restspringboot.service.AuthService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Sign in to the application")
    @PostMapping("/signin")
    @Override
    public ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO credentials) {

        if (credentials == null
                || StringUtils.isEmpty(credentials.getUsername())
                || StringUtils.isEmpty(credentials.getPassword()))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var token = authService.signIn(credentials);

        if (token == null || token.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(token.getBody());
    }

    @Operation(summary = "Refresh the authentication token")
    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username,
                                          @RequestHeader("Authorization") String refreshToken) {

        if (parametersAreInvalid(username, refreshToken)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        var token = authService.refreshToken(username, refreshToken);

        if (token == null || token.getBody() == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().body(token.getBody());
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken)
                || !refreshToken.startsWith("Bearer ");
    }

    @PostMapping(value = "/createUser")
    @Override
    public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials) {
        return authService.create(credentials);
    }
}
