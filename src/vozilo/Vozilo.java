/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo;

import garaza.Garaza;
import gui.forme.SimulacijaForm;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import vozilo.interfejs.PolicijskoVoziloInterface;
import vozilo.interfejs.RotacijaInterface;
import vozilo.interfejs.SanitetskoVoziloInterface;
import vozilo.interfejs.SpecijalnoVoziloInterface;
import vozilo.interfejs.VatrogasnoVoziloInterface;

/**
 *
 * @author djord
 */
public class Vozilo extends Thread implements Serializable {

    private String naziv;
    private String brojSasije;
    private String brojMotora;
    private File fotografija = null;
    private String registarskiBroj;
    private int brojPlatforme;
    private Date vrijemeUlaskaUGarazu;
    private int tipVozila;
    private boolean parkiran = false;
    private static int SACEKAJ = 500;

    private boolean zapocniParkiranje = false;

    private final Lock locker = new ReentrantLock();
    private final Condition free = locker.newCondition();
    private final Condition notfree = locker.newCondition();

    private boolean izlazakPetnaestPostoVozilaIzGaraze = false;
    private boolean gotovaSimulacija = false;
    private boolean isparkirava = false;
    private boolean isparkiraoSeSalijevogDijelaPlatforme = false;

    private boolean presaoNaSljedecuPlatformu = false;
    private boolean pratimPolicijskoVozilo = false;

    private static List<Vozilo> vozilaZaNaplatuParkinga = new ArrayList();
    private static Random rand = new Random();

    private boolean potjera = false;
    Vozilo voziloKojePratiPolicijsko = null;
    private boolean isparkiraj = false;
    private boolean potjeraZaDrugiDioPlatforme = false;

    private boolean vecObradioPotjeru = false; // jer ako i lijevi i desno od tebe ima auto sa potjere

    public static boolean sudar = false;
    // private static boolean obradaSudara = false;
    private boolean obradaSudara = false;
    private static boolean sudarSeVecDesioNaPlatformi = false;
    Vozilo udarenoVozilo1 = null, udarenoVozilo2 = null;

    private static int vrstaPrvogUdarenogVozila, kolonaPrvogUdarenogVozila;
    private static int vrstaDrugogUdarenogVozila, kolonaDrugogUdarenogVozila;

    private int vrstaVozilaNaPlatformi, kolonaVozilaNaPlatformi;
    private boolean ucestvujemUSudaru = false; // da bi obezbjedio da se vise ne krece

    private static int vrstaSudara, kolonaSudara;
    private boolean vecSamIzvrsioUvidjaj = false;

    private int vrijemeObradeSudara;
    private int kolonaZaKretanjeLijevoKodSudara = 4;
    private int kolonaZaKretanjeDesnoKodSudara = 2;

    //private static boolean policijaZavrsilaUvidjaj = false, hitnaZavrsilaUvidjaj = false, VatrogasciZavrsiliUvidjaj = false;
    private static int brojacUvidjaja; // za svaki uvidjaj ga uvecaj za jedan i kad ima vrijednost tri onda kao sudar = false;
    Thread policija = null, hitna = null, vatrogasno = null;
    private int brojPlatformeNaKojojSeDesioSudar;

    public int getVrstaVozilaNaPlatformi() {
        return this.vrstaVozilaNaPlatformi;
    }

    public void setVrstaVozilaNaPlatformi(int vrstaVozilaNaPlatformi) {
        this.vrstaVozilaNaPlatformi = vrstaVozilaNaPlatformi;
    }

    public int getKolonaVozilaNaPlatformi() {
        return this.kolonaVozilaNaPlatformi;
    }

    public void setKolonaVozilaNaPlatformi(int kolonaVozilaNaPlatformi) {
        this.kolonaVozilaNaPlatformi = kolonaVozilaNaPlatformi;
    }

    public static List<Vozilo> getVozilaZaNaplatuParkinga() {
        return vozilaZaNaplatuParkinga;
    }

    public Vozilo(String naziv, String brojSasije, String brojMotora, String registarskiBroj, File fotografija) {
        this.naziv = naziv;
        this.brojSasije = brojSasije;
        this.brojMotora = brojMotora;
        this.registarskiBroj = registarskiBroj;
        this.fotografija = fotografija;
    }

    @Override
    public String toString() {
        return "Vozilo{" + "naziv=" + naziv + ", brojSasije=" + brojSasije + ", brojMotora=" + brojMotora + ", fotografija="
                + fotografija + ", registarskiBroj=" + registarskiBroj + '}';
    }

    public boolean isZapocniParkiranje() {
        return zapocniParkiranje;
    }

    public void setZapocniParkiranje(boolean zapocniParkiranje) {
        this.zapocniParkiranje = zapocniParkiranje;
    }

    public boolean isIzlazakPetnaestPostoVozilaIzGaraze() {
        return izlazakPetnaestPostoVozilaIzGaraze;
    }

    public void setIzlazakPetnaestPostoVozilaIzGaraze(boolean izlazakPetnaestPostoVozilaIzGaraze) {
        this.izlazakPetnaestPostoVozilaIzGaraze = izlazakPetnaestPostoVozilaIzGaraze;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vozilo other = (Vozilo) obj;
        if ((!this.brojSasije.equals(other.brojSasije)) && (!this.registarskiBroj.equals(other.registarskiBroj)) && (!this.brojMotora.equals(other.brojMotora))) {
            return false;
        }
        return true;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getBrojSasije() {
        return brojSasije;
    }

    public String getBrojMotora() {
        return brojMotora;
    }

    public File getFotografija() {
        return fotografija;
    }

    public String getRegistarskiBroj() {
        return registarskiBroj;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setBrojSasije(String brojSasije) {
        this.brojSasije = brojSasije;
    }

    public void setBrojMotora(String brojMotora) {
        this.brojMotora = brojMotora;
    }

    public void setFotografija(File fotografija) {
        this.fotografija = fotografija;
    }

    public void setRegistarskiBroj(String registarskiBroj) {
        this.registarskiBroj = registarskiBroj;
    }

    public void setBrojPlatforme(int brojPlatforme) {
        this.brojPlatforme = brojPlatforme;
    }

    public int getBrojPlatforme() {
        return brojPlatforme;
    }

    public int getTipVozila() {
        return tipVozila;
    }

    public void setTipVozila(int tipVozila) {
        this.tipVozila = tipVozila;
    }

    public void setVrijemeUlaskaUGarazu(Date vrijemeUlaskaUGarazu) {
        this.vrijemeUlaskaUGarazu = vrijemeUlaskaUGarazu;
    }

    public Date getVrijemeUlaskaUGarazu() {
        return vrijemeUlaskaUGarazu;
    }

    private void ispisi() {
        try {
            sleep(SACEKAJ);
        } catch (Exception ex) {
            Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimulacijaForm.ispisiPlatformu();
    }

    private boolean isparkirajZaDrugiDioPlatforme = false;

    private void isparkirajSe(int vrsta, int kolona) { // isparkiravanje kod potjere :D

        if (kolona == 0) {
            voziloKojePratiPolicijsko.idiDesno(vrsta, 1);
        }
        if (kolona == 3) {
            voziloKojePratiPolicijsko.idiLijevo(vrsta, 2);
            voziloKojePratiPolicijsko.idiLijevo(vrsta, 1);
        }
        if (kolona == 4) {
            voziloKojePratiPolicijsko.idiDesno(vrsta, 5);
            voziloKojePratiPolicijsko.idiDesno(vrsta, 6);
        }
        if (kolona == 7) {
            voziloKojePratiPolicijsko.idiLijevo(vrsta, 6);
        }

    }

    public void parkirajSe() {
        while (!parkiran) {

            int lokalnaVrstaPremaDole = 2; //++ do 9
            int lokalnaZaDesno = 2; //++ do 6
            int lokalnaVrstaZaGore = 9; // -- do 1 
            int vrstaZaPotjeru = 0;
            int kolonaZaPotjeru = 0;

            // kad dodam vise platformi pa za sudar kao ako platforma nije puna i ako vozilo(this) ucestvuje u sudaru neka napusti Garazu
            if ((Garaza.garaza[brojPlatforme].DaLiJePlatformaPopunjena() == true) && !(Garaza.garaza[brojPlatforme].isDaLiJeZadnjaPlatforma())) {
                int lokalnaKolona = 1;
                if (brojPlatforme == 0) {
                    lokalnaKolona = 0;
                }
                // int lokalnaKolona = 1;
                while (lokalnaKolona < 8) {
                    idiDesno(1, lokalnaKolona++);
                }
                // if (this.ucestvujemUSudaru) {
                //    this.idiGore(0, 7);
                //    this.idiLijevo(0, 6);
                //    if (this != null) {
                //        napustiGarazu();
                //   }
                // } else {
                idiNaSljedecuPlatformu();
                //}
            }

            if (!this.presaoNaSljedecuPlatformu) { // VRATI OVO
                if (brojPlatforme == 0) {
                    idiDesno(1, 0);
                }
            }
            idiDesno(1, 1);

            while (lokalnaVrstaPremaDole < 10 && !parkiran) {

                idiDole(lokalnaVrstaPremaDole, 1);
                if (isparkiraj) {
                    if (lokalnaVrstaPremaDole - 1 == vrstaZaPotjeru) {
                        isparkirajSe(vrstaZaPotjeru, kolonaZaPotjeru);
                    } else {
                        voziloKojePratiPolicijsko.idiDole(lokalnaVrstaPremaDole - 1, 1);
                    }
                }
                if (!potjera && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].isDaLiJePoljeSlobodno())
                        && (this instanceof PolicijskoVoziloInterface) && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].getVozilo() instanceof SpecijalnoVoziloInterface)) {
                    if (((PolicijskoVoziloInterface) this).daLiJeVoziloSaPotjerniceNaPlatformi(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].getVozilo().getRegistarskiBroj())) {
                        voziloKojePratiPolicijsko = Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].getVozilo();
                        voziloKojePratiPolicijsko.pratimPolicijskoVozilo = true;
                        potjera = true;
                        ((RotacijaInterface) this).setRotacija(true);
                        try {
                            ((PolicijskoVoziloInterface) this).evidentirajVoziloSaPotjernice(voziloKojePratiPolicijsko);
                            sleep(rand.nextInt(3_000) + 5_000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        vrstaZaPotjeru = lokalnaVrstaPremaDole;
                        kolonaZaPotjeru = 0;
                        isparkiraj = true;
                    }
                }

                if (!potjera && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].isDaLiJePoljeSlobodno())
                        && (this instanceof PolicijskoVoziloInterface) && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].getVozilo() instanceof SpecijalnoVoziloInterface)) {
                    if (!(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][2].getVozilo() instanceof PolicijskoVoziloInterface)) {
                        if (((PolicijskoVoziloInterface) this).daLiJeVoziloSaPotjerniceNaPlatformi(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].getVozilo().getRegistarskiBroj())) {
                            voziloKojePratiPolicijsko = Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].getVozilo();
                            voziloKojePratiPolicijsko.pratimPolicijskoVozilo = true;
                            potjera = true;
                            ((RotacijaInterface) this).setRotacija(true);
                            idiDesno(lokalnaVrstaPremaDole, 2);
                            try {
                                ((PolicijskoVoziloInterface) this).evidentirajVoziloSaPotjernice(voziloKojePratiPolicijsko);
                                sleep(rand.nextInt(3_000) + 5_000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            vrstaZaPotjeru = lokalnaVrstaPremaDole;
                            kolonaZaPotjeru = 3;
                            isparkiraj = true;
                            idiLijevo(lokalnaVrstaPremaDole, 1);
                        }
                    }

                }

                if ((Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].getTipPolja().equals("Parking"))
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][0].isDaLiJePoljeSlobodno()) && !potjera && !this.ucestvujemUSudaru) {
                    idiLijevo(lokalnaVrstaPremaDole, 0);
                    parkiran = true;
                    this.setVrstaVozilaNaPlatformi(lokalnaVrstaPremaDole);
                    this.setKolonaVozilaNaPlatformi(0);
                    break;
                }
                if ((Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].getTipPolja().equals("Parking"))
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][3].isDaLiJePoljeSlobodno())
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][2].isDaLiJePoljeSlobodno()) && !potjera && !this.ucestvujemUSudaru) {
                    idiDesno(lokalnaVrstaPremaDole, 2);
                    idiDesno(lokalnaVrstaPremaDole, 3);
                    parkiran = true;
                    this.setVrstaVozilaNaPlatformi(lokalnaVrstaPremaDole);
                    this.setKolonaVozilaNaPlatformi(3);
                    break;
                }
                lokalnaVrstaPremaDole++;
            }

            if (!parkiran) {
                idiDesno(9, 2);
                if (lokalnaVrstaPremaDole == 10 && voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiDole(lokalnaVrstaPremaDole - 1, 1);
                }
                idiDesno(9, 3);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiDesno(9, 2);
                }
                idiDesno(9, 4);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiDesno(9, 3);
                }
                idiDesno(9, 5);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiDesno(9, 4);
                }
                idiDesno(9, 6);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiDesno(9, 5);
                }
            }

            while (lokalnaVrstaZaGore > 0 && !parkiran) {
                if (lokalnaVrstaZaGore != 9) {
                    idiGore(lokalnaVrstaZaGore, 6);

                    if (lokalnaVrstaZaGore == 8 && voziloKojePratiPolicijsko != null) {
                        voziloKojePratiPolicijsko.idiDesno(9, 6);
                    }
                    if (potjera && !isparkirajZaDrugiDioPlatforme && lokalnaVrstaZaGore < 8 && voziloKojePratiPolicijsko != null) {
                        voziloKojePratiPolicijsko.idiGore(lokalnaVrstaZaGore + 1, 6);
                    }
                    if (isparkirajZaDrugiDioPlatforme) {
                        if (lokalnaVrstaZaGore + 1 == vrstaZaPotjeru) {
                            isparkirajSe(vrstaZaPotjeru, kolonaZaPotjeru);
                        } else {
                            voziloKojePratiPolicijsko.idiGore(lokalnaVrstaZaGore + 1, 6);
                        }
                    }

                    if (!potjera && !potjeraZaDrugiDioPlatforme && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].isDaLiJePoljeSlobodno())
                            && (this instanceof PolicijskoVoziloInterface) && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].getVozilo() instanceof SpecijalnoVoziloInterface)) {
                        if (((PolicijskoVoziloInterface) this).daLiJeVoziloSaPotjerniceNaPlatformi(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].getVozilo().getRegistarskiBroj())) {
                            voziloKojePratiPolicijsko = Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].getVozilo();
                            potjeraZaDrugiDioPlatforme = true;
                            voziloKojePratiPolicijsko.pratimPolicijskoVozilo = true;
                            ((RotacijaInterface) this).setRotacija(true);
                            try {
                                ((PolicijskoVoziloInterface) this).evidentirajVoziloSaPotjernice(voziloKojePratiPolicijsko);
                                sleep(rand.nextInt(3_000) + 5_000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            vrstaZaPotjeru = lokalnaVrstaZaGore;
                            kolonaZaPotjeru = 7;
                            isparkirajZaDrugiDioPlatforme = true;

                            potjera = true;
                        }
                    }

                    if (!potjera && !potjeraZaDrugiDioPlatforme && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].isDaLiJePoljeSlobodno())
                            && (this instanceof PolicijskoVoziloInterface) && !(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].getVozilo() instanceof SpecijalnoVoziloInterface)) {
                        if (!(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaPremaDole][5].getVozilo() instanceof PolicijskoVoziloInterface)) { // ako vec neko policijsko vozilo nije zauzelo :D vozilo sa potjere
                            if (((PolicijskoVoziloInterface) this).daLiJeVoziloSaPotjerniceNaPlatformi(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].getVozilo().getRegistarskiBroj())) {
                                voziloKojePratiPolicijsko = Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].getVozilo();
                                potjeraZaDrugiDioPlatforme = true;
                                voziloKojePratiPolicijsko.pratimPolicijskoVozilo = true;
                                ((RotacijaInterface) this).setRotacija(true);
                                idiLijevo(lokalnaVrstaZaGore, 5);
                                try {
                                    ((PolicijskoVoziloInterface) this).evidentirajVoziloSaPotjernice(voziloKojePratiPolicijsko);
                                    sleep(rand.nextInt(3_000) + 5_000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                vrstaZaPotjeru = lokalnaVrstaZaGore;
                                kolonaZaPotjeru = 4;
                                isparkirajZaDrugiDioPlatforme = true;
                                idiDesno(lokalnaVrstaZaGore, 6);

                                potjera = true;
                            }
                        }
                    }
                }

                if ((Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].getTipPolja().equals("Parking"))
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][4].isDaLiJePoljeSlobodno())
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][5].isDaLiJePoljeSlobodno()) && !potjera && !this.ucestvujemUSudaru) {
                    idiLijevo(lokalnaVrstaZaGore, 5);
                    idiLijevo(lokalnaVrstaZaGore, 4);
                    parkiran = true;
                    this.setVrstaVozilaNaPlatformi(lokalnaVrstaZaGore);
                    this.setKolonaVozilaNaPlatformi(4);
                    break;
                }
                if ((Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].getTipPolja().equals("Parking"))
                        && (Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaGore][7].isDaLiJePoljeSlobodno()) && !potjera && !this.ucestvujemUSudaru) {
                    idiDesno(lokalnaVrstaZaGore, 7);
                    parkiran = true;
                    this.setVrstaVozilaNaPlatformi(lokalnaVrstaZaGore);
                    this.setKolonaVozilaNaPlatformi(7);
                    break;
                }
                lokalnaVrstaZaGore--;
            }
            //Razmisli o ispod zakomentarisanoj linij jer bubam auta idu platforma puna , jbg u tom redu je i policijsko naleti na auto
            //sa potjere i platforma onda nije vise puna :/ , pa sam uklinio da li je platforma puna, uglavnom fino radi
            //if (!potjera && !parkiran && !Garaza.garaza[brojPlatforme].isDaLiJeZadnjaPlatforma() && Garaza.garaza[brojPlatforme].DaLiJePlatformaPopunjena()) {
            if (!potjera && !parkiran && !Garaza.garaza[brojPlatforme].isDaLiJeZadnjaPlatforma() && !this.ucestvujemUSudaru) {
                idiDesno(1, 7);
                idiNaSljedecuPlatformu();
            }

            if (potjera) {
                idiGore(0, 6);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiGore(1, 6);
                }
                idiLijevo(0, 5);
                if (voziloKojePratiPolicijsko != null) {
                    voziloKojePratiPolicijsko.idiGore(0, 6);
                }
                int kolonaZaNapustanjePlatforme = 4;
                int brojPlatformeZaVoziloKojePratiPolicijsko = 0;
                while (brojPlatforme > (-1)) {
                    if (kolonaZaNapustanjePlatforme < 0) {
                        kolonaZaNapustanjePlatforme = 0;
                    }
                    while ((kolonaZaNapustanjePlatforme) > (-1)) {
                        idiLijevo(0, kolonaZaNapustanjePlatforme);
                        if (kolonaZaNapustanjePlatforme != 6) {
                            voziloKojePratiPolicijsko.idiLijevo(0, kolonaZaNapustanjePlatforme + 1);
                            if ((brojPlatforme == 0) && (Garaza.garaza[brojPlatforme].isDaLiJeZadnjaPlatforma())) { // ovo sam dodao jer kad imam samo jednu platformu onda uzme brojPlatforme + 1 pa ima null pointer. exc.
                                Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                            } else {
                                Garaza.garaza[brojPlatforme + 1].getPlatforma()[0][0].setVozilo(null);
                            }
                        }
                        kolonaZaNapustanjePlatforme--;
                    }
                    if (kolonaZaNapustanjePlatforme == (-1)) {
                        if (this != null) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[0][0].getVozilo() != null && Garaza.garaza[brojPlatforme].getPlatforma()[0][0].getVozilo().equals(this)) {
                                Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                                voziloKojePratiPolicijsko.idiLijevo(0, kolonaZaNapustanjePlatforme + 1);
                                Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                                ispisi();
                            } else {
                                Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                                voziloKojePratiPolicijsko.idiLijevo(0, kolonaZaNapustanjePlatforme + 1);
                                Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                                ispisi();
                            }
                        }
                    }

                    kolonaZaNapustanjePlatforme = 6;
                    brojPlatforme--;
                    brojPlatformeZaVoziloKojePratiPolicijsko = brojPlatforme;
                    voziloKojePratiPolicijsko.setBrojPlatforme(brojPlatformeZaVoziloKojePratiPolicijsko);
                }

                parkiran = true;
                break;
            }
            // nezgodno ovo za zadnjom realizovati radi potjere :D
            if ((Garaza.garaza[brojPlatforme].isDaLiJeZadnjaPlatforma()) && !(parkiran)) {
                if (!Garaza.garaza[brojPlatforme].getPlatforma()[1][6].isDaLiJePoljeSlobodno()) {
                    idiGore(0, 6);
                    if (this != null) {
                        napustiGarazu();
                    }
                    parkiran = true;
                    break;
                }
            }
            if (this.ucestvujemUSudaru) {
                idiGore(0, 6);
                if (this != null) {
                    napustiGarazu();
                }
                parkiran = true;
            }

        }
        this.zapocniParkiranje = false;

    }

    private void napustiGarazu() {
        locker.lock();
        try {
            int lokalnaKolonaZaIzlazakIzGaraze = 5;

            while (brojPlatforme > (-1)) {
                if (lokalnaKolonaZaIzlazakIzGaraze < 0) {
                    lokalnaKolonaZaIzlazakIzGaraze = 0;
                }
                while (!Garaza.garaza[this.brojPlatforme].getPlatforma()[0][lokalnaKolonaZaIzlazakIzGaraze].isDaLiJePoljeSlobodno()) {
                    notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }
                while ((lokalnaKolonaZaIzlazakIzGaraze) > (-1)) {
                    idiLijevo(0, lokalnaKolonaZaIzlazakIzGaraze);
                    lokalnaKolonaZaIzlazakIzGaraze--;
                }
                if (lokalnaKolonaZaIzlazakIzGaraze == (-1) && (this != null)) {
                    if (Garaza.garaza[brojPlatforme].getPlatforma()[0][0].getVozilo().equals(this)) {

                        Garaza.garaza[brojPlatforme].getPlatforma()[0][0].setVozilo(null);
                        ispisi();
                        free.signalAll();
                    }
                }

                lokalnaKolonaZaIzlazakIzGaraze = 6;
                brojPlatforme--;
            }

        } catch (Exception ex) {
            Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            locker.unlock();
        }
    }

    private void idiNaSljedecuPlatformu() {
        locker.lock();

        try {
            if (Garaza.garaza[brojPlatforme].getPlatforma()[1][7].getVozilo().equals(this)) {
                Garaza.garaza[brojPlatforme].getPlatforma()[1][7].setVozilo(null);
                ispisi();
                free.signalAll();
            }
            while (!Garaza.garaza[this.brojPlatforme + 1].getPlatforma()[1][0].isDaLiJePoljeSlobodno()) {
                notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
            }
            brojPlatforme++;
            Garaza.garaza[brojPlatforme].getPlatforma()[1][0].setVozilo(this);
            ispisi();
            free.signalAll();
        } catch (Exception ex) {
            Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.presaoNaSljedecuPlatformu = true;
            locker.unlock();
        }
    }

    boolean daLiJeMogucSudar = false;
    private boolean ucestvujemUUvidjaju = false;

    private void idiDole(int vrsta, int kolona) {
        locker.lock();
        try {

            postaviPrioritete();

            if (!this.ucestvujemUUvidjaju) {
                while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                    sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                    //notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }

                if (!sudar && (!sudarSeVecDesioNaPlatformi) && (vrsta != 9 && vrsta > 1) && !potjera && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].isDaLiJePoljeSlobodno())
                        && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].isDaLiJePoljeSlobodno() && !(Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo() instanceof SpecijalnoVoziloInterface) && !(this instanceof SpecijalnoVoziloInterface))) {
                    if (daLiJeMogucSudar = rand.nextInt(100) < 10) {
                        // System.out.println("SUDAR");
                        //  System.out.println("A:" + vrsta + "_" + kolona);
                        vrstaSudara = vrsta;
                        kolonaSudara = kolona;

                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        udarenoVozilo1 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo();
                        udarenoVozilo2 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo();
                        //System.out.println("UDARENO1: " + udarenoVozilo1);
                        // System.out.println("UDARENO2: " + udarenoVozilo2);
                        vrstaPrvogUdarenogVozila = (vrsta - 1);
                        kolonaPrvogUdarenogVozila = kolona;

                        vrstaDrugogUdarenogVozila = vrsta + 1;
                        kolonaDrugogUdarenogVozila = kolona;

                        sudar = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo().ucestvujemUSudaru = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo().ucestvujemUSudaru = true;
                        sudarSeVecDesioNaPlatformi = true;
                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                            pronadjiPolicijskaVozila();
                            sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                            Garaza.garaza[brojPlatforme].setSudarNaPlatformi(false);
                            break;
                        }
                    }

                }
            }
            while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
            }

            if (vrsta != 9 && Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo() instanceof RotacijaInterface
                    && !pratimPolicijskoVozilo) {
                sleep(1000);
                while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                    notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }
                free.signalAll();

            }

            //synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona]) {
            synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].setVozilo(null);
                ispisi();
            }
            free.signalAll();
            //}

        } catch (InterruptedException ex) {
            Logger.getLogger(Vozilo.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            locker.unlock();
        }
    }

    private void idiGore(int vrsta, int kolona) {
        locker.lock();
        try {
            postaviPrioritete();
            if (vrsta < 0 || kolona < 0) {
                vrsta = 0;
                kolona = 0;
            }

            if (!ucestvujemUUvidjaju) {

                while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                    sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                }
                if (!sudar && (!sudarSeVecDesioNaPlatformi) && (vrsta > 2) && !potjera && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].isDaLiJePoljeSlobodno())
                        && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].isDaLiJePoljeSlobodno() && !(Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo() instanceof SpecijalnoVoziloInterface) && !(this instanceof SpecijalnoVoziloInterface))) {
                    if (daLiJeMogucSudar = rand.nextInt(100) < 30) {
                        System.out.println("SUDAR");
                        System.out.println("A:" + vrsta + "_" + kolona);
                        vrstaSudara = vrsta;
                        kolonaSudara = kolona;

                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        udarenoVozilo1 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo();
                        udarenoVozilo2 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo();
                        //System.out.println("UDARENO1: " + udarenoVozilo1);
                        // System.out.println("UDARENO2: " + udarenoVozilo2);
                        vrstaPrvogUdarenogVozila = (vrsta + 1);
                        kolonaPrvogUdarenogVozila = kolona;

                        vrstaDrugogUdarenogVozila = (vrsta - 1);
                        kolonaDrugogUdarenogVozila = kolona;

                        sudar = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].getVozilo().ucestvujemUSudaru = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo().ucestvujemUSudaru = true;
                        sudarSeVecDesioNaPlatformi = true;
                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                            // System.out.println("UPSSS");
                            pronadjiPolicijskaVozila();
                            // System.out.println("KKKK KK K");
                            sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                            Garaza.garaza[brojPlatforme].setSudarNaPlatformi(false);
                            break;
                        }
                    }

                }

            }
            while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
            }

            if (vrsta != 0 && Garaza.garaza[brojPlatforme].getPlatforma()[vrsta - 1][kolona].getVozilo() instanceof RotacijaInterface
                    && !pratimPolicijskoVozilo) {
                //sleep(1000);
                sleep(400);
                while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                    notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }
                free.signalAll();
            }

            if (vrsta == 9) {
                // synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                ispisi();
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(null);
                //  }
                // free.signalAll();
            } else {
                // synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona]) {
                // synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta + 1][kolona].setVozilo(null);
                ispisi();
                // }
                //}
                //  free.signalAll();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Vozilo.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            locker.unlock();
        }
    }

    private void idiLijevo(int vrsta, int kolona) {
        locker.lock();
        try {
            // locker.lock();
            postaviPrioritete();
            if (!ucestvujemUUvidjaju) {
                //  if (sudar) {
                //System.out.println("vec je sudar!");
                while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                    sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                }
                // 

                if (!sudar && (!sudarSeVecDesioNaPlatformi) && (kolona > 2 && kolona < 7) && !potjera && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].isDaLiJePoljeSlobodno())
                        && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].isDaLiJePoljeSlobodno() && (kolona != 5) && !(Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo() instanceof SpecijalnoVoziloInterface) && !(this instanceof SpecijalnoVoziloInterface)) && (vrsta == 0)) {
                    // System.out.println("probo");
                    if (daLiJeMogucSudar = rand.nextInt(100) < 25) {

                        //System.out.println("SUDAR: " + vrsta + "_" + kolona);
                        // vrstaSudara = vrsta;
                        // kolonaSudara = kolona;
                        // obrnuto udareno vozilo jedan i udareno da da bih se prilagodio funkciji idi desno da nemam dupliranje koda
                        //udarenoVozilo1 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo();
                        //udarenoVozilo2 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo();
                        udarenoVozilo1 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo();
                        udarenoVozilo2 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo();

                        vrstaPrvogUdarenogVozila = vrsta;
                        //kolonaPrvogUdarenogVozila = (kolona - 1);
                        kolonaPrvogUdarenogVozila = (kolona + 1);

                        vrstaDrugogUdarenogVozila = vrsta;
                        //kolonaDrugogUdarenogVozila = (kolona + 1);
                        kolonaDrugogUdarenogVozila = (kolona - 1);

                        sudar = true;
                        //Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo().ucestvujemUSudaru = true;
                        //Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo().ucestvujemUSudaru = true;
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1] != null) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo().ucestvujemUSudaru = true;
                        }
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1] != null) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo().ucestvujemUSudaru = true;
                        }
                        sudarSeVecDesioNaPlatformi = true;
                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                            pronadjiPolicijskaVozila();
                            sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                            Garaza.garaza[brojPlatforme].setSudarNaPlatformi(false);
                            break;
                        }
                    }

                }

            }

            while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
            }

            if (kolona != 0 && Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo() instanceof RotacijaInterface
                    && pratimPolicijskoVozilo) {
                sleep(400);
                while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                    notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }
                free.signalAll();
            }

            //synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1]) {
            synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].setVozilo(null);
                ispisi();
            }
            // }
            free.signalAll();

            if (brojPlatforme == 0 && vrsta == 0 && kolona == 0) {
                if (this.izlazakPetnaestPostoVozilaIzGaraze) {
                    vozilaZaNaplatuParkinga.add(this);
                }
                if (vozilaZaNaplatuParkinga.size() == SimulacijaForm.brojVozilaKojaSuNapustilaGarazu) {
                    SimulacijaForm.bDownloadCSV.setEnabled(true);
                }
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Vozilo.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            locker.unlock();
        }
    }

    private void idiDesno(int vrsta, int kolona) {

        locker.lock();
        try {

            postaviPrioritete();

            if (!ucestvujemUUvidjaju) {
                while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                    sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                }

                if (!sudar && (!sudarSeVecDesioNaPlatformi) && (kolona > 2 && kolona < 6) && !potjera && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].isDaLiJePoljeSlobodno())
                        && (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].isDaLiJePoljeSlobodno() && !(Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo() instanceof SpecijalnoVoziloInterface) && !(this instanceof SpecijalnoVoziloInterface)) && (vrsta < 2 || vrsta > 8)) {
                    if (daLiJeMogucSudar = rand.nextInt(100) < 10) {
                        //System.out.println("SUDAR");
                        //System.out.println("A:" + vrsta + "_" + kolona);
                        vrstaSudara = vrsta;
                        kolonaSudara = kolona;

                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        udarenoVozilo1 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo();
                        udarenoVozilo2 = Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo();
                        vrstaPrvogUdarenogVozila = vrsta;
                        kolonaPrvogUdarenogVozila = (kolona - 1);

                        vrstaDrugogUdarenogVozila = vrsta;
                        kolonaDrugogUdarenogVozila = (kolona + 1);

                        sudar = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].getVozilo().ucestvujemUSudaru = true;
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo().ucestvujemUSudaru = true;
                        sudarSeVecDesioNaPlatformi = true;
                        Garaza.garaza[brojPlatforme].setSudarNaPlatformi(true);
                        while (sudar && Garaza.garaza[brojPlatforme].isSudarNaPlatformi()) {
                            pronadjiPolicijskaVozila();
                            sleep(Garaza.garaza[brojPlatforme].getUspavajVozila());
                            Garaza.garaza[brojPlatforme].setSudarNaPlatformi(false);
                            break;
                        }
                    }

                }
            }

            while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
            }

            if (kolona != 7 && Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona + 1].getVozilo() instanceof RotacijaInterface
                    && !pratimPolicijskoVozilo) {
                sleep(400);
                while (!Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].isDaLiJePoljeSlobodno()) {
                    notfree.await(SACEKAJ, TimeUnit.MILLISECONDS);
                }
                free.signalAll();
            }

            if (kolona == 0) { // za ulazak u garazu //caprko si ovuda za sada se cini dobro
                // synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                ispisi();
                // sleep(100); //vrati ovo :D kad zavrsis sudar
                //kolona = 1;
                // Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].setVozilo(null);
                // ispisi();
                //}
                //free.signalAll();
            } else {

                // synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1]) {
                synchronized (Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona]) {
                    Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona].setVozilo(this);
                    Garaza.garaza[brojPlatforme].getPlatforma()[vrsta][kolona - 1].setVozilo(null);
                    ispisi();
                }
            }
            free.signalAll();
            //}
            //}
        } catch (Exception ex) {
            Logger.getLogger(Vozilo.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            locker.unlock();
        }
    }

    private void postaviPrioritete() {
        if (this instanceof SpecijalnoVoziloInterface && this.ucestvujemUUvidjaju) {
            if (this instanceof SanitetskoVoziloInterface) {
                SACEKAJ = 400;
            }
            if (this instanceof VatrogasnoVoziloInterface) {
                SACEKAJ = 425;
            }
            if (this instanceof PolicijskoVoziloInterface) {
                SACEKAJ = 450;
            }
        } else {
            SACEKAJ = 500;
        }
    }

    private void pronadjiPolicijskaVozila() {

        Vozilo policijskoVoziloSaPlatforme = null;
        Vozilo sanitetskoVoziloSaPlatforme = null;
        Vozilo vatrogasnoVoziloSaPlatforme = null;

        ArrayList<Vozilo> parkiranaVozilaNaPlatformi = Garaza.garaza[brojPlatforme].svaParkiranaVozilaNaPlatformi();

        for (Vozilo v : parkiranaVozilaNaPlatformi) {
            if (policijskoVoziloSaPlatforme != null && sanitetskoVoziloSaPlatforme != null && vatrogasnoVoziloSaPlatforme != null) {
                break;
            }

            if (v instanceof PolicijskoVoziloInterface) {
                policijskoVoziloSaPlatforme = v;
                //policijskoVoziloSaPlatforme.setVrstaVozilaNaPlatformi(v.getVrstaVozilaNaPlatformi());
                //policijskoVoziloSaPlatforme.setKolonaVozilaNaPlatformi(v.getKolonaVozilaNaPlatformi());
                // policijskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            } else if (v instanceof SanitetskoVoziloInterface) {
                sanitetskoVoziloSaPlatforme = v;
                //sanitetskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            } else if (v instanceof VatrogasnoVoziloInterface) {
                vatrogasnoVoziloSaPlatforme = v;
                //vatrogasnoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            }
        }
        // H A R D C O D E
  /*      if (!Garaza.garaza[brojPlatforme].getPlatforma()[4][7].isDaLiJePoljeSlobodno()) {
         policijskoVoziloSaPlatforme = Garaza.garaza[brojPlatforme].getPlatforma()[4][7].getVozilo();
         policijskoVoziloSaPlatforme.setVrstaVozilaNaPlatformi(4);
         policijskoVoziloSaPlatforme.setKolonaVozilaNaPlatformi(7);
         policijskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;

         //System.out.println("POLICIJSKO VOZILO: " + policijskoVoziloSaPlatforme);
         }

         if (!Garaza.garaza[brojPlatforme].getPlatforma()[8][7].isDaLiJePoljeSlobodno()) {
         sanitetskoVoziloSaPlatforme = Garaza.garaza[brojPlatforme].getPlatforma()[8][7].getVozilo();
         sanitetskoVoziloSaPlatforme.setVrstaVozilaNaPlatformi(8);
         sanitetskoVoziloSaPlatforme.setKolonaVozilaNaPlatformi(7);
         sanitetskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;

         // System.out.println("SANITETSKO VOZILO: " + policijskoVoziloSaPlatforme);
         }
         /*     if (!Garaza.garaza[brojPlatforme].getPlatforma()[3][3].isDaLiJePoljeSlobodno()) {
         vatrogasnoVoziloSaPlatforme = Garaza.garaza[brojPlatforme].getPlatforma()[3][3].getVozilo();
         vatrogasnoVoziloSaPlatforme.setVrstaVozilaNaPlatformi(3);
         vatrogasnoVoziloSaPlatforme.setKolonaVozilaNaPlatformi(3);
         vatrogasnoVoziloSaPlatforme.ucestvujemUUvidjaju = true;

         //System.out.println("VATROGASNO VOZILO: " + policijskoVoziloSaPlatforme);
         }
         */
        ////////////////////////////
        if (policijskoVoziloSaPlatforme != null) {
            policijskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            policija = new Thread(policijskoVoziloSaPlatforme);
            policijskoVoziloSaPlatforme.obradaSudara = true;
            // System.out.println("KOORDINATE POLICISJKOG VOZILA:" + policijskoVoziloSaPlatforme.getVrstaVozilaNaPlatformi() + " " + policijskoVoziloSaPlatforme.getKolonaVozilaNaPlatformi());
            // System.out.println("POLICIJSKO VOZILO: " + this);
            policija.start();
        }
        if (sanitetskoVoziloSaPlatforme != null) {
            sanitetskoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            hitna = new Thread(sanitetskoVoziloSaPlatforme);
            sanitetskoVoziloSaPlatforme.obradaSudara = true;
            //System.out.println("KOORDINATE SANITETSKOg VOZILA:" + sanitetskoVoziloSaPlatforme.getVrstaVozilaNaPlatformi() + " " + sanitetskoVoziloSaPlatforme.getKolonaVozilaNaPlatformi());
            // System.out.println("SANITETSKO VOZILO: " + this);
            hitna.start();
        }
        if (vatrogasnoVoziloSaPlatforme != null) {
            vatrogasnoVoziloSaPlatforme.ucestvujemUUvidjaju = true;
            vatrogasno = new Thread(vatrogasnoVoziloSaPlatforme);
            vatrogasnoVoziloSaPlatforme.obradaSudara = true;
            //  System.out.println("KOORDINATE VATROGASNOG VOZILA:" + vatrogasnoVoziloSaPlatforme.getVrstaVozilaNaPlatformi() + " " + vatrogasnoVoziloSaPlatforme.getKolonaVozilaNaPlatformi());
            // System.out.println("VATROGASNO VOZILO: " + this);
            vatrogasno.start();
        }

        //sudar = false;
    }

    private void obradiSudarNaPlatformi() {

        //System.out.println("OBRADA UDARENO1: " + Garaza.garaza[brojPlatforme].getPlatforma()[vrstaPrvogUdarenogVozila][kolonaPrvogUdarenogVozila].getVozilo());
        //System.out.println("OBRADA UDARENO2: " + Garaza.garaza[brojPlatforme].getPlatforma()[vrstaDrugogUdarenogVozila][kolonaDrugogUdarenogVozila].getVozilo());
        int pocetnaVrstaSpecijalnogVozila = this.vrstaVozilaNaPlatformi;
        int pocetnaKolonaSpecijalnogVozila = this.kolonaVozilaNaPlatformi;

        //if ((vrstaDrugogUdarenogVozila > 0 && vrstaDrugogUdarenogVozila < 10) && (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 3)) {
        // OVO JE ZA IDI DOLE NE BRISI !!!
        if ((vrstaDrugogUdarenogVozila > 1 && vrstaDrugogUdarenogVozila < 10) && (kolonaDrugogUdarenogVozila == 1)) {
            if (this.kolonaVozilaNaPlatformi == 0) { //ako je specijalno vozilo u prvoj koloni

                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()
                        && !((vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 1) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 1)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 1))) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 1);
                }
                if ((this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 1) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 1)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 1)) {
                    System.out.println("VRSIM UVIDJAJ ALI NEMGU IZACI JER SE PORED MENE DESIO SUDAR");
                    if (this instanceof SanitetskoVoziloInterface) {
                        System.out.println("SANITETSKO");
                    }
                    if (this instanceof PolicijskoVoziloInterface) {
                        System.out.println("POLICIJSKO");
                    }
                    if (this instanceof VatrogasnoVoziloInterface) {
                        System.out.println("VATROGASNO");
                    }
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 2].isDaLiJePoljeSlobodno()) { // za kolonu broj 2
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 2);

                    if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {

                        while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    break;
                                }
                            }
                        }
                        // System.out.println("uvidjaJJJ");
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);

                    } else if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) { //ako je spec. vozilo iza udarenog
                        while (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    break; // isto neko zaobilazenje ili slicno
                                }
                            }
                        }
                        // System.out.println("uvidjaJJJ");
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][2].setVozilo(null);
                    }

                } else {
                    if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                        while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][1].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 1);
                                } else {
                                    break; // isto neko zaobilazenje
                                }
                            }

                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrstaVozilaNaPlatformi + 1][1].setVozilo(null);

                    } else if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) { // iza sam udarenog vozila ali sada sam u prvoj koloni
                        while (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][1].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 1);
                                } else {
                                    break; // isto neko zaobilazenje
                                }
                            }

                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrstaVozilaNaPlatformi - 1][1].setVozilo(null);
                    }
                }

            }

            if (this.kolonaVozilaNaPlatformi == 3) { // ako je spec. voz. u koloni 3
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 2);
                }

                if ((this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 1) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 1)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 1)) {
                    System.out.println("VRSIM UVIDJAJ ALI NEMGU IZACI JER SE PORED MENE DESIO SUDAR");
                    if (this instanceof SanitetskoVoziloInterface) {
                        System.out.println("SANITETSKO");
                    }
                    if (this instanceof PolicijskoVoziloInterface) {
                        System.out.println("POLICIJSKO");
                    }
                    if (this instanceof VatrogasnoVoziloInterface) {
                        System.out.println("VATROGASNO");
                    }
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                }

                if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                    while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi--;
                        if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiGore(this.vrstaVozilaNaPlatformi, 2);
                            } else {
                                break;
                            }
                        }
                    }

                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);

                } else if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                    while (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi++;
                        if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                            } else {
                                break;
                            }
                        }
                    }
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][2].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                }

            }

            if (this.kolonaVozilaNaPlatformi == 4) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 5);
                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < 9) {
                    while (this.vrstaVozilaNaPlatformi < 9) {
                        this.vrstaVozilaNaPlatformi++;
                        if (this.vrstaVozilaNaPlatformi < 9) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }
                    }

                    while (this.kolonaZaKretanjeLijevoKodSudara > 1) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                            this.idiLijevo(8, kolonaZaKretanjeLijevoKodSudara);
                            this.kolonaZaKretanjeLijevoKodSudara--;
                        } else {
                            break;
                        }
                    }

                    if (this.vrstaVozilaNaPlatformi == 9) {
                        this.vrstaVozilaNaPlatformi = 8;
                    }
                    if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                            && !(vrstaSudara == 8)) {
                        while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    //((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    break;
                                }
                            }
                        }
                    }

                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeLijevoKodSudara == 1 || kolonaZaKretanjeLijevoKodSudara == 2 || kolonaZaKretanjeLijevoKodSudara == 3 || kolonaZaKretanjeLijevoKodSudara == 4)) {
                        Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else {
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                }
            }

            if (this.kolonaVozilaNaPlatformi == 7 && !(this.vrstaVozilaNaPlatformi == 9)) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 6);
                    //}
                    if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 2].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        this.idiLijevo(this.vrstaVozilaNaPlatformi, 5);
                    } else { // obezbjedi da se krece kolonom 6
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][6].setVozilo(null);
                    }
                    if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < 9) {
                        while (this.vrstaVozilaNaPlatformi < 9) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < 9) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                                } else {
                                    break;
                                }
                            }
                        }

                    }
                    while (!this.vecSamIzvrsioUvidjaj && this.kolonaZaKretanjeLijevoKodSudara > 1) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                            this.idiLijevo(8, kolonaZaKretanjeLijevoKodSudara);
                            this.kolonaZaKretanjeLijevoKodSudara--;
                        } else {
                            break;
                        }
                    }
                    if (this.vrstaVozilaNaPlatformi == 9) {
                        this.vrstaVozilaNaPlatformi = 8;
                    }

                    if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                            && (!(vrstaSudara == 8) || !(vrstaDrugogUdarenogVozila == 8) || !(vrstaDrugogUdarenogVozila == 9))) {
                        while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeLijevoKodSudara == 1 || kolonaZaKretanjeLijevoKodSudara == 2 || kolonaZaKretanjeLijevoKodSudara == 3 || kolonaZaKretanjeLijevoKodSudara == 4)) {
                        Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else {
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }

            if (this.vrstaVozilaNaPlatformi == 9 && this.kolonaVozilaNaPlatformi == 7) { // ako je kolona 7 i vrsta 9

                ((RotacijaInterface) this).setRotacija(true);
                if (Garaza.garaza[brojPlatforme].getPlatforma()[9][6].isDaLiJePoljeSlobodno()
                        && Garaza.garaza[brojPlatforme].getPlatforma()[8][6].isDaLiJePoljeSlobodno()
                        && Garaza.garaza[brojPlatforme].getPlatforma()[8][5].isDaLiJePoljeSlobodno()) {
                    this.idiLijevo(9, 6);
                    this.idiGore(8, 6);
                    this.idiLijevo(8, 5);
                    // } 

                    while (this.kolonaZaKretanjeLijevoKodSudara > 1) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                            this.idiLijevo(8, kolonaZaKretanjeLijevoKodSudara);
                            this.kolonaZaKretanjeLijevoKodSudara--;
                        } else {
                            break;
                        }
                    }

                    if (this.vrstaVozilaNaPlatformi == 9) {
                        this.vrstaVozilaNaPlatformi = 8;
                    }
                    if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                            && (!(vrstaSudara == 8) || !(vrstaDrugogUdarenogVozila == 8) || !(vrstaDrugogUdarenogVozila == 9))) {
                        while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeLijevoKodSudara == 1 || kolonaZaKretanjeLijevoKodSudara == 2 || kolonaZaKretanjeLijevoKodSudara == 3 || kolonaZaKretanjeLijevoKodSudara == 4)) {
                        Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else {
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                } else { // ako nisi uspio izac jbg// malo jos ovo doraditi oko izlaska :S
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
                return;
            }

        }

        // ZA IDI DESNO
        if (vrstaDrugogUdarenogVozila == 9 && (kolonaDrugogUdarenogVozila > 2 && kolonaDrugogUdarenogVozila < 7)) {

            if (this.kolonaVozilaNaPlatformi == 0 && this.vrstaVozilaNaPlatformi != 9) { //ako je specijalno vozilo u prvoj koloni

                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 1);

                    // jos malo poraditi da se krece kolonom 1 ako je kolona 2 zauzeta 
                    if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 2].isDaLiJePoljeSlobodno()) { // za kolonu broj 2
                        ((RotacijaInterface) this).setRotacija(true);
                        this.idiDesno(this.vrstaVozilaNaPlatformi, 2);
                    } else {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                    }
                    if (!this.vecSamIzvrsioUvidjaj && (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila)) {
                        while (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    evidentirajSudar();
                                    ((RotacijaInterface) this).setRotacija(true);
                                    break;
                                }
                            }
                        }
                        if (this.vrstaVozilaNaPlatformi == 9) {
                            this.vrstaVozilaNaPlatformi = 8;
                        }

                        // if (kolonaPrvogUdarenogVozila > 2 && kolonaPrvogUdarenogVozila < 5) {
                        // if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                        //    this.idiDole(vrstaSudara, kolonaSudara);
                        // }
                        while (!this.vecSamIzvrsioUvidjaj && (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila)) {
                            this.kolonaZaKretanjeDesnoKodSudara++;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno()
                                    && (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila)) {
                                idiDesno(this.vrstaVozilaNaPlatformi, this.kolonaZaKretanjeDesnoKodSudara);
                            } else {
                                break;
                            }
                        }
                        if (this.vrstaVozilaNaPlatformi == 8 && kolonaZaKretanjeDesnoKodSudara == 2) {
                            evidentirajSudar();
                            Garaza.garaza[brojPlatforme].getPlatforma()[8][2].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }

                        if (this.vrstaVozilaNaPlatformi == 8 && ((kolonaZaKretanjeDesnoKodSudara == 3) || (kolonaZaKretanjeDesnoKodSudara == 4) || (kolonaZaKretanjeDesnoKodSudara == 5))) {
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } //else if (kolonaPrvogUdarenogVozila > 0 && kolonaPrvogUdarenogVozila < 3) {
                        else {
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][2].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }
                    // if (this.vrstaVozilaNaPlatformi == 9) {
                    //     this.vrstaVozilaNaPlatformi = 8;
                    //  }
                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }

            if (this.kolonaVozilaNaPlatformi == 0 && this.vrstaVozilaNaPlatformi == 9) {
                if (!Garaza.garaza[brojPlatforme].getPlatforma()[9][1].isDaLiJePoljeSlobodno() || (this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 2) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 2)) {
                    //|| (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 2)) {
                    ((RotacijaInterface) this).setRotacija(true);
                    System.out.println("VRSIM UVIDJAJ ALI NEMGU IZACI JER SE PORED MENE DESIO SUDAR");
                    if (this instanceof SanitetskoVoziloInterface) {
                        System.out.println("SANITETSKO");
                    }
                    if (this instanceof PolicijskoVoziloInterface) {
                        System.out.println("POLICIJSKO");
                    }
                    if (this instanceof VatrogasnoVoziloInterface) {
                        System.out.println("VATROGASNO");
                    }
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);

                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(9, 1);

                    while (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[9][this.kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno()) {
                            this.idiDesno(9, kolonaZaKretanjeDesnoKodSudara);
                            this.kolonaZaKretanjeDesnoKodSudara++;
                        } else {
                            break;
                        }
                    }
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    if (this.kolonaZaKretanjeDesnoKodSudara == 1) {
                        Garaza.garaza[brojPlatforme].getPlatforma()[9][this.kolonaZaKretanjeDesnoKodSudara].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else {
                        Garaza.garaza[brojPlatforme].getPlatforma()[9][this.kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }

                }
            }

            if (this.kolonaVozilaNaPlatformi == 3) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 2);
                } else { // izvrsi sudar na licu mjesta
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                    while (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi++;
                        if (this.vrstaVozilaNaPlatformi < vrstaPrvogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                            } else {
                                evidentirajSudar();
                                ((RotacijaInterface) this).setRotacija(false);
                                break;
                            }
                        }
                    }
                    if (this.vrstaVozilaNaPlatformi == 9) {
                        this.vrstaVozilaNaPlatformi = 8;
                    }

                    // if (kolonaPrvogUdarenogVozila > 2 && kolonaPrvogUdarenogVozila < 5) {
                    // if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                    //    this.idiDole(vrstaSudara, kolonaSudara);
                    // }
                    while (!this.vecSamIzvrsioUvidjaj && (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila)) {
                        this.kolonaZaKretanjeDesnoKodSudara++;
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno()
                                && (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila)) {
                            idiDesno(this.vrstaVozilaNaPlatformi, this.kolonaZaKretanjeDesnoKodSudara);
                        } else {
                            break;
                        }
                    }
                    if (this.vrstaVozilaNaPlatformi == 8 && ((kolonaZaKretanjeDesnoKodSudara == 3) || (kolonaZaKretanjeDesnoKodSudara == 4) || (kolonaZaKretanjeDesnoKodSudara == 5))) {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } //else if (kolonaPrvogUdarenogVozila > 0 && kolonaPrvogUdarenogVozila < 3) {
                    else {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                }

            }
            if (this.kolonaVozilaNaPlatformi == 4) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 5);
                } else { // izvrsi sudar na licu mjesta
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                    while (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi++;
                        if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                evidentirajSudar();
                                ((RotacijaInterface) this).setRotacija(false);
                                break;
                            }
                        }
                    }

                    while (!this.vecSamIzvrsioUvidjaj && this.kolonaZaKretanjeLijevoKodSudara > kolonaDrugogUdarenogVozila) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][this.kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                            idiLijevo(8, this.kolonaZaKretanjeLijevoKodSudara);
                            this.kolonaZaKretanjeLijevoKodSudara--;
                        } else {
                            break;
                        }
                    }
                    if (kolonaDrugogUdarenogVozila > 2 && kolonaDrugogUdarenogVozila < 4) {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else if (kolonaDrugogUdarenogVozila > 3 && kolonaPrvogUdarenogVozila < 7) {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][5].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                }

            }
            if (this.kolonaVozilaNaPlatformi == 7 && this.vrstaVozilaNaPlatformi != 9) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 6);
                    // } else { // izvrsi sudar na licu mjesta
                    //   ((RotacijaInterface) this).setRotacija(true);
                    // evidentirajSudar();
                    //((RotacijaInterface) this).setRotacija(false);
                    //}

                    if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 2].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        this.idiLijevo(this.vrstaVozilaNaPlatformi, 5);

                        if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                            while (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                                this.vrstaVozilaNaPlatformi++;
                                if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                                    if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                        ((RotacijaInterface) this).setRotacija(true);
                                        this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                                    } else {
                                        break;
                                    }
                                }
                            }

                            if (this.vrstaVozilaNaPlatformi == 9) {
                                this.vrstaVozilaNaPlatformi = 8;
                            }

                            while (this.kolonaZaKretanjeLijevoKodSudara > kolonaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                                    idiLijevo(this.vrstaVozilaNaPlatformi, this.kolonaZaKretanjeLijevoKodSudara);
                                    this.kolonaZaKretanjeLijevoKodSudara--;
                                } else {
                                    break;
                                }
                            }
                            if (kolonaDrugogUdarenogVozila > 2 && kolonaDrugogUdarenogVozila < 4) {
                                evidentirajSudar();
                                ((RotacijaInterface) this).setRotacija(false);
                                Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            } else if (kolonaDrugogUdarenogVozila > 3 && kolonaPrvogUdarenogVozila < 7) {
                                evidentirajSudar();
                                ((RotacijaInterface) this).setRotacija(false);
                                if (kolonaZaKretanjeLijevoKodSudara == 4 && this.vrstaVozilaNaPlatformi == 8) {
                                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].setVozilo(null);
                                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                                } else {
                                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][5].setVozilo(null);
                                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                                }
                            }
                        }

                    }

                } else { // malo jos bolje doraditi  da se krece kolonom 6
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (this.kolonaVozilaNaPlatformi == 7 && this.vrstaVozilaNaPlatformi == 9) {

                    if (Garaza.garaza[brojPlatforme].getPlatforma()[9][6].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        idiLijevo(9, 6);
                    } else {
                        ((RotacijaInterface) this).setRotacija(true);
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                    }
                    if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[9][5].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        idiLijevo(9, 5);
                    } else {
                        ((RotacijaInterface) this).setRotacija(true);
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[9][6].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                    while (!this.vecSamIzvrsioUvidjaj && this.kolonaZaKretanjeLijevoKodSudara > kolonaDrugogUdarenogVozila) {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[9][this.kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                            idiLijevo(9, this.kolonaZaKretanjeLijevoKodSudara);
                            this.kolonaZaKretanjeLijevoKodSudara--;
                        } else {
                            break;
                        }

                    }
                    if (!vecSamIzvrsioUvidjaj) {
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[9][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                }
            } else { // izvrsi sudar na licu mjesta
                ((RotacijaInterface) this).setRotacija(true);
                evidentirajSudar();
                ((RotacijaInterface) this).setRotacija(false);
            }

        }
        //ZA IDI DESNO i ZA IDI LIJEVO
        if (vrstaDrugogUdarenogVozila == 1 || vrstaDrugogUdarenogVozila == 0) {
            if (this.kolonaVozilaNaPlatformi == 0) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiDesno(this.vrstaVozilaNaPlatformi, 1);
                    // } else {
                    //     ((RotacijaInterface) this).setRotacija(true);
                    //    evidentirajSudar();
                    //    ((RotacijaInterface) this).setRotacija(false);
                    //}
                    if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        idiDesno(this.vrstaVozilaNaPlatformi, 2);

                        if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 5) {
                            while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                                this.vrstaVozilaNaPlatformi--;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()
                                        && !((this.vrstaVozilaNaPlatformi == 1 && kolonaSudara == 2)) && !(this.vrstaVozilaNaPlatformi == 0)) {  //&& kolonaSudara == 2))) { 
                                    idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    evidentirajSudar();
                                    ((RotacijaInterface) this).setRotacija(false);
                                    break;
                                }
                            }
                            System.out.println("THISSSS VRSTA VOZILA : " + this.vrstaVozilaNaPlatformi);
                           // if(this.vrstaVozilaNaPlatformi == 0){

                            //}
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);

                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } else if (kolonaDrugogUdarenogVozila > 4 && kolonaDrugogUdarenogVozila < 7) {
                            while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                                this.vrstaVozilaNaPlatformi--;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                    idiGore(this.vrstaVozilaNaPlatformi, 2);
                                } else {
                                    evidentirajSudar();
                                    ((RotacijaInterface) this).setRotacija(false);
                                    break;
                                }
                            }
                            System.out.println("KOLONA PRVOG UDARENOG : " + kolonaPrvogUdarenogVozila);
                            System.out.println("VRSTA VOZILA: " + this.vrstaVozilaNaPlatformi);
                            while (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila) {
                                this.kolonaZaKretanjeDesnoKodSudara++;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno()) {
                                    idiDesno(this.vrstaVozilaNaPlatformi, kolonaZaKretanjeDesnoKodSudara);
                                } else {
                                    break;
                                }
                            }
                            System.out.println("KOLONA ZA KRETANJE DESNO : " + this.kolonaZaKretanjeDesnoKodSudara);
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            //if (this.vrstaVozilaNaPlatformi == 1) {
                            if (this.vrstaVozilaNaPlatformi == 1 && this.kolonaZaKretanjeDesnoKodSudara > 2) {
                                Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            } // if (this.vrstaVozilaNaPlatformi == 1) { 
                            //    Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                            //    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            //} 
                            else {
                                Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            }
                        }

                    } else { // obezbjdi da ide prvom kolonom
                        ((RotacijaInterface) this).setRotacija(true);
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][1].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }
            if (this.kolonaVozilaNaPlatformi == 3) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiLijevo(this.vrstaVozilaNaPlatformi, 2);

                    if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 5) {
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()
                                    && !(this.vrstaVozilaNaPlatformi == 1 && kolonaSudara == 2) && !(this.vrstaVozilaNaPlatformi == 0)) {
                                idiGore(this.vrstaVozilaNaPlatformi, 2);
                            } else {
                                break;
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else if (kolonaDrugogUdarenogVozila > 4 && kolonaDrugogUdarenogVozila < 7) {
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].isDaLiJePoljeSlobodno()) {
                                idiGore(this.vrstaVozilaNaPlatformi, 2);
                            } else {
                                break;
                            }
                        }
                        while (this.kolonaZaKretanjeDesnoKodSudara < kolonaPrvogUdarenogVozila) {
                            this.kolonaZaKretanjeDesnoKodSudara++;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno()) {
                                idiDesno(this.vrstaVozilaNaPlatformi, kolonaZaKretanjeDesnoKodSudara);
                            } else {
                                break;
                            }
                        }
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        //if (this.vrstaVozilaNaPlatformi == 1) {
                        if (this.vrstaVozilaNaPlatformi == 1 && this.kolonaZaKretanjeDesnoKodSudara > 2) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } // if (this.vrstaVozilaNaPlatformi == 1) { 
                        //    Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                        //    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        //} 
                        else {
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][2].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }

                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }
            if (this.kolonaVozilaNaPlatformi == 4) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiDesno(this.vrstaVozilaNaPlatformi, 5);

                    //if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 5) {
                    if (kolonaDrugogUdarenogVozila > 4 && kolonaDrugogUdarenogVozila < 7) {
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (!(this.vrstaVozilaNaPlatformi == 0 && kolonaSudara == 5) && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()
                                    && !(this.vrstaVozilaNaPlatformi == 1 && kolonaSudara == 5)) {
                                idiGore(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 5) {
                        while (this.vrstaVozilaNaPlatformi > 1) {
                            this.vrstaVozilaNaPlatformi--;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno() && !(this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 5)) {
                                idiGore(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }
                        if (this.vrstaVozilaNaPlatformi == -1 && vrstaDrugogUdarenogVozila == 0) {
                            this.vrstaVozilaNaPlatformi = 0;
                        }

                        if (this.vrstaVozilaNaPlatformi == 0 && vrstaDrugogUdarenogVozila == 1) {
                            this.vrstaVozilaNaPlatformi = 1;
                        }
                        if (this.vrstaVozilaNaPlatformi == 0) {
                            this.vrstaVozilaNaPlatformi = 1;
                        }

                        while (this.kolonaZaKretanjeLijevoKodSudara > kolonaDrugogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                                idiLijevo(this.vrstaVozilaNaPlatformi, kolonaZaKretanjeLijevoKodSudara);
                                this.kolonaZaKretanjeLijevoKodSudara--;
                            } else {
                                break;
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);

                        if (this.vrstaVozilaNaPlatformi == 1) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } else {
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }

                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }
            if (this.kolonaVozilaNaPlatformi == 7) {

                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][6].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiLijevo(this.vrstaVozilaNaPlatformi, 6);
                } else { // napravi kretaje za 6 kolonu
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiLijevo(this.vrstaVozilaNaPlatformi, 5);

                    //if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 5) {
                    if (kolonaDrugogUdarenogVozila > 3 && kolonaDrugogUdarenogVozila < 7) {
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()
                                    && !(this.vrstaVozilaNaPlatformi == 1 && kolonaSudara == 5) && !(this.vrstaVozilaNaPlatformi == 0)) {
                                idiGore(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        if (this.vrstaVozilaNaPlatformi == 1) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    } else if (kolonaDrugogUdarenogVozila > 0 && kolonaDrugogUdarenogVozila < 4) {
                        //while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                        while(this.vrstaVozilaNaPlatformi > 1){
                            this.vrstaVozilaNaPlatformi--;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                idiGore(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }

                        if (this.vrstaVozilaNaPlatformi == -1 && vrstaDrugogUdarenogVozila == 0) {
                            this.vrstaVozilaNaPlatformi = 0;
                        }

                        if (this.vrstaVozilaNaPlatformi == 0 && vrstaDrugogUdarenogVozila == 1) {
                            this.vrstaVozilaNaPlatformi = 1;
                        }

                        while (this.kolonaZaKretanjeLijevoKodSudara > kolonaDrugogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][kolonaZaKretanjeLijevoKodSudara].isDaLiJePoljeSlobodno()) {
                                idiLijevo(this.vrstaVozilaNaPlatformi, kolonaZaKretanjeLijevoKodSudara);
                                this.kolonaZaKretanjeLijevoKodSudara--;
                            } else {
                                break;
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        if (this.vrstaVozilaNaPlatformi == 1) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[1][kolonaZaKretanjeLijevoKodSudara + 1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } else {
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }

                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][6].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                }
            }
        } // 
        // ZA IDI GORE

        if ((vrstaDrugogUdarenogVozila > 1 && vrstaDrugogUdarenogVozila < 10) && (kolonaDrugogUdarenogVozila > 4 && kolonaDrugogUdarenogVozila < 7)) {
            if (this.kolonaVozilaNaPlatformi == 7) { //ako je specijalno vozilo u sedmoj koloni

                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()
                        && !((vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 6) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 6)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 6))) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 6);
                }
                if ((this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 6) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 6)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 6)) {
                    System.out.println("VRSIM UVIDJAJ ALI NEMGU IZACI JER SE PORED MENE DESIO SUDAR");
                    if (this instanceof SanitetskoVoziloInterface) {
                        System.out.println("SANITETSKO");
                    }
                    if (this instanceof PolicijskoVoziloInterface) {
                        System.out.println("POLICIJSKO");
                    }
                    if (this instanceof VatrogasnoVoziloInterface) {
                        System.out.println("VATROGASNO");
                    }
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

                if (!this.vecSamIzvrsioUvidjaj && Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 2].isDaLiJePoljeSlobodno()) { // za kolonu broj 2
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 5);

                    if (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) { // ispod sam drugog udarenog auta 
                        // System.out.println("A");
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 5);
                                } else {
                                    break;
                                }
                            }
                        }
                        // System.out.println("uvidjaJJJ");
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);

                    } else if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) { //ako je spec. vozilo iza udarenog                        
                        while (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                                } else {
                                    break; // isto neko zaobilazenje ili slicno
                                }
                            }
                        }
                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][5].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);

                    }

                } else {
                    if (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                        while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi--;
                            if (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][6].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiGore(this.vrstaVozilaNaPlatformi, 6);
                                } else {
                                    break; // isto neko zaobilazenje
                                }
                            }

                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrstaVozilaNaPlatformi + 1][6].setVozilo(null);

                    } else if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) { // iza sam udarenog vozila ali sada sam u prvoj koloni
                        while (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][6].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 6);
                                } else {
                                    break; // isto neko zaobilazenje
                                }
                            }

                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        Garaza.garaza[brojPlatforme].getPlatforma()[vrstaVozilaNaPlatformi - 1][6].setVozilo(null);
                    }
                }

            }
            if (this.kolonaVozilaNaPlatformi == 4) { // ako je spec. voz. u koloni 3
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 5);
                }

                if ((this.vrstaVozilaNaPlatformi == vrstaSudara && kolonaSudara == 6) || (this.vrstaVozilaNaPlatformi == vrstaPrvogUdarenogVozila && kolonaSudara == 6)
                        || (this.vrstaVozilaNaPlatformi == vrstaDrugogUdarenogVozila && kolonaSudara == 6)) {
                    System.out.println("VRSIM UVIDJAJ ALI NEMGU IZACI JER SE PORED MENE DESIO SUDAR");
                    if (this instanceof SanitetskoVoziloInterface) {
                        System.out.println("SANITETSKO");
                    }
                    if (this instanceof PolicijskoVoziloInterface) {
                        System.out.println("POLICIJSKO");
                    }
                    if (this instanceof VatrogasnoVoziloInterface) {
                        System.out.println("VATROGASNO");
                    }
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                }

                if (!this.vecSamIzvrsioUvidjaj && (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila)) {
                    while (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi--;
                        if (this.vrstaVozilaNaPlatformi > vrstaPrvogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiGore(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }
                    }

                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);

                } else if (!this.vecSamIzvrsioUvidjaj && (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila)) {
                    while (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                        this.vrstaVozilaNaPlatformi++;
                        if (this.vrstaVozilaNaPlatformi < vrstaDrugogUdarenogVozila) {
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 5);
                            } else {
                                break;
                            }
                        }
                    }
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                    Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi - 1][5].setVozilo(null);
                    Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                }

            }
            if (this.kolonaVozilaNaPlatformi == 3) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi - 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiLijevo(this.vrstaVozilaNaPlatformi, 2);

                    if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < 9) {
                        while (this.vrstaVozilaNaPlatformi < 9) {
                            this.vrstaVozilaNaPlatformi++;
                            if (this.vrstaVozilaNaPlatformi < 9) {
                                //  if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                ((RotacijaInterface) this).setRotacija(true);
                                this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                                //  } else { // napraviti neko zaoibilazenje
                                //      evidentirajSudar();
                                //      ((RotacijaInterface) this).setRotacija(false);
                                //      break;
                                // }
                            }
                        }

                        while (!this.vecSamIzvrsioUvidjaj && (this.kolonaZaKretanjeDesnoKodSudara < 6)) {
                            this.kolonaZaKretanjeDesnoKodSudara++;
                            if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno() && kolonaZaKretanjeDesnoKodSudara != kolonaSudara) {
                                this.idiDesno(8, kolonaZaKretanjeDesnoKodSudara);
                            } else {
                                break;
                            }
                        }

                        if (this.vrstaVozilaNaPlatformi == 9) {
                            this.vrstaVozilaNaPlatformi = 8;
                        }
                        if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                                && !(vrstaSudara == 8)) {
                            while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                this.vrstaVozilaNaPlatformi--;
                                if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                    if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                        //((RotacijaInterface) this).setRotacija(true);
                                        this.idiGore(this.vrstaVozilaNaPlatformi, 5);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }

                        evidentirajSudar();
                        ((RotacijaInterface) this).setRotacija(false);
                        if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeDesnoKodSudara == 6 || kolonaZaKretanjeDesnoKodSudara == 5 || kolonaZaKretanjeDesnoKodSudara == 4 || kolonaZaKretanjeDesnoKodSudara == 3)) {
                            Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        } else {
                            Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }
                } else { // na licu mjesta evidentiraj sudar
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }
            if (this.kolonaVozilaNaPlatformi == 0 && vrstaVozilaNaPlatformi != 9) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    this.idiDesno(this.vrstaVozilaNaPlatformi, 1);

                    if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][this.kolonaVozilaNaPlatformi + 2].isDaLiJePoljeSlobodno()) {
                        ((RotacijaInterface) this).setRotacija(true);
                        this.idiDesno(this.vrstaVozilaNaPlatformi, 2);

                        if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi < 9) {
                            while (this.vrstaVozilaNaPlatformi < 9) {
                                this.vrstaVozilaNaPlatformi++;
                                if (this.vrstaVozilaNaPlatformi < 9) {
                                    // if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                    ((RotacijaInterface) this).setRotacija(true);
                                    this.idiDole(this.vrstaVozilaNaPlatformi, 2);
                                    // } else {  //napraviti neko zaoibilazenje
                                    ///      evidentirajSudar();
                                    //      ((RotacijaInterface) this).setRotacija(false);
                                    //      break;
                                    //  }
                                }
                            }

                            while (!this.vecSamIzvrsioUvidjaj && (this.kolonaZaKretanjeDesnoKodSudara < 6)) {
                                this.kolonaZaKretanjeDesnoKodSudara++;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno() && kolonaZaKretanjeDesnoKodSudara != kolonaSudara) {
                                    this.idiDesno(8, kolonaZaKretanjeDesnoKodSudara);
                                } else {
                                    break;
                                }
                            }

                            if (this.vrstaVozilaNaPlatformi == 9) {
                                this.vrstaVozilaNaPlatformi = 8;
                            }
                            if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                                    && !(vrstaSudara == 8)) {
                                while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                    this.vrstaVozilaNaPlatformi--;
                                    if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                        if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                            //((RotacijaInterface) this).setRotacija(true);
                                            this.idiGore(this.vrstaVozilaNaPlatformi, 5);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }

                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeDesnoKodSudara == 6 || kolonaZaKretanjeDesnoKodSudara == 5 || kolonaZaKretanjeDesnoKodSudara == 4 || kolonaZaKretanjeDesnoKodSudara == 3)) {
                                Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            } else {
                                Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            }
                        }
                    } else { // napraviti da ide kolonom 1 :D
                        Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][2].setVozilo(null);
                        Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                    }
                } else { // na licu mjesta evidentiraj sudar
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }

            }
            if (this.kolonaVozilaNaPlatformi == 0 && this.vrstaVozilaNaPlatformi == 9) {
                if (Garaza.garaza[brojPlatforme].getPlatforma()[9][1].isDaLiJePoljeSlobodno()) {
                    ((RotacijaInterface) this).setRotacija(true);
                    idiDesno(9, 1);
                    if (Garaza.garaza[brojPlatforme].getPlatforma()[8][1].isDaLiJePoljeSlobodno()) {
                        idiGore(8, 1);
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[8][2].isDaLiJePoljeSlobodno()) {
                            idiDesno(8, 2);

                            while (this.kolonaZaKretanjeDesnoKodSudara < 6) {
                                this.kolonaZaKretanjeDesnoKodSudara++;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno() && kolonaZaKretanjeDesnoKodSudara != kolonaSudara) {
                                    this.idiDesno(8, kolonaZaKretanjeDesnoKodSudara);
                                } else {
                                    break;
                                }
                            }
                            if (this.vrstaVozilaNaPlatformi == 9) {
                                this.vrstaVozilaNaPlatformi = 8;
                            }
                            if (!this.vecSamIzvrsioUvidjaj && this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila
                                    && !(vrstaSudara == 8)) {
                                while (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                    this.vrstaVozilaNaPlatformi--;
                                    if (this.vrstaVozilaNaPlatformi > vrstaDrugogUdarenogVozila) {
                                        if (Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi][5].isDaLiJePoljeSlobodno()) {
                                            //((RotacijaInterface) this).setRotacija(true);
                                            this.idiGore(this.vrstaVozilaNaPlatformi, 5);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }

                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            if (this.vrstaVozilaNaPlatformi == 8 && (kolonaZaKretanjeDesnoKodSudara == 6 || kolonaZaKretanjeDesnoKodSudara == 5 || kolonaZaKretanjeDesnoKodSudara == 4 || kolonaZaKretanjeDesnoKodSudara == 3)) {
                                Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            } else {
                                Garaza.garaza[brojPlatforme].getPlatforma()[this.vrstaVozilaNaPlatformi + 1][5].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            }

                        } else {
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            Garaza.garaza[brojPlatforme].getPlatforma()[8][1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    } else {
                        if (Garaza.garaza[brojPlatforme].getPlatforma()[9][2].isDaLiJePoljeSlobodno()) { // jos treba poraditi na ovom da se omoguci i kretanje po 5 koloni
                            idiDesno(9, 2);
                            while (this.kolonaZaKretanjeDesnoKodSudara < 6) {
                                this.kolonaZaKretanjeDesnoKodSudara++;
                                if (Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara].isDaLiJePoljeSlobodno() && kolonaZaKretanjeDesnoKodSudara != kolonaSudara) {
                                    this.idiDesno(8, kolonaZaKretanjeDesnoKodSudara);
                                } else {
                                    break;
                                }
                            }
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            if (this.vrstaVozilaNaPlatformi == 9 && (kolonaZaKretanjeDesnoKodSudara == 6 || kolonaZaKretanjeDesnoKodSudara == 5 || kolonaZaKretanjeDesnoKodSudara == 4 || kolonaZaKretanjeDesnoKodSudara == 3)) {
                                Garaza.garaza[brojPlatforme].getPlatforma()[8][kolonaZaKretanjeDesnoKodSudara - 1].setVozilo(null);
                                Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                            }

                        } else {
                            evidentirajSudar();
                            ((RotacijaInterface) this).setRotacija(false);
                            Garaza.garaza[brojPlatforme].getPlatforma()[9][1].setVozilo(null);
                            Garaza.garaza[brojPlatforme].getPlatforma()[pocetnaVrstaSpecijalnogVozila][pocetnaKolonaSpecijalnogVozila].setVozilo(this);
                        }
                    }
                } else {
                    ((RotacijaInterface) this).setRotacija(true);
                    evidentirajSudar();
                    ((RotacijaInterface) this).setRotacija(false);
                }
            }
        }

        sudar = false;

    }

    public void evidentirajSudar() {
        try { // ovo na kraju stavi kao funkciju koju pozivas da nemas dupliranje koda
            vrijemeObradeSudara = rand.nextInt(10_000) + 3_000;
            this.vecSamIzvrsioUvidjaj = true;
            if (this instanceof PolicijskoVoziloInterface) {
                ((PolicijskoVoziloInterface) this).evidentirajVozilaKojaSuUcestvoavalaUSudaru(Garaza.garaza[brojPlatforme].getPlatforma()[vrstaPrvogUdarenogVozila][kolonaPrvogUdarenogVozila].getVozilo(), Garaza.garaza[brojPlatforme].getPlatforma()[vrstaDrugogUdarenogVozila][kolonaDrugogUdarenogVozila].getVozilo());
                sleep(vrijemeObradeSudara);
            } else {
                sleep(vrijemeObradeSudara);

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Vozilo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        if (sudar && (this instanceof PolicijskoVoziloInterface || this instanceof SanitetskoVoziloInterface
                || this instanceof VatrogasnoVoziloInterface) && this.obradaSudara) {
            obradiSudarNaPlatformi();
        }

        if (zapocniParkiranje) {
            parkirajSe();
        }
        if (izlazakPetnaestPostoVozilaIzGaraze) {
            int lokalnaVrsta = 2;
            while (lokalnaVrsta < 10) {
                if (this.equals(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrsta][0].getVozilo())) {
                    isparkirava = true;
                    isparkiraoSeSalijevogDijelaPlatforme = true;
                    idiDesno(lokalnaVrsta, 1);
                    int idiPremaDole = lokalnaVrsta + 1;
                    while (idiPremaDole < 10) {
                        idiDole(idiPremaDole, 1);
                        idiPremaDole++;
                    }
                }

                if (this.equals(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrsta][3].getVozilo())) {
                    isparkirava = true;
                    isparkiraoSeSalijevogDijelaPlatforme = true;
                    idiLijevo(lokalnaVrsta, 2);
                    idiLijevo(lokalnaVrsta, 1);
                    int idiPremaDole = lokalnaVrsta + 1;
                    while (idiPremaDole < 10) {
                        idiDole(idiPremaDole, 1);
                        idiPremaDole++;
                    }
                }

                lokalnaVrsta++;
            }

            if (isparkirava && isparkiraoSeSalijevogDijelaPlatforme) {
                int brojac = 2;
                while (brojac < 7) {
                    idiDesno(9, brojac++);
                }
            }

            if (isparkirava && isparkiraoSeSalijevogDijelaPlatforme) {
                int brojac = 8;
                while (brojac > (-1)) {
                    idiGore(brojac--, 6);
                }
                napustiGarazu();
                isparkirava = false;
            }
            if (!isparkiraoSeSalijevogDijelaPlatforme) {
                int lokalnaVrstaZaDesniDioPlatforme = 9;
                while (lokalnaVrstaZaDesniDioPlatforme > 0) {
                    if (this.equals(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaDesniDioPlatforme][4].getVozilo())) {
                        isparkirava = true;
                        idiDesno(lokalnaVrstaZaDesniDioPlatforme, 5);
                        idiDesno(lokalnaVrstaZaDesniDioPlatforme, 6);
                        int idiPremaGore = lokalnaVrstaZaDesniDioPlatforme - 1;
                        while (idiPremaGore > (-1)) {
                            idiGore(idiPremaGore, 6);
                            idiPremaGore--;
                        }
                    }
                    if (this.equals(Garaza.garaza[brojPlatforme].getPlatforma()[lokalnaVrstaZaDesniDioPlatforme][7].getVozilo())) {
                        isparkirava = true;
                        idiLijevo(lokalnaVrstaZaDesniDioPlatforme, 6);
                        int idiPremaGore = lokalnaVrstaZaDesniDioPlatforme - 1;
                        while (idiPremaGore > (-1)) {
                            idiGore(idiPremaGore, 6);
                            idiPremaGore--;
                        }
                    }
                    lokalnaVrstaZaDesniDioPlatforme--;
                }

                if (isparkirava) {
                    napustiGarazu();
                }
            }
            isparkirava = false;
            izlazakPetnaestPostoVozilaIzGaraze = false;
            parkiran = true;

        }
    }

}
