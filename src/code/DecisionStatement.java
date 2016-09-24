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
    private ArrayList<Statement> body;
    private ArrayList<String> elseifs;
    
    
    public DecisionStatement(String code, String body) {
        this.elseifs = new ArrayList<String>();
        this.conditions = new ArrayList<Condition>();
        this.rawCode = code.trim();
        this.bodyCode = body;
        this.parseComparison();
        this.setTime();
    }
    
    public ArrayList<Condition> getConditions() { return this.conditions; }
    public ArrayList<String> getelseIfs() { return this.elseifs; }
    
    
    private void parseComparison() {
        String[] _conditions = this.rawCode.trim().split("(&&)|(\\|\\|)");
        for (String condition: _conditions){
            conditions.add(new Condition(condition.trim()));
        }
    }    

    @Override
    public void setTime() {
        Polynomial p = new Polynomial();
        for (int i = 0; i < this.conditions.size(); i++) {
            p.add(this.conditions.get(i).getTime());
        }
        this.time = new Polynomial(p);
    }

}
