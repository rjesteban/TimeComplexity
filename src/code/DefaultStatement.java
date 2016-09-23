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
public class DefaultStatement extends Statement {
    
    public DefaultStatement (String code) {
        this.rawCode = code.trim();
        this.setTime();
    }
    
    public DefaultStatement (String code, String type) {
        code = code.trim();
        if (type.equals("initialization")) {
            String[] token = code.split("\\s+");
            
            this.rawCode = token.length==2? token[1].trim():token[0].trim();
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
