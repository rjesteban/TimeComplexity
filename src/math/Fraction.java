package math;

public class Fraction {
    private int numerator;
    private int denominator;
    private Fraction exponent;

    public Fraction() {
        this.numerator = 0;
        this.denominator = 1;
    }
    
    public Fraction clone() {
        Fraction clone = new Fraction();
        int num = this.numerator;
        int den = this.denominator;
        clone.setNumerator(num);
        clone.setDenominator(den);
        if (exponent != null)
            clone.setExponent(new Fraction(exponent.numerator, exponent.denominator));
        
        
        return clone;
    }
    
    public Fraction(int numerator) {
        this.numerator = numerator;
        this.denominator = 1;
    }
    
    public Fraction(int numerator, Fraction exponent) {
        this.numerator = numerator;
        this.denominator = 1;
        this.exponent = exponent;
    }

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.simplify();
    }
    
    public Fraction(int numerator, int denominator, Fraction exponent) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.exponent = exponent;
        this.simplify();
    }
    
    public int getNumerator() { return numerator; }
    public void setNumerator(int numerator) { this.numerator = numerator; }
    public int getDenominator() { return denominator; }
    public void setDenominator(int denominator) { this.denominator = denominator; }
    public Fraction getExponent() { return this.exponent; }
    
    private int gcd (int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    
    private void simplify() {
        int gcd = this.gcd(this.getNumerator(), this.getDenominator());
        this.setNumerator(this.getNumerator() / gcd);
        this.setDenominator(this.getDenominator() / gcd);
    }
    
    public Fraction plus (Fraction f) {
        Fraction sum = new Fraction(
                (this.getNumerator()*f.getDenominator()) + (f.getNumerator()*this.getDenominator()),
                (this.getDenominator()*f.getDenominator()));
        sum.simplify();
        return sum;
    }

    public Fraction minus (Fraction f) {
        Fraction difference = new Fraction(
                (this.getNumerator()*f.getDenominator()) - (f.getNumerator()*this.getDenominator()),
                (this.getDenominator()*f.getDenominator()));
        difference.simplify();
        return difference;
    }
    
    public Fraction times (Fraction f) {
        Fraction product = new Fraction(
                (this.getNumerator()*f.getNumerator()),
                (this.getDenominator()*f.getDenominator()));
        product.simplify();
        return product;
    }
    
    public Fraction getReciprocal() {
        return new Fraction(this.getDenominator(), this.getNumerator());
    }
    
    public Fraction divideBy (Fraction f) {
        Fraction product = new Fraction(
                (this.getNumerator()*f.getDenominator()),
                (this.getDenominator()*f.getNumerator()));
        product.simplify();
        return product;
    }
    
    @Override
    public String toString() {

        if (this.getDenominator() != 1 && this.getExponent() == null)
            return this.getNumerator() + "/" + this.getDenominator();
        else if (this.getDenominator() != 1 && this.getExponent() != null)
            return "(" + this.getNumerator() + "/" + this.getDenominator() + ")^" + this.getExponent();
        else
            return this.getNumerator() + "";
    }
     
    public boolean equals(Fraction f) {
        this.simplify();
        f.simplify();
        return (this.getNumerator() == f.getNumerator() && 
                this.getDenominator() == f.getDenominator());
    }

    /**
     * @param exponent the exponent to set
     */
    public void setExponent(Fraction exponent) {
        this.exponent = exponent;
    }
}
