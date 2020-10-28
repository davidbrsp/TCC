package medidas;

import java.text.DecimalFormat;

import main.Medidas;
import util.ExtratorDeMedidas;

/**
 * Classe que representa a energia de dobramento de um objeto Medida que
 * sintetiza a curvatura
 */
public class EnergiaDeDobramento extends Medida {
    private double energiaDeDobramento;

    public EnergiaDeDobramento() {
	super();
    }

    public double pegaEnergia() {
	return this.energiaDeDobramento;
    }

    /**
     * Função que extrai a energia de dobramento, baseado na curvatura
     * 
     * @see ExtratorDeMedidas
     */
    @Override
    protected void extraiMedidaDeFato() {
	Medidas.getInstance().pegaCurvatura().extraiMedida();
	double[] curv = Medidas.getInstance().pegaCurvatura().pegaCurvatura();
	this.energiaDeDobramento = ExtratorDeMedidas.energiaDeDobramento(curv);
    }

    @Override
    public String toString() {
	DecimalFormat df = new DecimalFormat("0.00");

	return "Energia de dobramento do objeto: "
		+ df.format(this.energiaDeDobramento);
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.energiaDeDobramento = 0;
    }

    @Override
    public String pegaValor() {
	return this.pegaEnergia() + "";
    }
}
