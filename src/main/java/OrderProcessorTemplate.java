import enums.Category;
import enums.OrderStatus;
import exceptions.AppException;
import exceptions.ArchiveOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;

abstract public class OrderProcessorTemplate {
    private final Logger logger = LoggerFactory.getLogger(OrderProcessorTemplate.class);
    private final PaymentMethod paymentMethod;

    public OrderProcessorTemplate(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Optional<Order> findByID(String id, List<Order> orderList) {
        return orderList.stream()
                .filter(order -> order.getId().equalsIgnoreCase(id))
                .findFirst();

    }

    private void validate(Order order) {
        for (OrderItem item : order.getItems()) {
            if (item.getPrice().getAmount() > 50000) {
                throw new AppException("Item price exceeds limit of 50000");
            }
        }
    }

    private void applyDiscounts(Order order) {
        double total = 0;
        for (OrderItem item : order.getItems()) {
            double price = item.getPrice().getAmount();
            if (item.getCategory() == Category.CLEARANCE) {
                price *= 0.8;
            }
            total += price;
        }
        order.setTotalAmount(new Money(total));
    }

    private void ship(Order order) {
        order.setStatus(OrderStatus.SHIPPED);
    }

    private void deliver(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
    }


    public final void process(Order order) {
        try {
            logger.info("init order processing for ID: {}", order.getId());

            validate(order);
            applyDiscounts(order);

            paymentMethod.pay(order);
            order.setStatus(OrderStatus.PAID);
            logger.info("Order {} status changed to PAID", order.getId());

            ship(order);
            deliver(order);

            archiveAfterDelivery(order);
            logger.info("Order {} successfully processed and archived", order.getId());

        } catch (ArchiveOperationException e) {
            logger.error("failure occured during archiving for order: {}", order.getId(), e);
            throw e;
        } catch (AppException e) {
            logger.warn("some business logic failed for order {}: {}", order.getId(), e.getMessage());
            throw e;
        }
    }

    abstract void archiveAfterDelivery(Order order);
}

