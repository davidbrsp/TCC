package util;

import java.util.Vector;

/**
 * Classe que representa uma fila, tamb�m conhecida como FIFO (First In, First
 * Out) Consiste em uma lista que devolve primeiro os elementos que entraram
 * primeiro nela
 * 
 * @param <Type>
 *                tipo do elemento
 * @see PreProcessador
 */
public class Fila<Type> {
    private Vector<Type> fila = null;

    /**
     * Construtor da classe
     */
    public Fila() {
	fila = new Vector<Type>();
    }

    /**
     * M�todo para enfileirar
     * 
     * @param novo
     *                elemento a entrar na fila
     */
    public void insere(Type novo) {
	fila.add(novo);
    }

    /**
     * Para verificar se h� algum elemento na fila
     * 
     * @return devolve true se e somente se a fila est� vazia
     */
    public boolean filaVazia() {
	return fila.isEmpty();
    }

    /**
     * Pega e remove o pr�ximo da fila
     * 
     * @return devolve null se a fila estiver vazia ou o elemento mais antigo se
     *         existir
     */
    public Type pegaPrimeiroDaFila() {
	if (filaVazia())
	    return null;
	return fila.remove(0);
    }

    /**
     * Pega o pr�ximo da fila sem remover
     * 
     * @return devolve null se a fila estiver vazia ou o elemento mais antigo se
     *         existir
     */
    public Type consultaPrimeiroDaFila() {
	if (filaVazia())
	    return null;
	return (Type) fila.get(0);
    }
}
