/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import math.Term;
import math.Variable;

/**
 *
 * @author rj
 */
public class Util {
    
    public static String[] getMatches (String toMatch, String delimiter) {
        Pattern p = Pattern.compile(delimiter);
        Matcher m = p.matcher(toMatch);
        ArrayList<String> token = new ArrayList<>();
        while (m.find()) {
            token.add(m.group());
        }
        return token.toArray(new String[0]);
    }
    
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
        
//        String[] blocks = compressedCode.split(";(?![^\\(]*\\)|[^{]*})|}(?![^{]*})");
        String[] blocks = splitString(compressedCode.trim());
        String forDelimiter = "\\s*for\\s*\\(([^\\)]*)\\)\\s*\\{{0,1}?(.*)\\}?";
        for (String s: blocks) {
            //s = s.replaceAll("\\/\\/.*", ""); // remove comments
            Pattern p = Pattern.compile(forDelimiter);
            Matcher m = p.matcher(s);
            
            if (m.matches()) {
                if (m.group(2).startsWith("{")) {
                    splitShizz.add(new ForLoop(m.group(1), m.group(2).substring(1, m.group(2).length() - 1)));
                } else {
                    if (m.group(2).endsWith(";"))
                        splitShizz.add(new ForLoop(m.group(1), m.group(2)));
                    else
                        splitShizz.add(new ForLoop(m.group(1), m.group(2).concat(";")));
                }
            }
            
            //====================GONNA FIX THIS IF SHIZZ====================
            else if(s.trim().startsWith("if(") || s.trim().startsWith("if (")) {
                Pattern paren = Pattern.compile("\\s*if\\s*\\(([^\\)]*)\\)\\s*\\{{0,1}?(.*)\\}?");
                Matcher matchIf = paren.matcher(s);
                if (matchIf.matches()){
                    if (matchIf.group(2).startsWith("{") && matchIf.group(2).startsWith("}"))
                        splitShizz.add(new DecisionStatement(matchIf.group(1), matchIf.group(2)));
                    else
                        splitShizz.add(new DecisionStatement(matchIf.group(1), matchIf.group(2)+";"));
                }
            }
            
            else if(s.trim().startsWith("else if(") || s.trim().startsWith("else if (")) {
                Pattern paren = Pattern.compile("\\s*else\\s*if\\s*\\(([^\\)]*)\\)\\s*\\{{0,1}?(.*)\\}?");
                Matcher matchIf = paren.matcher(s);
                if (matchIf.matches()){
                    DecisionStatement prevDec = (DecisionStatement)splitShizz.get(splitShizz.size()-1);
                     
                    String[] conds = matchIf.group(1).split("\\|\\||\\&\\&");
                    String conditionals = "";
                    String decBodystatements = "";
                    for (String condi: conds) {
                        conditionals += condi.trim() + ";";
                    }
                    if(matchIf.group(2).trim().startsWith("{") && matchIf.group(2).trim().endsWith("}")) {
                        prevDec.getelseIfs().add( conditionals +
                                matchIf.group(2).trim().substring(1, matchIf.group(2).length()-1)
                        );
                    }
                    else {
                        prevDec.getelseIfs().add( conditionals +
                                matchIf.group(2).trim() + ";"
                        );
                    }
                }
            }
            
            else if(s.trim().startsWith("else") || s.trim().startsWith("else")) {
                Pattern paren = Pattern.compile("\\s*(else)\\s*\\{{0,1}?(.*)\\}?");
                Matcher matchIf = paren.matcher(s);
                if (matchIf.matches()){

                    DecisionStatement prevDec = (DecisionStatement)splitShizz.get(splitShizz.size()-1);
                    
                    if(matchIf.group(2).trim().startsWith("{") && matchIf.group(2).trim().endsWith("}"))
                        prevDec.getelseIfs().add(
                                matchIf.group(2).trim().substring(1, matchIf.group(2).length()-1)
                        );
                    else
                        prevDec.getelseIfs().add(matchIf.group(2).trim() + ";");
                        
                }
            }
            
            else if (s.trim().startsWith("{") && s.trim().endsWith("}")) {
                splitShizz.add(new Scope(s));
            }
            
           // else if assignment
            else {
                splitShizz.add(new AssignmentStatement(s));
            }
            
        }
        return splitShizz;
    }
    
    public static String[] splitString(String s) {
        String line = "";
        List<String> codes = new LinkedList<String>();
        Stack<Character> blockStack = new Stack<Character>();
        Stack<Character> parenStack = new Stack<Character>();
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ';' && line.length() > 0 && (blockStack.isEmpty() && parenStack.isEmpty())) {
                codes.add(line.trim());
                line = "";
            } else if (s.charAt(i) == ';' && !blockStack.isEmpty()) {
                line += s.charAt(i);
                
            } else if (s.charAt(i) == '{') {
                line += s.charAt(i);
                blockStack.push(s.charAt(i));
                
            } else if (s.charAt(i) == '}') {
                line += s.charAt(i);
                blockStack.pop();
                
                if (blockStack.isEmpty()) {
                    codes.add(line.trim());
                    line = "";
                }
                
                
            } else if (s.charAt(i) == '(') {
                line += s.charAt(i);
                parenStack.push(s.charAt(i));
                
            } else if (s.charAt(i) == ')') {
                line += s.charAt(i);
                parenStack.pop();
                
            } else {
                line += s.charAt(i);
            }
        }
        return codes.toArray(new String[codes.size()]);
    }

}
