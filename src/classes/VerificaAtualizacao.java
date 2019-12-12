package classes;

import frames.Login;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author lucas.vieira
 *
 */

public class VerificaAtualizacao {
    
    Banco  objCon = new Banco();
    
    public VerificaAtualizacao() {
        
    }
    
    
    
    public boolean atualizar(String versaoSis) {
        boolean nesAtz = false;
        
        String vl = "";
        try {
      
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
        
            String SQL = "SELECT * from configuracoes WHERE numero = 4";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            
            objCon.rs.first();
            
            vl = objCon.rs.getString("parametro6");
            
            if(vl.equals(versaoSis)) {
                nesAtz = false;
            }else{
                nesAtz = true;
            }

            } catch (SQLException ex) {
                nesAtz = false;
            }
       
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        return nesAtz;
    }
}
