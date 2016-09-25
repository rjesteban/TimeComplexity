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
        super.times(m);
        
        
        return log;
    }
    
    @Override
    public String toString() {
        return super.toString() + "log(" + this.b + ")" + this.x;
    }

    
    public static void main(String[] args) {
        Logarithm logn = new Logarithm(new Fraction(2), new Polynomial("n", true));
        Polynomial m = new Polynomial("m", false);
        
        Logarithm prod = logn.times(m.getTerms().get(0));
        System.out.println("prod: " + prod);
    }

    
}
