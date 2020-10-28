package base;

import java.text.DecimalFormat;

/**
 * Classe que representa um n�mero complexo
 */
public class NumeroComplexo {
    /**
     * Parte real do n�mero complexo. Acesso pode ser direto.
     */
    public double parteReal;
    /**
     * Parte imagin�ria do n�mero complexo. Acesso pode ser direto.
     */
    public double parteImaginaria;

    /**
     * Construtor da classe. Aceita quaisquer valores
     * 
     * @param parteReal
     * @param parteImaginaria
     */
    public NumeroComplexo(double parteReal, double parteImaginaria) {
	this.parteReal = parteReal;
	this.parteImaginaria = parteImaginaria;
    }

    /**
     * Pega o m�dulo do n�mero imagin�rio, ou a norma dois (euclidiana)
     * 
     * @return raiz quadrada de (parteReal� + parteImaginaria�)
     */
    public double modulo() {
	return Math.sqrt(Math.pow(parteReal, 2) + Math.pow(parteImaginaria, 2));
    }

    /**
     * Devolve um novo objeto representando o conjugado do original
     * 
     * @return novo objeto mantendo a parte real e invertendo o sinal da parte
     *         imagin�ria
     */
    public NumeroComplexo conjugado() {
	return new NumeroComplexo(this.parteReal, -(this.parteImaginaria));
    }

    /**
     * Soma parte real com parte real e parte imagin�ria com parte imagin�ria
     * 
     * @param parcela
     * @return novo objeto representado a soma do objeto original com a parcela
     *         passada como par�metro
     */
    public NumeroComplexo soma(NumeroComplexo parcela) {
	return new NumeroComplexo(this.parteReal + parcela.parteReal,
		this.parteImaginaria + parcela.parteImaginaria);
    }

    /**
     * Subtrai parte real da parte real e parte imagin�ria da parte imagin�ria
     * 
     * @param parcela
     * @return novo objeto representado este objeto - parcela
     */
    public NumeroComplexo subtrai(NumeroComplexo parcela) {
	return new NumeroComplexo(this.parteReal - parcela.parteReal,
		this.parteImaginaria - parcela.parteImaginaria);
    }

    /**
     * Multiplica��o de termo a termo
     * 
     * @param fator
     *                n�mero complexo pelo qual multiplicar
     * @return sendo ( a + b * i ) o objeto original e ( c + d * i ) o fator,
     *         executa a multiplica��o ( a + b * i ) * ( c + d * i ) e devolve
     *         um novo objeto representando ( a * c - b * d ) + ( a * d + b * c ) *
     *         i
     */
    public NumeroComplexo multiplica(NumeroComplexo fator) {
	double parteReal = this.parteReal * fator.parteReal
		- this.parteImaginaria * fator.parteImaginaria;
	double parteImaginaria = this.parteReal * fator.parteImaginaria
		+ this.parteImaginaria * fator.parteReal;
	return new NumeroComplexo(parteReal, parteImaginaria);
    }

    /**
     * Multiplica��o por escalar
     * 
     * @param fator
     *                escalar
     * @return sendo ( a + b * i ) o objeto original, devolve um novo objeto
     *         representando fator * a + fator * b * i
     */
    public NumeroComplexo multiplicaPorEscalar(double fator) {
	return new NumeroComplexo(this.parteReal *= fator,
		this.parteImaginaria *= fator);
    }

    /**
     * Devolve uma cadeia de caracteres representando o n�mero sob a forma a +b *
     * i, onde a e b s�o colocados com no m�ximo duas casas decimais.
     */
    public String toString() {
	DecimalFormat formatador = new DecimalFormat("0.00");
	String saidaDaParteReal = formatador.format(this.parteReal);
	String saidaDaParteImaginaria = formatador.format(this.parteImaginaria);
	String ret;
	if (this.parteReal < 0)
	    ret = saidaDaParteReal;
	else
	    ret = " " + saidaDaParteReal;
	if (this.parteImaginaria >= 0)
	    ret = ret + " +" + saidaDaParteImaginaria + "i";
	else
	    ret = ret + " " + saidaDaParteImaginaria + "i";
	return ret;
    }
}
