/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garaza;

import platforma.Platforma;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import platforma.Platforma;
import platforma.Polje;
import vozilo.Automobil;
import vozilo.Kombi;
import vozilo.Motocikl;
import vozilo.Vozilo;
import vozilo.specijalno.PolicijskiAutomobil;
import vozilo.specijalno.PolicijskiKombi;
import vozilo.specijalno.PolicijskiMotocikl;
import vozilo.specijalno.SanitetskiAutomobil;
import vozilo.specijalno.SanitetskiKombi;
import vozilo.specijalno.VatrogasniKombi;

/**
 *
 * @author djord
 */
public class Garaza implements Serializable {

    private static int brojPlatformi;
    public static Platforma[] garaza; // garaza ima n platformi
    private static final Random rand = new Random();

    public Garaza() {
        try (FileInputStream fis = new FileInputStream("configuration.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            brojPlatformi = Integer.parseInt(properties.getProperty("broj"));
        } catch (IOException ex) {
            Logger.getLogger(Garaza.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (new File("garaza.ser").exists()) {
            try {
                deserijalizacija();
            } catch (IOException ex) {
                Logger.getLogger(Garaza.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Garaza.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            garaza = new Platforma[brojPlatformi];
            for (int i = 0; i < brojPlatformi; ++i) {
                garaza[i] = new Platforma(i);
            }
            try {
                serijalizacija();
            } catch (IOException ex) {
                Logger.getLogger(Garaza.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        garaza[brojPlatformi - 1].setDaLiJeZadnjaPlatforma(true);
        System.out.println("Broj platformi: " + brojPlatformi);

    }

    public static final void serijalizacija() throws FileNotFoundException, IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("garaza.ser")))) {
            oos.writeObject(garaza);
        }
    }

    public static final void deserijalizacija() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (!(new File("garaza.ser").exists() && new File("Garaza.ser").length() != 0)) {
            System.out.println("Ne postoji fajl 'garaza.ser'");
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("garaza.ser"))) {
                garaza = (Platforma[]) ois.readObject();
            }
        }
    }

    public static void dodajVozilo(Vozilo vozilo, int brojPlatforme, int tipVozila) {
        int x = rand.nextInt(10);
        int y = rand.nextInt(8);

        String tipPolja = garaza[brojPlatforme].getPlatforma()[x][y].getTipPolja();
        while (true) {
            if ("Parking".equals(garaza[brojPlatforme].getPlatforma()[x][y].getTipPolja()) && garaza[brojPlatforme].getPlatforma()[x][y].isDaLiJePoljeSlobodno()) {
                //System.out.println("TIP POLJA: " + garaza[brojPlatforme].getPlatforma()[x][y].getTipPolja());

                break;
            }
            x = rand.nextInt(10);
            y = rand.nextInt(8);
        }

        garaza[brojPlatforme].getPlatforma()[x][y].setVozilo(vozilo);
        vozilo.setVrijemeUlaskaUGarazu(new Date());
        vozilo.setTipVozila(tipVozila);
        vozilo.setBrojPlatforme(brojPlatforme);

        vozilo.setVrstaVozilaNaPlatformi(x);
        vozilo.setKolonaVozilaNaPlatformi(y);

        //System.out.println("BROJ VOZILA NA PLATFORMI: " + brojPlatforme + " JE: " + getBrojVozilaSaPlatforme(brojPlatforme));
        //System.out.println("UKUPAN BROJ VOZILA U GARAZI: " + getUkupnoVozilaUGarazi());
        //printGaraza();
    }

    public static Object[][] prikazTrenutnePlatforme(int brojPlatforme) {
        if (getBrojVozilaSaPlatforme(brojPlatforme) == 0) {
            return new Object[0][0];
        }
        Object[][] tabela = new Object[getBrojVozilaSaPlatforme(brojPlatforme)][5];
        int sljedecaVrsta = 0;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 8; ++j) {
                SimpleDateFormat simpleDateformat = new SimpleDateFormat("[dd:MM:yy] [hh:mm:ss]");
                int trenutnaPozicijaUPlatformi = (i * 8 + j);
                Polje polje = garaza[brojPlatforme].getPlatforma()[i][j];
                if (polje.getTipPolja().equals("Parking") && !(polje.isDaLiJePoljeSlobodno())) {
                    Object[] vrsta = new Object[5];
                    vrsta[0] = polje.getVozilo().getNaziv();
                    vrsta[1] = polje.getVozilo().getRegistarskiBroj();
                    vrsta[2] = trenutnaPozicijaUPlatformi;
                    vrsta[4] = simpleDateformat.format(polje.getVozilo().getVrijemeUlaskaUGarazu());
                    if (polje.getVozilo() instanceof Motocikl) {
                        vrsta[3] = "Motocikl";
                    } else if (polje.getVozilo() instanceof Automobil) {
                        vrsta[3] = "Automobil";
                    } else if (polje.getVozilo() instanceof Kombi) {
                        vrsta[3] = "Kombi";
                    } else if (polje.getVozilo() instanceof PolicijskiMotocikl) {
                        vrsta[3] = "Policijski motocikl";
                    } else if (polje.getVozilo() instanceof PolicijskiAutomobil) {
                        vrsta[3] = "Policijski automobil";
                    } else if (polje.getVozilo() instanceof PolicijskiKombi) {
                        vrsta[3] = "Policijski kombi";
                    } else if (polje.getVozilo() instanceof SanitetskiAutomobil) {
                        vrsta[3] = "Sanitetski automobil";
                    } else if (polje.getVozilo() instanceof SanitetskiKombi) {
                        vrsta[3] = "Sanitetski kombi";
                    } else if (polje.getVozilo() instanceof VatrogasniKombi) {
                        vrsta[3] = "Vatrogasni kombi";
                    }
                    tabela[sljedecaVrsta++] = vrsta;
                }
            }
        }
        return tabela;
    }

    public static int getBrojPlatformi() {
        return brojPlatformi;
    }

    public static void setBrojPlatformi(int brojPlatformi) {
        Garaza.brojPlatformi = brojPlatformi;
    }

    public static Platforma[] getGaraza() {
        return garaza;
    }

    public static void setGaraza(Platforma[] garaza) {
        Garaza.garaza = garaza;
    }

    public static int getBrojVozilaSaPlatforme(int brojPlatforme) {
        int brojVozila = 0;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (!(garaza[brojPlatforme].getPlatforma()[i][j].isDaLiJePoljeSlobodno())) {
                    ++brojVozila;
                }
            }
        }
        return brojVozila;
    }

    public static int getUkupnoVozilaUGarazi() {
        int ukupanBrojVozilaUGarazi = 0;
        for (int i = 0; i < brojPlatformi; ++i) {
            ukupanBrojVozilaUGarazi += getBrojVozilaSaPlatforme(i);
        }
        return ukupanBrojVozilaUGarazi;
    }

    public static Vozilo getVozilo(int brojPlatforme, String registarskaOznaka) {
        Vozilo vozilo = null;
        if ("".equals(registarskaOznaka)) {
            return vozilo;
        }
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (garaza[brojPlatforme].getPlatforma()[i][j].getVozilo() != null && garaza[brojPlatforme].getPlatforma()[i][j].getVozilo().getRegistarskiBroj()
                        .equals(registarskaOznaka)) {
                    vozilo = garaza[brojPlatforme].getPlatforma()[i][j].getVozilo();
                }
            }
        }
        return vozilo;
    }

    public static boolean izbrisiVozilo(int brojPlatforme, String registarskaOznaka) {
        if (registarskaOznaka == null) {
            return false;

        }
        Vozilo vozilo;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (garaza[brojPlatforme].getPlatforma()[i][j].getVozilo() != null && garaza[brojPlatforme].getPlatforma()[i][j].getVozilo().getRegistarskiBroj()
                        .equals(registarskaOznaka)) {
                    vozilo = garaza[brojPlatforme].getPlatforma()[i][j].getVozilo();
                    garaza[brojPlatforme].getPlatforma()[i][j].setVozilo(null);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean daLiJeGarazaPopunjena() {
        int pom = 0;
        for (int i = 0; i < brojPlatformi; ++i) {
            if (getBrojVozilaSaPlatforme(i) > 27) {
                pom++;
            }
        }
        if (pom == brojPlatformi) {
            return true;
        }
        return false;
    }

    public static int naplatiParking(Vozilo vozilo) {

        if (vozilo == null) {
            return 0;
        }
        long vrijemeParkiranja = Calendar.getInstance().getTimeInMillis() - vozilo.getVrijemeUlaskaUGarazu().getTime();
        if (vrijemeParkiranja <= 3_600_000) {
            return 1;
        }
        if (vrijemeParkiranja <= 10_800_000) {
            return 2;
        }
        return 8;
    }

    public static boolean daLiPostojiVoziloUGarazi(Vozilo vozilo) {
        ArrayList<Vozilo> listaVozilaUGarazi = new ArrayList<>();
        for (int i = 0; i < brojPlatformi; i++) {
            listaVozilaUGarazi.addAll(garaza[i].svaParkiranaVozilaNaPlatformi());
        }
        for (Vozilo v : listaVozilaUGarazi) {
            if (listaVozilaUGarazi.contains(vozilo)) {
                return true;
            }
        }
        return false;
    }

}
