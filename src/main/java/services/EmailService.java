package services;

import properties.MailProperties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private Session session;
    public EmailService(){
        // Taoj session dung chung
        Properties properties = MailProperties.getProperties();
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailProperties.getEmail(), MailProperties.getPassword());
            }
        };
        this.session = Session.getInstance(properties, auth);
    }
    public  void sendEmail(String to, String subject, String content) throws MessagingException{
        // tạo mail
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MailProperties.getEmail()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        message.setSubject(subject);
        message.setContent(content, "text/html; charset=UTF-8");
        // gửi mail
        Transport.send(message);
    }

}
