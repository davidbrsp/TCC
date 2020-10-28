package util;

import medidas.MedidaDeAssimetria;

/**
 * Classe que executa opera��es relativas a matrizes, como c�lculo de valores e
 * vetores pr�prios e matriz de covari�ncia
 * 
 * @see ExtratorDeMedidas
 * @see MedidaDeAssimetria
 */
public class OperacoesComMatriz {
    private static final double epsilon = 0.00000001;

    /**
     * Fun��o que, dada uma matriz de reais, calcula os valores pr�prios (ou
     * autovalores) associados � matriz
     * 
     * @param m
     *                Matriz de reais.
     * @return Vetor de reais com duas posi��es, uma para cada valor pr�prio.
     */
    public double[] valoresProprios(double[][] m) {
	double[] valoresProprios = new double[2];
	double primeiraParcela = m[1][1] + m[0][0];
	double segundaParcela = Math.sqrt(m[1][1] * m[1][1] + m[0][0] * m[0][0]
		+ 2 * m[0][0] * m[1][1] - 4 * m[0][0] * m[1][1] + 4 * m[0][1]
		* m[1][0]);

	valoresProprios[0] = (primeiraParcela + segundaParcela) / 2;
	valoresProprios[1] = (primeiraParcela - segundaParcela) / 2;
	return valoresProprios;
    }

    /**
     * Fun��o que, dada uma matriz booleana, transforma a matriz em dois vetores
     * x e y que cont�m as cooredenadas das posi��es que valem true na matriz e
     * calcula a matriz de covari�ncia da matriz [ x | y ].
     * 
     * @param binarizada
     *                imagem bin�ria cuja matriz de covari�ncia ser� calculada
     * @return matriz 2x2 de covari�ncia: [Var(x) Cov(x,y); Cov(x,y) Var(y)]
     */
    public double[][] matrizDeCovariancia(boolean[][] binarizada) {
	double[] x = new double[binarizada.length * binarizada[0].length];
	double[] y = new double[binarizada.length * binarizada[0].length];
	int n = 0;

	for (int i = 0; i < binarizada.length; i++)
	    for (int j = 0; j < binarizada[0].length; j++)
		if (binarizada[i][j]) {
		    x[n] = i;
		    y[n] = j;
		    n++;
		}

	double[] u = new double[n];
	double[] v = new double[n];

	for (int i = 0; i < n; i++) {
	    u[i] = x[i];
	    v[i] = y[i];
	}

	double cov[][] = new double[2][2];

	cov[0][0] = variancia(u);
	cov[0][1] = covariancia(u, v);
	cov[1][0] = cov[0][1];
	cov[1][1] = variancia(v);
	return cov;
    }

    private double variancia(double[] x) {
	double esperancaDeXquadrado = momento(x, 2);
	double esperancaAoQuadradoDeX = Math.pow(momento(x, 1), 2);
	return esperancaDeXquadrado - esperancaAoQuadradoDeX;
    }

    private double momento(double[] x, int m) {
	int i;
	double momento = 0.0;

	for (i = 0; i < x.length; i++)
	    momento += Math.pow(x[i], m);
	return momento / i;
    }

    private double covariancia(double[] x, double[] y) {
	double esperancaDeXY = momento(produtoInterno(x, y), 1);
	double esperancaDeXesperancaDeY = momento(x, 1) * momento(y, 1);

	return esperancaDeXY - esperancaDeXesperancaDeY;
    }

    private double[] produtoInterno(double[] x, double[] y) {
	if (x.length != y.length)
	    System.out
		    .println("Cuidado: fazendo produto interno de vetores de tamanhos diferentes!");

	double[] result = new double[x.length];

	for (int i = 0; i < result.length; i++)
	    result[i] = x[i] * y[i];
	return result;
    }

    /**
     * Verificar se um n�mero � suficientemente pr�ximo de zero
     * 
     * @param valor
     *                a ser testado
     * @return true se e somente se o valor for pr�ximo o suficiente de zero
     */
    public boolean nulo(double valor) {
	return Math.abs(valor) <= epsilon;
    }

    /**
     * Calcula o vetor pr�prio (ou autovetor) associado a um valor pr�prio e a
     * uma matriz
     * 
     * @param matriz
     * @param valorProprio
     *                ou autovalor associado � matriz
     * @return devolve um vetor em R� contendo o vetor pr�prio associado ao
     *         valor pr�prio e a matriz passados
     */
    public double[] vetorProprioAssociado(double[][] matriz, double valorProprio) {
	double[][] m = copia(matriz);
	double[] vetorProprio = new double[2];

	m = subtraiDaDiagonal(m, valorProprio);

	int quantosZeros = contaZeros(m);

	// escalona se n�o tiver nenhum zero
	if (quantosZeros == 0) {
	    m[1][1] -= (m[1][0] * m[0][1]) / m[0][0];
	    m[1][0] = 0;
	    quantosZeros = contaZeros(m);
	}
	// se a matriz ( com os autovetores j� subtra�dos da diagonal )
	// tem posto completo, ent�o ela n�o tem autovetor, mas qquer um serve
	// pro nosso caso
	if (quantosZeros == 1 || (quantosZeros == 2 && diagonalNula(m))) {
	    vetorProprio[0] = 1.0;
	    vetorProprio[1] = 0.0;
	} else if (quantosZeros == 4) {
	    vetorProprio[0] = 1.0;
	    vetorProprio[1] = 0.0;
	} else if (colunaNula(m, 0)) {
	    vetorProprio[0] = 1.0;
	    vetorProprio[1] = 0.0;
	} else if (colunaNula(m, 1)) {
	    vetorProprio[0] = 0.0;
	    vetorProprio[1] = 1.0;
	} else {
	    double a, b;

	    if (linhaNula(m, 0)) {
		a = m[1][0];
		b = m[1][1];
	    } else if (linhaNula(m, 1)) {
		a = m[0][0];
		b = m[0][1];
	    } else {
		System.err
			.println("Erro inesperado ao extrair o vetor pr�prio.");
		return null;
	    }
	    vetorProprio[0] = -b / a;
	    vetorProprio[1] = 1.0;
	}
	return vetorProprio;
    }

    private double[][] copia(double[][] matriz) {
	double[][] copia = new double[matriz.length][matriz[0].length];

	for (int i = 0; i < matriz.length; i++)
	    for (int j = 0; j < matriz[0].length; j++)
		copia[i][j] = matriz[i][j];
	return copia;
    }

    private double[][] subtraiDaDiagonal(double[][] m, double valor) {
	for (int i = 0; i < m.length && i < m[0].length; i++)
	    m[i][i] -= valor;
	return m;
    }

    private boolean linhaNula(double[][] m, int linha) {
	for (int i = 0; i < m[0].length; i++)
	    if (!nulo(m[linha][i]))
		return false;
	return true;
    }

    private boolean colunaNula(double[][] m, int coluna) {
	for (int i = 0; i < m.length; i++)
	    if (!nulo(m[i][coluna]))
		return false;
	return true;
    }

    private boolean diagonalNula(double[][] m) {
	for (int i = 0; i < m.length && i < m[0].length; i++)
	    if (!nulo(m[i][i]))
		return false;
	return true;
    }

    private int contaZeros(double[][] m) {
	int quantosZeros = 0;

	for (int i = 0; i < m.length; i++)
	    for (int j = 0; j < m[0].length; j++)
		if (nulo(m[i][j]))
		    quantosZeros++;
	return quantosZeros;
    }

    /**
     * Verifica se dois vetores s�o suficientemente pr�ximos para serem
     * considerados iguais
     * 
     * @param v1
     *                vetor de qualquer dimens�o
     * @param v2
     *                vetor de qualquer dimens�o
     * @return true se e somente se os valores forem suficientemente pr�ximos
     */
    public boolean iguais(double[] v1, double[] v2) {
	if (v1.length != v2.length)
	    return false;
	for (int i = 0; i < v1.length; i++)
	    if (nulo(v1[i] - v2[i]))
		return false;
	return true;
    }

    /**
     * Divide cada componente do vetor pela norma do vetor
     * 
     * @param v
     *                vetor de qualquer dimens�o a ser normalizado
     * @return um novo vetor com mesma dire��o e mesmo sentido mas com norma
     *         igual a um
     */
    public double[] normalizaVetor(double[] v) {
	double[] result = new double[v.length];
	double norma = norma(v);

	if (nulo(norma))
	    return v;
	for (int i = 0; i < v.length; i++)
	    result[i] = v[i] / norma;
	return result;
    }

    /**
     * Calcula a norma de um vetor
     * 
     * @param v
     *                vetor de qualquer dimens�o
     * @return norma euclidiana (ou norma 2) do vetor
     */
    public double norma(double[] v) {
	double norma = 0.0;

	for (int i = 0; i < v.length; i++)
	    norma += Math.pow(v[i], 2);
	return Math.sqrt(norma);
    }
}
