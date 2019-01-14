/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vozilo.interfejs;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import vozilo.Vozilo;
import vozilo.prioritet.Prioritet;

/**
 *
 * @author djord
 */
public interface PolicijskoVoziloInterface extends SpecijalnoVoziloInterface {

    final Prioritet prioritet = Prioritet.NIZAK;

    final File potjernica = new File("potjernica.txt");
    final File evidencijaVozilaSaPotjernice = new File("evidencijaVozilaSaPotjernice.bin");
    final File evidencijaVozilaKojaSuUcestvovalaUSudaru = new File("evidencijaVozilaKojaSuUcestvovalaUSudaru.bin");

    default boolean daLiJeVoziloSaPotjerniceNaPlatformi(String registracija) {
        try (BufferedReader citac = new BufferedReader(new FileReader(potjernica))) {
            String linija = "";
            synchronized (potjernica) {
                while ((linija = citac.readLine()) != null) {
                    if (linija.equals(registracija)) {
                        return true;
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    default void evidentirajVoziloSaPotjernice(Vozilo vozilo) {
        String path = System.getProperty("user.home") + File.separator + "Documents";
        try (BufferedOutputStream pisac = new BufferedOutputStream(new FileOutputStream(path + File.separator + evidencijaVozilaSaPotjernice, true))) {
            byte[] registracijaVozila = String.valueOf(vozilo.getRegistarskiBroj()).getBytes();
            byte[] slikaVozila = Files.readAllBytes(vozilo.getFotografija().toPath());
            byte[] hesteg = String.valueOf("#").getBytes();

            long time = System.currentTimeMillis();
            Long newTime = new Long(time);
            String str = newTime.toString();
            byte[] vrijemPotjere = str.getBytes();

            ByteBuffer bb = ByteBuffer.allocate(registracijaVozila.length + 3 * slikaVozila.length + hesteg.length);

            bb.put(hesteg);
            bb.put(registracijaVozila);
            bb.put(hesteg);
            bb.put(slikaVozila);
            bb.put(hesteg);
            bb.put(vrijemPotjere);
            bb.put(hesteg);

            pisac.write(bb.array());
            pisac.flush();
            

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    default void evidentirajVozilaKojaSuUcestvoavalaUSudaru(Vozilo voziloPrvo,Vozilo voziloDrugo) {
        String path = System.getProperty("user.home") + File.separator + "Documents";
        try (BufferedOutputStream pisac = new BufferedOutputStream(new FileOutputStream(path + File.separator + evidencijaVozilaKojaSuUcestvovalaUSudaru,true))) {
            byte[] registracijaPrvogVozila = String.valueOf(voziloPrvo.getRegistarskiBroj()).getBytes();
            byte[] slikaPrvogVozila = Files.readAllBytes(voziloPrvo.getFotografija().toPath());
            byte[] registracijaDrugogVozila = String.valueOf(voziloDrugo.getRegistarskiBroj()).getBytes();
            byte[] slikaDrugogVozila = Files.readAllBytes(voziloDrugo.getFotografija().toPath());
            byte[] hesteg = String.valueOf("#").getBytes();

            long time = System.currentTimeMillis();
            Long newTime = new Long(time);
            String str = newTime.toString();
            byte[] vrijemSudara = str.getBytes();

            ByteBuffer bb = ByteBuffer.allocate(registracijaPrvogVozila.length + 3 * slikaPrvogVozila.length + hesteg.length + registracijaDrugogVozila.length + 3*slikaDrugogVozila.length + hesteg.length);

            bb.put(hesteg);
            bb.put(registracijaPrvogVozila);
            bb.put(hesteg);
            bb.put(slikaPrvogVozila);
            bb.put(hesteg);
            bb.put(vrijemSudara);
            bb.put(hesteg);
            
            bb.put(hesteg);
            bb.put(registracijaDrugogVozila);
            bb.put(hesteg);
            bb.put(slikaDrugogVozila);
            bb.put(hesteg);
            bb.put(vrijemSudara);
            bb.put(hesteg);

            pisac.write(bb.array());
            pisac.flush();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PolicijskoVoziloInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
