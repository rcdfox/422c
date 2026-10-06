public class IntStack {
    private int[] data;
    private int top;

    public IntStack(int capacity) {
        data = new int[capacity];
        top = 0;
    }

    // Adds value to the top of the stack.
    // Assume the stack is not full.
    public void push(int value) {
        data[top] = value;
        top++;
    }

    // Removes and returns the top value.
    // Assume the stack is not empty.
    public int pop() {
        top--;
        return data[top];
    }

    // Returns the top value without removing it.
    // Assume the stack is not empty.
    public int peek() {
        return data[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    public static void main(String[] args) {
        IntStack s = new IntStack(5);

        System.out.println("Initially empty: " + s.isEmpty()); // true
        System.out.println("Initial size: " + s.size());       // 0

        s.push(10);
        s.push(20);
        s.push(30);

        System.out.println("Size after pushes: " + s.size());  // 3
        System.out.println("Top value: " + s.peek());          // 30

        System.out.println("Popped: " + s.pop());              // 30
        System.out.println("Top after pop: " + s.peek());      // 20
        System.out.println("Size after pop: " + s.size());     // 2

        s.pop();
        s.pop();

        System.out.println("Empty at end: " + s.isEmpty());    // true
        System.out.println("Final size: " + s.size());         // 0
    }
}