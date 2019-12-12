package classes;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/*
 *
 * @author lucas.vieira
 *
 */

public class Atualizacao {

    Config global = new Config();
    public boolean execucao = true;
    Banco objCon = new Banco();
    String pastaRem;
    JLabel eCurva;
    JLabel eMb52;
    JLabel ePbl5050m;
    JLabel ePln0055r;

    JTextField eDatAt;

    JButton btnExec;
    JButton btnRemove;

    public Atualizacao(JLabel curva, JLabel mb52, JLabel pbl5050m, JLabel pln0055r, JTextField datAt, JButton bexec, JButton bemove) {
        eCurva = curva;
        eMb52 = mb52;
        ePbl5050m = pbl5050m;
        ePln0055r = pln0055r;
        eDatAt = datAt;
        btnExec = bexec;
        btnRemove = bemove;

        pastaRem = global.arquivoRemoto.replace("\\", "/");

        verifExistArq();
    }

    public boolean verifExistArq() {
        boolean stat = true;

        File curva = new File(global.arquivoRemoto + "Curva ABC do Produto.csv");

        File mb52 = new File(global.arquivoRemoto + "mb52.csv");

        File pbl5050m = new File(global.arquivoRemoto + "pbl5050m.csv");

        File pln0055r = new File(global.arquivoRemoto + "pln0055r.txt");

        if (!curva.exists()) {
            eCurva.setText("ERRO");
            eCurva.setForeground(Color.red);
            stat = false;
        } else {
            eCurva.setText("OK");
            eCurva.setForeground(Color.green);
        }

        if (!mb52.exists()) {
            eMb52.setText("ERRO");
            eMb52.setForeground(Color.red);
            stat = false;
        } else {
            eMb52.setText("OK");
            eMb52.setForeground(Color.green);
        }

        if (!pbl5050m.exists()) {
            ePbl5050m.setText("ERRO");
            ePbl5050m.setForeground(Color.red);
            stat = false;
        } else {
            ePbl5050m.setText("OK");
            ePbl5050m.setForeground(Color.green);
        }

        if (!pln0055r.exists()) {
            ePln0055r.setText("ERRO");
            ePln0055r.setForeground(Color.red);
            stat = false;
        } else {
            ePln0055r.setText("OK");
            ePln0055r.setForeground(Color.green);
        }

        return stat;

    }

    public int realizaAtualizacao() throws SQLException, IOException, FileNotFoundException, ParseException {
        if (verifExistArq() != true) {
            execucao = false;
            erroAtualizacao(1);
        }

        if (execucao == true && verifUso() != true) {
            execucao = false;
            erroAtualizacao(2);
        }

        if (execucao == true) {
            bloquear();
        }

        if (execucao == true) {
            atualizarCurva();
        }

        if (execucao == true) {
            atzPbl5050m();
        }

        if (execucao == true) {
            atualizarMb52();
        }

        if (execucao == true) {
            atualizarPln0055r()/*atzPln0055r()*/;
        }

        if (execucao == true) {
            atualizaDataAtz();
        }

        desbloquear();

        if (execucao == true) {
            JOptionPane.showMessageDialog(null, "Atualização Realizada!");
        }

        return 0;
    }

    public void erroAtualizacao(int codErr) {

        switch (codErr) {
            case 1:
                JOptionPane.showMessageDialog(null, "Faltam arquivos necessários para a atualização!");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Outro usuario está atualizando o programa!");
                break;
        }

    }

    public void atualizarCurva() throws SQLException {
        deletarDados("curva_abc");

        System.out.println("atualizando curva_abc");
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String sq = "LOAD DATA LOCAL INFILE '" + pastaRem + "Curva ABC do Produto.csv' INTO TABLE curva_abc FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 4 LINES (endereco, pfat, sap, fornec, curva, descricao, ven_d, ven_m, ven_d_l, ven_m_l, ac_d, ac_m, med_sem, emb, conj, est)";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.executeUpdate();

            objCon.stmt.close();

            pst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void atualizarPbl5050m() {
        deletarDados("pbl5050m");

        System.out.println("atualizando pbl5050m");
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            
            String sq = "LOAD DATA LOCAL INFILE '" + pastaRem + "pbl5050m.csv' INTO TABLE pbl5050m FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 2 LINES (rua, endereco, estacao, prateleira, codigo, descricao, saldo, lote, validade)";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.executeUpdate();

            objCon.stmt.close();

            pst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        deletarVagosPbl5050m();

        DataPbl aq = new DataPbl();
        try {
            aq.alteraDatas();
        } catch (ParseException ex) {
            System.out.println("Erro");
        }
    }

    public void atualizarMb52() {
        deletarDados("mb52");

        System.out.println("atualizando mb52");
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String sq = "LOAD DATA LOCAL INFILE '" + pastaRem + "mb52.csv' INTO TABLE mb52 FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 2 LINES (repz, material, descricao, cen, dep, umb, lote, unidades, valor_unidades, contr_qual, valor_contr_qual, bloqueado, valor_bloqueado, devolucoes, transito_te)";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.executeUpdate();

            objCon.stmt.close();

            pst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean atzPln0055r() throws FileNotFoundException, IOException, ParseException {

        File arquivo = new File(global.arquivoRemoto + "pln0055r.txt");

        System.out.println("atualizando pln0055r");
        deletarDados("pln0055r");

        String est = "";
        String prt = "";
        String cod = "0";
        String desc = "";
        String alt = "";
        String larg = "";
        String prof = "";
        String uniCx = "";
        String cjp = "";
        String ean = "0";
        String atv = "";
        int eanNum = 0;

        BufferedReader buffRead = new BufferedReader(new FileReader(arquivo));
        String linha = "";

        int tempCod = 0;
        int tempAlt = 0;
        int tempLarg = 0;
        int tempProf = 0;
        int tempUni = 0;
        int tempCjp = 0;

        int cont = 0;

        while (true) {
            if (linha != null) {
                if (cont > 2) {
                    est = linha.substring(0, 3);
                    prt = linha.substring(4, 7);
                    cod = linha.substring(8, 18);
                    desc = linha.substring(19, 51);
                    alt = linha.substring(52, 57);
                    larg = linha.substring(58, 63);
                    prof = linha.substring(64, 69);
                    uniCx = linha.substring(70, 75);
                    cjp = linha.substring(76, 79);
                    ean = linha.substring(80, 93);
                    atv = linha.substring(94, 95);

                    ean = ean.replace(" ", "");

                    tempCod = Integer.parseInt(cod);
                    cod = Integer.toString(tempCod);
                    tempAlt = Integer.parseInt(alt);
                    alt = Integer.toString(tempAlt);
                    tempLarg = Integer.parseInt(larg);
                    larg = Integer.toString(tempLarg);
                    tempProf = Integer.parseInt(prof);
                    prof = Integer.toString(tempProf);
                    tempUni = Integer.parseInt(uniCx);
                    uniCx = Integer.toString(tempUni);
                    tempCjp = Integer.parseInt(cjp);
                    cjp = Integer.toString(tempCjp);

                    try {

                        objCon.openConnectionMysql();
                        objCon.stmt = objCon.con.createStatement();

                        String sq = "INSERT INTO pln0055r (est, pra, codigo, descricao, altura, largura, profundidade, embp, cjp, ean, ativo) VALUES ('" + est + "', '" + prt + "', '" + cod + "', '" + desc + "', '" + alt + "', '" + larg + "', '" + prof + "', '" + uniCx + "', '" + cjp + "', '" + ean + "', '" + atv + "')";
                        objCon.rs = objCon.stmt.executeQuery(sq);

                    } catch (SQLException ex) {
                        System.out.println("erro pln0055r");
                    }

                    try {
                        objCon.con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                cont++;
            } else {
                break;
            }

            linha = buffRead.readLine();
        }

        return true;
    }

    public void atualizarPln0055r() {
        deletarDados("pln0055r");

        System.out.println("atualizando pln0055r");
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String sq = "LOAD DATA LOCAL INFILE '" + pastaRem + "pln0055r.csv' INTO TABLE pln0055r FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' (est, pra, codigo, descricao, altura, largura, profundidade, embp, cjp, ean, ativo)";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.executeUpdate();

            objCon.stmt.close();

            pst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM pln0055r WHERE ean = 0";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir ean 0");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deletarDados(String tabela) {

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM " + tabela + "";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao deletar " + tabela);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deletarVagosPbl5050m() {

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM pbl5050m WHERE descricao = 'ENDERECO VAGO          '";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao deletar Endereços vagos pbl5050m");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean bloquear() {
        btnExec.setEnabled(false);
        eDatAt.setText("Atualizando ...");

        boolean valor = true;
        int modulo = 1;
        int bloquear = 1;
        try {

            objCon.openConnectionMysql();

            objCon.stmt = objCon.con.createStatement();

            String sq = "UPDATE configuracoes SET bloqueio = ? WHERE numero = ?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setInt(1, bloquear);
            pst.setInt(2, modulo);

            pst.executeUpdate();

            pst.close();

            valor = true;

            System.out.println("Bloqueado!");

        } catch (SQLException ex) {
            valor = false;
            System.out.println("Erro no bloqueio geral!");
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }

        return valor;

    }

    public boolean desbloquear() {
        btnExec.setEnabled(true);
        eDatAt.setText("");

        boolean valor = true;
        int modulo = 1;
        int bloquear = 0;
        try {

            objCon.openConnectionMysql();

            objCon.stmt = objCon.con.createStatement();

            String sq = "UPDATE configuracoes SET bloqueio = ? WHERE numero = ?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setInt(1, bloquear);
            pst.setInt(2, modulo);

            pst.executeUpdate();

            pst.close();

            valor = true;

            System.out.println("Desbloqueado!");

        } catch (SQLException ex) {
            valor = false;
            System.out.println("Erro no bloqueio geral!");
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }

        return valor;

    }

    public void atualizaDataAtz() throws ParseException {

        DataHora dh = new DataHora();
        String horaAtualZ = dh.Hora();
        String dataAtualZ = dh.Data();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataMsl = new java.sql.Date(format.parse(dataAtualZ).getTime());

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String sq = "UPDATE configuracoes SET data = ?, hora = ? WHERE numero = ?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setDate(1, dataMsl);
            pst.setString(2, horaAtualZ);
            pst.setInt(3, 1);

            pst.executeUpdate();

            pst.close();

            System.out.println("Data Atualizada!");

        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }

    }

    public boolean verifUso() {

        boolean uso = true;
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from configuracoes WHERE numero = 1";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            int bloc = objCon.rs.getInt("bloqueio");

            if (bloc == 1) {
                uso = false;
            } else {
                uso = true;

            }

        } catch (SQLException ex) {
            uso = false;
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uso;
    }

    public boolean atzPbl5050m() throws FileNotFoundException, IOException, SQLException, ParseException {
        System.out.println("Atualiando pbl5050m");

        File arquivo = new File(global.arquivoRemoto + "pbl5050m.csv");

        deletarDados("pbl5050m");

        BufferedReader buffRead = new BufferedReader(new FileReader(arquivo));
        String linha = "";

        int cont = 0;

        while (true) {
            String dataNew = "";
            if (linha != null) {
                if (cont > 3) {

                    String array[] = new String[9];

                    array = linha.split(";");

                    if (!array[2].equals("   ")) {

                        try {

                            array[0] = array[0].replace(" ", "");
                            array[6] = array[6].replace(" ", "");
                            java.sql.Date dataMysql;

                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                dataMysql = new java.sql.Date(format.parse(array[8]).getTime());
                                dataNew = array[8];
                            } catch (Exception e) {
                                dataNew = "01/01/2032";
                                String dtErr = "01/01/2032";
                                array[7] = "9999999";
                                System.out.println(cont);
                                System.out.println(array[4]);
                                dataMysql = new java.sql.Date(format.parse(dtErr).getTime());
                                System.out.println("erro data");

                            }

                            SimpleDateFormat formaData = new SimpleDateFormat("yyyy-MM-dd");
                            String dataFinal = formaData.format(dataMysql);

                            objCon.openConnectionMysql();
                            objCon.stmt = objCon.con.createStatement();

                            String sq = "INSERT INTO pbl5050m (rua, endereco, estacao, prateleira, codigo, descricao, saldo, lote, validade, data_mysql) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement pst = objCon.con.prepareStatement(sq);

                            pst.setString(1, array[0]);
                            pst.setString(2, array[1]);
                            pst.setString(3, array[2]);
                            pst.setString(4, array[3]);
                            pst.setString(5, array[4]);
                            pst.setString(6, array[5]);
                            pst.setString(7, array[6]);
                            pst.setString(8, array[7]);
                            pst.setString(9, dataNew);
                            pst.setDate(10, dataMysql);

                            pst.executeUpdate();

                            pst.close();

                        } catch (SQLException ex) {
                            System.out.println("erro mb52");
                        }

                        try {
                            objCon.con.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Atualizacao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                cont++;
            } else {
                break;
            }

            linha = buffRead.readLine();
        }

        return true;
    }
}
