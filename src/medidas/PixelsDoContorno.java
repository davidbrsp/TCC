package medidas;

import java.util.ArrayList;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoNatural;

/**
 * Classe que representa a lista de pontos do contorno de um objeto
 * 
 * @see Medida
 * @see Medidas
 */
public class PixelsDoContorno extends Medida {
    private ArrayList<PontoNatural> contorno;

    public PixelsDoContorno() {
	super();
    }

    /**
     * @return lista de pixels do contorno em ordem
     */
    public ArrayList<PontoNatural> pegaContorno() {
	return this.contorno;
    }

    @Override
    protected void extraiMedidaDeFato() {
	this.contorno = ExtratorDeMedidas.extraiContorno(Controlador
		.getInstance().objetoBinarizado);
    }

    /**
     * @see Controlador
     */
    @Override
    public String toString() {
	return "Contorno com " + this.contorno.size() + " pontos";
    }

    @Override
    public void zeraMedidaEspecifica() {

	this.contorno = null;
    }

    @Override
    public String pegaValor() {
	return this.toString();
    }
}
