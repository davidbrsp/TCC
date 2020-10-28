package medidas;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.Controlador;
import util.ExtratorDeMedidas;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe que representa a escala milímetros por pixel do objeto
 */
public class Escala extends Medida {
    private double escala;

    public Escala() {
	super();
    }

    public double pegaEscala() {
	return this.escala;
    }

    /**
     * Função que extrai a escala, baseado no contorno da imagem da referência
     * de escala
     * 
     * @see ExtratorDeMedidas
     */
    protected double escala(boolean[][] imagemDaRefDeEscala,
	    double raioEmMilimetros) {
	ArrayList<PontoNatural> contornoDaRefDeEscala = ExtratorDeMedidas
		.extraiContorno(imagemDaRefDeEscala);

	if (contornoDaRefDeEscala != null) {
	    PontoDouble centroide = ExtratorDeMedidas
		    .centroide(imagemDaRefDeEscala);
	    double raioEmPixels = ExtratorDeMedidas.distanciaMediaAte(
		    centroide, contornoDaRefDeEscala);

	    // System.out.println( "centroide = " + centroide );
	    // System.out.println( "raioEmMilimetros = " + raioEmMilimetros );
	    // System.out.println( "raioEmPixels = " + raioEmPixels );
	    // System.out.println( "raioEmMilimetros / raioEmPixels = " +
	    // raioEmMilimetros / raioEmPixels );
	    return raioEmMilimetros / raioEmPixels;
	} else
	    return 0;
    }

    @Override
    public void extraiMedidaDeFato() {
	// this.escala = escala( Controlador.getInstance( ).objetoBinarizado,
	// Controlador.RAIO_DA_REFERENCIA_DA_ESCALA );
	this.escala = escala(
		Controlador.getInstance().referenciaDeEscalaBinarizada,
		Controlador.RAIO_DA_REFERENCIA_DA_ESCALA);
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "Escala: " + df.format(this.escala) + " mm/pixel";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.escala = 0;
    }

    @Override
    public String pegaValor() {
	return this.pegaEscala() + " mm/pixel";
    }
}
