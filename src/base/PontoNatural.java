package base;

/**
 * Classe que representa uma posição numa matriz ou imagem. Estas posições (
 * coordenadas ) são sempre inteiras e não-negativas.
 * 
 * @see Ponto
 * @see PontoDouble
 */
public class PontoNatural extends Ponto {
    /**
     * Coordenadas inteiras e não-negativas.
     */
    public int i;
    public int j;

    /**
     * Só permite valores não-negativos. Se algum valor for negativo, o ponto
     * criado é o ( 0, 0 ).
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
     *                Novo valor da coordenada ( a conversão para inteiro é
     *                automática ).
     */
    @Override
    protected void defineI(double i) {
	this.i = (int) i;
    }

    /**
     * Seta o valor da coordenada j.
     * 
     * @param j
     *                Novo valor da coordenada ( a conversão para inteiro é
     *                automática ).
     */
    @Override
    protected void defineJ(double j) {
	this.j = (int) j;
    }

    /**
     * Copia para este objeto os valores do outro ponto recebido como parâmetro.
     * 
     * @param outroPonto
     *                Ponto de onde os valores serão copiados.
     */
    public void copiaValores(PontoNatural outroPonto) {
	this.i = outroPonto.i;
	this.j = outroPonto.j;
    }

    /**
     * Converte as coordenadas do ponto de int para double.
     * 
     * @author David Macedo da Conceição
     * @return Ponto convertido para PontoDouble.
     * @see PontoDouble
     */
    public PontoDouble toPontoDouble() {
	return new PontoDouble((double) this.i, (double) this.j);
    }

}
