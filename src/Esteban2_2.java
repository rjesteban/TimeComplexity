
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rj
 */
public class Esteban2_2 {
    public static void main(String[] args) throws FileNotFoundException {
        String s = Util.compress("file.in.txt");
        
        ArrayList<Statement> blocks = Util.split(s);
        for (Statement ss: blocks) {
            // get T(n)
            // System.out.println(ss.getCode());
            System.out.println("T(n) for block: " + ss.getTime());
        }
    }
}


class AssignmentStatement extends Statement {
    private DefaultStatement leftStatement;
    private DefaultStatement rightStatement;
    private String assignmentOp;
    
    
    public AssignmentStatement(String code) {
        this.rawCode = code.trim();
        this.splitAssignment();
    }
    
    public DefaultStatement getLeftStatement() { return this.leftStatement; }
    public DefaultStatement getRightStatement() { return this.rightStatement; }
    
    private void splitAssignment() {
        String[] tokens = rawCode.split(String.format(WITH_DELIMITER, "-=|\\+=|\\/=|\\*="));
        if (tokens.length >1) {
            int length = tokens.length;
            this.leftStatement = new DefaultStatement(tokens[0]);
            this.assignmentOp = tokens[1];
            this.rightStatement = new DefaultStatement(tokens[2]);
        } else {
            int equals = this.rawCode.indexOf("=");
            if (equals !=-1) {
                char next = rawCode.charAt(equals+1);
                if (next != '=') {
                    this.assignmentOp = "=";
                    this.leftStatement = new DefaultStatement(this.rawCode.substring(0,equals), "initialization");
                    this.rightStatement = new DefaultStatement(this.rawCode.substring(equals+1, this.rawCode.length()));
                } else {
                    this.leftStatement = new DefaultStatement(this.rawCode);
                }
            } else {
                String[] ar = rawCode.split(String.format(WITH_DELIMITER, "\\+\\+|\\-\\-"));
                this.leftStatement = new DefaultStatement(ar[0]);
                if (ar.length > 1)
                    this.rightStatement = new DefaultStatement(ar[1]);
            }
        }
        this.setTime();
    }

    public String getAssignmentOp() { return this.assignmentOp;}
    
    @Override
    public void setTime() {
        this.time = new Polynomial(new Monomial(new Fraction(0)));
        this.time.add(this.leftStatement.getTime());
        this.time.add(new Polynomial(new Monomial(new Fraction(1))));
        if (this.rightStatement != null)
            this.time.add(this.rightStatement.getTime());
        //System.out.println("T(n) for " + this.rawCode + ": " + this.getTime());
    }
    
}


class Condition {
    private Statement leftStatement;
    private String comparator;
    private Statement rightStatement;
    private String code;
    
    public Condition(String code) {
        this.code = code;
        String[] statements = Util.getMatches(code,"[\\w-+/*()\\[\\]]+|==|!=|<=|>=|>|<");
        this.leftStatement = new DefaultStatement(statements[0]);
        if (statements.length == 3) {
            this.comparator = statements[1];
            this.rightStatement = new DefaultStatement(statements[2]);
        }
    }
    
    public String getCode() { return this.code; }
    
    public Statement getLeftStatement() { return this.leftStatement; }
    public Statement getRightStatement() { return this.rightStatement; }
    public String getComparator() { return this.comparator; }
    
    public Polynomial getTime() {
        Polynomial time = new Polynomial(this.leftStatement.getTime());
        time.add(this.rightStatement.getTime());
        if (this.comparator != null) {
            time.add(new Polynomial(new Monomial(new Fraction(1))));
        }
        return time;
    }
    
}


class DecisionStatement extends Statement {
    private ArrayList<Condition> conditions;
    private String bodyCode;
    private ArrayList<Statement> body;
    
    
    public DecisionStatement(String code, String body) {
        this.conditions = new ArrayList<>();
        this.rawCode = code.trim();
        this.bodyCode = body;
        this.parseComparison();
        this.parseBody();
        this.setTime();
    }
    
    public ArrayList<Condition> getConditions() { return this.conditions; }
    
    
    private void parseBody() {
        if (this.bodyCode != null) {
            this.body = Util.split(this.bodyCode);
        }
    }
    
    private void parseComparison() {
        String[] _conditions = this.rawCode.trim().split("(&&)|(\\|\\|)");
        for (String condition: _conditions){
            conditions.add(new Condition(condition.trim()));
        }
    }    

    @Override
    public void setTime() {
        Polynomial p = new Polynomial(new Monomial(new Fraction(0)));

        for (Condition condition: this.conditions) {
            p.add(condition.getTime());
        }
        if (this.body != null) {
            for (Statement s: this.body) {
                System.out.println("s: " + s.rawCode + " | " + this.rawCode);
                p.add(s.getTime());
            }
        }
        this.time = p;
    }

}


class DefaultStatement extends Statement {
    
    public DefaultStatement (String code) {
        this.rawCode = code;
        this.setTime();
    }
    
    public DefaultStatement (String code, String type) {
        code = code.trim();
        if (type.equals("initialization")) {
            String[] token = code.split("\\s+");
            
            this.rawCode = token.length==2? token[1]:token[0];
        }
        this.setTime();
    }

    @Override
    public void setTime() {
        int count = Util.getMatches(rawCode, "(\\-\\-)|(\\+\\+)|\\s*(^(\\+|\\-)){0,2}\\w+").length; 
        if (count >= 1)
            count--;        
        this.time = new Polynomial(new Monomial(new Fraction(count)));
    }
    
}


class ForLoop extends Statement {
    private AssignmentStatement[] initializationStatements;
    private DecisionStatement stoppingCondition;
    private AssignmentStatement[] iterationStatements;
    private String body;
    private boolean specialLoop;
    
    
    private Polynomial lowerBound;
    private Polynomial upperBound;
    
    public ForLoop (String code, String body) {
        this.rawCode = code;
        this.parseForPart();
        this.body = body;
        this.setBoundaries();
        this.setTime();
    }
    
    private void parseForPart() {
        String[] tokens = this.rawCode.split("\\s*;\\s*");
        
        String[] initializations = tokens[0].split("\\s*,\\s*");
        this.initializationStatements = new AssignmentStatement[initializations.length];
        
        for (int i = 0; i < initializations.length; i++) { 
            this.initializationStatements[i] = new AssignmentStatement(initializations[i]);
        }
        
        this.stoppingCondition = new DecisionStatement(tokens[1], null);
        
        String[] iterations = tokens[2].split("\\s*,\\s*");
        this.iterationStatements = new AssignmentStatement[iterations.length];
        for (int i = 0; i < iterations.length; i++) {
            this.iterationStatements[i] = new AssignmentStatement(iterations[i]);
        }
    }
      
    private void setBoundaries() {
        
        // ---------- find the matching upper bound, lower bound, and iterator--
        int len = this.initializationStatements.length;
        Condition upperB = null;
        AssignmentStatement lowerB = null;
        AssignmentStatement iterator = null;
        for (int i = 0; i < len; i++) {
            AssignmentStatement as = this.initializationStatements[i];
            Condition c = findMatchingCondition(as.getLeftStatement().rawCode);
            if (c != null ){
                upperB = c;
                lowerB = as;
                break;
            }
        }
        iterator = this.findIteration(lowerB.getLeftStatement().rawCode);
        
        // evaluate upper bound  
        evaluateUpperBound(lowerB, upperB, iterator);
        
    }
    
    
    private void evaluateUpperBound(AssignmentStatement lowerB,
            Condition c, AssignmentStatement iterator) {
        
        String assignment = iterator.getAssignmentOp();
        
        if (assignment == null) {
            // case when ++ or --
            String rightStatement = iterator.getRightStatement().rawCode.trim();
            if (rightStatement.equals("--")) {
                // BALI MANI
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.lowerBound.add(new Polynomial(new Monomial(new Fraction(1))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
                    System.out.println("T(n) cannot be evaluated");
                }
            }
            else if (rightStatement.equals("++")) {
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                
                if (comp.equals("<")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    String[] ub = c.getLeftStatement().rawCode.split("\\*");
                    if (ub.length == 2 && ub[0].equals(ub[1])) {
                        this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                        this.upperBound.subtract(new Polynomial(new Monomial(new Fraction(1))));
                        System.out.println("WALA PA***********************************");
                    } else {
                        this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                        this.upperBound.subtract(new Polynomial(new Monomial(new Fraction(1))));
                    }
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------aaaaa");
                } 
                
                else if (comp.equals("<=")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                }
                
                
                else {
                    System.out.println("T(n) cannot be evaluated");
                }
            }
        } else {
            if (assignment.equals("+=")) {
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                
                if (comp.equals("<")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                    this.upperBound.divide(new Polynomial(iterator.getRightStatement().rawCode));
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    this.upperBound.subtract(new Polynomial(new Monomial(new Fraction(1))));
                } 
                
                else if (comp.equals("<=")) {
                    
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                    this.upperBound.divide(new Polynomial(iterator.getRightStatement().rawCode));
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                }
                
                else {
                    System.out.println("T(n) cannot be evaluated");
                }
            } else if (assignment.equals("-=")) {
                String comp = c.getComparator().trim();
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.lowerBound.add(new Polynomial(new Monomial(new Fraction(1))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
                    System.out.println("T(n) cannot be evaluated");
                }
            } else if (assignment.equals("*=")) {
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                
                if (comp.equals("<")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(new Logarithm(c.getRightStatement().rawCode, iterator.getRightStatement().rawCode));
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    this.upperBound.subtract(new Polynomial(new Monomial(new Fraction(1))));
                } 
                
                else if (comp.equals("<=")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                }
                
                else {
                    System.out.println("T(n) cannot be evaluated");
                }
                
            } else if (assignment.equals("/=")) {
                String comp = c.getComparator().trim();
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.lowerBound.add(new Polynomial(new Monomial(new Fraction(1))));
                    this.upperBound = new Polynomial(new Logarithm(lowerB.getRightStatement().rawCode, iterator.getRightStatement().rawCode));
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(new Monomial(new Fraction(Integer.valueOf(c.getRightStatement().rawCode))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
                    System.out.println("T(n) cannot be evaluated");
                }
            }   
        }
    }
    
    private boolean isInfinite() {
        return true;
    }
    
    //*********************SAKTO NA NI DON'T EDIT*****************
    private Condition findMatchingCondition(String s) {
        ArrayList<Condition> conditions = this.stoppingCondition.getConditions();
        for (int i = 0; i < conditions.size(); i++) {
            String identifiers[] = conditions.get(i).getLeftStatement().rawCode.split("\\*");
            int len = identifiers.length;
            for (int j = 0; j < len; j++) {
                if (s.equals(identifiers[j]))
                    return conditions.get(i);
            }
        }
        return null;
    }
    
    //*********************SAKTO NA NI DON'T EDIT*****************
    private AssignmentStatement findIteration(String s) {
        AssignmentStatement[] iterations = this.iterationStatements;
        int length = iterations.length;
        for (int i = 0; i < length; i++) {
            if (iterations[i].getLeftStatement().rawCode.equals(s)) {
                return iterations[i];
            }    
        }
        return null;
    }
    
    @Override
    public void setTime() {
        ArrayList<Statement> statements = Util.split(this.body);
        Polynomial inside = new Polynomial(new Monomial(new Fraction(0)));
        Polynomial outside = new Polynomial(this.stoppingCondition.getTime());
        // inside Count
        for (Statement s: statements) {
            try {
                inside.add(new Polynomial(s.getTime()));
            } catch (Exception e) {
                System.out.println(">>>>>>>>>>> " + s.getCode());
            }
            
        }
        
        int numOfIterations = this.iterationStatements.length;
        for (int i = 0; i < numOfIterations; i++) {
            inside.add(this.iterationStatements[i].getTime());
        }
        
        int numOfInitializations = this.initializationStatements.length;
        for (int i = 0; i < numOfInitializations; i++) {
            outside.add(this.initializationStatements[i].getTime());
        }
        
        ArrayList<Condition> conditions = this.stoppingCondition.getConditions();
        int numOfConditions = conditions.size();
        for (int i = 0; i < numOfConditions; i++) {
            inside.add(conditions.get(i).getTime());
        }
        
        Polynomial upperExpressed = new Polynomial(this.upperBound);
        upperExpressed.subtract(this.lowerBound);
        upperExpressed.add(new Polynomial(new Monomial(new Fraction(1))));
        System.out.println("lowerB: " + upperExpressed);
        inside.multiply(upperExpressed);
        
        
        inside.add(outside);
        this.time = inside;
    }
}


abstract class Statement {
    protected String rawCode;
    protected Polynomial time;
    public Polynomial getTime() { return this.time; }
    public String getCode() { return this.rawCode; }
    public abstract void setTime();
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
}


final class Util {
    
    public static String compress(String fileName) throws FileNotFoundException {
        String compressedCode = "";
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()) {
            compressedCode += sc.nextLine().trim();
        }
        return compressedCode;
    }
    
    public static ArrayList<Statement> split(String compressedCode) {
        ArrayList<Statement> splitShizz = new ArrayList<Statement>();
        
        String[] blocks = compressedCode.split(";(?![^\\(]*\\)|[^{]*})|}(?![^{]*})");
        String forDelimiter = "\\s*for\\s*\\(([^\\)]*)\\)\\s*\\{{0,1}?(.*)\\}?";
        for (String s: blocks) {
            s = s.replaceAll("\\/\\/.*", ""); // remove comments
            Pattern p = Pattern.compile(forDelimiter);
            Matcher m = p.matcher(s);
            
            if (m.matches()) {
                if (m.group(2).startsWith("{")) {
                    splitShizz.add(new ForLoop(m.group(1), m.group(2).substring(1)));
                } else {
                    splitShizz.add(new ForLoop(m.group(1), m.group(2)));
                }
            }
            
            //====================GONNA FIX THIS IF SHIZZ====================
            else if(s.trim().startsWith("if(") || s.trim().startsWith("if (")) {
                Pattern paren = Pattern.compile("\\((.*)\\)\\s*\\{{0,1}?(.*)\\}?");
                Matcher matchIf = paren.matcher(s);
                if (matchIf.find()){
                    splitShizz.add(new DecisionStatement(matchIf.group(1), matchIf.group(2)));
                }
            }
            
           // else if assignment
            else {
                //System.out.println("assignment: " + s);
                splitShizz.add(new AssignmentStatement(s));
            }
            
        }
        return splitShizz;
    }
    
    public static String[] getMatches (String toMatch, String delimiter) {
        Pattern p = Pattern.compile(delimiter);
        Matcher m = p.matcher(toMatch);
        ArrayList<String> token = new ArrayList<>();
        while (m.find()) {
            token.add(m.group());
        }
        return token.toArray(new String[0]);
    }
    
}


class Fraction {
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


class Logarithm extends Term {
    // logb(x) equals exponent y
    private Polynomial c;
    private Polynomial b;
    private Polynomial x;

 
    public Polynomial getConstant() { return this.c; }
    public Polynomial getBase() { return this.b; }
    public Polynomial getX() { return this.x; }
    
    public void setConstant(Polynomial c) { this.c = c; }
    public void setBase(Polynomial b) { this.b = b; }
    public void setX(Polynomial x) { this.x = x; }
    
    
    public Logarithm(String x,String b) {
        this.c = new Polynomial(new Monomial(new Fraction(1)));
        this.b = new Polynomial(b);
        this.x = new Polynomial(x);
        this.x.removeConstants();
    }
    
    
    public Logarithm(Polynomial c, Polynomial b, Polynomial x) {
        this.c = c;
        this.b = b;
        this.x = x;
    }
    
    // iff logb(x) + logb(y) then logb(xy)
    public void plus (Logarithm l) {
        if (this.b.equals(l.b)) {
            this.x.multiply(l.x);
        }
    }
    
    // iff logb(x) - logb(y) then logb(x/y)
    public void minus (Logarithm l) {
        if (this.b.equals(l.b)) {
            this.x.divide(l.x);
        }
    }
    
    @Override
    public boolean isOperator() {
        return false;
    }

    public String toString() {
        if (this.c.toString().equals("1"))
            return "log" + this.b + "(" + this.x + ")";
        return this.c + "log" + this.b + "(" + this.x + ")";
    }
    
}


class Monomial extends Term {
    private Fraction coefficient;
    private ArrayList<Variable> variable;
    private boolean isOperator;
    
    public Monomial (Fraction coefficient) {
        this.coefficient = coefficient;
        this.variable = new ArrayList<>();
        this.isOperator = false;
        this.isConstant = true;
    }
    
    public Monomial (Fraction coefficient, ArrayList<Variable> variable) {
        this.coefficient = coefficient;
        this.variable = variable;
        this.isOperator = false;
        this.isConstant = false;
    }
    
    public Fraction getCoefficient() { return this.coefficient; }
    public void setCoefficient(Fraction f) { this.coefficient = f; }
    public ArrayList<Variable> getVariable() { return this.variable; }
    
    public Monomial (ArrayList<Variable> variable) {
        this.coefficient = new Fraction(1);
        this.variable = variable;
        if (variable.get(0).getVariable().equals("+") || variable.get(0).getVariable().equals("-") || 
            variable.get(0).getVariable().equals("/") || variable.get(0).getVariable().equals("*")) {
            this.isOperator = true;
        }
        else {
            this.isOperator = false;
        }
        this.isConstant = false;
    }
    
    
    public boolean isOperator() {
        return this.isOperator;
    }
    
    @Override
    public String toString() {
        if (this.isOperator())
            return "" + this.getVariable().get(0).toString() + "";
        else {
            String variables = "";
            int len = this.variable.size();
            for (int i = 0; i < len; i++) {
                variables += "" + this.variable.get(i).toString() + "";
            }
            if (this.coefficient.equals(new Fraction(1))) {
                if (variables.equals(""))
                    return this.coefficient.toString();
                else
                    return variables;
            } else {
                return this.coefficient + variables;
            }
        }
    }
    
}


class Polynomial {
    private final ArrayList<Term> terms;
    
    public void add(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("+");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
        evaluate();
    }
    
    public void subtract(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("-");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
        evaluate();
    }
    
    public void multiply(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("*");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
        evaluate();
    }
    
    public void divide(Polynomial p) { 
        this.terms.addAll(p.getTerms());
        Variable v = new Variable("/");
        ArrayList<Variable> vlist = new ArrayList<>();
        vlist.add(v);
        this.terms.add(new Monomial(vlist));
        evaluate();
    }
    
    public Polynomial(Polynomial p) {
        this.terms = (ArrayList<Term>)p.terms.clone();
        evaluate();
    }
    
    public Polynomial (Term m) {
        this.terms = new ArrayList<>();
        this.terms.add(m);
        evaluate();
    }
    
    public Polynomial () {
        this.terms = new ArrayList<>();
    }
    
    
    //if the input is a polynomial expression represented as an infix string
    public Polynomial (String s) {
        this.terms = new ArrayList<>();
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
        evaluate();
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
    
    private void evaluate() {
        /*
        String s = "";
        ArrayList<Term> stack = (ArrayList<Term>)this.terms.clone();
        ArrayList<String> infix = new ArrayList<>();
        if (stack.size() == 1) {
            infix.add(stack.remove(0).toString());
        } else {
            while(!stack.isEmpty()){
                Term t = stack.remove(0);
                if (!t.isOperator()) {
                    infix.add(t.toString());
                } else {
                    String combine = "";
                    combine += "(";
                    combine += infix.remove(0);
                    combine += t.toString();
                    combine += infix.remove(0);
                    combine += ")";
                    infix.add(combine);
                }
            }
        }
        for (int i = 0; i < infix.size(); i++)
            s += infix.get(i);
                */
    }
    
    @Override
    public String toString() {
        String s = "";
        ArrayList<Term> stack = (ArrayList<Term>)this.terms.clone();
        ArrayList<String> infix = new ArrayList<>();
        if (stack.size() == 1) {
            infix.add(stack.remove(0).toString());
        } else {
            while(!stack.isEmpty()){
                Term t = stack.remove(0);
                if (!t.isOperator()) {
                    infix.add(t.toString());
                } else {
                    String combine = "";
                    combine += "(";
                    combine += infix.remove(0);
                    combine += t.toString();
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
    
    public void removeConstants() {
        if (this.terms.size() > 1 && !this.terms.get(0).isConstant) {
            for (int i = 0; i < this.terms.size(); i++) {
                if (this.terms.get(i).isConstant) {
                    this.terms.remove(i);
                    if (i < this.terms.size()) {
                        if (this.terms.get(i).isOperator())
                            this.terms.remove(i);
                    } if (i + 1 < this.terms.size()) {
                        if (this.terms.get(i + 1).isOperator())
                            this.terms.remove(i + 1);
                    }
                }

            }
        }
    }
    
    public ArrayList<Term> getTerms() { return this.terms; }
}


abstract class Term {
    protected boolean isConstant;
    public abstract boolean isOperator();
}



class Variable {
    private final String var;
    private final Monomial exponent;
    
    public Variable(String variable, Monomial exponent) {
        this.var = variable;
        
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Monomial(new Fraction(0));
        } else {
            this.exponent = exponent;
        }
    }
    
    public Variable(String variable) {
        this.var = variable;
        if (variable.matches("\\+|\\-|\\*|\\/")) {
            this.exponent = new Monomial(new Fraction(0));
        }
        else
            this.exponent = new Monomial(new Fraction(1));
    }
    
    public String getVariable() { return this.var; }
    public Monomial getExponent() { return this.exponent; }
    
    @Override
    public String toString() {
        if (this.exponent.getCoefficient().equals(new Fraction(0)) ||
                this.exponent.getCoefficient().equals(new Fraction(1))) {
            return this.var;
        }
        else
            return this.var + "^" + this.exponent;
    }
    
}
