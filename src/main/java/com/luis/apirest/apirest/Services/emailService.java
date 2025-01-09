package com.luis.apirest.apirest.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class emailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Configurar encabezados del correo
        helper.setFrom("luis.j.fcfm@gmail.com"); // Cambia por tu correo verificado
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // `true` permite contenido HTML

        // Enviar el mensaje
        mailSender.send(message);
    }
}

