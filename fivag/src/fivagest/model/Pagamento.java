package fivagest.model;

import fivagest.model.soldi.Euro;
import fivagest.util.Data;


/**
 * Classe che rappresenta un Pagamento effettuato da un cliente in una certa data.<br>
 * Il cliente può venire in un giorno qualsiasi e pagare una cifra qualsiasi.<br>
 * Questo è una comodità per lui, ma causa notevoli problemi a noi poichè
 * non si sa bene COSA sta pagando. Col suo pagamento, potrebbe infatti saldare parzialmente un vecchio debito
 * oppure coprire l'intero costo di una nuova pratica, o ancora dare un acconto per qualche servizio futuro.<br>
 * Chi decide questo non è il cliente e nemmeno noi, ma è il sistema! 
 * @author nico
 *
 */
public class Pagamento {

	private int id;
	private Cliente cliente;
	private Data data;
	private Euro pagamento;
	/**
	 * se il pagamento è fatto per assegno o bonifico, viene per forza anche calcolata l'IVA
	 * sul costo delle pratiche, quindi il pagamento andrà a pagare pure l'IVA (true).
	 * Se invece il pagamento è in contanti, può essere false, cioè non andare a pagare l'IVA
	 */
	private boolean pagaAncheIVA;
	
	
	/**
	 * Creo un oggetto Pagamento che si riferisce ad un pagamento già registrato sul DB
	 * @param id identificativo del pagamento sul db
	 */
	public Pagamento(int id) {
		this.id = id;
	}
	
	
	/**
	 * Rappresentazione di un nuovo pagamento effettuato da un cliente in una certa data.
	 * @param cliente cliente che effettua il pagamento
	 * @param data giorno in cui viene effettuato il pagamento
	 * @param pagamento importo > 0.00 €
	 */
	public Pagamento(Cliente cliente, Data data, Euro pagamento, boolean pagaAncheIVA) {
		this.cliente = cliente;
		this.data =  data;
		this.pagamento = pagamento;
		this.pagaAncheIVA = pagaAncheIVA;
	}
	
	
	/**
	 * Restituisce il numero col quale il Pagamento è identificato univocamente sul DB.
	 * @return identificativo
	 */
	public int getId() {
		return this.id;
	}
	
	
	/**
	 * Restituisce il cliente autore del Pagamento corrente.
	 * @return cliente
	 */
	public Cliente getCliente() {
		return this.cliente;
	}
	
	
	/**
	 * Imposta il Cliente che effettua il Pagamento corrente.
	 * @param cliente cliente
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	/**
	 * Restituisce il giorno nel quale è stato effettuato il Pagamento corrente.
	 * @return data
	 */
	public Data getData () {
		return this.data;
	}
	
	
	/**
	 * Imposta la data del Pagamento corrente.
	 * @param data data
	 */
	public void setData(Data data) {
		this.data = data;
	}
	
	
	/**
	 * Restituisce l'importo del Pagamento corrente.
	 * @return importo
	 */
	public Euro getPagamento() {
		return this.pagamento;
	}
	
	
	/**
	 * Imposta l'importo del Pagamento corrente.
	 * @param importo importo
	 */
	public void setPagamento(Euro pagamento) {
		this.pagamento = pagamento;
	}
	
	
	/**
	 * restituisce se il Pagamento corrente andrà a pagare anche l'IVA delle pratiche,
	 * (perchè ad esempio è stato fatto con assegno o bonifico) oppure no (contanti).
	 * @return booleano
	 */
	public boolean pagaAncheIva() {
		return this.pagaAncheIVA;
	}
	
	
	/**
	 * Imposta se il Pagamento corrente andrà a pagare anche l'IVA delle pratiche,
	 * (perchè ad esempio viene fatto con assegno o bonifico) oppure no (contanti).
	 * @param paga booleano
	 */
	public void setPagaAncheIva(boolean paga) {
		this.pagaAncheIVA = paga;
	}


	@Override
	public String toString() {
		return "Pagamento [del cliente=" + cliente + ", in data="
				+ data + ", di euro=" + pagamento + " che "+(this.pagaAncheIva() ? "paga" : "non paga")+" l'IVA";
	}
	
	
	
	
}
