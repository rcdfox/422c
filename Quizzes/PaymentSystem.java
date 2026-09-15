public class PaymentSystem {

    public static void processPayment(Payments paymentMethod, double amount) {
        paymentMethod.pay(amount);
    }

    public static void main(String[] args) {
        Credit card = new Credit("1234567890123456", "John Smith");

        Venmo venmo = new Venmo("johnsmith");

        processPayment(card, 49.99);
        processPayment(venmo, 25.50);
        processPayment(card, 120.00);
        processPayment(venmo, 8.75);
    }
}