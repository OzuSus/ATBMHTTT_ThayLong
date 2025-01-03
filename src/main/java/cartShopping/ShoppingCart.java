package cartShopping;

import models.*;
import utils.FormatCurrency;
import utils.ProductFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    private HashMap<Integer, List<AbstractCartProduct>> shoppingCartMap = new HashMap<>();
    private Voucher voucherApplied;
    private DeliveryMethod deliveryMethod;
    private PaymentMethod paymentMethod;
    private DeliveryInfo deliveryInfo;

    private String noteOrder;

    public ShoppingCart() {
    }

    public void setShoppingCartMap(HashMap<Integer, List<AbstractCartProduct>> shoppingCartMap) {
        this.shoppingCartMap = shoppingCartMap;
    }

    public HashMap<Integer, List<AbstractCartProduct>> getShoppingCartMap() {
        return shoppingCartMap;
    }


    public Voucher getVoucherApplied() {
        return voucherApplied;
    }

    public void setVoucherApplied(Voucher voucherApplied) {
        this.voucherApplied = voucherApplied;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getNoteOrder() {
        return noteOrder;
    }

    public void setNoteOrder(String noteOrder) {
        this.noteOrder = noteOrder;
    }

    // Dùng cho trang productDetail khi người dùng chọn số lượng và nhấn nút "thêm vào giỏ hàng"
    public void add(int productId, int quantity, Color color, Object size) {
        if (shoppingCartMap.containsKey(productId)) {
            List<AbstractCartProduct> listCartProducts = shoppingCartMap.get(productId);
            Product product = ProductFactory.getProductById(productId);
            AbstractCartProduct cartProduct;
            if (size instanceof Size) {
                cartProduct = new CartProduct(product, quantity, color, (Size) size);
            } else {
                cartProduct = new CartProductCustom(product, quantity, color, (String) size);
            }
            if (!listCartProducts.contains(cartProduct)) {
                listCartProducts.add(cartProduct);
            } else {
                int currentIndex = listCartProducts.indexOf(cartProduct);
                int currentQuantity = listCartProducts.get(currentIndex).getQuantity();
                cartProduct.setQuantity(currentQuantity + quantity);
                listCartProducts.set(currentIndex, cartProduct);
            }
        } else {
            List<AbstractCartProduct> listCartProducts = new ArrayList<>();
            Product product = ProductFactory.getProductById(productId);
            if (product == null) {
                return;
            } else {
                AbstractCartProduct cartProduct;
                if (size instanceof Size) {
                    cartProduct = new CartProduct(product, quantity, color, (Size) size);
                } else {
                    cartProduct = new CartProductCustom(product, quantity, color, (String) size);
                }

                listCartProducts.add(cartProduct);
                shoppingCartMap.put(productId, listCartProducts);
            }
        }
    }

    // Xử lý cập nhật số lượng của sản phẩm còn lại trong kho
    //  public void update(int productId, int quantity) {}

    // Dùng cho trang shoppingCart khi người dùng nhấn nút "+" để tăng số lượng
    public void increase(int productId, int cartProductIndex) {
        if (shoppingCartMap.containsKey(productId)) {
            List<AbstractCartProduct> listCartProducts = shoppingCartMap.get(productId);
            int currentQuantity = listCartProducts.get(cartProductIndex).getQuantity();
            AbstractCartProduct cartProduct = listCartProducts.get(cartProductIndex);
            cartProduct.setQuantity(currentQuantity + 1);
            System.out.println(cartProduct);
            listCartProducts.set(cartProductIndex, cartProduct);
        }
    }

    public void decrease(int productId, int cartProductIndex) {
        if (shoppingCartMap.containsKey(productId)) {
            List<AbstractCartProduct> listCartProducts = shoppingCartMap.get(productId);
            int currentQuantity = listCartProducts.get(cartProductIndex).getQuantity();
            AbstractCartProduct cartProduct = listCartProducts.get(cartProductIndex);
            int quantityDecreased = currentQuantity - 1;
            if (quantityDecreased > 0) {
                cartProduct.setQuantity(quantityDecreased);
                System.out.println(cartProduct);
                listCartProducts.set(cartProductIndex, cartProduct);
            }
        }
    }

    // Dùng cho trang shoppingCart khi người dùng nhấn nút "xóa" để xóa sản phẩm ra khỏi giỏ hàng
    public void remove(int productId, int cartProductIndex) {
        if (shoppingCartMap.containsKey(productId)) {
            List<AbstractCartProduct> listCartProducts = shoppingCartMap.get(productId);
            AbstractCartProduct cartProductTarget = listCartProducts.get(cartProductIndex);
            listCartProducts.remove(cartProductTarget);
            if (listCartProducts.isEmpty()) {
                shoppingCartMap.remove(productId);
            }
        }
    }

    public double getTemporaryPrice() {
        double temporaryPrice = 0;
        for (int productId : shoppingCartMap.keySet()) {
            for (AbstractCartProduct cartProduct : shoppingCartMap.get(productId)) {
                temporaryPrice += cartProduct.getSubtotal();
            }
        }
        return temporaryPrice;
    }

    public double getDiscountPrice() {
        if (voucherApplied != null) {
            return getTemporaryPrice() * voucherApplied.getDiscountPercent();
        }
        return 0;
    }

    public double getTotalPrice() {
        double totalPrice = getTemporaryPrice() - getDiscountPrice();
        if (deliveryMethod != null) {
            totalPrice += deliveryMethod.getShippingFee();
        }
        return totalPrice;
    }

    public String temporaryPriceFormat() {
        return FormatCurrency.vietNamCurrency(getTemporaryPrice());
    }

    public String discountPriceFormat() {
        return FormatCurrency.vietNamCurrency(getDiscountPrice());
    }

    public String totalPriceFormat() {
        return FormatCurrency.vietNamCurrency(getTotalPrice());
    }

    public int getTotalItems() {
        int totalItems = 0;
        for (int productId : shoppingCartMap.keySet()) {
            List<AbstractCartProduct> listCartProducts = shoppingCartMap.get(productId);
            totalItems += listCartProducts.size();
        }
        return totalItems;
    }

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
//        Color color = new Color();
//        color.setCodeColor("aaa");
//        color.setId(1);
//        color.setProductId(1);
//
//        Size size = new Size();
//        size.setNameSize("x");
//        size.setSizePrice(100000);
//        size.setId(1);
//        size.setProductId(1);
//
//        cart.add(1, 1, color, size);
//        cart.add(1, 1, color, size);
//
//        Product product1 = ProductFactory.getProductById(1);
//        Product product2 = ProductFactory.getProductById(1);

//        System.out.println(shoppingCartMap);
    }
}