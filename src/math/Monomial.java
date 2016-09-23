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
    private Fraction coefficient;
    private ArrayList<Variable> variable;
    
    public Monomial (Fraction coefficient) {
        this.coefficient = coefficient;
        this.variable = new ArrayList<>();
    }
    
    public Fraction getCoefficient() { return this.coefficient; }
    public void setCoefficient(Fraction coefficient) { this.coefficient = coefficient; }
    public ArrayList<Variable> getVariable() { return this.variable; }
    
    public Monomial (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = (ArrayList<Variable>)variable.clone();
    }
    
    @Override
    public void times(Term t) {
        //it is a coefficient
        if (t instanceof Monomial) {
            Monomial m = (Monomial)t;
            this.coefficient = coefficient.times(m.coefficient);
            for (int i = 0; i < this.variable.size(); i++) {
                Variable v = this.variable.get(i);
                for (int j = 0; j < m.variable.size(); j++) {
                    if (m.variable.get(j).getVariable().equals(v.getVariable())) {
                        Fraction f = m.variable.get(j).getExponent()
                                //DO SOMETHING HEEEEREEE T.T! huhuhu
                    }
                }
            }
        }
    }
    
    private void update() {
        // do this to update the variables and the value of the coefficient,
        // that is, remove variables with 0 coefficient
    }
    
    @Override
    public void divide(Term t) {
        //it is a coefficient
        if (t instanceof Monomial) {
            Monomial m = (Monomial)t;
            if (m.variable.isEmpty())         
            this.coefficient = coefficient.divideBy(m.coefficient);
        }
    }
    
    
    @Override
    public String toString() {
        String variables = "";
        if (!this.variable.isEmpty())
            for (int i = 0; i < this.variable.size(); i++) {
                variables += this.variable.get(i).toString();
            }
        if (this.coefficient.equals(new Fraction(1)) || this.coefficient.equals(new Fraction(-1))) {
            if (variables.equals(""))
                return this.coefficient.toString();
            else {
                return variables;
            }
        } else {
            return this.coefficient + "" + variables;
        }
        
    }

    @Override
    public void plus(Term t) {
        Monomial m = (Monomial)t;
        if (this.isLike(m)) {
            Fraction f = this.coefficient.plus(m.getCoefficient());
            this.coefficient = f;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void minus(Term m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLike(Term t) {
        Monomial m = (Monomial)t;
        return this.variable.equals(m.variable);
    }
   
    
}
