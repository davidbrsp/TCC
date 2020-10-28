package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import medidas.Area;
import medidas.ContornoEmCm;
import medidas.Curvatura;
import medidas.DistanciaMediaAteOCentro;
import medidas.EnergiaDeDobramento;
import medidas.MaiorDiametro;
import medidas.Medida;
import medidas.MedidaDeAssimetria;
import medidas.Perimetro;
import medidas.PixelsDoContorno;
import medidas.Volume;
import util.LidaComArquivos;
import util.PreProcessador;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe principal da aplicação. Contém as imagens: exibida, do objeto (
 * binarizada ) e da referência de escala binarizada. É ouvidora do slider da
 * GUI e invoca o processo de binarização. Associa as medidas aos menus.
 * Responsável por tomar a ação adequada quando uma medida acaba de ser extraída (
 * informar ao usuário, salvar um arquivo, etc. ) Instância única ( Singleton ).
 * Usar o getInstance( ) ao invés do construtor.
 */
public class Controlador implements ActionListener, ChangeListener {
    private static Controlador instancia;

    /**
     * Esta classe é um Singleton, só possui uma instância. Não é possível
     * instanciá-la usando o construtor
     * 
     * @return instância única da classe
     */
    public static Controlador getInstance() {
	if (instancia == null)
	    instancia = new Controlador();
	return instancia;
    }

    /**
     * Construtor privado da classe.
     * 
     * @see getInstance
     */
    private Controlador() {
	limiar = 0;
	imagemOriginal = null;
	objetoBinarizado = null;
	referenciaDeEscalaBinarizada = null;
	medidas = Medidas.getInstance();
    }

    /**
     * Instância única de Medidas
     */
    public Medidas medidas;
    private BufferedImage imagemOriginal;
    private BufferedImage imagemExibida;
    /**
     * Imagem binarizada do objeto binarizado
     */
    public boolean[][] objetoBinarizado;
    /**
     * imagem binarizada da referência de escala
     */
    public boolean[][] referenciaDeEscalaBinarizada;
    // DAVID: mudei para public
    public GUI gui;
    private int limiar;
    /**
     * Raio da referência de escala em milímetros
     */
    public static final double RAIO_DA_REFERENCIA_DA_ESCALA = 13;

    /**
     * Exibe erro quando não há ainda imagem binária e limiariza a imagem quando
     * há
     */
    public void stateChanged(ChangeEvent e) {
	if (imagemOriginal == null)
	    gui
		    .exibeErro("Abra uma imagem ( pelo menu \"Arquivo\" ) antes de usar o slider");
	else {
	    JSlider source = (JSlider) e.getSource();

	    if (!source.getValueIsAdjusting()) {
		limiar = (int) source.getValue();
		// System.out.print("Binarizando imagem com limiar igual a "
		// + limiar + "..");
		boolean[][] binarizada = PreProcessador.limiariza(limiar,
			imagemOriginal, 0);
		gui
			.alteraTituloDaJanela("Processando GrayScale da banda vermelha de "
				+ LidaComArquivos.nomeArquivoDeEntrada
				+ " com limiar " + limiar + ".");
		examinaImagem(binarizada);
		gui.alteraTituloDaJanela("Examinando ovo: "
			+ LidaComArquivos.nomeArquivoDeEntrada);
		this.gui.exibeImagem(this.imagemExibida);
	    } else {
		limiar = (int) source.getValue();
		gui.alteraTituloDaJanela("Examinando ovo: "
			+ LidaComArquivos.nomeArquivoDeEntrada
			+ ". Limiar em: " + limiar + ".");
	    }
	}
    }

    private boolean[][] copia(boolean[][] matriz) {
	if (matriz == null)
	    return null;
	boolean[][] copia = new boolean[matriz.length][matriz[0].length];
	for (int i = 0; i < matriz.length; i++)
	    for (int j = 0; j < matriz[0].length; j++)
		copia[i][j] = matriz[i][j];
	return copia;
    }

    /**
     * Abre um arquivo de imagem, salva imagem binária, salva todas as medidas
     * ou exibe erro quando não há imagem binária ainda.
     */
    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equalsIgnoreCase(this.gui.ITEM_ABRIR)) {
	    this.imagemOriginal = LidaComArquivos.abreImagem(this.gui);
	    if (this.imagemOriginal != null) {
		if (!this.imagemOriginal.equals(this.imagemExibida)) {
		    this.imagemExibida = this.imagemOriginal;
		    this.gui.exibeImagem(this.imagemExibida);
		    this.medidas.zeraMedidas();
		    this.objetoBinarizado = null;
		    this.referenciaDeEscalaBinarizada = null;
		    this.limiar = 0;
		    this.gui.alteraTickDoSlider(this.limiar);
		}
	    }
	}
	// DAVID
	if (e.getActionCommand()
		.equalsIgnoreCase(this.gui.ITEM_ABRIR_DIRETORIO)) {
	    boolean[][] binarizada;
	    ArrayList<File> arquivosDeImagem = LidaComArquivos
		    .abreImagensDoDiretorio(gui);
	    int numeroDeArquivos = arquivosDeImagem.size();

	    if (numeroDeArquivos > 0) {
		ArrayList<String> medidas = new ArrayList<String>();
		ArrayList<ArrayList<String>> todas = new ArrayList<ArrayList<String>>();
		BufferedImage localImg = null;
		double doubleW = 0.0;
		double doubleH = 0.0;
		int tolerancia = 1000; // maior dimensao permitida
		double fator = 0.25; // fator de escala. ex: se 1000 vai pra
		// 420.
		int intW = 0;
		int intH = 0;
		DecimalFormat df = new DecimalFormat("0");
		double porcento = 100.0;
		BufferedImage resizedImage = null;
		String nomeDiretorio = LidaComArquivos.nomeDiretorioDeEntrada;
		boolean salvaBinaria = true;
		String pergunta = "Deseja salvar imagens binárias no sub diretório binárias?";
		String titulo = "Salvar imagens binárias!";
		Object[] opcoes = { "sim", "não" };
		int escolha = gui.exibeEscolha(pergunta, titulo, opcoes);

		if (escolha == 0)
		    salvaBinaria = true;
		else if (escolha == 1)
		    salvaBinaria = false;
		// }
		todas.add(Medidas.getInstance().pegaCabecalho());
		for (int i = 0; i < numeroDeArquivos; i++) {
		    String inFileName = LidaComArquivos.nomesDosArquivoDeEntrada
			    .get(i);
		    try {
			localImg = ImageIO.read(arquivosDeImagem.get(i));
			if (localImg == null)
			    gui.exibeErro("O arquivo: " + inFileName
				    + ". não tem um formato de imagem válido.");
		    } catch (IOException ioe) {
			ioe.printStackTrace(System.err);
			gui.exibeErro("Erro ao ler o arquivo: " + inFileName);
		    }
		    resizedImage = localImg;
		    intW = localImg.getWidth();
		    intH = localImg.getHeight();
		    if (Math.max(intW, intH) > tolerancia) {
			doubleW = intW * fator;
			doubleH = intH * fator;
			intW = (int) doubleW;
			intH = (int) doubleH;
			resizedImage = PreProcessador.resize(localImg, intW,
				intH);
		    }
		    // resize acelera o processamento
		    binarizada = PreProcessador.limiarizaGmR(resizedImage);
		    // this.gui.exibeImagem(resizedImage);
		    if (numeroDeArquivos - 1 > 0)
			porcento = (i / ((double) numeroDeArquivos - 1)) * 100;
		    gui.alteraTituloDaJanela("Examinando ovo: " + inFileName
			    + ". " + df.format(porcento) + "% do total.");
		    examinaImagem(binarizada);
		    medidas = Medidas.getInstance()
			    .pegaTodasAsMedidasNumericas();
		    medidas.add(0, LidaComArquivos.nomesDosArquivoDeEntrada
			    .get(i));
		    todas.add(medidas);
		    if (salvaBinaria) {
			File diretorio = new File(nomeDiretorio
				+ File.separator + "binarias");
			if (!diretorio.exists())
			    diretorio.mkdir();
			File arquivo = new File(nomeDiretorio
				+ File.separator
				+ "binarias"
				+ File.separator
				+ inFileName.substring(0,
					inFileName.length() - 4) + ".pgm");
			if (arquivo != null)
			    LidaComArquivos.salvaImagemBinaria(arquivo,
				    inFileName, this.objetoBinarizado,
				    this.gui, this.limiar);
		    }
		}
		// if (numeroDeArquivos > 0) {
		File arquivo = LidaComArquivos.salvaArquivo(this.gui,
			nomeDiretorio
				+ nomeDiretorio.substring(nomeDiretorio
					.lastIndexOf(File.separator),
					nomeDiretorio.length()), ".csv");
		if (arquivo != null)
		    LidaComArquivos.salvaArraysCSV(todas, arquivo, gui,
			    "Todas as medidas", "as");
		gui.alteraTituloDaJanela("MAP2050");
		this.imagemOriginal = null;
		this.imagemExibida = null;
		this.objetoBinarizado = null;
		this.referenciaDeEscalaBinarizada = null;
		this.limiar = 0;
		// this.gui.alteraTickDoSlider(this.limiar);
	    }
	} else {
	    if (this.imagemOriginal != null) {
		// DAVID
		if (e.getActionCommand()
			.equalsIgnoreCase(this.gui.ITEM_GET_RED)) {
		    this.imagemExibida = PreProcessador.pegaBandaEmCinza(
			    this.imagemOriginal, 0);
		    this.gui.exibeImagem(imagemExibida);
		    this.medidas.zeraMedidas();
		    this.objetoBinarizado = null;
		    this.referenciaDeEscalaBinarizada = null;
		    this.limiar = 0;
		    // this.gui.alteraTickDoSlider(this.limiar);
		}
		// DAVID
		else if (e.getActionCommand().equalsIgnoreCase(
			this.gui.ITEM_LIMIARIZA_MAXIMUM_ENTROPY)) {
		    int limiar = PreProcessador.entropySplit(PreProcessador
			    .histogramaBanda(imagemOriginal, 0));

		    this.gui.alteraTickDoSlider(this.limiar);
		    boolean[][] binarizada;

		    binarizada = PreProcessador.limiariza(limiar,
			    this.imagemOriginal, 0);
		    examinaImagem(binarizada);
		}
		// DAVID
		else if (e.getActionCommand().equalsIgnoreCase(
			this.gui.ITEM_LIMIARIZA_GMR)
			|| e.getActionCommand().equalsIgnoreCase(
				this.gui.ITEM_PROCESSAMENTO_AUTOMATICO)) {
		    boolean[][] binarizada;
		    // BufferedImage localImg = this.imagemOriginal;
		    int tolerancia = 1000;
		    double fator = 0.25;
		    BufferedImage resizedImage = this.imagemOriginal;
		    // resizedImage = localImg;
		    int intW = imagemOriginal.getWidth();
		    int intH = imagemOriginal.getHeight();
		    if (Math.max(intW, intH) > tolerancia) {
			double doubleW = intW * fator;
			double doubleH = intH * fator;

			intW = (int) doubleW;
			intH = (int) doubleH;
			resizedImage = PreProcessador.resize(imagemOriginal,
				intW, intH);
		    }
		    this.limiar = 1;
		    binarizada = PreProcessador.limiarizaGmR(resizedImage);
		    examinaImagem(binarizada);
		    this.gui.alteraTickDoSlider(this.limiar);
		    this.gui.exibeImagem(this.imagemExibida);
		    this.gui.alteraTituloDaJanela("Examinando ovo: "
			    + LidaComArquivos.nomeArquivoDeEntrada);
		}
	    } else if (this.imagemExibida == null)
		this.gui.exibeAlerta("Abra uma imagem com \"Menu > Abrir...\"");
	    if (this.objetoBinarizado != null) {
		// if( objetoBinarizado == null )
		// gui.exibeAlerta( "Binarize a imagem ( usando o regulador de
		// limiar à esquerda
		// ) antes de extrair medidas." );
		if (e.getActionCommand().equalsIgnoreCase(
			this.gui.ITEM_SALVAR_IMAGEM_BINARIA)) {
		    File arquivo = LidaComArquivos.salvaArquivo(gui,
			    LidaComArquivos.nomeArquivoDeEntrada.substring(0,
				    LidaComArquivos.nomeArquivoDeEntrada
					    .length() - 4), ".pgm");
		    if (arquivo != null)
			LidaComArquivos.salvaImagemBinaria(arquivo,
				LidaComArquivos.nomeArquivoDeEntrada,
				this.objetoBinarizado, this.gui, this.limiar);
		} else if (e.getActionCommand().equalsIgnoreCase(
			this.gui.ITEM_SALVA_TUDO)) {
		    ArrayList<Medida> todas = Medidas.getInstance()
			    .pegaTodasAsMedidas();
		    ArrayList<String> medidas = new ArrayList<String>(todas
			    .size());

		    for (int i = 0; i < todas.size(); i++) {
			Medida atual = todas.get(i);
			atual.extraiMedida();
			medidas.add(atual.getClass().getSimpleName() + ": "
				+ atual.pegaValor());
		    }
		    File arquivo = LidaComArquivos.salvaArquivo(gui,
			    LidaComArquivos.nomeArquivoDeEntrada.substring(0,
				    LidaComArquivos.nomeArquivoDeEntrada
					    .length() - 4), ".txt");

		    if (arquivo != null)
			LidaComArquivos.salvaArray(medidas, arquivo, gui,
				"Todas as medidas", "as");
		} else if (e.getActionCommand().equalsIgnoreCase(
			gui.ITEM_SALVA_CSV)) {
		    ArrayList<String> medidas = Medidas.getInstance()
			    .pegaTodasAsMedidasNumericas();
		    ArrayList<ArrayList<String>> todas = new ArrayList<ArrayList<String>>();

		    medidas.add(0, LidaComArquivos.nomeArquivoDeEntrada);
		    File arquivo = LidaComArquivos.salvaArquivo(gui,
			    LidaComArquivos.nomeArquivoDeEntrada.substring(0,
				    LidaComArquivos.nomeArquivoDeEntrada
					    .length() - 4), ".csv");

		    todas.add(Medidas.getInstance().pegaCabecalho());
		    todas.add(medidas);
		    if (arquivo != null)
			LidaComArquivos.salvaArraysCSV(todas, arquivo, gui,
				"Todas as medidas", "as");
		}
	    } else if (this.imagemExibida != null) {
		gui
			.exibeAlerta("Binarize a imagem ( usando o regulador de limiar à esquerda \n"
				+ "ou o processamento automático ) antes de extrair medidas.");
	    }
	}
    }

    // DAVID

    void examinaImagem(boolean[][] binarizada) {

	// binarizada = PreProcessador.limiarizaGmR(imagemOriginal);
	/* debug */
	// imagemExibida = converteParaBufferedImage( binarizada );
	// gui.exibeImagem( imagemExibida );
	// gui.exibeMensagem( "Limiar usando máxima entropia: " +
	// limiar );
	int areaDaSegundaMaiorComponente = (int) PreProcessador
		.segundoMaiorRotulo(PreProcessador
			.rotulaComponentesComSuasAreas(binarizada));

	binarizada = PreProcessador.retiraComponentesComAreaMenorQue(
		areaDaSegundaMaiorComponente - 1, binarizada);
	this.referenciaDeEscalaBinarizada = PreProcessador
		.retiraPrimeiraComponente(copia(binarizada));

	/* debug */
	// imagemExibida = converteParaBufferedImage( binarizada );
	// gui.exibeImagem( imagemExibida );
	// gui.exibeMensagem( "binarizada =
	// PreProcessador.retiraComponentesComAreaMenorQue(
	// areaDaSegundaMaiorComponente
	// - 1, binarizada );" );
	/* debug */
	// imagemExibida =
	// converteParaBufferedImage(this.referenciaDeEscalaBinarizada);
	// gui.exibeImagem(imagemExibida);
	// gui.exibeMensagem("this.referenciaDeEscalaBinarizada =
	// PreProcessador.retiraPrimeiraComponente( copia(
	// binarizada ) );");
	this.referenciaDeEscalaBinarizada = PreProcessador
		.tampaBuracos(this.referenciaDeEscalaBinarizada);

	/* debug */
	// imagemExibida = converteParaBufferedImage(
	// this.referenciaDeEscalaBinarizada
	// );
	// gui.exibeImagem( imagemExibida );
	// gui.exibeMensagem( "this.referenciaDeEscalaBinarizada =
	// PreProcessador.tampaBuracos(this.referenciaDeEscalaBinarizada);"
	// );
	this.objetoBinarizado = PreProcessador
		.retiraComponentesComAreaMenorQue(
			areaDaSegundaMaiorComponente + 1, copia(binarizada));

	/* debug */
	// imagemExibida = converteParaBufferedImage(
	// this.objetoBinarizado );
	// gui.exibeImagem( imagemExibida );
	// gui.exibeMensagem( "this.objetoBinarizado =
	// PreProcessador.retiraComponentesComAreaMenorQue(
	// areaDaSegundaMaiorComponente
	// + 1, copia( binarizada ) );" );
	this.objetoBinarizado = PreProcessador
		.tampaBuracos(this.objetoBinarizado);

	/* debug */
	// imagemExibida = converteParaBufferedImage(
	// this.objetoBinarizado );
	// gui.exibeImagem( imagemExibida );
	// gui.exibeMensagem( "this.objetoBinarizado =
	// PreProcessador.tampaBuracos(
	// this.objetoBinarizado );" );
	imagemExibida = converteParaBufferedImage(this.objetoBinarizado);
	// this.gui.exibeImagem(imagemExibida);
	this.medidas.zeraMedidas();
	this.medidas.printMedidas();
    }

    /**
     * Função que toma uma ação após uma medida ter sido extraída Exibe a medida
     * na tela, salva num arquivo ou desenha algo na imagem
     * 
     * @param m
     *                a medida que foi extraída
     */
    public void tomaUmaAtitude(Medida m) {
	if (this.objetoBinarizado == null)
	    this.gui
		    .exibeErro("Não há imagem binária para se extrair medida alguma");
	else {
	    if (temQueExibirMsg(m))
		this.gui.exibeMensagem(m.toString());
	    if (temQueSalvarAlgo(m)) {
		File arquivo = LidaComArquivos.abreArquivo(gui);

		if (arquivo != null) {
		    if (m instanceof Curvatura)
			LidaComArquivos.salvaVetor(((Curvatura) m)
				.pegaCurvatura(), arquivo, gui, "Curvatura",
				"a");
		    else if (m instanceof ContornoEmCm)
			LidaComArquivos.salvaArray(((ContornoEmCm) m)
				.pegaContorno(), arquivo, gui,
				"Contorno em cm", "o");
		    else if (m instanceof PixelsDoContorno)
			LidaComArquivos.salvaArray(((PixelsDoContorno) m)
				.pegaContorno(), arquivo, gui,
				"Contorno em pixels", "o");
		    else
			System.err.println("Ainda não sei salvar um( a ) "
				+ m.getClass().getSimpleName());
		}
	    }
	    if (temQueDesenhar(m)) {
		if (m instanceof MaiorDiametro) {
		    marcaPonto(((MaiorDiametro) m).pegaUmExtremo(), new Color(
			    200, 100, 0).getRGB());
		    marcaPonto(((MaiorDiametro) m).pegaOutroExtremo(),
			    new Color(200, 100, 0).getRGB());
		} else if (m instanceof MedidaDeAssimetria) {
		    MedidaDeAssimetria assimetria = ((MedidaDeAssimetria) m);

		    marcaReta(medidas.pegaCentroide().pegaCentroide(),
			    assimetria.pegaVetorPrimario(), new Color(100, 100,
				    100).getRGB());
		    marcaReta(medidas.pegaCentroide().pegaCentroide(),
			    assimetria.pegaVetorSecundario(), new Color(0, 170,
				    95).getRGB());
		} else if (m instanceof Volume) {
		    Volume volume = ((Volume) m);
		    PontoDouble centro = medidas.pegaCentroide()
			    .pegaCentroide();
		    PontoNatural centroSuperior = volume
			    .pegaCentroideSuperior().toPontoNatural();
		    PontoNatural centroInferior = volume
			    .pegaCentroideInferior().toPontoNatural();

		    marcaReta(centro, volume.pegaVetorPrimario(), new Color(
			    255, 0, 0).getRGB());
		    marcaPonto(centroSuperior, new Color(0, 0, 255).getRGB());
		    marcaPonto(centroInferior, new Color(0, 255, 0).getRGB());
		    desenhaSegmento(new PontoNatural((int) centro.j,
			    (int) centro.i), new PontoNatural(centroSuperior.j,
			    centroSuperior.i), new Color(0, 0, 255));
		    desenhaSegmento(new PontoNatural((int) centro.j,
			    (int) centro.i), new PontoNatural(centroInferior.j,
			    centroInferior.i), new Color(0, 255, 0));
		}
		gui.exibeImagem(imagemExibida);
	    }
	}
    }

    private boolean temQueExibirMsg(Medida m) {
	return m instanceof Area || m instanceof DistanciaMediaAteOCentro
		|| m instanceof EnergiaDeDobramento
		|| m instanceof MaiorDiametro
		|| m instanceof MedidaDeAssimetria || m instanceof Perimetro;
    }

    private boolean temQueSalvarAlgo(Medida m) {
	return m instanceof ContornoEmCm || m instanceof PixelsDoContorno
		|| m instanceof Curvatura;
    }

    private boolean temQueDesenhar(Medida m) {
	return m instanceof MedidaDeAssimetria || m instanceof MaiorDiametro
		|| m instanceof Volume;
    }

    private void marcaPonto(PontoNatural p, int corRGB) {
	int n = Math.min(p.i + 5, this.imagemExibida.getWidth());
	int m = Math.min(p.j + 5, this.imagemExibida.getHeight());

	for (int i = p.i - 5; i < n; i++)
	    for (int j = p.j - 5; j < m; j++) {
		double dist = p.normaEuclidiana(new PontoNatural(i, j));

		if (2 < dist && dist < 5)
		    this.imagemExibida.setRGB(j, i, corRGB);
	    }
    }

    private void desenhaPontinho(PontoNatural p, int corRGB) {
	this.imagemExibida.setRGB(p.j, p.i, corRGB);
    }

    private void marcaReta(PontoDouble partida, double[] vetorDiretor,
	    int corRGB) {
	double[] vetorOposto = vetorOposto(vetorDiretor);

	desenhaSemiReta(vetorDiretor, partida, corRGB);
	desenhaSemiReta(vetorOposto, partida, corRGB);
    }

    private double[] vetorOposto(double[] v) {
	double[] oposto = new double[v.length];

	for (int i = 0; i < v.length; i++)
	    oposto[i] = -v[i];
	return oposto;
    }

    private void desenhaSemiReta(double[] vetorDiretor, PontoDouble partida,
	    int corRGB) {
	int i = (int) partida.i;
	int j = (int) partida.j;
	double passo = 0.0005;
	double k = passo;
	while (estaDentro(i, j)) {
	    desenhaPontinho(new PontoNatural(i, j), corRGB);
	    i = (int) ((vetorDiretor[0] * k) + partida.i);
	    j = (int) ((vetorDiretor[1] * k) + partida.j);
	    k += passo;
	}
    }

    /**
     * Desenha segmento de reta na imagem
     * 
     * @author David Macedo da Conceição
     * @param partida
     *                inicio do segmento
     * @param chegada
     *                fim do segmento
     */
    private void desenhaSegmento(PontoNatural partida, PontoNatural chegada,
	    Color cor) {
	Graphics g = this.imagemExibida.getGraphics();
	Color corAntes = g.getColor();
	g.setColor(cor);
	g.drawLine(partida.i, partida.j, chegada.i, chegada.j);
	g.setColor(corAntes);
    }

    private boolean estaDentro(int i, int j) {
	try {
	    this.imagemExibida.getRGB(j, i);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return false;
	}
	return true;
	// return i >= 0 && i >= 0 && i < matriz.getRaster().getHeight() &&
	// j < matriz.getRaster().getWidth();
    }

    // DAVID : mudei pra public
    public BufferedImage converteParaBufferedImage(boolean[][] binaria) {
	BufferedImage imagem = new BufferedImage(binaria[0].length,
		binaria.length, BufferedImage.TYPE_INT_RGB);

	for (int i = 0; i < binaria.length; i++) {
	    for (int j = 0; j < binaria[0].length; j++) {
		if (binaria[i][j])
		    imagem.setRGB(j, i, new Color(0, 0, 0).getRGB());
		else
		    imagem.setRGB(j, i, new Color(255, 255, 255).getRGB());
	    }
	}
	return imagem;
    }

    public static void main(String[] args) {
	Controlador controlador = Controlador.getInstance();

	controlador.gui = new GUI();
	controlador.gui.constroiInterface();
	controlador.gui.adicionaOuvidoresAosItensDoMenu(controlador.medidas);
	controlador.gui.adicionaOuvidorAoSlider(controlador);
    }
}
