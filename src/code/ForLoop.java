package code;


import java.util.ArrayList;
import math.Fraction;
import math.Logarithm;
import math.Polynomial;
import math.Term;
import math.Variable;

public class ForLoop extends Statement {
    private ForLoop parent;
    private AssignmentStatement[] initializationStatements;
    private DecisionStatement stoppingCondition;
    private AssignmentStatement[] iterationStatements;
    private String body;
    private boolean specialLoop;
    
    
    private Polynomial lowerBound;
    private Polynomial upperBound;
    
    private Polynomial insideCount;
    private Polynomial outsideCount;
    
    // private int outsideCount; // includes assignment count and stopping condition
    // private int insideCount; // includes stopping condition only

    public ForLoop (String code, String body) {
        this.rawcode = code;
        this.parseForPart();
        this.body = body;
        this.setBoundaries();
        this.parseBodyPart();
        this.setTime();
    }
    
    private void parseForPart() {
        String[] tokens = this.rawcode.split("\\s*;\\s*");
        
        String[] initializations = tokens[0].split("\\s*,\\s*");
        this.initializationStatements = new AssignmentStatement[initializations.length];
        
        for (int i = 0; i < initializations.length; i++) { 
            this.initializationStatements[i] = new AssignmentStatement(initializations[i]);
        }
        
        this.stoppingCondition = new DecisionStatement(tokens[1], "");
        
        String[] iterations = tokens[2].split("\\s*,\\s*");
        this.iterationStatements = new AssignmentStatement[iterations.length];
        for (int i = 0; i < iterations.length; i++) {
            this.iterationStatements[i] = new AssignmentStatement(iterations[i]);
        }
    }
    
    private void parseBodyPart() {
        ArrayList<Statement> statements = Util.split(this.body);
        this.insideCount = new Polynomial();
        this.outsideCount = new Polynomial();
        
        // cond
        this.insideCount.add(this.stoppingCondition.getTime());
        
        // iter
        for (int i = 0, len = this.iterationStatements.length; i < len; i++) {
            this.insideCount.add(this.iterationStatements[i].getTime());
        }
        // body
        for (int i = 0, len = statements.size(); i < len; i++) {
            try {
            this.insideCount.add(statements.get(i).getTime());
            } catch (Exception e) {
                this.time = new Polynomial();
            }
        }

        // ------------ outside count ---------------
        for (int i = 0, len = this.initializationStatements.length; i < len ; i++) {
            this.outsideCount.add(this.initializationStatements[i].getTime());
        }
        this.outsideCount.add(this.stoppingCondition.getTime());
    }
    
    private void setBoundaries() {
        
        // ---------- find the matching upper bound, lower bound, and iterator--
        
        Condition upperB = null;
        AssignmentStatement lowerB = null;
        AssignmentStatement iterator = null;
        for (int i = 0, len = this.initializationStatements.length; i < len; i++) {
            AssignmentStatement as = this.initializationStatements[i];
            Condition c = findMatchingCondition(as.getLeftStatement().rawcode);
            if (c != null ){
                upperB = c;
                lowerB = as;
                break;
            }
        }
        iterator = this.findIteration(lowerB.getLeftStatement().rawcode.trim());
        
        // evaluate upper bound  
        evaluateUpperBound(lowerB, upperB, iterator);
        
    }
    
    
    private void evaluateUpperBound(AssignmentStatement lowerB,
            Condition c, AssignmentStatement iterator) {

        String assignment = iterator.getAssignmentOp();
        
        if (assignment == null) {
            // case when ++ or --
            String rightStatement = iterator.getRightStatement().rawcode.trim();
            if (rightStatement.equals("--")) {
                // BALI MANI
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawcode, false);
                    this.lowerBound.add(new Polynomial(new Term(new Fraction(1))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(new Term(new Fraction(Integer.valueOf(c.getRightStatement().rawcode))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
//                    System.out.println("T(n) cannot be evaluated");
                    this.time = new Polynomial();
                }
            }
            else if (rightStatement.equals("++")) {
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                
                if (comp.equals("<")) {
                    if (c.getLeftStatement().rawcode.contains("*")
                        ) {
                        
                        Polynomial upper = new Polynomial(c.getLeftStatement().rawcode, false).substituteVar(
                                c.getRightStatement().rawcode.trim()
                        );
                        
                        this.upperBound = upper.copy();
                        for(Term t:this.upperBound.getTerms()) {
                            for (Variable v:t.getVariable()){
                                v.setExponent(v.getExponent().getReciprocal());
                            }
                        }
                        this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                        this.upperBound.subtract(new Polynomial(new Term(new Fraction(1))));
                    
                    } else {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    this.upperBound = new Polynomial(c.getRightStatement().rawcode, false);
                    this.upperBound.subtract(new Polynomial(new Term(new Fraction(1))));
                    }
                    
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                } 
                
                else if (comp.equals("<=")) {
                    if (c.getLeftStatement().rawcode.contains("*")
                        ) {
                        
                        Polynomial upper = new Polynomial(c.getLeftStatement().rawcode, false).substituteVar(
                                c.getRightStatement().rawcode.trim()
                        );
                        
                        this.upperBound = upper.copy();
                        for(Term t:this.upperBound.getTerms()) {
                            for (Variable v:t.getVariable()){
                                v.setExponent(v.getExponent().getReciprocal());
                            }
                        }
                        this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    
                    } else {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    this.upperBound = new Polynomial(c.getRightStatement().rawcode, false);
                    }
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                }
                
                
                else {
//                    System.out.println("T(n) cannot be evaluated");
                    this.time = new Polynomial();
                }
            }
        } else {
            if (assignment.equals("+=")) {
                
                String comp = c.getComparator().trim();
                // iterator value is 1 pero balihun ang sign ug ang bounds
                
                if (comp.equals("<")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    this.upperBound = new Polynomial(c.getRightStatement().rawcode, false);
                    Polynomial p = new Polynomial(iterator.getRightStatement().rawcode, false);
                    this.upperBound.subtract(new Polynomial(new Term(new Fraction(1))));
                    this.upperBound.divide(p);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                } 
                
                else if (comp.equals("<=")) {
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    this.upperBound = new Polynomial(c.getRightStatement().rawcode, false);
                    Polynomial p = new Polynomial(iterator.getRightStatement().rawcode, false);
                    this.upperBound.divide(p);
//                     System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                }
                
                
                else {
//                    System.out.println("T(n) cannot be evaluated");
                    this.time = new Polynomial();
                }
            } else if (assignment.equals("-=")) {
                String comp = c.getComparator().trim();
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawcode, false);
                    this.lowerBound.add(new Polynomial(new Term(new Fraction(1))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    Polynomial p = new Polynomial(iterator.getRightStatement().rawcode, false);
                    this.upperBound.divide(p);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(new Term(new Fraction(Integer.valueOf(c.getRightStatement().rawcode))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    Polynomial p = new Polynomial(iterator.getRightStatement().rawcode, false);
                    this.upperBound.divide(p);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
//                    System.out.println("T(n) cannot be evaluated");
                    this.time = new Polynomial();
                }
            } else if (assignment.equals("*=")) {
                String comp = c.getComparator().trim();
                if (comp.equals("<") || comp.equals("<=")) {
                    
                    
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    this.upperBound = new Polynomial(c.getRightStatement().rawcode, false);
                    Polynomial p = new Polynomial(iterator.getRightStatement().rawcode, false);
                    
                    
                    this.lowerBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    Fraction b = new Fraction(Integer.valueOf(iterator.getRightStatement().rawcode.trim()));
                    Polynomial x = new Polynomial(c.getRightStatement().rawcode, true);
 
                    Logarithm sl = new Logarithm(b, x);
                    this.upperBound = new Polynomial(sl.copy());

                    
                } else {
                    this.time = new Polynomial();
                }
                
                
                
                
            } else if (assignment.equals("/=")) {
                String comp = c.getComparator().trim();
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawcode, false);
                    this.lowerBound.add(new Polynomial(new Term(new Fraction(1))));
                    
                    Fraction b = new Fraction(Integer.valueOf(iterator.getRightStatement().rawcode.trim()));
                    Polynomial x = new Polynomial(lowerB.getRightStatement().rawcode, true);
                    
                    Logarithm l = new Logarithm(b, x);
                    
                    
                    this.upperBound = new Polynomial(l);
                    
//                    throw new UnsupportedOperationException("LOGS NOT SUPPORTED YET!");
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                    
                } else if (comp.equals(">=")) {
                    this.lowerBound = new Polynomial(new Term(new Fraction(Integer.valueOf(c.getRightStatement().rawcode))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawcode, false);
                    // System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
//                    System.out.println("T(n) cannot be evaluated");
                    this.time = new Polynomial();
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
            String match = conditions.get(i).getLeftStatement().rawcode.trim();
            
            if (s.contains(String.valueOf(
                    conditions.get(i).getLeftStatement().rawcode.trim().
                            replaceAll("\\*|\\+|\\-|\\/", "").charAt(0)
                                        )
                            )
                    )
                return conditions.get(i);
        }
        return null;
    }
    
    //*********************SAKTO NA NI DON'T EDIT*****************
    private AssignmentStatement findIteration(String s) {
        AssignmentStatement[] iterations = this.iterationStatements;
        int length = iterations.length;
        for (int i = 0; i < length; i++) {
            if (iterations[i].getLeftStatement().rawcode.equals(s)) {
                return iterations[i];
            }    
        }
        return null;
    }
    
    @Override
    public void setTime() {
        if (upperBound != null && lowerBound != null && this.time == null) {
//            Polynomial p = new Polynomial(upperBound.copy());
            Polynomial p = new Polynomial(this.upperBound);
            p.subtract(lowerBound);
            p.add(new Polynomial(new Term(new Fraction(1))));
            p.multiply(this.insideCount);
            p.add(this.outsideCount);
            this.time = p;
        } else {
            this.time = null;
        }
    }
}
