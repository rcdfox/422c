public class Manager extends Employee {
    private double bonus;

    // Constructor initializing all fields, including those from the superclass
    public Manager(String name, double baseSalary, double bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return super.calculateSalary() + bonus; // Calculate total salary including bonus
    }
}