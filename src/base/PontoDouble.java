package base;

/**
 * Classe que representa um par ordenado real
 * 
 * @see Ponto
 * @see PontoNatural
 */
public class PontoDouble extends Ponto {
    /**
     * coordenadas
     */
    public double i;
    public double j;

    /**
     * Permite quaisquer valores
     * 
     * @param u
     *                coordenada i
     * @param v
     *                coordenada j
     */
    public PontoDouble(double u, double v) {
	i = u;
	j = v;
    }

    @Override
    protected double pegaI() {
	return this.i;
    }

    @Override
    protected double pegaJ() {
	return this.j;
    }

    @Override
    protected void defineI(double i) {
	this.i = i;
    }

    @Override
    protected void defineJ(double j) {
	this.j = j;
    }

    /**
     * Copia para este objeto os valores do outro ponto recebido como parâmetro.
     * 
     * @author David Macedo da Conceição
     * @param outroPonto
     *                Ponto de onde os valores serão copiados.
     */
    public void copiaValores(PontoDouble outroPonto) {
	this.i = outroPonto.i;
	this.j = outroPonto.j;
    }

    /**
     * Converte as coordenadas do ponto de double para int.
     * 
     * @author David Macedo da Conceição
     * @return Ponto convertido para PontoNatural.
     * @see PontoNatural
     */
    public PontoNatural toPontoNatural() {
	return new PontoNatural((int) this.i, (int) this.j);
    }
}
