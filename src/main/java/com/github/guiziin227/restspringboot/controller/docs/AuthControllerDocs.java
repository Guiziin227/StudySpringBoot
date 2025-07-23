package com.github.guiziin227.restspringboot.controller.docs;

import com.github.guiziin227.restspringboot.dto.security.AccountCredentialsDTO;
import com.github.guiziin227.restspringboot.dto.security.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthControllerDocs {
    @Operation(summary = "Sign in to the application")
    ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh the authentication token")
    ResponseEntity<?> refreshToken(@PathVariable("username") String username,
                                   @RequestHeader("Authorization") String refreshToken);

    @Operation(summary = "Create new account credentials")
    AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials);
}
