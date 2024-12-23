package controller.checkout;

import dao.UserDAO;
import dao.UserDAOImplement;
import models.PaymentMethod;
import models.DeliveryMethod;
import models.shoppingCart.ShoppingCart;
import models.User;
import org.json.JSONObject;
import services.CheckoutServices;
import utils.HashUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "CheckoutController", value = "/Checkout")
public class CheckoutController extends HttpServlet{

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if(action != null){
            switch (action) {

                case "choiceDeliveryMethod" -> {
                    String deliveryMethodId = request.getParameter("deliveryMethodId");
                    request.setAttribute("deliveryMethodId", deliveryMethodId);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("ChoiceDeliveryMethod");
                    requestDispatcher.forward(request, response);
                }
                case "choicePaymentMethod" -> {
                    String paymentMethodId = request.getParameter("paymentMethodId");
                    request.setAttribute("paymentMethodId", paymentMethodId);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("ChoicePaymentMethod");
                    requestDispatcher.forward(request, response);
                }
            }
            if ("verifySignature".equals(action)) {
                HttpSession session = request.getSession();
                String electronicSignature = request.getParameter("eSign");
                boolean isValid = false;

                try {
                    isValid = verifyDigitalSignature(electronicSignature, session);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("isValid", isValid);
                session.setAttribute("isValid", isValid);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(jsonResponse.toString());

            }


            if(action.equals("addDeliveryInfo") || action.equals("editDeliveryInfo")){
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("address", address);

                if(action.equals("addDeliveryInfo")){
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("AddDeliveryInfo");
                    requestDispatcher.forward(request, response);
                }

                if(action.equals("editDeliveryInfo")){
                    String deliveryInfoKey = request.getParameter("deliveryInfoKey");
                    request.setAttribute("deliveryInfoKey", deliveryInfoKey);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("EditDeliveryInfo");
                    requestDispatcher.forward(request, response);
                }
            }
        }else{
            String typeEdit = request.getParameter("typeEdit");
            if(typeEdit != null) {
                String deliveryInfoKey = request.getParameter("deliveryInfoKey");
                request.setAttribute("deliveryInfoKey", deliveryInfoKey);
                switch (typeEdit) {
                    case "removeDeliveryInfo" -> {
                        String statusChoice = request.getParameter("statusChoice");
                        request.setAttribute("statusChoice", statusChoice);
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("RemoveDeliveryInfo");
                        requestDispatcher.forward(request, response);
                    }
                    case "choiceDeliveryInfo" -> {
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("ChoiceDeliveryInfo");
                        requestDispatcher.forward(request, response);
                    }
                }
            }
        }

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DeliveryMethod> listDeliveryMethod = CheckoutServices.getINSTANCE().getAllInformationDeliveryMethod();
        List<PaymentMethod> listPaymentMethod = CheckoutServices.getINSTANCE().getAllPaymentMethod();
        HttpSession session = request.getSession();

        User userAuth = (User) session.getAttribute("auth");
        String userIdCart = String.valueOf(userAuth.getId());
        ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);

        if(cart.getTotalPrice(false) < 5000000){
            if(cart.getDeliveryMethod() == null){
                DeliveryMethod deliveryMethodDefault = CheckoutServices.getINSTANCE().getDeliveryMethodById(1);
                cart.setDeliveryMethod(deliveryMethodDefault);
                session.setAttribute(userIdCart, cart);
            }
        }else {
            cart.setDeliveryMethod(null);
            session.setAttribute(userIdCart, cart);
        }

        if(cart.getPaymentMethod() == null){
            PaymentMethod paymentMethodDefault = CheckoutServices.getINSTANCE().getPaymentMethodById(1);
            cart.setPaymentMethod(paymentMethodDefault);
            session.setAttribute(userIdCart, cart);
        }
        // Tạo thông tin giỏ hàng để băm (dữ liệu bạn muốn bảo vệ)
        String cartInfo = "ID: " + cart.getTotalItems() + "\n"
                + "Tên khách hàng: " + userAuth.getFullName() + "\n"
                + "Tổng tiền: " + cart.getTotalPrice(true) + " VND";
        // Băm thông tin giỏ hàng
        String cartInfoHashed = HashUtils.hashString(cartInfo);  // Hàm băm từ HashUtils

        // Lấy mã băm cũ từ session (hoặc cơ sở dữ liệu)
        String existingCartInfoHash = (String) session.getAttribute("cartInfoHash");

        // Nếu mã băm cũ không tồn tại, đây là lần đầu tiên hoặc không có thông tin
        if (existingCartInfoHash == null) {
            // Lưu mã băm mới vào session
            session.setAttribute("cartInfoHash", cartInfoHashed);
            request.setAttribute("message", "Giỏ hàng của bạn đã được cập nhật lần đầu tiên.");
        } else {
            // Kiểm tra nếu có sự thay đổi trong giỏ hàng
            if (!cartInfoHashed.equals(existingCartInfoHash)) {
                // Nếu hash giỏ hàng đã thay đổi, thông báo cho người dùng
                session.setAttribute("cartInfoHash", cartInfoHashed);  // Cập nhật hash mới vào session
                request.setAttribute("message", "Giỏ hàng của bạn đã được cập nhật!");
            } else {
                request.setAttribute("message", "Giỏ hàng của bạn không thay đổi.");
            }
        }
        request.setAttribute("cartInfoHashed", cartInfoHashed);
        // Chuyển tiếp thông báo đến trang checkout.jsp hoặc trang khác

        request.setAttribute("listDeliveryMethod",listDeliveryMethod);
        request.setAttribute("listPaymentMethod", listPaymentMethod);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("checkout.jsp");
        requestDispatcher.forward(request, response);
    }

    public boolean verifySignature(String publicKeyString, String data, String signatureText) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);

        signature.update(data.getBytes());

        byte[] digitalSignature = Base64.getDecoder().decode(signatureText);

        return signature.verify(digitalSignature);
    }

    public boolean verifyDigitalSignature(String signature, HttpSession session) throws Exception {
        User userAuth = (User) session.getAttribute("auth");
        UserDAO userDAO = new UserDAOImplement();
        User user = userDAO.selectPublicKeyById(userAuth.getId());

        String cartInfoHash = (String) session.getAttribute("cartInfoHash");
        if (cartInfoHash == null) {
            throw new IllegalStateException("Cart hash not found in session.");
        }

        return verifySignature(user.getPublicKey(), cartInfoHash, signature);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
