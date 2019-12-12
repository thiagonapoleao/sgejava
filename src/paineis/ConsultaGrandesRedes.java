/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paineis;

import classes.Banco;
import classes.Tabela;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author lucas.vieira
 */
public class ConsultaGrandesRedes extends javax.swing.JPanel {

    public static Banco objCon = new Banco();
    private int rdSel = 2;
    List todosPicks;
    String[][] pickVols;

    /**
     * Creates new form ConsultaGrandesRedes
     */
    public ConsultaGrandesRedes() {
        initComponents();

        zeraTabela();
        try {
            picksAbertos();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir actPicksAbertos()");
        }

        relatorioBtn.setVisible(false);
    }

    public void actPickVol() {
        boolean conti = false;
        String ret = pickTxt.getText();
        String volume = volumeTxt.getText();
        int vol = 0;
        if (ret.length() > 0) {
            if (volume.length() > 0) {
                try {
                    vol = Integer.parseInt(volume);
                    conti = true;
                } catch (Exception e) {
                    conti = false;
                }
            }
        }

        if (conti) {
            try {
                ArrayList dados = new ArrayList();

                String[] Colunas = new String[]{"Picklist", "Rota", "Volume (Excluir)", "Data", "Hora"};
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = "SELECT * FROM vol_picklists WHERE pick = '" + ret + "' AND volume = " + vol + "";//sqlString;
                objCon.rs = objCon.stmt.executeQuery(SQL);

                while (objCon.rs.next()) {
                    String dataR = objCon.rs.getString("datahora");

                    String ano = dataR.substring(0, 4);
                    String mes = dataR.substring(5, 7);
                    String dia = dataR.substring(8, 10);
                    String data = dia + "/" + mes + "/" + ano;
                    String horaMin = dataR.substring(11, 16);
                    dados.add(new Object[]{objCon.rs.getString("pick"), objCon.rs.getString("rota"), vol, data, horaMin});

                }

                Tabela tabela = new Tabela(dados, Colunas);
                tblGRedes.setModel(tabela);
                tblGRedes.getColumnModel().getColumn(0).setPreferredWidth(198);
                tblGRedes.getColumnModel().getColumn(0).setResizable(false);
                tblGRedes.getColumnModel().getColumn(1).setPreferredWidth(198);
                tblGRedes.getColumnModel().getColumn(1).setResizable(false);
                tblGRedes.getColumnModel().getColumn(2).setPreferredWidth(198);
                tblGRedes.getColumnModel().getColumn(2).setResizable(false);
                tblGRedes.getColumnModel().getColumn(3).setPreferredWidth(198);
                tblGRedes.getColumnModel().getColumn(3).setResizable(false);
                tblGRedes.getColumnModel().getColumn(4).setPreferredWidth(198);
                tblGRedes.getColumnModel().getColumn(4).setResizable(false);
                tblGRedes.getTableHeader().setReorderingAllowed(false);
                tblGRedes.setAutoResizeMode(tblGRedes.AUTO_RESIZE_OFF);
                tblGRedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } catch (Exception e) {
                zeraTabela();
            }
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                System.out.println("erro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pick ou volume inválido!");
        }
        pickTxt.setText("");
        volumeTxt.setText("");
        pickTxt.requestFocus();
    }

    public void actPicklist() throws SQLException {
        if (rdSel == 1) {
            boolean continuar = false;
            int numeroPick = 0;

            String ret = pickTxt.getText();
            if (ret.length() > 0) {
                try {
                    numeroPick = Integer.parseInt(ret);
                    continuar = true;
                } catch (Exception e) {
                    continuar = false;
                }
            }
            if (continuar) {
                try {
                    contaVolumes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String stat = "a";
                ArrayList dados = new ArrayList();
                String pic = Integer.toString(numeroPick);
                String volTotal = "";
                String[] Colunas = new String[]{"Picklist", "Total de Volumes", "Rota", "Volume", "Status", "Data", "Hora"};

                try {
                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * FROM picklists WHERE numero_pick = '" + pic + "'";
                    objCon.rs = objCon.stmt.executeQuery(SQL);

                    while (objCon.rs.next()) {
                        volTotal = objCon.rs.getString("volumes");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    System.out.println("erro");
                }
                int t = 0;
                boolean incluir = false;
                try {
                    t = Integer.parseInt(volTotal);
                    incluir = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Pick inexistente!");
                }
                for (int i = 1; i <= t; i++) {
                    if (incluir) {
                        String rota = "";
                        boolean conf = false;
                        String dataHora = "";

                        objCon.openConnectionMysql();
                        objCon.stmt = objCon.con.createStatement();

                        String SL = "SELECT * FROM vol_picklists WHERE volume = '" + i + "'";
                        objCon.rs = objCon.stmt.executeQuery(SL);

                        while (objCon.rs.next()) {
                            dataHora = objCon.rs.getString("datahora");
                            rota = objCon.rs.getString("rota");
                            conf = true;

                        }
                        try {
                            objCon.con.close();
                        } catch (SQLException ex) {
                            System.out.println("erro");
                        }

                        String data = "";
                        String horaMin = "";
                        boolean cofr = false;
                        if (dataHora.length() == 21) {
                            cofr = true;
                            String ano = dataHora.substring(0, 4);
                            String mes = dataHora.substring(5, 7);
                            String dia = dataHora.substring(8, 10);
                            data = dia + "/" + mes + "/" + ano;

                            horaMin = dataHora.substring(11, 16);
                        }
                        if (cofr) {
                            dados.add(new Object[]{ret, volTotal, rota, i, "OK", data, horaMin});
                        } else {
                            dados.add(new Object[]{ret, volTotal, rota, i, "Pendente", "", ""});
                        }
                    }
                }

                Tabela tabela = new Tabela(dados, Colunas);
                tblGRedes.setModel(tabela);//975
                tblGRedes.getColumnModel().getColumn(0).setPreferredWidth(125);
                tblGRedes.getColumnModel().getColumn(0).setResizable(false);
                tblGRedes.getColumnModel().getColumn(1).setPreferredWidth(125);
                tblGRedes.getColumnModel().getColumn(1).setResizable(false);
                tblGRedes.getColumnModel().getColumn(2).setPreferredWidth(75);
                tblGRedes.getColumnModel().getColumn(2).setResizable(false);
                tblGRedes.getColumnModel().getColumn(3).setPreferredWidth(150);
                tblGRedes.getColumnModel().getColumn(3).setResizable(false);
                tblGRedes.getColumnModel().getColumn(4).setPreferredWidth(153);
                tblGRedes.getColumnModel().getColumn(4).setResizable(false);
                tblGRedes.getColumnModel().getColumn(5).setPreferredWidth(175);
                tblGRedes.getColumnModel().getColumn(5).setResizable(false);
                tblGRedes.getColumnModel().getColumn(6).setPreferredWidth(175);
                tblGRedes.getColumnModel().getColumn(6).setResizable(false);
                tblGRedes.getTableHeader().setReorderingAllowed(false);
                tblGRedes.setAutoResizeMode(tblGRedes.AUTO_RESIZE_OFF);
                tblGRedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } else {
                JOptionPane.showMessageDialog(null, "Numero de picklist Inválido");
                pickTxt.setText("");
                pickTxt.requestFocus();
            }

            pickTxt.setText("");
            pickTxt.requestFocus();
        }
    }

    public void volumes() {

    }

    public void actPickAbertos() throws SQLException {
        if (rdSel == 2) {

            try {
                contaVolumes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String stat = "a";
                ArrayList dados = new ArrayList();

                String[] Colunas = new String[]{"Picklist (Excluir)", "Volumes", "Conferidos (Concluir)", "Data", "Hora"};
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = "SELECT * FROM picklists WHERE status = '" + stat + "'";//sqlString;
                objCon.rs = objCon.stmt.executeQuery(SQL);

                objCon.rs.first();

                do {
                    int volumes = 0;
                    for (int i = 0; i < todosPicks.size(); i++) {
                        String pki = pickVols[i][0];
                        int pkv = Integer.parseInt((String) pickVols[i][1]);
                        if (pki.equals(objCon.rs.getString("numero_pick"))) {
                            volumes = pkv;
                        }
                    }
                    String dataR = objCon.rs.getString("data_hora");

                    String ano = dataR.substring(0, 4);
                    String mes = dataR.substring(5, 7);
                    String dia = dataR.substring(8, 10);
                    String data = dia + "/" + mes + "/" + ano;
                    String horaMin = dataR.substring(11, 16);

                    dados.add(new Object[]{objCon.rs.getString("numero_pick"), objCon.rs.getInt("volumes"), volumes, data, horaMin});
                } while (objCon.rs.next());
                Tabela tabela = new Tabela(dados, Colunas);
                tblGRedes.setModel(tabela);
                tblGRedes.getColumnModel().getColumn(0).setPreferredWidth(210);
                tblGRedes.getColumnModel().getColumn(0).setResizable(false);
                tblGRedes.getColumnModel().getColumn(1).setPreferredWidth(210);
                tblGRedes.getColumnModel().getColumn(1).setResizable(false);
                tblGRedes.getColumnModel().getColumn(2).setPreferredWidth(190);
                tblGRedes.getColumnModel().getColumn(2).setResizable(false);
                tblGRedes.getColumnModel().getColumn(3).setPreferredWidth(190);
                tblGRedes.getColumnModel().getColumn(3).setResizable(false);
                tblGRedes.getColumnModel().getColumn(4).setPreferredWidth(190);
                tblGRedes.getColumnModel().getColumn(4).setResizable(false);
                tblGRedes.getTableHeader().setReorderingAllowed(false);
                tblGRedes.setAutoResizeMode(tblGRedes.AUTO_RESIZE_OFF);
                tblGRedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } catch (Exception e) {
                zeraTabela();
            }
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                System.out.println("erro");
            }
        }

    }

    public void contaVolumes() throws SQLException {
        todosPicks = new ArrayList();
        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * FROM picklists";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            int cont = 0;
            while (objCon.rs.next()) {
                todosPicks.add(objCon.rs.getString("numero_pick"));
                cont++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        pickVols = new String[todosPicks.size()][2];
        for (int i = 0; i < todosPicks.size(); i++) {
            String pk = (String) todosPicks.get(i);
            //System.out.println(pk);
            int cont = 0;
            try {
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = "SELECT * FROM vol_picklists WHERE pick = '" + pk + "'";
                objCon.rs = objCon.stmt.executeQuery(SQL);

                while (objCon.rs.next()) {
                    cont++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pickVols[i][0] = pk;
            pickVols[i][1] = Integer.toString(cont);

        }

    }

    public void radioButons(int b) {

        if (b == 1) {
            rdSel = 1;
            consultPk.setSelected(true);
            consultPkA.setSelected(false);
            consVol.setSelected(false);
            zeraTabela();
            try {
                consultaPicks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (b == 2) {
            rdSel = 2;
            consultPk.setSelected(false);
            consultPkA.setSelected(true);
            consVol.setSelected(false);
            zeraTabela();
            try {
                picksAbertos();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao abrir actPicksAbertos()");
            }
        } else if (b == 3) {
            rdSel = 3;
            consultPk.setSelected(false);
            consultPkA.setSelected(false);
            consVol.setSelected(true);
            zeraTabela();
            consultaVolumes();
        }

    }

    public void consultaPicks() {
        pickTxt.setText("");
        pickTxt.setEnabled(true);
        volumeTxt.setText("");
        volumeTxt.setEnabled(false);
        pickTxt.requestFocus();
        consultaBtn.setEnabled(true);
        //actPicklist();
    }

    public void picksAbertos() throws SQLException {
        pickTxt.setText("");
        pickTxt.setEnabled(false);
        volumeTxt.setText("");
        volumeTxt.setEnabled(false);
        consultaBtn.setEnabled(false);
        actPickAbertos();
    }

    public void consultaVolumes() {
        pickTxt.setText("");
        pickTxt.setEnabled(true);
        volumeTxt.setText("");
        volumeTxt.setEnabled(true);
        pickTxt.requestFocus();
        consultaBtn.setEnabled(true);
    }

    public void zeraTabela() {
        ArrayList dd = new ArrayList();
        String[] Col = new String[]{""};

        Tabela tabela = new Tabela(dd, Col);
        tblGRedes.setModel(tabela);
        tblGRedes.getColumnModel().getColumn(0).setPreferredWidth(975);
        tblGRedes.getColumnModel().getColumn(0).setResizable(false);
        tblGRedes.getTableHeader().setReorderingAllowed(false);
        tblGRedes.setAutoResizeMode(tblGRedes.AUTO_RESIZE_OFF);
        tblGRedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void consultas() {
        switch (rdSel) {
            case 1: {
                try {
                    actPicklist();
                } catch (SQLException ex) {
                    Logger.getLogger(ConsultaGrandesRedes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 3:
                try {
                    actPickVol();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void concluirPick(String nPick) {
        String vl = "c";
        try {

            objCon.openConnectionMysql();

            objCon.stmt = objCon.con.createStatement();

            String sq = "UPDATE picklists SET status = ? WHERE numero_pick = ?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setString(1, vl);
            pst.setString(2, nPick);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro na alteração!");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }
        try {
            actPickAbertos();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaGrandesRedes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirVolumePick(String pick, int volume) {
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM vol_picklists WHERE pick = '" + pick + "' AND volume = " + volume + "";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir volume");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {

            pickTxt.setText(pick);
            volumeTxt.setText(Integer.toString(volume));
            actPickVol();
        } catch (Exception e) {
            e.printStackTrace();
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

        pnlFixo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGRedes = new javax.swing.JTable();
        consultPk = new javax.swing.JRadioButton();
        consultPkA = new javax.swing.JRadioButton();
        consVol = new javax.swing.JRadioButton();
        volumeTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        consultaBtn = new javax.swing.JButton();
        relatorioBtn = new javax.swing.JButton();
        pickTxt = new javax.swing.JTextField();

        pnlFixo.setMinimumSize(new java.awt.Dimension(1000, 750));
        pnlFixo.setPreferredSize(new java.awt.Dimension(1000, 750));

        tblGRedes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblGRedes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRedesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGRedes);

        consultPk.setText("Consultar PickList");
        consultPk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultPkActionPerformed(evt);
            }
        });

        consultPkA.setSelected(true);
        consultPkA.setText("Consultar PickLists Abertos");
        consultPkA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultPkAActionPerformed(evt);
            }
        });

        consVol.setText("Consultar Volumes");
        consVol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consVolActionPerformed(evt);
            }
        });

        volumeTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volumeTxtActionPerformed(evt);
            }
        });

        jLabel1.setText("PickList : ");

        jLabel2.setText("Volume : ");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(50, 76, 156));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Consulta de Volumes -- Grandes Redes");

        consultaBtn.setText("Consultar");
        consultaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultaBtnActionPerformed(evt);
            }
        });
        consultaBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                consultaBtnKeyPressed(evt);
            }
        });

        relatorioBtn.setText("Relatório");

        pickTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFixoLayout = new javax.swing.GroupLayout(pnlFixo);
        pnlFixo.setLayout(pnlFixoLayout);
        pnlFixoLayout.setHorizontalGroup(
            pnlFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(pnlFixoLayout.createSequentialGroup()
                .addContainerGap(285, Short.MAX_VALUE)
                .addGroup(pnlFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlFixoLayout.createSequentialGroup()
                        .addComponent(consultPk)
                        .addGap(18, 18, 18)
                        .addComponent(consultPkA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(consVol))
                    .addComponent(jLabel28))
                .addContainerGap(255, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFixoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pickTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(volumeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(consultaBtn)
                .addGap(18, 18, 18)
                .addComponent(relatorioBtn)
                .addGap(198, 198, 198))
        );
        pnlFixoLayout.setVerticalGroup(
            pnlFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFixoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel28)
                .addGap(47, 47, 47)
                .addGroup(pnlFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consVol)
                    .addComponent(consultPkA)
                    .addComponent(consultPk))
                .addGap(18, 18, 18)
                .addGroup(pnlFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(relatorioBtn)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(volumeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(consultaBtn)
                    .addComponent(pickTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("consRedes");
    }// </editor-fold>//GEN-END:initComponents

    private void consultPkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultPkActionPerformed
        // TODO add your handling code here:
        radioButons(1);
    }//GEN-LAST:event_consultPkActionPerformed

    private void consultPkAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultPkAActionPerformed
        // TODO add your handling code here:
        radioButons(2);
    }//GEN-LAST:event_consultPkAActionPerformed

    private void consVolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consVolActionPerformed
        // TODO add your handling code here:
        radioButons(3);
    }//GEN-LAST:event_consVolActionPerformed

    private void consultaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultaBtnActionPerformed
        // TODO add your handling code here:
        consultas();
    }//GEN-LAST:event_consultaBtnActionPerformed

    private void tblGRedesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRedesMouseClicked
        // TODO add your handling code here:
        if (rdSel == 2) {
            if (tblGRedes.getSelectedColumn() == 0) {
                int linha = tblGRedes.getSelectedRow();

                String resultado = (String) tblGRedes.getValueAt(linha, 0);
                JOptionPane opcao = new JOptionPane();

                int r = opcao.showConfirmDialog(null, "Deseja excluir o pick " + resultado + "?");

                if (r == 0) {
                    excluirPick(resultado);
                }
            }

            if (tblGRedes.getSelectedColumn() == 2) {
                int linha = tblGRedes.getSelectedRow();

                String resultado = (String) tblGRedes.getValueAt(linha, 0);
                JOptionPane opcao = new JOptionPane();

                int r = opcao.showConfirmDialog(null, "Deseja concluir o pick " + resultado + "?");

                if (r == 0) {
                    concluirPick(resultado);
                }
            }
        }

        if (rdSel == 3) {
            if (tblGRedes.getSelectedColumn() == 2) {
                int linha = tblGRedes.getSelectedRow();

                String pick = (String) tblGRedes.getValueAt(linha, 0);
                int volume = (int) tblGRedes.getValueAt(linha, 2);
                JOptionPane opcao = new JOptionPane();

                int r = opcao.showConfirmDialog(null, "Deseja excluir o volume " + volume + " do pick " + pick + "?");

                if (r == 0) {
                    excluirVolumePick(pick, volume);
                }
            }
        }
    }//GEN-LAST:event_tblGRedesMouseClicked

    private void pickTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickTxtActionPerformed
        // TODO add your handling code here:
        consultas();
    }//GEN-LAST:event_pickTxtActionPerformed

    private void volumeTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volumeTxtActionPerformed
        // TODO add your handling code here:
        consultas();
    }//GEN-LAST:event_volumeTxtActionPerformed

    private void consultaBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_consultaBtnKeyPressed
        // TODO add your handling code here:
        consultas();
    }//GEN-LAST:event_consultaBtnKeyPressed

    public void excluirPick(String nPick) {
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM picklists WHERE numero_pick = '" + nPick + "'";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir picklist " + nPick);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM vol_picklists WHERE pick = '" + nPick + "'";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir volumes do picklist " + nPick);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            actPickAbertos();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultaGrandesRedes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton consVol;
    private javax.swing.JRadioButton consultPk;
    private javax.swing.JRadioButton consultPkA;
    private javax.swing.JButton consultaBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField pickTxt;
    private javax.swing.JPanel pnlFixo;
    private javax.swing.JButton relatorioBtn;
    private javax.swing.JTable tblGRedes;
    private javax.swing.JTextField volumeTxt;
    // End of variables declaration//GEN-END:variables
}
