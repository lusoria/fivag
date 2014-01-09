package fivagest.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Rappresentazione di un indirizzo mail
 * @author nico
 *
 */
public class Email {
	
	
	private String indirizzo;
	
	
	/**
	 * Costruttore blabla
	 * @param indirizzo
	 * @throws Exception
	 */
	public Email(String indirizzo) throws Exception {
		if(indirizzo.length() != 0) {
			EmailValidator validator = new EmailValidator();
			if(validator.validate(indirizzo)) {
				// email valida
				this.indirizzo = indirizzo;
			}else{
				throw new Exception(indirizzo+" non è un indirizzo email valido.");
			}
		}else{
			throw new Exception("L'indirizzo email non è stato specificato.");
		}
	}
	
	
	/**
	 * Traduce l'oggetto corrente in una stringa NON linkabile<br>
	 * Per avere il link HTML usare link()
	 * @return notazione in stringa dell'oggetto corrente
	 */
	public String toString() {
		return this.indirizzo;
	}
	
	
	/**
	 * Restituisce l'indirizzo mail linkabile in HTML
	 * @return indirizzo linkabile
	 */
	public String link() {
		return "<a href='mailto:"+this.indirizzo+"'>"+this.indirizzo+"</a>";
	}
	
	
	/**
	 * Restituisce una stringa che linka all'indirizzo Email corrente.
	 * @param link Stringa che viene restituita linkabile
	 * @return link che punta all'indirizzo email corrente
	 */
	public String link(String link) {
		return "<a href='mailto:"+this.indirizzo+"'>"+link+"</a>";
	}
	
	
	/**
	 * Presa da <a href='http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/'>QUI</a><br>
	 * @author mkyong per EmailValidator 
	 *
	 */
	private class EmailValidator {
	 
		private Pattern pattern;
		private Matcher matcher;
	 
		private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		public EmailValidator() {
			pattern = Pattern.compile(EMAIL_PATTERN);
		}
	 
		/**
		 * Validate hex with regular expression
		 * 
		 * @param hex
		 *            hex for validation
		 * @return true valid hex, false invalid hex
		 */
		public boolean validate(final String hex) {
	 
			matcher = pattern.matcher(hex);
			return matcher.matches();
	 
		}
	}
}