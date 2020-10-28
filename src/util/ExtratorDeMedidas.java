package util;

import java.util.ArrayList;

import base.NumeroComplexo;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe que extrai medidas de imagens bin�rias
 */
public class ExtratorDeMedidas {
    /**
     * Fun��o que calcula a �rea ( em pixels ) de um objeto numa imagem bin�ria
     * 
     * @param imagem
     *                Imagem bin�ria.
     * @return N�mero de posi��es da matriz que valem true.
     */
    public static long area(boolean imagem[][]) {
	long area = 0;

	for (int i = 0; i < imagem.length; i++)
	    for (int j = 0; j < imagem[0].length; j++)
		if (imagem[i][j] == true)
		    area++;
	return area;
    }

    /**
     * Recebe uma imagem bin�ria com somente o objeto de interesse e devolve o
     * centr�ide desse objeto.
     * 
     * @param imagem
     *                Imagem bin�ria.
     * @return Centr�ide ( centro de massa ).
     */
    public static PontoDouble centroide(boolean[][] imagem) {
	long linhaDoCentroide = 0;
	long colunaDoCentroide = 0;
	long area = 0;

	for (int i = 0; i < imagem.length; i++) {
	    int j;

	    for (j = 0; j < imagem[0].length; j++) {
		if (imagem[i][j] == true) {
		    linhaDoCentroide += i;
		    colunaDoCentroide += j;
		    area++;
		}
	    }
	}
	if (area > 0) {
	    linhaDoCentroide /= area;
	    colunaDoCentroide /= area;
	}
	// errado
	return new PontoDouble(linhaDoCentroide, colunaDoCentroide);
	// certo
	// return new PontoDouble( colunaDoCentroide, linhaDoCentroide );
    }

    /**
     * Recebe um conjunto de pontos e um ponto espec�fico e devolve a dist�ncia (
     * euclidiana ) m�dia entre esse ponto e todos os demais. A inten��o �
     * calcular a dist�ncia m�dia do contorno at� o centr�ide.
     * 
     * @param ponto
     *                Ponto espec�fico ( por exemplo, o centr�ide ).
     * @param contorno
     *                Lista de pontos ( por exemplo, o conjunto de pontos do
     *                contorno ).
     * @return A m�dia das dist�ncia euclidianas.
     */
    public static double distanciaMediaAte(PontoDouble ponto,
	    ArrayList<PontoNatural> contorno) {
	double somaDasdistancias = 0.0;
	int k;

	for (k = 0; k < contorno.size(); k++)
	    somaDasdistancias += ponto.normaEuclidiana(contorno.get(k));
	return (somaDasdistancias / (double) k);
    }

    /**
     * Fun��o que extrai o contorno externo de um objeto ( Contour following ).
     * 
     * @param imagem
     *                Imagem bin�ria contendo somente o objeto de interesse.
     * @return Lista de pontos que representam o contorno do objeto.
     */
    public static ArrayList<PontoNatural> extraiContorno(boolean[][] imagem) {
	if (imagem == null)
	    return null;

	ArrayList<PontoNatural> contorno = new ArrayList<PontoNatural>();
	int i;
	int j = 0;
	boolean achou = false;
	// encontrando o ponto inicial

	for (i = 0; i < imagem.length && !achou; i++) {
	    for (j = 0; j < imagem[0].length && !achou; j++)
		if (imagem[i][j]) {
		    contorno.add(new PontoNatural(i, j - 1));
		    achou = true;
		}
	}
	if (!contorno.isEmpty()) {
	    // encontrando o segundo ponto do contorno
	    PontoNatural proximo = null;
	    PontoNatural candidato = null;
	    PontoNatural vizinhoDoCandidato = null;
	    int numeroDeCandidatosASegundoPonto = 0;

	    for (int direcao = 4; direcao <= 7; direcao++) {
		candidato = codigoDaCadeia(contorno.get(0), direcao);
		vizinhoDoCandidato = codigoDaCadeia(contorno.get(0),
			(direcao + 1) % 8);
		if (estaDentro(candidato, imagem)
			&& estaDentro(vizinhoDoCandidato, imagem)) {
		    if (imagem[candidato.i][candidato.j] == false
			    && imagem[vizinhoDoCandidato.i][vizinhoDoCandidato.j] == true) {
			if (numeroDeCandidatosASegundoPonto == 0) // ent�o
			    // achou
			    proximo = new PontoNatural(candidato.i, candidato.j); // o
			// "candidato"
			// �
			// eleito
			numeroDeCandidatosASegundoPonto++;
		    }
		}
	    }
	    boolean haMaisDeUmCaminhoParaComecar = (numeroDeCandidatosASegundoPonto > 1);

	    if (proximo == null)
		return contorno;
	    else {
		int direcaoAtualParaProximo = direcao(contorno.get(0), proximo);

		// enquanto n�o chegamos de volta ao ponto inicial do contorno
		// ( a menos que a figura seja vazada. Neste caso, continuamos a
		// contornar )
		while (proximo != null
			&& (!contorno.get(0).igual(proximo) || haMaisDeUmCaminhoParaComecar)) {
		    if (contorno.get(0).igual(proximo)
			    && haMaisDeUmCaminhoParaComecar)
			haMaisDeUmCaminhoParaComecar = false;
		    contorno.add(new PontoNatural(proximo.i, proximo.j));

		    int direcaoAnteriorParaAtual = direcaoAtualParaProximo;

		    direcaoAtualParaProximo = direcaoParaProximoPontoDoContorno(
			    proximo, direcaoAnteriorParaAtual, imagem);
		    if (direcaoAtualParaProximo == 8) // dire��o nenhuma, deu
			// erro
			return null;
		}
	    }
	}
	return contorno;
    }

    /**
     * @param atual
     *                Pixel atual do contorno. Recebido por refer�ncia: ao
     *                t�rmino da fun��o � atualizado com as coordenadas do
     *                pr�ximo ponto encontrado.
     * @param direcaoAnteriorParaAtual
     * @param binaria
     *                Imagem com objeto a ser contornado
     * @return Devolve a dire��o do ponto atual para o pr�ximo do contorno e
     *         devolve tamb�m o pr�ximo pixel na vari�vel "atual"
     */
    private static int direcaoParaProximoPontoDoContorno(PontoNatural atual,
	    int direcaoAnteriorParaAtual, boolean binaria[][]) {
	int direcaoAtualParaProximo = 8; // dire��o nenhuma
	int direcaoAtualParaAnterior = inverteDirecao(direcaoAnteriorParaAtual);
	PontoNatural proximo = null;

	for (int r = 0; r <= 6; r++) {
	    int direcaoE = (direcaoAtualParaAnterior + r) % 8;
	    int direcaoI = (direcaoAtualParaAnterior + r + 1) % 8;
	    PontoNatural e = codigoDaCadeia(atual, direcaoE);
	    PontoNatural i = codigoDaCadeia(atual, direcaoI);

	    if (estaDentro(e, binaria) && estaDentro(i, binaria))
		if (binaria[e.i][e.j] == false && binaria[i.i][i.j] == true) {
		    proximo = e;
		    direcaoAtualParaProximo = direcaoE;
		}
	}
	if (proximo != null) // ent�o tudo bem
	{
	    atual.i = proximo.i;
	    atual.j = proximo.j;
	} else
	    System.err
		    .println("  ***Erro: o objeto de interesse estava colado � borda da imagem e eu tentei extrair o contorno.");
	return direcaoAtualParaProximo;
    }

    /**
     * Devolve qual a dist�ncia, segundo o c�digo da cadeia, de um ponto para o
     * outro.
     * 
     * @param umPonto
     * @param outroPonto
     * @return Inteiro entre zero e oito. Zero significa leste, um significa
     *         nordeste e assim por diante. Oito significa erro.
     */
    private static int direcao(PontoNatural umPonto, PontoNatural outroPonto) {
	if (umPonto == null || outroPonto == null)
	    return 8;

	int i = umPonto.i;
	int j = umPonto.j;
	int u = outroPonto.i;
	int v = outroPonto.j;

	if (i == u && j + 1 == v)
	    return 0;
	else if (i - 1 == u && j + 1 == v)
	    return 1;
	else if (i - 1 == u && j == v)
	    return 2;
	else if (i - 1 == u && j - 1 == v)
	    return 3;
	else if (i == u && j - 1 == v)
	    return 4;
	else if (i + 1 == u && j - 1 == v)
	    return 5;
	else if (i + 1 == u && j == v)
	    return 6;
	else if (i + 1 == u && j + 1 == v)
	    return 7;
	else
	    return 8;
    }

    /**
     * Dada uma dire��o no c�digo da cadeia, devolve a dire��o inversa.
     * 
     * @param direcao
     *                Dire��o no c�digo da cadeia.
     * @return Inteiro entre zero e sete.
     */
    private static int inverteDirecao(int direcao) {
	return (direcao + 4) % 8;
    }

    /**
     * Anda uma casa do ponto de partida seguindo a dire��o dada.
     * 
     * @param atual
     *                Ponto de partida.
     * @param direcao
     *                Inteiro entre zero e sete informando a dire��o no c�digo
     *                da cadeia a ser seguida.
     * @return Um ponto deslocado em um pixel seguindo a dire��o dada.
     */
    private static PontoNatural codigoDaCadeia(PontoNatural atual, int direcao) {
	switch (direcao) {
	case 0:
	    return new PontoNatural(atual.i, atual.j + 1);
	case 1:
	    return new PontoNatural(atual.i - 1, atual.j + 1);
	case 2:
	    return new PontoNatural(atual.i - 1, atual.j);
	case 3:
	    return new PontoNatural(atual.i - 1, atual.j - 1);
	case 4:
	    return new PontoNatural(atual.i, atual.j - 1);
	case 5:
	    return new PontoNatural(atual.i + 1, atual.j - 1);
	case 6:
	    return new PontoNatural(atual.i + 1, atual.j);
	case 7:
	    return new PontoNatural(atual.i + 1, atual.j + 1);
	default: {
	    System.err.println("dire��o desconhecida: " + direcao);
	    return null;
	}
	}
    }

    /**
     * Valida��o de �ndice em matriz
     * 
     * @param p
     *                Coordenadas do ponto a ser testado.
     * @param matriz
     *                Matriz onde o ponto ser� testado.
     * @return Verdadeiro se e s� se o ponto est� dentro dos limites da matriz.
     */
    private static boolean estaDentro(PontoNatural p, boolean matriz[][]) {
	if (matriz == null)
	    return false;
	if (p == null)
	    return false;
	return (p.i >= 0 && p.j >= 0 && p.i < matriz.length && p.j < matriz[0].length);
    }

    /**
     * Fun��o que estima a curvatura de um objeto em cada ponto do contorno
     * atrav�s do m�todo do curvograma ( curvegram ).
     * 
     * @param x
     *                Coordenadas x dos pontos do contorno em ordem.
     * @param y
     *                Coordenadas y dos pontos do contorno em ordem.
     * @return Vetor com os valores de curvatura correspondentes a cada ponto do
     *         contorno.
     */
    public static double[] curvatura(double[] x, double[] y) {
	int i;

	if (x == null || y == null || x.length != y.length)
	    return null;

	int n = x.length;
	double[] curvatura = new double[n];
	Fourier f = new Fourier();
	NumeroComplexo[] fX = f.tdf(x);
	NumeroComplexo[] fY = f.tdf(y);
	NumeroComplexo[] f2X = f.tdf(x);
	NumeroComplexo[] f2Y = f.tdf(y);

	for (i = 0; i < n; i++) {
	    fX[i] = fX[i].multiplicaPorEscalar(i);
	    fY[i] = fY[i].multiplicaPorEscalar(i);
	    f2X[i] = f2X[i].multiplicaPorEscalar(i * i);
	    f2Y[i] = f2Y[i].multiplicaPorEscalar(i * i);
	}

	NumeroComplexo[] ifX = f.tdif(fX);
	NumeroComplexo[] ifY = f.tdif(fY);
	NumeroComplexo[] if2X = f.tdif(f2X);
	NumeroComplexo[] if2Y = f.tdif(f2Y);
	double numerador, denominador;

	for (i = 0; i < n; i++) {
	    numerador = (ifX[i].multiplica(if2Y[i]).subtrai(if2X[i]
		    .multiplica(ifY[i]))).parteReal;
	    denominador = Math.pow((ifX[i].multiplica(ifX[i]).soma(ifY[i]
		    .multiplica(ifY[i]))).parteReal, 1.5);
	    curvatura[i] = numerador / denominador;
	}
	return curvatura;
    }

    /**
     * Fun��o que calcula a energia de dobramento ( Medida que sintetiza a
     * curvatura em um n�mero ) de um objeto, dada sua curvatura.
     * 
     * @param curvatura
     *                Curvatura do objeto.
     * @return Soma dos quadrados da curvatura.
     */
    public static double energiaDeDobramento(double[] curvatura) {
	double energia = 0.0;
	for (int i = 0; i < curvatura.length; i++)
	    energia += Math.pow(curvatura[i], 2);
	return energia / (double) curvatura.length;
    }

    /**
     * Fun��o que calcula o valor ( em pixels ) da maior corda contida num
     * objeto numa imagem bin�ria e tamb�m os pontos extremos dessa corda. Faz
     * busca exaustiva medindo a dist�ncia entre todos os pares de pontos do
     * contorno.
     * 
     * @param contorno
     *                Lista de pontos do contorno do objeto de interesse.
     * @param umExtremo
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ).
     * @param outroExtremo
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ).
     * @return N�mero de pixels do maior segmento inteiramente contido no
     *         objeto.
     */
    public static double maiorDiametro(ArrayList<PontoNatural> contorno,
	    PontoNatural umExtremo, PontoNatural outroExtremo) {
	int n = contorno.size();
	double distMax = 0;

	for (int i = 0; i < n; i++) {
	    PontoNatural umPonto = contorno.get(i);
	    for (int j = i; j < n; j++) {
		PontoNatural outroPonto = contorno.get(j);
		double dist = umPonto.normaEuclidiana(outroPonto);
		if (dist > distMax) {
		    distMax = dist;
		    umExtremo.copiaValores(umPonto);
		    outroExtremo.copiaValores(outroPonto);
		}
	    }
	}
	return distMax;
    }

    /**
     * Mede a assimetria de um objeto em rela��o ao eixo secund�rio.
     * 
     * @param imagemBinaria
     *                Imagem bin�ria contendo o objeto de interesse.
     * @param vetorPrimario
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ). Conter� o vetor
     *                diretor do eixo principal do objeto.
     * @param vetorSecundario
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ). Conter� o vetor
     *                diretor do eixo secund�rio do objeto.
     * @param centroide
     *                Centro de massa do objeto.
     * @return Subtra��o entre os dois lados lados do objeto, em rela��o ao eixo
     *         secund�rio.
     */
    public static long assimetria(boolean[][] imagemBinaria,
	    double[] vetorPrimario, double[] vetorSecundario,
	    PontoDouble centroide) {
	long medidaDeAssimetria = 0;

	extraiVetoresProprios(imagemBinaria, vetorPrimario, vetorSecundario);
	for (int i = 0; i < imagemBinaria.length; i++)
	    for (int j = 0; j < imagemBinaria[0].length; j++) {
		if (imagemBinaria[i][j]) {
		    int posicao = posicaoRelativa(new PontoNatural(i, j),
			    vetorSecundario, centroide);

		    if (posicao == 1)
			medidaDeAssimetria++;
		    else if (posicao == -1)
			medidaDeAssimetria--;
		}
	    }
	return medidaDeAssimetria;
    }

    /**
     * Calcula os vetores pr�prios de uma imagem bin�ria.
     * 
     * @param imagemBinaria
     * @param vetorProprio1
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ).
     * @param vetorProprio2
     *                Deve ser inicializado antes ( n�o pode ser null ) pois �
     *                modificado ( passado por refer�ncia ).
     */
    public static void extraiVetoresProprios(boolean[][] imagemBinaria,
	    double[] vetorProprio1, double[] vetorProprio2) {
	OperacoesComMatriz op = new OperacoesComMatriz();
	double[][] matrizDeCovariancia = op.matrizDeCovariancia(imagemBinaria);
	double[] valoresProprios = op.valoresProprios(matrizDeCovariancia);
	double[] v1 = op.vetorProprioAssociado(matrizDeCovariancia,
		valoresProprios[0]);
	double[] v2 = op.vetorProprioAssociado(matrizDeCovariancia,
		valoresProprios[1]);

	vetorProprio1[0] = v1[0];
	vetorProprio1[1] = v1[1];
	vetorProprio2[0] = v2[0];
	vetorProprio2[1] = v2[1];

	if (op.iguais(vetorProprio1, vetorProprio2))
	    vetorProprio2[1] *= -1;
    }

    // DAVID: mudei pra public static
    public static int posicaoRelativa(PontoNatural p, double[] vetorDiretor,
	    PontoDouble umPontoDaReta) {
	double k = ((double) (p.i - umPontoDaReta.i)) / vetorDiretor[0];

	if (k * vetorDiretor[1] + umPontoDaReta.j > p.j)
	    return -1;
	else if (k * vetorDiretor[1] + umPontoDaReta.j < p.j)
	    return 1;
	else
	    // a reta passa pelo ponto p
	    return 0;
    }

    /**
     * Calcula o per�metro (em pixels) de um objeto.
     * 
     * @param contorno
     *                Lista de pontos representado o contorno do objeto.
     * @return Medida do per�metro.
     */
    public static double perimetro(ArrayList<PontoNatural> contorno) {
	double tamanhoDoContorno = 0;
	int n = contorno.size();

	for (int i = 1; i < n; i++) {
	    if (saoVizinhos(contorno.get(i - 1), contorno.get(i)))
		tamanhoDoContorno += 1;
	    else
		// ent�o eles formam uma diagonal e est�o mais longes do que s�
		// um pixel um do outro
		tamanhoDoContorno += Math.sqrt(2);
	}
	return tamanhoDoContorno;
    }

    /**
     * Usando a quatro-vizinhan�a.
     * 
     * @param umPonto
     * @param outroPonto
     * @return
     */
    private static boolean saoVizinhos(PontoNatural umPonto,
	    PontoNatural outroPonto) {
	return (umPonto.i == outroPonto.i && Math.abs(umPonto.j - outroPonto.j) == 1)
		|| (umPonto.j == outroPonto.j && Math.abs(umPonto.i
			- outroPonto.i) == 1);
    }

}
