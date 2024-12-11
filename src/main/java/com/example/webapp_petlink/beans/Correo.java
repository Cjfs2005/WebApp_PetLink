package com.example.webapp_petlink.beans;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class Correo {

    private static final String senderEmail = "temporalpetlink@gmail.com";
    private static final String senderPassword = "iyly olxq qyzw wlyg";

    public void sendEmail(String correoReceptor, String asunto, String mensaje) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoReceptor));
        message.setSubject(asunto);
        message.setText(mensaje);
        Transport.send(message);
    }

    public Session getEmailSession(){
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }

    public Properties getGmailProperties() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return props;
    }

}
