import enums.OrderStatus;

import java.util.Arrays;

public class Order {
    private final String id;
    private OrderStatus status;
    private OrderItem[] items;
    private Money totalAmount;

    public Order(String id, OrderItem[] items) {
        this.id = id;
        this.status = OrderStatus.NEW;
        this.items = Arrays.copyOf(items, items.length);
        calculateTotal();
    }

    private void calculateTotal() {
        double sum = Arrays.stream(items)
                .mapToDouble(item -> item.getPrice().getAmount())
                .reduce(0.0, Double::sum);

        this.totalAmount = new Money(sum);
    }

    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Money getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Money totalAmount) { this.totalAmount = totalAmount; }
    public String getId() { return id; }
}
