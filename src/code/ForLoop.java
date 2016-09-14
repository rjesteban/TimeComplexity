package code;


import java.util.ArrayList;
import math.Fraction;
import math.Logarithm;
import math.Monomial;
import math.Polynomial;

public class ForLoop extends Statement {
    private AssignmentStatement[] initializationStatements;
    private DecisionStatement stoppingCondition;
    private AssignmentStatement[] iterationStatements;
    private String body;
    private boolean specialLoop;
    
    
    private Polynomial lowerBound;
    private Polynomial upperBound;
    
    private int outsideCount; // includes assignment count and stopping condition
    private int insideCount; // includes stopping condition only

    public ForLoop (String code, String body) {
        this.rawCode = code;
        this.parseForPart();
        this.body = body;
        System.out.println("loop: " + this.rawCode + " {" + this.body + "}");
        this.setBoundaries();
        this.parseBodyPart();
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
    
    private void parseBodyPart() {
        ArrayList<Statement> statements = Splitter.split(this.body);
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
                    this.lowerBound = new Polynomial(new Monomial(new Fraction(Integer.valueOf(c.getRightStatement().rawCode))));
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
                    this.upperBound = new Polynomial(c.getRightStatement().rawCode);
                    this.upperBound.subtract(new Polynomial(new Monomial(new Fraction(1))));
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
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
                    this.lowerBound = new Polynomial(new Monomial(new Fraction(Integer.valueOf(c.getRightStatement().rawCode))));
                    this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
                    System.out.println("-------" + this.lowerBound + "|" + this.upperBound + "-------");
                
                } else {
                    System.out.println("T(n) cannot be evaluated");
                }
            } else if (assignment.equals("*=")) {
                System.out.println("*= siya");
                
                
                
                
            } else if (assignment.equals("/=")) {
                String comp = c.getComparator().trim();
                if (comp.equals(">")) {
                    this.lowerBound = new Polynomial(c.getRightStatement().rawCode);
                    this.lowerBound.add(new Polynomial(new Monomial(new Fraction(1))));
                    //this.upperBound = new Polynomial(lowerB.getRightStatement().rawCode);
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
            if (s.equals(conditions.get(i).getLeftStatement().rawCode))
                return conditions.get(i);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
