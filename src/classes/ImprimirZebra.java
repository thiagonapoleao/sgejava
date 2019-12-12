/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author lucas.vieira
 * 
 **/

public class ImprimirZebra {
    
    Config global = new Config();
    private static PrintService impressora;
    public void  ImprimirZebra() throws PrintException {
           detectaImpressoras();
    }
    
    
    public void detectaImpressoras() {
        try {
            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
            for (PrintService p: ps) {
                //System.out.println("Impressora encontrada: " + p.getName());
                if (p.getName().equals(global.impZebra)){
                   //if (p.getName().contains("Z6Mplus")){
	               // System.out.println("Impressora Selecionada: " + p.getName());
                    impressora = p;
                    break; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized boolean imprime(String texto) {
        
        if (impressora == null) {
            String msg = "Nennhuma impressora foi encontrada. Instale uma impressora padrão \r\n(Generic Text Only) e reinicie o programa.";
	       	System.out.println(msg);
        } else {
            try {
                DocPrintJob dpj = impressora.createPrintJob();
                InputStream stream = new ByteArrayInputStream(texto.getBytes());
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc doc = new SimpleDoc(stream, flavor, null);

                    dpj.print(doc, null);
                 //   System.out.println("impresso");
                    
                return true;
                
            } catch (PrintException e) {
                e.printStackTrace();
              //  System.out.println("não impresso");
            }
        }
        return false;
    }
    
}



