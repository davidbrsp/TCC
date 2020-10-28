package medidas;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;

/**
 * Fun��o que representa a �rea em cent�metros quadrados de um objeto
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
     * Fun��o que calcula a �rea baseada na �rea em pixels e na escala
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
	this.area /= 100.0; // transformando de mm� para cm�
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.area = 0;
    }

    @Override
    public String toString() {
	return "�rea do objeto: " + this.area + " cm�";
    }

    @Override
    public String pegaValor() {
	return this.pegaArea() + " cm�";
    }
}
