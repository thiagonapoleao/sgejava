/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author lucas.vieira
 */
public class ConfProperties {
    Properties props = new Properties();
    String caminho;
    
    public ConfProperties(String caminho) throws IOException {
        this.caminho = caminho;
        LoadProps();
    }

    
    public Properties LoadProps() throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(caminho);
        props.load(file);
        
        file.close();
        return props;
    }
    
    
    public void setProp(String propriedade, String valor) throws FileNotFoundException, IOException {
        props.setProperty(propriedade, valor);
        props.store(new FileOutputStream(caminho), "Configuracoes");
    }
    
    
    public String getProp(String prop) {
        String propriedade;
        
        propriedade = props.getProperty(prop);
        
        return propriedade;
    }

}
