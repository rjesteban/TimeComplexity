/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import math.Fraction;
import math.Monomial;
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
        this.rawCode = code.trim();
        this.splitAssignment();
        this.setTime();
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
    }

    public String getAssignmentOp() { return this.assignmentOp;}
    
    @Override
    public void setTime() {
        this.time = new Polynomial();
        this.time.add(this.leftStatement.getTime());
        this.time.add(new Polynomial(new Monomial(new Fraction(1))));
        if (this.rightStatement != null)
            this.time.add(this.rightStatement.getTime());
//        System.out.println("T(n) for " + this.rawCode + ": " + this.getTime());
    }
    
}
