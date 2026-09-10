public class Salesperson extends Employee {
    private double sales;
    private double commissionRate;

    // Constructor initializing all fields, including those from the superclass
    public Salesperson(String name, double baseSalary, double sales, double commissionRate) {
        super(name, baseSalary);
        this.sales = sales;
        this.commissionRate = commissionRate;
    }

    @Override
    public double calculateSalary() {
        return super.calculateSalary() + (sales * commissionRate); // Calculate total salary including commission
    }
}