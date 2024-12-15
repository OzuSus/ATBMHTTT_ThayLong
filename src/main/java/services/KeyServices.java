package services;

import dao.UserDAO;
import dao.UserDAOImplement;
import models.User;
import utils.RSAKeyUtil;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.List;

// xử lý lộ key
public class KeyServices {
    private final UserDAO userDAO;
    public KeyServices(UserDAO userDAO){
        if (userDAO == null) {
            throw new IllegalArgumentException("UserDAO cannot be null");
        }
        this.userDAO = userDAO;
    }
    public void revokeAndGenerateNewKey(int userId) throws Exception {
        // Thu hồi key cũ (cập nhật trạng thái trong DB)
        List<User> users = userDAO.getUserByID(userId);
//        User user = (User) userDAO.getUserByID(userId);
        if (users == null) {
            throw new Exception("Người dùng không tồn tại");
        }
        for (User user :users){
           // Revoke the curent key
            revokeCurrentKey(userId);
//        Generate a new key pair
            KeyPair newKeyPair = RSAKeyUtil.generateKeyPair();
            String newPublicKey =RSAKeyUtil.encodeKeyToBase64(newKeyPair.getPublic());
            String newPrivateKey = RSAKeyUtil.encodeKeyToBase64(newKeyPair.getPrivate());
//        Save the new public key in the database
//        saveNewPublicKey(userId, newPublicKey);
//        sendNewPrivateKey(userId, newPrivateKey);
//            userDAO.updateUserPublicKey(user.getId(), newPublicKey);
            userDAO.savePublicKey(user.getId(), newPublicKey);
            String email = user.getEmail();
            if (email != null && !email.isEmpty()){
                EmailService emailService = new EmailService();
//            String subject = "Cặp khóa mới của bạn";
//            String body = "Đây là private key mới của bạn:\n" + newPrivateKey + "\nVui lòng lưu trữ cẩn thận.";
                MailRegistrationService mailServices = new MailRegistrationService(email, newPublicKey, newPrivateKey, emailService);
                mailServices.send();
            }else {

                throw new Exception("Không thể gửi email vì không tìm thấy địa chỉ email của người dùng.");
            }
        }
//
    }
    // Phương thức thu hồi khóa cũ từ cơ sở dữ liệu
    private void revokeCurrentKey(int userId) throws Exception {
        try {
            // Cập nhật trạng thái khóa cũ trong DB (có thể chỉ cần set status, hoặc xóa)
            userDAO.revokeCurrentKey(String.valueOf(userId));
        } catch (Exception e) {
            // Xử lý lỗi khi không thể thu hồi khóa cũ
            System.err.println("Lỗi khi thu hồi khóa cũ của người dùng " + userId + ": " + e.getMessage());
            throw new Exception("Không thể thu hồi khóa cũ.", e);
        }
    }

// Phương thức thu hồi khóa cũ từ cơ sở dữ liệu
//    private void revokeCurrentKey(String userId) throws Exception{
//        try {
//            userDAO.revokeCurrentKey(userId);
//        }catch (Exception e){
//            // Xử lý lỗi nếu không thể thu hồi khóa cũ
//            System.err.println("Lỗi khi thu hồi khóa cũ của người dùng " + userId + ": " + e.getMessage());
//            throw new Exception("Không thể thu hồi khóa cũ.", e);
//        }
//    }
//
//    private void sendNewPrivateKey(String userID, String newPrivateKey) {
//        try {
//            // Thực hiện gửi khóa riêng mới cho khách hàng qua một kênh bảo mật
//            // Ví dụ có thể gửi qua email mã hóa hoặc một kênh bảo mật khác
//            System.out.println("Đang gửi khóa riêng mới cho người dùng: " + userID);
//            System.out.println("Khóa riêng: " + newPrivateKey);
//        } catch (Exception e) {
//            // Xử lý lỗi khi gửi khóa riêng
//            System.err.println("Lỗi khi gửi khóa riêng mới cho người dùng " + userID + ": " + e.getMessage());
//        }
//    }
//
//    private void saveNewPublicKey(String userID, String newPublicKey) throws Exception{
////        String email = userDAO.getUserEmail(userId); // Lấy email người dùng từ DB
////        if (email == null || email.isEmpty()) {
////            throw new Exception("Không tìm thấy email của người dùng.");
////        }
////
////        String subject = "Thông báo về Key của bạn";
////        String content = "Khóa riêng (Private Key) của bạn là:\n\n" + privateKey +
////                "\n\nVui lòng lưu trữ khóa này một cách an toàn.";
////
////        EmailUtil.sendEmail(email, subject, content);
//        try {
//            // Gọi DAO để lưu khóa công khai mới vào cơ sở dữ liệu
//            userDAO.savePublicKey(userID, newPublicKey);
//        } catch (Exception e) {
//            // Xử lý lỗi nếu không thể lưu khóa công khai mới
//            System.err.println("Lỗi khi lưu khóa công khai mới cho người dùng " + userID + ": " + e.getMessage());
//            throw new Exception("Không thể lưu khóa công khai mới.", e);
//        }
//    }
}
