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
public class Logarithm extends Term{
    // clogb(x) equals exponent y
    private Fraction b;
    private Polynomial x;

 
    public Logarithm(Fraction base, Polynomial x){
        super(new Fraction(1));
        this.b = base.copy();
        this.x = x.copy();
        this.TYPE = "log";
    }
    
    public Logarithm(Term m, Fraction base, Polynomial x) {
        this.b = base.copy();
        this.x = x.copy();
        this.coefficient = m.coefficient.copy();
        this.variable = Term.copyvars(m.variable);
        this.TYPE = "log";
    }
    
    public Logarithm(){
        super();
        this.TYPE = "log";
    }
        
  
    public Fraction getB() { return b; }
    public void setB(Fraction b) { this.b = b.copy();}
    public Polynomial getX() { return x; }
    public void setX(Polynomial x) { this.x = x.copy(); }
    
    @Override
    public Logarithm copy() {
        Logarithm log = new Logarithm(this.b, this.x);
        log.setCoefficient(this.getCoefficient());
        log.setVariable(this.variable);
        return log;
    }
    
    
    @Override
    public Logarithm times(Term m) {
        Logarithm log = this.copy();
        Term prod = super.times(m);
        log.variable = Term.copyvars(prod.variable);
        log.coefficient = prod.coefficient.copy();
        
        if (m.TYPE.equals("log"))
            System.out.println("its a looog");
        
        
        return log;
    }
    
    @Override
    public String toString() {
        return super.toString() + "log[" + this.b + "](" + this.x + ")";
    }

    
    public static void main(String[] args) {
        ArrayList<Variable> var = new ArrayList<Variable>();
        var.add(new Variable("g", new Fraction(3)));
        
        Term t = new Term(new Fraction(3), var);
        Logarithm logn = new Logarithm(t, new Fraction(2), new Polynomial("n", true));
                        
        Polynomial m = new Polynomial("6*m", false);
        Polynomial prod = new Polynomial(logn);
        Polynomial m2 = new Polynomial("6*m", false);
        Polynomial prod2 = new Polynomial(logn);
        Polynomial prod3 = new Polynomial(logn);
        Polynomial prod4 = new Polynomial(logn);
        prod.multiply(m);
        m2.multiply(prod2);
        prod3.multiply(prod);

        System.out.println("prod : " + prod);
        System.out.println("   m2: " + m2);
        System.out.println("prod3: " + prod3);
    }

    
}
