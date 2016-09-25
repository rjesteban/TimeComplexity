/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.util.ArrayList;
import math.Polynomial;

/**
 *
 * @author rj
 */
public class DecisionStatement extends Statement {
    private ArrayList<Condition> conditions;
    private String bodyCode;
    private ArrayList<String> elseifs;
    
    
    public DecisionStatement(String code, String body) {
        this.elseifs = new ArrayList<String>();
        this.conditions = new ArrayList<Condition>();
        this.rawcode = code.trim();
        this.bodyCode = body;
        this.parseComparison();
        this.setTime();
    }
    
    public ArrayList<Condition> getConditions() { return this.conditions; }
    public ArrayList<String> getelseIfs() { return this.elseifs; }
    
    
    private void parseComparison() {
        String[] _conditions = this.rawcode.trim().split("(&&)|(\\|\\|)");
        for (String condition: _conditions){
            conditions.add(new Condition(condition.trim()));
        }
    }    

    @Override
    public void setTime() {
        Polynomial ifstmt = new Polynomial();
        for (int i = 0; i < this.conditions.size(); i++) {
            ifstmt.add(this.conditions.get(i).getTime());
        }
        
        if (!this.bodyCode.equals("{}")) {
            ArrayList<Statement> ifbody = Util.split(this.bodyCode);
            for (Statement ifbodystatement: ifbody) {
                ifstmt.add(ifbodystatement.getTime());
            }
        }
        this.time = ifstmt.copy();
    }

}
