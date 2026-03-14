import exceptions.AppException;

public class PayPalPayment implements PaymentMethod{
    @Override
    public void pay(Order order) {
        if (order.getTotalAmount().getAmount() < 100) {
            throw new AppException("PayPal payment minimum is 100");
        }
    }
}
