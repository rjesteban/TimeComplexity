package math;

public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction() {
        this.numerator = 0;
        this.denominator = 1;
    }
    
    public Fraction(int numerator) {
        this.numerator = numerator;
        this.denominator = 1;
    }
    
    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    public int getNumerator() { return numerator; }
    public void setNumerator(int numerator) { this.numerator = numerator; }
    public int getDenominator() { return denominator; }
    public void setDenominator(int denominator) { this.denominator = denominator; }
    
    private int gcd (int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    
    private void simplify() {
        int gcd = this.gcd(this.numerator, this.denominator);
        this.numerator /= gcd;
        this.denominator /= gcd;
    }
    
    private Fraction plus (Fraction f) {
        Fraction sum = new Fraction(
                (this.numerator*f.denominator) + (f.numerator*this.denominator),
                (this.denominator*f.denominator));
        sum.simplify();
        return sum;
    }

    private Fraction minus (Fraction f) {
        Fraction difference = new Fraction(
                (this.numerator*f.denominator) - (f.numerator*this.denominator),
                (this.denominator*f.denominator));
        difference.simplify();
        return difference;
    }
    
    private Fraction times (Fraction f) {
        Fraction product = new Fraction(
                (this.numerator*f.numerator),
                (this.denominator*f.denominator));
        product.simplify();
        return product;
    }
    
    private Fraction getReciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }
    
    private Fraction divideBy (Fraction f) {
        Fraction product = new Fraction(
                (this.numerator*f.denominator),
                (this.denominator*f.numerator));
        product.simplify();
        return product;
    }
    
    @Override
    public String toString() {
        if (this.denominator != 1)
            return this.numerator + "/" + this.denominator;
        else
            return this.numerator + "";
    }
     
    public boolean equals(Fraction f) {
        this.simplify();
        f.simplify();
        return (this.numerator == f.numerator && 
                this.denominator == f.denominator);
    }
}
