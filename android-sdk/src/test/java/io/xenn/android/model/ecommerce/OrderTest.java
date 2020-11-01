package io.xenn.android.model.ecommerce;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void it_should_create_order_with_given_payment_method() {
        Order order = Order.create("orderId")
                .addItem("product1", "variant1", 3, 300d, 200d, "USD", "supplier1")
                .addItem("product2", "variant2", 1, 100d, 76d, "USD", "supplier2")
                .addItem("product3", "variant2", 4, 1300d, 1200d, "USD", "supplier3")
                .paidWith("creditCard")
                .totalAmount(3220d)
                .withPromotion("promotionName")
                .withDiscount("discountName")
                .withCoupon("couponName");

        assertEquals(3, order.getOrderItems().size());
        assertEquals("orderId", order.getOrderId());
        assertEquals("creditCard", order.getPaymentMethod());
        assertEquals(Double.valueOf(3220d), order.getTotalAmount());
        assertEquals("promotionName", order.getPromotionName());
        assertEquals("discountName", order.getDiscountName());
        assertEquals("couponName", order.getCouponName());

        OrderItem orderItem1 = order.getOrderItems().get(0);

        assertEquals("product1", orderItem1.getProductId());
        assertEquals("variant1", orderItem1.getVariantId());
        assertEquals(3, orderItem1.getQuantity());
        assertEquals(Double.valueOf(300d), orderItem1.getPrice());
        assertEquals(Double.valueOf(200d), orderItem1.getDiscountedPrice());
        assertEquals("USD", orderItem1.getCurrency());
        assertEquals("supplier1", orderItem1.getSupplierId());

        OrderItem orderItem2 = order.getOrderItems().get(0);

        assertEquals("product2", orderItem2.getProductId());
        assertEquals("variant2", orderItem2.getVariantId());
        assertEquals(1, orderItem2.getQuantity());
        assertEquals(Double.valueOf(100d), orderItem2.getPrice());
        assertEquals(Double.valueOf(76d), orderItem2.getDiscountedPrice());
        assertEquals("USD", orderItem2.getCurrency());
        assertEquals("supplier2", orderItem2.getSupplierId());

        OrderItem orderItem3 = order.getOrderItems().get(0);

        assertEquals("product3", orderItem3.getProductId());
        assertEquals("variant2", orderItem3.getVariantId());
        assertEquals(4, orderItem3.getQuantity());
        assertEquals(Double.valueOf(1300d), orderItem3.getPrice());
        assertEquals(Double.valueOf(1200d), orderItem3.getDiscountedPrice());
        assertEquals("USD", orderItem3.getCurrency());
        assertEquals("supplier3", orderItem3.getSupplierId());
    }
}
