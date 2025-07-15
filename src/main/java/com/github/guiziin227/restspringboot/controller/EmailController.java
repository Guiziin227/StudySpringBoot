package com.github.guiziin227.restspringboot.controller;

import com.github.guiziin227.restspringboot.controller.docs.EmailControllerDocs;
import com.github.guiziin227.restspringboot.dto.request.EmailRequestDTO;
import com.github.guiziin227.restspringboot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        emailService.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sendEmail(String emailRequestJSON, MultipartFile multipartFile) {
        return null;
    }
}
