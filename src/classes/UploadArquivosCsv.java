package classes;


import classes.DataPbl;
import frames.Login;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 *
 * @author lucas.vieira
 * 
 **/


public class UploadArquivosCsv {
    
    
    public int progress = 0;
    public boolean execut = false;
    
    
    public UploadArquivosCsv(File pln0055r, String patchPln0055r, String relPln0055r, File curvaAbc, String patchCurvaAbc, String relCurvaAbc,
    File mb52, String patchMb52, String relMb52, File pbl5050m, String patchPbl5050m, String relPbl5050m) throws FileNotFoundException, IOException, ParseException, SQLException, InterruptedException {
        
        
        execut = true;
        boolean upOk = true; 
        boolean ver = verifUso();
        if(ver == true) {
            upOk = true;
        }else{
            upOk = false;
        }
        
        
        System.out.println("Bloqueando acesso ao banco de dados..."); 
        if(upOk){bloquear();}
        progress = progress + 15;
        incrementaCont(progress);
        
        if(upOk){if(atzPln0055r(pln0055r, patchPln0055r, relPln0055r)){System.out.println("Pln0055r atualizado !"); progress = progress + 15; incrementaCont(progress);}else{upOk = false;}}
        if(upOk){if(atzCurvaAbc(curvaAbc, patchCurvaAbc, relCurvaAbc)){System.out.println("Curva Abc atualizado !");progress = progress + 15; incrementaCont(progress);}else{upOk = false;}}
        if(upOk){if(atzMb52(mb52, patchMb52, relMb52)){System.out.println("Mb52 atualizado !");progress = progress + 15; incrementaCont(progress);}else{upOk = false;}}
        if(upOk){if(atzPbl5050m(pbl5050m, patchPbl5050m, relPbl5050m)){System.out.println("Pbl5050m atualizado !");progress = progress + 15; incrementaCont(progress);}else{upOk = false;}}
        
        if(upOk){desbloquear();}
        progress = progress + 24;
        incrementaCont(progress);
        
        Thread.sleep(2000);
        progress = progress + 1;
        incrementaCont(progress);
        
        incrementaCont(0);
        
        if(upOk == false) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro !");
        }else{
            atualizaDataAtz();
        }


     }
    
    public boolean bloquear()  {
        Banco  objCon = new Banco();
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
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            
        } 
        
        return valor;

    }
    
    
    public boolean atzPln0055r(File pln0055r, String patchPln0055r, String relPln0055r) throws FileNotFoundException, IOException, ParseException {
        Banco  objCon = new Banco();
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

        BufferedReader buffRead = new BufferedReader(new FileReader(pln0055r));
        String linha = "";
        
        int tempCod = 0;
        int tempAlt = 0;
        int tempLarg = 0;
        int tempProf = 0;
        int tempUni = 0;
        int tempCjp = 0;
        
        int cont = 0;
        
        while(true) {
            if(linha != null) {
                if(cont > 2) {
                     est = linha.substring(0, 3);
                     prt = linha.substring(4, 7);
                     cod = linha.substring(8, 18);
                     desc = linha.substring(19,51);
                     alt = linha.substring(52, 57);
                     larg = linha.substring(58, 63);
                     prof = linha.substring(64, 69);
                     uniCx = linha.substring(70, 75);
                     cjp = linha.substring(76, 79);
                     ean = linha.substring(80, 93);
                     atv = linha.substring(94,95);
                     
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
        
                        String sq = "INSERT INTO pln0055r (est, pra, codigo, descricao, altura, largura, profundidade, embp, cjp, ean, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pst = objCon.con.prepareStatement(sq);
           
            
                        pst.setString(1, est);
                        pst.setString(2, prt);
                        pst.setString(3, cod);
                        pst.setString(4, desc);
                        pst.setString(5, alt);
                        pst.setString(6, larg);
                        pst.setString(7, prof);
                        pst.setString(8, uniCx);
                        pst.setString(9, cjp);
                        pst.setString(10, ean);
                        pst.setString(11, atv);

                        pst.executeUpdate();
            
                        pst.close();

                    } catch (SQLException ex) {
                        //System.out.println("erro pln0055r");
                    }
        
                    try {
                        objCon.con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
                cont ++;
            }else{
                break;
            }
             
            linha = buffRead.readLine();
        }

        
        return true;
    }
    
    
    public boolean atzCurvaAbc(File curvaAbc, String patchCurvaAbc, String relCurvaAbc) throws FileNotFoundException, IOException {
        Banco  objCon = new Banco();
        deletarDados("curva_abc");

        BufferedReader buffRead = new BufferedReader(new FileReader(curvaAbc));
        String linha = "";
        
        System.out.println("ATE AQUI OK -- BUFFER DO ARQUIVO CARREGADO XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        
        
        int cont = 0;
        
        while(true) {
            if(linha != null) {
                if(cont > 4) {
                    String array[] = new String[16];
                    
                    array = linha.split(";");

                    try {
            
                        objCon.openConnectionMysql();
                        objCon.stmt = objCon.con.createStatement();
        
                        String sq = "INSERT INTO curva_abc (endereco, pfat, sap, fornec, curva, descricao, ven_d, ven_m, ven_d_l, ven_m_l, ac_d, ac_m, med_sem, emb, conj, est) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pst = objCon.con.prepareStatement(sq);
           
                        pst.setString(1, array[0]);
                        pst.setString(2, array[1]);
                        pst.setString(3, array[2]);
                        pst.setString(4, array[3]);
                        pst.setString(5, array[4]);
                        pst.setString(6, array[5]);
                        pst.setString(7, array[6]);
                        pst.setString(8, array[7]);
                        pst.setString(9, array[8]);
                        pst.setString(10, array[9]);
                        pst.setString(11, array[10]);
                        pst.setString(12, array[11]);
                        pst.setString(13, array[12]);
                        pst.setString(14, array[13]);
                        pst.setString(15, array[14]);
                        pst.setString(16, array[15]);

                        pst.executeUpdate();
            
                        pst.close(); 
                      
                      //String sq = "INSERT INTO curva_abc (endereco, pfat, sap, fornec, curva, descricao, ven_d, ven_m, ven_d_l, ven_m_l, ac_d, ac_m, med_sem, emb, conj, est) VALUES ("+array[0]+", "+array[1]+", "+array[2]+", "+array[3]+", "+array[4]+", "+array[5]+", "+array[6]+", "+array[7]+", "+array[8]+", "+array[9]+", "+array[10]+", "+array[11]+", "+array[12]+", "+array[13]+", "+array[14]+", "+array[15]+")";
                      //objCon.rs = objCon.stmt.executeQuery(sq);  
                      //objCon.rs.first();
                      

                    } catch (SQLException ex) {
                        System.out.println("erro no curva abc");
                    }
        
                    try {
                        objCon.con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }    
                }
                cont ++;
            }else{
                break;
            }
             
            linha = buffRead.readLine();
        }
        
        return true;
    }
    
    
    public boolean atzMb52(File mb52, String patchMb52, String relMb52) throws FileNotFoundException, IOException {
        Banco  objCon = new Banco();
        deletarDados("mb52");

        BufferedReader buffRead = new BufferedReader(new FileReader(mb52));
        String linha = "";
        
        int cont = 0;
        
        while(true) {
            if(linha != null) {
                if(cont > 2) {
                    String array[] = new String[15];
                    
                    array = linha.split(";");

                   int tesr = 0;
                    
                   try{
                       String teste = array[1];
                   }catch(Exception ex){
                       tesr = 1;
                   }
                   
                   if(tesr == 0) {
                   
                   
                    try {
                               
                        objCon.openConnectionMysql();
                        objCon.stmt = objCon.con.createStatement();
        
                        String sq = "INSERT INTO mb52 (material, descricao, cen, dep, umb, lote, unidades, valor_unidades,contr_qual, valor_contr_qual, bloqueado, valor_bloqueado, devolucoes, transito_te) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pst = objCon.con.prepareStatement(sq);
           

                        pst.setString(1, array[1]);
                        pst.setString(2, array[2]);
                        pst.setString(3, array[3]);
                        pst.setString(4, array[4]);
                        pst.setString(5, array[5]);
                        pst.setString(6, array[6]);
                        pst.setString(7, array[7]);
                        pst.setString(8, array[8]);
                        pst.setString(9, array[9]);
                        pst.setString(10, array[10]);
                        pst.setString(11, array[11]);
                        pst.setString(12, array[12]);
                        pst.setString(13, array[13]);
                        pst.setString(14, array[14]);


                        pst.executeUpdate();
            
                        pst.close();

                    } catch (SQLException ex) {
                        System.out.println("erro");
                    }
        
                    try {
                        objCon.con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }
                cont ++;
            }else{
                break;
            }
             
            linha = buffRead.readLine();
        }

        return true;
    }
    
    
    public boolean atzPbl5050m(File pbl5050m, String patchPbl5050m, String relPbl5050m) throws FileNotFoundException, IOException, SQLException, ParseException {
        Banco  objCon = new Banco();
        deletarDados("pbl5050m");

        BufferedReader buffRead = new BufferedReader(new FileReader(pbl5050m));
        String linha = "";
        
        int cont = 0;
        
        while(true) {
            if(linha != null) {
                if(cont > 3) {
                    
                    String array[] = new String[9];
                    
                    //System.out.println(linha);
                    
                    array = linha.split(";");
                    
                    if(!array[2].equals("   ")) {
                    
                    try {
                        
                        array[0] = array[0].replace(" ", "");
                        array[6] = array[6].replace(" ", "");
                        
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        java.sql.Date  dataMysql= new java.sql.Date(format.parse(array[8]).getTime());
               
                        //Date dtMysql = new Date(dataF);
               
                        SimpleDateFormat formaData= new SimpleDateFormat("yyyy-MM-dd");
                        String dataFinal = formaData.format(dataMysql);

                    
                    //System.out.println(linha);
                               
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
                        pst.setString(9, array[8]);
                        pst.setDate(10, dataMysql);

                        pst.executeUpdate();
            
                        pst.close(); 

                    } catch (SQLException ex ) {
                        System.out.println("erro mb52");
                    }
        
                    try {
                        objCon.con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                    }
                }
                cont ++;
            }else{
                break;
            }
             
            linha = buffRead.readLine();
        }
        
     /*   DataPbl aq = new DataPbl();
        try {
            aq.alteraDatas();
        } catch (ParseException ex) {
            System.out.println("Erro");
        }  */
        return true;
    }

    
    public void deletarDados(String tabela) {
        Banco  objCon = new Banco();
       try {
            
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String SQL = "DELETE FROM "+tabela+"";
            PreparedStatement pst = objCon.con.prepareStatement(SQL);
            
            pst.executeUpdate();
            
            pst.close();

        } catch (SQLException ex) {
                System.out.println("Erro ao deletar " + tabela);
        }
       
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
    public void incrementaCont(int num) {
        Banco  objCon = new Banco();
        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
            String sq = "update configuracoes set parametro4 =? where numero =?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setInt(1, num);
            pst.setInt(2, 1);

            pst.executeUpdate();
            
            pst.close();
              System.out.println("foi");
            } catch (SQLException ex) {
                System.out.println("incrementei nao");
            }
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
}
 
    
    public boolean desbloquear() {
       Banco  objCon = new Banco();
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
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            
        } 
        
        return valor;

    }
    
    public void atualizaDataAtz() throws ParseException {
        Banco  objCon = new Banco();
        DataHora dh = new DataHora();
        String horaAtualZ = dh.Hora();
        String dataAtualZ = dh.Data();
            
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date  dataMsl= new java.sql.Date(format.parse(dataAtualZ).getTime());
               
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
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            
        } 
        
    }
    
    
    
    
    public boolean verifUso() {
        Banco  objCon = new Banco();
        boolean uso = true;
        try {
      
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
        
            String SQL = "SELECT * from configuracoes WHERE numero = 1";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            
            objCon.rs.first();
            
            int bloc = objCon.rs.getInt("bloqueio");
            
            if(bloc == 1) {
                uso =  false;
            }else{
                uso =  true;

            }
            
            

            } catch (SQLException ex) {
                uso =  false;
            }
       
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            return uso;
    }

}

