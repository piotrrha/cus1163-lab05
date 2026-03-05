package academy.javapro.lab05;

import java.util.Scanner;

public class MathCalculatorLab {

    // Simple class to hold calculation data (PROVIDED - DO NOT MODIFY)
    static class CalculatorBase {
        int n;
        long result;

        public CalculatorBase(int n) {
            this.n = n;
            this.result = 0;
        }
    }

// TODO 1: Implement this method
public static Runnable fibonacciCalculator(CalculatorBase calc) {
    return () -> {
        String threadName = Thread.currentThread().getName();
        int n = calc.n;

        System.out.println(threadName + " computing: fib(" + n + ")");

        long fib;
        if (n == 0) {
            fib = 0;
        } else if (n == 1) {
            fib = 1;
        } else {
            long prev = 0;
            long curr = 1;

            for (int i = 2; i <= n; i++) {
                long next = prev + curr;
                prev = curr;
                curr = next;

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            fib = curr;
        }

        calc.result = fib;
        System.out.println(threadName + " completed: fibonacci(" + n + ") = " + calc.result);
    };
}

// TODO 2: Implement this method
public static Runnable sumOfSquaresCalculator(CalculatorBase calc) {
    return () -> {
        String threadName = Thread.currentThread().getName();
        int n = calc.n;

        System.out.println(threadName + " computing: 1² + 2² + 3² + ... + " + n + "²");

        long sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += (long) i * i;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        calc.result = sum;
        System.out.println(threadName + " completed: sumOfSquares(" + n + ") = " + calc.result);
    };
}
    // Example: sumOfSquares(5) = 1 + 4 + 9 + 16 + 25 = 55

    

    // Main method (PROVIDED - DO NOT MODIFY)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Threaded Math Calculator ===");
            System.out.println("1. Single calculation demo");
            System.out.println("2. Multiple concurrent calculations");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    runSingleDemo(scanner);
                    break;
                case 2:
                    runMultipleDemo(scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // Helper method for single calculation (PROVIDED - DO NOT MODIFY)
    private static void runSingleDemo(Scanner scanner) {
        System.out.println("\n=== Single Calculation Demo ===");
        System.out.println("1. Fibonacci");
        System.out.println("2. Sum of Squares");
        System.out.print("\nChoose calculator: ");

        int calcChoice = scanner.nextInt();
        System.out.print("Enter a number (1-20): ");
        int n = scanner.nextInt();

        if (n < 1 || n > 20) {
            System.out.println("Number must be between 1 and 20");
            return;
        }

        CalculatorBase calc = new CalculatorBase(n);
        Runnable task = null;
        String calcName = "";

        if (calcChoice == 1) {
            calcName = "Fibonacci";
            task = fibonacciCalculator(calc);
        } else if (calcChoice == 2) {
            calcName = "Sum of Squares";
            task = sumOfSquaresCalculator(calc);
        } else {
            System.out.println("Invalid calculator choice");
            return;
        }

        System.out.println("\nCreating " + calcName + " calculator thread...");
        long startTime = System.currentTimeMillis();

        Thread thread = new Thread(task);
        System.out.println(thread.getName() + " starting calculation for " + n);
        thread.start();

        try {
            System.out.println("\nMain thread waiting for calculation to complete...");
            thread.join();
            System.out.println(thread.getName() + " has finished execution");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nResult: " + calcName.toLowerCase() + "(" + n + ") = " + calc.result);
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Helper method for multiple calculations (PROVIDED - DO NOT MODIFY)
    private static void runMultipleDemo(Scanner scanner) {
        System.out.println("\n=== Multiple Concurrent Calculations ===");
        System.out.print("Enter a number (1-20): ");
        int n = scanner.nextInt();

        if (n < 1 || n > 20) {
            System.out.println("Number must be between 1 and 20");
            return;
        }

        System.out.println("\nCreating all calculator threads...");

        CalculatorBase fibCalc = new CalculatorBase(n);
        CalculatorBase sumCalc = new CalculatorBase(n);

        System.out.println("Creating thread for Fibonacci...");
        Runnable fibTask = fibonacciCalculator(fibCalc);
        Thread fibThread = new Thread(fibTask);

        System.out.println("Creating thread for Sum of Squares...");
        Runnable sumTask = sumOfSquaresCalculator(sumCalc);
        Thread sumThread = new Thread(sumTask);

        System.out.println("\nStarting both threads concurrently...\n");
        long startTime = System.currentTimeMillis();

        fibThread.start();
        sumThread.start();

        try {
            System.out.println("\nMain thread waiting for all calculations to complete...");
            fibThread.join();
            System.out.println(fibThread.getName() + " has finished execution");

            sumThread.join();
            System.out.println(sumThread.getName() + " has finished execution");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("\n=== Results ===");
        System.out.println("Fibonacci(" + n + ") = " + fibCalc.result);
        System.out.println("Sum of Squares(" + n + ") = " + sumCalc.result);
        System.out.println("\nTotal execution time: " + (endTime - startTime) + "ms");
        System.out.println("All calculations completed successfully!");
    }
}
