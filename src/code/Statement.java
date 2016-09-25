/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import math.*;

/**
 *
 * @author rj
 */
public abstract class Statement {
    protected String rawcode;
    protected Polynomial time;
    public Polynomial getTime() { return this.time; }
    public String getCode() { return this.rawcode; }
    public abstract void setTime();
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
}
