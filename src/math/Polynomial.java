package math;

import code.Statement;
import code.Util;
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
        ArrayList<Term> _terms = Util.copyterms(p.terms);
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void subtract(Polynomial p) {
        p.negate();
        ArrayList<Term> _terms = Util.copyterms(p.terms);
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void multiply(Polynomial p) { 
        ArrayList<Term> product = new ArrayList<Term>();
        
        for (int i = 0; i < this.terms.size(); i++) {
            for (int j = 0; j < p.terms.size(); j++) {
                Term t = this.terms.get(i).copy().times(p.terms.get(j).copy());
                product.add(t);
            }
        }
        
        this.terms = Util.copyterms(product);
        this.simplify();
    }
    
    public void divide(Polynomial p) { 
        ArrayList<Term> quot = new ArrayList<Term>();
        
        for (int i = 0, len = this.terms.size(); i < len; i++) {
            for (int j = 0; j < p.terms.size(); j++) {
                quot.add(this.terms.get(i).copy().divide(p.terms.get(j).copy()));
            }
        }
        this.terms = Util.copyterms(quot);
    }
    
    public void negate() {
        for (int i = 0, len = terms.size(); i < len; i++) {
            Fraction f = terms.get(i).getCoefficient().times(new Fraction(-1));
            terms.get(i).setCoefficient(f);
        }
    }
    
    
    public void simplify() {
        for (int i = 0; i < this.terms.size(); i++) {
            for (int j = i + 1; j < this.terms.size(); j++) {
                if (this.terms.get(i).isLike(this.terms.get(j))) {
                    this.terms.set(i, this.terms.get(i).plus(this.terms.get(j)).copy());
                    this.terms.remove(j);
                    if (this.terms.get(i).getCoefficient().equals(new Fraction(0)))
                        this.terms.remove(i);
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
        this.terms.add(new Term(new Fraction(0)));
    }
    
    public Polynomial(Polynomial p) {
        this.terms = Util.copyterms(p.terms);
        this.evaluateable = true;
    }
      

    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String string, boolean keepConstants) {
        string = string.replaceAll("\\s", "");
        String[] sterms = string.split(String.format(Statement.WITH_DELIMITER, "\\+|\\-"));
        this.terms = new ArrayList<Term>();
        boolean prevIsAdd = false;
        boolean prevIsSubt = false;
        for (String s: sterms) {
            // if it is a term. evaluate and add it
            if (!(s.equals("+") || s.equals("-"))) {
                Term t = new Term();
                t.setCoefficient(new Fraction(1));

                String[] comp = s.split(String.format(Statement.WITH_DELIMITER, "\\*|\\/"));
                boolean prevIsDiv = false;
                boolean prevIsMult = false;

                for (String comp1 : comp) {
                    if (isNumber(comp1)) {
                        if (prevIsDiv == false && prevIsMult == false) {
                            t.setCoefficient(new Fraction(Integer.valueOf(comp1)));
                        } else {
                            if (prevIsMult) {
                                t.setCoefficient(t.getCoefficient().times(new Fraction(Integer.valueOf(comp1))));
                                prevIsMult = false;
                            } else if (prevIsDiv) {
                                t.setCoefficient(t.getCoefficient().divideBy(new Fraction(Integer.valueOf(comp1))));
                                prevIsDiv = false;
                            }
                        }
                    } else if (comp1.charAt(0) == '/') {
                        prevIsDiv = true;
                    } else if (comp1.charAt(0) == '*') {
                        prevIsMult = true;
                    } else if (comp1.charAt(0) >= 'a' && comp1.charAt(0) <= 'z') {
                        if (prevIsDiv) {
                            t.getVariable().add(new Variable(comp1, new Fraction(-1)));
                            prevIsDiv = false;
                        } else if (prevIsMult) {
                            t.getVariable().add(new Variable(comp1, new Fraction(1)));
                            prevIsMult = false;
                        } else{
                            t.getVariable().add(new Variable(comp1, new Fraction(1)));
                        }
                    }
                }
                
                Term.updateVars(t.getVariable());
               
                if (prevIsAdd) {
                    this.terms.add(t);
                } else if (prevIsSubt) {
                    t.setCoefficient(t.getCoefficient().times(new Fraction(-1)));
                    this.terms.add(t);
                } else {
                    this.terms.add(t);
                }
            } 
            
            
            
            
            else if (s.equals("+")) {
                prevIsAdd = true;
            } else if (s.equals("-")) {
                prevIsSubt = true;

            }
        }
    }

    public Polynomial copy() {
        Polynomial p = new Polynomial();
        p.terms = Util.copyterms(this.terms);
        return p;
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
    
    public boolean isNumber(String s) {
        try {
            int i = Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        Polynomial n = new Polynomial("7*n*n", false);
        Polynomial m = new Polynomial("n*n*n*n", false);
        n.divide(m);
        System.out.println("P: " + n);
        
        
    }
}
