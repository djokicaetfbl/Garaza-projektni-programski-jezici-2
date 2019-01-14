/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platforma;

import garaza.Garaza;
import java.io.Serializable;
import java.util.ArrayList;
import vozilo.Vozilo;

/**
 *
 * @author djord
 */
public class Platforma implements Serializable {

    private static final int BROJ_REDOVA = 10;
    private static final int BROJ_KOLONA = 8;

    private Polje[][] platforma = new Polje[BROJ_REDOVA][BROJ_KOLONA];
    int brojPlatforme;
    private boolean daLiJeZadnjaPlatforma = false;
    private boolean sudarNaPlatformi = false;
    private int uspavajVozila = 10000;

    public Platforma(int brojPlatforme) {
        for (int i = 0; i < BROJ_REDOVA; ++i) {
            for (int j = 0; j < BROJ_KOLONA; ++j) {
                if (((i > 1 && i < 8) && (j == 0 || j == 3 || j == 4 || j == 7)) || ((i > 7 && i < BROJ_REDOVA) && (j == 0 || j == (BROJ_KOLONA - 1)))) {
                    platforma[i][j] = new Polje("Parking", brojPlatforme);
                    platforma[i][j].setTipPolja("Parking");
                    //platforma[i][j] = new Polje("*",brojPlatforme);
                } else {
                    platforma[i][j] = new Polje("Put", brojPlatforme);
                    platforma[i][j].setTipPolja("Put");
                    //platforma[i][j] = new Polje("-", brojPlatforme);
                }
            }
        }
        this.brojPlatforme = brojPlatforme;
    }

    public boolean DaLiJePlatformaPopunjena() {
        for (int i = 0; i < BROJ_REDOVA; ++i) {
            for (int j = 0; j < BROJ_KOLONA; ++j) {
                if (platforma[i][j].getTipPolja().equals("Parking") && platforma[i][j].isDaLiJePoljeSlobodno()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printPlatforma() { //izbrisati kasnije mozda

        System.out.println(platforma.length);

        for (int i = 0; i < platforma.length; ++i) {
            for (int j = 0; j < platforma[i].length; ++j) {
                if (platforma[i][j].getVozilo() != null) {
                    System.out.println("[" + i + "][" + j + "]" + platforma[i][j].getVozilo());
                } else {
                    System.out.print("[" + i + "][" + j + "]" + platforma[i][j] + "\t");
                }
            }
            System.out.println("");
        }
    }
    
    public ArrayList<Vozilo> svaParkiranaVozilaNaPlatformi(){
        ArrayList<Vozilo> listaVozila = new ArrayList<>();
        
        synchronized(platforma){ // da bi stanje bilo konzistentno, npr da mi neki tred ne uzme vozilo sa parking mjesta,dok ga ja ne izlistam
            for(int i=0; i < BROJ_REDOVA; i++){
                for(int j=0; j < BROJ_KOLONA; j++){
                    if(this.platforma[i][j].getTipPolja().equals("Parking")&& !this.platforma[i][j].isDaLiJePoljeSlobodno()){
                    listaVozila.add(this.platforma[i][j].getVozilo());
                   // this.platforma[i][j].getVozilo().setVrstaVozilaNaPlatformi(i);
                   // this.platforma[i][j].getVozilo().setKolonaVozilaNaPlatformi(j);
                }}
            }
            
        }
        
        return listaVozila;
    }

    public static int getBROJ_REDOVA() {
        return BROJ_REDOVA;
    }

    public static int getBROJ_KOLONA() {
        return BROJ_KOLONA;
    }

    public Polje[][] getPlatforma() {
        return platforma;
    }

    public int getBrojPlatforme() {
        return brojPlatforme;
    }

    public boolean isDaLiJeZadnjaPlatforma() {
        return daLiJeZadnjaPlatforma;
    }

    public void setPlatforma(Polje[][] platforma) {
        this.platforma = platforma;
    }

    public void setBrojPlatforme(int brojPlatforme) {
        this.brojPlatforme = brojPlatforme;
    }

    public void setDaLiJeZadnjaPlatforma(boolean daLiJeZadnjaPlatforma) {
        this.daLiJeZadnjaPlatforma = daLiJeZadnjaPlatforma;
    }

    public int getUspavajVozila() {
        return uspavajVozila;
    }

    public void setUspavajVozila(int uspavajVozila) {
        this.uspavajVozila = uspavajVozila;
    }

    public void setSudarNaPlatformi(boolean sudarNaPlatformi) {
        this.sudarNaPlatformi = sudarNaPlatformi;
    }

    public boolean isSudarNaPlatformi() {
        return sudarNaPlatformi;
    }
}
