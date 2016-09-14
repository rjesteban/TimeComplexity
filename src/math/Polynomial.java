package math;

import code.Statement;
import java.util.ArrayList;
import java.util.Stack;

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
    private final ArrayList<Term> terms;
    
    public void add(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("+");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
    }
    
    public void subtract(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("-");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
    }
    
    public void multiply(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("*");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
    }
    
    public void divide(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("/");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
    }
    
    public Polynomial (Term m) {
        this.terms = new ArrayList<>();
        this.terms.add(m);
    }
    
    public Polynomial () {
        this.terms = new ArrayList<>();
    }
    
    
    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String s) {
        this.terms = new ArrayList<>();
//        this.terms.add(new Monomial(new Fraction(0)));
        ArrayList<Term> infixTerms = new ArrayList<>();
        
        String[] sterms = s.split(String.format(Statement.WITH_DELIMITER, "\\+|\\-"));
        
        
        for (int i = 0; i < sterms.length; i++) {
            try { // if it is a number
                infixTerms.add(new Monomial(new Fraction(Integer.valueOf(sterms[i]))));
            } catch (NumberFormatException nfe) {
                ArrayList<Variable> shizz = new ArrayList<Variable>();
                    shizz.add(new Variable(sterms[i], new Monomial(new Fraction(1))));
                    infixTerms.add(new Monomial(shizz));
            }
        }
        
        //convert to postFix
        // terms is postfix
        // Stack of operators
        convertToPostFix(infixTerms);
    }
    

    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String s, boolean keepConstants) {
        this.terms = new ArrayList<>();
//        this.terms.add(new Monomial(new Fraction(0)));
        ArrayList<Term> infixTerms = new ArrayList<>();
        
        String[] sterms = s.split(String.format(Statement.WITH_DELIMITER, "\\+|\\-"));
        
        
        for (int i = 0; i < sterms.length; i++) {
            try { // if it is a number
                infixTerms.add(new Monomial(new Fraction(Integer.valueOf(sterms[i]))));
            } catch (NumberFormatException nfe) {
                ArrayList<Variable> shizz = new ArrayList<Variable>();
                    shizz.add(new Variable(sterms[i], new Monomial(new Fraction(1))));
                    infixTerms.add(new Monomial(shizz));
            }
        }
        
        //convert to postFix
        // terms is postfix
        // Stack of operators
        convertToPostFix(infixTerms);
    }
    
    
    private void convertToPostFix(ArrayList<Term> infixTerms) {
        Stack<Term> operator = new Stack<>();
        while(!infixTerms.isEmpty()) {
            Term t = infixTerms.remove(0);
            if (t.isOperator()) {
                operator.push(t);
            } else {
                this.terms.add(t);
                if (!operator.isEmpty()) {
                    Term op = operator.pop();
                    this.terms.add(op);
                }
            }
        }
    }
    
    
    
    @Override
    public String toString() {
        String s = "";
        ArrayList<Monomial> stack = (ArrayList<Monomial>)this.terms.clone();
        ArrayList<String> infix = new ArrayList<>();
        if (stack.size() == 1) {
            infix.add(stack.remove(0).toString());
        } else {
            while(!stack.isEmpty()){
                Monomial m = stack.remove(0);
                if (!m.isOperator()) {
                    infix.add(m.toString());
                } else {
                    String combine = "";
                    combine += "(";
                    combine += infix.remove(0);
                    combine += m.toString();
                    combine += infix.remove(0);
                    combine += ")";
                    infix.add(combine);
                }
            }
        }
        for (int i = 0; i < infix.size(); i++)
            s += infix.get(i);
        return s;
    }
    
    public ArrayList<Term> getTerms() { return this.terms; }
}
