/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paineis;

import classes.Banco;
import classes.Config;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas.vieira
 */
public class ListaAntibioticos extends javax.swing.JPanel {

    Config global = new Config();
    Banco objCon = new Banco();
    double divisao = 0;
    double falta = 0;
    double total = 0;
    double inicial = 0;
    double resto = 0;
    double totalP = 0;
    int cont = 0;

    int valorBarra = 0;

    /**
     * Creates new form ListaAntibioticos
     */
    public ListaAntibioticos() {
        initComponents();
        progressoLbl.setVisible(false);
        progressoBar.setVisible(false);
        jTextField1.setText("");
    }

    public void incrementaBarra() {

    }

    public void processaRel() throws InterruptedException, IOException {
        int geraArq = 0;
        File arquivoRec;
        String patchRec = "";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int i = file.showSaveDialog(null);

        if (i == 1) {

            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
        } else {
            arquivoRec = file.getSelectedFile();
            patchRec = arquivoRec.getPath();
            patchRec = patchRec + ".csv";
            geraArq = 1;
            
            jTextField1.setText(patchRec);

            if (geraArq == 1) {

                FileWriter writer = new FileWriter(patchRec);
                try {
                    writer.append("Endereco");
                    writer.append(";");
                    writer.append("Codigo Pfat");
                    writer.append(";");
                    writer.append("Descricao");
                    writer.append(";");
                    writer.append("Ean");
                    writer.append(";");
                    writer.append("Quantidade");
                    writer.append("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                progressoBar.setVisible(true);
                progressoLbl.setVisible(true);
                try {

                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * from curva_abc";
                    objCon.rs = objCon.stmt.executeQuery(SQL);

                    while (objCon.rs.next()) {
                        cont++;
                    }

                    //int bloc = objCon.rs.getInt("bloqueio");
                } catch (SQLException ex) {
                    System.out.println("erro na contagem de quantidade de registros");
                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
                }

                inicial = cont;
                resto = inicial % 100;
                totalP = inicial - resto;

                divisao = totalP / 100;

                falta = inicial - totalP;

                System.out.println("Total : " + inicial);
                System.out.println("Resto : " + resto);
                System.out.println("Total sem resto : " + totalP);
                System.out.println("Divisão : " + divisao);
                System.out.println("Falta : " + falta);
                // System.out.println("Total : " + cont);

                String ean = "0";
                valorBarra = 0;
                String codigo = "";
                try {

                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * from curva_abc";
                    objCon.rs = objCon.stmt.executeQuery(SQL);
                    int ct = 0;
                    while (objCon.rs.next()) {
                        ct++;
                        System.out.println("lendo registros");
                        //Thread.sleep(4);
                        codigo = objCon.rs.getString("pfat");

                        System.out.println(codigo);
                        if (ct % divisao == 0) {
                            valorBarra++;
                            progressoBar.setValue(valorBarra);
                            System.out.println("setando valor da barra");
                        }

                        System.out.println("antes do ean");
                        ean = executaConsulta(codigo);
                        System.out.println("depois do ean");
                        System.out.println(ean);
                        if (!ean.equals("0")) {

                            try {
                                writer.append(objCon.rs.getString("endereco"));
                                writer.append(";");
                                writer.append(codigo);
                                writer.append(";");
                                writer.append(objCon.rs.getString("descricao"));
                                writer.append(";");
                                writer.append(ean);
                                writer.append(";");
                                writer.append(objCon.rs.getString("est"));
                                writer.append("\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    writer.flush();
                    writer.close();

                     valorBarra = valorBarra + (int)falta;
                     progressoBar.setValue(valorBarra);
                     
                     progressoLbl.setVisible(false);
                     progressoBar.setVisible(false);
                     jTextField1.setText("");
                     
                     JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso ! \n Local : " + patchRec);
                } catch (SQLException ex) {
                    System.out.println("erro ao gerar registro");
                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String executaConsulta(String codigo) {
        String ean = "0";
        Banco objCon = new Banco();
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            int cont = 0;
            String SQL = "SELECT * from pln1030r WHERE codigo ='"+codigo +"'";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            int ct = 0;
            while (objCon.rs.next()) {
                cont++;
            }

            if (cont > 0) {
                ean = procuraEan(codigo);
            } else {
                ean = "0";
            }

        } catch (SQLException ex) {
            System.out.println("erro ao localizar antibioticos");
            ean = "0";
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
        }

        

        return ean;
    }

    public String procuraEan(String codigo) {
        String ean = "0";
        long eanN = 0000000000000L;
        Banco objCon = new Banco();
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            int cont = 0;
            String SQL = "SELECT * from pln0055r WHERE codigo ='" + codigo + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            int ct = 0;
            while (objCon.rs.next()) {
                //ean = Integer.toString(objCon.rs.getInt("ean"));
                ean = Long.toString(objCon.rs.getLong("ean"));
                cont++;
            }

        } catch (SQLException ex) {
            System.out.println("erro ao receber codigo ean");
            ean = "0";
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ean;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fixo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        progressoBar = new javax.swing.JProgressBar();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        progressoLbl = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Relatório de antibióticos");

        jLabel2.setText("Local do arquivo :");

        progressoBar.setStringPainted(true);

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setFocusable(false);

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        progressoLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        progressoLbl.setText("Gerando Relatório ...");

        javax.swing.GroupLayout FixoLayout = new javax.swing.GroupLayout(Fixo);
        Fixo.setLayout(FixoLayout);
        FixoLayout.setHorizontalGroup(
            FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FixoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressoBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addGroup(FixoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(progressoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        FixoLayout.setVerticalGroup(
            FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FixoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(41, 41, 41)
                .addComponent(progressoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressoBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(207, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Fixo.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("relAntib");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread() {

            @Override
            public void run() {
                try {
                    processaRel();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ListaAntibioticos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.start();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fixo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JProgressBar progressoBar;
    private javax.swing.JLabel progressoLbl;
    // End of variables declaration//GEN-END:variables
}
