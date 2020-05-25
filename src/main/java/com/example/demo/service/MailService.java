package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service public class MailService {

    @Autowired private JavaMailSender javaMailSender;

    public void mailResetPassword(String destinatario, String token)
        throws IOException, MessagingException {
        String template = readTemplate("resetPasswordEmail.html");
        String enlace = "http://localhost:8080/api/v1/users/password/reset?token=" + token;
        template = template.replace("*|ENLACE|*", enlace);
        sendEmail(destinatario, "Reset contraseña", template);
    }

    public void mailVerifyEmail(String destinatario, String token)
        throws IOException, MessagingException {
        String template = readTemplate("verifyEmailEmail.html");
        String enlace = "http://localhost:8080/api/v1/users/email/verify?token=" + token;
        template = template.replace("*|ENLACE|*", enlace);
        sendEmail(destinatario, "Verificar correo", template);
    }

    public void mailNewUser(String destinatario, String token)
        throws IOException, MessagingException {
        String template = readTemplate("newUser.html");
        String enlace = "http://localhost:8080/api/v1/users/email/verify?token=" + token;
        template = template.replace("*|ENLACE|*", enlace);
        sendEmail(destinatario, "Bienvenido", template);
    }

    private void sendEmail(String destinatario, String subject, String template)
        throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(subject);
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setFrom("dws@gmail.com");
        helper.setTo(destinatario);
        helper.setText(template, true);
        javaMailSender.send(message);
    }



    private String readTemplate(String file) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(file);
        return readFromInputStream(inputStream);
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


}
