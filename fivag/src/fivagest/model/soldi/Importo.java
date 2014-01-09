package fivagest.model.soldi;

import java.math.BigDecimal;

/**
 * <p>Un Importo è la rappresentazione di un valore Euro su cui si applicherà o si è già applicata un'imposta.</p>
 * <p>Ad esempio, compilando una fattura, l'Importo può essere utilizzato<br>
 * - sia come il totale fattura (quindi ivato, da cui eventualmente scorporare l'iva ma MAI applicarla una seconda volta)<br>
 * - sia una imponibile non ancora ivato (e che quindi non è possibile scorporarne l'iva).<br>
 * <p>Per quegli importi ESENTI iva per art.15 o via dicendo, (su cui MAI si applicheranno imposte),
 * è preferibile utilizzare direttamente la classe Euro.</p>
 * @author nico
 *
 */
public class Importo extends Euro {
	
	
	private Aliquota aliquota;
	private boolean ivato;
	
	
	
	public Importo(Aliquota aliquota, boolean ivato) {
		super();
		this.aliquota = aliquota;
		this.ivato = ivato;
	}
	
	
	
	public Importo(String valore, Aliquota aliquota, boolean ivato) {
		super(valore);
		this.aliquota = aliquota;
		this.ivato = ivato;
	}
	
		
	
	public Importo(BigDecimal valore, Aliquota aliquota, boolean ivato){
		super(valore);
		this.aliquota = aliquota;
		this.ivato = ivato;
	}
	
	
	public Importo(double valore, Aliquota aliquota, boolean ivato){
		super(valore);
		this.aliquota = aliquota;
		this.ivato = ivato;
	}
	
	
	
	/**
	 * Restituisce l'aliquota applicata o da applicare all'oggetto corrente
	 * @return oggetto Aliquota
	 */
	public Aliquota getAliquota() {
		return aliquota;
	}
	
	
	
	/**
	 * Restituisce se all'oggetto corrente è già stata applicata l'IVA oppure no.
	 * Questo garantisce di non applicare o scorporare l'imposta più volte.
	 * @return boolean
	 */
	public boolean isIvato() {
		return this.ivato;
	}
	

	
	/**
	 * Imposta su IVATO l'oggetto corrente (quindi l'oggetto corrente rappresenta ad esempio un totale fattura).
	 * Questo è un metodo privato, poichè da fuori la classe non si deve poter agire direttamente sull'attributo ivato.
	 */
	private void setIvato() {
		if(!this.isIvato()) {
			this.ivato = true;
		}
	}
	
	
	
	/**
	 * Imposta su NON IVATO l'oggetto corrente (quindi l'oggetto corrente rappresenta ad esempio un imponibile).
	 * Questo è un metodo privato, poichè da fuori la classe non si deve poter agire direttamente sull'attributo ivato.
	 */
	private void setNonIvato() {
		if(this.isIvato()) {
			this.ivato = false;
		}
	}



	/**
	 * Restituisce l'imposta già applicata o ancora da applicare all'Importo corrente.
	 * @return oggetto Euro dell'imposta.
	 */
	public Euro getImposta() {
		Euro imposta = null;
		if(! this.isIvato()) {
			// non ivato. Imposta = importoNonIvato * aliquota / 100
			imposta = new Euro(this.getValore());	// creo un oggetto di appoggio da moltiplicare per l'iva
			imposta.per(aliquota.getAliquota());
			imposta.diviso(100);
		}else{
			// già ivato! bisogna scorporare!
			//imposta = new Euro(this.getValore());
			//imposta.meno(this.getImponibile());
			
			imposta = Euro.sottrazione(this, this.getImponibile());
			// imposta.diviso(1.22);
		}
		return imposta;
	}



	/**
	 * Restituisce la parte non ivata dell'oggetto Importo corrente.
	 * Nota: se l'Importo corrente NON è ancora stato ivato, è lui stesso un Imponibile.
	 * Il metodo restituirà quindi un nuovo oggetto Euro con lo stesso valore dell'Importo corrente.
	 * Se invece l'oggetto corrente e' già stato ivato, il metodo restituirà un oggetto Euro del valore dell'Importo corrente MENO l'imposta.
	 * @return oggetto Euro della parte imponibile dell'Importo.
	 */
	public Euro getImponibile() {
		Euro imponibile = null;
		if(!this.isIvato()) {
			imponibile = new Euro(this.getValore());
		}else{
			//imponibile = new Euro(this.getValore());
			//imponibile.diviso((double)this.aliquota.getAliquota()/100+1);
			
			imponibile = Euro.divisione(this, (double)this.aliquota.getAliquota()/100+1);
		}
		return imponibile;
	}



	/**
	 * Scorpora l'IVA dall'oggetto corrente SOLO SE all'Importo corrente non è stata ancora scorporata l'imposta. 
	 */
	public void scorporaIVA() {
		if(this.isIvato()) {
			// posso scorporare l'iva
			this.meno(this.getImposta());
			this.setNonIvato();
		}else{
			// eccezione?
		}
	}
	
	
	
	/**
	 * Applica l'IVA dall'oggetto corrente SOLO SE all'Importo corrente non è stata ancora applicata l'imposta. 
	 */
	public void applicaIva() {
		if(!this.isIvato()) {
			// posso applicare l'IVA
			this.piu(this.getImposta());
			this.setIvato();
		}else{
			// eccezione?
		}
	}

}
