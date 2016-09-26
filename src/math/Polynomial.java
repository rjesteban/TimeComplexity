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
        ArrayList<Term> _terms = copyterms(p.terms);
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void subtract(Polynomial p) {
        p.negate();
        ArrayList<Term> _terms = copyterms(p.terms);
        this.terms.addAll(_terms);
        this.simplify();
    }
    
    public void multiply(Polynomial p) { 
        ArrayList<Term> product = new ArrayList<Term>();
        
        for (int i = 0; i < this.terms.size(); i++) {
            for (int j = 0; j < p.terms.size(); j++) {
                if (this.terms.get(i).TYPE.equals("log")) {
                    product.add(this.terms.get(i).copy().times(p.terms.get(j).copy()));
                } else { // no support yet from mult sa term so balihon
                    product.add(p.terms.get(j).copy().times(this.terms.get(i).copy()));
                }
            }
        }
        
        this.terms = copyterms(product);
        this.simplify();
    }
    
    public void divide(Polynomial p) { 
        ArrayList<Term> quot = new ArrayList<Term>();
        
        for (int i = 0, len = this.terms.size(); i < len; i++) {
            for (int j = 0; j < p.terms.size(); j++) {
                quot.add(this.terms.get(i).copy().divide(p.terms.get(j).copy()));
            }
        }
        this.terms = copyterms(quot);
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
                    Term t = this.terms.get(i).copy().plus(this.terms.get(j)).copy();
                    this.terms.set(i, this.terms.get(i).plus(this.terms.get(j)).copy());
                    this.terms.remove(j);
                    if (this.terms.get(i).getCoefficient().equals(new Fraction(0)) && !this.terms.get(i).getVariable().isEmpty()) {
                        this.terms.remove(i);
                        i--;
                    }
                }
            }
        }
       
        if (this.terms.isEmpty()) {
            this.add(new Polynomial(new Term(new Fraction(0))));
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
        this.terms = copyterms(p.terms);
        this.evaluateable = true;
    }
      

    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String string, boolean removeConstants) {
        string = string.replaceAll("\\s", "");
        String[] sterms = string.split(String.format(Statement.WITH_DELIMITER, "\\+|\\-"));
        this.terms = new ArrayList<Term>();
        boolean prevIsAdd = false;
        boolean prevIsSubt = false;
        for (String s: sterms) {
            // if it is a term. evaluate and add it
            if (!(s.equals("+") || s.equals("-"))) {
                Term t = new Term();
                

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
                        if(!(s.charAt(0) >= '0' && s.charAt(0) <= '9'))
                            t.setCoefficient(new Fraction(1));
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
        simplify();
        
        if (removeConstants) {
            for (int i = 0; i < this.terms.size(); i++) {
                if(this.terms.get(i).variable.isEmpty()) {
                    this.terms.remove(i);
                    i--;
                }
            }
        }
    }

    public Polynomial copy() {
        Polynomial p = new Polynomial();
        p.terms = copyterms(this.terms);
        return p;
    }
    
    @Override
    public String toString() {
        String string = "";
        if (this.terms.isEmpty()) {
            return this.terms.get(0).toString();
        }
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
        Polynomial p = new Polynomial("3*n*n/n/n+3*n + 5", false);
        System.out.println("p: " + p);
        
        Polynomial p2 = new Polynomial("3*n-2*n", false);
        System.out.println("p: " + p2);
    }
    
    public static ArrayList<Term> copyterms(ArrayList<Term> array) {
        ArrayList<Term> terms = new ArrayList<Term>();
        for (Term v: array)
           terms.add(v);
        return terms;
    }
    
    public Polynomial substituteVar(String replacement) {
        Polynomial p = this.copy();
        for (Term t: p.terms) {
            for (Variable v: t.getVariable()) {
                v.setVariable(replacement);
            }
        }
        return p;
    }
    
}
