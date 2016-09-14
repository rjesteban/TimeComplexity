/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.util.ArrayList;

/**
 *
 * @author rj
 */
public class Monomial implements Term {
    private final Fraction coefficient;
    private ArrayList<Variable> variable;
    private boolean isOperator;
    
    public Monomial (Fraction coefficient) {
        this.coefficient = coefficient;
        this.variable = new ArrayList<>();
        this.isOperator = false;
    }
    
    public Fraction getCoefficient() { return this.coefficient; }
    public ArrayList<Variable> getVariable() { return this.variable; }
    
    public Monomial (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = variable;
        if (variable.get(0).getVariable().equals("+") || variable.get(0).getVariable().equals("-") || 
            variable.get(0).getVariable().equals("/") || variable.get(0).getVariable().equals("*")) {
            this.isOperator = true;
        }
        else {
            this.isOperator = false;
        }
    }
    
    
    public boolean isOperator() {
        return this.isOperator;
    }
    
    @Override
    public String toString() {
        if (this.isOperator())
            return "" + this.getVariable().get(0).toString() + "";
        else {
            String variables = "";
            int len = this.variable.size();
            for (int i = 0; i < len; i++) {
                variables += "" + this.variable.get(i).toString() + "";
            }
            if (this.coefficient.equals(new Fraction(1))) {
                if (variables.equals(""))
                    return this.coefficient.toString();
                else
                    return variables;
            } else {
                return this.coefficient + variables;
            }
        }
    }
    
}
