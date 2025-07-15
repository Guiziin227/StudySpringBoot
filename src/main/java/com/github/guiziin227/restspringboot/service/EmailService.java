package com.github.guiziin227.restspringboot.service;

import com.github.guiziin227.restspringboot.config.EmailConfig;
import com.github.guiziin227.restspringboot.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(String to, String subject, String body) {
        emailSender
                .to(to)
                .withSubject(subject)
                .withMessage(body)
                .send(emailConfig);
    }

}
