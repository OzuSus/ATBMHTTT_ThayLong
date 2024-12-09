package controller.checkout;

import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PlaceOrderController", value = "/PlaceOrder")
public class PlaceOrderController extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

//        HttpSession session = request.getSession(true);
//        User userAuth = (User) session.getAttribute("auth");
//        String userIdCart = String.valueOf(userAuth.getId());
//        ShoppingCart cart = (ShoppingCart) session.getAttribute(userIdCart);
        HttpSession session = request.getSession(); // Không tạo mới session
        boolean validSign = (boolean) session.getAttribute("isValid");

        if (validSign) {
            int invoiceNo = Math.abs(UUID.randomUUID().hashCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invoiceNo", invoiceNo);
            response.setContentType("application/json");
            response.getWriter().print(jsonObject);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("message", "Chữ ký không hợp lệ.");
            response.getWriter().print(errorResponse);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}