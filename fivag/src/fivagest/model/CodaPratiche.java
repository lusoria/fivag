package fivagest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * Con CodaPratiche si intende la lista di TUTTE le pratiche non pagate di un cliente, ordinate da quella il cui pagamento è più urgente, a quella meno urgente.
 * Il criterio che decide quanto sia urgente il pagamento di una pratica è vario e potrebbe cambiare nel tempo.
 * Quando il cliente viene a dare dei soldi, questi andranno a pagare le pratiche più urgenti, lasciando
 * eventualmente "scoperte" le pratiche più indietro nella coda.
 * <p>La classe è iterabile, quindi ad esempio inseribile in un for, che restituirà una pratica alla volta in ordine.</p>
 * @author nico
 *
 */
public class CodaPratiche implements Iterable<Pratica> {

	private ArrayList<Pratica> pratiche;
	
	/**
	 * Rappresentazione di una Coda di Pratiche
	 * @param cliente
	 * @throws Exception
	 */
	public CodaPratiche(Cliente cliente) throws Exception {
		ArrayList<Pratica> pratiche = cliente.selectPraticheNonPagate();
		CodaPratiche.ordina(pratiche);
		this.pratiche = pratiche;
	}
	
	
	/**
	 * Restituisce le pratiche dentro la Coda.<br>
	 * Può anche restituire una lista vuota.
	 * @return ArrayList delle pratiche contenute nella Coda.
	 */
	public ArrayList<Pratica> getPratiche() {
		return pratiche;
	}


	/**
	 * Ordina le pratiche secondo il "criterio di urgenza del pagamento".<br>
	 * Questo metodo in pratica decide quale pratica deve essere pagata presto e quale può aspettare.  
	 * @param pratiche lista delle pratiche da ordinare (tendenzialmente le pratiche non pagate del cliente).
	 */
	private static void ordina(ArrayList<Pratica> pratiche) {
		Collections.sort(pratiche, new Pratica.DataPagamentoComparator());
	}


	/**
	 * Iteratore che permette ad un oggetto CodaPratiche di essere usato in un ciclo for.
	 */
	@Override
	public Iterator<Pratica> iterator() {
		Iterator<Pratica> p = this.pratiche.iterator();
		return p;
	}
	
	
	
	

	
	
}
