package medidas;

import main.Controlador;
import util.ExtratorDeMedidas;
import base.PontoDouble;

/**
 * Classe que representa o centróide de um objeto
 */
public class Centroide extends Medida {
    private PontoDouble centroide;

    public Centroide() {
	super();
    }

    public PontoDouble pegaCentroide() {
	return this.centroide;
    }

    /**
     * Função que extrai o centróide do objeto
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	boolean[][] imagem = Controlador.getInstance().objetoBinarizado;
	this.centroide = ExtratorDeMedidas.centroide(imagem);
    }

    @Override
    public String toString() {
	return "Centróide da imagem: (" + this.centroide + ")";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.centroide = null;
    }

    @Override
    public String pegaValor() {
	return this.pegaCentroide().toString();
    }
}
