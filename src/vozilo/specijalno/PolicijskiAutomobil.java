/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo.specijalno;

import java.io.File;
import vozilo.Automobil;
import vozilo.interfejs.PolicijskoVoziloInterface;
import vozilo.interfejs.RotacijaInterface;
/**
 *
 * @author djord
 */
public class PolicijskiAutomobil extends Automobil implements PolicijskoVoziloInterface,RotacijaInterface  {
    
    private boolean rotacija = false;
    
    public PolicijskiAutomobil(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija, int brojVrata) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija, brojVrata);
    }

    @Override
    public void setRotacija(boolean rotacija) {
        this.rotacija = rotacija;
    }

    @Override
    public boolean getRotacija() {
        return rotacija;
    }
    
    
}
