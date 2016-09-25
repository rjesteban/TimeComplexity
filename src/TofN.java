
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
//        String s = Util.compress("file.in.txt");
        String s = Util.compress("jacetestcase_1.in");
//        String s = Util.compress("emantestcase.in");
        
        ArrayList<Statement> blocks = Util.split(s);
        
        int ctr = 1;
        for (Statement ss: blocks) {
            // get T(n)
            if (ss.getTime() == null)
                System.out.println(ctr++ + ") Infinite loop maaan!");
            else
                System.out.println(ctr++ + ") T(n) = " + ss.getTime());
        }
    }
}
