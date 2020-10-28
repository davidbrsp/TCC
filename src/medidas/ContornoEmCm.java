package medidas;

import java.util.ArrayList;

import main.Medidas;
import util.ExtratorDeMedidas;
import base.PontoDouble;
import base.PontoNatural;

/**
 * Classe que representa o contorno em centímetros de um objeto
 */
public class ContornoEmCm extends Medida {
    private ArrayList<PontoDouble> contorno;

    public ContornoEmCm() {
	super();
    }

    public ArrayList<PontoDouble> pegaContorno() {
	return this.contorno;
    }

    /**
     * Função que extrai o contorno em cm, baseado na lista de pixels do
     * contorno e na escala
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

	this.contorno = new ArrayList<PontoDouble>();
	for (int i = 0; i < contorno.size(); i++)
	    this.contorno.add(transformaEmCm(contorno.get(i), escala / 10.0));
	// divisão por dez para transformar de mm para cm
    }

    private PontoDouble transformaEmCm(PontoNatural ponto, double escalaCmPorPx) {
	return new PontoDouble(ponto.i * escalaCmPorPx, ponto.j * escalaCmPorPx);
    }

    @Override
    public String toString() {
	return "Contorno em centímetros com " + this.contorno.size()
		+ " pontos";
    }

    @Override
    public void zeraMedidaEspecifica() {
	this.contorno = null;
    }

    @Override
    public String pegaValor() {
	return this.toString();
    }
}
