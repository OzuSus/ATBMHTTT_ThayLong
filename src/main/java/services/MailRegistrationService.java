package services;

import javax.mail.MessagingException;
import utils.FileUtils;

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
//                "<p><strong>Public Key:</strong> " + publicKey + "</p>" +
//                "<p><strong>Private Key:</strong> " + privateKey + "</p>" +

                "<p>Please save your Private Key locally for future use.</p>" +
                "</body></html>";
        try {
            // luu cac khoa vao file
            String publicKeyFile = "public_key_" + System.currentTimeMillis() + ".txt";
            String privateKeyFile = "private_key_" + System.currentTimeMillis() + ".txt";
            FileUtils.saveKeyToFile(publicKeyFile, publicKey);
            FileUtils.saveKeyToFile(privateKeyFile, privateKey);

            emailService.sendMaillWithAttachment(to, subject, content, publicKeyFile);
            emailService.sendMaillWithAttachment(to, subject, content, privateKeyFile); // Gọi phương thức gửi email
            // Sau khi gửi, có thể xóa file tạm nếu không cần lưu lại
            FileUtils.deleteFile(publicKeyFile);
            FileUtils.deleteFile(privateKeyFile);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException("Error handling private key file: " + e.getMessage());
        }
//        emailService.sendEmail(to, subject, content); // Gọi phương thức gửi email

    }
}
