package base;

/**
 * Classe que representa uma posi��o numa matriz ou imagem. Estas posi��es (
 * coordenadas ) s�o sempre inteiras e n�o-negativas.
 * 
 * @see Ponto
 * @see PontoDouble
 */
public class PontoNatural extends Ponto {
    /**
     * Coordenadas inteiras e n�o-negativas.
     */
    public int i;
    public int j;

    /**
     * S� permite valores n�o-negativos. Se algum valor for negativo, o ponto
     * criado � o ( 0, 0 ).
     * 
     * @param linha
     *                Coordenada i
     * @param coluna
     *                Coordenada j
     */
    public PontoNatural(int linha, int coluna) {
	if (linha < 0 || coluna < 0) {
	    i = 0;
	    j = 0;
	} else {
	    i = linha;
	    j = coluna;
	}
    }

    /**
     * Pega valor da coordenada i.
     */
    @Override
    protected double pegaI() {
	return this.i;
    }

    /**
     * Pega valor da coordenada j.
     */
    @Override
    protected double pegaJ() {
	return this.j;
    }

    /**
     * Seta o valor da coordenada i.
     * 
     * @param i
     *                Novo valor da coordenada ( a convers�o para inteiro �
     *                autom�tica ).
     */
    @Override
    protected void defineI(double i) {
	this.i = (int) i;
    }

    /**
     * Seta o valor da coordenada j.
     * 
     * @param j
     *                Novo valor da coordenada ( a convers�o para inteiro �
     *                autom�tica ).
     */
    @Override
    protected void defineJ(double j) {
	this.j = (int) j;
    }

    /**
     * Copia para este objeto os valores do outro ponto recebido como par�metro.
     * 
     * @param outroPonto
     *                Ponto de onde os valores ser�o copiados.
     */
    public void copiaValores(PontoNatural outroPonto) {
	this.i = outroPonto.i;
	this.j = outroPonto.j;
    }

    /**
     * Converte as coordenadas do ponto de int para double.
     * 
     * @author David Macedo da Concei��o
     * @return Ponto convertido para PontoDouble.
     * @see PontoDouble
     */
    public PontoDouble toPontoDouble() {
	return new PontoDouble((double) this.i, (double) this.j);
    }

}
