
package classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucas Vieira
 * 
 **/

public class Banco {
    Config global = new Config();
    
    public String ipConexao = global.ipServ;
    public Connection con;
    public Statement stmt;
    public ResultSet rs;
    
    
    
    String url1 = "jdbc:mysql://";
    String url2 = ":" + global.portaMysql +"/";
    String url3= global.dataB;
    String url = url1+ipConexao+url2+url3;
    
    
    //String url1 = "jdbc:mysql://";
    //String url2 = ":8899/eergteste";
    //String url2 = ":8899/eerg";
    //String url = url1+ipConexao+url2;
    //String url = "jdbc:mysql://10.16.1.163:8899/eergteste";
    //String url = "jdbc:mysql://10.16.1.163:8899/eerg";
    
    //String user = "root";
    //String password = "";
    
    String user = "";
    String password = "";
    String driver = "com.mysql.jdbc.Driver";
    
    public void openConnectionMysql() {
        //System.out.println(url);
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, user, password);
            
            stmt = con.createStatement();
        }catch(Exception e) {
           // e.printStackTrace();
            System.out.println("erro");
        }
        
    }

    PreparedStatement prepareStatement(String sq) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

   
}
