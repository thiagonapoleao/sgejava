/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author lucas.vieira
 */
public class LoadComboBox {
    public LoadComboBox(String sql, String objeto) throws SQLException {
      /*  
        Banco  objCon = new Banco();
        
        objCon.openConnectionMysql();
        objCon.stmt = objCon.con.createStatement();
        
        PreparedStatement pst = cbd.con.prepareStatement(sql);
        objCon.rs = pst.executeQuery();
        while (objCon.rs.next()){
            String turno = objCon.rs.getString("turno");
            objeto.getClass().addItem(turno);
        }
        */
        
    }
}
