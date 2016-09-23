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
public class Logarithm {
    // clogb(x) equals exponent y
    private Term c;
    private Polynomial b;
    private Polynomial x;

 
    public Term getConstant() { return this.c; }
    public Polynomial getBase() { return this.b; }
    public Polynomial getX() { return this.x; }
    
    public void setConstant(Term c) { this.c = c; }
    public void setBase(Polynomial b) { this.b = b; }
    public void setX(Polynomial x) { this.x = x; }
    
    
    public Logarithm(String x,String b) {
        //this.b = new Polynomial(b);
        //this.x = new Polynomial(x,);
    
    }
    
    
    public Logarithm(Term c, Polynomial b, Polynomial x) {
        this.c = c;
        this.b = b;
        this.x = x;
    }
    
    // iff logb(x) + logb(y) then logb(xy)
    public void plus (Logarithm l) {
        if (this.b.equals(l.b)) {
            
        }
    }
    
    // iff logb(x) - logb(y) then logb(x/y)
    public void minus (Logarithm l) {
        if (this.b.equals(l.b)) {
            
        }
    }


    
}
