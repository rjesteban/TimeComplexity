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
    protected Fraction coefficient;
    protected ArrayList<Variable> variable;
    protected String TYPE;
    
    public Term (Fraction coefficient) {
        this.coefficient = coefficient;
        this.variable = new ArrayList<>();
        this.TYPE = "norm";
    }
    
    public Term() {
        this.coefficient = new Fraction(0);
        this.variable = new ArrayList<Variable>();
        this.TYPE = "norm";
    }
    
    public Term(Fraction coef, ArrayList<Variable> var) {
        this.coefficient = coef.copy();
        this.variable = Term.copyvars(var);
        this.TYPE = "norm";
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
    public void setVariable(ArrayList<Variable> vars) { 
        this.variable = Term.copyvars(vars);
    }
    
    public Term (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = copyvars(variable);
        this.TYPE = "norm";
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
            m = this.copy();
            Fraction f = this.coefficient.plus(t.getCoefficient());
            m.coefficient = f;
        }
        return m;
    }

    public boolean isLike(Term t) {
        boolean vars = this.variable.toString().equals(t.variable.toString());
        boolean types = this.TYPE.equals(t.TYPE);
        return vars && types;
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
        
        for (int i = 0; i < clonedVars.size(); i++) {
            if (clonedVars.get(i).getExponent().equals(new Fraction(0))){
                clonedVars.remove(i);
                i--;
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
    
    public static void main(String[] args) {
        ArrayList<Variable> vars = new ArrayList<>();
        vars.add(new Variable("x", new Fraction(4)));
        Term t1 = new Term(new Fraction(3), vars);
        Term t2 = new Term(new Fraction(1));
        
        Polynomial p = new Polynomial(t1);
        Polynomial p2 = new Polynomial(t2);
        
        p.add(p2);
        System.out.println("p: " + p);
        
    }
    
}
