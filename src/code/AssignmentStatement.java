/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import math.Fraction;
import math.Term;
import math.Polynomial;

/**
 *
 * @author rj
 */
public class AssignmentStatement extends Statement {
    private DefaultStatement leftStatement;
    private DefaultStatement rightStatement;
    private String assignmentOp;
    
    
    public AssignmentStatement(String code) {
        this.rawcode = code.trim();
        this.splitAssignment();
        this.setTime();
    }
    
    public DefaultStatement getLeftStatement() { return this.leftStatement; }
    public DefaultStatement getRightStatement() { return this.rightStatement; }
    
    private void splitAssignment() {
        String[] tokens = rawcode.split(String.format(WITH_DELIMITER, "-=|\\+=|\\/=|\\*="));
        if (tokens.length >1) {
            int length = tokens.length;
            this.leftStatement = new DefaultStatement(tokens[0]);
            this.assignmentOp = tokens[1];
            this.rightStatement = new DefaultStatement(tokens[2]);
        } else {
            int equals = this.rawcode.indexOf("=");
            if (equals !=-1) {
                char next = rawcode.charAt(equals+1);
                if (next != '=') {
                    this.assignmentOp = "=";
                    this.leftStatement = new DefaultStatement(this.rawcode.substring(0,equals), "initialization");
                    this.rightStatement = new DefaultStatement(this.rawcode.substring(equals+1, this.rawcode.length()));
                } else {
                    this.leftStatement = new DefaultStatement(this.rawcode);
                }
            } else {
                String[] ar = rawcode.split(String.format(WITH_DELIMITER, "\\+\\+|\\-\\-"));
                this.leftStatement = new DefaultStatement(ar[0]);
                if (ar.length > 1)
                    this.rightStatement = new DefaultStatement(ar[1]);
            }
        }
    }

    public String getAssignmentOp() { return this.assignmentOp;}
    
    @Override
    public void setTime() {
        System.out.println("aw: " + rawcode);
        this.time = new Polynomial();
        this.time.add(this.leftStatement.getTime());
        
//        if (!(this.rawCode.contains("{") || this.rawCode.contains("}")))
            this.time.add(new Polynomial(new Term(new Fraction(1))));
            
        
        if (this.rightStatement != null)
            this.time.add(this.rightStatement.getTime());
//        System.out.println("T(n) for " + this.rawCode + ": " + this.getTime());
    }
    
}
