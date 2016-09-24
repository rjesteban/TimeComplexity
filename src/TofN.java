
import code.Util;
import code.Statement;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import math.Polynomial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rj
 */
public class TofN {
    public static void main(String[] args) throws FileNotFoundException {
        String s = Util.compress("file.in.txt");
//        String s = Util.compress("jacetestcase_1.in");
//        String s = Util.compress("emantestcase.in");
        
        ArrayList<Statement> blocks = Util.split(s);
        
        int ctr = 1;
        for (Statement ss: blocks) {
            // get T(n)
            // System.out.println(ss.getCode());
            if (ss.getTime() == null)
                System.out.println(ctr++ + ") Infinite loop maaan!");
            else
                System.out.println(ctr++ + ") T(n) = " + ss.getTime());
        }

//        Polynomial k = new Polynomial("i/n*34*3/6+5*n", false);
//        Polynomial l = new Polynomial("n-n*n", false);
//        System.out.println("k bef: " + k);
//        l.simplify();
//        System.out.println("l bef: " + l);
//        k.multiply(l);
//        System.out.println("k: " + k);
//        p.multiply(l);
//        System.out.println("p: " + p);
    }
}
