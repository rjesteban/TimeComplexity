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
public class Condition {
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
        Polynomial time = this.leftStatement.getTime();
        time.add(this.rightStatement.getTime());
        if (this.comparator != null) {
            time.add(new Polynomial(new Monomial(new Fraction(1))));
        }
        return time;
    }
    
}
