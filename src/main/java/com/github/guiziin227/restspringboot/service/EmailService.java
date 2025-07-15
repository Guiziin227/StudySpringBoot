package com.github.guiziin227.restspringboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.guiziin227.restspringboot.config.EmailConfig;
import com.github.guiziin227.restspringboot.dto.request.EmailRequestDTO;
import com.github.guiziin227.restspringboot.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequestDTO) {
        emailSender
                .to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .send(emailConfig);
    }

    public void sendWithAttachment(String emailRequestJson, MultipartFile multipartFile) {
        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            File tempFile = File.createTempFile("attachment", multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);

            emailSender
                    .to(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);

        } catch (JsonProcessingException e){
            throw new RuntimeException("Failed to parse email request JSON", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (multipartFile != null && multipartFile.getOriginalFilename() != null) {
                File tempFile = new File(multipartFile.getOriginalFilename());
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }

}
