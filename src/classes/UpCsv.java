package classes;

import java.io.File;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;

/*
 *
 * @author lucas.vieira
 * 
 */

public class UpCsv {
    Banco  objCon = new Banco();
    
    
    
    
    public UpCsv() throws IOException {
        //String sq = "LOAD DATA LOW_PRIORITY LOCAL INFILE '"+patch+"' INTO TABLE curva_abc CHARACTER SET latin1 FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"' LINES TERMINATED BY '\r\n' (endereco, pfat, sap, fornec, curva, descricao, ven_d, ven_m, ven_d_l, ven_m_l, ac_d, ac_m, med_sem, emb, conj, est)";
        movArq();
        
        
    }
    
    public void movArq() throws IOException {
        FTPClient ftp = new FTPClient();
        
        ftp.connect( "10.16.0.185" );

        ftp.login( "default", "123456" );

        ftp.changeWorkingDirectory ("repossge");

        String[] arq = ftp.listNames();

        System.out.println ("Listando arquivos: \n");

        for (String f : arq){
            System.out.println(f);            
        }
    }
    
    
    
}
