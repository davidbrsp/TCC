package util;

/**
 * Classe usada para depuração
 */
public class Depurador {
    /**
     * Método usado para visualizar na saída padrão o estado de uma matriz
     * 
     * @param componentes
     *                Matriz a ser impressa.
     * @param passo
     *                Resolução da impressão (quantas casas devem ser puladas
     *                entre uma casa impressa e outra). Resolução máxima = 1,
     *                todas as casas são impressas e o máximo de espaço na tela
     *                é ocupado.
     * @param branco
     *                O valor na matriz componentes que será impresso como
     *                espaço em branco.
     * @param nome
     *                Nome da matriz a ser impressa.
     */
    public static void imprimeMatriz(long[][] componentes, int passo,
	    int branco, String nome) {
	System.out.println("");
	System.out.println("--------\nEstado da matriz " + nome + ":");
	for (int i = 0; i < componentes.length; i = i + passo) {
	    for (int j = 0; j < componentes[0].length; j = j + passo) {
		if (componentes[i][j] == branco)
		    System.out.print("   ");
		else if (componentes[i][j] < 0 || componentes[i][j] > 9)
		    System.out.print(componentes[i][j] + " ");
		else
		    System.out.print(" " + componentes[i][j] + " ");
	    }
	    System.out.println("");
	}
	System.out.println("--------\n");
    }

    /**
     * Imprime na saída padrão o estado de uma matriz binária
     * 
     * @param binaria
     *                Matriz de zeros e uns ( pode representar uma imagem ).
     * @param nome
     *                Para imprimir na saída padrão ( pode ser String vazia ).
     */
    public static void imprimeMatriz(boolean[][] binaria, String nome) {
	if (binaria == null)
	    System.out.println("   -> A matriz " + nome + " é nula!");
	else {
	    int i;
	    int passoHorizontal = binaria.length / 40;
	    int passoVertical = binaria[0].length / 40;

	    System.out.println("");
	    System.out.print("Estado da matriz " + nome + ":\n --");
	    if (passoHorizontal <= 0)
		passoHorizontal = 1;
	    if (passoVertical <= 0)
		passoVertical = 1;
	    for (i = 0; i < binaria.length; i = i + passoHorizontal)
		System.out.print("--");
	    System.out.println("");
	    for (i = 0; i < binaria.length; i = i + passoHorizontal) {
		System.out.print("|");
		for (int j = 0; j < binaria[0].length; j = j + passoVertical)
		    if (binaria[i][j])
			System.out.print(" X");
		    else
			System.out.print("  ");
		System.out.println("|");
	    }
	    System.out.print(" ");
	    for (i = 0; i < binaria.length; i = i + passoHorizontal)
		System.out.print("--");
	    System.out.println("--");
	}
    }

    /**
     * Método para criação rápida de uma matriz booleana
     * 
     * @return Uma matriz de bits pré-definida.
     */
    public static boolean[][] criaMatrizBooleana() {
	boolean X = true;
	boolean _ = false;
	boolean[][] devolucao = { { X, X, X, X, X, X, X, X, X, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, _, _, _, _, _, _, _, _, X },
		{ X, X, X, X, X, X, X, X, X, X }, };

	return devolucao;
    }
}
