package services;

import properties.MailProperties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
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
//    public  void sendEmail(String to, String subject, String content) throws MessagingException{
//        // tạo mail
//        Message message = new MimeMessage(session);
//        message.setFrom(new InternetAddress(MailProperties.getEmail()));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
//        message.setSubject(subject);
//        message.setContent(content, "text/html; charset=UTF-8");
//        // gửi mail
//        Transport.send(message);
//    }

//    Phuong thuc gui mail co tep dinh kem

    public void sendMaillWithAttachment(String to, String subject, String content, String filePath) throws MessagingException, IOException {
//        Tao doi tuong mail
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MailProperties.getEmail()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        message.setSubject(subject);
//        Tao noi dung email
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(content, "text/html;charset=UTF-8");
//        tao tep dinh kem
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(filePath));

//        ket noi noi dung email va tep dinh kem
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        Transport.send(message);
    }

}
