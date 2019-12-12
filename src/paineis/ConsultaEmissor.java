/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paineis;

import classes.Banco;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas.vieira
 */
public class ConsultaEmissor extends javax.swing.JPanel {
    long emissAdm = 9999999999987L;
    Banco objCon = new Banco();
    long numCod = 0000000000000L;
    String nome = "";
    //codEmissorPass      

    /**
     * Creates new form ConsultaEmissor
     */
    public ConsultaEmissor() {
        initComponents();
        zeraCampos();
        nomeC.setText("");
    }
    
    public void zeraCampos() {
        codEmissorPass.setText("");
        numCod = 0000000000000L;
    }

    public void processamento() {

        if (!codEmissorPass.getPassword().toString().equals("")) {
            try {
                numCod = Long.parseLong(codEmissorPass.getPassword().toString());
                System.out.println(numCod);
                try {

                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * from login WHERE chacha = " + numCod;
                    objCon.rs = objCon.stmt.executeQuery(SQL);

                    objCon.rs.first();

                    if(numCod == emissAdm) {
                        nome = "Administrador";
                    }else{
                        nome = objCon.rs.getString("nome");
                    }
                    
                    nomeC.setText(nome);

                    zeraCampos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Código inválido!");
                    zeraCampos();
                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConsultaEmissor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Código inválido!");
                zeraCampos();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Código inválido!");
            zeraCampos();
        } 

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
        jLabel28 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        codEmissorPass = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        nomeC = new javax.swing.JLabel();

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(50, 76, 156));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Consulta Emissor");

        jLabel1.setText("Código Emissor : ");

        codEmissorPass.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton1.setText("Consultar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        nomeC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nomeC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout FixoLayout = new javax.swing.GroupLayout(Fixo);
        Fixo.setLayout(FixoLayout);
        FixoLayout.setHorizontalGroup(
            FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FixoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FixoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(codEmissorPass, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomeC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        FixoLayout.setVerticalGroup(
            FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FixoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(44, 44, 44)
                .addGroup(FixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(codEmissorPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(45, 45, 45)
                .addComponent(nomeC, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Fixo.getAccessibleContext().setAccessibleName("conemissor");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        processamento();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fixo;
    private javax.swing.JPasswordField codEmissorPass;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel nomeC;
    // End of variables declaration//GEN-END:variables
}
