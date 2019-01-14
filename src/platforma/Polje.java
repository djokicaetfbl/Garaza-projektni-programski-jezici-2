/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platforma;

import java.io.Serializable;
import vozilo.Vozilo;

/**
 *
 * @author djord
 */
public class Polje implements Serializable{
    
    private String tipPolja;
    private boolean daLiJePoljeSlobodno=true;
    int brojPlatforme;
    private Vozilo vozilo = null;
    
    public Polje(String tipPolja, int brojPlatforme){
        this.tipPolja = tipPolja;
        this.brojPlatforme = brojPlatforme;
        this.daLiJePoljeSlobodno = true;
    }

    public String getTipPolja() {
        return tipPolja;
    }

    public boolean isDaLiJePoljeSlobodno() {
        return daLiJePoljeSlobodno;
    }

    public int getBrojPlatforme() {
        return brojPlatforme;
    }

    public void setTipPolja(String tipPolja) {
        this.tipPolja = tipPolja;
    }

    public void setDaLiJePoljeSlobodno(boolean daLiJePoljeSlobodno) {
        this.daLiJePoljeSlobodno = daLiJePoljeSlobodno;
    }

    public void setBrojPlatforme(int brojPlatforme) {
        this.brojPlatforme = brojPlatforme;
    }

    public Vozilo getVozilo() {
        return vozilo;
    }

    public void setVozilo(Vozilo vozilo) {
        this.vozilo = vozilo;
        if(vozilo == null){
            setDaLiJePoljeSlobodno(true);
            return;
        }
        
        daLiJePoljeSlobodno = false;
        vozilo.setBrojPlatforme(brojPlatforme);
        vozilo.setVrijemeUlaskaUGarazu(vozilo.getVrijemeUlaskaUGarazu());
    }
    
    
    
    @Override
    public String toString(){
        return "["+tipPolja+"]";
    }
}
