/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;


/**
 *
 * @author lucas.vieira
 */
public class GerenciaAcesso {
    
    public String[] chave = new String[20];

    
    public void chaveAc(String acesso) {

        chave[0] = acesso.substring(0, 1);
        chave[1] = acesso.substring(1, 2);
        chave[2] = acesso.substring(2, 3);
        chave[3] = acesso.substring(3, 4);
        chave[4] = acesso.substring(4, 5);
        chave[5] = acesso.substring(5, 6);
        chave[6] = acesso.substring(6, 7);
        chave[7] = acesso.substring(7, 8);
        chave[8] = acesso.substring(8, 9);
        chave[9] = acesso.substring(9, 10);
        chave[10] = acesso.substring(10, 11);
        chave[11] = acesso.substring(11, 12);
        chave[12] = acesso.substring(12, 13);
        chave[13] = acesso.substring(13, 14);
        chave[14] = acesso.substring(14, 15);
        chave[15] = acesso.substring(15, 16);
        chave[16] = acesso.substring(16, 17);
        chave[17] = acesso.substring(17, 18);
        chave[18] = acesso.substring(18, 19);
        chave[19] = acesso.substring(19, 20); 
        
    }
}
