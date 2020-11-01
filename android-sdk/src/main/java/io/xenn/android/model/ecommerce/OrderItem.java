package io.xenn.android.model.ecommerce;

public class OrderItem {
    private final String productId;
    private final String variantId;
    private final int quantity;
    private final Double price;
    private final Double discountedPrice;
    private final String currency;
    private final String supplierId;


    OrderItem(String productId, String variantId, int quantity, Double price, Double discountedPrice, String currency, String supplierId) {
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.currency = currency;
        this.supplierId = supplierId;
    }

    public String getProductId() {
        return productId;
    }

    public String getVariantId() {
        return variantId;
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
