package util;

import java.text.DecimalFormat;

import medidas.Curvatura;
import base.NumeroComplexo;

/**
 * Classe que contém métodos para calcular transformadas de Fourier
 * 
 * @see ExtratorDeMedidas
 * @see Curvatura
 */
public class Fourier {
    /**
     * Transformada direta de Fourier
     * 
     * @param x
     *                vetor a ser transformado
     * @return um novo vetor representando x transformado para o domínio das
     *         freqüências
     */
    public NumeroComplexo[] tdf(double x[]) {
	int N = x.length;
	NumeroComplexo[] tdf = new NumeroComplexo[N];

	for (int n = 0; n < N; n++) {
	    NumeroComplexo soma = new NumeroComplexo(0.0, 0.0);

	    for (int k = 0; k < N; k++) {
		double p = -2.0 * Math.PI * ((double) (k * (n - (N / 2))))
			/ (double) N;
		NumeroComplexo parcela = new NumeroComplexo(Math.cos(p), Math
			.sin(p));
		parcela = parcela.multiplicaPorEscalar(x[k]);
		soma = soma.soma(parcela);
	    }
	    tdf[n] = soma;
	}
	return tdf;
    }

    /**
     * Transformada inversa de Fourier
     * 
     * @param X
     *                vetor a ser transformado de volta
     * @return um novo vetor representando X transformado de volta para o
     *         domínio do tempo. Teoricamente deve ser um vetor de números reais
     */
    public NumeroComplexo[] tdif(NumeroComplexo X[]) {
	int N = X.length;
	NumeroComplexo[] tdif = new NumeroComplexo[N];

	for (int i = 0; i < N; i++) {
	    NumeroComplexo soma = new NumeroComplexo(0.0, 0.0);

	    for (int n = 0; n < N; n++) {
		double p = 2 * Math.PI * ((double) (i * (n - N / 2.0)))
			/ (double) N;
		NumeroComplexo parcela = new NumeroComplexo(Math.cos(p), Math
			.sin(p));

		parcela = parcela.multiplica(X[n]);
		soma = soma.soma(parcela);
	    }
	    soma = soma.multiplicaPorEscalar(1.0 / (double) N);
	    tdif[i] = soma;
	}
	return tdif;
    }

    /**
     * Método usado para testar esta classe. Dentro do código pode-se colocar
     * vetores que são transformados e transforamdos de volta e é impresso na
     * tela se ir e voltar não os alterou (teoricamente não pode alterar)
     */
    public static void main(String argv[]) {
	Fourier fourier = new Fourier();
	double[] x = { 10, 10, 10, 10, 10, 10, 10, 11, 12, 13, 14, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15, 15, 14, 13, 12, 11, 10, 10, 10,
		10, 10 };
	double[] y = { 18, 19, 20, 21, 22, 23, 24, 24, 24, 24, 24, 24, 23, 22,
		21, 20, 19, 18, 17, 16, 15, 14, 14, 13, 13, 13, 13, 13, 14, 15,
		16, 17 };
	final int n = x.length;
	DecimalFormat formatador = new DecimalFormat("0.000000");

	System.out.println("\n\nOriginal:");
	for (int i = 0; i < n; i++)
	    System.out.println(formatador.format(x[i]) + "\t"
		    + formatador.format(y[i]));
	System.out.println("\n\nTransf:");

	NumeroComplexo[] X = fourier.tdf(x);
	NumeroComplexo[] Y = fourier.tdf(y);

	for (int i = 0; i < n; i++)
	    System.out.println(X[i] + "\t" + Y[i]);
	System.out.println("\n\nInvert:");

	NumeroComplexo[] xi = fourier.tdif(X);
	NumeroComplexo[] yi = fourier.tdif(Y);

	for (int i = 0; i < n; i++)
	    System.out.println(xi[i] + "\t" + yi[i]);

	double[] curv = ExtratorDeMedidas.curvatura(x, y);

	System.out.println("\n\nCurvatura:");
	for (int i = 0; i < n; i++)
	    System.out.println(i + ": " + formatador.format(curv[i]));
    }
}
