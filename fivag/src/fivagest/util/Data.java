package fivagest.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Con questa classe estendo GregorianCalendar perchè mi risulta troppo noioso<br>
 * sia istanziare una nuova data che stamparne una in output. 
 * @author admin
 *
 */
@SuppressWarnings("serial")
public class Data extends GregorianCalendar {


	/*
	 * formati supportati per costruire nuovi oggetti Data:
	 * convenzione comune e date in mysql
	 * non so perchè ma prende anche correttamente dd/MM/yyyy
	 */
	final String[] FORMATI_SUPPORTATI = {"dd/MM/yy", "yyyy-MM-dd"};
	
	
	/**
	 * Crea un oggetto Data da una stringa.
	 * Per vedere i formati supportati, leggere la documentazione
	 * della costante FORMATI.
	 * @param stringaDataDaValidare
	 */
	public Data(String stringaData) {
		super.setTime(this.parsaFormato(stringaData));
	}
	
	
	/**
	 * Crea un oggetto Data dalla data corrente
	 */
	public Data() {
		super();
	}
	
	
	private Date parsaFormato(String stringaData) {
		for(String formato : this.FORMATI_SUPPORTATI) {
			try {
				return new SimpleDateFormat(formato).parse(stringaData);
			}catch(ParseException e) {

			}
		}
		System.out.println("Errore nel parsing della data: "+stringaData);
		System.exit(1);
		return null;

	}
	
	
	/**
	 * Traduce la data corrente in una stringa dd/MM/yyyy
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(this.getTime());
	}
	
}
