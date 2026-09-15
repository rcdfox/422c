public class Venmo extends Payments {
    private String username;

    public Venmo(String username) {
        this.username = username;
    }

    @Override
    public void pay(double amount) {
        System.out.println(
            "Paid $" + String.format("%.2f", amount)
            + " using Venmo account @" + username
        );
    }
}