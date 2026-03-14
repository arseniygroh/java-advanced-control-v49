import enums.Category;

public class OrderItem {
    private final String name;
    private final Money price;
    private final Category category;

    public OrderItem(String name, Money price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Money getPrice() { return price; }
    public Category getCategory() { return category; }
}
