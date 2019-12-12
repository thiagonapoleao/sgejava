package classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author lucas.vieira
 *
 */

public class RelatorioEmissaoEtiquetas {

    JTable rTabela;

    JTextField dataIniTxt;
    JTextField dataFimTxt;
    JTextField prodPfatTxt;

    JCheckBox onlyZeroTxt;
    JCheckBox desconsidZeroTxt;

    JButton processarBtn;

    public RelatorioEmissaoEtiquetas(JTable tabela, JTextField txtDtIni, JTextField txtDtFim, JTextField txtProdPfat, JCheckBox chkSomenteZero, JCheckBox chkDescZero, JButton btnProcess) {

        rTabela = tabela;
        dataIniTxt = txtDtIni;
        dataFimTxt = txtDtFim;
        prodPfatTxt = txtProdPfat;
        onlyZeroTxt = chkSomenteZero;
        desconsidZeroTxt = chkDescZero;
        processarBtn = btnProcess;

    }

    public void processarTabela() throws ParseException {
        Banco objCon = new Banco();
        String dataInicio = validaData(dataIniTxt.getText());
        String dataFim = validaData(dataFimTxt.getText());
        String prodPfat = prodPfatTxt.getText();

        String sqlString1 = "";
        String sqlString2 = "";
        String sqlString3 = "";
        String sqlString4 = "";
        String sqlString5 = "";
        String sqlString6 = "";
        String sqlString7 = "";

        String sqlString = "";

        sqlString1 = "select * from log_emissao_etq WHERE data BETWEEN ('" + dataInicio + "') and ('" + dataFim + "')";
        sqlString7 = " ORDER BY data DESC, hora DESC";

        int somenteZerados = 0;
        int descZero = 0;

        if (!prodPfat.equals("")) {
            sqlString4 = "and codigo ='";
            sqlString5 = prodPfat;
            sqlString6 = "'";
        }

        if (onlyZeroTxt.isSelected()) {
            somenteZerados = 1;
            desconsidZeroTxt.setSelected(false);
            descZero = 0;
            System.out.println("somente zero secelionado!");
            sqlString2 = " and zerado =";
            sqlString3 = "1";
        }

        if (desconsidZeroTxt.isSelected()) {
            onlyZeroTxt.setSelected(false);
            somenteZerados = 0;
            System.out.println("desconsiderar zero secelionado!");
            sqlString2 = " and zerado =";
            sqlString3 = "0";
        }

        sqlString = sqlString1 + sqlString2 + sqlString3 + sqlString4 + sqlString5 + sqlString6 + sqlString7;

        try {
            ArrayList dados = new ArrayList();

            String[] Colunas = new String[]{"Codigo", "Estação", "Descrição", "Emissor", "Validade", "Data Emissão", "Hora"};
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = sqlString;
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            do {
                Date dataR = objCon.rs.getDate("data");
                SimpleDateFormat formaData = new SimpleDateFormat("dd/MM/yyyy");
                String dataAtual = formaData.format(dataR);

                dados.add(new Object[]{objCon.rs.getInt("codigo"), objCon.rs.getString("estacao"), objCon.rs.getString("descricao"), objCon.rs.getString("emissor"), objCon.rs.getString("validade"), dataAtual, objCon.rs.getString("hora")});
            } while (objCon.rs.next());

            Tabela tabela = new Tabela(dados, Colunas);
            rTabela.setModel(tabela);
            rTabela.getColumnModel().getColumn(0).setPreferredWidth(70);
            rTabela.getColumnModel().getColumn(0).setResizable(false);
            rTabela.getColumnModel().getColumn(1).setPreferredWidth(100);
            rTabela.getColumnModel().getColumn(1).setResizable(false);
            rTabela.getColumnModel().getColumn(2).setPreferredWidth(300);
            rTabela.getColumnModel().getColumn(2).setResizable(false);

            rTabela.getColumnModel().getColumn(3).setPreferredWidth(167);
            rTabela.getColumnModel().getColumn(3).setResizable(false);

            rTabela.getColumnModel().getColumn(4).setPreferredWidth(100);
            rTabela.getColumnModel().getColumn(4).setResizable(false);
            rTabela.getColumnModel().getColumn(5).setPreferredWidth(110);
            rTabela.getColumnModel().getColumn(5).setResizable(false);
            rTabela.getColumnModel().getColumn(6).setPreferredWidth(90);
            rTabela.getColumnModel().getColumn(6).setResizable(false);
            rTabela.getTableHeader().setReorderingAllowed(false);
            rTabela.setAutoResizeMode(rTabela.AUTO_RESIZE_OFF);
            rTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        }catch(Exception e) {

            ArrayList dadoss = new ArrayList();

            String[] Colunass = new String[]{"Codigo", "Estação", "Descrição", "Emissor", "Validade", "Data Emissão", "Hora"};

            dadoss.removeAll(dadoss);
            Tabela tabelal = new Tabela(dadoss, Colunass);
            rTabela.setModel(tabelal);
            rTabela.getColumnModel().getColumn(0).setPreferredWidth(70);
            rTabela.getColumnModel().getColumn(0).setResizable(false);
            rTabela.getColumnModel().getColumn(1).setPreferredWidth(100);
            rTabela.getColumnModel().getColumn(1).setResizable(false);
            rTabela.getColumnModel().getColumn(2).setPreferredWidth(300);
            rTabela.getColumnModel().getColumn(2).setResizable(false);
            rTabela.getColumnModel().getColumn(3).setPreferredWidth(167);
            rTabela.getColumnModel().getColumn(3).setResizable(false);
            rTabela.getColumnModel().getColumn(4).setPreferredWidth(100);
            rTabela.getColumnModel().getColumn(4).setResizable(false);
            rTabela.getColumnModel().getColumn(5).setPreferredWidth(110);
            rTabela.getColumnModel().getColumn(5).setResizable(false);
            rTabela.getColumnModel().getColumn(6).setPreferredWidth(90);
            rTabela.getColumnModel().getColumn(6).setResizable(false);
            rTabela.getTableHeader().setReorderingAllowed(false);
            rTabela.setAutoResizeMode(rTabela.AUTO_RESIZE_OFF);
            rTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }
    }

    public String validaData(String dataInformada) throws ParseException {

        int tamanhoData = dataInformada.length();

        String data = dataInformada;

        if (tamanhoData == 8) {

            String val1 = dataInformada.substring(0, 2);
            String val2 = dataInformada.substring(2, 4);
            String val3 = dataInformada.substring(4, 8);
            data = val3 + "-" + val2 + "-" + val1;

        } else {
            if (tamanhoData == 10) {
                String val1 = dataInformada.substring(0, 2);
                String val2 = dataInformada.substring(3, 5);
                String val3 = dataInformada.substring(6, 10);
                data = val3 + "-" + val2 + "-" + val1;
            } else {
                JOptionPane.showMessageDialog(null, "Datas informadas Invalidas!");
            }
        }

        return data;
    }

    public void dadosGeracaoRelatorio() throws ParseException, SQLException, IOException {
        String dataInicio = validaData(dataIniTxt.getText());
        String dataFim = validaData(dataFimTxt.getText());
        String prodPfat = prodPfatTxt.getText();

        String sqlString1 = "";
        String sqlString2 = "";
        String sqlString3 = "";
        String sqlString4 = "";
        String sqlString5 = "";
        String sqlString6 = "";
        String sqlString7 = "";

        String sqlString = "";

        sqlString1 = "select * from log_emissao_etq WHERE data BETWEEN ('" + dataInicio + "') and ('" + dataFim + "')";
        sqlString7 = " ORDER BY data DESC";

        int somenteZerados = 0;
        int descZero = 0;

        if (!prodPfat.equals("")) {
            sqlString4 = "and codigo ='";
            sqlString5 = prodPfat;
            sqlString6 = "'";
        }

        if (onlyZeroTxt.isSelected()) {
            somenteZerados = 1;
            desconsidZeroTxt.setSelected(false);
            descZero = 0;
            System.out.println("somente zero secelionado!");
            sqlString2 = " and zerado =";
            sqlString3 = "1";
        }

        if (desconsidZeroTxt.isSelected()) {
            onlyZeroTxt.setSelected(false);
            somenteZerados = 0;
            System.out.println("desconsiderar zero secelionado!");
            sqlString2 = " and zerado =";
            sqlString3 = "0";
        }

        sqlString = sqlString1 + sqlString2 + sqlString3 + sqlString4 + sqlString5 + sqlString6 + sqlString7;

        geraRelatorio(sqlString);
    }

    public void geraRelatorio(String srcSql) throws SQLException, IOException {
        Banco objCon = new Banco();
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
        }

        if (geraArq == 1) {

            FileWriter writer = new FileWriter(patchRec);
            try {
                writer.append("Data de emissao");
                writer.append(";");
                writer.append("Hora");
                writer.append(";");
                writer.append("Emissor");
                writer.append(";");
                writer.append("Usuario");
                writer.append(";");
                writer.append("Codigo PFAT");
                writer.append(";");
                writer.append("Descricao");
                writer.append(";");
                writer.append("Estacao");
                writer.append(";");
                writer.append("Validade");
                writer.append(";");
                writer.append("Reposicao urgente");
                writer.append("\n");

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = srcSql;
                objCon.rs = objCon.stmt.executeQuery(SQL);

                while (objCon.rs.next()) {
                    String nesRep = "";
                    String dataN = "";
                    String horaN = "";

                    SimpleDateFormat formaData = new SimpleDateFormat("dd/MM/yyyy");
                    dataN = formaData.format(objCon.rs.getDate("data"));

                    SimpleDateFormat formaHora = new SimpleDateFormat("HH:mm:ss");
                    horaN = formaHora.format(objCon.rs.getTime("hora"));

                    if (objCon.rs.getInt("zerado") == 1) {
                        nesRep = "Sim";
                    } else {
                        nesRep = "Nao";
                    }

                    try {
                        writer.append(dataN);
                        writer.append(";");
                        writer.append(horaN);
                        writer.append(";");
                        writer.append(objCon.rs.getString("emissor"));
                        writer.append(";");
                        writer.append(objCon.rs.getString("usuario"));
                        writer.append(";");
                        writer.append(objCon.rs.getString("codigo"));
                        writer.append(";");
                        writer.append(objCon.rs.getString("descricao"));
                        writer.append(";");
                        writer.append(objCon.rs.getString("estacao"));
                        writer.append(";");
                        writer.append(objCon.rs.getString("validade"));
                        writer.append(";");
                        writer.append(nesRep);
                        writer.append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                writer.flush();
                writer.close();

                JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(RelatorioEmissaoEtiquetas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /*
        
        JOptionPane.showMessageDialog(null, "Lembre-se : \n O arquivo deverá estar no formato .TXT");
            
        File   arquivoRec;
        String patchRec;
            
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int i= file.showSaveDialog(null);
            
        if (i==1){

            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
            } else {
            arquivoRec = file.getSelectedFile();
            patchRec = arquivoRec.getPath();
            
            
            
            String extensaoArq = patchRec.substring(patchRec.lastIndexOf(".") + 1);
            
            extensaoArq = extensaoArq.toUpperCase();
            
            
            if("CSV".equals(extensaoArq)) {


            
            }else{
                JOptionPane.showMessageDialog(null, "Arquivo Invalido! \n\n O arquivo deverá estar no formato .TXT");

            }
            
            
            }  */
    }
}
