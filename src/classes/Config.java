/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas.vieira
 * 
 */

public class Config {
    private boolean erroLocal = false;
    public String dirLocal = "C:\\sgejava\\sge\\";
    public String arquivoLocal = "C:\\sgejava\\sge\\conf.properties";
    public String arquivoRemoto;//diretorio remoto
    public String impZebra;
    public String impA4;
    public String ipServ;
    public String dataB;
    public String portaMysql;
    public String userFtp;
    public String senhaFtp;
    public String dirFtp;
    public String versao;
    
    
    public Config() {
        
        try {
            loadProperties();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void loadProperties() throws FileNotFoundException, IOException {
        
        ConfProperties conf = new ConfProperties(this.arquivoLocal);
        
        this.arquivoRemoto = conf.getProp("prop.pastaremota");
        this.ipServ = conf.getProp("prop.ip");
        this.impZebra = conf.getProp("prop.zebra");
        this.impA4 = conf.getProp("prop.impa4");
        this.dataB = conf.getProp("prop.serv");
        this.portaMysql = conf.getProp("prop.portmysql");
        this.userFtp = conf.getProp("prop.uftp");
        this.senhaFtp = conf.getProp("prop.sftp");
        this.dirFtp = conf.getProp("prop.dirftp");
        this.versao = conf.getProp("prop.versao");
    }
    
    
    public void carregaIpRemoto() throws IOException {
        System.out.println(this.arquivoRemoto);
        File rFile = new File(this.arquivoRemoto);
        
        if(rFile.exists()) {
            ConfProperties confRem = new ConfProperties(this.arquivoRemoto);
            this.ipServ = confRem .getProp("prop.ip");
            ConfProperties conP = new ConfProperties(this.arquivoLocal);
            conP.setProp("prop.ip", this.ipServ);
        }
    }
    
    
    public void alteraProp(String propriedade, String valor) throws IOException {
        ConfProperties conP = new ConfProperties(this.arquivoLocal);
        conP.setProp(propriedade, valor);
    }

}
