/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo.specijalno;

import java.io.File;
import vozilo.Motocikl;
import vozilo.interfejs.PolicijskoVoziloInterface;
import vozilo.interfejs.RotacijaInterface;
/**
 *
 * @author djord
 */
public class PolicijskiMotocikl extends Motocikl implements PolicijskoVoziloInterface, RotacijaInterface {

    private boolean rotacija = false;

    public PolicijskiMotocikl(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija) {
        super(naziv, brojSasije, brojMotora, registarskiBroj, fotografija);
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
