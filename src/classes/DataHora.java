/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author lucas.vieira
 */
public class DataHora {
    
    String dataAtual;
    String horas;
    Date data = new Date();

    public String Data() {
        
        SimpleDateFormat formaData= new SimpleDateFormat("dd/MM/yyyy");
        dataAtual = formaData.format(data);

        
        return dataAtual;
    }
    
    public String Hora() {
        
        SimpleDateFormat formaData = new SimpleDateFormat("HH:mm:ss");
        horas = formaData.format(data);

        
        return horas;
    }
    
}
