package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;

import main.Controlador;
import base.PontoNatural;

/**
 * Classe que executa procedimentos de pré-processamento de imagens, como
 * binarização e correções Sempre usando a 4-vizinhança (vizinhança em cruz)
 * 
 * @see Controlador
 */
public class PreProcessador {
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;

    // DAVID DEBUG
    /**
     * @author David Macedo da Conceição public static BufferedImage
     *         convertToGrayscale( BufferedImage source )
     * @param source
     *                Imagem colorida.
     * @return Retorna a representacao em nivel de cinza da imagem.
     */
    public static BufferedImage convertToGrayscale(BufferedImage source) {
	BufferedImageOp op = new ColorConvertOp(ColorSpace
		.getInstance(ColorSpace.CS_GRAY), null);

	return op.filter(source, null);
    }

    // DAVID
    // retirado de: http://www.javalobby.org/articles/ultimate-image/#11
    // mais uma bo opcao em: http://www.componenthouse.com/article-20
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
	int w = img.getWidth();
	int h = img.getHeight();
	BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
	Graphics2D g = dimg.createGraphics();
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
	g.dispose();
	return dimg;
    }

    // DAVID
    /**
     * @author David Macedo da Conceição Representacao em nivel de cinza da
     *         banda de uma imagem colorida.
     * @param colorImage
     *                Imagem colorida.
     * @param banda
     *                Banda escolhida, (R = 0, G = 1, B = 2)
     * @return Retorna a representacao em nivel de cinza da banda escolhida.
     */
    public static BufferedImage pegaBandaEmCinza(BufferedImage colorImage,
	    int banda) {
	BufferedImage grayImage = new BufferedImage(colorImage.getWidth(),
		colorImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

	if (0 < banda && banda > 2) {
	    System.out.print("Banda inválida: " + banda + "!!!\n");
	    grayImage = null;
	} else {
	    for (int i = 0; i < colorImage.getWidth(); i++)
		for (int j = 0; j < colorImage.getHeight(); j++) {
		    grayImage.getRaster().setSample(i, j, 0,
			    colorImage.getRaster().getSample(i, j, banda));
		}
	}
	return grayImage;
    }

    // DAVID
    /**
     * Calculate maximum entropy split of a histogram.
     * 
     * @param hist
     *                histogram to be thresholded.
     * @return index of the maximum entropy split.`
     * @see <code> http://rsbweb.nih.gov/ij/plugins/download/Entropy_Threshold.java </code>
     */
    public static int entropySplit(int[] hist) {
	// Normalize histogram, that is makes the sum of all bins equal to 1.
	double sum = 0;

	for (int i = 0; i < hist.length; ++i)
	    sum += hist[i];
	if (sum == 0)
	    // This should not normally happen, but...
	    throw new IllegalArgumentException(
		    "Empty histogram: sum of all bins is zero.");

	double[] normalizedHist = new double[hist.length];
	for (int i = 0; i < hist.length; i++)
	    normalizedHist[i] = hist[i] / sum;

	double[] pT = new double[hist.length];

	pT[0] = normalizedHist[0];
	for (int i = 1; i < hist.length; i++)
	    pT[i] = pT[i - 1] + normalizedHist[i];

	// Entropy for black and white parts of the histogram
	final double epsilon = Double.MIN_VALUE;
	double[] hB = new double[hist.length];
	double[] hW = new double[hist.length];

	for (int t = 0; t < hist.length; t++) {
	    // Black entropy
	    if (pT[t] > epsilon) {
		double hhB = 0;

		for (int i = 0; i <= t; i++)
		    if (normalizedHist[i] > epsilon)
			hhB -= normalizedHist[i] / pT[t]
				* Math.log(normalizedHist[i] / pT[t]);
		hB[t] = hhB;
	    } else
		hB[t] = 0;
	    // White entropy
	    double pTW = 1 - pT[t];

	    if (pTW > epsilon) {
		double hhW = 0;

		for (int i = t + 1; i < hist.length; ++i)
		    if (normalizedHist[i] > epsilon)
			hhW -= normalizedHist[i] / pTW
				* Math.log(normalizedHist[i] / pTW);
		hW[t] = hhW;
	    } else
		hW[t] = 0;
	}
	// Find histogram index with maximum entropy
	double jMax = hB[0] + hW[0];
	int tMax = 0;

	for (int t = 1; t < hist.length; ++t) {
	    double j = hB[t] + hW[t];

	    if (j > jMax) {
		jMax = j;
		tMax = t;
	    }
	}

	return tMax;
    }

    // DAVID
    /**
     * @author David Macedo da Conceicão Calcula o histograma para a banda da
     *         Imagem
     * @param image
     *                Imagem colorida.
     * @param banda
     *                Banda escolhida, (R = 0, G = 1, B = 2).
     * @return retorna o vetor de inteiros com o histograma.
     */
    public static int[] histogramaBanda(BufferedImage image, int banda) {
	if (image != null) {
	    int[] hist = new int[256];
	    int index;

	    for (int i = 0; i < hist.length; i++)
		hist[i] = 0;
	    for (int i = 0; i < image.getHeight(); i++)
		for (int j = 0; j < image.getWidth(); j++) {
		    index = image.getRaster().getSample(j, i, banda);
		    hist[index] += 1;
		}
	    return hist;
	} else {
	    System.out.print("Banda inválida: " + banda + "!!!\n");
	    return null;
	}
    }

    // DAVID
    public static boolean[][] limiarizaGmR(BufferedImage original) {

	if (original == null)
	    return null;
	else {
	    int largura = original.getWidth();
	    int altura = original.getHeight();
	    boolean[][] binarizada = new boolean[altura][largura];

	    for (int i = 0; i < altura; i++) {
		for (int j = 0; j < largura; j++) {
		    if (original.getRaster().getSample(j, i, 1)
			    - original.getRaster().getSample(j, i, 0) < 1)
			binarizada[i][j] = true;
		}
	    }
	    return binarizada;
	}
    }

    /**
     * Binarização através de limiarização (também conhecida como Thresholding)
     * em uma das bandas
     * 
     * @param limite
     *                limiar entre 0 e 255
     * @param original
     *                imagem RGB de entrada
     * @param banda
     *                banda RGB sobre a qual executar a limiarização
     * @return devolve uma matriz booleana representando a imagem binária
     *         resultante da limiarização
     */
    public static boolean[][] limiariza(int limite, BufferedImage original,
	    int banda) {
	if (original == null)
	    return null;
	else {
	    int largura = original.getWidth();
	    int altura = original.getHeight();
	    boolean[][] binarizada = new boolean[altura][largura];

	    for (int i = 0; i < altura; i++) {
		for (int j = 0; j < largura; j++) {
		    if (original.getRaster().getSample(j, i, banda) > limite)
			binarizada[i][j] = true;
		}
	    }
	    return binarizada;
	}
    }

    /**
     * Recebe uma imagem binária e preenche todas as casas que têm true em algum
     * lugar à esquerda, à direita, acima e abaixo. Usada para corrigir a
     * binarização (supomos que o objeto de interesse não é vazado)
     * 
     * @param imagem
     *                matriz booleana representando a imagem binária
     * @return a própria matriz com buracos preenchidos
     */
    public static boolean[][] tampaBuracos(boolean[][] imagem) {
	int i, j;

	for (i = 0; i < imagem.length; i++)
	    for (j = 0; j < imagem[0].length; j++)
		if (imagem[i][j] == false && temObjetoEmVoltaDe(i, j, imagem))
		    imagem[i][j] = true;

	return imagem;
    }

    private static boolean temObjetoEmVoltaDe(int linha, int coluna,
	    boolean[][] imagem) {
	return temObjetoEntre(linha, 0, VERTICAL, coluna, imagem)
		&& temObjetoEntre(0, coluna, HORIZONTAL, linha, imagem)
		&& temObjetoEntre(linha, imagem.length - 1, VERTICAL, coluna,
			imagem)
		&& temObjetoEntre(imagem[0].length - 1, coluna, HORIZONTAL,
			linha, imagem);
    }

    private static boolean temObjetoEntre(int umaCoordenada,
	    int outraCoordenada, int eixo, int coordenadaFixa,
	    boolean[][] imagem) {
	int i = Math.min(umaCoordenada, outraCoordenada);
	int fim = Math.max(umaCoordenada, outraCoordenada);
	while (i <= fim) {
	    if (eixo == HORIZONTAL) {
		if (imagem[coordenadaFixa][i]) {
		    return true;
		}
	    } else if (eixo == VERTICAL) {
		if (imagem[i][coordenadaFixa]) {
		    return true;
		}
	    } else
		System.out.println("erro inesperado!!!!!");
	    i++;
	}
	return false;
    }

    /**
     * Retira qquer componente da imagem com área (em pixels) estritamente menor
     * do que a menorAreaPermitida. Por componente entende-se grupo de posições
     * vizinhas (usando a 4-vizinhança) todas valendo true
     * 
     * @param menorAreaPermitida
     *                valor mínimo de área permitido
     * @param imagem
     *                matriz booleana que será modificada
     * @return devolve a própria imagem com apenas as componentes conexas com
     *         área (em pixels) maior do que menorAreaPermitida
     */
    public static boolean[][] retiraComponentesComAreaMenorQue(
	    int menorAreaPermitida, boolean[][] imagem) {
	int i, j;
	int m = imagem.length;
	int n = imagem[0].length;
	long[][] componentes = new long[m][n];

	for (i = 0; i < m; i++)
	    for (j = 0; j < n; j++)
		componentes[i][j] = -1;

	long rotulo = 0;

	for (i = 0; i < m; i++) {
	    for (j = 0; j < n; j++) {
		// se ainda não foi rotulado
		if (componentes[i][j] == -1 && imagem[i][j]) {
		    int areaDaComponente = rotulaVizinhosEDevolveArea(rotulo,
			    componentes, imagem, i, j);

		    if (areaDaComponente < menorAreaPermitida) {
			retiraComponente(i, j, componentes, imagem);
		    }
		    rotulo++;
		} // senão, já foi verificado o valor da área, então não faz
		// nada
	    }
	}
	return imagem;
    }

    /**
     * Retira uma certa componente conexa que contém o pixel ( linha, coluna )
     * 
     * @param linha
     *                uma das coordenadas de um ponto contido na componente a
     *                ser retirada
     * @param coluna
     *                outra coordenadas de um ponto contido na componente a ser
     *                retirada
     * @param componentes
     *                Matriz correspondente à imagem com as componentes conexas
     *                rotuladas.
     * @param imagem
     *                Imagem binária a ser processada.
     */
    private static void retiraComponente(int linha, int coluna,
	    long[][] componentes, boolean[][] imagem) {
	// se o ponto estiver dentro da matriz..
	if (linha >= 0 && coluna >= 0 && linha < componentes.length
		&& coluna < componentes[0].length) {
	    // se o ponto estiver rotulado..
	    if (imagem[linha][coluna] && componentes[linha][coluna] != -1) {
		componentes[linha][coluna] = -1; // desrotula
		imagem[linha][coluna] = false; // tira da imagem original

		// empilha o ponto
		Fila<PontoNatural> fila = new Fila<PontoNatural>();

		fila.insere(new PontoNatural(linha, coluna));

		while (!fila.filaVazia()) {
		    PontoNatural atual = (PontoNatural) fila
			    .pegaPrimeiroDaFila();
		    int i = atual.i;
		    int j = atual.j;

		    // empilha os vizinhos desrotulados internos à matriz
		    if (i - 1 >= 0 && imagem[i - 1][j]
			    && componentes[i - 1][j] != -1) {
			fila.insere(new PontoNatural(i - 1, j));
			componentes[i - 1][j] = -1; // desrotula
			imagem[i - 1][j] = false; // tira da imagem original
		    }
		    if (i + 1 < componentes.length && imagem[i + 1][j]
			    && componentes[i + 1][j] != -1) {
			fila.insere(new PontoNatural(i + 1, j));
			componentes[i + 1][j] = -1; // desrotula
			imagem[i + 1][j] = false; // tira da imagem original
		    }
		    if (j - 1 >= 0 && imagem[i][j - 1]
			    && componentes[i][j - 1] != -1) {
			fila.insere(new PontoNatural(i, j - 1));
			componentes[i][j - 1] = -1; // desrotula
			imagem[i][j - 1] = false; // tira da imagem original
		    }
		    if (j + 1 < componentes[0].length && imagem[i][j + 1]
			    && componentes[i][j + 1] != -1) {
			fila.insere(new PontoNatural(i, j + 1));
			componentes[i][j + 1] = -1; // desrotula
			imagem[i][j + 1] = false; // tira da imagem original
		    }
		}
	    }
	}
    }

    /**
     * Usando a quatro-vizinhança
     * 
     * @param valor
     *                valor.
     * @param componentes
     *                onde copiar o valor.
     * @param imagem
     *                binária.
     * @param linha
     *                linha.
     * @param coluna
     *                coluna.
     */
    private static int rotulaVizinhosEDevolveArea(long valor,
	    long[][] componentes, boolean[][] imagem, int linha, int coluna) {
	int area = 0;

	// se o ponto estiver dentro da matriz
	if (linha >= 0 && coluna >= 0 && linha < componentes.length
		&& coluna < componentes[0].length) {
	    if (imagem[linha][coluna]) {
		componentes[linha][coluna] = valor;

		Fila<PontoNatural> fila = new Fila<PontoNatural>();

		fila.insere(new PontoNatural(linha, coluna));

		while (!fila.filaVazia()) {
		    PontoNatural atual = (PontoNatural) fila
			    .pegaPrimeiroDaFila();
		    int i = atual.i;
		    int j = atual.j;

		    area++;
		    // empilha os vizinhos desrotulados internos à matriz
		    if (i - 1 >= 0 && imagem[i - 1][j]
			    && componentes[i - 1][j] == -1) {
			fila.insere(new PontoNatural(i - 1, j));
			componentes[i - 1][j] = valor;
		    }
		    if (i + 1 < componentes.length && imagem[i + 1][j]
			    && componentes[i + 1][j] == -1) {
			fila.insere(new PontoNatural(i + 1, j));
			componentes[i + 1][j] = valor;
		    }
		    if (j - 1 >= 0 && imagem[i][j - 1]
			    && componentes[i][j - 1] == -1) {
			fila.insere(new PontoNatural(i, j - 1));
			componentes[i][j - 1] = valor;
		    }
		    if (j + 1 < componentes[0].length && imagem[i][j + 1]
			    && componentes[i][j + 1] == -1) {
			fila.insere(new PontoNatural(i, j + 1));
			componentes[i][j + 1] = valor;
		    }
		}
	    }
	}
	return area;
    }

    /**
     * Define as componentes conexas de uma imagem binária e as rotula com o
     * tamanho de suas áreas
     * 
     * @param imagem
     *                binária
     * @return matriz de long com 0 nas posições que valiam false na imagem
     *         passada como parâmetro e o valor da área da componente a qual
     *         pertence nas demais posições
     */
    public static long[][] rotulaComponentesComSuasAreas(boolean[][] imagem) {
	long[][] componentes = new long[imagem.length][imagem[0].length];
	int i, j;

	for (i = 0; i < imagem.length; i++)
	    for (j = 0; j < imagem[0].length; j++)
		componentes[i][j] = -1;
	for (i = 0; i < imagem.length; i++)
	    for (j = 0; j < imagem[0].length; j++) {
		if (imagem[i][j] && componentes[i][j] == -1) {
		    // rotula a componente com valor zero e descobre sua área
		    long area = rotulaVizinhosEDevolveArea(0, componentes,
			    imagem, i, j);
		    // agora, sabendo a área, rotula a mesma componente com este
		    // valor

		    rotulaVizinhosEDevolveArea(area, componentes, imagem, i, j);
		}
	    }
	return componentes;
    }

    /**
     * Percorre a matriz com componentes conexas e devolve o segundo maior
     * rótulo
     * 
     * @param componentesConexas
     * @return
     */
    public static long segundoMaiorRotulo(long[][] componentesConexas) {
	long maior = 0, segundoMaior = 0;
	int i, j;

	for (i = 0; i < componentesConexas.length; i++)
	    for (j = 0; j < componentesConexas[0].length; j++) {
		if (componentesConexas[i][j] > maior) {
		    segundoMaior = maior;
		    maior = componentesConexas[i][j];
		} else if (componentesConexas[i][j] > segundoMaior
			&& componentesConexas[i][j] < maior)
		    segundoMaior = componentesConexas[i][j];
	    }
	return segundoMaior;
    }

    /**
     * Percorre a matriz com componentes conexas e devolve o maior rótulo
     * 
     * @param componentesConexas
     * @return
     */
    public static long MaiorRotulo(long[][] componentesConexas) {
	long maior = 0, segundoMaior = 0;
	int i, j;

	for (i = 0; i < componentesConexas.length; i++)
	    for (j = 0; j < componentesConexas[0].length; j++) {
		if (componentesConexas[i][j] > maior) {
		    segundoMaior = maior;
		    maior = componentesConexas[i][j];
		} else if (componentesConexas[i][j] > segundoMaior
			&& componentesConexas[i][j] < maior)
		    segundoMaior = componentesConexas[i][j];
	    }
	return maior;
    }

    /**
     * Retira a primeira componente conexa que encontra percorrendo a imagem
     * binária da esq para a direita, de cima para baixo.
     */
    public static boolean[][] retiraPrimeiraComponente(boolean[][] binarizada) {
	if (binarizada == null)
	    return null;
	int i = 0, j = 0;

	// encontra o primeiro pixel
	boolean achou = false;

	for (i = 0; i < binarizada.length && !achou; i++) {
	    for (j = 0; j < binarizada[0].length && !achou; j++) {
		if (binarizada[i][j])
		    achou = true;
	    }
	}
	retiraComponente(i - 1, j - 1, binarizada);
	return binarizada;
    }

    private static void retiraComponente(int linha, int coluna,
	    boolean[][] binarizada) {
	Fila<PontoNatural> fila = new Fila<PontoNatural>();

	fila.insere(new PontoNatural(linha, coluna));
	binarizada[linha][coluna] = false;
	while (!fila.filaVazia()) {
	    PontoNatural atual = (PontoNatural) fila.pegaPrimeiroDaFila();
	    int i = atual.i;
	    int j = atual.j;

	    // empilha os vizinhos que são objeto
	    if (i - 1 >= 0 && binarizada[i - 1][j]) {
		binarizada[i - 1][j] = false;
		fila.insere(new PontoNatural(i - 1, j));
	    }
	    if (i + 1 < binarizada.length && binarizada[i + 1][j]) {
		binarizada[i + 1][j] = false;
		fila.insere(new PontoNatural(i + 1, j));
	    }
	    if (j - 1 >= 0 && binarizada[i][j - 1]) {
		binarizada[i][j - 1] = false;
		fila.insere(new PontoNatural(i, j - 1));
	    }
	    if (j + 1 < binarizada[0].length && binarizada[i][j + 1]) {
		binarizada[i][j + 1] = false;
		fila.insere(new PontoNatural(i, j + 1));
	    }
	}
    }
    /***************************************************************************
     * Essa função não foi usada * / public static long[ ][ ]
     * componentesConexas( boolean[ ][ ] imagem ) { if( imagem == null ) return
     * null;
     * 
     * long[ ][ ] componentes = new long[ imagem.length ][ imagem[ 0 ].length ];
     * int i,j;
     * 
     * for( i = 0; i < imagem.length; i++ ) for( j = 0; j < imagem[ 0 ].length;
     * j++ ) componentes[ i ][ j ] = -1;
     * 
     * int numComponentes = 0;
     * 
     * for( i = 0; i < imagem.length; i++ ) for( j = 0; j < imagem[ 0 ].length;
     * j++ ) { if( imagem[ i ][ j ] && componentes[ i ][ j ] == -1 ) {
     * rotulaVizinhosEDevolveArea( numComponentes,componentes, imagem, i, j );
     * numComponentes++; } } return componentes; }
     */
}
