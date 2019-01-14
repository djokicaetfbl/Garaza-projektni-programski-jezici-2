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
public class Motocikl extends Vozilo {
    
    public Motocikl(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija);
    }
    
    @Override
    public String toString(){
        return "Motocikl "+super.toString();
    }
}
