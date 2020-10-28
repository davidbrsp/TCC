package main;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 * Classe responsável pela interface com o usuário. GUI = Graphical User
 * Interface. Contém menus e janelas pelos quais o usuário interage
 */
public class GUI {
    private JFrame frame;
    private JMenuBar barraDeMenu;
    private JMenu menuArquivo;
    private JMenuItem itemAbrir;
    private JMenuItem itemSalvar;
    private JMenu menuFerramentas;
    private JMenuItem itemArea;
    private JMenuItem itemPerimetro;
    private JMenuItem itemContornoEmCm;
    private JMenuItem itemContornoEmPixels;
    private JMenuItem itemCurvatura;
    private JMenuItem itemEnergiaDeDobramento;
    private JMenuItem itemMaiorDiametro;
    private JMenuItem itemDistanciaMediaAteOCentro;
    private JMenuItem itemMedidaDeAssimetria;
    private JMenuItem itemSalvaTudo;
    // DAVID
    private JMenu menuVolume;
    private JMenuItem itemGetRed;
    private JMenuItem itemLimiarizaMaximumEntropy;
    private JMenuItem itemVolumePappus;
    private JMenuItem itemLimiarizaGmR;
    private JMenuItem itemProcessamentoAutomatico;
    private JMenuItem itemSalvaCsv;
    private JMenu menuDiretorio;
    private JMenuItem itemAbrirDiretorio;

    // mudei de public static final pra public final
    /**
     * Constantes associadas aos menus
     */
    public final String MENU_ARQUIVO = "Arquivo";
    public final String MENU_FERRAMENTAS = "Ferramentas";
    public final String ITEM_CONTORNO_EM_CM = "Salvar contorno (em cm) em ...";
    public final String ITEM_CONTORNO_EM_PIXELS = "Salvar contorno (em pixels) em ...";
    public final String ITEM_CURVATURA = "Salvar curvatura em ...";
    public final String ITEM_AREA = "Área";
    public final String ITEM_PERIMETRO = "Perímetro";
    public final String ITEM_ABRIR = "Abrir ...";
    public final String ITEM_SALVAR_IMAGEM_BINARIA = "Salvar imagem binária";
    public final String ITEM_ENERGIA_DE_DOBRAMENTO = "Energia de dobramento";
    public final String ITEM_MAIOR_DIAMETRO = "Maior diâmetro";
    public final String ITEM_DIST_MEDIA_ATE_O_CENTRO = "Distância média até o centro";
    public final String ITEM_MEDIDA_DE_ASSIMETRIA = "Medida de assimetria";
    public final String ITEM_SALVA_TUDO = "Salvar todas as medidas ...";
    public final String VETOR_PRIMARIO = "Vetor próprio primário"; // não
    // pertence
    // ao
    // menu
    public final String VETOR_SECUNDARIO = "Vetor próprio secundário"; // não
    // pertence
    // ao
    // menu
    // DAVID
    public final String MENU_VOLUME = "Volume";
    public final String ITEM_GET_RED = "Converte a faixa do vermelho em grayscale";
    public final String ITEM_LIMIARIZA_MAXIMUM_ENTROPY = "Limiariza usando a entropia maxima";
    public final String ITEM_VOLUME_PAPPUS = "Volume Pappus";
    public final String ITEM_LIMIARIZA_GMR = "Limiriza GmR";
    public final String ITEM_PROCESSAMENTO_AUTOMATICO = "Processamento Automático";
    public final String ITEM_SALVA_CSV = "Salva medidas em csv ...";
    public final String MENU_DIRETORIO = "Diretório";
    public final String ITEM_ABRIR_DIRETORIO = "Examina Diretório ...";

    private JPanel panoPrincipal;
    private JPanel panoDoSlider;
    private ImageIcon imageIcon;
    private JLabel label;
    private JScrollPane scrollPane;
    private JSlider slider;

    /**
     * Método inicial
     */
    public void constroiInterface() {
	this.frame = new JFrame("MAP2050");
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	this.slider = new JSlider(JSlider.VERTICAL, 0, 255, 128);
	this.slider.setMinorTickSpacing(5);
	this.slider.setMajorTickSpacing(50);
	this.slider.setPaintTicks(true);
	this.slider.setPaintLabels(true);
	this.slider.setPaintTrack(true);
	// slider.setLabelTable(slider.createStandardLabels(255));

	this.panoDoSlider = new JPanel();
	this.panoDoSlider.setLayout(new BoxLayout(this.panoDoSlider,
		BoxLayout.X_AXIS));
	this.panoDoSlider.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
		10));
	this.panoDoSlider.add(this.slider);

	this.panoPrincipal = new JPanel();
	this.panoPrincipal.setPreferredSize(new Dimension(480, 320));
	this.panoPrincipal.setLayout(new BoxLayout(this.panoPrincipal,
		BoxLayout.Y_AXIS));
	this.panoPrincipal.setBorder(BorderFactory
		.createEmptyBorder(5, 5, 5, 5));
	this.panoPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
	this.panoPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
	this.panoPrincipal.add(this.panoDoSlider);

	this.imageIcon = new ImageIcon("vazio");
	this.label = new JLabel(this.imageIcon);
	this.scrollPane = new JScrollPane(this.label);

	JPanel panoDaImagem = new JPanel();
	panoDaImagem.setLayout(new BoxLayout(panoDaImagem, BoxLayout.X_AXIS));

	this.panoDoSlider.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
		10));
	this.panoDoSlider.add(this.scrollPane);
	this.panoPrincipal.add(panoDaImagem);

	this.frame.add(this.panoPrincipal);
	this.frame.pack();

	// criando o menu
	this.barraDeMenu = new JMenuBar();
	this.menuArquivo = new JMenu(MENU_ARQUIVO);
	this.itemAbrir = new JMenuItem(ITEM_ABRIR);
	this.itemSalvar = new JMenuItem(ITEM_SALVAR_IMAGEM_BINARIA);
	this.menuFerramentas = new JMenu(MENU_FERRAMENTAS);
	this.itemArea = new JMenuItem(ITEM_AREA);
	this.itemPerimetro = new JMenuItem(ITEM_PERIMETRO);
	this.itemContornoEmCm = new JMenuItem(ITEM_CONTORNO_EM_CM);
	this.itemContornoEmPixels = new JMenuItem(ITEM_CONTORNO_EM_PIXELS);
	this.itemCurvatura = new JMenuItem(ITEM_CURVATURA);
	this.itemEnergiaDeDobramento = new JMenuItem(ITEM_ENERGIA_DE_DOBRAMENTO);
	this.itemMaiorDiametro = new JMenuItem(ITEM_MAIOR_DIAMETRO);
	this.itemMedidaDeAssimetria = new JMenuItem(ITEM_MEDIDA_DE_ASSIMETRIA);
	this.itemDistanciaMediaAteOCentro = new JMenuItem(
		ITEM_DIST_MEDIA_ATE_O_CENTRO);
	this.itemSalvaTudo = new JMenuItem(ITEM_SALVA_TUDO);

	// DAVID
	this.menuVolume = new JMenu(MENU_VOLUME);
	this.itemGetRed = new JMenuItem(ITEM_GET_RED);
	this.itemLimiarizaMaximumEntropy = new JMenuItem(
		ITEM_LIMIARIZA_MAXIMUM_ENTROPY);
	this.itemVolumePappus = new JMenuItem(ITEM_VOLUME_PAPPUS);
	this.itemLimiarizaGmR = new JMenuItem(ITEM_LIMIARIZA_GMR);
	this.itemProcessamentoAutomatico = new JMenuItem(
		ITEM_PROCESSAMENTO_AUTOMATICO);
	this.itemSalvaCsv = new JMenuItem(ITEM_SALVA_CSV);
	this.menuDiretorio = new JMenu(MENU_DIRETORIO);
	this.itemAbrirDiretorio = new JMenuItem(ITEM_ABRIR_DIRETORIO);

	// adicionando-os à tela
	this.menuArquivo.add(itemAbrir);
	this.menuArquivo.add(itemSalvar);
	// DAVID
	// barraDeMenu.add(menuVolume);
	this.menuVolume.add(this.itemGetRed);
	this.menuVolume.add(this.itemLimiarizaMaximumEntropy);
	this.menuVolume.add(this.itemVolumePappus);
	this.menuVolume.add(this.itemLimiarizaGmR);
	this.menuArquivo.add(this.itemProcessamentoAutomatico);
	this.menuArquivo.add(this.itemSalvaCsv);

	this.barraDeMenu.add(this.menuArquivo);

	this.barraDeMenu.add(this.menuDiretorio);
	this.menuDiretorio.add(this.itemAbrirDiretorio);

	this.menuFerramentas.add(this.itemArea);
	this.menuFerramentas.add(this.itemPerimetro);
	this.menuFerramentas.add(this.itemContornoEmCm);
	this.menuFerramentas.add(this.itemContornoEmPixels);
	this.menuFerramentas.add(this.itemCurvatura);
	this.menuFerramentas.add(this.itemEnergiaDeDobramento);
	this.menuFerramentas.add(this.itemMaiorDiametro);
	this.menuFerramentas.add(this.itemMedidaDeAssimetria);
	this.menuFerramentas.add(this.itemDistanciaMediaAteOCentro);
	this.menuFerramentas.add(this.itemSalvaTudo);
	this.menuFerramentas.add(this.itemVolumePappus);

	this.barraDeMenu.add(this.menuFerramentas);

	this.frame.setJMenuBar(this.barraDeMenu);
	this.frame.setVisible(true);
    }

    /**
     * Função que conecta menus às medidas
     * 
     * @param m
     *                instância de Medidas
     * @see Medidas
     */
    public void adicionaOuvidoresAosItensDoMenu(Medidas m) {
	this.itemArea.addActionListener(m.pegaArea());
	this.itemPerimetro.addActionListener(m.pegaPerimetro());
	this.itemContornoEmCm.addActionListener(m.pegaContornoEmCm());
	this.itemContornoEmPixels.addActionListener(m.pegaContorno());
	this.itemCurvatura.addActionListener(m.pegaCurvatura());
	this.itemEnergiaDeDobramento.addActionListener(m
		.pegaEnergiaDeDobramento());
	this.itemMaiorDiametro.addActionListener(m.pegaMaiorDiametro());
	this.itemMedidaDeAssimetria.addActionListener(m
		.pegaMedidaDeAssimetria());
	this.itemDistanciaMediaAteOCentro.addActionListener(m.pegaRaioMedio());
	this.itemAbrir.addActionListener(Controlador.getInstance());
	this.itemSalvar.addActionListener(Controlador.getInstance());
	this.itemSalvaTudo.addActionListener(Controlador.getInstance());
	// DAVID
	this.itemGetRed.addActionListener(Controlador.getInstance());
	this.itemLimiarizaMaximumEntropy.addActionListener(Controlador
		.getInstance());
	this.itemVolumePappus.addActionListener(m.pegaVolume());
	this.itemLimiarizaGmR.addActionListener(Controlador.getInstance());
	this.itemProcessamentoAutomatico.addActionListener(Controlador
		.getInstance());
	this.itemSalvaCsv.addActionListener(Controlador.getInstance());
	this.itemAbrirDiretorio.addActionListener(Controlador.getInstance());
    }

    /**
     * Função que muda o tamanho da janela
     * 
     * @author David Macedo da Conceição
     * @param largura
     *                Altura da janela.
     * @param altura
     *                Largura da janela.
     */
    public void redimensionaJanela(int largura, int altura) {
	this.frame.setSize(largura, altura);
    }

    /**
     * Função que adiciona ouvidores ao slider
     * 
     * @param ouvidor
     * @see Controlador
     */
    public void adicionaOuvidorAoSlider(ChangeListener ouvidor) {
	this.slider.addChangeListener(ouvidor);
    }

    /**
     * Função que exibe uma imagem na tela
     * 
     * @param imagem
     */
    public void exibeImagem(Image imagem) {
	this.imageIcon = new ImageIcon(imagem);
	this.label.setIcon(this.imageIcon);
	this.redimensionaJanela(this.imageIcon.getIconWidth() + 120,
		this.imageIcon.getIconHeight() + 120);
    }

    /**
     * Função que exibe um erro na tela
     * 
     * @param msg
     *                mensagem de erro
     */
    public void exibeErro(String msg) {
	JOptionPane.showMessageDialog(frame, msg, "Erro!",
		JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Função que exibe um alerta na tela
     * 
     * @param msg
     *                mensagem de alerta
     */
    public void exibeAlerta(String msg) {
	JOptionPane.showMessageDialog(frame, msg, "Alerta!",
		JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Função que exibe uma mensagem na tela
     * 
     * @param msg
     *                mensagem
     */
    public void exibeMensagem(String msg) {
	JOptionPane.showMessageDialog(this.frame, msg, "Informação!",
		JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Função que exibe uma janela pedindo uma escolha ao usuário
     * 
     * @param texto
     *                pergunta
     * @param titulo
     *                título da janela
     * @param opcoes
     *                vetor com as opções
     * @return opção escolhida
     */
    public int exibeEscolha(String texto, String titulo, Object[] opcoes) {
	return JOptionPane.showOptionDialog(this.frame, texto, titulo,
		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
		null, opcoes, opcoes[1]);
    }

    /**
     * Função que altera o título da janela principal
     * 
     * @param titulo
     */
    public void alteraTituloDaJanela(String titulo) {
	this.frame.setTitle(titulo);
    }

    /**
     * Função que altera o valor atual do slider
     * 
     * @author David Macedo da Conceição
     * @param n
     *                novo valor para o Slider.
     */
    public void alteraTickDoSlider(int n) {
	this.slider.setValueIsAdjusting(true);
	this.slider.setValue(n);
    }
}
