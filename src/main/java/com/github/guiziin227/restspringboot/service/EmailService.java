package com.github.guiziin227.restspringboot.service;

import com.github.guiziin227.restspringboot.config.EmailConfig;
import com.github.guiziin227.restspringboot.dto.request.EmailRequestDTO;
import com.github.guiziin227.restspringboot.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
