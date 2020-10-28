package medidas;

import java.util.ArrayList;

import main.Medidas;
import util.ExtratorDeMedidas;
import util.Fourier;
import base.PontoNatural;

/**
 * Classe que representa a curvatura de um objeto
 */
public class Curvatura extends Medida {
    double[] curvatura;

    public Curvatura() {
	super();
    }

    public double[] pegaCurvatura() {
	return this.curvatura;
    }

    /**
     * Função que extrai a curvatura do objeto baseando-se no contorno
     * 
     * @see ExtratorDeMedidas
     * @see Fourier
     */
    @Override
    protected void extraiMedidaDeFato() {
	Medidas.getInstance().pegaContorno().extraiMedida();
	ArrayList<PontoNatural> contorno = Medidas.getInstance().pegaContorno()
		.pegaContorno();

	int tamanhoContorno = contorno.size();

	// separa o contorno em dois vetores x e y
	double[] x = new double[tamanhoContorno];
	double[] y = new double[tamanhoContorno];

	for (int i = 0; i < tamanhoContorno; i++) {
	    x[i] = contorno.get(i).i;
	    y[i] = contorno.get(i).j;
	}
	this.curvatura = ExtratorDeMedidas.curvatura(x, y);
    }

    @Override
    public String toString() {
	return "Curvatura com " + this.curvatura.length + " pontos";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.curvatura = null;
    }

    @Override
    public String pegaValor() {
	return this.toString();
    }
}
