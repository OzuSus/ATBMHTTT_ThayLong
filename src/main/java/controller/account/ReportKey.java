package controller.account;

import dao.UserDAO;
import dao.UserDAOImplement;
import models.User;
import services.KeyServices;
import services.UserServices;
import utils.Encoding;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReportKey", value = "/report-key")
public class ReportKey extends HttpServlet {
    private KeyServices keyServices;
    @Override
    public void init() throws ServletException {
        // khởi tạo KeyServices với UserDAO
//
        this.keyServices = new KeyServices(new UserDAOImplement());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reportKey.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action  = req.getParameter("action");
        HttpSession session = req.getSession();
        User auth = (User) session.getAttribute("auth");
//        String userId =(String) req.getSession().getAttribute("userId");
        if (auth == null){
//            // Chuyển hướng đến trang đăng nhập nếu userId không tồn tại
//            req.setAttribute("error", "Bạn cần đăng nhập để thực hiện hành động này.");
//            req.getRequestDispatcher("account.jsp").forward(req, resp);
//            return;
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ban can dang nhap de thuc hien hanh dong nay");
            return;
        }
        try {
            if ("resetKey".equals(action)) {
                String password = req.getParameter("password");
                if (validatePassword(auth.getId(), password)) {
                    keyServices.revokeAndGenerateNewKey(auth.getId());
                    req.setAttribute("message", "Đã cấp lại khóa mới. Vui lòng kiểm tra email.");
                } else {
                    req.setAttribute("error", "Mật khẩu không chính xác!");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                return;
            }
        } catch (Exception e) {
            req.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        }
        req.getRequestDispatcher("reportKey.jsp").forward(req, resp);
//        try {
//            if ("resetKey".equals(action)){
////                Cap lai Key moi
//                keyServices.revokeAndGenerateNewKey(auth.getId());
//                req.setAttribute("message","Da cap lai khoa moi");
//            } else if ("forgetKey".equals(action)){
//                // kiem tra mat khau truoc khi cap lai key
//                String password = req.getParameter("password");
////                keyServices.sendCurrentPrivateKey(userId);
//                if (validatePassword(auth.getId(), password)){
//                    keyServices.revokeAndGenerateNewKey(auth.getId());
//                    req.setAttribute("message", "Da cap khoa thanh cong vui long kiem tra mail de kiem tra khoa rieng");
//                }else {
//                    req.setAttribute("error", "Mật khẩu không chính xác!");
//                }
//
//            }else {
//                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
//                return;
//            }
//        }catch (Exception e){
//            req.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
//            e.printStackTrace();
//        }
//        // Chuyển tiếp tới trang reportKey.jsp để hiển thị kết quả
//        req.getRequestDispatcher("reportKey.jsp").forward(req, resp);
    }
    private boolean validatePassword(int userId, String password) {
        try {
//            User user =(User) UserServices.getINSTANCE().getUserByID(userId);
            List<User> users = UserServices.getINSTANCE().getUserByID(userId);
            if (users == null || users.isEmpty()){
                return false; // khong tim thay user
            }
            // Lấy người dùng đầu tiên trong danh sách
            User user = users.get(0);
            String encodedPassword = Encoding.getINSTANCE().toSHA1(password);
            System.out.println("Mật khẩu nhập vào (SHA-1): " + encodedPassword);
            System.out.println("Mật khẩu trong DB: " + user.getPasswordEncoding());
//            return  encodedPassword.equals(user.getPasswordEncoding());
            boolean isMatch = encodedPassword.equals(user.getPasswordEncoding());

            if (isMatch) {
                System.out.println("Mật khẩu khớp!");
            } else {
                System.out.println("Mật khẩu không khớp!");
            }
//            if (!isMatch){
//                System.out.println("Mật khẩu khớp!");
//            }
            return isMatch;
        }catch (Exception e){
            e.printStackTrace();
            return false; // tra ve fale neu xay ra loi
        }
    }
}
