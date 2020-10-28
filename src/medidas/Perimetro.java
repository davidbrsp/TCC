package medidas;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoNatural;

/**
 * Classe que representa o perímetro de um objeto em centímetros
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
     * Extrai o perímetro com base no contorno e na escala
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
	this.perimetro = ((double) ExtratorDeMedidas.perimetro(contorno) * escala) / 10.0; // divisão
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

	return "Perímetro do objeto: " + df.format(this.perimetro) + " cm";
    }

    /**
     * Devolve o valor do perímetro em centímetros
     */
    @Override
    public String pegaValor() {
	return this.pegaPerimetro() + " cm";
    }
}
