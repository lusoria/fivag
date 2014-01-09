package fivagest.model.report;

import java.util.ArrayList;

import fivagest.model.Cliente;
import fivagest.model.Pratica;
import fivagest.util.Data;


/**
 * <p>Un report è genericamente un riassunto della situazione di alcune o tutte le pratiche di un singolo cliente.</p>
 * 
 * <a>Tipicamente un report prende in esame tutte o solo alcune pratiche di un cliente e ne calcola ad esempio l'importo totale dovuto,
 * o la percentuale del lavoro svolto su quello ancora da svolgere, o altre cose.</a>
 * 
 * <p>Caratteristica fondamentale di qualsiasi Report è che vengono salvati nel DB.</p> 
 * 
 * <p>E' implementato come una classe astratta che definisce un modello per tutti le possibili tipologie di report che verranno richiesti
 * dall'applicazione.
 * Questi possono essere un semplice riassunto visivo a schermo, o un "promemoria di pagamento" informale, una fattura proforma, ecc...</p>

 * @author nico
 *
 */
public abstract class Report {

	protected int id;						// identificativo nel DB
	protected Data creazione;				// timestamp del momento in cui viene creato il Report
	protected Cliente cliente;				// cliente oggetto dell'indagine del Report
	protected ArrayList<Pratica> pratiche;	// Lista delle pratiche (salvate nel db!) del cliente prese in esame dal Report
	

	
	
	/**
	 * Costruttore per rappresentare un Report già esistente nel DB, richiamato unicamente tramite
	 * l'identificativo, usando gli appositi metodi.
	 * @param id	identificativo del Report nel DB
	 */
	
	public Report(int id) {
		this.id = id;
	}
	

	
	/**
	 * Costruttore per creare un nuovo Report.
	 * Verrà registrato il timestamp del momento in cui si crea l'oggetto.
	 * @param cliente
	 * @param pratiche
	 */
	public Report(Cliente cliente, ArrayList<Pratica> pratiche) {
		this.creazione = new Data();
		this.cliente = cliente;
		this.pratiche = pratiche;
	}
	
	
	
	
	public abstract void stampa();
	public abstract boolean salvaPDF();
	
	

}
