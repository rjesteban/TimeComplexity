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
public class Term {
    private Fraction coefficient;
    private ArrayList<Variable> variable;
    
    public Term (Fraction coefficient) {
        this.coefficient = coefficient;
        this.variable = new ArrayList<>();
    }
    
    public Term() {
        this.coefficient = new Fraction(0);
        this.variable = new ArrayList<Variable>();
    }
    
    public Term clone() {
        Term m = new Term();
        m.coefficient = this.coefficient.clone();
        m.variable = (ArrayList<Variable>)this.variable.clone();
        return m;
    }
    
    public Fraction getCoefficient() { return this.coefficient; }
    public void setCoefficient(Fraction coefficient) { this.coefficient = coefficient; }
    public ArrayList<Variable> getVariable() { return this.variable; }
    
    public Term (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = (ArrayList<Variable>)variable.clone();
    }
    
    public Term times(Term m) {
        Term t = this.clone();
        ArrayList<Variable> vars = (ArrayList<Variable>)t.variable.clone();
        vars.addAll((ArrayList<Variable>)m.variable.clone());
        
        t.coefficient = this.coefficient.times(m.coefficient);
        
        
        // check similar variables and add their exponents, remove the other one
        for (int i = 0; i < vars.size(); i++) {
            for (int j = i + 1; j < vars.size(); j++) {
                // same var, add exponent. remove the other
                if (vars.get(i).getVariable().equals(vars.get(j).getVariable())) {   
                    Fraction f = vars.get(i).getExponent().plus(vars.get(j).getExponent());
                    vars.get(i).setExponent(f);
                    vars.remove(j);
                }
            }
        }
        t.variable = (ArrayList<Variable>)vars.clone();
        return t;
    }
    
    public Term divide(Term m) {
        //it is a coefficient
        Term t = this.clone();
        t.coefficient = this.coefficient.divideBy(m.coefficient);
        System.out.println("this neeeedss upppddaaaatteeeeee " + t.coefficient);
        return t;
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
    
    public Term plus(Term t) {
        Term m = new Term();
        if (this.isLike(t)) {
            Fraction f = this.coefficient.plus(t.getCoefficient());
            m.coefficient = f;
        }
        return m;
    }

    public boolean isLike(Term t) {
        boolean b = this.variable.equals(t.variable);
        return b;
    }
   
    
}
