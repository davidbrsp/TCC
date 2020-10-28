package base;

/**
 * Classe abstrata que representa um ponto com duas coordenadas: (i,j)
 * 
 * @see PontoDouble
 * @see PontoNatural
 */
public abstract class Ponto {
    /**
     * É usado o tipo primitivo double pois ele "contém" o tipo int
     * 
     * @return devolve a primeira coordenada
     */
    protected abstract double pegaI();

    /**
     * @return devolve a segunda coordenada
     */
    protected abstract double pegaJ();

    /**
     * Define a primeira coordenada
     */
    protected abstract void defineI(double i);

    /**
     * Define a segunda coordenada
     */
    protected abstract void defineJ(double j);

    /**
     * @param outro
     *                um ponto qualquer
     * @return devolve true se e somente se ambos os pontos têm as mesmas
     *         coordenadas
     */
    public boolean igual(Ponto outro) {
	return this.pegaI() == outro.pegaI() && this.pegaJ() == outro.pegaJ();
    }

    /**
     * Soma a este objeto as coordenadas do outro ponto passado como parâmetro
     * 
     * @param outroPonto
     *                um ponto a ser somado
     */
    public void soma(Ponto outroPonto) {
	this.defineI(this.pegaI() + outroPonto.pegaI());
	this.defineJ(this.pegaJ() + outroPonto.pegaJ());
    }

    /**
     * Subtrai às coordenadas deste objeto as coordenadas do outro ponto passado
     * como parâmetro
     * 
     * @param outroPonto
     *                um ponto a subtrair
     */
    public void subtrai(Ponto outroPonto) {
	this.defineI(this.pegaI() - outroPonto.pegaI());
	this.defineJ(this.pegaJ() - outroPonto.pegaJ());
    }

    /**
     * Devolve a distância euclidiana ( também conhecida como norma 2 ) entre
     * este objeto e o ponto passado como parâmetro.
     * 
     * @param ponto
     *                Um ponto ( c, d ) qualquer.
     * @return sendo este objeto representado por ( a, b ) e o parâmetro por (
     *         c, d ), devolve a raiz quadrada de ( ( a - c )² + ( b - d )² )
     */
    public double normaEuclidiana(Ponto ponto) {
	double dist;
	dist = Math.pow(this.pegaI() - ponto.pegaI(), 2)
		+ Math.pow(this.pegaJ() - ponto.pegaJ(), 2);
	dist = Math.sqrt(dist);
	// System.out.println( "| " + this + ", " + ponto + " | = " + dist );
	return dist;
    }

    /**
     * @return Devolve uma cadeia de caracteres que representa este objeto sob a
     *         forma ( i, j )
     */
    public String toStringCartesiana() {
	return "(" + this.pegaI() + "," + this.pegaJ() + ")";
    }

    /**
     * @return Devolve uma cadeia de caracteres que representa este objeto sob a
     *         forma i, j
     */
    public String toString() {
	return this.pegaI() + " " + this.pegaJ();
    }
}
