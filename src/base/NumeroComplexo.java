package base;

import java.text.DecimalFormat;

/**
 * Classe que representa um número complexo
 */
public class NumeroComplexo {
    /**
     * Parte real do número complexo. Acesso pode ser direto.
     */
    public double parteReal;
    /**
     * Parte imaginária do número complexo. Acesso pode ser direto.
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
     * Pega o módulo do número imaginário, ou a norma dois (euclidiana)
     * 
     * @return raiz quadrada de (parteReal² + parteImaginaria²)
     */
    public double modulo() {
	return Math.sqrt(Math.pow(parteReal, 2) + Math.pow(parteImaginaria, 2));
    }

    /**
     * Devolve um novo objeto representando o conjugado do original
     * 
     * @return novo objeto mantendo a parte real e invertendo o sinal da parte
     *         imaginária
     */
    public NumeroComplexo conjugado() {
	return new NumeroComplexo(this.parteReal, -(this.parteImaginaria));
    }

    /**
     * Soma parte real com parte real e parte imaginária com parte imaginária
     * 
     * @param parcela
     * @return novo objeto representado a soma do objeto original com a parcela
     *         passada como parâmetro
     */
    public NumeroComplexo soma(NumeroComplexo parcela) {
	return new NumeroComplexo(this.parteReal + parcela.parteReal,
		this.parteImaginaria + parcela.parteImaginaria);
    }

    /**
     * Subtrai parte real da parte real e parte imaginária da parte imaginária
     * 
     * @param parcela
     * @return novo objeto representado este objeto - parcela
     */
    public NumeroComplexo subtrai(NumeroComplexo parcela) {
	return new NumeroComplexo(this.parteReal - parcela.parteReal,
		this.parteImaginaria - parcela.parteImaginaria);
    }

    /**
     * Multiplicação de termo a termo
     * 
     * @param fator
     *                número complexo pelo qual multiplicar
     * @return sendo ( a + b * i ) o objeto original e ( c + d * i ) o fator,
     *         executa a multiplicação ( a + b * i ) * ( c + d * i ) e devolve
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
     * Multiplicação por escalar
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
     * Devolve uma cadeia de caracteres representando o número sob a forma a +b *
     * i, onde a e b são colocados com no máximo duas casas decimais.
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
