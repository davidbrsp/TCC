package main;

import java.util.ArrayList;

import medidas.Area;
import medidas.Centroide;
import medidas.ContornoEmCm;
import medidas.Curvatura;
import medidas.DistanciaMediaAteOCentro;
import medidas.EnergiaDeDobramento;
import medidas.Escala;
import medidas.MaiorDiametro;
import medidas.Medida;
import medidas.MedidaDeAssimetria;
import medidas.Perimetro;
import medidas.PixelsDoContorno;
import medidas.Volume;

/**
 * Guarda e extrai medidas. Instância única ( singleton ). Usar o getInstance( )
 * ao invés do construtor. Quando uma medida depende de outra para se extraída,
 * ela mesma pede à instância de Medida que extraia a medida desejada
 */
public class Medidas {
    private static Medidas instancia;

    private Area area;
    private Centroide centroide;
    private PixelsDoContorno contorno;
    private ContornoEmCm contornoEmCm;
    private Curvatura curvatura;
    private EnergiaDeDobramento energiaDeDobramento;
    private Escala escala;
    private MedidaDeAssimetria medidaDeAssimetria;
    private Perimetro perimetro;
    private DistanciaMediaAteOCentro raioMedio;
    private MaiorDiametro tamanhoDoMaiorDiametro;
    // DAVID
    private Volume volumePappus;

    /**
     * Construtor privado da classe. Cria todas as medidas
     * 
     * @see getInstance
     */
    private Medidas() {
	this.area = new Area();
	this.centroide = new Centroide();
	this.contorno = new PixelsDoContorno();
	this.contornoEmCm = new ContornoEmCm();
	this.curvatura = new Curvatura();
	this.energiaDeDobramento = new EnergiaDeDobramento();
	this.escala = new Escala();
	this.medidaDeAssimetria = new MedidaDeAssimetria();
	this.perimetro = new Perimetro();
	this.raioMedio = new DistanciaMediaAteOCentro();
	this.tamanhoDoMaiorDiametro = new MaiorDiametro();
	// DAVID
	this.volumePappus = new Volume();
    }

    /**
     * Esta classe é um singleton, só possui uma instância. Não é possível
     * instanciá-la usando o construtor
     * 
     * @return instância única da classe
     */
    public static Medidas getInstance() {
	if (instancia == null)
	    instancia = new Medidas();
	return instancia;
    }

    /**
     * Volume do objeto corrente
     * 
     * @author David Macedo da Conceicao
     * @return área
     */
    public Volume pegaVolume() {
	return this.volumePappus;
    }

    /**
     * Área do objeto corrente
     * 
     * @return área
     */
    public Area pegaArea() {
	return this.area;
    }

    /**
     * Centróide do objeto corrente
     * 
     * @return centróide
     */
    public Centroide pegaCentroide() {
	return this.centroide;
    }

    /**
     * Lista de pixels do contorno do objeto corrente
     * 
     * @return contorno
     */
    public PixelsDoContorno pegaContorno() {
	return this.contorno;
    }

    /**
     * Curvatura do objeto corrente
     * 
     * @return Curvatura
     */
    public Curvatura pegaCurvatura() {
	return this.curvatura;
    }

    /**
     * Energia de dobramento do objeto corrente
     * 
     * @return Energia de dobramento
     */
    public EnergiaDeDobramento pegaEnergiaDeDobramento() {
	return this.energiaDeDobramento;
    }

    /**
     * Escala atual entre pixels e milímetros
     * 
     * @return escala
     */
    public Escala pegaEscala() {
	return this.escala;
    }

    /**
     * Medida de assimetria do objeto corrente
     * 
     * @return medida de assimetria
     */
    public MedidaDeAssimetria pegaMedidaDeAssimetria() {
	return this.medidaDeAssimetria;
    }

    /**
     * Perímetro do objeto corrente
     * 
     * @return Perímetro
     */
    public Perimetro pegaPerimetro() {
	return this.perimetro;
    }

    /**
     * Meior segmento contido no objeto corrente
     * 
     * @return tamanho do maior segmento contido
     */
    public MaiorDiametro pegaMaiorDiametro() {
	return this.tamanhoDoMaiorDiametro;
    }

    /**
     * Distância média até o centróide do objeto corrente
     * 
     * @return raio médio
     */
    public DistanciaMediaAteOCentro pegaRaioMedio() {
	return this.raioMedio;
    }

    /**
     * Contorno em cm do objeto corrente
     * 
     * @return contorno
     */
    public ContornoEmCm pegaContornoEmCm() {
	return this.contornoEmCm;
    }

    /**
     * Função que zera as medidas e faz com que sejam recalculadas quando
     * acionadas
     */
    public void zeraMedidas() {
	ArrayList<Medida> todas = pegaTodasAsMedidas();

	for (int i = 0; i < todas.size(); i++)
	    if (todas.get(i) != null)
		todas.get(i).zeraMedida();
    }

    /**
     * Função que imprime as medidas e faz com que sejam recalculadas quando
     * acionadas
     * 
     * @author David Macedo da Conceição
     */
    public void printMedidas() {
	ArrayList<Medida> todas = pegaTodasAsMedidas();

	// ArrayList<Medida> todas = Medidas.getInstance()
	// .pegaTodasAsMedidas();
	// ArrayList<String> medidas = new ArrayList<String>(todas
	// .size());

	// for (int i = 0; i < todas.size(); i++) {
	// Medida atual = todas.get(i);
	// atual.extraiMedida();
	// medidas.add(atual.getClass().getSimpleName() + ": "
	// + atual.pegaValor());

	extraiTodasAsMedidas();
	for (int i = 0; i < todas.size(); i++)
	    if (todas.get(i) != null) {
		// todas.get(i).extraiMedida();
		System.out.println(todas.get(i).toString());
	    }
    }

    public void extraiTodasAsMedidas() {
	ArrayList<Medida> todas = pegaTodasAsMedidas();

	for (int i = 0; i < todas.size(); i++)
	    if (todas.get(i) != null) {
		todas.get(i).extraiMedida();
	    }
    }

    // DAVID
    public ArrayList<String> pegaCabecalho() {

	ArrayList<String> cabecalho = new ArrayList<String>();

	cabecalho.add("Arquivo");
	cabecalho.add("Area (cm²)");
	cabecalho.add("Centroide (pixel)");
	cabecalho.add("Contorno (pixel)");
	cabecalho.add("Contorno Em Cm (pixel)");
	cabecalho.add("Curvatura (pixel)");
	cabecalho.add("Energia De Dobramento");
	cabecalho.add("Escala (mm/pixel)");
	cabecalho.add("Medida De Assimetria (cm)");
	cabecalho.add("Perimetro (cm)");
	cabecalho.add("RaioMedio (cm)");
	cabecalho.add("Tamanho Do Maior Diametro (cm)");
	cabecalho.add("Volume (cm³)");

	return cabecalho;
    }

    // DAVID
    /**
     * @return lista os valores numericos de todas as medidas
     */
    public ArrayList<String> pegaTodasAsMedidasNumericas() {

	ArrayList<String> todas = new ArrayList<String>();

	todas.add(Double.toString(this.area.pegaArea()));
	todas.add(this.centroide.pegaCentroide().toString());
	todas.add(Integer.toString(this.contorno.pegaContorno().size()));
	todas.add(Integer.toString(this.contornoEmCm.pegaContorno().size()));
	todas.add(Integer.toString(this.curvatura.pegaCurvatura().length));
	todas.add(Double.toString(this.energiaDeDobramento.pegaEnergia()));
	todas.add(Double.toString(this.escala.pegaEscala()));
	todas.add(Double.toString(this.tamanhoDoMaiorDiametro
		.pegaMaiorDiametro()));
	todas.add(Double.toString(this.medidaDeAssimetria.pegaMedida()));
	todas.add(Double.toString(this.perimetro.pegaPerimetro()));
	todas.add(Double.toString(this.raioMedio.pegaDistanciaMedia()));
	todas.add(Double.toString(this.volumePappus.pegaVolume()));

	return todas;
    }

    /**
     * @return lista de todas as medidas
     */
    public ArrayList<Medida> pegaTodasAsMedidas() {
	ArrayList<Medida> todas = new ArrayList<Medida>();

	todas.add(this.area);
	todas.add(this.centroide);
	todas.add(this.contorno);
	todas.add(this.contornoEmCm);
	todas.add(this.curvatura);
	todas.add(this.energiaDeDobramento);
	todas.add(this.escala);
	todas.add(this.medidaDeAssimetria);
	todas.add(this.perimetro);
	todas.add(this.raioMedio);
	todas.add(this.tamanhoDoMaiorDiametro);
	todas.add(this.volumePappus);

	// for (int i = 0; i < todas.size(); i++)
	// if (todas.get(i) != null)
	// todas.get(i).extraiMedida();

	return todas;
    }
}