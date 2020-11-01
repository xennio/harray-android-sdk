package io.xenn.android.event;

import java.util.HashMap;
import java.util.Map;

import io.xenn.android.model.ecommerce.Order;
import io.xenn.android.model.ecommerce.OrderItem;

public class EcommerceEventProcessorHandler {

    private final EventProcessorHandler eventProcessorHandler;

    public EcommerceEventProcessorHandler(EventProcessorHandler eventProcessorHandler) {
        this.eventProcessorHandler = eventProcessorHandler;
    }

    public void productView(String productId, String variantId, double price, double discountedPrice, String currency, String supplierId, String path) {
        Map<String, Object> params = new HashMap<>();
        params.put("entity", "products");
        params.put("id", productId);
        params.put("variant", variantId);
        params.put("price", price);
        params.put("discountedPrice", discountedPrice);
        params.put("currency", currency);
        params.put("supplierId", supplierId);
        params.put("path", path);
        eventProcessorHandler.pageView("productDetail", params);
    }

    public void categoryView(String categoryId, String path) {
        Map<String, Object> params = new HashMap<>();
        params.put("entity", "categories");
        params.put("id", categoryId);
        params.put("path", path);
        eventProcessorHandler.pageView("categoryPage", params);
    }

    public void searchResult(String keyword, int resultCount, String path) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("resultCount", resultCount);
        params.put("path", path);
        eventProcessorHandler.pageView("searchPage", params);
    }

    public void addToCart(String productId, String variantId, int quantity, double price, double discountedPrice, String currency, String origin, String basketId, String supplierId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("variantId", variantId);
        params.put("quantity", quantity);
        params.put("price", price);
        params.put("discountedPrice", discountedPrice);
        params.put("currency", currency);
        params.put("origin", origin);
        params.put("basketId", basketId);
        params.put("supplierId", supplierId);
        eventProcessorHandler.actionResult("addToCart", params);
    }

    public void removeFromCart(String productId, String variantId, int quantity, String basketId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("variantId", variantId);
        params.put("quantity", quantity);
        params.put("basketId", basketId);
        eventProcessorHandler.actionResult("removeFromCart", params);
    }

    public void cartView(String basketId) {
        Map<String, Object> params = new HashMap<>();
        params.put("basketId", basketId);
        eventProcessorHandler.pageView("cartView", params);
    }

    public void orderFunnel(String basketId, String step) {
        Map<String, Object> params = new HashMap<>();
        params.put("basketId", basketId);
        params.put("step", step);
        eventProcessorHandler.pageView("orderFunnel", params);
    }

    public void orderSuccess(String basketId, Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("basketId", basketId);
        params.put("orderId", order.getOrderId());
        params.put("totalAmount", order.getTotalAmount());
        params.put("discountAmount", order.getDiscountAmount());
        params.put("discountName", order.getDiscountName());
        params.put("couponName", order.getCouponName());
        params.put("promotionName", order.getPromotionName());
        params.put("paymentMethod", order.getPaymentMethod());
        eventProcessorHandler.pageView("orderSuccess", params);

        for (OrderItem orderItem :
                order.getOrderItems()) {
            Map<String, Object> orderItemParams = new HashMap<>();
            orderItemParams.put("orderId", order.getOrderId());
            orderItemParams.put("productId", orderItem.getProductId());
            orderItemParams.put("variantId", orderItem.getVariantId());
            orderItemParams.put("quantity", orderItem.getQuantity());
            orderItemParams.put("price", orderItem.getPrice());
            orderItemParams.put("discountedPrice", orderItem.getDiscountedPrice());
            orderItemParams.put("currency", orderItem.getCurrency());
            orderItemParams.put("supplierId", orderItem.getSupplierId());
            eventProcessorHandler.actionResult("conversion", params);
        }
    }
}
