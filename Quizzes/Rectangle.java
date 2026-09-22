class Rectangle extends Shape{

    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color); // Calls the constructor of the Shape class
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public void print() {
        System.out.println("Rectangle color: " + color + ", Area: " + area());
    }
}