package fivagest.model.soldi;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Euro implements Comparable<Euro> {
	
	private BigDecimal valore;
	private final int PRECISIONE = 2;
	
	
	
	/**
	 * Traduce la rappresentazione in stringa del valore di un importo in un oggetto Euro.<br>
	 * es.:	"42" crea un oggetto Euro il cui valore è 42.00 e la cui rappresentazione in stringa è "42.00 €"
	 * @param valore	stringa da convertire nel valore dell'oggetto Euro
	 */
	public Euro(String valore) {
		// arrotondamento HALF_UP perchè nel dubbio è meglio arrotondare in eccesso
		// (meglio pagare 0.01 € di tasse in più che in meno) 
		this.valore = new BigDecimal(valore).setScale(PRECISIONE, RoundingMode.HALF_UP);
	}
	
	
	
	/**
	 * Traduce un oggetto BigDecimal in un oggetto Euro il cui valore è pari al valore del BigDecimal
	 * @param valore	valore dell'oggetto Euro
	 */
	public Euro(BigDecimal valore){
		this.valore = valore.setScale(PRECISIONE, RoundingMode.HALF_UP);
	}
	
	
	
	/**
	 * Crea un oggetto Euro con valore zero (0.00 €)
	 */
	public Euro() {
		this.valore = BigDecimal.ZERO.setScale(PRECISIONE);
	}
	
	
	
	/**
	 * Traduce un double in un oggetto Euro il cui valore è pari al valore del double
	 * @param valore	valore dell'oggetto Euro
	 */
	public Euro(double valore) {
		this(new BigDecimal(valore));
	}
	
	
	
	public BigDecimal getValore() {
		return this.valore;
	}
	
	
	
	/**
	 * Restituisce la rappresentazione in stringa dell'oggetto Euro corrente:<br>
	 * es.: "100.50 €", dove 100.50 è il valore dell'oggetto Euro
	 * @return notazione in stringa dell'oggetto corrente
	 */
	public String toString() {
		return valore.toString() + " €";
	}
	
	
	
	/**
	 * Somma al valore dell'oggetto corrente il valore dell'addendo.<br>
	 * Per capirci fa <i>this = this + addendo</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param addendo	importo il cui valore viene sommato al valore dell'oggetto corrente
	 */
	public void piu(Euro addendo) {
		// addendo + addendo = somma
		this.valore = this.getValore().add(addendo.getValore());
	}
	
	
	
	/**
	 * Sottrae al valore dell'oggetto corrente il valore del sottraendo.</br>
	 * Per capirci fa <i>this = this - sottraendo</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param sottraendo	importo il cui valore viene sottratto dal valore dell'oggetto corrente
	 */
	public void meno(Euro sottraendo) {
		// minuendo - sottraendo = differenza
		this.valore = this.getValore().subtract(sottraendo.getValore());
	}
	
	
	
	/**
	 * Divide il valore dell'oggetto corrente per il valore di divisore.<br>
	 * Per capirci fa <i>this = this / divisore</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param divisore	importo nel cui valore si divide l'oggetto corrente 
	 */
	public void diviso(Euro divisore) {
		// dividendo / divisore = quoziente
		this.valore = this.getValore().divide(divisore.getValore(), RoundingMode.HALF_UP);
	}


	
	/**
	 * Divide il valore dell'oggetto corrente per il divisore.<br>
	 * per capirci fa <i>this = this / divisore</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param divisore	numero in cui dividere l'oggetto corrente
	 */
	public void diviso(double divisore) {
		// dividendo / divisore = quoziente
		this.valore = this.getValore().divide(new BigDecimal(divisore), RoundingMode.HALF_UP);
	}
	
	
	
	/**
	 * Moltiplica il valore dell'oggetto corrente con il valore del moltiplicatore.<br>
	 * per capirci fa <i>this = this * moltiplicatore</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param moltiplicatore	importo Euro da moltiplicare per il valore dell'oggetto corrente
	 */
	public void per(Euro moltiplicatore) {
		// moltiplicando * moltiplicatore = prodotto
		// mi appoggio ad un oggetto poichè il metodo multiply aumenta la precisione:
		// 10.00 * 10.00 = 100.0000
		// se creo un nuovo oggetto Euro invece, forzo alla precisione voluta dagli Euro
		Euro a = new Euro(this.getValore().multiply(moltiplicatore.getValore()));
		this.valore = a.getValore();
	}
	
	
	
	/**
	 * Moltiplica il valore dell'oggetto corrente con il moltiplicatore.<br>
	 * per capirci fa <i>this = this * moltiplicatore</i><br>
	 * ATTENZIONE! Questo metodo modifica l'oggetto corrente!
	 * @param moltiplicatore	numero da moltiplicare per il valore dell'oggetto corrente
	 */
	public void per(double moltiplicatore) {
		// moltiplicando * moltiplicatore = prodotto
		// mi appoggio ad un oggetto poichè il metodo multiply aumenta la precisione:
		// 10.00 * 10.00 = 100.0000
		// se creo un nuovo oggetto Euro invece, forzo alla precisione voluta dagli Euro
		Euro a = new Euro(this.getValore().multiply(new BigDecimal(moltiplicatore)));
		this.valore = a.getValore();
	}
	
	
	
	/**
	 * <p>Metodo statico che restituisce la somma in Euro degli importi contenuti in un array
	 * @param addendi	array di oggetti Euro
	 * @return			oggetto Euro il cui valore è la somma dei valori degli oggetti contenuti in addendi
	 */
	public static Euro somma(Euro[] addendi) {
		Euro somma = new Euro();
		for(Euro addendo:addendi) {
			somma.piu(addendo);
		}
		return somma;
	}
	
	
	
	/**
	 * <p>Metodo statico che restituisce la somma in Euro di due soli addendi.</p>
	 * <p>Questa è una scorciatoia per due soli addendi rispetto a somma(Euro[] addendi) che ne accetta molti.</p>
	 * @param addendo1		Euro
	 * @param addendo2		Euro
	 * @return				oggetto Euro il cui valore è la somma dei valori degli oggetti contenuti in addendi
	 */
	public static Euro somma(Euro addendo1, Euro addendo2) {
		return Euro.somma(new Euro[]{addendo1, addendo2});
	}
	
	
	
	/**
	 * <p>Metodo statico che restituisce la differenza in Euro tra due oggetti Euro</p>
	 * @param minuendo		Euro da cui sottrarre il sottraendo
	 * @param sottraendo	Euro da sottrarre al minuendo
	 * @return				Euro differenza dei due Euro
	 */
	public static Euro sottrazione(Euro minuendo, Euro sottraendo) {
		Euro a = new Euro(minuendo.getValore());
		Euro b = new Euro(sottraendo.getValore());
		a.meno(b);
		return a;
	}
	
	
	
	/**
	 * <p>Metodo statico che restituisce il quoziente in Euro tra un oggetto Euro e un divisore</p>
	 * @param dividendo		Euro
	 * @param divisore		double
	 * @return				Euro quoziente = dividendo / divisore
	 */
	public static Euro divisione(Euro dividendo, double divisore) {
		Euro a = new Euro(dividendo.getValore());
		a.diviso(divisore);
		return a;
	}
	
	
	
	/**
	 * <p>Metodo statico che restituisce il prodotto in Euro tra un oggetto Euro e un moltiplicatore
	 * @param moltiplicando		Euro
	 * @param moltiplicatore	double
	 * @return					Euro prodotto = moltiplicando * moltiplicatore
	 */
	public static Euro moltiplicazione(Euro moltiplicando, double moltiplicatore) {
		Euro prodotto = new Euro(moltiplicando.getValore());
		prodotto.per(moltiplicatore);
		return prodotto;
	}
	
	
	
	/**
	 * Confronta due importi in euro. L'utilizzo suggerito per questo metodo è<br>
	 * <i>(a.compareTo(b) {op} 0)</i><br>
	 * dove {op} è un'operazione tra <, <=, ==, >=, >, !=<br>
	 * mentre 'a' e 'b' sono due oggetti Euro.<br>
	 * La formula suggerita restituirà il valore di verità di (a {op} b)
	 * @param paragone	oggetto Euro da paragonare
	 * @return			-1, 0, 1 a seconda che this sia rispettivamente minore, uguale o maggiore di paragone
	 */
	@Override
	public int compareTo(Euro paragone) {
		// TODO Auto-generated method stub
		return this.getValore().compareTo(paragone.getValore());
	}
	
	

	/**
	 * Confronta due importi in Euro e restituisce true se il valore di this è uguale al valore di paragone, false se invece è diverso.
	 * @param paragone	importo da paragonare
	 * @return			valore di verità dell'uguaglianza
	 */
	public boolean uguale(Euro paragone) {
		return (this.compareTo(paragone) == 0);
	}

	

	/**
	 * Ritorna se l'oggetto corrente è di zero euro.
	 * @return		valore di verità se l'oggetto Euro corrente è Zero
	 */
	public boolean isZero() {
		return (this.getValore().compareTo(BigDecimal.ZERO) == 0);
	}
	
	
	
	/**
	 * Ritorna se l'oggetto corrente è positivo o uguale a zero
	 * @return	valore di verità se this è >= 0 €
	 */
	public boolean isPositivo() {
		return (this.compareTo(new Euro()) >= 0);
	}
	
	
	
	/**
	 * Ritorna se l'oggetto corrente è negativo (e diverso da zero)
	 * @return	valore di verità se this è < 0 €
	 */
	public boolean isNegativo() {
		return (this.compareTo(new Euro()) < 0);
	}
	

	

}
