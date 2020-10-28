package medidas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Controlador;
import main.GUI;
import main.Medidas;

/**
 * Classe que representa uma medida no software
 * 
 * @see Medidas
 * @see Area
 * @see Centroide
 * @see ContornoEmCm
 * @see Curvatura
 * @see DistanciaMediaAteOCentro
 * @see EnergiaDeDobramento
 * @see Escala
 * @see MaiorDiametro
 * @see MedidaDeAssimetria
 * @see Perimetro
 * @see PixelsDoContorno
 * @see Volume
 */
public abstract class Medida implements ActionListener {
    protected boolean medidaJaExtraida;

    public Medida() {
	zeraMedida();
    }

    /**
     * Fun��o usada para zerar a medida e para que ela seja recalculada na
     * pr�xima em que for invocada
     */
    public void zeraMedida() {
	this.medidaJaExtraida = false;
	this.zeraMedidaEspecifica();
    }

    /**
     * Esta fun��o deve zerar a medida real
     */
    protected abstract void zeraMedidaEspecifica();

    /**
     * Quando o menu ao qual esta medida est� associada � acionado, extrai a
     * medida (desde que n�o tenha sido j� extra�da) e manda o Controlador do
     * software tomar uma atitude (ex: exibir na tela a medida)
     * 
     * @see Controlador
     * @see GUI.adicionaOuvidoresAosItensDoMenu
     */
    public void actionPerformed(ActionEvent e) {
	if (Controlador.getInstance().objetoBinarizado != null)
	    this.extraiMedida();
	Controlador.getInstance().tomaUmaAtitude(this);
    }

    /**
     * Fun��o que extrai a medida (se ela ainda n�o tiver sido extra�da)
     */
    public void extraiMedida() {
	if (!this.medidaJaExtraida) {
	    this.extraiMedidaDeFato();
	    this.medidaJaExtraida = true;
	}
    }

    /**
     * Fun��o que realmente extrai a medida ( implementada nas filhas )
     */
    protected abstract void extraiMedidaDeFato();

    public abstract String toString();

    public abstract String pegaValor();
}
