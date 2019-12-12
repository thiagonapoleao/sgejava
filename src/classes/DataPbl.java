/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import frames.Login;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import classes.Banco;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lucas.vieira
 */
public class DataPbl {
    Banco  objCon = new Banco();
    
    public DataPbl(){
        
    }
    
    public void alteraDatas2(String cod, String dat) throws ParseException {
        

            Date dataF;
            String dataTemp = "";
            String dia = "";
            
            String mes = "";
            
            String ano = "";
            

            
            if(!dat.equals("")) {
              //  dia = dat.substring(0,2);
               // mes = dat.substring(3,5);
               // ano = dat.substring(6,10);
               // System.out.println("dia , " + dia);
               // System.out.println("mes , " + mes);
               // System.out.println("ano , " + ano);
               // dataTemp = ano + "-" + mes + "-" + dia;
                
               // System.out.println("Data Mysql : " + dtMysql);
               //SimpleDateFormat formaData= new SimpleDateFormat("yyyy/MM/dd");
               // = formaData.format(dataTemp);
               
               SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
               java.sql.Date  dataMysql= new java.sql.Date(format.parse(dat).getTime());
               
               //Date dtMysql = new Date(dataF);
               
               SimpleDateFormat formaData= new SimpleDateFormat("yyyy-MM-dd");
               String dataFinal = formaData.format(dataMysql);
               
                System.out.println(dataFinal);
                System.out.println("data " + dat + " codigo " + cod);
            
            try {

            String SQL = "UPDATE pbl5050m SET data_mysql = '"+dataFinal+"' WHERE codigo = '"+cod+"' AND validade = '"+dat+"'";
            objCon.stmt.executeQuery(SQL);
            
            System.out.println("alteração realizada!");
           
            
           
            } catch (SQLException ex) {
                System.out.println("erro");
            }
        

        } 
    }
    
    
    public void alteraDatas() throws ParseException {
        try {
            
            String cod = "";
            String data = "";
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
        
            String SQL = "SELECT * from pbl5050m";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            
            
            
            while (objCon.rs.next()){
                cod = objCon.rs.getString("codigo");
                data = objCon.rs.getString("validade");
                         
              //  System.out.println(cod + " , " + data);
              if(!data.equals("")) {
              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
               java.sql.Date  dataMysql= new java.sql.Date(format.parse(data).getTime());
               
               //Date dtMysql = new Date(dataF);
               
               SimpleDateFormat formaData= new SimpleDateFormat("yyyy-MM-dd");
               String dataFinal = formaData.format(dataMysql);
               
                System.out.println(dataFinal);
                System.out.println("data " + data + " codigo " + cod);

                String sq = "update pbl5050m set data_mysql =? where codigo =? and validade =?";
                PreparedStatement pst = objCon.con.prepareStatement(sq);
           
            
            pst.setDate(1, dataMysql);
            pst.setString(2, cod);
            pst.setString(3, data);


            pst.executeUpdate();
            
            pst.close();
                
                
              }
              
            }

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
