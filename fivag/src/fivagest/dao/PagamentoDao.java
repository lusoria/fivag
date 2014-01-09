package fivagest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import fivagest.model.Cliente;
import fivagest.model.Pagamento;
import fivagest.model.Pratica;
import fivagest.model.soldi.AccontoVirtuale;
import fivagest.model.soldi.Euro;
import fivagest.util.Data;
import fivagest.util.DataBaseHelper;


/**
 * Pagamento Data Access Object
 * Questa classe si occupa della lettura e scrittura nel DB di dati che riguardano un Pagamento
 * CRUD: Create, Read, Update, Delete
 * @author nico
 *
 */
public class PagamentoDao {

	/**
	 * Costruttore nascosto
	 */
	private PagamentoDao() {}
	
	
	/**
	 * Interroga il database e restituisce un oggetto Pagamento.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Pagamento getPagamento(int id) throws Exception {
		
		Pagamento pagamento = new Pagamento(id);
		
		Connection connessione = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		connessione = DataBaseHelper.getConnection();
		
		try {
			ps = connessione.prepareStatement("SELECT * FROM Pagamento WHERE id = ?");
			ps.setInt(1, pagamento.getId());
			
			result = ps.executeQuery();
			
			// recupero i dati
			if (result.isBeforeFirst()) {
				result.first();
				
				// cliente
				pagamento.setCliente(ClienteDao.getCliente(result.getInt("cliente")));
				
				// importo
				pagamento.setPagamento(new Euro(result.getDouble("ammontare")));
				
				// data
				pagamento.setData(new Data(result.getString("data")));
				
				// paga anche l'IVA?
				pagamento.setPagaAncheIva(result.getBoolean("pagaIVA"));
				
				
			} else {
				// nessun pagamento selezionato
				throw new Exception("nessuna pagamento con id "+pagamento.getId()+" è presente nel database");
			}
			
		} catch (SQLException e) {
			DataBaseHelper.manageError(e);
		}
		
		return pagamento;
	}
	
	
	/**
	 * 
	 * @param pagamento
	 */
	public static void effettuaPagamento(Pagamento pagamento) {
		if(!pagamento.getPagamento().isZero()) {
			
			Cliente cliente = pagamento.getCliente();
			
			// raccolgo le pratiche non pagate del cliente
			ArrayList<Pratica> praticheNonPagate = PraticaDao.selectPraticheNonPagateByCliente(cliente);
			
			// ordino le pratiche non pagate secondo un criterio di urgenza
			Collections.sort(praticheNonPagate, new Pratica.DataPagamentoComparator());
			
			// debug
			System.out.println("--- situazione iniziale ---");
			System.out.print("il cliente: "+cliente+" con acconto virtuale di "+cliente.getAccontoVirtuale()+" che ");
			System.out.print(cliente.getAccontoVirtuale().getPagaIVA() ? "paga": "non paga");
			System.out.println(" l'IVA.");
			
			System.out.println("ha effettuato il "+pagamento);
			
			System.out.println("Ecco le sue pratiche non pagate ordinate");
			for(Pratica pratica : praticheNonPagate) {
				pratica.printPratica();
			}
			System.out.println("--- inizio pagamento ---");
			
			/**
			 * L'acconto virtuale precedente e il pagamento corrente possono essere ognuno di due tipi:
			 * accIVA/acc = Acconto Virtuale precedente che ha pagato delle pratiche IVATE o non IVATE
			 * pagIVA/pag = Pagamento corrente andrà a pagare rispettivamente pratiche con IVA o senza IVA
			 * Possono esserci quindi quattro casi distinti da prendere in esame:
			 * 1- accIVA + pagIVA: si possono tranquillamente sommare perchè andranno a pagare pratiche IVATE
			 * 2- accIVA + pag: Viene rilasciata una Nota di Accredito in modo da trasformare l'accIVA in acc
			 * 3- acc + pagIVA: acc viene trasformato in accIVA e andrà a pagare insieme a pagIVA pratiche IVATE
			 * 4- acc + pag: si sommano tranquillamente e andranno a pagare pratiche senza IVA
			 * 
			 * Il pagamento corrente decide quindi se le pratiche vengono calcolate con o senza IVA.
			 * In tutti i casi pagamento corrente e acconto virtuale vengono sommati.
			 * Caso particolare (2) viene generata una Nota di Accredito.
			 */
			

			Euro pagamentoTot = Euro.somma(cliente.getAccontoVirtuale(), pagamento.getPagamento());
			boolean praticheIVATE = pagamento.pagaAncheIva();
			
			if(cliente.getAccontoVirtuale().getPagaIVA() && !pagamento.pagaAncheIva()) {
				// caso 2.
				// TODO: implementare!
				System.out.println("Nota di Accredito di "+cliente.getAccontoVirtuale());
			}
			
			// prima pratica fuori dal while processata sempre
			Iterator<Pratica> iterator = praticheNonPagate.iterator();
			Pratica pratica = (Pratica) iterator.next();
			
			
			// calcolo il costo totale della pratica: imponibile + sua eventuale IVA + spese esenti IVA
			if(praticheIVATE) {pratica.getImponibile().applicaIva(); }
			Euro costoTotPratica = Euro.somma(pratica.getSpese(), pratica.getImponibile());
			// DEBUG: riassuntino
			System.out.println("acconto: "+pagamentoTot+" costo pratica "+pratica.getDescrizione()+": "+costoTotPratica);
			
			while(pagamentoTot.compareTo(costoTotPratica) >= 0) {

				// il pagamento riesce a coprire il costo complessivo della pratica
				System.out.println("pratica "+pratica.getDescrizione()+" pagata!");
				pratica.setPagata();
				pagamentoTot.meno(costoTotPratica);
				
				// aggiorno la pratica sul db (è stata pagata quindi sicuramente pagata)
				PraticaDao.update(pratica);

				// prossima pratica
				pratica = (Pratica) iterator.next();
				
				// calcolo il costo totale della pratica: imponibile + sua eventuale IVA + spese esenti IVA
				if(praticheIVATE) {pratica.getImponibile().applicaIva(); }
				costoTotPratica = Euro.somma(pratica.getSpese(), pratica.getImponibile());
				// DEBUG: riassuntino
				System.out.println("acconto: "+pagamentoTot+" costo pratica "+pratica.getDescrizione()+": "+costoTotPratica);
				
			}
			
			// ciò che è avanzato è il nuovo Acconto Virtuale del cliente
			cliente.setAccontoVirtuale(new AccontoVirtuale(pagamentoTot.getValore(), praticheIVATE));
			
			// aggiorno il cliente (molto probabilmente è cambiato l'acconto virtuale
			try {
				ClienteDao.update(cliente);
			} catch (Exception e) {
				System.err.println("Errore nell'aggiornamento del cliente "+cliente+"durante il pagamento");
				e.printStackTrace();
			}
			
			// debug
			System.out.println("FINE pagamento: al cliente resta un acconto virtuale di "+cliente.getAccontoVirtuale());
		}		
	}
	
}
