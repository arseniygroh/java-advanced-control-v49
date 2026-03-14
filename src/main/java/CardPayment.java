import exceptions.AppException;

public class CardPayment implements PaymentMethod{
    @Override
    public void pay(Order order) {
        if (order.getTotalAmount().getAmount() > 20000) {
            throw new AppException("Card payment limit exceeded (max 20000)");
        }
    }
}
