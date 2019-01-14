/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo.specijalno;

import java.io.File;
import vozilo.Automobil;
import vozilo.interfejs.RotacijaInterface;
import vozilo.interfejs.SanitetskoVoziloInterface;
/**
 *
 * @author djord
 */
public class SanitetskiAutomobil extends Automobil implements SanitetskoVoziloInterface,RotacijaInterface{

    private boolean rotacija = false;

    public SanitetskiAutomobil(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija, int brojVrata) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija, brojVrata);
    }

    public boolean isRotacija() {
        return rotacija;
    }

    public void setRotacija(boolean rotacija) {
        this.rotacija = rotacija;
    }

    @Override
    public boolean getRotacija() {
        return rotacija;
    }

}
