package io.xenn.android.model.ecommerce;

public class OrderItem {
    private final String productId;
    private final String variant;
    private final int quantity;
    private final Double price;
    private final Double discountedPrice;
    private final String currency;
    private final String supplierId;


    OrderItem(String productId, String variant, int quantity, Double price, Double discountedPrice, String currency, String supplierId) {
        this.productId = productId;
        this.variant = variant;
        this.quantity = quantity;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.currency = currency;
        this.supplierId = supplierId;
    }

    public String getProductId() {
        return productId;
    }

    public String getVariant() {
        return variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getCurrency() {
        return currency;
    }
}
