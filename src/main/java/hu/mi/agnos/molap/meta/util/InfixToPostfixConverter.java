/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.util;

import java.util.Stack;

/**
 *
 * @author parisek
 */
public class InfixToPostfixConverter {

    public static String convert(String infixString) {
        StringBuilder result = new StringBuilder();

        Stack<Character> stackForOperators = new Stack<>();

        for (int i = 0; i < infixString.length(); i++) {
            char c = infixString.charAt(i);
            //the spaces are removed from the expression
            if (c != ' ') {
                //if operators
                if (isOperator(c)) {
                    result.append(' ');
                    while (!stackForOperators.empty() && isBiger(stackForOperators.peek(), c)) {
                        result.append(stackForOperators.pop());
                        result.append(' ');
                    }

                    stackForOperators.push(c);
                } else if (c == '(') {
                    stackForOperators.push(c);
                } else if (c == ')') {
                    while (!stackForOperators.peek().equals('(')) {
                        result.append(' ');
                        result.append(stackForOperators.pop());
                    }
                    stackForOperators.pop();
                } else {
                    result.append(c);
                }
            }
        }
        result.append(' ');
        while (!stackForOperators.empty()) {
            result.append(stackForOperators.pop());
        }
        return result.toString();
    }

    public static boolean isOperator(char c) {
        return c == '+'
                || c == '-'
                || c == '*'
                || c == '/'
                || c == '^'
                || c == '%';
    }

    public static boolean isBiger(char c2, char c1) {
        if ((c1 == '+' || c1 == '-') && (c2 == '*' || c2 == '/' || c2 == '^' || c2 == '%')) {
            return true;
        }
        return false;
    }

}
