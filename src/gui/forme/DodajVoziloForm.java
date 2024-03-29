/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.forme;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import garaza.Garaza;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import platforma.Platforma;
import vozilo.Vozilo;
import vozilo.Automobil;
import vozilo.Kombi;
import vozilo.Motocikl;
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
public class DodajVoziloForm extends javax.swing.JFrame {

    /**
     * Creates new form AddVehicleForm
     */
    private AdministratorForm adminForm;
    private Vozilo vozilo;
    private Integer tipVozila = 0; // tipVozila iz comboBoxa
    protected File file = null;
    private int brojPlatforme;
    Platforma platforma;
    private int brojVrata;
    private double nosivost;
    private boolean nosivostBoolean = false;
    private boolean brojVrataBoolean = false;

    //private Vozilo vozilo;
    public DodajVoziloForm() {
        initComponents();
        this.setTitle("Garaza");
        this.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
    }

    public DodajVoziloForm(int tipVozila, int brojPlatforme, AdministratorForm adminForm) {
        //System.out.println("AddVehicleForm tipVozila: "+tipVozila);
        this.adminForm = adminForm;
        this.tipVozila = tipVozila;
        this.brojPlatforme = brojPlatforme;
        initComponents();
        //this.setIconImage(new ImageIcon(getClass().getResource("/slikeGUI/world.png")).getImage());
        if (tipVozila == 0 || tipVozila == 5) {
            tfBrojVrata.setVisible(false);
            tfNosivost.setVisible(false);
            jLabelBrojVrata.setVisible(false);
            jLabelNosivost.setVisible(false);
        } else if (tipVozila == 1 || tipVozila == 3 || tipVozila == 8) {
            tfNosivost.setVisible(false);
            jLabelNosivost.setVisible(false);
            brojVrataBoolean = true;
        } else {
            tfBrojVrata.setVisible(false);
            jLabelBrojVrata.setVisible(false);
            nosivostBoolean = true;
        }
    }

    private boolean proveriBrojVrata(int brojVrata) {
        //int brojVrata = Integer.parseInt(tfBrojVrata.getText().toString().split("")[0]);
        System.out.println("BROJ VRATA: " + brojVrata);
        if (brojVrata == 3 || brojVrata == 5) {
            this.brojVrata = brojVrata;
            return true;
        }
        return false;
    }

    private boolean provjeriNosivost(double nosivost) {
        //double nosivost = Double.parseDouble(tfNosivost.getText().toString().split("")[0]);
        if (nosivost >= 0) {
            this.nosivost = nosivost;
            return true;
        }
        return false;
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
        jPanel2 = new javax.swing.JPanel();
        jLabelNaziv = new javax.swing.JLabel();
        jLabelBrojSasije = new javax.swing.JLabel();
        jLabelBrojMotora = new javax.swing.JLabel();
        jLabelRegistarskiBroj = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfNazivVozila = new javax.swing.JTextField();
        tfBrojSasijeVozila = new javax.swing.JTextField();
        tfBrojMotoraVozila = new javax.swing.JTextField();
        tfRegistarskiBrojVozila = new javax.swing.JTextField();
        slikaPanel = new javax.swing.JPanel();
        lFotografijaVozila = new javax.swing.JLabel();
        jLabelBrojVrata = new javax.swing.JLabel();
        tfBrojVrata = new javax.swing.JTextField();
        ldodajSliku1 = new javax.swing.JLabel();
        jLabelNosivost = new javax.swing.JLabel();
        tfNosivost = new javax.swing.JTextField();
        lObavjestenjeBrojVrata = new javax.swing.JLabel();
        lObavjestenjeNosivost = new javax.swing.JLabel();
        lObavjestenjeNaziv = new javax.swing.JLabel();
        lObavjestenjeBrojSasije = new javax.swing.JLabel();
        lObavjestenjeRegistarskiBroj = new javax.swing.JLabel();
        lObavjestenjeBrojMotora = new javax.swing.JLabel();
        bDoneVozilo = new javax.swing.JButton();
        bCancelVozilo = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lObavjestenjeDaVoziloVecPostojiUGarazi = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "Unos informacija", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabelNaziv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelNaziv.setText("Naziv");

        jLabelBrojSasije.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelBrojSasije.setText("Broj sasije");

        jLabelBrojMotora.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelBrojMotora.setText("Broj motora");

        jLabelRegistarskiBroj.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelRegistarskiBroj.setText("Registarski broj");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Fotografija");

        slikaPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        slikaPanel.setLayout(null);

        lFotografijaVozila.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lFotografijaVozila.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        lFotografijaVozila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lFotografijaVozilaMouseClicked(evt);
            }
        });
        slikaPanel.add(lFotografijaVozila);
        lFotografijaVozila.setBounds(0, 0, 220, 230);

        jLabelBrojVrata.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelBrojVrata.setText("Broj vrata");

        ldodajSliku1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/addPhoto.png"))); // NOI18N
        ldodajSliku1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ldodajSliku1MouseClicked(evt);
            }
        });

        jLabelNosivost.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelNosivost.setText("Nosivost");

        lObavjestenjeBrojVrata.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeBrojVrata.setForeground(new java.awt.Color(255, 51, 51));

        lObavjestenjeNosivost.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeNosivost.setForeground(new java.awt.Color(255, 51, 51));

        lObavjestenjeNaziv.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeNaziv.setForeground(new java.awt.Color(255, 51, 51));

        lObavjestenjeBrojSasije.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeBrojSasije.setForeground(new java.awt.Color(255, 51, 51));

        lObavjestenjeRegistarskiBroj.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeRegistarskiBroj.setForeground(new java.awt.Color(255, 51, 51));

        lObavjestenjeBrojMotora.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lObavjestenjeBrojMotora.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(ldodajSliku1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(slikaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelRegistarskiBroj)
                            .addComponent(jLabelBrojVrata, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBrojMotora)
                            .addComponent(jLabelBrojSasije)
                            .addComponent(jLabelNaziv)
                            .addComponent(jLabelNosivost))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfBrojVrata, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                    .addComponent(tfRegistarskiBrojVozila, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                    .addComponent(tfNosivost))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lObavjestenjeBrojVrata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lObavjestenjeNosivost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lObavjestenjeRegistarskiBroj, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(tfNazivVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lObavjestenjeNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(tfBrojSasijeVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lObavjestenjeBrojSasije, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(tfBrojMotoraVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lObavjestenjeBrojMotora, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelNaziv)
                    .addComponent(tfNazivVozila, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lObavjestenjeNaziv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabelBrojSasije))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lObavjestenjeBrojSasije, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfBrojSasijeVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelBrojMotora)
                    .addComponent(tfBrojMotoraVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lObavjestenjeBrojMotora, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabelRegistarskiBroj)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(tfRegistarskiBrojVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(3, 3, 3)))
                    .addComponent(lObavjestenjeRegistarskiBroj, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelBrojVrata)
                    .addComponent(tfBrojVrata, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lObavjestenjeBrojVrata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelNosivost))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfNosivost, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lObavjestenjeNosivost, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(slikaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(ldodajSliku1))
                .addContainerGap())
        );

        bDoneVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/donebutton.png"))); // NOI18N
        bDoneVozilo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bDoneVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDoneVoziloActionPerformed(evt);
            }
        });

        bCancelVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/slike/cancelbutton.png"))); // NOI18N
        bCancelVozilo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bCancelVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelVoziloActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Potvrdi");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Nazad");

        lObavjestenjeDaVoziloVecPostojiUGarazi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lObavjestenjeDaVoziloVecPostojiUGarazi.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bCancelVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(lObavjestenjeDaVoziloVecPostojiUGarazi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(bDoneVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(116, 116, 116))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(bCancelVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bDoneVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lObavjestenjeDaVoziloVecPostojiUGarazi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDoneVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDoneVoziloActionPerformed
        try {
            if (AdministratorForm.usaoUIzmjenu) {
                Vozilo izabranoVozilo = adminForm.garaza.getVozilo(adminForm.trenutnaPlatforma, adminForm.getRegistarskiBrojIzabranogVozila());
                adminForm.garaza.izbrisiVozilo(brojPlatforme, izabranoVozilo.getRegistarskiBroj());
                AdministratorForm.usaoUIzmjenu = false;
            }

            if (tfNazivVozila.getText().equals("")) {
                lObavjestenjeNaziv.setText("Nepravilan unos!");
                tfNazivVozila.setText("");
                return;
            } else {
                lObavjestenjeNaziv.setText("");
            }
            if (tfBrojSasijeVozila.getText().equals("")) {
                lObavjestenjeBrojSasije.setText("Nepravilan unos!");
                tfBrojSasijeVozila.setText("");
                return;
            } else {
                lObavjestenjeBrojSasije.setText("");
            }
            if (tfBrojMotoraVozila.getText().equals("")) {
                lObavjestenjeBrojMotora.setText("Nepravilan unos!");
                tfBrojMotoraVozila.setText("");
                return;
            } else {
                lObavjestenjeBrojMotora.setText("");
            }
            if (tfRegistarskiBrojVozila.getText().equals("")) {
                lObavjestenjeRegistarskiBroj.setText("Nepravilan unos!");
                tfRegistarskiBrojVozila.setText("");
                return;
            } else {
                lObavjestenjeRegistarskiBroj.setText("");
            }

            //if (tfBrojVrata.isVisible() && !proveriBrojVrata()) {
            if (brojVrataBoolean && ((tfBrojVrata.getText() == null || tfBrojVrata.getText().trim().isEmpty()) || !proveriBrojVrata(Integer.parseInt(tfBrojVrata.getText().toString().split(" ")[0])))) {
                // if(brojVrataBoolean && (!proveriBrojVrata (Integer.parseInt(tfBrojVrata.getText().toString().split("")[0])) || (tfBrojVrata.getText() == null || tfBrojVrata.getText().trim().isEmpty()))){
                lObavjestenjeBrojVrata.setText("Nepravilan unos!");
                tfBrojVrata.setText("");
                return;
            } else if (brojVrataBoolean && proveriBrojVrata(Integer.parseInt(tfBrojVrata.getText().toString().split("")[0]))) {
                lObavjestenjeBrojVrata.setText("");
            }
            // if (tfNosivost.isVisible() && !provjeriNosivost()) {
            if (nosivostBoolean && ((tfNosivost.getText() == null || tfNosivost.getText().trim().isEmpty()) || !provjeriNosivost(Double.parseDouble(tfNosivost.getText().toString().split(" ")[0])))) {
                lObavjestenjeNosivost.setText("Nepravilan unos!");
                tfNosivost.setText("");
                return;
            } else if (nosivostBoolean && provjeriNosivost(Double.parseDouble(tfNosivost.getText().toString().split("")[0]))) {
                lObavjestenjeNosivost.setText("");
            }

        } catch (Exception ex) {
        }

        //vozilo.setFotografija((File) getlFotografijaVozila().getIcon());
        if (tipVozila == 0) {
            vozilo = new Motocikl(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(), file);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 1) {
            vozilo = new Automobil(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, brojVrata);
            Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
        } else if (tipVozila == 2) {
            vozilo = new Kombi(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, nosivost);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 3) {
            vozilo = new PolicijskiAutomobil(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, brojVrata);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 4) {
            vozilo = new PolicijskiKombi(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, nosivost);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 5) {
            vozilo = new PolicijskiMotocikl(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(), file);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 6) {
            vozilo = new VatrogasniKombi(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, nosivost);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else if (tipVozila == 7) {
            vozilo = new SanitetskiKombi(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, nosivost);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        } else {
            vozilo = new SanitetskiAutomobil(tfNazivVozila.getText(), tfBrojSasijeVozila.getText(), tfBrojMotoraVozila.getText(), tfRegistarskiBrojVozila.getText(),
                    file, brojVrata);
            if (!Garaza.daLiPostojiVoziloUGarazi(vozilo)) {
                Garaza.dodajVozilo(vozilo, brojPlatforme, tipVozila);
            } else {
                lObavjestenjeDaVoziloVecPostojiUGarazi.setText("Vozilo vec postoji u garazi!");
                ponistiTextFieldove(vozilo);
                return;
            }
        }

        System.out.println("VOZILO: " + vozilo);

        adminForm.setVisible(true);
        adminForm.setTable();
        this.setVisible(false);
    }//GEN-LAST:event_bDoneVoziloActionPerformed

    private void lFotografijaVozilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lFotografijaVozilaMouseClicked
        //file = null;
        //lFotografijaVozila.setIcon(null);
    }//GEN-LAST:event_lFotografijaVozilaMouseClicked

    private void ponistiTextFieldove(Vozilo vozilo) {
        if (tipVozila == 0 || tipVozila == 5) {
            standardniTextFieldovi();
        } else if (tipVozila == 1 || tipVozila == 3 || tipVozila == 8) {
            standardniTextFieldovi();
            tfBrojVrata.setText("");
        } else {
            standardniTextFieldovi();
            tfNosivost.setText("");
        }
    }

    private void standardniTextFieldovi() {
        tfNazivVozila.setText("");
        tfRegistarskiBrojVozila.setText("");
        tfBrojMotoraVozila.setText("");
        tfBrojSasijeVozila.setText("");
    }


    private void ldodajSliku1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ldodajSliku1MouseClicked
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.showOpenDialog(null);
        file = chooser.getSelectedFile();
        File old = file;
        if (file != null) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("src/slikeVozila/" + file.getName()));
                file = new File("src/slikeVozila/" + file.getName());
                int k;
                while ((k = bis.read()) != -1) {
                    bos.write(k);
                }
                bis.close();
                bos.close();
            } catch (IOException ex) {
                System.out.println("Greska prilikom kopiranja slike " + ex);
            }
        }
        if (file != null) {
            String s = "/slikeVozila/" + file.getName().intern();
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }

            ImageIcon imageIcon = new ImageIcon(getClass().getResource(s));
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(215, 276, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);
            lFotografijaVozila.setIcon(imageIcon);
        }
        //vozilo.setFotografija(file);
    }//GEN-LAST:event_ldodajSliku1MouseClicked

    private void bCancelVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelVoziloActionPerformed
        dispose();
        adminForm.setVisible(true);
    }//GEN-LAST:event_bCancelVoziloActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancelVozilo;
    private javax.swing.JButton bDoneVozilo;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelBrojMotora;
    private javax.swing.JLabel jLabelBrojSasije;
    private javax.swing.JLabel jLabelBrojVrata;
    private javax.swing.JLabel jLabelNaziv;
    private javax.swing.JLabel jLabelNosivost;
    private javax.swing.JLabel jLabelRegistarskiBroj;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lFotografijaVozila;
    private javax.swing.JLabel lObavjestenjeBrojMotora;
    private javax.swing.JLabel lObavjestenjeBrojSasije;
    private javax.swing.JLabel lObavjestenjeBrojVrata;
    private javax.swing.JLabel lObavjestenjeDaVoziloVecPostojiUGarazi;
    private javax.swing.JLabel lObavjestenjeNaziv;
    private javax.swing.JLabel lObavjestenjeNosivost;
    private javax.swing.JLabel lObavjestenjeRegistarskiBroj;
    private javax.swing.JLabel ldodajSliku1;
    private javax.swing.JPanel slikaPanel;
    private javax.swing.JTextField tfBrojMotoraVozila;
    private javax.swing.JTextField tfBrojSasijeVozila;
    private javax.swing.JTextField tfBrojVrata;
    private javax.swing.JTextField tfNazivVozila;
    private javax.swing.JTextField tfNosivost;
    private javax.swing.JTextField tfRegistarskiBrojVozila;
    // End of variables declaration//GEN-END:variables

    public JLabel getlDoorNumberError() {
        return lObavjestenjeBrojVrata;
    }

    public File getFile() {
        return file;
    }

    public JLabel getlMaxCapacityError() {
        return lObavjestenjeNosivost;
    }

    public JLabel getLdodajSliku1() {
        return ldodajSliku1;
    }

    public JPanel getSlikaPanel() {
        return slikaPanel;
    }

    public JTextField getTfBrojMotoraVozila() {
        return tfBrojMotoraVozila;
    }

    public JTextField getTfBrojSasijeVozila() {
        return tfBrojSasijeVozila;
    }

    public JTextField getTfBrojVrata() {
        return tfBrojVrata;
    }

    public JTextField getTfNazivVozila() {
        return tfNazivVozila;
    }

    public JTextField getTfNosivost() {
        return tfNosivost;
    }

    public JTextField getTfRegistarskiBrojVozila() {
        return tfRegistarskiBrojVozila;
    }

    public JLabel getlFotografijaVozila() {
        return lFotografijaVozila;
    }

}
