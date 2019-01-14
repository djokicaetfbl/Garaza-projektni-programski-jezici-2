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
public class Automobil extends Vozilo{
    
    private int brojVrata;
    
    public Automobil(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija,int brojVrata) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija);
        this.brojVrata = brojVrata;
    }

    public int getBrojVrata() {
        return brojVrata;
    }

    public void setBrojVrata(int brojVrata) {
        this.brojVrata = brojVrata;
    }

    @Override
    public String toString() {
        return "Automobil{" + "brojVrata=" + brojVrata + '}'+super.toString();
    }
    
    
    
}
