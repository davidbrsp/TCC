package medidas;

import java.text.DecimalFormat;
import java.util.ArrayList;

import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoNatural;

/**
 * Classe que representa a maior corda de um objeto, ou o maior segmento
 * inteiramente contido no objeto
 */
public class MaiorDiametro extends Medida {
    private double maiorDiametro;
    private PontoNatural umExtremo;
    private PontoNatural outroExtremo;

    public MaiorDiametro() {
	super();
    }

    public double pegaMaiorDiametro() {
	return this.maiorDiametro;
    }

    public PontoNatural pegaUmExtremo() {
	return this.umExtremo;
    }

    public PontoNatural pegaOutroExtremo() {
	return this.outroExtremo;
    }

    /**
     * Função que extrai a maior corda em cm, baseado na lista de pixels do
     * contorno e na escala
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	Medidas.getInstance().pegaEscala().extraiMedida();

	double escala = Medidas.getInstance().pegaEscala().pegaEscala();

	Medidas.getInstance().pegaContorno().extraiMedida();

	ArrayList<PontoNatural> contorno = Medidas.getInstance().pegaContorno()
		.pegaContorno();

	this.umExtremo = new PontoNatural(0, 0);
	this.outroExtremo = new PontoNatural(0, 0);
	this.maiorDiametro = ExtratorDeMedidas.maiorDiametro(contorno,
		umExtremo, outroExtremo);
	// divisão por dez para transformar de mm para cm
	this.maiorDiametro *= escala / 10.0;
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "O maior segmento inteiramente contido no objeto mede "
		+ df.format(this.maiorDiametro) + " cm";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.maiorDiametro = 0;
	this.umExtremo = new PontoNatural(0, 0);
	this.outroExtremo = new PontoNatural(0, 0);
    }

    @Override
    public String pegaValor() {
	return this.pegaMaiorDiametro() + " cm";
    }
}
