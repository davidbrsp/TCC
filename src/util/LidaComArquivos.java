package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.GUI;

/**
 * Classe responsável por abrir e salvar arquivos
 */
public class LidaComArquivos {

    public static String nomeArquivoDeEntrada = new String();
    public static ArrayList<String> nomesDosArquivoDeEntrada = new ArrayList<String>();
    public static String nomeDiretorioDeEntrada = new String();

    /**
     * Exibe um navegador para que o usuário escolha um arquivo de imagem e abre
     * o arquivo escolhido
     * 
     * @param gui
     *                interface sobre a qual mostrar o navegador e as possíveis
     *                mensagens de erro
     * @return uma imagem escolhida pelo usuário
     */
    public static BufferedImage abreImagem(GUI gui) {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setAccessory(new ImagePreview(fileChooser));
	// lista os formatos para leitura de imagem disponiveis
	String[] formatos = ImageIO.getReaderFormatNames();
	FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
		"Image Files", formatos);
	BufferedImage imagem = null;

	fileChooser.setFileFilter(filter1);
	fileChooser.setCurrentDirectory(new File("."));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    File arquivoEntrada = fileChooser.getSelectedFile();
	    String inFileName = arquivoEntrada.getName();
	    nomeArquivoDeEntrada = inFileName;
	    try {
		imagem = ImageIO.read(arquivoEntrada);
		if (imagem == null)
		    gui.exibeErro("O arquivo " + inFileName
			    + " não tem um formato de imagem válido");
		else
		    gui.alteraTituloDaJanela("Examinando ovos: " + inFileName);
	    } catch (IOException ioe) {
		ioe.printStackTrace(System.err);
		gui.exibeErro("Erro ao ler o arquivo " + inFileName);
	    }
	}
	return imagem;
    }

    /**
     * Salva uma imagem binária no formato pgm ASCII
     * 
     * @param arquivo
     *                a ser escrito
     * @param identificacao
     *                que aparecerá dentro de um comentário no arquivo
     * @param imagem
     *                imagem binária a ser gravada
     * @param gui
     *                interface sobre a qual exibir mensagens
     * @param limiar
     *                limiar usado na binarização para constar também no
     *                comentário do arquivo
     */
    public static void salvaImagemBinaria(File arquivo, String identificacao,
	    boolean[][] imagem, GUI gui, int limiar) {
	if (arquivo == null)
	    gui
		    .exibeErro("Erro! A imagem não foi salva pois o arquivo não existe.");
	else {
	    try {
		FileWriter escritor = new FileWriter(arquivo);

		escritor.write("P1\n");
		escritor.write("#imagem binária correspondente ao ovo "
			+ identificacao + " com limiar de " + limiar + "\n");
		escritor.write(imagem[0].length + " " + imagem.length + "\n");
		for (int i = 0; i < imagem.length; i++)
		    for (int j = 0; j < imagem[0].length; j++)
			if (imagem[i][j])
			    escritor.write("1\n");
			else
			    escritor.write("0\n");
		escritor.close();
	    } catch (IOException ioe) {
		ioe.printStackTrace(System.err);
		gui.exibeErro("Não consegui escrever no arquivo \""
			+ arquivo.getName() + "\".");
	    }
	}
    }

    /**
     * Exibe um navegador para que o usuário escolha um arquivo qualquer
     * (existente ou não) e abre o arquivo escolhido
     * 
     * @param gui
     *                interface sobre a qual mostrar o navegador e as possíveis
     *                mensagens de erro
     * @return um arquivo escolhido pelo usuário
     */
    public static File abreArquivo(GUI gui) {
	File arquivo = null;
	JFileChooser fileChooser = new JFileChooser();

	fileChooser.setCurrentDirectory(new File("."));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    arquivo = fileChooser.getSelectedFile();
	    if (arquivo.exists()) {
		String pergunta = "O arquivo \"" + arquivo.getName()
			+ "\" será sobrescrito. Deseja continuar?";
		String titulo = "Esse arquivo já existe!";
		Object[] opcoes = { "sim", "não", "cancelar" };
		int escolha = gui.exibeEscolha(pergunta, titulo, opcoes);

		if (escolha == 0)
		    return arquivo;
	    } else
		return null;
	}
	return null;
    }

    /**
     * Exibe um navegador para que o usuário escolha um diretorio
     * 
     * @author David Macedo da Conceição
     * @param gui
     *                interface sobre a qual mostrar o navegador e as possíveis
     *                mensagens de erro
     * @return uma lista de nomes de arquivos de imagem do diretorio escolhido
     *         pelo usuário
     */
    public static ArrayList<String> abreDiretorio(GUI gui) {
	File diretorio;
	JFileChooser fileChooser = new JFileChooser();
	ArrayList<String> lista = new ArrayList<String>();
	/*
	 * // lista os formatos para leitura de imagem disponiveis String[]
	 * formatos = ImageIO.getReaderFormatNames(); for (int i = 0; i <
	 * formatos.length; i++) System.out.println(formatos[i]);
	 * 
	 * FileNameExtensionFilter filter1 = new FileNameExtensionFilter( "Image
	 * Files", formatos); fileChooser.setFileFilter(filter1);
	 */
	// retirado de:
	// http://www.arquivodecodigos.net/dicas/java-usando-jfilechooser-para-escolher-um-diretorio-a-partir-de-um-jframe-485.html
	// restringe a amostra a diretorios apenas
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	fileChooser.setCurrentDirectory(new File("."));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    diretorio = fileChooser.getSelectedFile();
	    // retirado de:
	    // http://www.ime.usp.br/~cef/mac323/eps/TestaArquivo.java
	    if (diretorio.isDirectory()) {
		nomeDiretorioDeEntrada = diretorio.getName();
		// lista os formatos para leitura de imagem disponiveis
		final String[] formatos = ImageIO.getReaderFormatNames();
		// retirado de:
		// http://javafree.uol.com.br/viewtopic.jbb?t=850121
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
			boolean aceita = false;
			for (int i = 0; i < formatos.length; i++)
			    if (name.endsWith("." + formatos[i])) {
				aceita = true;
				break;
			    }
			return aceita;// ou
			// name.matches(regex);
		    }
		};
		/**
		 * o metodo list() recebe um filtro e devolve um vetor de
		 * Strings, com os nomes dos arquivos neste diretorio que passam
		 * pelo filtro
		 */
		lista.add(diretorio.getPath());
		String[] local = diretorio.list(filter);
		for (int i = 0; i < local.length; i++)
		    lista.add(local[i]);
		System.out.println(lista);
		return lista;
	    } else
		return null;
	} else
	    return null;
    }

    /**
     * Exibe um navegador para que o usuário escolha um diretorio
     * 
     * @author David Macedo da Conceição
     * @param gui
     *                interface sobre a qual mostrar o navegador e as possíveis
     *                mensagens de erro
     * @return uma lista de arquivos de imagem do diretorio escolhido pelo
     *         usuário
     */
    public static ArrayList<File> abreImagensDoDiretorio(GUI gui) {
	File diretorio;
	JFileChooser fileChooser = new JFileChooser();
	ArrayList<BufferedImage> listaDeImagens = new ArrayList<BufferedImage>();
	ArrayList<File> arquivos = new ArrayList<File>();

	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	fileChooser.setCurrentDirectory(new File("."));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    diretorio = fileChooser.getSelectedFile();
	    if (diretorio.isDirectory()) {
		nomeDiretorioDeEntrada = diretorio.getPath();
		final String[] formatos = ImageIO.getReaderFormatNames();
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
			boolean aceita = false;
			for (int i = 0; i < formatos.length; i++)
			    if (name.endsWith("." + formatos[i])) {
				aceita = true;
				break;
			    }
			return aceita;
		    }
		};
		/**
		 * o metodo list() recebe um filtro e devolve um vetor de
		 * Strings, com os nomes dos arquivos neste diretorio que passam
		 * pelo filtro
		 */
		String[] local = diretorio.list(filter);
		nomesDosArquivoDeEntrada.clear();
		arquivos.clear();
		listaDeImagens.clear();
		for (int i = 0; i < local.length; i++) {
		    arquivos.add(new File(nomeDiretorioDeEntrada
			    + File.separator + local[i]));
		    String inFileName = arquivos.get(i).getName();
		    nomesDosArquivoDeEntrada.add(inFileName);
		}
		return arquivos;
	    } else
		return null;
	} else
	    return null;
    }

    /**
     * Exibe um navegador para que o usuário escolha um arquivo qualquer
     * (existente ou não) e salva o arquivo escolhido, por padrao escolhe o nome
     * do arquivo da imagem que está sendo analisada
     * 
     * @author David Macedo da Conceição
     * @param gui
     *                interface sobre a qual mostrar o navegador e as possíveis
     *                mensagens de erro
     * @param name
     *                Nome do arquivo que se deseja salvar
     * @param extension
     *                Extensao que se deseja salvar o arquivo
     * @return um arquivo escolhido pelo usuário
     */
    public static File salvaArquivo(GUI gui, String name, String extension) {
	File arquivo = null;
	JFileChooser fileChooser = new JFileChooser();

	fileChooser.setCurrentDirectory(new File("."));
	fileChooser.setSelectedFile(new File(name + extension));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    arquivo = fileChooser.getSelectedFile();
	    if (arquivo.exists()) {
		String pergunta = "O arquivo \"" + arquivo.getName()
			+ "\" será sobrescrito. Deseja continuar?";
		String titulo = "Esse arquivo já existe!";
		Object[] opcoes = { "sim", "não", "cancelar" };
		int escolha = gui.exibeEscolha(pergunta, titulo, opcoes);

		if (escolha == 0)
		    return arquivo;
	    } else
		return arquivo;
	}
	return null;
    }

    /**
     * Função que persiste um ArrayList
     * 
     * @param array
     *                a ser gravado
     * @param arquivo
     *                no qual gravar
     * @param gui
     *                sobre a qual exibir mensagens
     * @param nomeDoArray
     *                para constar no comentário do arquivo
     * @param generoEnumero
     *                correspondente ao nome do array, para exibir a mensagem
     *                corretamente na GUI. Pode ser um dos seguintes valores:
     *                "a", "o", "as", "os"
     */
    public static void salvaArray(ArrayList<? extends Object> array,
	    File arquivo, GUI gui, String nomeDoArray, String generoEnumero) {
	try {
	    FileWriter escritor = new FileWriter(arquivo);
	    int n = array.size();

	    for (int i = 0; i < n; i++)
		escritor.write(array.get(i) + "\n");
	    escritor.close();
	    gui.exibeMensagem(nomeDoArray + " escrit" + generoEnumero
		    + " com sucesso no arquivo \"" + arquivo.getName() + "\".");
	} catch (IOException ioexception) {
	    String textoDoErro = "Não consegui escrever no arquivo \""
		    + arquivo.getName() + "\".";
	    gui.exibeErro(textoDoErro);
	    System.err.println(textoDoErro);
	    ioexception.printStackTrace(System.err);
	}
    }

    // DAVID
    /**
     * Função que persiste um ArrayList para o formato CSV.
     * 
     * @author David Macedo da Conceição
     * @param arrays
     *                a serem gravados.
     * @param arquivo
     *                no qual gravar.
     * @param gui
     *                sobre a qual exibir mensagens.
     * @param nomeDoArray
     *                para constar no comentário do arquivo.
     * @param generoEnumero
     *                correspondente ao nome do array, para exibir a mensagem
     *                corretamente na GUI. Pode ser um dos seguintes valores:
     *                "a", "o", "as", "os".
     */
    public static void salvaArraysCSV(ArrayList<ArrayList<String>> arrays,
	    File arquivo, GUI gui, String nomeDoArray, String generoEnumero) {
	try {
	    FileWriter escritor = new FileWriter(arquivo);
	    int n = arrays.size();
	    Object elemento;

	    for (int i = 0; i < n; i++) {
		int m = arrays.get(i).size();

		for (int j = 0; j < m; j++) {
		    elemento = arrays.get(i).get(j);
		    escritor.write(elemento + (j == m - 1 ? "" : ","));
		}
		escritor.write("\n");
	    }
	    escritor.close();
	    gui.exibeMensagem(nomeDoArray + " escrit" + generoEnumero
		    + " com sucesso no arquivo \"" + arquivo.getName() + "\".");
	} catch (IOException ioexception) {
	    String textoDoErro = "Não consegui escrever no arquivo \""
		    + arquivo.getName() + "\".";
	    gui.exibeErro(textoDoErro);
	    System.err.println(textoDoErro);
	    ioexception.printStackTrace(System.err);
	}
    }

    /**
     * Função que persiste um vetor de reais
     * 
     * @param vetor
     *                a ser gravado
     * @param arquivo
     *                no qual gravar
     * @param gui
     *                sobre a qual exibir mensagens
     * @param nomeDoArray
     *                para constar no comentário do arquivo
     * @param generoEnumero
     *                correspondente ao nome do array, para exibir a mensagem
     *                corretamente na GUI. Pode ser um dos seguintes valores:
     *                "a", "o", "as", "os"
     */
    public static void salvaVetor(double[] vetor, File arquivo, GUI gui,
	    String nomeDoArray, String generoEnumero) {
	try {
	    FileWriter escritor = new FileWriter(arquivo);
	    int n = vetor.length;

	    for (int i = 0; i < n; i++)
		escritor.write(vetor[i] + "\n");
	    escritor.close();
	    gui.exibeMensagem(nomeDoArray + " escrit" + generoEnumero
		    + " com sucesso no arquivo \"" + arquivo.getName() + "\".");
	} catch (IOException ioexception) {
	    String textoDoErro = "Não consegui escrever no arquivo \""
		    + arquivo.getName() + "\".";

	    gui.exibeErro(textoDoErro);
	    System.err.println(textoDoErro);
	    ioexception.printStackTrace(System.err);
	}
    }
}
