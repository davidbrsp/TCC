package medidas;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;

/**
 * Função que representa a área em centímetros quadrados de um objeto
 */
public class Area extends Medida {
    private double area;

    public Area() {
	super();
    }

    public double pegaArea() {
	return this.area;
    }

    /**
     * Função que calcula a área baseada na área em pixels e na escala
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	long area = ExtratorDeMedidas
		.area(Controlador.getInstance().objetoBinarizado);

	Medidas.getInstance().pegaEscala().extraiMedida();
	this.area = Math
		.pow(Medidas.getInstance().pegaEscala().pegaEscala(), 2)
		* ((double) area);
	this.area /= 100.0; // transformando de mm² para cm²
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.area = 0;
    }

    @Override
    public String toString() {
	return "Área do objeto: " + this.area + " cm²";
    }

    @Override
    public String pegaValor() {
	return this.pegaArea() + " cm²";
    }
}
