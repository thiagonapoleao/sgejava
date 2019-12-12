/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author lucas.vieira
 */
public class EtqPranchao {
    public static Banco  objCon = new Banco();
    
    JTextField areaStzTxt;
    JTextField estacaoTxt;
    JTextField nivelTxt;
    
    JRadioButton todosR;
    JRadioButton pNivelR;
    
    String SQL = "";
    
    JButton btnImprimir;
    
    ImprimirZebra impressao = new ImprimirZebra();
    
    public String rua = "";
    public String nivel = "";
    public String excesso = "";
    
    public String area = "xxxx"; // R06.3.56
    public String primeiroNum = "1";//3
    public String ultimoNum = "5";//3
    public String estacao = "20";//2
    public String sequencia = "lucas";//55
    public String barcode = ""; //R>50635>66    (R normal)  (>5 reservado) (0635 noraml) (>6 reservado) (6)normal
    
    
    
    public EtqPranchao(JTextField jAreaStcruz, JTextField jEstacao, JTextField jNivel, JRadioButton jTodos, JRadioButton jPNivel, JButton btnImp) throws PrintException {
        
        areaStzTxt = jAreaStcruz;
        estacaoTxt = jEstacao;
        nivelTxt = jNivel;
    
        todosR = jTodos;
        pNivelR = jPNivel;
    
        btnImprimir = btnImp;
        
             
    }
    
    
    public void processaInfo() throws PrintException {
        if(!areaStzTxt.getText().equals("")) {
            SQL = "SELECT * from area_stcruz WHERE area_stz = '"+areaStzTxt.getText()+"'";
        }else{
            if(todosR.isSelected()) {
                SQL = "SELECT * from area_stcruz WHERE estacao = '"+estacaoTxt.getText()+"' ORDER BY area_stz ASC";
            }else{
                SQL = "SELECT * from area_stcruz WHERE estacao = '"+estacaoTxt.getText()+"' AND nivel = '"+nivelTxt.getText()+"' ORDER BY area_stz ASC";
            }
        }
        
        
        if(!areaStzTxt.getText().equals("") || !estacaoTxt.getText().equals("")) {
            imprimir();
        }else{
            JOptionPane.showMessageDialog(null, "Dados informados invalidos!");
        }
        
        areaStzTxt.setText("");
        estacaoTxt.setText("");
        nivelTxt.setText("");
        
        areaStzTxt.requestFocus();
        
        
    }
    
    
    
    
    
    
    
    
    
    public String montaBarcode(String ra, String nv, String ex) {
        String finalEst = "";
        
        String fixo1 = "R";
        String reservado1 = ">5";
        String reservado2 = ">6";
        
        String r = "";
        String n = "";
        String e1 = "";
        String e2 = "";
        
        if(ra.length() == 1) {
            r = "0" + ra;
        }else{
            r = ra;
        }
                
        n = nv;
        
        if(ex.length() == 1) {
            e1 = "0";
            e2 = ex;
        }else{
            e1 = ex.substring(0, 1);
            e2 = ex.substring(1, 2);
        }
        
        finalEst = fixo1 + reservado1 + r + n + e1 + reservado2 + e2;
        
        
        //System.out.println("montagem barcode realizada");
        
        
        return finalEst;
    }
    
    public String acEstacao(String est) {
        
        String esta = "";
        
        if(est.length() == 1) {
            esta = "0" + est;
        }else{
            esta = est;
        }
        
        //System.out.println("estacao realizada");
        
        return esta;
    }
    
    public String acSequencia(String seq) {
        String seqNova = "";
        int quantS = 0;
        int quantF = 0;
        double cada = 0;
        int cada2 = 0;
        String cadaS = "";
        
        quantS = seq.length();
        
        if(quantS < 90) {
            
            quantF = 90 - quantS;
            
            if(quantF % 2 == 1) {
                quantF = quantF - 1;
                cada = quantF / 2;
            }else{
                cada = quantF / 2;
            }
            
            cada2 = (int) cada;
            
            for(int i = 0; i <= cada2 ; i++) {
                cadaS = cadaS + "_";
            }
            
            seqNova = cadaS + seq + cadaS;
            
        }else{
            seqNova = seq;
        }
        
        
  
        
        //System.out.println("sequencia realizada");
        
        return seqNova;
    }
    
    public String acPrimeiroNum(String pN) {
        
        String numRec = pN;
        
        if(numRec.length() == 1) {
            numRec = "00" + numRec;
        }else{
            if(numRec.length() == 2) {
               numRec = "0" + numRec; 
            }
        }
        
        //System.out.println("primeiroNum realizado");
        
        return numRec;
    }
    
    public String acUltimoNum(String uN) {
        
        String numRec = uN;
        
        if(numRec.length() == 1) {
            numRec = "00" + numRec;
        }else{
            if(numRec.length() == 2) {
               numRec = "0" + numRec; 
            }
        }
        
        //System.out.println("ultimoNum realizado");
        
        return numRec;
    }
    
    public String acArea(String ra, String nv, String ex) {
        
        String areaF = "";
        String ru = "";
        String niv = "";
        String exc = "";
        
        if(ra.length() == 1) {
            ru = "0" + ra;
        }else{
            ru = ra;
        }
        
        niv = nv;
        
        if(ex.length() == 1) {
            exc = "0" + ex;
        }else{
            exc = ex;
        }
        
        areaF = "R" + ru + "." + niv + "." + exc;
        
        //System.out.println("area realizada");
        
        return areaF;
    }
    
    
    public void realizaImpressao() throws PrintException {
        impressao.ImprimirZebra();
        
        impressao.imprime(
                "^XA\n" +
                "^MMT\n" +
                "^PW639\n" +
                "^LL1998\n" +
                "^LS0\n" +
                "^FT521,1417^A0R,51,50^FH\\^FDDist. Med. SantaCruz^FS\n" +
                "^FT523,49^A0R,51,50^FH\\^FDPosi\\87\\C6o: "+area+"^FS\n" +
                "^BY3,3,98^FT434,54^BCI,,Y,N\n" +
                "^FD>:"+barcode+"^FS\n" +
                "^FT47,107^A0R,39,38^FH\\^FD"+sequencia+"^FS\n" +
                "^FT184,1403^A0R,310,307^FH\\^FD"+ultimoNum+"^FS\n" +
                "^FT207,853^A0R,310,307^FH\\^FD"+estacao+"^FS\n" +
                "^FT181,176^A0R,310,307^FH\\^FD"+primeiroNum+"^FS\n" +
                "^LRY^FO109,718^GB0,585,392^FS^LRN\n" +
                "^PQ1,0,1,Y^XZ");
        
        
        
        /*
        impressao.imprime(
                "^XA\n" +
                "^MMT\n" +
                "^PW639\n" +
                "^LL1998\n" +
                "^LS0\n" +
                "^FT521,1417^A0R,51,50^FH\\^FDDist. Med. SantaCruz^FS\n" +
                "^FT523,49^A0R,51,50^FH\\^FDPosi\\87\\C6o: "+area+"^FS\n" +
                "^BY3,3,98^FT434,54^BCI,,Y,N\n" +
                "^FD>:"+barcode+"^FS\n" +
                "^FT43,40^A0R,48,48^FH\\^FD"+sequencia+"^FS\n" +
                "^FT184,1403^A0R,310,307^FH\\^FD"+ultimoNum+"^FS\n" +
                "^FT207,853^A0R,310,307^FH\\^FD"+estacao+"^FS\n" +
                "^FT181,176^A0R,310,307^FH\\^FD"+primeiroNum+"^FS\n" +
                "^LRY^FO109,718^GB0,585,392^FS^LRN\n" +
                "^PQ1,0,1,Y^XZ");
        
        */
    }
    
    public void mostraValores() {
        System.out.println(area);
        System.out.println(barcode);
        System.out.println(sequencia);
        System.out.println(primeiroNum);
        System.out.println(estacao);
        System.out.println(ultimoNum);
    }
    
    
    public void imprimir() throws PrintException {

        try {
      
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();
        
                //SQL = "SELECT * from area_stcruz WHERE area_stz = '"+areaBusca+"'";
                objCon.rs = objCon.stmt.executeQuery(SQL);
                
                //objCon.rs.first();
                while (objCon.rs.next()){
                
                rua = objCon.rs.getString("rua");
                nivel = objCon.rs.getString("nivel");
                excesso = objCon.rs.getString("excesso");
                
                area = objCon.rs.getString("area_stz");
                estacao = objCon.rs.getString("estacao");
                String firstNum = objCon.rs.getString("pos_ini");
                String ultNum = objCon.rs.getString("pos_fim");
                sequencia = objCon.rs.getString("sequencia");
                
                //System.out.println("feito");
                
                
                area = acArea(rua, nivel, excesso);
                primeiroNum = acPrimeiroNum(firstNum);
                ultimoNum = acUltimoNum(ultNum);
                estacao = acEstacao(estacao);
                sequencia = acSequencia(sequencia);
                barcode = montaBarcode(rua, nivel, excesso);
                
            
                realizaImpressao();
                //mostraValores();
            }
                
            } catch (SQLException ex) {
                System.out.println("erro");
                JOptionPane.showMessageDialog(null, "Area SantaCruz Inexistente!");
            }
            
       
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EtqPranchao.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
    }
    
    public void rBtnTodos() {
        
        if(pNivelR.isSelected()) {
            todosR.setSelected(true);
            pNivelR.setSelected(false);
        }else{
            todosR.setSelected(true);
        }         
    }
    
    public void rBtnPNivel() {
        
        if(todosR.isSelected()) {
            pNivelR.setSelected(true);
            todosR.setSelected(false);
        }else{
            pNivelR.setSelected(true);
        }
    }
    
    
    public void zeraCampos() {
        areaStzTxt.requestFocus();
        areaStzTxt.setText("");
        estacaoTxt.setText("");
        nivelTxt.setText("");
    
        todosR.setSelected(false);
        pNivelR.setSelected(true);
    }
}
