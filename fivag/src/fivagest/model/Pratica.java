package fivagest.model;

import java.util.Comparator;

import fivagest.model.soldi.Euro;
import fivagest.model.soldi.Importo;
import fivagest.util.Data;


/**
 * Un oggetto Pratica è la rappresentazione di un qualsiasi lavoro/servizio/oggetto
 * che l'ufficio fornisce ad un solo cliente e che viene ricompensato da un solo importo in Euro (>= 0.00 €).
 * Un oggetto Pratica ha anche una voce Spese (in Euro) che rappresenta la somma di tutte quelle spese esenti iva
 * (quindi non oggetti Importi) che è utile che stiano visibilmente separate e che, in fattura, avranno una voce dedicata.
 * @author nico
 *
 */
public class Pratica implements Comparable<Pratica>{

	
	private int id;
	private Cliente cliente;
	private Importo imponibile;
	private Euro spese;
	private String descrizione;
	/*
	data pagamento:
	Data dalla quale il cliente sarà ufficialmente in debito per la pratica in oggetto.
	Molto spesso coinciderà con la data di inserimento della pratica nel sistema, poichè si inizia subito a lavorarci e il cliente deve pagare da subito.
	Ma a volte capita che una pratica viene inserite all'inizio dell'anno, ma non deve comparire tra i debiti del cliente, poichè non è ancora tempo.
	Esempio: contabilità dei trimestri futuri.
	*/
	private Data dataPagamento;
	private boolean pagata;
	
	
	/**
	 * Costruttore per rappresentare una nuova pratica, non ancora presente nel database.
	 * Con questo costruttore si potrà istanziare un oggetto pronto per essere memorizzato nel database poichè
	 * qui bisogna fornire tutti gli attributi obbligatori.
	 * @param cliente Cliente per il quale è stata aperta la pratica da creare
	 * @param imponibile Euro di costo per il cliente della pratica da creare
	 * @param spese Euro di spese nostre sostenute per avviare la pratica
	 * @param descrizione Nome o brevissima descrizione della pratica
	 * @param dataPagamento Data dalla quale il cliente sarà ufficialmente in debito
	 */
	public Pratica(Cliente cliente, Importo imponibile, Euro spese, String descrizione, Data dataPagamento) {
		// TODO: controllini sui parametri
		this.cliente = cliente;
		this.imponibile = imponibile;
		this.spese = spese;
		this.descrizione = descrizione;
		this.setDataPagamento(dataPagamento);
		this.pagata = false;
		
	}
	
	
	/**
	 * Costruttore per una pratica vuota.
	 * @param id identificativo della pratica nel database
	 */
	public Pratica(int id) {
		this.id = id;
	}
	
	
	/**
	 * Restituisce l'identificativo univoco con la quale la Pratica è salvata nel database. 
	 * @return	int identificativo univoco della pratica
	 */
	public int getId() {
		return this.id;
	}
	
	
	/**
	 * Imposta l'identificativo della pratica sul database.
	 * Teoricamente questo metodo viene usato solo dalle ClassiDao che costruiscono la Pratica dal DB.
	 * @param id identificativo della pratica sul db
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * Restituisce la descrizione (ovvero il titolo) della Pratica (es.: "PEC: ATTIVAZIONE E COMUNICAZIONE")
	 * @return descrizione (stringa tutta maiuscola)
	 */
	public String getDescrizione() {
		return descrizione;
	}
	
	
	/**
	 * Imposta la descrizione della Pratica corrente.
	 * @param descrizione
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
	/**
	 * Restituisce il Cliente (l'unico) della Pratica
	 * @return oggetto Cliente
	 */
	public Cliente getCliente() {
		return this.cliente;
	}
	
	
	/**
	 * Imposta il cliente della pratica corrente
	 * @param cliente (oggetto Cliente)
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	/**
	 * Restituisce il costo della Pratica per il Cliente
	 * @return imponibile (oggetto Importo)
	 */
	public Importo getImponibile() {
		return this.imponibile;
	}
	
	
	/**
	 * Imposta il valore dell'imponibile (costo della pratica al cliente)
	 * @param imponibile (oggetto Importo, tendenzialmente non ancora ivato)
	 */
	public void setImponibile(Importo imponibile) {
		if(imponibile.isPositivo()) {
			this.imponibile = imponibile;			
		}else{
			// TODO: inserire eccezione!
		}
		
	}
	
	
	/**
	 * Restituisce le spese affrontate dallo studio per la Pratica corrente.
	 * Essenso spese esenti IVA, il metodo restituisce un oggetto Euro, dove appunto l'iva non potrà essere applicata.
	 * @return	spese esenti IVA 
	 */
	public Euro getSpese() {
		return this.spese;
	}
	
	
	/**
	 * Imposta le spese per la Pratica corrente in Euro.
	 * @param spese
	 */
	public void setSpese(Euro spese) {
		if(spese.isPositivo()) {
			this.spese = spese;
		}else{
			// TODO: scrivere l'eccezione per spese negative
		}
	}
	
	
	/**
	 * Restituisce la data di pagamento della pratica corrente.<br>
	 * @return data di pagamento
	 */
	public Data getDataPagamento() {
		return dataPagamento;
	}
	
	
	/**
	 * Imposta la data del pagamento. Vedi la documentazione tra gli attributi
	 * @param dataPagamento
	 */
	public void setDataPagamento(Data dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	

	/**
	 * Restituisce se la Pratica corrente è stata pagata oppure no.  
	 * @return	boolean
	 */
	public boolean isPagata() {
		return this.pagata;
	}
	
	
	
	/**
	 * Imposta la Pratica corrente come pagata.<br>
	 * Non è consentita l'operazione inversa.
	 */
	public void setPagata() {
		this.pagata = true;
	}
	
	
	/**
	 * Stampa la descrizione della Pratica. SOLO PER DEBUG!
	 */
	public void printPratica() {
		Cliente tizio = this.cliente;

		// TODO: tizio è sincronizzato col db?
		
		System.out.print("Pratica ["+this.getId()+"] di "+this.imponibile+" con "+this.getSpese()+" di spese: "+this.getDescrizione().toUpperCase());
		System.out.print(" per "+tizio);
		System.out.println(" pagamento al "+this.getDataPagamento());
	}
	

	
	/**
	 * alfabeticamente sulla descrizione!!
	 */
	@Override
	public int compareTo(Pratica pratica) {
		// TODO Auto-generated method stub
		return this.descrizione.compareToIgnoreCase(pratica.getDescrizione());
		//return this.getData().compareTo(pratica.getData());
	}
	
	
	/**
	 * Classe comparator. Implementa unicamente il metodo compare e serve per ordinare le Pratiche per loro Imponibile
	 * dal più basso al più alto.
	 * @author nico
	 *
	 */
	public static class ImponibileComparator implements Comparator<Pratica> {

		@Override
		public int compare(Pratica p1, Pratica p2) {
			return p1.getImponibile().compareTo(p2.getImponibile());
		}
	}
	
	
	/**
	 * Sottoclasse comparator. Implementa unicamente il metodo compare e serve per ordinare le Pratiche per Data di pagamento
	 * dalla meno recente alla più recente.
	 * @author nico
	 *
	 */
	public static class DataPagamentoComparator implements Comparator<Pratica> {

		@Override
		public int compare(Pratica p1, Pratica p2) {
			return p1.getDataPagamento().compareTo(p2.getDataPagamento());
		}
		
	}
}
