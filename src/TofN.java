
import code.Splitter;
import code.Statement;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
        String s = Splitter.compress("file.in.txt");
        
        ArrayList<Statement> blocks = Splitter.split(s);
        for (Statement ss: blocks) {
            // get T(n)
            // System.out.println(ss.getCode());
        }
    }
}
