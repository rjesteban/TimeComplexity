/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import code.Util;
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
    
    public Term copy() {
        Term m = new Term();
        m.coefficient = this.coefficient.copy();
        ArrayList<Variable> arr = copyvars(this.variable);
        m.variable = copyvars(this.variable);
        return m;
    }
    
    public Fraction getCoefficient() { return this.coefficient; }
    public void setCoefficient(Fraction coefficient) { this.coefficient = coefficient; }
    public ArrayList<Variable> getVariable() { return this.variable; }
    
    public Term (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = copyvars(variable);
    }
    
    public Term times(Term m) {
        Term t = this.copy();
        ArrayList<Variable> vars = copyvars(t.variable);
        vars.addAll(copyvars(m.variable));
        t.coefficient = this.coefficient.times(m.coefficient);      
        updateVars(vars);
        t.variable = copyvars(vars);
        return t;
    }
    
    public Term divide(Term m) {
        //it is a coefficient
        Term mm = m.copy();
        
        //negate all terms' exponents
        for (Variable v: mm.variable)
            v.setExponent(v.getExponent().times(new Fraction(-1)));
        Term t = this.copy();
        ArrayList<Variable> vars = copyvars(t.variable);
        vars.addAll(copyvars(mm.variable));
        t.coefficient = this.coefficient.divideBy(mm.coefficient);
        updateVars(vars);
        t.variable = copyvars(vars);
        return t;
    }
    
    
    @Override
    public String toString() {
        String variables = "";
        if (!this.variable.isEmpty())
            for (int i = 0; i < this.variable.size(); i++) {
                variables += this.variable.get(i).toString();
            }
        if (this.coefficient.equals(new Fraction(1))) {
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
        boolean b = this.variable.toString().equals(t.variable.toString());
        return b;
    }
   
    public static void updateVars(ArrayList<Variable> vars) {
        ArrayList<Variable> clonedVars = new ArrayList<Variable>();
        for (Variable V: vars)
            clonedVars.add(new Variable(V.getVariable(), V.getExponent().copy()));
        for (int i = 0; i < clonedVars.size(); i++) {
            for (int j = i  + 1; j < clonedVars.size(); j++) {
                if (clonedVars.get(i).getVariable().equals(clonedVars.get(j).getVariable())) {
                    clonedVars.get(i).setExponent(clonedVars.get(i).getExponent().plus(clonedVars.get(j).getExponent()));
                    if (clonedVars.remove(j) != null) 
                        j--;
                }    
            }
        }
        
        vars.clear();
        for (Variable V: clonedVars)
            vars.add(new Variable(V.getVariable(), V.getExponent().copy()));
    }
    
    public static ArrayList<Variable> copyvars(ArrayList<Variable> array) {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        for (Variable v: array)
           variables.add(v);
        return variables;
    }
    
}
