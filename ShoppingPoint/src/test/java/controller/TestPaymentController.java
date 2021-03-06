package controller;

import com.shoppingpoint.controller.PaymentController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPaymentController {
    // Ronzello Gianluca
    @Test
    public void testDiscountPercentage() {
        PaymentController controller = new PaymentController();
        float output = controller.calculateDiscountPercentage(100, 80);
        assertEquals(20.0, output);
    }

    // Ronzello Gianluca
    @Test
    public void testPointsUsed() {
        PaymentController controller = new PaymentController();
        int output = controller.calculatePointsUsed(10, 30, 2);
        assertEquals(output, 20);
    }
}
