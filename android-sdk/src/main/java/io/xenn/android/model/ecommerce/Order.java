package io.xenn.android.model.ecommerce;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> orderItems = new ArrayList<>();
    private final String orderId;
    private String promotionName;
    private Double totalAmount;
    private Double discountAmount;
    private String paymentMethod;
    private String discountName;
    private String couponName;

    private Order(String orderId) {
        this.orderId = orderId;
    }

    public static Order create(String orderId) {
        return new Order(orderId);
    }

    public Order addItem(String productId, String variantId, int quantity, Double price, Double discountedPrice, String currency, String supplierId) {
        OrderItem orderItem = new OrderItem(productId, variantId, quantity, price, discountedPrice, currency, supplierId);
        this.orderItems.add(orderItem);
        return this;
    }

    public Order paidWith(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public Order withPromotion(String promotionName) {
        this.promotionName = promotionName;
        return this;
    }

    public Order withDiscount(String discountName) {
        this.discountName = discountName;
        return this;
    }

    public Order withCoupon(String couponName) {
        this.couponName = couponName;
        return this;
    }

    public Order totalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public Order discountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDiscountName() {
        return discountName;
    }

    public String getCouponName() {
        return couponName;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }
}
