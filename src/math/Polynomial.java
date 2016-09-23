package math;

import code.Statement;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rj
 */
public class Polynomial {
    private ArrayList<Term> terms;
    private boolean evaluateable;
    
    
    public void add(Polynomial p) {
        ArrayList<Term> _terms = (ArrayList<Term>)p.terms.clone();
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void sub(Polynomial p) {
        p.negate();
        ArrayList<Term> _terms = (ArrayList<Term>)p.terms.clone();
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void mult(Polynomial p) { 
        ArrayList<Term> product = new ArrayList<Term>();
        for (int i = 0, len = this.terms.size(); i < len; i++) {
            for (int j = 0; j < p.terms.size(); j++) {
                Monomial l = (Monomial)this.terms.get(i);
                Monomial m = (Monomial)p.terms.get(j);
                System.out.println( "product =" + l + "*" + m);
                l.times(m);
                product.add((Term)l);
            }
        }
        this.terms = (ArrayList<Term>)product.clone();
    }
    
    public void divide(Term t) { 
        for (int i = 0, len = this.terms.size(); i < len; i++) {
            if (this.terms.get(i) instanceof Monomial && t instanceof Monomial) {
                Monomial m = (Monomial)terms.get(i);
                m.divide(t);
            }
        }
    }
    
    public void negate() {
        for (int i = 0, len = terms.size(); i < len; i++) {
            if (terms.get(i) instanceof Monomial) {
                Monomial m = (Monomial)terms.get(i);
                Fraction f = m.getCoefficient().times(new Fraction(-1));
                m.setCoefficient(f);
            }
        }
    }
    
    
    public void simplify() {
        for (int i = 0; i < this.terms.size(); i++) {
            for (int j = i + 1; j < this.terms.size(); j++) {
                if (this.terms.get(i).isLike(this.terms.get(j))){
                    this.terms.get(i).plus(this.terms.get(j));
                    this.terms.remove(j);
                }
            }
        }
    }
    
    public Polynomial (Term m) {
        this.terms = new ArrayList<Term>();
        this.terms.add(m);
        this.evaluateable = true;
    }
    
    public Polynomial () {
        this.terms = new ArrayList<Term>();
        this.terms.add(new Monomial(new Fraction(0)));
    }
    
    public Polynomial(Polynomial p) {
        this.terms = (ArrayList<Term>)p.terms.clone();
        this.evaluateable = true;
    }
      

    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String s, boolean keepConstants) {
        this.terms = new ArrayList<>();
        
        String[] sterms = s.trim().split(String.format(Statement.WITH_DELIMITER, "\\+|\\-"));
        
        
        for (int i = 0, len = sterms.length; i < len; i++) {
            try { // if it is a number
                int n = Integer.valueOf(sterms[i]);
                if (i == 0)
                    this.terms.add(new Monomial(new Fraction(n)));
                if ((i-1 != -1) && (i - 1 != 0)) {
                    if (sterms[i-1].equals("-"))
                        this.terms.add(new Monomial(new Fraction(-1*n)));
                    else
                        this.terms.add(new Monomial(new Fraction(n)));
                }
                
            } catch (NumberFormatException nfe) {
                // assume it is only a variable, or a + or - operator
                if (!(sterms[i].equals("+") || sterms[i].equals("-"))) {
                    ArrayList<Variable> var = new ArrayList<Variable>();
                    if (i==0) {
                        var.add(new Variable(sterms[i], new Monomial(new Fraction(1))));
                        this.terms.add(new Monomial(var));
                    }
                    else if (i - 1 != -1 && i != 0) {
                        if (sterms[i-1].equals("-"))
                            var.add(new Variable(sterms[i], new Monomial(new Fraction(-1))));
                        else
                            var.add(new Variable(sterms[i], new Monomial(new Fraction(1))));
                        this.terms.add(new Monomial(var));
                    }
                }
            }
        }
    }
    

    
    @Override
    public String toString() {
        String string = "";
        
        for (int i = 0, len = this.terms.size(); i < len; i++) {
            string += this.terms.get(i).toString() + "";
            if (i < len - 1)
                string += " + ";
            
        }
        return string;
    }
    
    public ArrayList<Term> getTerms() { return this.terms; }
    
}
