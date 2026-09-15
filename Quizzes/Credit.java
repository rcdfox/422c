public class Credit extends Payments {
    private String cardNumber;
    private String cardHolder;

    public Credit(String cardNumber, String cardHolder) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public void pay(double amount) {
        System.out.println(
            "Paid $" + String.format("%.2f", amount)
            + " using Credit Card ending in "
            + cardNumber.substring(cardNumber.length() - 4)
        );
    }
}