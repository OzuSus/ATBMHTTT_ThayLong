package controller.authentication;

import models.User;
import services.AuthenticateServices;
import services.EmailService;
import services.IMailServices;
import services.MailRegistrationService;
import utils.Validation;
import utils.RSAKeyUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@WebServlet(name = "signUp", value = "/signUp")
public class SignUp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        Validation validation = AuthenticateServices.getINSTANCE().checkSignUp(username, email, password, confirmPassword);

        Map<String, String> mapErrorPassword = AuthenticateServices.getINSTANCE().checkPasswordTemplate(password);


// Đăng nhập thành công khi: mapErrorPassword == null và validation.getObjReturn() != null


        if (validation.getObjReturn() != null && mapErrorPassword == null) {

//                tạo cặp khóa RSA
                /**
                 *  try {
                 * KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
                 * String publicKey = KeyGenetarorUtil.encodeKy(keyPair.getPublic());
                 * String privateKey = KeyGeneratorUtil.encodeKey(keyPair.getPrivateKey());
                 * //Lấy đối tượng User và gắn public Key
                 * User newUser = (User) vailidation.getObjReturn();
                 * newUser.setPublicKey(publicKey);
                 * // Lưu User vào DB
                 * AuthenticateServices.getINSTANCE().crateUser(newUser);
                 * // Gửi private Key qua mail
                 * EmailService emailService = new EmailService();
                 * emailService.sendEmail(email, " Your Private Key", "Ưelcome to our system! Your private Key is:\n\n" + privateKey);
                 * // Hiển thị thông báo thành công
                 * request.setAttribute("sendMail", "Send Mail Success");
                 *
                 *  } catch(Exception e ){
                 *      // xử lý lỗi tạo key or gửi mail
                 *      e.printStackTrace();
                 *       request.setAttribute("errorMessage", "Lỗi khi xử lý đăng ký tài khoản.");
                 *  }
                 *
                 * **/
                try {
                    KeyPair keyPair = RSAKeyUtil.generateKeyPair();
                    String publicKey = RSAKeyUtil.encodeKeyToBase64(keyPair.getPublic());
                    String privateKey = RSAKeyUtil.encodeKeyToBase64(keyPair.getPrivate());
//                    tạo đối tượng User mới và gắn public key
                    User newUser = (User) validation.getObjReturn();
                    newUser.setPublicKey(publicKey);
//                    Lưu user vào db
                    AuthenticateServices.getINSTANCE().createUser(newUser);
//                    Gửi Private Key qua mail
                    EmailService emailService = new EmailService();
                    MailRegistrationService mailServices = new MailRegistrationService(email, publicKey, privateKey, emailService);
                    mailServices.send();
                    request.setAttribute("sendMail", "Send Mail Success");
                } catch (Exception e) {
                   e.printStackTrace();
                    request.setAttribute("errorMessage", "Error during registration: " + e.getMessage());
                }

//            User newUser = (User) validation.getObjReturn();
//            AuthenticateServices.getINSTANCE().createUser(newUser);
//            request.setAttribute("sendMail", "Send Mail Success");
        } else {
            request.setAttribute("usernameError", validation.getFieldUsername());
            request.setAttribute("emailError", validation.getFieldEmail());
            request.setAttribute("passwordError", validation.getFieldPassword());
            request.setAttribute("passwordConfirmError", validation.getFieldConfirmPassword());

            // Mật khẩu thỏa nhưng tài khoản có trong csdl? -> mapErrorPassword != null
            if (mapErrorPassword != null) {
                request.setAttribute("charUpper", mapErrorPassword.get("char-upper"));
                request.setAttribute("charMinLength", mapErrorPassword.get("char-min-length"));
                request.setAttribute("charLower", mapErrorPassword.get("char-lower"));
                request.setAttribute("charNumber", mapErrorPassword.get("char-number"));
                request.setAttribute("charSpecial", mapErrorPassword.get("char-special"));
                request.setAttribute("noSpace", mapErrorPassword.get("no-space"));
            }
        }
        request.getRequestDispatcher("signUp.jsp").forward(request, response);
    }
}