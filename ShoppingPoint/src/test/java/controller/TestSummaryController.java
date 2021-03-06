package controller;

import com.shoppingpoint.controller.SummaryController;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSummaryController {
    // Lioi Alessandro
    @Test
    public void testCalculateIncrementalProfit() {
        SummaryController controller = new SummaryController();
        float output = controller.calculateIncrementalProfit(List.of(7f, 11f, 9f, 13f, 5f), List.of(20f, 12f, 7f, 6f, 3f, 2f));
        assertEquals(11.111112f, output);
    }
}
