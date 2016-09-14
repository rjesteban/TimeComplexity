/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rj
 */
public final class Splitter {
    
    public static String compress(String fileName) throws FileNotFoundException {
        String compressedCode = "";
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()) {
            compressedCode += sc.nextLine().trim();
        }
        return compressedCode;
    }
    
    public static ArrayList<Statement> split(String compressedCode) {
        ArrayList<Statement> splitShizz = new ArrayList<Statement>();
        
        String[] blocks = compressedCode.split(";(?![^\\(]*\\)|[^{]*})|}(?![^{]*})");
        String forDelimiter = "\\s*for\\s*\\(([^\\)]*)\\)\\s*\\{{0,1}?(.*)\\}?";
        for (String s: blocks) {
            s = s.replaceAll("\\/\\/.*", ""); // remove comments
            Pattern p = Pattern.compile(forDelimiter);
            Matcher m = p.matcher(s);
            
            if (m.matches()) {
                if (m.group(2).startsWith("{")) {
                    splitShizz.add(new ForLoop(m.group(1), m.group(2).substring(1)));
                } else {
                    splitShizz.add(new ForLoop(m.group(1), m.group(2)));
                }
            }
            
            //====================GONNA FIX THIS IF SHIZZ====================
            else if(s.trim().startsWith("if(") || s.trim().startsWith("if (")) {
                Pattern paren = Pattern.compile("\\((.*)\\)\\s*\\{{0,1}?(.*)\\}?");
                Matcher matchIf = paren.matcher(s);
                if (matchIf.find()){
                    splitShizz.add(new DecisionStatement(matchIf.group(1), matchIf.group(2)));
                }
            }
            
           // else if assignment
            else {
                //System.out.println("assignment: " + s);
                splitShizz.add(new AssignmentStatement(s));
            }
            
        }
        return splitShizz;
    }
    
}
