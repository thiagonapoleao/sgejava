/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import classes.Banco;
import classes.Config;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas.vieira
 */
public class Configuracoes extends javax.swing.JFrame {
    Banco  objCon = new Banco();
    Config global = new Config();
    String nome;
    String usuario;
    String acesso;
    int adm;
    String senhaUser;
    
    
    /**
     * Creates new form Config
     */
    public Configuracoes(String nomeFunc) {
        initComponents();
        this.nome = nomeFunc;
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "impress");
        listarImpressoroas();
        
        impZebraAtual.setText(global.impZebra);
        impA4Atual.setText(global.impA4);
        carregaInfoUser();
    }
    
   public void listarImpressoroas() {
       String nomeImpressSel = "";
       cmbImpZebra.removeAllItems();
       try {
            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
            for (PrintService p: ps) {
                cmbImpZebra.addItem(p.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
       cmbImpA4.removeAllItems();
       try {
            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
            for (PrintService p: ps) {
                cmbImpA4.addItem(p.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
   }

   public void carregaInfoUser() {
       try {
      
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
        
            String SQL = "SELECT * from login WHERE nome = '"+this.nome+"'";
            objCon.rs = objCon.stmt.executeQuery(SQL);
            
            objCon.rs.first();
            this.usuario = objCon.rs.getString("usuario");
            this.acesso = objCon.rs.getString("acesso");
            this.adm = objCon.rs.getInt("admin");
            this.senhaUser = objCon.rs.getString("senha");
       }catch(SQLException ex) {
           ex.printStackTrace();
       }
           
       
            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            loadEdit(this.adm);
   }
   
   
   public void loadEdit(int ad) {
       if(ad == 0) {
           nomeColab.setFocusable(false);
           nomeColab.setText(nome);
           nomeColab.setEnabled(true);
           nomeColab.setEditable(false);
           
           userColab.setFocusable(false);
           userColab.setText(usuario);
           userColab.setEnabled(true);
           userColab.setEditable(false);
           
           senhaColab.setEnabled(true);
           rSenhaColab.setEnabled(true);
           
           btnAlterar.setEnabled(true);
           
           
       }else{

           nomeColab.setEnabled(true);
           userColab.setEnabled(true);
           senhaColab.setEnabled(true);
           rSenhaColab.setEnabled(true);
           btnVisualizar.setEnabled(true);
           btnAlterar.setEnabled(true);
           btnExcluir.setEnabled(true);
           
           
           cInicio.setEnabled(true);
           cRecebimento.setEnabled(true);
           cArmazenagem.setEnabled(true);
           cReposicao.setEnabled(true);
           cBalanceamento.setEnabled(true);
           cAtualizacoes.setEnabled(true);
           cConfiguracoes.setEnabled(true);
       }
   }
   
   
   
   public void alteraInfo(int tp) {
       if(tp == 1) {
           try {
                        
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();
        
            String SQL = "UPDATE login SET senha = ? WHERE usuario = ? ";

            PreparedStatement pst = objCon.con.prepareStatement(SQL);
           
            
            pst.setString(1, senhaColab.getText());
            pst.setString(2, usuario);



            pst.executeUpdate();
            
            pst.close();
                     
            JOptionPane.showMessageDialog(null, "Senha alterada com sucesso!");
            limpaCampos();
        } catch (SQLException ex) {
            System.out.println("erro ao alterar Senha");
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
   }
   
   public void limpaCampos() {
       nomeColab.setText("");
       userColab.setText("");
       senhaColab.setText("");
       rSenhaColab.setText("");
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        Root = new javax.swing.JPanel();
        Inicio = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        Impressoras = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbImpZebra = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        impZebraAtual = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        cmbImpA4 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        impA4Atual = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        Usuario = new javax.swing.JPanel();
        userColab = new javax.swing.JTextField();
        cInicio = new javax.swing.JCheckBox();
        cRecebimento = new javax.swing.JCheckBox();
        cArmazenagem = new javax.swing.JCheckBox();
        cReposicao = new javax.swing.JCheckBox();
        cConfiguracoes = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nomeColab = new javax.swing.JTextField();
        senhaColab = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        rSenhaColab = new javax.swing.JTextField();
        cBalanceamento = new javax.swing.JCheckBox();
        cAtualizacoes = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        btnVisualizar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        Configuracoes = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        patchPasta = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configurações");
        setResizable(false);

        Root.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 765, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout InicioLayout = new javax.swing.GroupLayout(Inicio);
        Inicio.setLayout(InicioLayout);
        InicioLayout.setHorizontalGroup(
            InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        InicioLayout.setVerticalGroup(
            InicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        Root.add(Inicio, "Inicio");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Impressora Zebra : ");

        jButton1.setText("Definir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Definir Impressoras");

        jLabel3.setText("Atualmente :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Impressora A4 : ");

        jLabel5.setText("Atualmente :");

        jButton2.setText("Definir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ImpressorasLayout = new javax.swing.GroupLayout(Impressoras);
        Impressoras.setLayout(ImpressorasLayout);
        ImpressorasLayout.setHorizontalGroup(
            ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImpressorasLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbImpZebra, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ImpressorasLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(impZebraAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 72, Short.MAX_VALUE))
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImpressorasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(228, 228, 228))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImpressorasLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImpressorasLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(impA4Atual, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImpressorasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbImpA4, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ImpressorasLayout.setVerticalGroup(
            ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImpressorasLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ImpressorasLayout.createSequentialGroup()
                        .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(impZebraAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbImpZebra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addGap(30, 30, 30)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImpressorasLayout.createSequentialGroup()
                        .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(impA4Atual, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ImpressorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbImpA4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)))
                    .addGroup(ImpressorasLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(101, 101, 101))
        );

        Root.add(Impressoras, "impress");

        userColab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userColab.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userColab.setEnabled(false);
        userColab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userColabActionPerformed(evt);
            }
        });

        cInicio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cInicio.setText("Inicio");
        cInicio.setEnabled(false);

        cRecebimento.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cRecebimento.setText("Recebimento");
        cRecebimento.setEnabled(false);

        cArmazenagem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cArmazenagem.setText("Armazenagem");
        cArmazenagem.setEnabled(false);

        cReposicao.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cReposicao.setText("Reposição");
        cReposicao.setEnabled(false);

        cConfiguracoes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cConfiguracoes.setText("Configurações");
        cConfiguracoes.setEnabled(false);
        cConfiguracoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cConfiguracoesActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Nome :");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Usuario :");

        nomeColab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nomeColab.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nomeColab.setEnabled(false);
        nomeColab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeColabActionPerformed(evt);
            }
        });

        senhaColab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        senhaColab.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        senhaColab.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Repetir Senha :");

        rSenhaColab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rSenhaColab.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rSenhaColab.setEnabled(false);

        cBalanceamento.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cBalanceamento.setText("Balanceamento");
        cBalanceamento.setEnabled(false);
        cBalanceamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBalanceamentoActionPerformed(evt);
            }
        });

        cAtualizacoes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cAtualizacoes.setText("Atualizações");
        cAtualizacoes.setEnabled(false);
        cAtualizacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cAtualizacoesActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Senha Nova :");

        btnVisualizar.setText("Visualizar");
        btnVisualizar.setEnabled(false);

        btnAlterar.setText("Alterar");
        btnAlterar.setEnabled(false);
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Incluir / Editar Usuários");

        javax.swing.GroupLayout UsuarioLayout = new javax.swing.GroupLayout(Usuario);
        Usuario.setLayout(UsuarioLayout);
        UsuarioLayout.setHorizontalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UsuarioLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 45, Short.MAX_VALUE))
                    .addGroup(UsuarioLayout.createSequentialGroup()
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnVisualizar))
                        .addGap(18, 18, 18)
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsuarioLayout.createSequentialGroup()
                                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nomeColab)
                            .addComponent(userColab)
                            .addComponent(senhaColab)
                            .addComponent(rSenhaColab))
                        .addGap(25, 25, 25)
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(UsuarioLayout.createSequentialGroup()
                                .addComponent(cReposicao)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(UsuarioLayout.createSequentialGroup()
                                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cInicio)
                                    .addComponent(cRecebimento)
                                    .addComponent(cArmazenagem))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cAtualizacoes)
                                    .addComponent(cBalanceamento)
                                    .addComponent(cConfiguracoes))
                                .addGap(57, 57, 57))))))
        );
        UsuarioLayout.setVerticalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addGap(54, 54, 54)
                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UsuarioLayout.createSequentialGroup()
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(UsuarioLayout.createSequentialGroup()
                                .addComponent(cInicio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cRecebimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cArmazenagem))
                            .addGroup(UsuarioLayout.createSequentialGroup()
                                .addComponent(cBalanceamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cAtualizacoes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cConfiguracoes)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cReposicao))
                    .addGroup(UsuarioLayout.createSequentialGroup()
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nomeColab)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(userColab)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(senhaColab)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(13, 13, 13)
                        .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rSenhaColab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(32, 32, 32)
                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVisualizar)
                    .addComponent(btnAlterar)
                    .addComponent(btnExcluir))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        Root.add(Usuario, "Usuario");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Configurações");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Pasta da Aplicação :");

        patchPasta.setEditable(false);

        jButton5.setText("Selecionar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ConfiguracoesLayout = new javax.swing.GroupLayout(Configuracoes);
        Configuracoes.setLayout(ConfiguracoesLayout);
        ConfiguracoesLayout.setHorizontalGroup(
            ConfiguracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfiguracoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConfiguracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ConfiguracoesLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(patchPasta, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        ConfiguracoesLayout.setVerticalGroup(
            ConfiguracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfiguracoesLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(ConfiguracoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(patchPasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addContainerGap(294, Short.MAX_VALUE))
        );

        Root.add(Configuracoes, "configuracoes");

        jMenu1.setText("Preferências");

        jMenuItem1.setText("Impressoras");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Usuário");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Configurações");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            global.alteraProp("prop.zebra", cmbImpZebra.getSelectedItem().toString());
            JOptionPane.showMessageDialog(null, "Impressora alterada com sucesso!");
        } catch (IOException ex) {
            Logger.getLogger(Configuracoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            global.alteraProp("prop.impa4", cmbImpA4.getSelectedItem().toString());
        } catch (IOException ex) {
            Logger.getLogger(Configuracoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "Usuario");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "impress");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void userColabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userColabActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userColabActionPerformed

    private void nomeColabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeColabActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeColabActionPerformed

    private void cConfiguracoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cConfiguracoesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cConfiguracoesActionPerformed

    private void cBalanceamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBalanceamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cBalanceamentoActionPerformed

    private void cAtualizacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cAtualizacoesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cAtualizacoesActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        if(senhaColab.getText().equals(rSenhaColab.getText())) {
            if(!btnVisualizar.isEnabled()) {
                alteraInfo(1);  
            }else{
                
            }
                
           }else{
               JOptionPane.showMessageDialog(null, "As Senhas não são identicas! \n Verifique!");
           }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "configuracoes");
        patchPasta.setText(global.arquivoRemoto);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        File dirF;
        String dirPatch;
        
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int i = file.showSaveDialog(null);

        if (i == 1) {

            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
        } else {

            dirF = file.getSelectedFile();
            dirPatch = dirF.getPath();
            dirPatch = dirPatch.replace("\\", "\\");
            dirPatch = dirPatch + "\\";
            patchPasta.setText(dirPatch);
            try {
                global.alteraProp("prop.pastaremota", dirPatch);
                JOptionPane.showMessageDialog(null, "Pasta alterada com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro na alteração de pasta.");
                Logger.getLogger(Configuracoes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Configuracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Configuracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Configuracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuracoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuracoes("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Configuracoes;
    private javax.swing.JPanel Impressoras;
    private javax.swing.JPanel Inicio;
    private javax.swing.JPanel Root;
    private javax.swing.JPanel Usuario;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnVisualizar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cArmazenagem;
    private javax.swing.JCheckBox cAtualizacoes;
    private javax.swing.JCheckBox cBalanceamento;
    private javax.swing.JCheckBox cConfiguracoes;
    private javax.swing.JCheckBox cInicio;
    private javax.swing.JCheckBox cRecebimento;
    private javax.swing.JCheckBox cReposicao;
    private javax.swing.JComboBox<String> cmbImpA4;
    private javax.swing.JComboBox<String> cmbImpZebra;
    private javax.swing.JLabel impA4Atual;
    private javax.swing.JLabel impZebraAtual;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField nomeColab;
    private javax.swing.JTextField patchPasta;
    private javax.swing.JTextField rSenhaColab;
    private javax.swing.JTextField senhaColab;
    private javax.swing.JTextField userColab;
    // End of variables declaration//GEN-END:variables
}
