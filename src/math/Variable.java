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
    private final String var;
    private final Monomial exponent;
    
    public Variable(String variable, Monomial exponent) {
        this.var = variable;
        
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Monomial(new Fraction(0));
        } else {
            this.exponent = exponent;
        }
    }
    
    public Variable(String variable) {
        this.var = variable;
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Monomial(new Fraction(0));
        }
        else
            this.exponent = new Monomial(new Fraction(1));
    }
    
    public String getVariable() { return this.var; }
    public Monomial getExponent() { return this.exponent; }
    
    @Override
    public String toString() {
        if (this.exponent.getCoefficient().equals(new Fraction(0)) ||
                this.exponent.getCoefficient().equals(new Fraction(1))) {
            return this.var;
        }
        else
            return this.var + "^" + this.exponent;
    }
    
}
