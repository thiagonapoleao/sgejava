/*
 *
 * Java 8u121
 *
 */

package frames;

import classes.Banco;
import classes.Config;
import classes.DataHora;
import classes.DataPbl;
import classes.VerificaAtualizacao;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintException;
import javax.swing.JOptionPane;

/*
 *
 * @author Lucas Vieira
 * 
 */

public class Login extends javax.swing.JFrame {

    Banco objCon = new Banco();

    Config global = new Config();
    String versao = global.versao;

    public int idFunc;
    public String nomeFunc;
    public String acFunc;

    /**
     * Creates new form Login
     */
    public Login() throws IOException {
        initComponents();

        inicializar();
    }

    private void inicializar() throws IOException {

        lblVersao.setText(versao);

        userTxt.requestFocus();
        
        
        verificaServ();

    }

    
    public void verificaServ() {
        
        if(global.dataB.equals("eerg")) {
            servLbl.setText("SRV-PROD");
        }else if(global.dataB.equals("eergteste")) {
            servLbl.setText("SRV-TESTE");
        }
        
    }
    
    public void atualizaInfoLogin(String user) throws ParseException {

        DataHora dth = new DataHora();

        String dta = dth.Data();
        String hra = dth.Hora();
        System.out.println(dta);
        System.out.println(hra);
        System.out.println(user);

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "UPDATE login SET dt_ult_login = ?, hr_ult_login = ? WHERE usuario = ? ";

            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.setString(1, dta);
            pst.setString(2, hra);
            pst.setString(3, user);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("erro ao atualizar hora e data");
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void efetuaLogin() throws PrintException, ParseException {
        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from login WHERE usuario = '" + userTxt.getText() + "' AND senha = '" + passTxt.getText() + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            idFunc = objCon.rs.getInt("id");
            nomeFunc = objCon.rs.getString("nome");
            acFunc = objCon.rs.getString("acesso");

            if (userTxt.getText().equals(objCon.rs.getString("usuario")) && passTxt.getText().equals(objCon.rs.getString("senha"))) {
                atualizaInfoLogin(objCon.rs.getString("usuario"));
                Main main = new Main("", nomeFunc, acFunc);
                main.setVisible(true);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Dados inválidos");
                userTxt.setText("");
                passTxt.setText("");  
                userTxt.requestFocus();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Dados inválidos");
            userTxt.setText("");
            passTxt.setText("");
            userTxt.requestFocus();
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        userTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        passTxt = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        lblVersao = new javax.swing.JLabel();
        servLbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        userTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton1.setText("Entrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        passTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passTxtKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Login do usuário");

        lblVersao.setForeground(new java.awt.Color(255, 255, 255));
        lblVersao.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        servLbl.setForeground(new java.awt.Color(255, 255, 255));
        servLbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Lucas Vieira@2017");

        jDesktopPane1.setLayer(userTxt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(passTxt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(lblVersao, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(servLbl, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passTxt)
                    .addComponent(userTxt)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addContainerGap(88, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(132, 132, 132))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(servLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lblVersao, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(userTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(passTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(58, 58, 58)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVersao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(servLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        try {

            efetuaLogin();
        } catch (PrintException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                efetuaLogin();
            } catch (PrintException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_jButton1KeyPressed

    private void passTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passTxtKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                efetuaLogin();
            } catch (PrintException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_passTxtKeyPressed

    /*
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Login().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JPasswordField passTxt;
    private javax.swing.JLabel servLbl;
    private javax.swing.JTextField userTxt;
    // End of variables declaration//GEN-END:variables
}
