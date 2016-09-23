package math;

public class Fraction {
    private int numerator;
    private int denominator;
    private Fraction exponent;

    public Fraction() {
        this.numerator = 0;
        this.denominator = 1;
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
    }
    
    public Fraction(int numerator, int denominator, Fraction exponent) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.exponent = exponent;
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
        int gcd = this.gcd(this.numerator, this.denominator);
        this.numerator /= gcd;
        this.denominator /= gcd;
    }
    
    public Fraction plus (Fraction f) {
        Fraction sum = new Fraction(
                (this.numerator*f.denominator) + (f.numerator*this.denominator),
                (this.denominator*f.denominator));
        sum.simplify();
        return sum;
    }

    public Fraction minus (Fraction f) {
        Fraction difference = new Fraction(
                (this.numerator*f.denominator) - (f.numerator*this.denominator),
                (this.denominator*f.denominator));
        difference.simplify();
        return difference;
    }
    
    public Fraction times (Fraction f) {
        Fraction product = new Fraction(
                (this.numerator*f.numerator),
                (this.denominator*f.denominator));
        product.simplify();
        return product;
    }
    
    public Fraction getReciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }
    
    public Fraction divideBy (Fraction f) {
        Fraction product = new Fraction(
                (this.numerator*f.denominator),
                (this.denominator*f.numerator));
        product.simplify();
        return product;
    }
    
    @Override
    public String toString() {

        if (this.denominator != 1 && this.exponent == null)
            return this.numerator + "/" + this.denominator;
        else if (this.denominator != 1 && this.exponent != null)
            return "(" + this.numerator + "/" + this.denominator + ")^" + this.exponent;
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
