/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.util.ArrayList;

/**
 *
 * @author rj
 */
public interface Term {
    @Override
    public String toString();
    public void plus(Term m);
    public void minus(Term m);
    public void times(Term m);
    public void divide(Term m);
    public boolean isLike(Term t);
    public ArrayList<Variable> getVariable();
}
