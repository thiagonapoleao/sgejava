/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author lucas.vieira
 */
public class EditarAreaStz {

    Banco objCon = new Banco();

    private JRadioButton rdIncluir;
    private JRadioButton rdAlterar;
    private JRadioButton rdExcluir;

    private JTextField AreaStzTxt;
    private JTextField RuaTxt;
    private JTextField NivelTxt;
    private JTextField PosicaoTxt;
    private JTextField PosicaoIniTxt;
    private JTextField PosicaoFinalTxt;
    private JTextField SequenciaTxt;
    private JTextField EstacaoTxt;

    private JButton IncluirBtn;
    private JButton AlterarBtn;
    private JButton ExcluirBtn;

    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JLabel l4;
    private JLabel l5;
    private JLabel l6;
    private JLabel l7;
    private JLabel l8;

    public EditarAreaStz(JRadioButton rb1, JRadioButton rb2, JRadioButton rb3, JTextField tx1, JTextField tx2, JTextField tx3, JTextField tx4, JTextField tx5, JTextField tx6, JTextField tx7, JTextField tx8, JButton bt1, JButton bt2, JButton bt3, JLabel lb1, JLabel lb2, JLabel lb3, JLabel lb4, JLabel lb5, JLabel lb6, JLabel lb7, JLabel lb8) {
        rdIncluir = rb1;
        rdAlterar = rb2;
        rdExcluir = rb3;

        AreaStzTxt = tx1;
        RuaTxt = tx2;
        NivelTxt = tx3;
        PosicaoTxt = tx4;
        PosicaoIniTxt = tx5;
        PosicaoFinalTxt = tx6;
        SequenciaTxt = tx7;
        EstacaoTxt = tx8;

        IncluirBtn = bt1;
        AlterarBtn = bt2;
        ExcluirBtn = bt3;

        IncluirBtn.setVisible(false);
        AlterarBtn.setVisible(false);
        ExcluirBtn.setVisible(false);

        l1 = lb1;
        l2 = lb2;
        l3 = lb3;
        l4 = lb4;
        l5 = lb5;
        l6 = lb6;
        l7 = lb7;
        l8 = lb8;

        lb1.setVisible(false);
        lb2.setVisible(false);
        lb3.setVisible(false);
        lb4.setVisible(false);
        lb5.setVisible(false);
        lb6.setVisible(false);
        lb7.setVisible(false);
        lb8.setVisible(false);

        AreaStzTxt.setBackground(Color.WHITE);
        RuaTxt.setBackground(Color.WHITE);
        NivelTxt.setBackground(Color.WHITE);
        PosicaoTxt.setBackground(Color.WHITE);
        PosicaoIniTxt.setBackground(Color.WHITE);
        PosicaoFinalTxt.setBackground(Color.WHITE);
        SequenciaTxt.setBackground(Color.WHITE);
        EstacaoTxt.setBackground(Color.WHITE);

    }

    public void radioButons(int b) {

        if (b == 1) {
            rdIncluir.setSelected(true);
            rdAlterar.setSelected(false);
            rdExcluir.setSelected(false);
            preparaInclusao();
        } else if (b == 2) {
            rdIncluir.setSelected(false);
            rdAlterar.setSelected(true);
            rdExcluir.setSelected(false);
            preparaAlteracao();
        } else if (b == 3) {
            rdIncluir.setSelected(false);
            rdAlterar.setSelected(false);
            rdExcluir.setSelected(true);
            preparaExclusao();
        }

    }

    public void preparaInclusao() {

        AreaStzTxt.setText("");
        RuaTxt.setText("");
        NivelTxt.setText("");
        PosicaoTxt.setText("");
        PosicaoIniTxt.setText("");
        PosicaoFinalTxt.setText("");
        SequenciaTxt.setText("");
        EstacaoTxt.setText("");

        AreaStzTxt.setEnabled(false);
        RuaTxt.setEnabled(true);
        NivelTxt.setEnabled(true);
        PosicaoTxt.setEnabled(true);
        PosicaoIniTxt.setEnabled(true);
        PosicaoFinalTxt.setEnabled(true);
        SequenciaTxt.setEnabled(true);
        EstacaoTxt.setEnabled(true);

        IncluirBtn.setVisible(true);
        AlterarBtn.setVisible(false);
        ExcluirBtn.setVisible(false);

        IncluirBtn.setEnabled(true);
        l1.setVisible(false);
        l2.setVisible(true);
        l3.setVisible(true);
        l4.setVisible(true);
        l5.setVisible(true);
        l6.setVisible(true);
        l7.setVisible(true);
        l8.setVisible(true);
        RuaTxt.requestFocus();

        RuaTxt.setEditable(true);
        NivelTxt.setEditable(true);
        PosicaoTxt.setEditable(true);
        PosicaoIniTxt.setEditable(true);
        PosicaoFinalTxt.setEditable(true);
        SequenciaTxt.setEditable(true);
        EstacaoTxt.setEditable(true);

    }

    public void preparaAlteracao() {

        AreaStzTxt.setEditable(true);
        AreaStzTxt.setText("");
        RuaTxt.setText("");
        NivelTxt.setText("");
        PosicaoTxt.setText("");
        PosicaoIniTxt.setText("");
        PosicaoFinalTxt.setText("");
        SequenciaTxt.setText("");
        EstacaoTxt.setText("");

        AreaStzTxt.setEnabled(true);
        RuaTxt.setEnabled(false);
        NivelTxt.setEnabled(false);
        PosicaoTxt.setEnabled(false);
        PosicaoIniTxt.setEnabled(false);
        PosicaoFinalTxt.setEnabled(false);
        SequenciaTxt.setEnabled(false);
        EstacaoTxt.setEnabled(false);

        IncluirBtn.setVisible(false);
        AlterarBtn.setVisible(true);
        ExcluirBtn.setVisible(false);

        AlterarBtn.setEnabled(false);
        AreaStzTxt.requestFocus();
        l1.setVisible(false);
        l2.setVisible(false);
        l3.setVisible(false);
        l4.setVisible(false);
        l5.setVisible(false);
        l6.setVisible(false);
        l7.setVisible(false);
        l8.setVisible(false);

    }

    public void preparaExclusao() {
        AreaStzTxt.setEditable(true);
        AreaStzTxt.setText("");
        RuaTxt.setText("");
        NivelTxt.setText("");
        PosicaoTxt.setText("");
        PosicaoIniTxt.setText("");
        PosicaoFinalTxt.setText("");
        SequenciaTxt.setText("");
        EstacaoTxt.setText("");

        AreaStzTxt.setEnabled(true);
        RuaTxt.setEnabled(false);
        NivelTxt.setEnabled(false);
        PosicaoTxt.setEnabled(false);
        PosicaoIniTxt.setEnabled(false);
        PosicaoFinalTxt.setEnabled(false);
        SequenciaTxt.setEnabled(false);
        EstacaoTxt.setEnabled(false);

        IncluirBtn.setVisible(false);
        AlterarBtn.setVisible(false);
        ExcluirBtn.setVisible(true);

        ExcluirBtn.setEnabled(false);
        AreaStzTxt.requestFocus();
        l1.setVisible(false);
        l2.setVisible(false);
        l3.setVisible(false);
        l4.setVisible(false);
        l5.setVisible(false);
        l6.setVisible(false);
        l7.setVisible(false);
        l8.setVisible(false);
    }

    public void estadoInicial() {
        rdIncluir.setSelected(false);
        rdAlterar.setSelected(false);
        rdExcluir.setSelected(false);

        AreaStzTxt.setText("");
        RuaTxt.setText("");
        NivelTxt.setText("");
        PosicaoTxt.setText("");
        PosicaoIniTxt.setText("");
        PosicaoFinalTxt.setText("");
        SequenciaTxt.setText("");
        EstacaoTxt.setText("");

        IncluirBtn.setVisible(false);
        AlterarBtn.setVisible(false);
        ExcluirBtn.setVisible(false);

        AreaStzTxt.setEnabled(false);
        RuaTxt.setEnabled(false);
        NivelTxt.setEnabled(false);
        PosicaoTxt.setEnabled(false);
        PosicaoIniTxt.setEnabled(false);
        PosicaoFinalTxt.setEnabled(false);
        SequenciaTxt.setEnabled(false);
        EstacaoTxt.setEnabled(false);
    }

    public boolean validaAreaStz() {

        return true;
    }

    public boolean validaRua() {
        try {
            int rua = Integer.parseInt(RuaTxt.getText());
            if (rua > 0 && rua < 100) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validaNivel() {
        try {
            int nivel = Integer.parseInt(NivelTxt.getText());
            if (nivel < 10 && nivel > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validaPosicao() {
        try {
            int posicao = Integer.parseInt(PosicaoTxt.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validaPosIni() {
        try {
            if (!PosicaoIniTxt.equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validaPosFim() {
        try {
            if (!PosicaoFinalTxt.equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validaSequencia() {
        try {
            if (!SequenciaTxt.equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void realizaInclusao() {
        int cont = 0;

        if (validaRua() != true) {
            RuaTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaNivel() != true) {
            NivelTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaPosicao() != true) {
            PosicaoTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaPosIni() != true) {
            PosicaoIniTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaPosFim() != true) {
            PosicaoFinalTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaSequencia() != true) {
            SequenciaTxt.setBackground(Color.yellow);
            cont++;
        }

        if (cont == 0) {
            String ruaM = RuaTxt.getText();
            String nivelM = NivelTxt.getText();
            String posicaoM = PosicaoTxt.getText();
            String posicaoIniM = PosicaoIniTxt.getText();
            String posicaoFimM = PosicaoFinalTxt.getText();
            String sequenciaM = SequenciaTxt.getText();
            String estacaoM = EstacaoTxt.getText();

            if (posicaoM.length() == 1) {
                posicaoM = "0" + posicaoM;
            }

            int co = 0;
            String areaNova = ruaM + nivelM + posicaoM;

            try {
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = "SELECT * from area_stcruz WHERE area_stz = '" + areaNova + "'";
                objCon.rs = objCon.stmt.executeQuery(SQL);

                while (objCon.rs.next()) {
                    co++;
                }

            } catch (SQLException ex) {
                System.out.println("Erro");
            }

            try {
                objCon.con.close();
            } catch (SQLException ex) {
                System.out.println("erro");
            }

            if (co == 0) {
                try {
                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String sq = "INSERT INTO area_stcruz (pos_ini, pos_fim, estacao, area_stz, rua, nivel, excesso, sequencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst = objCon.con.prepareStatement(sq);

                    pst.setString(1, posicaoIniM);
                    pst.setString(2, posicaoFimM);
                    pst.setString(3, estacaoM);
                    pst.setString(4, areaNova);
                    pst.setString(5, ruaM);
                    pst.setString(6, nivelM);
                    pst.setString(7, posicaoM);
                    pst.setString(8, sequenciaM);

                    pst.executeUpdate();

                    pst.close();
                    if(rdAlterar.isSelected()) {
                        JOptionPane.showMessageDialog(null, "Area SantaCruz " +areaNova+ " alterada com sucesso!");
                        preparaAlteracao();
                    }else{
                        JOptionPane.showMessageDialog(null, "Area SantaCruz " +areaNova+ " criada com sucesso!");
                        preparaInclusao();
                    }
                } catch (SQLException ex) {
                    System.out.println("erro");
                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    System.out.println("Erro");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Area SantaCruz já existente!");
            }
        }
    }

    public void realizaEnter() {

        if (rdAlterar.isSelected()) {

            carregaCampos(1);

        } else if (rdExcluir.isSelected()) {

            carregaCampos(2);

        }
    }

    public void carregaCampos(int tp) {

        if (tp == 2) {
            AreaStzTxt.setEditable(true);
            RuaTxt.setEditable(false);
            NivelTxt.setEditable(false);
            PosicaoTxt.setEditable(false);
            PosicaoIniTxt.setEditable(false);
            PosicaoFinalTxt.setEditable(false);
            SequenciaTxt.setEditable(false);
            EstacaoTxt.setEditable(false);
            ExcluirBtn.setEnabled(true);
        } else {
            AreaStzTxt.setEditable(false);
            RuaTxt.setEditable(false);
            NivelTxt.setEditable(false);
            PosicaoTxt.setEditable(false);
            PosicaoIniTxt.setEditable(true);
            PosicaoFinalTxt.setEditable(true);
            SequenciaTxt.setEditable(true);
            EstacaoTxt.setEditable(true);
            AlterarBtn.setEnabled(true);
        }

        AreaStzTxt.setEnabled(true);
        RuaTxt.setEnabled(true);
        NivelTxt.setEnabled(true);
        PosicaoTxt.setEnabled(true);
        PosicaoIniTxt.setEnabled(true);
        PosicaoFinalTxt.setEnabled(true);
        SequenciaTxt.setEnabled(true);
        EstacaoTxt.setEnabled(true);

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from area_stcruz WHERE area_stz = '" + AreaStzTxt.getText() + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            while (objCon.rs.next()) {
                RuaTxt.setText(objCon.rs.getString("rua"));
                NivelTxt.setText(objCon.rs.getString("nivel"));
                PosicaoTxt.setText(objCon.rs.getString("excesso"));
                PosicaoIniTxt.setText(objCon.rs.getString("pos_ini"));
                PosicaoFinalTxt.setText(objCon.rs.getString("pos_fim"));
                SequenciaTxt.setText(objCon.rs.getString("sequencia"));
                EstacaoTxt.setText(objCon.rs.getString("estacao"));
            }

        } catch (SQLException ex) {
            System.out.println("Erro");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            System.out.println("erro");
        }
    }

    public void realizaExclusao() {
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM area_stcruz WHERE area_stz = '" + AreaStzTxt.getText() + "'";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

            if (rdExcluir.isSelected()) {
                preparaExclusao();
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir ean 0");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            System.out.println("erro");
        }
    }

    public void limpaCampos() {

        AreaStzTxt.setText("");
        RuaTxt.setText("");
        NivelTxt.setText("");
        PosicaoTxt.setText("");
        PosicaoIniTxt.setText("");
        PosicaoFinalTxt.setText("");
        SequenciaTxt.setText("");
        EstacaoTxt.setText("");

        if (rdIncluir.isSelected()) {

            AreaStzTxt.setEnabled(true);
            RuaTxt.setEnabled(true);
            NivelTxt.setEnabled(true);
            PosicaoTxt.setEnabled(true);
            PosicaoIniTxt.setEnabled(true);
            PosicaoFinalTxt.setEnabled(true);
            SequenciaTxt.setEnabled(true);
            EstacaoTxt.setEnabled(true);

            AreaStzTxt.setEditable(true);
            RuaTxt.setEditable(true);
            NivelTxt.setEditable(true);
            PosicaoTxt.setEditable(true);
            PosicaoIniTxt.setEditable(true);
            PosicaoFinalTxt.setEditable(true);
            SequenciaTxt.setEditable(true);
            EstacaoTxt.setEditable(true);

            l1.setVisible(true);
            l2.setVisible(true);
            l3.setVisible(true);
            l4.setVisible(true);
            l5.setVisible(true);
            l6.setVisible(true);
            l7.setVisible(true);
            l8.setVisible(true);

        } else if (rdAlterar.isSelected()) {

            AreaStzTxt.setEnabled(true);
            RuaTxt.setEnabled(false);
            NivelTxt.setEnabled(false);
            PosicaoTxt.setEnabled(false);
            PosicaoIniTxt.setEnabled(false);
            PosicaoFinalTxt.setEnabled(false);
            SequenciaTxt.setEnabled(false);
            EstacaoTxt.setEnabled(false);

            l1.setVisible(false);
            l2.setVisible(false);
            l3.setVisible(false);
            l4.setVisible(false);
            l5.setVisible(false);
            l6.setVisible(false);
            l7.setVisible(false);
            l8.setVisible(false);

            AlterarBtn.setEnabled(false);

        } else if (rdExcluir.isSelected()) {
            AreaStzTxt.setEnabled(true);
            RuaTxt.setEnabled(false);
            NivelTxt.setEnabled(false);
            PosicaoTxt.setEnabled(false);
            PosicaoIniTxt.setEnabled(false);
            PosicaoFinalTxt.setEnabled(false);
            SequenciaTxt.setEnabled(false);
            EstacaoTxt.setEnabled(false);

            l1.setVisible(false);
            l2.setVisible(false);
            l3.setVisible(false);
            l4.setVisible(false);
            l5.setVisible(false);
            l6.setVisible(false);
            l7.setVisible(false);
            l8.setVisible(false);

            ExcluirBtn.setEnabled(false);
        }

    }

    public void updateAreaStz() {
        String aStz = AreaStzTxt.getText();

        int cont = 0;

        if (validaPosIni() != true) {
            PosicaoIniTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaPosFim() != true) {
            PosicaoFinalTxt.setBackground(Color.yellow);
            cont++;
        }

        if (validaSequencia() != true) {
            SequenciaTxt.setBackground(Color.yellow);
            cont++;
        }

        if (cont == 0) {
            String posicaoIniM = PosicaoIniTxt.getText();
            String posicaoFimM = PosicaoFinalTxt.getText();
            String sequenciaM = SequenciaTxt.getText();
            String estacaoM = EstacaoTxt.getText();

            try {

                String sq = "UPDATE area_stcruz SET pos_ini = ?, pos_fim = ?, estacao = ?, sequencia = ? WHERE area_stz = ?";
                PreparedStatement pst = objCon.con.prepareStatement(sq);

                pst.setString(1, posicaoIniM);
                pst.setString(2, posicaoFimM);
                pst.setString(3, estacaoM);
                pst.setString(4, sequenciaM);
                pst.setString(5, aStz);

                pst.executeUpdate();

                pst.close();
                System.out.println("Area SantaCruz alterada com sucesso!");
            } catch (SQLException ex) {
                System.out.println("Erro no update da AreaStcruz");
            }

            try {
                objCon.con.close();
            } catch (SQLException ex) {
                System.out.println("erro");
            }
        }
    }
}
