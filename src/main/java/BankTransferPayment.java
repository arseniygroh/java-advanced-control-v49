public class BankTransferPayment implements PaymentMethod{
    @Override
    public void pay(Order order) {
        double totalAmount = order.getTotalAmount().getAmount();
        Money totalAmountWithPercent = new Money( totalAmount * 1.01);
        order.setTotalAmount(totalAmountWithPercent);
    }
}
