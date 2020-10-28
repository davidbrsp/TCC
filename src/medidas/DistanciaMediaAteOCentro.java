package medidas;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe que representa o Raio m�dio de um objeto
 */
public class DistanciaMediaAteOCentro extends Medida {
    private double distanciaMedia;

    public DistanciaMediaAteOCentro() {
	super();
    }

    public double pegaDistanciaMedia() {
	return this.distanciaMedia;
    }

    /**
     * Fun��o que extrai o raio m�dio do objeto em cm, baseando-se na escala
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	Medidas.getInstance().pegaCentroide().extraiMedida();
	PontoDouble centroide = Medidas.getInstance().pegaCentroide()
		.pegaCentroide();
	Medidas.getInstance().pegaEscala().extraiMedida();
	double escala = Medidas.getInstance().pegaEscala().pegaEscala();
	ArrayList<PontoNatural> contorno = Medidas.getInstance().pegaContorno()
		.pegaContorno();
	this.distanciaMedia = ExtratorDeMedidas.distanciaMediaAte(centroide,
		contorno);
	this.distanciaMedia *= escala / 10.0; // divis�o por dez para
	// transformar de mm para cm
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "Dist�ncia m�dia at� o centro de massa: "
		+ df.format(this.distanciaMedia) + " cm";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.distanciaMedia = 0;
    }

    @Override
    public String pegaValor() {
	return this.pegaDistanciaMedia() + " cm";
    }
}
