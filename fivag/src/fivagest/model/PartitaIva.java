package fivagest.model;


/**
 * La classe si assicura di gestire oggetti Partita Iva ben formati<br>
 * (11 cifre, senza altri simboli o spazi e validata utilizzando l'ultima cifra di controllo).<br>
 * <a href='http://it.wikipedia.org/wiki/Partita_IVA'>Fonte</a>
 * @author nico
 *
 */
public class PartitaIva {

	private String partitaIva;
	
	
	
	public PartitaIva(String piva) throws Exception {
		if(PartitaIva.checkPartitaIva(piva)) {
			this.partitaIva = piva;
		} else {
			throw new Exception("Partita IVA non valida: "+piva);
		}
	}
	
	
	
	/**
	 * Restituisce la rappresentazione in stringa dell'oggetto PartitaIva corrente:<br>
	 * @return notazione in stringa dell'oggetto corrente
	 */
	public String toString() {
		return this.partitaIva;
	}
	
	
	
	/**
	 * Come il metodo statico, ma esegue il controllo sull'oggetto corrente.
	 * @return boolean
	 */
	public boolean checkPartitaIva() {
		return PartitaIva.checkPartitaIva(this.partitaIva);
	}
	
	
	
	/**
	 * Controlla se una partita IVA è ben formata e valida
	 * @param piva String partita IVA
	 * @return boolean
	 */
	public static boolean checkPartitaIva(String piva) {
		return checkNumeric(piva) && checkLuhn(piva);

	}
	
	
	
	/**
	 * controlla se una partita IVA rispetta la Regola di Luhn (<a href='http://it.wikipedia.org/wiki/Partita_IVA'>Fonte</a>)<br>
	 * ovvero se è ben formata, controllando l'ultima cifra di controllo.<br>
	 * Si consiglia di utilizzare sempre e solo checkPartitaIva() poichè oltre questo ci sono altri controlli.
	 * @param piva String partita IVA di 11 cifre
	 * @return boolean
	 */
	private static boolean checkLuhn(String piva) {
		return ((getX(piva)+getY(piva)) % 10 == 0);
	}
	
	
	
	private static int getX(String piva) {
		int x = 0;
		for(int i = 0; i <= 10; i=i+2) {
			int v = Character.digit(piva.charAt(i), 10);
			x = x + v;
		}
		return x;
	}
	
	
	
	private static int getY(String piva) {
		int y = 0;
		for(int i=1; i<=9; i=i+2) {
			int v = Character.digit(piva.charAt(i), 10);
			int d = 2*v;
			if(d > 9) {d = d - 9;}
			y = y + d;
		}
		return y;
	}
	
	
	
	/**
	 * Controlla se la stringa contiene esattamente 11 cifre
	 * @param s oggetto String da controllare<br>
	 * Si consiglia di utilizzare sempre e solo checkPartitaIva() poichè oltre questo ci sono altri controlli.
	 * @return boolean
	 */
	private static boolean checkNumeric(String stringa) {  
	    return stringa.matches("\\d{11}");  
	}  
	
	
	
}
