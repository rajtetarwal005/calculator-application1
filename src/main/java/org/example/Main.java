package org.example;
import java.util.Stack;
import java.util.Scanner;

public class Main  {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a mathematical expression:");
        String input = scanner.nextLine();

        try {
            double result = evaluateExpression(input);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Invalid expression! Please check your input.");
        }

        scanner.close();
    }

    public static double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char ch = expression.charAt(i);

            // Skip spaces
            if (ch == ' ') {
                i++;
                continue;
            }

            // If it's a number, extract the full number
            if (Character.isDigit(ch)) {
                double num = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0'); // Convert char to int
                    i++;
                }
                numbers.push(num);
                continue; // Move to next character
            }

            // If it's an opening bracket, push it to operators stack
            if (ch == '(') {
                operators.push(ch);
            }
            // If it's a closing bracket, solve the entire bracket expression
            else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    compute(numbers, operators);
                }
                operators.pop(); // Remove '('
            }
            // If it's an operator (+, -, *, /), process based on precedence
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    compute(numbers, operators);
                }
                operators.push(ch);
            }

            i++; // Move to next character
        }

        // Process remaining operations
        while (!operators.isEmpty()) {
            compute(numbers, operators);
        }

        return numbers.pop(); // Final result
    }

    // Helper function to define operator precedence
    public static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0; // Default case
    }

    // Perform calculation on the top two numbers in stack
    public static void compute(Stack<Double> numbers, Stack<Character> operators) {
        double num2 = numbers.pop();
        double num1 = numbers.pop();
        char op = operators.pop();

        double result = 0;
        switch (op) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/':
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero!");
                }
                result = num1 / num2;
                break;
        }

        numbers.push(result); // Push result back to stack
    }
}

