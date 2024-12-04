package services;

import javax.mail.MessagingException;

public class MailRegistrationService implements IMailServices{
    private final String to;
    private final String publicKey;
    private final String privateKey;
    private final EmailService emailService;

    public MailRegistrationService(String to, String publicKey, String privateKey, EmailService emailService) {
        this.to = to;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.emailService = emailService;
    }

    @Override
    public void send() throws MessagingException {
        String subject = "Your Registration Keys";
        String content = "<html><body>" +
                "<h2>Welcome to our system!</h2>" +
                "<p>Here are your keys:</p>" +
                "<p><strong>Public Key:</strong> " + publicKey + "</p>" +
                "<p><strong>Private Key:</strong> " + privateKey + "</p>" +
                "<p>Please save your Private Key locally for future use.</p>" +
                "</body></html>";

        emailService.sendEmail(to, subject, content); // Gọi phương thức gửi email

    }
}
