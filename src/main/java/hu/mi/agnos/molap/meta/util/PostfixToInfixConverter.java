/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.util;

import hu.mi.agnos.molap.meta.exception.InvalidPostfixExpressionException;
import java.util.Stack;

/**
 *
 * @author parisek
 */
public class PostfixToInfixConverter {

      public static boolean isOperator(String text) {
        return text.equals("+")
                || text.equals("-")
                || text.equals("*")
                || text.equals("/")
                || text.equals("^")
                || text.equals("%");
    }

    public static boolean isLower(String c1, String c2) {
        if ((c1.equals("+") || c1.equals("-"))
                && (c2.equals("*") || c2.equals("/") || c2.equals("^") || c2.equals("%"))) {
            return true;
        }
        return false;
    }

    
    // Converting the given postfix expression to 
    // infix expression
    public static String convert(String postfix) throws InvalidPostfixExpressionException {
          // Create stack object
        Stack<String> s = new Stack<>();

        String previousOprator = "";
   
        for(String actualSegment : postfix.split(" ")){
            
            // Check whether given postfix location
            // at actualSegment is an operator or not
            if (isOperator(actualSegment)) {
                // When operator exist
                // Check that two operands exist or not
                if (s.size() > 1) {
                    String  op1 = s.pop();
                    String  op2 = s.pop();
                    if (isLower(previousOprator, actualSegment)) {
                        s.push( "( " + op2 + " ) " + actualSegment + " ( " + op1 + " )");
                    } else {
                        s.push( op2 + " " + actualSegment  + " " + op1);
                    }
                    previousOprator = actualSegment;
                    
                } else {
                    throw new InvalidPostfixExpressionException(postfix);
                }
            } else {
                // When get valid operands
                s.push(actualSegment);
            }
        }
        return s.pop();
    }
}

