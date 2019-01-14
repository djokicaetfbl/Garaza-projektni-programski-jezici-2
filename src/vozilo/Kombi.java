/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

import java.io.File;

/**
 *
 * @author djord
 */
public class Kombi extends Vozilo{
    
    private double nosivost;

    public Kombi(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija,double nosivost) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija);
        this.nosivost = nosivost;
    }

    public double getNosivost() {
        return nosivost;
    }

    public void setNosivost(double nosivost) {
        this.nosivost = nosivost;
    }
    
}
