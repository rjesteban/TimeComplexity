/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

/**
 *
 * @author rj
 */
public class Variable {
    private String var;
    private Fraction exponent;
    
    public Variable(String variable, Fraction exponent) {
        this.var = variable;
        
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Fraction(0);
        } else {
            this.exponent = exponent;
        }
    }
    
    public Variable(String variable) {
        this.var = variable;
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Fraction(0);
        }
        else
            this.exponent = new Fraction(1);
    }
    
    public String getVariable() { return this.var; }
    public Fraction getExponent() { return this.exponent; }
    
    @Override
    public String toString() {
        if (this.exponent.equals(new Fraction(1))) {
            return this.var;
        }
        else
            return this.var + "^" + this.exponent;
    }
    
    public void setExponent(Fraction exponent) {
        this.exponent = exponent;
    }
}
