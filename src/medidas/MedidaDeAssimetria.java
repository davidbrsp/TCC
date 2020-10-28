package medidas;

import java.text.DecimalFormat;

import main.Controlador;
import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoDouble;

/**
 * Classe que representa a medida de assimetria de um objeto em centímetros
 * quadrados
 * 
 * @see Medidas
 * @see Medida
 */
public class MedidaDeAssimetria extends Medida {
    private double medidaDeAssimetria;
    private double[] vetorPrimario;
    private double[] vetorSecundario;

    public MedidaDeAssimetria() {
	super();
    }

    /**
     * @return medida de assimetria em centímetros quadrados
     * @see extraiMedida()
     */
    public double pegaMedida() {
	return this.medidaDeAssimetria;
    }

    public double[] pegaVetorPrimario() {
	return this.vetorPrimario;
    }

    public double[] pegaVetorSecundario() {
	return this.vetorSecundario;
    }

    /**
     * Extrai medida com base na escala, no centróide e no eixo secundário do
     * objeto
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	boolean[][] imagem = Controlador.getInstance().objetoBinarizado;

	Medidas.getInstance().pegaEscala().extraiMedida();
	double escala = Medidas.getInstance().pegaEscala().pegaEscala();
	Medidas.getInstance().pegaCentroide().extraiMedida();
	PontoDouble centroide = Medidas.getInstance().pegaCentroide()
		.pegaCentroide();
	this.medidaDeAssimetria = ExtratorDeMedidas.assimetria(imagem,
		this.vetorPrimario, this.vetorSecundario, centroide);
	this.medidaDeAssimetria *= Math.pow(escala / 10.0, 2); // divisão por
	// dez para
	// transformar
	// de mm para cm
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "Medida de assimetria: " + df.format(this.medidaDeAssimetria)
		+ " cm";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.medidaDeAssimetria = 0;
	this.vetorPrimario = new double[2];
	this.vetorSecundario = new double[2];
    }

    @Override
    public String pegaValor() {
	return this.pegaMedida() + " cm";
    }
}
