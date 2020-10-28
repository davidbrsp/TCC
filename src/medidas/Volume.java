package medidas;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;
import util.OperacoesComMatriz;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe que contém métodos para calcular volume de sólidos de rotação.
 * 
 * @author David Macedo da Conceição
 */
public class Volume extends Medida {
    private double volume;
    private double[] vetorPrimario = { 0, 0 };
    private double[] vetorSecundario = { 0, 0 };
    private PontoDouble centroideSuperior;
    private PontoDouble centroideInferior;

    public Volume() {
	super();
    }

    public double pegaVolume() {
	return this.volume;
    }

    @Override
    // aqui vai o codigo do pappus, aqui esta baseado no Maior Diametro
    protected void extraiMedidaDeFato() {
	// Medidas.getInstance( ).pegaCentroide( ).extraiMedida( );
	// PontoDouble centroide = Medidas.getInstance( ).pegaCentroide(
	// ).pegaCentroide( );

	Medidas.getInstance().pegaEscala().extraiMedida();
	double escala = Medidas.getInstance().pegaEscala().pegaEscala();

	// Medidas.getInstance( ).pegaContorno( ).extraiMedida( );
	// ArrayList<PontoNatural> contorno = Medidas.getInstance(
	// ).pegaContorno( ).pegaContorno( );

	// ArrayList<PontoDouble> contornoDouble = new ArrayList<PontoDouble>(
	// );

	// System.out.println( "\n" + contorno.size( ) );
	// transforma pra double
	// for( int i = 0; i < contorno.size( ); i++ )
	// {
	// System.out.println( contorno.get( i ).toString( ) );
	// contornoDouble.add( new PontoDouble( ( double ) contorno.get( i ).i,
	// ( double ) contorno.get( i ).j ) );
	// }

	this.volume = pappus(escala);
	// umExtremo = new PontoNatural( 0,0 );
	// outroExtremo = new PontoNatural( 0, 0 );
	// this.maiorDiametro = ExtratorDeMedidas.maiorDiametro( contorno,
	// umExtremo, outroExtremo );
	// this.maiorDiametro *= escala / 10.0; // divisão por dez para
	// transformar de mm para cm
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "O Volume do Objeto e: " + df.format(this.volume) + " cm³";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.volume = 0;
	// this.umExtremo = new PontoNatural(0,0);
	// this.outroExtremo = new PontoNatural(0,0);
    }

    @Override
    public String pegaValor() {
	return this.pegaVolume() + " cm³";
    }

    // codigo original do DAVID
    /**
     * @author David Macedo da Conceição
     * @params
     * @return
     */
    public double pappus(double escala) {
	double volumeMedio;
	double escalaCubica = Math.pow(escala, 3) / 1000;
	double[] volumes = volumeAcimaAbaixo();
	volumeMedio = ((volumes[0] + volumes[1]) / 2);

	return volumeMedio * escalaCubica;
    }

    /**
     * @author David Macedo da Conceição
     */
    public double[] pegaVetorPrimario() {
	return this.vetorPrimario;
    }

    /**
     * @author David Macedo da Conceição
     */
    public double[] pegaVetorSecundario() {
	return this.vetorSecundario;
    }

    /**
     * @author David Macedo da Conceição
     */
    public PontoDouble pegaCentroideSuperior() {
	return this.centroideSuperior;
    }

    /**
     * @author David Macedo da Conceição
     */
    public PontoDouble pegaCentroideInferior() {
	return this.centroideInferior;
    }

    /**
     * Calcula estimativas do volume das imagens binarizadas acima e abaixo da
     * reta de simetria da imagem binarizada usando o teorema de Pappus.
     * 
     * @author David Macedo da Conceição
     * 
     * @return vetor com estimativas dos dois volumes.
     */
    public double[] volumeAcimaAbaixo() {
	double[] raios = { 0, 0 };
	double[] areas = { 0, 0 };
	double[] volumes = { 0, 0 };
	boolean[][] imagemBinaria = Controlador.getInstance().objetoBinarizado;
	boolean[][] imagemSuperior = new boolean[imagemBinaria.length][imagemBinaria[0].length];
	boolean[][] imagemInferior = new boolean[imagemBinaria.length][imagemBinaria[0].length];

	Medidas.getInstance().pegaCentroide().extraiMedida();
	PontoDouble centroide = Medidas.getInstance().pegaCentroide()
		.pegaCentroide();
	ExtratorDeMedidas.extraiVetoresProprios(imagemBinaria,
		this.vetorPrimario, this.vetorSecundario);

	// extraiVetoresProprios( imagemBinaria, vetorPrimario, vetorSecundario
	// );
	for (int i = 0; i < imagemBinaria.length; i++)
	    for (int j = 0; j < imagemBinaria[0].length; j++) {
		if (imagemBinaria[i][j]) {
		    int posicao = ExtratorDeMedidas.posicaoRelativa(
			    new PontoNatural(i, j), this.vetorPrimario,
			    centroide);

		    if (posicao == 1) {
			// medidaDeAssimetria++;
			imagemSuperior[i][j] = false;
			imagemInferior[i][j] = true;
			areas[0]++;
		    } else if (posicao == -1) {
			imagemSuperior[i][j] = true;
			imagemInferior[i][j] = false;
			areas[1]++;
			// medidaDeAssimetria--;
		    }
		} else {
		    imagemSuperior[i][j] = false;
		    imagemInferior[i][j] = false;
		}
	    }
	BufferedImage imagemExibida;

	imagemExibida = Controlador.getInstance().converteParaBufferedImage(
		imagemSuperior);
	// Controlador.getInstance().gui.exibeImagem(imagemExibida);
	// Controlador.getInstance().gui.exibeMensagem("imagemSuperior");

	// Medidas.getInstance( ).pegaCentroide( ).extraiMedida( );
	this.centroideSuperior = ExtratorDeMedidas.centroide(imagemSuperior);
	// System.out.println("centroide Superior: "
	// + centroideSuperior.toString());
	raios[0] = this.centroideSuperior.normaEuclidiana(centroide);
	volumes[0] = 2 * Math.PI * areas[0] * raios[0];
	/* debug */
	// System.out.println("Raio Superior: " + raios[0]);
	// System.out.println("Area Superior:" + areas[0]);
	// System.out.println("Volume Superior: " + volumes[0]);
	// System.out.println(ExtratorDeMedidas.extraiContorno(imagemSuperior).toString());
	ArrayList<PontoNatural> contornonatural = ExtratorDeMedidas
		.extraiContorno(imagemBinaria);
	ArrayList<PontoDouble> contornodouble = new ArrayList<PontoDouble>();

	for (int i = 0; i < contornonatural.size(); i++) {
	    contornodouble.add(new PontoDouble(contornonatural.get(i).i,
		    contornonatural.get(i).j));
	}
	// codigo de ouro
	// double[] reta = equacaoDaReta(new PontoDouble(centroide.i -
	// vetorPrimario[1], vetorPrimario[0] + centroide.j), centroide);
	// imprimeReta(reta, false);
	// ArrayList<PontoDouble> PerfilSuperior = contornoToPerfilSuperior(
	// contornodouble, reta);
	// ArrayList<PontoDouble> RH = perfilToRaios(PerfilSuperior, reta);

	/* debug */
	// System.out.println("vetor primario: " + vetorPrimario[0] + " ; "
	// + vetorPrimario[1]);
	// System.out.print( "\nr = [" );
	// for ( int i = 0; i <PerfilSuperior.size( ); i++ )
	// {
	// System.out.print( distanciaPontoAReta( PerfilSuperior.get( i ), reta
	// ) + ";" );
	// }
	// System.out.print( "]\n" );
	// double distancia = PerfilSuperior.get( 0 ).normaEuclidiana(
	// PerfilSuperior.get( PerfilSuperior.size( ) - 1 ) );
	// System.out.println( "Distancia: " + distancia );
	// System.out.println( "Numero de porntos: " + PerfilSuperior.size( ) );
	// System.out.println( "Razao: " + PerfilSuperior.size( ) / distancia );
	/* debug */
	// perfil superior em pixels amigavel pro matlab
	// System.out.print("\nx= [");
	// for (int i = 0; i < PerfilSuperior.size(); i++)
	// System.out.print(PerfilSuperior.get(i).i + ";");
	// System.out.print("]\n");
	// System.out.print("\ny= [");
	// for (int i = 0; i < PerfilSuperior.size(); i++)
	// System.out.print(PerfilSuperior.get(i).j + ";");
	// System.out.print("]\n");
	// raios e alturas amigaveis pro matlab
	// System.out.print("h = [");
	// for (int i = 0; i < RH.size(); i++)
	// System.out.print(RH.get(i).i + ";");
	// System.out.print("]");
	// System.out.print("\n r = [");
	// for (int i = 0; i < RH.size(); i++)
	// System.out.print(RH.get(i).j + ";");
	// System.out.print("]\n");
	imagemExibida = Controlador.getInstance().converteParaBufferedImage(
		imagemInferior);
	// Controlador.getInstance().gui.exibeImagem(imagemExibida);
	// Controlador.getInstance().gui.exibeMensagem("imagemInferior");

	this.centroideInferior = ExtratorDeMedidas.centroide(imagemInferior);
	// System.out.println("centroide Inferior: "
	// + centroideInferior.toString());
	raios[1] = this.centroideInferior.normaEuclidiana(centroide);
	volumes[1] = 2 * Math.PI * areas[1] * raios[1];
	// System.out.println("Raio Inferior: " + raios[1]);
	// System.out.println("Area Inferior:" + areas[1]);
	// System.out.println("Volume Inferior: " + volumes[1]);

	imagemExibida = Controlador.getInstance().converteParaBufferedImage(
		imagemBinaria);
	imagemExibida.setRGB((int) centroide.j, (int) centroide.i, new Color(
		200, 100, 0).getRGB());
	imagemExibida
		.setRGB((int) this.centroideSuperior.j,
			(int) this.centroideSuperior.i, new Color(200, 100, 0)
				.getRGB());
	imagemExibida
		.setRGB((int) this.centroideInferior.j,
			(int) this.centroideInferior.i, new Color(200, 100, 0)
				.getRGB());
	// Controlador.getInstance().gui.exibeImagem(imagemExibida);
	// Controlador.getInstance().gui.exibeMensagem("imagemBinaria");

	// Medidas.getInstance( ).pegaCentroide( ).extraiMedida( );
	// centroide = Medidas.getInstance( ).pegaCentroide( ).pegaCentroide( );
	// centroide = ExtratorDeMedidas.centroide( imagemBinaria );
	// System.out.println("centroide:" + centroide.toString());

	return volumes;
	// return medidaDeAssimetria;
    }

    /**
     * Função que calcula a área ( em pixels ) de um objeto numa imagem binária
     * 
     * @param imagem
     *                Imagem binária.
     * @return Número de posições da matriz que valem true.
     */
    public static long areaAcima(boolean imagem[][],
	    ArrayList<PontoDouble> linha) {

	long area = 0;
	int L;

	for (int i = 0; i < linha.size(); i++) {
	    System.out.println(linha.get(i).toString());
	}

	for (int i = imagem.length - 1; i >= 0; i--) {
	    L = (int) findX((double) i, linha);
	    System.out.println(L);
	    for (int j = 0; j < imagem[0].length; j++)
		if (imagem[i][j] == true && j < L) {
		    System.out.println("help");
		    area++;
		}
	}
	return area;
    }

    // dado y encontra o x correspondente na reta digital
    public static double findX(double Y, ArrayList<PontoDouble> linha) {
	double x = -1;

	for (int i = 0; i < linha.size(); i++) {
	    if (Y == linha.get(i).j) {
		x = linha.get(i).i;
		continue;
	    }
	}
	return x;
    }

    /**
     * Método para calcular volume de sólidos de rotação pelo método de
     * cavalieri.
     * 
     * @params perfil Pontos que descrevem o perfil da curva.
     * @return Soma das áreas.
     */
    public static double volumeCavalieri(ArrayList<PontoDouble> perfil,
	    double altura) {
	double somaDasAreas = 0;

	for (int k = 0; k < perfil.size(); k++)
	    somaDasAreas += Math.PI * Math.pow(perfil.get(k).j, 2);
	return somaDasAreas * (altura / perfil.size());
    }

    /**
     * Ainda Nao implementada.
     * 
     * @params
     * @params
     * @return
     */
    public static double volumeSomaInferior(ArrayList<PontoDouble> perfil) {
	double somaDasAreas = 0;

	return somaDasAreas;
    }

    /**
     * Ainda Nao implementada.
     * 
     * @params
     * @params
     * @return
     */
    public static double volumeSomaSuperior(ArrayList<PontoDouble> perfil) {
	double somaDasAreas = 0;

	return somaDasAreas;
    }

    /**
     * Calcula a distancia de um ponto a uma reta.
     * 
     * @params ponto Ponto a ser calculada a distância.
     * @params reta Coeficientes [ a, b, c ] da equacao da reta no formato a * x +
     *         b * y + c = 0.
     * @return Distância entre o ponto e reta.
     */
    public static double distanciaPontoAReta(PontoDouble ponto, double[] reta) {
	double D = 0;
	double funcao = Math.sqrt(Math.pow(reta[0], 2) + Math.pow(reta[1], 2));

	if (funcao != 0) // Não é função nula.
	    D = Math.abs(reta[0] * ponto.i + reta[1] * ponto.j + reta[2])
		    / funcao;
	else
	    // coordenada y do vetor
	    D = ponto.j;

	return D;
    }

    /**
     * Equação da reta que passa por dois pontos, p1 tem que ser diferente de p2
     * para haver uma solução.
     * 
     * @params p1 Ponto inicial.
     * @params p2 Ponto final.
     * @return Coeficientes [ a, b, c ] da equacao da reta no formato a * x + b *
     *         y + c = 0.
     */
    public static double[] equacaoDaReta(PontoDouble p1, PontoDouble p2) {
	double[] reta = new double[3];

	if (p1.igual(p2))
	    reta = null;
	else {
	    reta[0] = p1.j - p2.j;
	    reta[1] = -1 * (p1.i - p2.i);
	    reta[2] = p1.i * p2.j - p1.j * p2.i;
	}
	return reta;
    }

    /**
     * Encontra Maior segmento do contorno.
     * 
     * @params contorno Lista contendo os pontos do contorno.
     * @return Extremos do maior segmento inteiramente contido no contorno.
     */
    public static ArrayList<PontoDouble> maiorSegmento(
	    ArrayList<PontoDouble> contorno) {
	int n = contorno.size();
	double dist = 0, distMax = 0;
	ArrayList<PontoDouble> extremos = new ArrayList<PontoDouble>();
	PontoDouble umExtremo, outroExtremo;

	umExtremo = new PontoDouble(0, 0);
	outroExtremo = new PontoDouble(0, 0);
	for (int i = 0; i < n; i++) {
	    PontoDouble umPonto = contorno.get(i);
	    for (int j = i; j < n; j++) {
		PontoDouble outroPonto = contorno.get(j);

		dist = umPonto.normaEuclidiana(outroPonto);
		if (dist > distMax) {
		    distMax = dist;
		    umExtremo.copiaValores(umPonto);
		    outroExtremo.copiaValores(outroPonto);
		}
	    }
	}
	extremos.add(umExtremo);
	extremos.add(outroExtremo);
	return extremos;
    }

    /**
     * Le um conjuno de pontos, dois valores por linha de um arquivo de texto.
     * 
     * @params nomeDoArquivo Arquivo que será lido os pontos.
     * @return Lista de pontos que descreve o contorno de um objeto.
     */
    public static ArrayList<PontoDouble> leDoArquivo(String nomeDoArquivo) {
	ArrayList<PontoDouble> contorno = new ArrayList<PontoDouble>();
	try {
	    Scanner input = new Scanner(new File(nomeDoArquivo));

	    while (input.hasNext())
		contorno.add(new PontoDouble(input.nextDouble(), input
			.nextDouble()));
	    return contorno;
	} catch (IOException ioexception) {
	    String textoDoErro = "Não consegui ler do arquivo \""
		    + nomeDoArquivo + "\".";
	    System.err.println(textoDoErro);
	    ioexception.printStackTrace(System.err);
	    return null;
	}
    }

    /**
     * 
     * @params
     * @params
     * @return
     */
    // public static ArrayList<PontoDouble> contornoToPerfilSuperior(
    // ArrayList<PontoDouble> contorno , ArrayList<PontoDouble> extremos )
    public static ArrayList<PontoDouble> contornoToPerfilSuperior(
	    ArrayList<PontoDouble> contorno, double[] reta) {
	ArrayList<PontoDouble> perfil = new ArrayList<PontoDouble>();
	// double[ ] reta;

	// reta = equacaoDaReta( extremos.get( 0 ), extremos.get( 1 ) );
	for (int i = 0; i < contorno.size(); i++)
	    if (contorno.get(i).j > avaliaReta(reta, contorno.get(i).i))
		perfil.add(contorno.get(i));
	return perfil;
    }

    /**
     * 
     * @params
     * @params
     * @return
     */
    // public static ArrayList<PontoDouble> contornoToPerfilInferior(
    // ArrayList<PontoDouble> contorno , ArrayList<PontoDouble> extremos )
    public static ArrayList<PontoDouble> contornoToPerfilInferior(
	    ArrayList<PontoDouble> contorno, double[] reta) {
	ArrayList<PontoDouble> perfil = new ArrayList<PontoDouble>();
	// double[ ] reta;

	// reta = equacaoDaReta( extremos.get( 0 ), extremos.get( 1 ) );
	for (int i = 0; i < contorno.size(); i++)
	    if (contorno.get(i).j <= avaliaReta(reta, contorno.get(i).i))
		perfil.add(contorno.get(i));
	return perfil;
    }

    /**
     * Avalia reta no ponto.
     * 
     * @params reta Coeficientes [ a, b, c ] da equacao da reta no formato a * x +
     *         b * y + c = 0.
     * @params pontoX Ponto x onde se deseja avaliar a reta.
     * @return Valor y da reta na coordenada x.
     */
    public static double avaliaReta(double[] reta, double pontoX) {
	return -1 * ((reta[0] / reta[1]) * pontoX + (reta[2] / reta[1]));
    }

    /**
     * Imprime equação da reta.
     * 
     * @params reta Coeficientes [ a, b, c ] da equacao da reta no formato a * x +
     *         b * y + c = 0.
     * @params matricial Se true, equacao da reta no formato a * x + b * y + c =
     *         0, senão imprime no formato ponto - inclinação.
     */
    public static void imprimeReta(double[] reta, boolean matricial) {
	if (matricial)
	    System.out.println("( " + reta[0] + " ) * x + " + "( " + reta[1]
		    + " ) * y + " + "( " + reta[2] + " ) = 0");
	else
	    System.out.println("y = ( " + -1 * (reta[0] / reta[1])
		    + " ) * x + ( " + -1 * (reta[2] / reta[1]) + " )");
    }

    /**
     * Tranforma um perfil em raios, para serem usados no cálculo do volume.
     * 
     * @params perfil
     * @params reta
     * @return
     */
    // public static ArrayList<PontoDouble> perfilToRaios(
    // ArrayList<PontoDouble> perfil, ArrayList<PontoDouble> extremos )
    public static ArrayList<PontoDouble> perfilToRaios(
	    ArrayList<PontoDouble> perfil, double[] reta) {
	ArrayList<PontoDouble> raios = new ArrayList<PontoDouble>();
	double passo = 1; // gambiarra

	for (int i = 0; i < perfil.size(); i++) {
	    if (i > 0)
		if (perfil.get(i - 1).i == perfil.get(i).i)
		    continue;
	    PontoDouble ponto = new PontoDouble(i * passo, distanciaPontoAReta(
		    perfil.get(i), reta));
	    raios.add(ponto);
	}
	return raios;
    }

    /**
     * Encontra o maior x e o maior y de um contorno.
     * 
     * @params lista Lista contendo os pontos do contorno.
     * @return Maior x e o maior y de um contorno.
     */
    public static PontoDouble findMax(ArrayList<PontoDouble> lista) {
	double maxX = 0, maxY = 0;
	PontoDouble extraido = new PontoDouble(0, 0);

	for (int i = 0; i < lista.size(); i++) {
	    extraido.i = lista.get(i).i;
	    extraido.j = lista.get(i).j;
	    if (extraido.i > maxX)
		maxX = extraido.i;
	    if (extraido.j > maxY)
		maxY = extraido.j;
	}
	// System.out.println( maxX + " " + maxY );
	return new PontoDouble(maxX, maxY);
    }

    /**
     * Encontra o menor x e o menor y de um contorno.
     * 
     * @params lista Lista contendo os pontos do contorno.
     * @return Menor x e o menor y de um contorno.
     */
    public static PontoDouble findMin(ArrayList<PontoDouble> lista) {
	PontoDouble extraido = new PontoDouble(0, 0);
	PontoDouble minXminY = findMax(lista);

	for (int i = 0; i < lista.size(); i++) {
	    extraido.i = lista.get(i).i;
	    extraido.j = lista.get(i).j;
	    if (extraido.i < minXminY.i)
		minXminY.i = extraido.i;
	    if (extraido.j < minXminY.j)
		minXminY.j = extraido.j;
	}
	System.out.println(minXminY.toString());
	return minXminY;
    }

    /**
     * Encontra o centroide do contorno.
     * 
     * @params contorno Lista contendo os pontos do contorno.
     * @return Ponto que representa o centroide de contorno.
     */
    public static PontoDouble centroideDoContorno(
	    ArrayList<PontoDouble> contorno) {
	int n = contorno.size();
	double xDoCentro = 0, yDoCentro = 0;

	for (int i = 0; i < n; i++) {
	    xDoCentro += contorno.get(i).i;
	    yDoCentro += contorno.get(i).j;
	}
	xDoCentro /= n;
	yDoCentro /= n;

	// System.out.println( "Centroide: " + xDoCentro + " " + yDoCentro );
	return new PontoDouble(xDoCentro, yDoCentro);
    }

    /**
     * Encontra os autovetores associados ao contorno.
     * 
     * @params contorno Lista contendo os pontos do contorno.
     * @return Os autovetores associados ao contorno.
     */
    public static ArrayList<PontoDouble> eigenAxes(
	    ArrayList<PontoDouble> contorno) {
	int n = contorno.size();
	OperacoesComMatriz op = new OperacoesComMatriz();
	double[] u = new double[n];
	double[] v = new double[n];
	double cov[][] = new double[2][2];
	double[] valoresProprios = new double[2];
	double[] vetorProprio1 = new double[2];
	double[] vetorProprio2 = new double[2];
	ArrayList<PontoDouble> eixos = new ArrayList<PontoDouble>();

	for (int i = 0; i < n; i++) {
	    u[i] = contorno.get(i).i;
	    v[i] = contorno.get(i).j;
	}
	cov[0][0] = variancia(u);
	cov[0][1] = covariancia(u, v);
	cov[1][0] = cov[0][1];
	cov[1][1] = variancia(v);
	valoresProprios = op.valoresProprios(cov);
	vetorProprio1 = op.vetorProprioAssociado(cov, valoresProprios[0]);
	vetorProprio2 = op.vetorProprioAssociado(cov, valoresProprios[1]);
	eixos.add(new PontoDouble(vetorProprio1[0], vetorProprio1[1]));
	eixos.add(new PontoDouble(vetorProprio2[0], vetorProprio2[1]));

	// System.out.println( cov[ 0 ][ 0 ] + " " + cov[ 0 ][ 1 ] + "\n" + cov[
	// 1 ][ 0 ] + " " + cov[ 1 ][ 1 ] );
	// System.out.println( "valorProprio1: " + valoresProprios[ 0 ] );
	// System.out.println( "valorProprio2: " + valoresProprios[ 1 ] );
	// System.out.println( "vetorProprio1: " + eixos.get( 0 ) );
	// System.out.println( "vetorProprio2: " + eixos.get( 1 ) );

	return eixos;
    }

    public static double variancia(double[] x) {
	double esperancaDeXquadrado = momento(x, 2);
	double esperancaAoQuadradoDeX = Math.pow(momento(x, 1), 2);
	return esperancaDeXquadrado - esperancaAoQuadradoDeX;
    }

    public static double momento(double[] x, int m) {
	int i;
	double momento = 0.0;

	for (i = 0; i < x.length; i++)
	    momento += Math.pow(x[i], m);
	return momento / i;
    }

    public static double covariancia(double[] x, double[] y) {
	double esperancaDeXY = momento(produtoInterno(x, y), 1);
	double esperancaDeXesperancaDeY = momento(x, 1) * momento(y, 1);

	return esperancaDeXY - esperancaDeXesperancaDeY;
    }

    /**
     * Faz a operação de produto interno entre dois vetores
     * 
     * @params x Vetor.
     * @params y Vetor.
     * @return Retorna o vetor que representa o produto interno entre x e y.
     */
    public static double[] produtoInterno(double[] x, double[] y) {
	if (x.length != y.length)
	    System.out
		    .println("Cuidado: fazendo produto interno de vetores de tamanhos diferentes!");

	double[] result = new double[x.length];

	for (int i = 0; i < result.length; i++)
	    result[i] = x[i] * y[i];
	return result;
    }

    /**
     * 
     * @params contorno
     * @params reta
     * @return ArrayList<PontoDouble>
     */
    public static ArrayList<PontoDouble> linhaDivisoria(
	    ArrayList<PontoDouble> contorno, double[] reta) {
	ArrayList<PontoDouble> linha = new ArrayList<PontoDouble>();
	// double[ ] reta;

	ArrayList<PontoDouble> extremos = maiorSegmento(contorno);
	// double minX = Math.min(extremos.get(0).i, extremos.get(1).i);
	double maxX = Math.max(extremos.get(0).i, extremos.get(1).i);
	// reta = equacaoDaReta( extremos.get( 0 ), extremos.get( 1 ) );
	maxX = Controlador.getInstance().objetoBinarizado[0].length;
	for (int i = 0; i < maxX; i++) {
	    linha
		    .add(new PontoDouble(i, Math
			    .floor(avaliaReta(reta, i) + 0.5)));
	}
	for (int i = 0; i < linha.size(); i++) {
	    System.out.println(linha.get(i).i + " " + linha.get(i).j);
	}
	// if( contorno.get( i ).j <= avaliaReta( reta , contorno.get( i ).i ) )
	// linha.add( contorno.get( i ) );
	return linha;
    }

    /**
     * 
     * @params
     * @params
     * @return
     */
    public static void main(String argv[]) {
	ArrayList<PontoDouble> cilindro = new ArrayList<PontoDouble>();
	ArrayList<PontoDouble> cone = new ArrayList<PontoDouble>();
	// int maxAmostras = 100000;
	int amostras = 1000000;
	double altura = 10, raioCilindro = 27;
	double x = 0;
	// int y = 0;
	double passo = altura / amostras;
	// String arqLeitura =
	// "C:\\WORK\\java\\src_TFS_Heitor\\example1.contorno.px.txt";

	for (int k = 0; k < amostras; k++) {
	    cilindro.add(new PontoDouble(x, raioCilindro));
	    cone.add(new PontoDouble(x, x * x));
	    x += passo;
	}
	// System.out.println( cilindro );
	// System.out.println( cone );
	// System.out.println( "Cilindro: " + volumeCavalieri( cilindro, altura
	// ) );
	// System.out.println( "Cone: " + volumeCavalieri( cone, altura ) );

	ArrayList<PontoDouble> forma;
	ArrayList<PontoDouble> extremos = new ArrayList<PontoDouble>();
	ArrayList<PontoDouble> perfil;
	ArrayList<PontoDouble> eixosPrincipais;
	PontoDouble centroide;

	forma = cilindro;
	// forma = leDoArquivo(argv[0]);

	// extremos = maiorSegmento( forma );
	extremos.add(new PontoDouble(119, 255));
	extremos.add(new PontoDouble(271, 264));
	eixosPrincipais = eigenAxes(forma);
	centroide = centroideDoContorno(forma);
	eixosPrincipais.set(0, new PontoDouble(eixosPrincipais.get(0).i
		+ centroide.i, eixosPrincipais.get(0).j + centroide.j));
	eixosPrincipais.set(1, new PontoDouble(eixosPrincipais.get(1).i
		+ centroide.i, eixosPrincipais.get(1).j + centroide.j));

	// pappus
	// pappus(forma);
	// fim do pappus

	// distanciaPontoAReta( ,equacaoDaReta( extremos.get(0), extremos.get(0)
	// ) );
	perfil = contornoToPerfilSuperior(forma, equacaoDaReta(eixosPrincipais
		.get(0), centroide));
	altura = extremos.get(0).normaEuclidiana(extremos.get(1));

	// altura = 2;
	// System.out.println( "Transladado: " + new PontoDouble(
	// eixosPrincipais.get( 0 ).i, eixosPrincipais.get( 0 ).j ) + " " + new
	// PontoDouble( eixosPrincipais.get( 1 ).i, eixosPrincipais.get( 1 ).j )
	// );

	imprimeReta(equacaoDaReta(eixosPrincipais.get(0), centroide), false);
	imprimeReta(equacaoDaReta(eixosPrincipais.get(1), centroide), false);
	System.out.println("Volume Cavalieri: "
		+ volumeCavalieri(perfilToRaios(perfil, equacaoDaReta(
			eixosPrincipais.get(0), centroide)), altura));

	linhaDivisoria(forma, equacaoDaReta(eixosPrincipais.get(0), centroide));

	// imprimeReta( equacaoDaReta( eixosPrincipais.get( 0 ), centroide ),
	// true );
	// imprimeReta( equacaoDaReta( eixosPrincipais.get( 1 ), centroide ),
	// true );
	// imprimeReta( equacaoDaReta( new PontoDouble( 1 + centroide.i, 0.00032
	// + centroide.j ), centroide ), false );
	// imprimeReta( equacaoDaReta( new PontoDouble( -0.00032 + centroide.j,
	// 1 + centroide.i ), centroide ), false );

	// System.out.println( centroide.i + " " + centroide.j );
	// ContornoEmPxToBinaria( forma );
	// double[] vetorProprio1 = new double[ 2 ];
	// double[] vetorProprio2 = new double[ 2 ];
	// ExtratorDeMedidas.extraiVetoresProprios( ContornoEmPxToBinaria( forma
	// ), vetorProprio1, vetorProprio2 );
	// System.out.println( new PontoDouble( vetorProprio1[ 0 ],
	// vetorProprio1[ 1 ] ) + " " + new PontoDouble( vetorProprio2[ 0 ],
	// vetorProprio2[ 1 ] ) );
	// System.out.println( vetorProprio1 + " " + vetorProprio2 );
	// for( int i = 0; i < forma.size( ) ; i++ )
	// System.out.println( forma.size( ) + " : " + forma.get( i ) );
	// System.out.println( "---------------------" );
	// for( int i = 0; i < extremos.size( ); i++ )
	// System.out.println( extremos.size( ) + " : " + extremos.get( i
	// ).toString( ) );
	// System.out.println( "---------------------" );
	// for( int i = 0; i < perfil.size( ); i++ )
	// System.out.println( perfil.get( i ) );
	// perfil = contornoToPerfilInferior( forma, extremos );
	// for( int i = 0; i < perfil.size( ); i++ )
	// System.out.println( perfil.get( i ) );

	/* debug */
	/*
	 * double[ ] cara = new double[ 3 ]; cara[ 0 ] = 3; cara[ 1 ] = 4; cara[
	 * 2 ] = -6;
	 * 
	 * System.out.println( distanciaPontoAReta( new PontoDouble( 1, -2 ),
	 * cara ) ); cara = equacaoDaReta( new PontoDouble( 0, 1.5 ) , new
	 * PontoDouble( 1, 0.75 ) ); for( int i = 0; i < 3; i++ )
	 * System.out.print( cara[ i ] + " " ); System.out.println( avaliaReta(
	 * cara, 0 ) ); System.out.println( avaliaReta( cara, 1 ) );
	 */
    }
    /*
     * FUNCOES SEM USO
     * 
     * public static boolean[ ][ ] ContornoEmPxToBinaria( ArrayList<PontoDouble>
     * contorno ) { PontoDouble maxDimensoes = findMax( contorno );
     * 
     * maxDimensoes.i += 10; maxDimensoes.j += 10;
     * 
     * boolean [ ][ ] binaria = new boolean[ ( int ) maxDimensoes.i ][ ( int )
     * maxDimensoes.j ]; PontoDouble pontoTemp = new PontoDouble( 0, 0 );
     * 
     * for( int i = 0; i < maxDimensoes.i; i++ ) for( int j = 0; j <
     * maxDimensoes.j; j++ ) { binaria[ i ][ j ] = true; pontoTemp.i = i;
     * pontoTemp.j = j; if( contorno. contains( pontoTemp ) ) { binaria[ i ][ j ] =
     * true; } else binaria[ i ][ j ] = false; // if( binaria[ i ][ j ] ) //
     * System.out.println( pontoTemp.toString( ) ); } // printBinaria( binaria,
     * maxDimensoes ); return binaria; } public static void printBinaria(
     * boolean[ ][ ] binaria, PontoDouble maxDimensoes ) { for( int i = 0; i <
     * maxDimensoes.i; i++ ) { for( int j = 0; j < maxDimensoes.j; j++ ) { if(
     * binaria[ i ][ j ] ) System.out.print( "1 " ); else System.out.print( "0 " ); }
     * System.out.println( ); } }
     */
}
