/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
