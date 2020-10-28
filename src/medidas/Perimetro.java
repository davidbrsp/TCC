package medidas;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoNatural;

/**
 * Classe que representa o per�metro de um objeto em cent�metros
 * 
 * @see Medidas
 * @see Medida
 */
public class Perimetro extends Medida {
    private double perimetro;

    public double pegaPerimetro() {
	return this.perimetro;
    }

    public Perimetro() {
	super();
    }

    /**
     * Extrai o per�metro com base no contorno e na escala
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	Medidas.getInstance().pegaContorno().extraiMedida();
	ArrayList<PontoNatural> contorno = Medidas.getInstance().pegaContorno()
		.pegaContorno();
	Medidas.getInstance().pegaEscala().extraiMedida();
	double escala = Medidas.getInstance().pegaEscala().pegaEscala();
	this.perimetro = ((double) ExtratorDeMedidas.perimetro(contorno) * escala) / 10.0; // divis�o
											    // por
											    // dez
											    // para
											    // transformar
											    // de
											    // mm
											    // para
											    // cm
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.perimetro = 0;
    }

    /**
     * @see Controlador
     */
    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "Per�metro do objeto: " + df.format(this.perimetro) + " cm";
    }

    /**
     * Devolve o valor do per�metro em cent�metros
     */
    @Override
    public String pegaValor() {
	return this.pegaPerimetro() + " cm";
    }
}
