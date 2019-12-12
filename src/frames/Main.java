package frames;

import classes.Atualizacao;
import classes.Banco;
import classes.Config;
import classes.DataHora;
import classes.DataPbl;
import classes.EditarAreaStz;
import classes.EtqPranchao;
import classes.GerenciaAcesso;
import classes.ImprimirZebra;
import classes.RelatorioEmissaoEtiquetas;
import classes.Tabela;
import classes.UpCsv;
import classes.UploadArquivosCsv;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import paineis.ConsultaEmissor;
import paineis.ConsultaGrandesRedes;
import paineis.EtqArmazenagem;
import paineis.ListaAntibioticos;


/*
 *
 * @author Lucas Vieira
 * 
 */



public class Main extends javax.swing.JFrame {
    Config global = new Config();
    ConsultaEmissor consultEmissor;
    EtqArmazenagem pnlEArmazenagem;
    EtqPranchao etqPranchao;
    EditarAreaStz editArea;
    ListaAntibioticos grafEst;
    ConsultaGrandesRedes conRedes;

    boolean bloquearAplic = false;
    boolean progressVisible = false;
    int progressValue = 0;

    ImprimirZebra impressao = new ImprimirZebra();

    Banco objCon = new Banco();

    public int statZerado = 0;
    public int quantEtq;
    public String idEmissor = "";
    public int numRows;
    public String data;
    public String horaMinuto;
    public String nomeEmissor;
    public String acessoEmissor;
    public String rua = "";
    public String nivel = "";
    public String excesso = "";
    public String estacaoPos;
    public String estacaoId;
    public String prodTipo;
    public String codIntervalo = "";
    public String codAStcruz = "";
    public String codEmissor;
    public String eanProduto;
    public String validadeProduto;
    public String quantidade;
    public String operador;
    public String descricaoProd;
    public String pfatProd;
    public String sapProd;
    public String endProd;
    public boolean confPsico = false;
    public boolean confContainer = false;
    public boolean confHb = false;
    public String atuaAtual = "";

    File uppln0055r;
    String uppatchPln0055r;
    String uprelPln0055r;

    File upcurvaAbc;
    String uppatchCurvaAbc;
    String uprelCurvaAbc;

    File upmb52;
    String uppatchMb52;
    String uprelMb52;

    File uppbl5050m;
    String uppatchPbl5050m;
    String uprelPbl5050m;

    /**
     * Creates new form Main
     */
    public Main(String idE, String noE, String acE) throws PrintException, SQLException, ParseException {
        initComponents();
        //procuraAtz();

        GerenciaAcesso gA = new GerenciaAcesso();
        String[] chaveAcesso = new String[20];
        gA.chaveAc(acE);
        chaveAcesso = gA.chave;

        setAcesso(chaveAcesso);

        inicioAplic();

        impressao.ImprimirZebra();

        nomeEmissor = noE;
        acessoEmissor = acE;
        idEmissor = idE;
        nomeUsuarioLbl.setText(noE);

        progressAtzPb.setVisible(false);
        origemAtzTxt.setText(global.arquivoRemoto);

    }

    public void setNEmissor(String nEmiss) {
        nomeEmissor = nEmiss;
    }

    public void setAcessoEmissor(String aceEmiss) {
        acessoEmissor = aceEmiss;
    }

    public void setEmissor(String fEmiss) {
        idEmissor = fEmiss;
    }

    public void procuraAtz() {

        new Thread() {

            @Override
            public void run() {
                while (true) {

                    verifBloq();
                    System.out.println("rodando thread");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();
    }

    public void verifBloq() {
        int vl = 0;
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from configuracoes WHERE numero = 1";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            vl = objCon.rs.getInt("bloqueio");

            if (vl == 1) {
                bloquearAplic = true;
                new Thread(contThread).start();
                emAcaoTxt.setEnabled(false);
            } else {
                bloquearAplic = false;
                progressAtzPb.setVisible(false);

                if (!emAcaoTxt.isEnabled()) {
                    emAcaoTxt.setEnabled(true);
                    emAcaoTxt.requestFocus();
                }

            }

        } catch (SQLException ex) {
            bloquearAplic = false;
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Runnable contThread = new Runnable() {

        public void run() {
            while (bloquearAplic) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {

                    progressAtzPb.setVisible(true);

                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * from configuracoes WHERE numero = 1";
                    objCon.rs = objCon.stmt.executeQuery(SQL);

                    objCon.rs.first();
                    progressValue = objCon.rs.getInt("parametro4");
                    int tempB = objCon.rs.getInt("bloqueio");

                    if (progressValue < 100 || tempB != 0) {

                        progressAtzPb.setValue(progressValue);
                    } else {
                        progressAtzPb.setVisible(false);
                        bloquearAplic = false;
                    }

                } catch (SQLException ex) {

                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    };

    public void bloqueiaEmissao() {
        int vl = 0;
        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from configuracoes WHERE numero = 1";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            vl = objCon.rs.getInt("bloqueio");

            if (vl == 1) {
                emAcaoTxt.setText("Atualizando");
                emAcaoTxt.setEditable(false);
            }

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String retornaDataAtz() {
        String dtUAtz = "";
        Date dataN;

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from configuracoes WHERE numero = 1";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            dataN = objCon.rs.getDate("data");
            SimpleDateFormat formaData = new SimpleDateFormat("dd/MM/yyyy");
            dtUAtz = formaData.format(dataN);

            txtAtuRelDatTxt.setText(dtUAtz);

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dtUAtz;
    }

    private void inicioAplic() throws SQLException, ParseException {

        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "home");

        atuaAtual = retornaDataAtz();
        txtAtuRelDatTxt.setText(atuaAtual);

        preencherTabela();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        Principal = new javax.swing.JPanel();
        Backgroung = new javax.swing.JDesktopPane();
        Menu2 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        progressAtzPb = new javax.swing.JProgressBar();
        nomeUsuarioLbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Root = new javax.swing.JPanel();
        Home = new javax.swing.JPanel();
        BackHome = new javax.swing.JDesktopPane();
        Emetq = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EmIndex = new javax.swing.JPanel();
        EmFixo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pfatLbl = new javax.swing.JTextField();
        sapLbl = new javax.swing.JTextField();
        eanLbl = new javax.swing.JTextField();
        enderecoLbl = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        descProdTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLogEmissao = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        lblAcaoEmetq = new javax.swing.JLabel();
        emAcaoTxt = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        excessoLbl = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        validadeVezLbl = new javax.swing.JTextField();
        AtzArquivos = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pln0055rTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        curvaAbcTxt = new javax.swing.JTextField();
        curvaAbcBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        mb52Txt = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        pbl5050mTxt = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        dataAtualizacaoTxt = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        pastaAplicTx = new javax.swing.JTextField();
        RelEmissao = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        EmIndex1 = new javax.swing.JPanel();
        EmFixo1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRelEmissao = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        txtDtIni = new javax.swing.JTextField();
        txtDtFim = new javax.swing.JTextField();
        chkSomenteZero = new javax.swing.JCheckBox();
        chkDescZero = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnProcess = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtProdPfat = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        Etiquetas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        EmIndex2 = new javax.swing.JPanel();
        EmFixo2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pranAreaStzTxt = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        pranEstacaoTxt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        pranNivelTxt = new javax.swing.JTextField();
        pranNivelTodosRb = new javax.swing.JRadioButton();
        pranPNivelRb = new javax.swing.JRadioButton();
        pranBtnImpBtn = new javax.swing.JButton();
        AreaStz = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        EAIncluirRd = new javax.swing.JRadioButton();
        EAAlterarRd = new javax.swing.JRadioButton();
        EAExcluirRd = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        EAAreaStzTxt = new javax.swing.JTextField();
        EARuaTxt = new javax.swing.JTextField();
        EANivelTxt = new javax.swing.JTextField();
        EAPosicaoTxt = new javax.swing.JTextField();
        EAPosicaoIniTxt = new javax.swing.JTextField();
        EAPosicaoFimTxt = new javax.swing.JTextField();
        EASequenciaTxt = new javax.swing.JTextField();
        EAIncluirBtn = new javax.swing.JButton();
        EAAlterarBtn = new javax.swing.JButton();
        EAExcluirBtn = new javax.swing.JButton();
        EA1lbl = new javax.swing.JLabel();
        EA2lbl = new javax.swing.JLabel();
        EA3lbl = new javax.swing.JLabel();
        EA4lbl = new javax.swing.JLabel();
        EA5lbl = new javax.swing.JLabel();
        EA6lbl = new javax.swing.JLabel();
        EA7lbl = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        EAEstacaoTxt = new javax.swing.JTextField();
        EA8lbl = new javax.swing.JLabel();
        Atualizacao = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        btnReatBtn = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txtAtuRelDatTxt = new javax.swing.JTextField();
        btnRemUserBtn = new javax.swing.JButton();
        lblAtuPln0055rLbl = new javax.swing.JLabel();
        lblAtuCurvaLbl = new javax.swing.JLabel();
        lblAtuMb52Lbl = new javax.swing.JLabel();
        lblAtuPbl5050mLbl = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        origemAtzTxt = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuPrincInicio = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        menuPrincRecebimento = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuPrincArmazenagem = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        menuPrincReposicao = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        menuPrincBalanceamento = new javax.swing.JMenu();
        menuPrincExpedicao = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        menuPrincAtualiz = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        menuPrincConf = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        menuPrincAjuda = new javax.swing.JMenu();

        jMenu3.setText("jMenu3");

        jMenu5.setText("jMenu5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SGE");
        setIconImages(null);

        progressAtzPb.setValue(100);
        progressAtzPb.setStringPainted(true);

        nomeUsuarioLbl.setForeground(new java.awt.Color(255, 255, 255));
        nomeUsuarioLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nomeUsuarioLbl.setText("Lucas Vieira da Silva");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sair");
        jLabel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel2MouseMoved(evt);
            }
        });
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });
        jLabel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel2KeyPressed(evt);
            }
        });

        jDesktopPane1.setLayer(progressAtzPb, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(nomeUsuarioLbl, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressAtzPb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nomeUsuarioLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(progressAtzPb, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addComponent(nomeUsuarioLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Root.setPreferredSize(new java.awt.Dimension(1002, 784));
        Root.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout BackHomeLayout = new javax.swing.GroupLayout(BackHome);
        BackHome.setLayout(BackHomeLayout);
        BackHomeLayout.setHorizontalGroup(
            BackHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1002, Short.MAX_VALUE)
        );
        BackHomeLayout.setVerticalGroup(
            BackHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 784, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackHome)
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackHome)
        );

        Root.add(Home, "home");

        jScrollPane1.setBackground(new java.awt.Color(153, 255, 153));
        jScrollPane1.setBorder(null);

        EmIndex.setBorder(new javax.swing.border.MatteBorder(null));

        EmFixo.setBorder(new javax.swing.border.MatteBorder(null));
        EmFixo.setMinimumSize(new java.awt.Dimension(1000, 750));
        EmFixo.setPreferredSize(new java.awt.Dimension(1000, 750));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 76, 156));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Emissão de etiquetas de recebimento");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 76, 156));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Código PFAT");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 76, 156));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Código SAP");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(50, 76, 156));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Código de barras");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(50, 76, 156));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Endereço");

        pfatLbl.setEditable(false);
        pfatLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pfatLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pfatLbl.setFocusable(false);

        sapLbl.setEditable(false);
        sapLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sapLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sapLbl.setFocusable(false);

        eanLbl.setEditable(false);
        eanLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        eanLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        eanLbl.setFocusable(false);

        enderecoLbl.setEditable(false);
        enderecoLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        enderecoLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        enderecoLbl.setFocusable(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(50, 76, 156));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Descrição do produto");

        descProdTxt.setEditable(false);
        descProdTxt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        descProdTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        descProdTxt.setFocusable(false);

        tblLogEmissao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Título 1", "Título 2", "Título 3", "Título 4", "Título 5", "Título 6", "Título 7", "Título 8", "Título 9", "Título 10"
            }
        ));
        tblLogEmissao.setAutoscrolls(false);
        jScrollPane2.setViewportView(tblLogEmissao);

        lblAcaoEmetq.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblAcaoEmetq.setForeground(new java.awt.Color(255, 255, 255));
        lblAcaoEmetq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAcaoEmetq.setText("Passe o crachá na leitora");

        emAcaoTxt.setBackground(java.awt.SystemColor.info);
        emAcaoTxt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        emAcaoTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        emAcaoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emAcaoTxtActionPerformed(evt);
            }
        });
        emAcaoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emAcaoTxtKeyPressed(evt);
            }
        });

        jButton2.setText("Retornar");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jDesktopPane3.setLayer(lblAcaoEmetq, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(emAcaoTxt, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane3Layout = new javax.swing.GroupLayout(jDesktopPane3);
        jDesktopPane3.setLayout(jDesktopPane3Layout);
        jDesktopPane3Layout.setHorizontalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(emAcaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(lblAcaoEmetq, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDesktopPane3Layout.setVerticalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                .addComponent(lblAcaoEmetq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(emAcaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jDesktopPane3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jDesktopPane3))
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(50, 76, 156));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Quantidade Excessos");

        excessoLbl.setEditable(false);
        excessoLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        excessoLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        excessoLbl.setFocusable(false);

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(50, 76, 156));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Validade da vez");

        validadeVezLbl.setEditable(false);
        validadeVezLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        validadeVezLbl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        validadeVezLbl.setFocusable(false);

        javax.swing.GroupLayout EmFixoLayout = new javax.swing.GroupLayout(EmFixo);
        EmFixo.setLayout(EmFixoLayout);
        EmFixoLayout.setHorizontalGroup(
            EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixoLayout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(EmFixoLayout.createSequentialGroup()
                        .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pfatLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sapLbl)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                        .addGap(77, 77, 77)
                        .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eanLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(91, 91, 91)
                        .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(enderecoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(116, 116, 116))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixoLayout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmFixoLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(41, 41, 41)
                        .addComponent(excessoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel23)
                        .addGap(32, 32, 32)
                        .addComponent(validadeVezLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixoLayout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(76, 76, 76))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixoLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(267, 267, 267))
                        .addGroup(EmFixoLayout.createSequentialGroup()
                            .addComponent(descProdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap()))))
        );
        EmFixoLayout.setVerticalGroup(
            EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmFixoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pfatLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sapLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eanLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enderecoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descProdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(EmFixoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(excessoLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(validadeVezLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout EmIndexLayout = new javax.swing.GroupLayout(EmIndex);
        EmIndex.setLayout(EmIndexLayout);
        EmIndexLayout.setHorizontalGroup(
            EmIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EmFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        EmIndexLayout.setVerticalGroup(
            EmIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmIndexLayout.createSequentialGroup()
                .addComponent(EmFixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(EmIndex);

        javax.swing.GroupLayout EmetqLayout = new javax.swing.GroupLayout(Emetq);
        Emetq.setLayout(EmetqLayout);
        EmetqLayout.setHorizontalGroup(
            EmetqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        EmetqLayout.setVerticalGroup(
            EmetqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        Root.add(Emetq, "emetq");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(50, 76, 156));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Pln0055r");

        pln0055rTxt.setEditable(false);
        pln0055rTxt.setFocusable(false);

        jButton1.setText("Procurar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(50, 76, 156));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Curva ABC");

        curvaAbcTxt.setEditable(false);
        curvaAbcTxt.setFocusable(false);

        curvaAbcBtn.setText("Procurar");
        curvaAbcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curvaAbcBtnActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(50, 76, 156));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Mb52");

        mb52Txt.setEditable(false);
        mb52Txt.setFocusable(false);

        jButton3.setText("Procurar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(50, 76, 156));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Pbl5050m");

        pbl5050mTxt.setEditable(false);
        pbl5050mTxt.setFocusable(false);

        jButton4.setText("Procurar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(50, 76, 156));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Atualização de Relatorios");

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton7.setText("Realizar Atualização");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(50, 76, 156));
        jLabel13.setText("Atualização");

        dataAtualizacaoTxt.setEditable(false);
        dataAtualizacaoTxt.setBackground(new java.awt.Color(0, 0, 0));
        dataAtualizacaoTxt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        dataAtualizacaoTxt.setForeground(new java.awt.Color(255, 255, 255));
        dataAtualizacaoTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dataAtualizacaoTxt.setFocusable(false);

        jButton5.setText("Remover Usuario Atualizando");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(50, 76, 156));
        jLabel39.setText("Origem");

        pastaAplicTx.setEditable(false);
        pastaAplicTx.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(37, 37, 37)
                                    .addComponent(mb52Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton3))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(37, 37, 37)
                                    .addComponent(pln0055rTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(37, 37, 37)
                                    .addComponent(curvaAbcTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(curvaAbcBtn))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel39)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGap(37, 37, 37)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(pbl5050mTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(dataAtualizacaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addComponent(jButton4))
                                        .addComponent(pastaAplicTx)))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(jButton7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(jButton5)))
                .addContainerGap(254, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel15)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pln0055rTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1))
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(curvaAbcTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(curvaAbcBtn))
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mb52Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3))
                    .addComponent(jLabel11))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pbl5050mTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4))
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataAtualizacaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(pastaAplicTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AtzArquivosLayout = new javax.swing.GroupLayout(AtzArquivos);
        AtzArquivos.setLayout(AtzArquivosLayout);
        AtzArquivosLayout.setHorizontalGroup(
            AtzArquivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AtzArquivosLayout.setVerticalGroup(
            AtzArquivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Root.add(AtzArquivos, "atzRel");

        RelEmissao.setMaximumSize(new java.awt.Dimension(1002, 784));

        jScrollPane3.setBackground(new java.awt.Color(153, 255, 153));
        jScrollPane3.setBorder(null);

        EmIndex1.setBorder(new javax.swing.border.MatteBorder(null));
        EmIndex1.setPreferredSize(new java.awt.Dimension(1002, 784));

        EmFixo1.setBorder(new javax.swing.border.MatteBorder(null));
        EmFixo1.setMaximumSize(new java.awt.Dimension(1002, 784));
        EmFixo1.setMinimumSize(new java.awt.Dimension(1002, 784));
        EmFixo1.setPreferredSize(new java.awt.Dimension(1002, 784));

        tblRelEmissao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Estação", "Descrição", "Validade", "Data Emissão", "Hora"
            }
        ));
        tblRelEmissao.setAutoscrolls(false);
        jScrollPane4.setViewportView(tblRelEmissao);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(50, 76, 156));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Relatório de Emissão de Etiquetas");

        txtDtIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtDtFim.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        chkSomenteZero.setText("Somente reposição urgente");

        chkDescZero.setText("Não listar reposição urgente");
        chkDescZero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDescZeroActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(50, 76, 156));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Data Final");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(50, 76, 156));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Pesquisa por produto(PFAT)");

        btnProcess.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnProcess.setText("Processar");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(50, 76, 156));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Data Inicial");

        txtProdPfat.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton6.setText(".CSV");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EmFixo1Layout = new javax.swing.GroupLayout(EmFixo1);
        EmFixo1.setLayout(EmFixo1Layout);
        EmFixo1Layout.setHorizontalGroup(
            EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmFixo1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDtIni, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkSomenteZero, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkDescZero, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(txtProdPfat, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)))
                .addGap(18, 18, 18)
                .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27))
            .addGroup(EmFixo1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 958, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
            .addGroup(EmFixo1Layout.createSequentialGroup()
                .addGap(298, 298, 298)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EmFixo1Layout.setVerticalGroup(
            EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmFixo1Layout.createSequentialGroup()
                .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel14)
                        .addGap(46, 46, 46)
                        .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDtIni, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkSomenteZero, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkDescZero, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProdPfat)))
                    .addGroup(EmFixo1Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(EmFixo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout EmIndex1Layout = new javax.swing.GroupLayout(EmIndex1);
        EmIndex1.setLayout(EmIndex1Layout);
        EmIndex1Layout.setHorizontalGroup(
            EmIndex1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EmFixo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        EmIndex1Layout.setVerticalGroup(
            EmIndex1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EmFixo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jScrollPane3.setViewportView(EmIndex1);

        javax.swing.GroupLayout RelEmissaoLayout = new javax.swing.GroupLayout(RelEmissao);
        RelEmissao.setLayout(RelEmissaoLayout);
        RelEmissaoLayout.setHorizontalGroup(
            RelEmissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        RelEmissaoLayout.setVerticalGroup(
            RelEmissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        Root.add(RelEmissao, "pnlRel");

        Etiquetas.setMaximumSize(new java.awt.Dimension(1002, 784));

        jScrollPane5.setBackground(new java.awt.Color(153, 255, 153));
        jScrollPane5.setBorder(null);

        EmIndex2.setBorder(new javax.swing.border.MatteBorder(null));
        EmIndex2.setPreferredSize(new java.awt.Dimension(1002, 784));

        EmFixo2.setBorder(new javax.swing.border.MatteBorder(null));
        EmFixo2.setMaximumSize(new java.awt.Dimension(1002, 784));
        EmFixo2.setMinimumSize(new java.awt.Dimension(1002, 784));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(50, 76, 156));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Emissão de Etiqueta de Pranchão");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Estação _________________");

        pranAreaStzTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pranAreaStzTxtActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setText("Area SantaCruz ___________");

        pranEstacaoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pranEstacaoTxtActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel22.setText("Nivel ___________________");

        pranNivelTodosRb.setText("Imprimir todos os niveis");
        pranNivelTodosRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pranNivelTodosRbActionPerformed(evt);
            }
        });

        pranPNivelRb.setSelected(true);
        pranPNivelRb.setText("Imprimir por nivel");
        pranPNivelRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pranPNivelRbActionPerformed(evt);
            }
        });

        pranBtnImpBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pranBtnImpBtn.setText("Imprimir");
        pranBtnImpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pranBtnImpBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EmFixo2Layout = new javax.swing.GroupLayout(EmFixo2);
        EmFixo2.setLayout(EmFixo2Layout);
        EmFixo2Layout.setHorizontalGroup(
            EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmFixo2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(EmFixo2Layout.createSequentialGroup()
                            .addComponent(pranNivelTodosRb)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pranPNivelRb))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixo2Layout.createSequentialGroup()
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(pranAreaStzTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmFixo2Layout.createSequentialGroup()
                            .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pranNivelTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pranEstacaoTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(EmFixo2Layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(pranBtnImpBtn)))
                .addContainerGap(527, Short.MAX_VALUE))
        );
        EmFixo2Layout.setVerticalGroup(
            EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmFixo2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel20)
                .addGap(59, 59, 59)
                .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pranAreaStzTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pranEstacaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(pranNivelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(EmFixo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pranNivelTodosRb)
                    .addComponent(pranPNivelRb))
                .addGap(18, 18, 18)
                .addComponent(pranBtnImpBtn)
                .addContainerGap(458, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EmIndex2Layout = new javax.swing.GroupLayout(EmIndex2);
        EmIndex2.setLayout(EmIndex2Layout);
        EmIndex2Layout.setHorizontalGroup(
            EmIndex2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EmFixo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        EmIndex2Layout.setVerticalGroup(
            EmIndex2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(EmFixo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jScrollPane5.setViewportView(EmIndex2);

        javax.swing.GroupLayout EtiquetasLayout = new javax.swing.GroupLayout(Etiquetas);
        Etiquetas.setLayout(EtiquetasLayout);
        EtiquetasLayout.setHorizontalGroup(
            EtiquetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        EtiquetasLayout.setVerticalGroup(
            EtiquetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );

        Root.add(Etiquetas, "pnletq");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(50, 76, 156));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Inclusão / Alteração de AreaStz");

        EAIncluirRd.setText("Incluir");
        EAIncluirRd.setFocusable(false);
        EAIncluirRd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EAIncluirRdMouseClicked(evt);
            }
        });
        EAIncluirRd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAIncluirRdActionPerformed(evt);
            }
        });

        EAAlterarRd.setText("Alterar");
        EAAlterarRd.setFocusable(false);
        EAAlterarRd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EAAlterarRdMouseClicked(evt);
            }
        });
        EAAlterarRd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAAlterarRdActionPerformed(evt);
            }
        });

        EAExcluirRd.setText("Excluir");
        EAExcluirRd.setFocusable(false);
        EAExcluirRd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EAExcluirRdMouseClicked(evt);
            }
        });
        EAExcluirRd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAExcluirRdActionPerformed(evt);
            }
        });

        jLabel24.setText("Area Stcruz");

        jLabel25.setText("Rua :");

        jLabel26.setText("Nivel :");

        jLabel27.setText("Posição :");

        jLabel29.setText("Posição Inicial :");

        jLabel30.setText("Posição Final :");

        jLabel31.setText("Sequência : ");

        EAAreaStzTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EAAreaStzTxt.setEnabled(false);
        EAAreaStzTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAAreaStzTxtActionPerformed(evt);
            }
        });
        EAAreaStzTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EAAreaStzTxtKeyPressed(evt);
            }
        });

        EARuaTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EARuaTxt.setEnabled(false);

        EANivelTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EANivelTxt.setEnabled(false);

        EAPosicaoTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EAPosicaoTxt.setEnabled(false);

        EAPosicaoIniTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EAPosicaoIniTxt.setEnabled(false);

        EAPosicaoFimTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EAPosicaoFimTxt.setEnabled(false);

        EASequenciaTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EASequenciaTxt.setEnabled(false);

        EAIncluirBtn.setText("Incluir");
        EAIncluirBtn.setEnabled(false);
        EAIncluirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAIncluirBtnActionPerformed(evt);
            }
        });

        EAAlterarBtn.setText("Alterar");
        EAAlterarBtn.setEnabled(false);
        EAAlterarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAAlterarBtnActionPerformed(evt);
            }
        });

        EAExcluirBtn.setText("Excluir");
        EAExcluirBtn.setEnabled(false);
        EAExcluirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EAExcluirBtnActionPerformed(evt);
            }
        });

        EA1lbl.setText("Exemplo : 10150");

        EA2lbl.setText("Exemplo : 10");

        EA3lbl.setText("Exemplo : 1");

        EA4lbl.setText("Exemplo : 50");

        EA5lbl.setText("Exemplo : A01");

        EA6lbl.setText("Exemplo : A05");

        EA7lbl.setText("Exemplo : A01-A02-A03-A04-A05");

        jLabel38.setText("Estação :");

        EAEstacaoTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EAEstacaoTxt.setEnabled(false);

        EA8lbl.setText("Exemplo : 30");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(EAIncluirBtn)
                        .addGap(18, 18, 18)
                        .addComponent(EAAlterarBtn)
                        .addGap(18, 18, 18)
                        .addComponent(EAExcluirBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(EAIncluirRd)
                                .addGap(102, 102, 102)
                                .addComponent(EAAlterarRd)
                                .addGap(113, 113, 113)
                                .addComponent(EAExcluirRd))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(EAEstacaoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                                    .addComponent(EASequenciaTxt)
                                    .addComponent(EAPosicaoFimTxt)
                                    .addComponent(EAPosicaoIniTxt)
                                    .addComponent(EANivelTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(EARuaTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(EAAreaStzTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(EAPosicaoTxt))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(EA1lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA2lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA3lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA4lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA5lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA6lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(EA7lbl, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(EA8lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(249, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EAIncluirRd)
                    .addComponent(EAAlterarRd)
                    .addComponent(EAExcluirRd))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(EAAreaStzTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA1lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(EARuaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA2lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(EANivelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA3lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(EAPosicaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA4lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(EAPosicaoIniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA5lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(EAPosicaoFimTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA6lbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(EASequenciaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA7lbl))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(EAEstacaoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EA8lbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EAIncluirBtn)
                    .addComponent(EAAlterarBtn)
                    .addComponent(EAExcluirBtn))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AreaStzLayout = new javax.swing.GroupLayout(AreaStz);
        AreaStz.setLayout(AreaStzLayout);
        AreaStzLayout.setHorizontalGroup(
            AreaStzLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AreaStzLayout.setVerticalGroup(
            AreaStzLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Root.add(AreaStz, "editAStz");

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(50, 76, 156));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Arquivo pln0055r.csv ____________________");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(50, 76, 156));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Arquivo Curva ABC do Produto.csv _________");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(50, 76, 156));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Arquivo mb52.csv ______________________");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(50, 76, 156));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Arquivo pbl5050m.csv ___________________");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(50, 76, 156));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Atualização de Relatorios");

        btnReatBtn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnReatBtn.setText("Realizar Atualização");
        btnReatBtn.setFocusable(false);
        btnReatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReatBtnActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(50, 76, 156));
        jLabel37.setText("Atualização");

        txtAtuRelDatTxt.setEditable(false);
        txtAtuRelDatTxt.setBackground(new java.awt.Color(0, 0, 0));
        txtAtuRelDatTxt.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtAtuRelDatTxt.setForeground(new java.awt.Color(255, 255, 255));
        txtAtuRelDatTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAtuRelDatTxt.setFocusable(false);
        txtAtuRelDatTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAtuRelDatTxtActionPerformed(evt);
            }
        });

        btnRemUserBtn.setText("Remover Usuario Atualizando");
        btnRemUserBtn.setFocusable(false);
        btnRemUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemUserBtnActionPerformed(evt);
            }
        });

        lblAtuPln0055rLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblAtuPln0055rLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblAtuCurvaLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblAtuCurvaLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblAtuMb52Lbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblAtuMb52Lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblAtuPbl5050mLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblAtuPbl5050mLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(50, 76, 156));
        jLabel40.setText("Origem");

        origemAtzTxt.setEditable(false);
        origemAtzTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        origemAtzTxt.setFocusable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(btnRemUserBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnReatBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel37)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtAtuRelDatTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(origemAtzTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAtuPln0055rLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAtuCurvaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAtuMb52Lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAtuPbl5050mLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(lblAtuPln0055rLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(lblAtuCurvaLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addComponent(lblAtuMb52Lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lblAtuPbl5050mLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(txtAtuRelDatTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(origemAtzTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRemUserBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReatBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AtualizacaoLayout = new javax.swing.GroupLayout(Atualizacao);
        Atualizacao.setLayout(AtualizacaoLayout);
        AtualizacaoLayout.setHorizontalGroup(
            AtualizacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AtualizacaoLayout.setVerticalGroup(
            AtualizacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Root.add(Atualizacao, "atualizacao");

        Backgroung.setLayer(Menu2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Backgroung.setLayer(Root, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout BackgroungLayout = new javax.swing.GroupLayout(Backgroung);
        Backgroung.setLayout(BackgroungLayout);
        BackgroungLayout.setHorizontalGroup(
            BackgroungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        BackgroungLayout.setVerticalGroup(
            BackgroungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroungLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PrincipalLayout = new javax.swing.GroupLayout(Principal);
        Principal.setLayout(PrincipalLayout);
        PrincipalLayout.setHorizontalGroup(
            PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Backgroung)
        );
        PrincipalLayout.setVerticalGroup(
            PrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Backgroung)
        );

        menuPrincInicio.setText("Inicio");
        menuPrincInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrincInicioActionPerformed(evt);
            }
        });

        jMenuItem4.setText("Inicio");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        menuPrincInicio.add(jMenuItem4);

        jMenuBar1.add(menuPrincInicio);

        menuPrincRecebimento.setText("Recebimento");
        menuPrincRecebimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrincRecebimentoActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Etiquetas");
        jMenuItem1.setEnabled(false);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuPrincRecebimento.add(jMenuItem1);

        jMenuItem2.setText("Consulta Emissor");
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menuPrincRecebimento.add(jMenuItem2);

        jMenuItem3.setText("Relatorios");
        jMenuItem3.setEnabled(false);
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuPrincRecebimento.add(jMenuItem3);

        jMenuBar1.add(menuPrincRecebimento);

        menuPrincArmazenagem.setText("Armazenagem");

        jMenuItem7.setText("Etiqueta de Pranchão");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        menuPrincArmazenagem.add(jMenuItem7);

        jMenuItem8.setText("Editar AreaStCruz");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        menuPrincArmazenagem.add(jMenuItem8);

        jMenuItem9.setText("Etiqueta de Excesso");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        menuPrincArmazenagem.add(jMenuItem9);

        jMenuBar1.add(menuPrincArmazenagem);

        menuPrincReposicao.setText("Reposição");

        jMenuItem10.setText("Relatório de antibióticos");
        jMenuItem10.setEnabled(false);
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        menuPrincReposicao.add(jMenuItem10);

        jMenuBar1.add(menuPrincReposicao);

        menuPrincBalanceamento.setText("Balanceamento");
        jMenuBar1.add(menuPrincBalanceamento);

        menuPrincExpedicao.setText("Expedição");

        jMenuItem11.setText("Grandes Redes");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        menuPrincExpedicao.add(jMenuItem11);

        jMenuBar1.add(menuPrincExpedicao);

        menuPrincAtualiz.setText("Atualizações");

        jMenuItem5.setText("Atualizar Relatórios");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        menuPrincAtualiz.add(jMenuItem5);

        jMenuBar1.add(menuPrincAtualiz);

        menuPrincConf.setText("Configurações");
        menuPrincConf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrincConfActionPerformed(evt);
            }
        });

        jMenuItem6.setText("Configurações");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        menuPrincConf.add(jMenuItem6);

        jMenuBar1.add(menuPrincConf);

        menuPrincAjuda.setText("Ajuda");
        jMenuBar1.add(menuPrincAjuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuPrincRecebimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrincRecebimentoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_menuPrincRecebimentoActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        consultEmissor = new ConsultaEmissor();
        Root.add(consultEmissor, "conemissor");
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "conemissor");
        consultEmissor.zeraCampos();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "emetq");
        emAcaoTxt.requestFocus();
        retornaTela();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuPrincInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrincInicioActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_menuPrincInicioActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "home");
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jLabel2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel2KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel2KeyPressed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel2MouseReleased

    private void jLabel2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel2MouseMoved

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        // TODO add your handling code here:
        jLabel2.setForeground(Color.red);
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        // TODO add your handling code here:
        jLabel2.setForeground(Color.WHITE);
    }//GEN-LAST:event_jLabel2MouseExited

    private void emAcaoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emAcaoTxtKeyPressed

        String acaoEmetq;
        String acaoLblEmetq;
        String contCamp = emAcaoTxt.getText();
        if (contCamp.equals("Atualizando")) {
            emAcaoTxt.setText("");
        }

        emAcaoTxt.setEditable(true);
        emAcaoTxt.requestFocus();

        int quantText = emAcaoTxt.getText().length();
        bloqueiaEmissao();

        if (!emAcaoTxt.getText().equals("Atualizando") && !emAcaoTxt.getText().equals("")) {

            /*if(quantText >= 13) { try {
            realizaEan();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
} */
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    realizaEan();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (contCamp.contains("/erro")) {
            retornaTela();
        }


    }//GEN-LAST:event_emAcaoTxtKeyPressed

    public void realizaEan() throws SQLException, ParseException {
        switch (lblAcaoEmetq.getText()) {
            case "Passe o crachá na leitora":
                desbloqEm();
                break;
            case "Código EAN do produto":
                coletaEan();
                break;
            case "Escreva a validade do produto":
                coletaVal();
                break;
            case "Escreva a quantidade de etiquetas":

                try {
                    coletaQuant();
                } catch (PrintException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
        }
    }

    private void desbloqEm() {
        boolean desbloqOk = false;
        int cCont = 0;
        String codCracha = emAcaoTxt.getText();

        if (!codCracha.equals("")) {
            try {
                objCon.openConnectionMysql();
                objCon.stmt = objCon.con.createStatement();

                String SQL = "SELECT * from login WHERE cracha = '" + codCracha + "'";
                objCon.rs = objCon.stmt.executeQuery(SQL);

                while (objCon.rs.next()) {
                    cCont++;
                }

                if (cCont > 0) {
                    desbloqOk = true;
                }

            } catch (SQLException ex) {

            }

            try {
                objCon.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (desbloqOk) {
            emAcaoTxt.setText("");
            lblAcaoEmetq.setText("Código EAN do produto");

            idEmissor = codCracha;
        } else {
            emAcaoTxt.setText("");
            JOptionPane.showMessageDialog(null, "Usuário não cadastrado!");
        }
    }

    private void coletaEan() {
        int numResults = 0;
        numRows = 0;
        int quant;
        quant = emAcaoTxt.getText().length();

        eanProduto = emAcaoTxt.getText();

            try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from pln0055r WHERE ean = " + eanProduto + " OR codigo = " + emAcaoTxt.getText().toString();
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();
            descricaoProd = objCon.rs.getString("descricao");
            pfatProd = objCon.rs.getString("codigo");
            estacaoId = objCon.rs.getString("est");
            estacaoId = estacaoId.replace(" ", "");
            estacaoPos = objCon.rs.getString("pra");

            descProdTxt.setText(descricaoProd);
            eanLbl.setText(eanProduto);
            pfatLbl.setText(pfatProd);

            numResults = Integer.parseInt(pfatProd);

            System.out.println(numResults);

            consultaQEx();

        } catch (SQLException ex) {

        }


        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from curva_abc WHERE pfat = '" + pfatProd + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            endProd = objCon.rs.getString("endereco");
            sapProd = objCon.rs.getString("sap");
            enderecoLbl.setText(endProd);
            sapLbl.setText(sapProd);

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (numResults > 0) {
            lblAcaoEmetq.setText("Escreva a validade do produto");
            emAcaoTxt.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Ean Inválido!");
            retornaTela();
            emAcaoTxt.setText("");
        }

    }

    private void coletaVal() {
        String quanti;
        int quant;

        quant = emAcaoTxt.getText().length();
        if (quant == 7 && emAcaoTxt.getText().substring(2, 3).equals("/")) {

            if (validaDat(emAcaoTxt.getText()) == true) {

                validadeProduto = emAcaoTxt.getText();
                emAcaoTxt.setText("");
                lblAcaoEmetq.setText("Escreva a quantidade de etiquetas");
            } else {
                JOptionPane.showMessageDialog(null, "Validade Invalida");
                emAcaoTxt.setText("");
            }
        } else {
            if (quant == 6) {
                String val1 = emAcaoTxt.getText().substring(0, 2);
                String val2 = emAcaoTxt.getText().substring(2, 6);
                validadeProduto = val1 + "/" + val2;

                if (validaDat(validadeProduto) == true) {
                    emAcaoTxt.setText("");
                    lblAcaoEmetq.setText("Escreva a quantidade de etiquetas");
                } else {
                    JOptionPane.showMessageDialog(null, "Validade Invalida");
                    emAcaoTxt.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Validade Invalida");
                emAcaoTxt.setText("");
            }
        }

    }

    private void coletaQuant() throws PrintException, SQLException, ParseException {
        String quanti;
        int quant;

        try {

            quanti = emAcaoTxt.getText();

            quant = Integer.parseInt(quanti);

            quantidade = quanti;

            emAcaoTxt.setText("");

            quantEtq = quant;
            System.out.println(quantidade);

            captaInfo();

        } catch (NumberFormatException e) {
            System.out.println("catch quant");
            JOptionPane.showMessageDialog(null, "Quantidade Invalida");
        }
    }

    public void captaInfo() throws PrintException, SQLException, ParseException {

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from area_stcruz WHERE estacao = '" + estacaoId + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            //objCon.rs.first();
            while (objCon.rs.next()) {

                if (objCon.rs.getString("sequencia").contains(estacaoPos)) {
                    rua = objCon.rs.getString("rua");
                    nivel = objCon.rs.getString("nivel");
                    excesso = objCon.rs.getString("excesso");
                    break;
                }
            }
            //System.out.println(rua + "   ruaaaaaaaaaaaaaaaaaaaaa");
            if (rua.length() == 1) {
                rua = "0" + rua;
            }

            if (excesso.length() == 1) {
                excesso = "00" + excesso;
            } else {
                if (excesso.length() == 2) {
                    excesso = "0" + excesso;
                }
            }

            codAStcruz = "R." + rua + "." + nivel + "." + excesso;

            codIntervalo = objCon.rs.getString("sequencia");

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        prodTipo = "   NORMAL    ";

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from pln1030r WHERE codigo = '" + pfatProd + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();
            int contAnt = 0;
            contAnt = objCon.rs.getString("descricao").length();

            if (contAnt > 0) {
                prodTipo = "ANTIBIOTICO";
            }

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from mb52 WHERE material = '" + sapProd + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();
            int qtdEst = 0;
            String estQtd;

            estQtd = objCon.rs.getString("unidades");
            estQtd = estQtd.replace(".", "");
            try{
                qtdEst = Integer.parseInt(estQtd);
            }catch(Exception e){
                qtdEst = 0;
                e.printStackTrace();
            }
            

            if (qtdEst < 10) {
                statZerado = 1;
            }

            if (qtdEst < 10) {
                impressaoRUrgente();
            }

        } catch (SQLException ex) {

        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataHora dtHr = new DataHora();
        data = dtHr.Data();

        horaMinuto = dtHr.Hora();
        horaMinuto = horaMinuto.substring(0, 5);

        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from pbl5050m WHERE codigo = '" + pfatProd + "' ORDER BY data_mysql ASC";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            String valProd = "";

            valProd = objCon.rs.getString("validade");

            String anoProd = validadeProduto.substring(3, 7);
            String mesProd = validadeProduto.substring(0, 2);

            int prodAno = Integer.parseInt(anoProd);
            int prodMes = Integer.parseInt(mesProd);

            String anoEst = valProd.substring(6, 10);
            String mesEst = valProd.substring(3, 5);

            int EstAno = Integer.parseInt(anoEst);
            int EstMes = Integer.parseInt(mesEst);

            if (EstAno < prodAno) {
                impressaoExcesso();
            } else {
                if (EstMes < prodMes) {
                    impressaoExcesso();
                }
            }

        } catch (SQLException ex) {
            System.out.println("erro");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String validaContainer = "";
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from container WHERE codigo = '" + pfatProd + "'";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            if (!objCon.rs.getString("codigo").equals("")) {
                confContainer = true;
            }

        } catch (SQLException ex) {
            System.out.println("Erro");
        }
        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (confContainer == true) {
            confContainer = true;
            if (quantEtq < 10) {
                prodTipo = " CONTAINER    ";
                for (int i = 0; i < quantEtq; i++) {
                    if (i == 0) {
                        geraLogEtq();
                        preencherTabela();
                    }
                    impressaoPsico();

                }
                retornaTela();
            } else {
                JOptionPane.showMessageDialog(null, "Quantidade Acima do permitido!");
            }
        }

        if (!pfatProd.equals("")) {
            if (Integer.parseInt(pfatProd.substring(0, 1)) == 5 && confContainer == false) {//if(numEst >= 41 && numEst <= 44)

                confPsico = true;
                if (quantEtq < 10) {
                    prodTipo = "     PSICO    ";
                    for (int i = 0; i < quantEtq; i++) {
                        if (i == 0) {
                            geraLogEtq();
                            preencherTabela();
                        }
                        impressaoPsico();

                    }
                    retornaTela();
                } else {
                    JOptionPane.showMessageDialog(null, "Quantidade Acima do permitido!");
                }
            }
        }
        //busca por container

        if (!pfatProd.equals("")) {
            if (Integer.parseInt(pfatProd.substring(0, 1)) == 6 || Integer.parseInt(pfatProd.substring(0, 1)) == 7 && confContainer == false) {//if(numEst >= 41 && numEst <= 44)
                confHb = true;
                if (quantEtq < 10) {
                    prodTipo = "       HB      ";
                    for (int i = 0; i < quantEtq; i++) {
                        if (i == 0) {
                            geraLogEtq();
                            preencherTabela();
                        }
                        impressaoComum();

                    }
                    retornaTela();
                } else {
                    JOptionPane.showMessageDialog(null, "Quantidade Acima do permitido!");
                }
            }
        }

        if (confPsico == false && confContainer == false && confHb == false) {
            if (quantEtq < 10) {
                for (int i = 0; i < quantEtq; i++) {
                    if (i == 0) {
                        geraLogEtq();
                        preencherTabela();
                    }
                    impressaoComum();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Quantidade Acima do permitido!");
            }

            confPsico = false;
            retornaTela();

        }

        confPsico = false;
        confContainer = false;
        confHb = false;
    }

    public boolean validaDat(String dataInformada) {
        boolean erro = false;

        DataHora dth = new DataHora();

        String dataAt = dth.Data();

        int mes = 0;
        int ano = 0;

        int mesAt = 0;

        int anoAt = 0;

        String[] dataC = new String[2];
        String[] dataAtual = new String[3];

        dataC = dataInformada.split("/");
        dataAtual = dataAt.split("/");

        System.out.println("mes digitado : " + dataC[0]);
        System.out.println("ano digitado : " + dataC[1]);

        System.out.println("mes atual : " + dataAtual[1]);
        System.out.println("ano atual : " + dataAtual[2]);

        try {
            mes = Integer.parseInt(dataC[0]);
            ano = Integer.parseInt(dataC[1]);

            mesAt = Integer.parseInt(dataAtual[1]);
            anoAt = Integer.parseInt(dataAtual[2]);

            if (mes <= 12 && mes > 0) {
                if (ano >= anoAt && ano <= anoAt + 10) {
                    if (ano == anoAt) {
                        if (mes - mesAt == 3) {
                            JOptionPane.showMessageDialog(null, "Atenção ! \n Produto Próximo ao vencimento!");
                            System.out.println("mes do ano atual maior que 3");
                            erro = true;
                        } else {
                            if (mes - mesAt <= 2) {
                                JOptionPane.showMessageDialog(null, "Atenção ! \n Produto vencido!");
                                erro = false;
                            } else {
                                System.out.println("mes do ano atual menor que 3");
                                erro = true;
                            }
                        }
                    } else {
                        erro = true;
                    }
                } else {
                    System.out.println("ano menor que atual ou maior que 10");
                    erro = false;
                }
            } else {
                System.out.println("mes maior que 12 ou menor que 1");
                erro = false;
            }

        } catch (Exception e) {
            erro = false;
        }

        return erro;
    }

    private void impressaoComum() throws PrintException {

        impressao.imprime("^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n"
                + "^XA\n"
                + "^MMT\n"
                + "^PW799\n"
                + "^LL0480\n"
                + "^LS0\n"
                + "^FO3,3^GB757,432,8^FS\n"
                + "^FT55,136^A0N,113,206^FH\\^FD" + endProd + "^FS\n"
                + "^FO12,244^GB739,0,5^FS\n"
                + "^FO12,200^GB739,0,5^FS\n"
                + "^FT27,235^A0N,28,50^FH\\^FD" + descricaoProd + "^FS\n"
                + "^FT23,435^BQN,2,6\n"
                + "^FH\\^FDLA," + idEmissor + "^FS\n"
                + "^FO466,250^GB0,177,5^FS\n"
                + "^FO161,251^GB0,176,5^FS\n"
                + "^FO471,276^GB281,0,4^FS\n"
                + "^FO10,274^GB155,0,5^FS\n"
                + "^FT179,283^A0N,17,16^FH\\^FDEmiss\\C6o^FS\n"
                + "^FT555,269^A0N,17,16^FH\\^FDC\\A2digo Ean PFAT^FS\n"
                + "^FT24,269^A0N,17,16^FH\\^FDCodigo do Emissor^FS\n"
                + "^FT244,287^A0N,28,28^FH\\^FD" + data + "^FS\n"
                + "^FT387,286^A0N,28,28^FH\\^FD" + horaMinuto + "^FS\n"
                + "^FT179,414^A0N,20,19^FH\\^FDArea SantaCruz^FS\n"
                + "^FT342,413^A0N,20,19^FH\\^FD" + codAStcruz + "^FS\n"
                + "^FT14,195^A0N,17,16^FH\\^FD" + codIntervalo + "^FS\n"
                + "^BY3,3,78^FT507,373^BCN,,Y,N\n"
                + "^FD>;" + pfatProd + "^FS\n"
                + "^FT181,364^A0N,51,50^FH\\^FD" + prodTipo + "^FS\n"
                + "^LRY^FO12,12^GB739,0,166^FS^LRN\n"
                + "^LRY^FO167,300^GB297,0,126^FS^LRN\n"
                + "^PQ1,0,1,Y^XZ");

    }

    private void impressaoExcesso() throws PrintException {

        impressao.imprime("^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n"
                + "^XA\n"
                + "^MMT\n"
                + "^PW799\n"
                + "^LL0480\n"
                + "^LS0\n"
                + "^FO4,4^GB757,432,8^FS\n"
                + "^FT48,114^A0N,90,208^FH\\^FD" + endProd + "^FS\n"
                + "^FT72,342^A0N,150,165^FH\\^FDEXCESSO^FS\n"
                + "^LRY^FO14,150^GB739,0,277^FS^LRN\n"
                + "^PQ1,0,1,Y^XZ");

    }

    private void impressaoRUrgente() throws PrintException {

        impressao.imprime("^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n"
                + "^XA\n"
                + "^MMT\n"
                + "^PW799\n"
                + "^LL0480\n"
                + "^LS0\n"
                + "^FO3,3^GB757,432,8^FS\n"
                + "^FT61,112^A0N,90,204^FH\\^FD" + endProd + "^FS\n"
                + "^FT192,387^A0N,92,93^FH\\^FDURGENTE^FS\n"
                + "^FT159,259^A0N,92,93^FH\\^FDREPOSI\\80\\C7O^FS\n"
                + "^LRY^FO12,148^GB739,0,278^FS^LRN\n"
                + "^PQ1,0,1,Y^XZ");

    }

    private void impressaoPsico() throws PrintException {

        impressao.imprime("^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR4,4~SD15^JUS^LRN^CI0^XZ\n"
                + "^XA\n"
                + "^MMT\n"
                + "^PW799\n"
                + "^LL0480\n"
                + "^LS0\n"
                + "^FO3,3^GB757,432,8^FS\n"
                + "^FT55,136^A0N,113,206^FH\\^FD" + endProd + "^FS\n"
                + "^FO12,244^GB739,0,5^FS\n"
                + "^FO12,200^GB739,0,5^FS\n"
                + "^FT27,235^A0N,28,50^FH\\^FD" + descricaoProd + "^FS\n"
                + "^FT23,435^BQN,2,6\n"
                + "^FH\\^FDLA," + idEmissor + "^FS\n"
                + "^FO466,250^GB0,177,5^FS\n"
                + "^FO161,251^GB0,176,5^FS\n"
                + "^FO471,276^GB281,0,4^FS\n"
                + "^FO10,274^GB155,0,5^FS\n"
                + "^FT179,283^A0N,17,16^FH\\^FDEmiss\\C6o^FS\n"
                + "^FT555,269^A0N,17,16^FH\\^FDC\\A2digo Ean PFAT^FS\n"
                + "^FT24,269^A0N,17,16^FH\\^FDCodigo do Emissor^FS\n"
                + "^FT244,287^A0N,28,28^FH\\^FD" + data + "^FS\n"
                + "^FT387,286^A0N,28,28^FH\\^FD" + horaMinuto + "^FS\n"
                + "^FT179,414^A0N,20,19^FH\\^FDArea SantaCruz^FS\n"
                + "^FT342,413^A0N,20,19^FH\\^FD" + codAStcruz + "^FS\n"
                + "^FT14,195^A0N,17,16^FH\\^FD" + codIntervalo + "^FS\n"
                + "^BY3,3,78^FT507,373^BCN,,Y,N\n"
                + "^FD>;" + pfatProd + "^FS\n"
                + "^FT181,364^A0N,51,50^FH\\^FD" + prodTipo + "^FS\n"
                + "^LRY^FO167,300^GB297,0,126^FS^LRN\n"
                + "^PQ1,0,1,Y^XZ");
    }

    private void retornaTela() {

        eanProduto = "";
        descricaoProd = "";
        endProd = "";
        pfatProd = "";
        sapProd = "";
        validadeProduto = "";

        lblAcaoEmetq.setText("Passe o crachá na leitora");

        descProdTxt.setText("");
        eanLbl.setText("");
        pfatLbl.setText("");
        enderecoLbl.setText("");
        sapLbl.setText("");
        prodTipo = "";
        codAStcruz = "";
        codIntervalo = "";
        excessoLbl.setText("");
        validadeVezLbl.setText("");
        statZerado = 0;

    }

    public void consultaQEx() {
        try {

            int contEx = 0;
            String textoEx;
            String valVez = "";
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from pbl5050m WHERE codigo = '" + pfatProd + "' ORDER BY data_mysql ASC";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            while (objCon.rs.next()) {
                contEx++;
                if (contEx == 1) {
                    valVez = objCon.rs.getString("validade");
                }
            }

            textoEx = Integer.toString(contEx);
            excessoLbl.setText(textoEx);

            validadeVezLbl.setText(valVez);

        } catch (SQLException ex) {
            System.out.println("erro");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void geraLogEtq() throws SQLException, ParseException {
        String nomeWEmissor = "";

        //System.out.println("idems" + " " + idEmissor);
        try {
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from login WHERE cracha = " + idEmissor + "";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            objCon.rs.first();

            nomeWEmissor = objCon.rs.getString("nome");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Usuário Inválido");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataHora dh = new DataHora();
        String horaAtualZ = dh.Hora();
        String dataAtualZ = dh.Data();
        //String EmissZ = idEmissor;
        String EmissZ = nomeWEmissor;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataMsl = new java.sql.Date(format.parse(dataAtualZ).getTime());

        SimpleDateFormat formaData = new SimpleDateFormat("yyyy-MM-dd");
        String dataFinal = formaData.format(dataMsl);

        try {

            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String sq = "INSERT INTO log_emissao_etq (data, hora, emissor, usuario, codigo, descricao, estacao, validade, zerado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setDate(1, dataMsl);
            pst.setString(2, horaAtualZ);
            pst.setString(3, EmissZ);
            pst.setString(4, nomeEmissor);
            pst.setString(5, pfatProd);
            pst.setString(6, descricaoProd);
            pst.setString(7, endProd);
            pst.setString(8, validadeProduto);
            pst.setInt(9, statZerado);

            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            System.out.println("erro");
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAcesso(String[] chaveAcesso) {

        menuPrincInicio.setVisible(false);
        menuPrincRecebimento.setVisible(false);
        menuPrincArmazenagem.setVisible(false);
        menuPrincReposicao.setVisible(false);
        menuPrincBalanceamento.setVisible(false);
        menuPrincAtualiz.setVisible(false);
        menuPrincConf.setVisible(false);
        menuPrincAjuda.setVisible(false);
        menuPrincExpedicao.setVisible(false);
        
        int recebimentoLevelAc = Integer.parseInt(chaveAcesso[1]);

        System.out.println("chave 1 = " + chaveAcesso[0]);
        //IndiceMenu
        if (chaveAcesso[0].equals("1")) {
            menuPrincInicio.setVisible(true);
        }

       /* if (chaveAcesso[1].equals("1")) {
            menuPrincRecebimento.setVisible(true);
        } */
       
       if(recebimentoLevelAc > 0) {
           menuPrincRecebimento.setVisible(true);
           switch(recebimentoLevelAc) {
                case 1 :
                   jMenuItem1.setEnabled(true);
                break;
                case 2 :
                   jMenuItem1.setEnabled(true);
                   //jMenuItem2.setEnabled(true);
                break;
                case 3 :
                   jMenuItem1.setEnabled(true);
                   //jMenuItem2.setEnabled(true);
                   jMenuItem3.setEnabled(true);
                break;
           }
       }

        if (chaveAcesso[2].equals("1")) {
            menuPrincArmazenagem.setVisible(true);
        }

        if (chaveAcesso[3].equals("1")) {
            menuPrincReposicao.setVisible(true);
        }

        if (chaveAcesso[4].equals("1")) {
            menuPrincBalanceamento.setVisible(true);
        }

        if (chaveAcesso[5].equals("1")) {
            menuPrincAtualiz.setVisible(true);
        }

        if (chaveAcesso[6].equals("1")) {
            menuPrincConf.setVisible(true);
        }

        if (chaveAcesso[7].equals("1")) {
            menuPrincAjuda.setVisible(true);
        }
        
        
        if (chaveAcesso[8].equals("1")) {
            menuPrincExpedicao.setVisible(true);
        }

        //Item de Menu
    }

    public void preencherTabela() throws SQLException, ParseException {

        DataHora dh = new DataHora();
        String horaAtualZ = dh.Hora();
        String dataAtualZ = dh.Data();
        String EmissZ = idEmissor;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataMsl = new java.sql.Date(format.parse(dataAtualZ).getTime());

        int contTab = 0;
        try {
            ArrayList dados = new ArrayList();

            String[] Colunas = new String[]{"Codigo", "Descrição", "Validade", "Data Emissão", "Hora"};
            objCon.openConnectionMysql();
            objCon.stmt = objCon.con.createStatement();

            String SQL = "SELECT * from log_emissao_etq WHERE data = '" + dataMsl + "' ORDER BY hora DESC";
            objCon.rs = objCon.stmt.executeQuery(SQL);

            ///faltou o executa sql
            objCon.rs.first();
            /////////////////////////////////////
            do {
                contTab++;
                if (contTab == 20) {
                    break;
                }
                dados.add(new Object[]{objCon.rs.getInt("codigo"), objCon.rs.getString("descricao"), objCon.rs.getString("validade"), objCon.rs.getString("data"), objCon.rs.getString("hora")});
            } while (objCon.rs.next());

            Tabela tabela = new Tabela(dados, Colunas);
            tblLogEmissao.setModel(tabela);
            tblLogEmissao.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblLogEmissao.getColumnModel().getColumn(0).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(1).setPreferredWidth(612);
            tblLogEmissao.getColumnModel().getColumn(1).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblLogEmissao.getColumnModel().getColumn(2).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblLogEmissao.getColumnModel().getColumn(3).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblLogEmissao.getColumnModel().getColumn(4).setResizable(false);
            tblLogEmissao.getTableHeader().setReorderingAllowed(false);
            tblLogEmissao.setAutoResizeMode(tblLogEmissao.AUTO_RESIZE_OFF);
            tblLogEmissao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        } catch (Exception e) {
            System.out.println("catch");
            ArrayList dadoss = new ArrayList();

            String[] Colunass = new String[]{"Codigo", "Descrição", "Validade", "Data Emissão", "Hora"};

            dadoss.removeAll(dadoss);
            Tabela tabelal = new Tabela(dadoss, Colunass);
            tblLogEmissao.setModel(tabelal);
            tblLogEmissao.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblLogEmissao.getColumnModel().getColumn(0).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(1).setPreferredWidth(612);
            tblLogEmissao.getColumnModel().getColumn(1).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblLogEmissao.getColumnModel().getColumn(2).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblLogEmissao.getColumnModel().getColumn(3).setResizable(false);
            tblLogEmissao.getColumnModel().getColumn(4).setPreferredWidth(90);
            tblLogEmissao.getColumnModel().getColumn(4).setResizable(false);
            tblLogEmissao.getTableHeader().setReorderingAllowed(false);
            tblLogEmissao.setAutoResizeMode(tblLogEmissao.AUTO_RESIZE_OFF);
            tblLogEmissao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "atualizacao");

        Atualizacao atzRel = new Atualizacao(lblAtuCurvaLbl, lblAtuMb52Lbl, lblAtuPbl5050mLbl, lblAtuPln0055rLbl, txtAtuRelDatTxt, btnReatBtn, btnRemUserBtn);


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JOptionPane.showMessageDialog(null, "Lembre-se : \n O arquivo deverá estar no formato .TXT");

        uprelPln0055r = "pln0055r";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //int i= file.showSaveDialog(null);
        int i = file.showOpenDialog(null);

        if (i == 1) {
            pln0055rTxt.setText("");
            uppln0055r = null;
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
        } else {
            uppln0055r = file.getSelectedFile();
            uppatchPln0055r = uppln0055r.getPath();

            String extensaoArq = uppatchPln0055r.substring(uppatchPln0055r.lastIndexOf(".") + 1);

            extensaoArq = extensaoArq.toUpperCase();

            if ("TXT".equals(extensaoArq)) {
                pln0055rTxt.setText(uppln0055r.getPath());
                String patch = uppln0055r.getPath();

            } else {
                JOptionPane.showMessageDialog(null, "Arquivo Invalido! \n\n O arquivo deverá estar no formato .TXT");
                uppln0055r = null;
            }

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void emAcaoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emAcaoTxtActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_emAcaoTxtActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        new Thread() {

            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "ATENÇÃO! \n Não feche a janela até a finalização da atualização \n caso contrário ocasionará em erros graves!\n \n"
                        + "Travamentos são comuns durante a atualização. \n \n" + "Clique em OK para continuar com a atualização \n e aguarde a finalização");

                dataAtualizacaoTxt.setText("Atualizando...");
                try {

                    UploadArquivosCsv uploadPln0055r = new UploadArquivosCsv(uppln0055r, uppatchPln0055r, uprelPln0055r, upcurvaAbc, uppatchCurvaAbc, uprelCurvaAbc,
                            upmb52, uppatchMb52, uprelMb52, uppbl5050m, uppatchPbl5050m, uprelPbl5050m);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                boolean uso = true;
                try {

                    objCon.openConnectionMysql();
                    objCon.stmt = objCon.con.createStatement();

                    String SQL = "SELECT * from configuracoes WHERE numero = 1";
                    objCon.rs = objCon.stmt.executeQuery(SQL);

                    objCon.rs.first();

                    int bloc = objCon.rs.getInt("bloqueio");

                    if (bloc == 1) {
                        uso = false;
                    } else {
                        uso = true;

                    }

                } catch (SQLException ex) {
                    uso = false;
                }

                try {
                    objCon.con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                atuaAtual = retornaDataAtz();
                retornaDataAtz();
                if (uso == true) {
                    JOptionPane.showMessageDialog(null, "Atualização realizada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro \n Outro usuário está atualizando.");
                }

            }
        }.start();


    }//GEN-LAST:event_jButton7ActionPerformed

    private void curvaAbcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curvaAbcBtnActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Lembre-se : \n O arquivo deverá estar no formato .CSV");

        uprelCurvaAbc = "curva_abc";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //int i= file.showSaveDialog(null);
        int i = file.showOpenDialog(null);

        if (i == 1) {
            curvaAbcTxt.setText("");
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
            upcurvaAbc = null;
        } else {
            upcurvaAbc = file.getSelectedFile();
            uppatchCurvaAbc = upcurvaAbc.getPath();

            String extensaoArq = uppatchCurvaAbc.substring(uppatchCurvaAbc.lastIndexOf(".") + 1);

            extensaoArq = extensaoArq.toUpperCase();

            if ("CSV".equals(extensaoArq)) {
                curvaAbcTxt.setText(upcurvaAbc.getPath());
                String patch = upcurvaAbc.getPath();
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo Invalido! \n\n O arquivo deverá estar no formato .CSV");
                upcurvaAbc = null;
            }
        }
    }//GEN-LAST:event_curvaAbcBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Lembre-se : \n O arquivo deverá estar no formato .CSV");

        uprelMb52 = "mb52";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //int i= file.showSaveDialog(null);
        int i = file.showOpenDialog(null);

        if (i == 1) {
            mb52Txt.setText("");
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
            upmb52 = null;
        } else {
            upmb52 = file.getSelectedFile();
            uppatchMb52 = upmb52.getPath();

            String extensaoArq = uppatchMb52.substring(uppatchMb52.lastIndexOf(".") + 1);

            extensaoArq = extensaoArq.toUpperCase();

            if ("CSV".equals(extensaoArq)) {
                mb52Txt.setText(upmb52.getPath());
                String patch = upmb52.getPath();
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo Invalido! \n\n O arquivo deverá estar no formato .CSV");
                upmb52 = null;
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Lembre-se : \n O arquivo deverá estar no formato .CSV");

        uprelPbl5050m = "pbl5050m";

        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int i = file.showOpenDialog(null);

        if (i == 1) {
            pbl5050mTxt.setText("");
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
            uppbl5050m = null;
        } else {
            uppbl5050m = file.getSelectedFile();
            uppatchPbl5050m = uppbl5050m.getPath();

            String extensaoArq = uppatchPbl5050m.substring(uppatchPbl5050m.lastIndexOf(".") + 1);
            extensaoArq = extensaoArq.toUpperCase();

            if ("CSV".equals(extensaoArq)) {
                pbl5050mTxt.setText(uppbl5050m.getPath());
                String patch = uppbl5050m.getPath();
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo Invalido! \n\n O arquivo deverá estar no formato .CSV");
                uppbl5050m = null;
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:

        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "pnlRel");

        DataHora dh = new DataHora();
        String dataAtualZ = dh.Data();

        txtDtIni.setText(dataAtualZ);
        txtDtFim.setText(dataAtualZ);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        retornaTela();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void chkDescZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDescZeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDescZeroActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        RelatorioEmissaoEtiquetas rE = new RelatorioEmissaoEtiquetas(tblRelEmissao, txtDtIni, txtDtFim, txtProdPfat, chkSomenteZero, chkDescZero, btnProcess);
        try {
            rE.processarTabela();
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void menuPrincConfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrincConfActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_menuPrincConfActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:

        Configuracoes cfg = new Configuracoes(nomeEmissor);
        cfg.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "pnletq");

        try {
            etqPranchao = new EtqPranchao(pranAreaStzTxt, pranEstacaoTxt, pranNivelTxt, pranNivelTodosRb, pranPNivelRb, pranBtnImpBtn);
            etqPranchao.zeraCampos();
        } catch (PrintException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void pranAreaStzTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pranAreaStzTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pranAreaStzTxtActionPerformed

    private void pranEstacaoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pranEstacaoTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pranEstacaoTxtActionPerformed

    private void pranBtnImpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pranBtnImpBtnActionPerformed
        try {
            // TODO add your handling code here:
            etqPranchao.processaInfo();
        } catch (PrintException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pranBtnImpBtnActionPerformed

    private void pranNivelTodosRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pranNivelTodosRbActionPerformed
        // TODO add your handling code here:
        etqPranchao.rBtnTodos();
    }//GEN-LAST:event_pranNivelTodosRbActionPerformed

    private void pranPNivelRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pranPNivelRbActionPerformed
        // TODO add your handling code here:

        etqPranchao.rBtnPNivel();
    }//GEN-LAST:event_pranPNivelRbActionPerformed

    private void EAAlterarRdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAAlterarRdActionPerformed
        // TODO add your handling code here:
        editArea.radioButons(2);
    }//GEN-LAST:event_EAAlterarRdActionPerformed

    private void EAExcluirRdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAExcluirRdActionPerformed
        // TODO add your handling code here:
        editArea.radioButons(3);
    }//GEN-LAST:event_EAExcluirRdActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int modulo = 1;
        int bloquear = 0;
        try {

            objCon.openConnectionMysql();

            objCon.stmt = objCon.con.createStatement();

            String sq = "UPDATE configuracoes SET bloqueio = ? WHERE numero = ?";
            PreparedStatement pst = objCon.con.prepareStatement(sq);

            pst.setInt(1, bloquear);
            pst.setInt(2, modulo);

            pst.executeUpdate();

            pst.close();

            System.out.println("Desbloqueado!");

        } catch (SQLException ex) {
            System.out.println("Erro no bloqueio geral!");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            objCon.con.close();
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        RelatorioEmissaoEtiquetas rE = new RelatorioEmissaoEtiquetas(tblRelEmissao, txtDtIni, txtDtFim, txtProdPfat, chkSomenteZero, chkDescZero, btnProcess);
        try {
            rE.dadosGeracaoRelatorio();
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnReatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReatBtnActionPerformed
        // TODO add your handling code here:
        new Thread() {

            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "ATENÇÃO! \n Não feche a janela até a finalização da atualização \n caso contrário ocasionará em erros graves!\n \n"
                        + "Clique em OK para continuar com a atualização \n e aguarde a finalização.");

                Atualizacao atzRel = new Atualizacao(lblAtuCurvaLbl, lblAtuMb52Lbl, lblAtuPbl5050mLbl, lblAtuPln0055rLbl, txtAtuRelDatTxt, btnReatBtn, btnRemUserBtn);
                try {
                    atzRel.realizaAtualizacao();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                retornaDataAtz();

            }
        }.start();


    }//GEN-LAST:event_btnReatBtnActionPerformed

    private void btnRemUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemUserBtnActionPerformed
        // TODO add your handling code here:

        Atualizacao atzRel = new Atualizacao(lblAtuCurvaLbl, lblAtuMb52Lbl, lblAtuPbl5050mLbl, lblAtuPln0055rLbl, txtAtuRelDatTxt, btnReatBtn, btnRemUserBtn);

        atzRel.desbloquear();
    }//GEN-LAST:event_btnRemUserBtnActionPerformed

    private void EAAreaStzTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAAreaStzTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EAAreaStzTxtActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "editAStz");

        editArea = new EditarAreaStz(EAIncluirRd, EAAlterarRd, EAExcluirRd, EAAreaStzTxt, EARuaTxt, EANivelTxt, EAPosicaoTxt, EAPosicaoIniTxt, EAPosicaoFimTxt, EASequenciaTxt, EAEstacaoTxt, EAIncluirBtn, EAAlterarBtn, EAExcluirBtn, EA1lbl, EA2lbl, EA3lbl, EA4lbl, EA5lbl, EA6lbl, EA7lbl, EA8lbl);
        editArea.estadoInicial();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void EAIncluirRdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EAIncluirRdMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_EAIncluirRdMouseClicked

    private void EAAlterarRdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EAAlterarRdMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_EAAlterarRdMouseClicked

    private void EAExcluirRdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EAExcluirRdMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_EAExcluirRdMouseClicked

    private void EAIncluirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAIncluirBtnActionPerformed
        // TODO add your handling code here:
        editArea.realizaInclusao();
    }//GEN-LAST:event_EAIncluirBtnActionPerformed

    private void EAAreaStzTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EAAreaStzTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editArea.realizaEnter();
        }
    }//GEN-LAST:event_EAAreaStzTxtKeyPressed

    private void EAAlterarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAAlterarBtnActionPerformed
        // TODO add your handling code here:
        editArea.realizaExclusao();
        editArea.realizaInclusao();
        //editArea.updateAreaStz();
    }//GEN-LAST:event_EAAlterarBtnActionPerformed

    private void EAExcluirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAExcluirBtnActionPerformed
        // TODO add your handling code here:
        editArea.realizaExclusao();
    }//GEN-LAST:event_EAExcluirBtnActionPerformed

    private void EAIncluirRdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EAIncluirRdActionPerformed
        // TODO add your handling code here:
        editArea.radioButons(1);
    }//GEN-LAST:event_EAIncluirRdActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        pnlEArmazenagem = new EtqArmazenagem();
        Root.add(pnlEArmazenagem, "etqexcesso");
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "etqexcesso");
        pnlEArmazenagem.setaFoco();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        grafEst = new ListaAntibioticos();
        
        Root.add(grafEst, "relAntib");
        
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "relAntib");
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void txtAtuRelDatTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAtuRelDatTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAtuRelDatTxtActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        conRedes = new ConsultaGrandesRedes();
        
        Root.add(conRedes, "consRedes");
        
        CardLayout card = (CardLayout) Root.getLayout();
        card.show(Root, "consRedes");
    }//GEN-LAST:event_jMenuItem11ActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main("", "", "").setVisible(true);
                } catch (PrintException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AreaStz;
    private javax.swing.JPanel Atualizacao;
    private javax.swing.JPanel AtzArquivos;
    private javax.swing.JDesktopPane BackHome;
    private javax.swing.JDesktopPane Backgroung;
    private javax.swing.JLabel EA1lbl;
    private javax.swing.JLabel EA2lbl;
    private javax.swing.JLabel EA3lbl;
    private javax.swing.JLabel EA4lbl;
    private javax.swing.JLabel EA5lbl;
    private javax.swing.JLabel EA6lbl;
    private javax.swing.JLabel EA7lbl;
    private javax.swing.JLabel EA8lbl;
    private javax.swing.JButton EAAlterarBtn;
    private javax.swing.JRadioButton EAAlterarRd;
    private javax.swing.JTextField EAAreaStzTxt;
    private javax.swing.JTextField EAEstacaoTxt;
    private javax.swing.JButton EAExcluirBtn;
    private javax.swing.JRadioButton EAExcluirRd;
    private javax.swing.JButton EAIncluirBtn;
    private javax.swing.JRadioButton EAIncluirRd;
    private javax.swing.JTextField EANivelTxt;
    private javax.swing.JTextField EAPosicaoFimTxt;
    private javax.swing.JTextField EAPosicaoIniTxt;
    private javax.swing.JTextField EAPosicaoTxt;
    private javax.swing.JTextField EARuaTxt;
    private javax.swing.JTextField EASequenciaTxt;
    private javax.swing.JPanel EmFixo;
    private javax.swing.JPanel EmFixo1;
    private javax.swing.JPanel EmFixo2;
    private javax.swing.JPanel EmIndex;
    private javax.swing.JPanel EmIndex1;
    private javax.swing.JPanel EmIndex2;
    private javax.swing.JPanel Emetq;
    private javax.swing.JPanel Etiquetas;
    private javax.swing.JPanel Home;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel Principal;
    private javax.swing.JPanel RelEmissao;
    private javax.swing.JPanel Root;
    private javax.swing.JButton btnProcess;
    private javax.swing.JButton btnReatBtn;
    private javax.swing.JButton btnRemUserBtn;
    private javax.swing.JCheckBox chkDescZero;
    private javax.swing.JCheckBox chkSomenteZero;
    private javax.swing.JButton curvaAbcBtn;
    private javax.swing.JTextField curvaAbcTxt;
    private javax.swing.JTextField dataAtualizacaoTxt;
    private javax.swing.JTextField descProdTxt;
    private javax.swing.JTextField eanLbl;
    private javax.swing.JTextField emAcaoTxt;
    private javax.swing.JTextField enderecoLbl;
    private javax.swing.JTextField excessoLbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAcaoEmetq;
    private javax.swing.JLabel lblAtuCurvaLbl;
    private javax.swing.JLabel lblAtuMb52Lbl;
    private javax.swing.JLabel lblAtuPbl5050mLbl;
    private javax.swing.JLabel lblAtuPln0055rLbl;
    private javax.swing.JTextField mb52Txt;
    private javax.swing.JMenu menuPrincAjuda;
    private javax.swing.JMenu menuPrincArmazenagem;
    private javax.swing.JMenu menuPrincAtualiz;
    private javax.swing.JMenu menuPrincBalanceamento;
    private javax.swing.JMenu menuPrincConf;
    private javax.swing.JMenu menuPrincExpedicao;
    private javax.swing.JMenu menuPrincInicio;
    private javax.swing.JMenu menuPrincRecebimento;
    private javax.swing.JMenu menuPrincReposicao;
    private javax.swing.JLabel nomeUsuarioLbl;
    private javax.swing.JTextField origemAtzTxt;
    private javax.swing.JTextField pastaAplicTx;
    private javax.swing.JTextField pbl5050mTxt;
    private javax.swing.JTextField pfatLbl;
    private javax.swing.JTextField pln0055rTxt;
    private javax.swing.JTextField pranAreaStzTxt;
    private javax.swing.JButton pranBtnImpBtn;
    private javax.swing.JTextField pranEstacaoTxt;
    private javax.swing.JRadioButton pranNivelTodosRb;
    private javax.swing.JTextField pranNivelTxt;
    private javax.swing.JRadioButton pranPNivelRb;
    private javax.swing.JProgressBar progressAtzPb;
    private javax.swing.JTextField sapLbl;
    private javax.swing.JTable tblLogEmissao;
    private javax.swing.JTable tblRelEmissao;
    private javax.swing.JTextField txtAtuRelDatTxt;
    private javax.swing.JTextField txtDtFim;
    private javax.swing.JTextField txtDtIni;
    private javax.swing.JTextField txtProdPfat;
    private javax.swing.JTextField validadeVezLbl;
    // End of variables declaration//GEN-END:variables
}
