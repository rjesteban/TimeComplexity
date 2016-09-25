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
public class Scope extends Statement {
    
    public Scope (String rawcode) {
        this.rawcode = rawcode.substring(1, rawcode.length()-1);
        setTime();
    }
    
    @Override
    public void setTime() {
        ArrayList<Statement> statements = Util.split(rawcode);
        this.time = new Polynomial();
        for(Statement stmt: statements)
            this.time.add(stmt.getTime());
    }
    
}
