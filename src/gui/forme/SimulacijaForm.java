/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.forme;

import garaza.Garaza;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.util.Random;
import javax.swing.JLabel;
import platforma.Platforma;
import vozilo.Automobil;
import vozilo.Kombi;
import vozilo.Motocikl;
import vozilo.Vozilo;
import vozilo.interfejs.PolicijskoVoziloInterface;
import vozilo.interfejs.RotacijaInterface;
import vozilo.interfejs.SanitetskoVoziloInterface;
import vozilo.interfejs.VatrogasnoVoziloInterface;
import vozilo.specijalno.PolicijskiAutomobil;
import vozilo.specijalno.PolicijskiKombi;
import vozilo.specijalno.PolicijskiMotocikl;
import vozilo.specijalno.SanitetskiAutomobil;
import vozilo.specijalno.SanitetskiKombi;
import vozilo.specijalno.VatrogasniKombi;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import vozilo.interfejs.SpecijalnoVoziloInterface;

/**
 *
 * @author djord
 */
public class SimulacijaForm extends javax.swing.JFrame {

    /**
     * Creates new form SimulationForm
     */
    Garaza garaza;
    private static int trenutnaPlatforma;
    private int tipVozila;
    AdministratorForm adminForm;
    Random rand = new Random();
    public static int brojVozilaKojaSuNapustilaGarazu = 0;

    public SimulacijaForm() {
        initComponents();
    }

    public SimulacijaForm(int brojVozila) {
        initComponents();
        setCmbPlatforma();

        for (int i = 0; i < Garaza.getBrojPlatformi(); i++) {
            generisiSlucajnaVozila(brojVozila, i, false);
        }
        ispisiPlatformu();
    }

    private void setCmbPlatforma() {
        for (Integer i = 0; i < Garaza.getBrojPlatformi(); i++) {
            cmbPlatforma.addItem("Platforma " + i.toString());
        }
    }

    public static void ispisiPlatformu() {
        taPlatforma.setText("");
        taPlatforma.setLineWrap(false);

        String platforma = "";
        for (int i = 0; i < Platforma.getBROJ_REDOVA(); ++i) {
            for (int j = 0; j < Platforma.getBROJ_KOLONA(); ++j) {
                Vozilo vozilo = Garaza.garaza[trenutnaPlatforma].getPlatforma()[i][j].getVozilo();
                if (Garaza.garaza[trenutnaPlatforma].getPlatforma()[i][j].getTipPolja().equals("Parking") && Garaza.garaza[trenutnaPlatforma].getPlatforma()[i][j]
                        .isDaLiJePoljeSlobodno()) {
                    platforma += "|    *    ";
                } else if (vozilo instanceof PolicijskoVoziloInterface && vozilo != null && !((RotacijaInterface) vozilo).getRotacija()) {
                    platforma += "|    P    ";
                } else if (vozilo instanceof SanitetskoVoziloInterface && vozilo != null && !((RotacijaInterface) vozilo).getRotacija()) {
                    platforma += "|    H    ";
                } else if (vozilo instanceof VatrogasnoVoziloInterface && vozilo != null && !((RotacijaInterface) vozilo).getRotacija()) {
                    platforma += "|    F    ";
                } else if (vozilo instanceof PolicijskoVoziloInterface && ((RotacijaInterface) vozilo).getRotacija() && vozilo != null) {
                    platforma += "|    PR    ";
                } else if (vozilo instanceof SanitetskoVoziloInterface && ((RotacijaInterface) vozilo).getRotacija() && vozilo != null) {
                    platforma += "|    HR    ";
                } else if (vozilo instanceof VatrogasnoVoziloInterface && ((RotacijaInterface) vozilo).getRotacija() && vozilo != null) {
                    platforma += "|    FR    ";
                } else if ((vozilo instanceof Automobil || vozilo instanceof Motocikl || vozilo instanceof Kombi) && vozilo != null
                        && !Garaza.garaza[trenutnaPlatforma].getPlatforma()[i][j].isDaLiJePoljeSlobodno()) {
                    platforma += "|    V    ";
                } else {
                    platforma += "|          ";
                }
            }
            platforma += System.lineSeparator();
            platforma += System.lineSeparator();
        }
        taPlatforma.setText(platforma);
        taPlatforma.setEditable(false);
    }

    private Vozilo generisiSlucajnaVozila(int brojVozila, int brojPlatforme, boolean jednoVozilo) {
        String[] nazivVozila = new String[]{"Renault", "Peugeot", "Citroen", "Volkswagen", "Audi", "Mercedes", "Lada", "Zastava", "BMW", "Opel", "Cadillac", "Chevrolet"};
        File slikaAutomobil = new File("src/slikeVozila/civilCar3.jpg");
        File slikaMotocikl = new File("src/slikeVozila/policeMotorBike.jpg");
        File slikaKombi = new File("src/slikeVozila/fireTruck.png");

        Vozilo slucajnoVozilo = null;

        if (Garaza.getBrojVozilaSaPlatforme(brojPlatforme) < brojVozila || jednoVozilo == true) {
            int brojNovihVozila = brojVozila - Garaza.getBrojVozilaSaPlatforme(brojPlatforme);
            if (jednoVozilo == true) {
                brojNovihVozila = 1;
            }

            for (int i = brojNovihVozila; i > 0; i--) {
                boolean obicnoVozilo = rand.nextInt(100) < 90;
                //boolean obicnoVozilo = rand.nextInt(100) < 99;
                String slucajnoVoziloNaziv = nazivVozila[rand.nextInt(nazivVozila.length)];

                if (obicnoVozilo) {
                    tipVozila = rand.nextInt(2);
                } else {
                    tipVozila = rand.nextInt(8) + 3;
                }
                Integer slucajnaOznaka = rand.nextInt(9999) + 1000;

                if (tipVozila == 0) {
                    slucajnoVozilo = new Motocikl(slucajnoVoziloNaziv, slucajnaOznaka.toString(), slucajnaOznaka.toString(), slucajnaOznaka.toString(), slikaMotocikl);
                } else if (tipVozila == 1) {
                    slucajnoVozilo = new Automobil(slucajnoVoziloNaziv, slucajnaOznaka.toString(), slucajnaOznaka.toString(), slucajnaOznaka.toString(), slikaAutomobil, rand.nextInt(5 - 3) + 3);
                } else if (tipVozila == 2) {
                    slucajnoVozilo = new Kombi(slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slikaKombi, rand.nextInt(10 - 1) + 10);
                } else if (tipVozila == 3) {
                    slucajnoVozilo = new PolicijskiAutomobil(slucajnoVoziloNaziv, slucajnaOznaka.toString(), slucajnaOznaka.toString(), slucajnaOznaka.toString(), slikaAutomobil, rand.nextInt(5 - 3) + 3);
                } else if (tipVozila == 4) {
                    slucajnoVozilo = new PolicijskiKombi(slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slikaKombi, rand.nextInt(10 - 1) + 10);
                } else if (tipVozila == 5) {
                    slucajnoVozilo = new PolicijskiMotocikl(slucajnoVoziloNaziv, slucajnaOznaka.toString(), slucajnaOznaka.toString(), slucajnaOznaka.toString(), slikaMotocikl);
                } else if (tipVozila == 6) {
                    slucajnoVozilo = new VatrogasniKombi(slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slikaKombi, rand.nextInt(10 - 1) + 10);
                } else if (tipVozila == 7) {
                    slucajnoVozilo = new SanitetskiKombi(slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slucajnoVoziloNaziv, slikaKombi, rand.nextInt(10 - 1) + 10);
                } else {
                    slucajnoVozilo = new SanitetskiKombi(slucajnoVoziloNaziv, slucajnaOznaka.toString(), slucajnaOznaka.toString(), slucajnaOznaka.toString(), slikaAutomobil, rand.nextInt(5 - 3) + 3);
                }
                if (!jednoVozilo) {
                    Garaza.dodajVozilo(slucajnoVozilo, brojPlatforme, tipVozila);
                } else {
                    //  slucajnoVozilo.setBrojPlatforme(brojPlatforme);
                    slucajnoVozilo.setVrijemeUlaskaUGarazu(new Date());
                    slucajnoVozilo.setTipVozila(tipVozila);
                    slucajnoVozilo.setZapocniParkiranje(true);
                    // slucajnoVozilo.setVrstaVozilaNaPlatformi(slucajnoVozilo.getVrstaVozilaNaPlatformi());
                    // slucajnoVozilo.setKolonaVozilaNaPlatformi(slucajnoVozilo.getKolonaVozilaNaPlatformi());
                    slucajnoVozilo.setBrojPlatforme(0);

                    return slucajnoVozilo;
                }
            }
        }
        return null;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cmbPlatforma = new javax.swing.JComboBox<String>();
        bExitAdmin = new javax.swing.JButton();
        bPlaySimulation = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bDownloadCSV = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taPlatforma = new javax.swing.JTextArea();
        bAdd = new javax.swing.JButton();
        ladd = new javax.swing.JLabel();
        lObavjestenja = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "Platforma", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setMinimumSize(new java.awt.Dimension(180, 140));
        jPanel3.setLayout(null);

        cmbPlatforma.setMaximumRowCount(3);
        cmbPlatforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPlatformaActionPerformed(evt);
            }
        });
        jPanel3.add(cmbPlatforma);
        cmbPlatforma.setBounds(10, 30, 140, 20);

        jPanel1.add(jPanel3);
        jPanel3.setBounds(70, 10, 160, 140);

        bExitAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/exitbutton.png"))); // NOI18N
        bExitAdmin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bExitAdmin.setPreferredSize(new java.awt.Dimension(48, 57));
        bExitAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExitAdminActionPerformed(evt);
            }
        });
        jPanel1.add(bExitAdmin);
        bExitAdmin.setBounds(10, 420, 50, 50);

        bPlaySimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/playbutton.png"))); // NOI18N
        bPlaySimulation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bPlaySimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlaySimulationActionPerformed(evt);
            }
        });
        jPanel1.add(bPlaySimulation);
        bPlaySimulation.setBounds(700, 420, 50, 50);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Pokreni");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(700, 480, 80, 14);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("IZLAZ");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 480, 50, 17);

        bDownloadCSV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/csvbutton.png"))); // NOI18N
        bDownloadCSV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bDownloadCSV.setPreferredSize(new java.awt.Dimension(48, 48));
        bDownloadCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDownloadCSVActionPerformed(evt);
            }
        });
        jPanel1.add(bDownloadCSV);
        bDownloadCSV.setBounds(700, 20, 48, 48);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Preuzmi");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(700, 80, 60, 14);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 83));

        taPlatforma.setColumns(20);
        taPlatforma.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        taPlatforma.setRows(5);
        jScrollPane1.setViewportView(taPlatforma);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(240, 20, 430, 360);

        bAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/addVehicle.png"))); // NOI18N
        bAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bAdd.setPreferredSize(new java.awt.Dimension(48, 48));
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });
        jPanel1.add(bAdd);
        bAdd.setBounds(10, 20, 48, 48);

        ladd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ladd.setText("Dodaj");
        jPanel1.add(ladd);
        ladd.setBounds(10, 80, 50, 20);

        lObavjestenja.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lObavjestenja.setForeground(new java.awt.Color(255, 0, 51));
        jPanel1.add(lObavjestenja);
        lObavjestenja.setBounds(110, 430, 430, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExitAdminActionPerformed
        try {
            Garaza.serijalizacija();
        } catch (IOException ex) {
            Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }//GEN-LAST:event_bExitAdminActionPerformed

    private void bDownloadCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDownloadCSVActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sacuvaj CSV.");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".csv", ".CSV"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selektrovaniFajl = fileChooser.getSelectedFile();
            selektrovaniFajl = new File(selektrovaniFajl.toString() + ".csv");
            try {
                try (PrintWriter pisac = new PrintWriter(new BufferedWriter(new FileWriter(selektrovaniFajl)))) {

                    pisac.println("Reg. oznaka" + " | " + "Vrijeme(min)" + " | " + "Za napaltu(KM)");
                    for (Vozilo v : Vozilo.getVozilaZaNaplatuParkinga()) {
                        if (!(v instanceof SpecijalnoVoziloInterface)) {
                            pisac.println(v.getRegistarskiBroj() + "          |" + ((Calendar.getInstance().getTimeInMillis() - v.getVrijemeUlaskaUGarazu().getTime()) / 60_000) + "          |"
                                    + Garaza.naplatiParking(v));
                        }
                    }
                }

            } catch (Exception e) {
                Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }//GEN-LAST:event_bDownloadCSVActionPerformed

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed
        if (Garaza.daLiJeGarazaPopunjena()) {
            lObavjestenja.setText("Garaza je puna!");
            return;
        }
        try {
            sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(() -> {
            generisiSlucajnaVozila(tipVozila, trenutnaPlatforma, true).start();
            try {
                Garaza.serijalizacija();
            } catch (IOException ex) {
                Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }//GEN-LAST:event_bAddActionPerformed

    private void bPlaySimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlaySimulationActionPerformed
        // 15% vozila napusta garazu
        bPlaySimulation.setEnabled(false);
        bDownloadCSV.setEnabled(false);
        try {
            new Thread(() -> {
                for (int i = 0; i < Garaza.getBrojPlatformi(); i++) {
                    int brojVozilaKojiNapustajuPlatformu = (int) (Garaza.getBrojVozilaSaPlatforme(i) * 0.15);
                    int pomBrojVozilaKojiNapustajuPlatformu = brojVozilaKojiNapustajuPlatformu;
                    List<Vozilo> vozilaKojaNapustajuPlatformu = new ArrayList<>();
                    while (brojVozilaKojiNapustajuPlatformu > 0) {
                        int x = rand.nextInt(10);
                        int y = rand.nextInt(8);
                        if (Garaza.garaza[i].getPlatforma()[x][y].getTipPolja().equals("Parking")
                                && !Garaza.garaza[i].getPlatforma()[x][y].isDaLiJePoljeSlobodno()) {
                            Garaza.garaza[i].getPlatforma()[x][y].getVozilo().setIzlazakPetnaestPostoVozilaIzGaraze(true);
                            if (!vozilaKojaNapustajuPlatformu.contains(Garaza.garaza[i].getPlatforma()[x][y].getVozilo())) {
                                vozilaKojaNapustajuPlatformu.add(Garaza.garaza[i].getPlatforma()[x][y].getVozilo());
                                brojVozilaKojiNapustajuPlatformu--;

                            }
                        }
                    }
                    for (int j = 0; j < pomBrojVozilaKojiNapustajuPlatformu; j++) {
                        if (vozilaKojaNapustajuPlatformu.get(j).getState() == Thread.State.TERMINATED) {
                            Thread noviTred = new Thread(vozilaKojaNapustajuPlatformu.get(j));
                            noviTred.start();
                        } else {
                            brojVozilaKojaSuNapustilaGarazu++;
                            vozilaKojaNapustajuPlatformu.get(j).start();
                        }

                    }
                }
                try {
                    Garaza.serijalizacija();
                } catch (IOException ex) {
                    Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        } catch (Exception ex) {
            Logger.getLogger(SimulacijaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bPlaySimulationActionPerformed

    private void cmbPlatformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPlatformaActionPerformed
        trenutnaPlatforma = Integer.parseInt(cmbPlatforma.getSelectedItem().toString().split(" ")[1]);
        ispisiPlatformu();
    }//GEN-LAST:event_cmbPlatformaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            Garaza.serijalizacija();
        } catch (IOException ex) {
            Logger.getLogger(AdministratorForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    public static javax.swing.JButton bDownloadCSV;
    private javax.swing.JButton bExitAdmin;
    private javax.swing.JButton bPlaySimulation;
    private javax.swing.JComboBox<String> cmbPlatforma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lObavjestenja;
    private javax.swing.JLabel ladd;
    static javax.swing.JTextArea taPlatforma;
    // End of variables declaration//GEN-END:variables
}
