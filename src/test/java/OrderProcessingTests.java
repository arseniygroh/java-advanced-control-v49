import enums.Category;
import enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderProcessingTests {
    private static class TestOrderProcessor extends OrderProcessorTemplate {
        public TestOrderProcessor(PaymentMethod paymentMethod) {
            super(paymentMethod);
        }
        @Override
        protected void archiveAfterDelivery(Order order) {
            order.setStatus(OrderStatus.ARCHIVED);
        }
    }

    private OrderProcessorTemplate cardProcessor;
    private OrderProcessorTemplate payPalProcessor;
    private OrderProcessorTemplate bankProcessor;

    @BeforeEach
    void init() {
        cardProcessor = new TestOrderProcessor(new CardPayment());
        payPalProcessor = new TestOrderProcessor(new PayPalPayment());
        bankProcessor = new TestOrderProcessor(new BankTransferPayment());
    }


    @Test
    void ShouldCompleteAllStepsSuccessfully() {
        OrderItem[] items = { new OrderItem("sndsds", new Money(1500), Category.REGULAR) };
        Order order = new Order("1", items);

        cardProcessor.process(order);

        assertEquals(OrderStatus.ARCHIVED, order.getStatus());
    }

    @Test
    void ShouldApply20PercentDiscountForClearance() {
        OrderItem[] items = { new OrderItem("sdsdsd", new Money(1000), Category.CLEARANCE) };
        Order order = new Order("2", items);
        cardProcessor.process(order);

        assertEquals(800.0, order.getTotalAmount().getAmount());
    }


    @Test
    void process_ShouldAddBankTransferFee() {
        OrderItem[] items = { new OrderItem("skdjskdskjdsj", new Money(1000), Category.REGULAR) };
        Order order = new Order("3", items);
        bankProcessor.process(order);
        assertEquals(1010.0, order.getTotalAmount().getAmount());
    }

    @Test
    void findByIdShouldReturnOrder() {
        Order order = new Order("4", new OrderItem[0]);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        Optional<Order> result = cardProcessor.findByID("4", orders);
        assertTrue(result.isPresent());
        assertEquals("4", result.get().getId());
    }


}
