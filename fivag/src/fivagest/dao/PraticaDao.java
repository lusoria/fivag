package fivagest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fivagest.model.Cliente;
import fivagest.model.Pratica;
import fivagest.model.soldi.Aliquota;
import fivagest.model.soldi.Euro;
import fivagest.model.soldi.Importo;
import fivagest.util.Data;
import fivagest.util.DataBaseHelper;

public class PraticaDao {

	
	/**
	 * Costruttore nascosto
	 */
	private PraticaDao() {}
	
	
	/**
	 * Recupera i dati dal database e cerca di riempire l'oggetto Pratica con più dati possibili.
	 * Alcuni saranno obbligatori e quindi sicuramente presenti nel database, mentre altri facoltativi.
	 * @param pratica
	 * @throws Exception
	 */
	public static Pratica getPratica(int idPratica) throws Exception {
		
		Connection connessione = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		connessione = DataBaseHelper.getConnection();
		
		Pratica pratica = null;
		try {
			ps = connessione.prepareStatement("SELECT * FROM Pratica WHERE id = ?");
			ps.setInt(1, idPratica);
			result = ps.executeQuery();
		
			if (result.isBeforeFirst()) {
				result.first();
				
				// cliente obbligatorio
				Cliente cliente = ClienteDao.getCliente(result.getInt("cliente"));
				
				// imponibile obbligatorio
				// TODO: ATTENZIONE!! aliquota al 22% e importo non ivato HARD CODED!
				Importo imponibile = new Importo(result.getBigDecimal("imponibile"), new Aliquota(22), false);
								
				// spese obbligatorie
				Euro spese = new Euro(result.getBigDecimal("spese"));
				
				// descrizione obbligatoria
				String descrizione = result.getString("descrizione");
				
				// data pagamento obbligatoria
				Data data = new Data(result.getString("dataPagamento"));
				
				// creo la pratica ora che ho raccolto i parametri obbligatori
				pratica = new Pratica(cliente, imponibile, spese, descrizione, data);
				
				// parametri non obbligatori
				pratica.setId(idPratica);
				
			} else {
				// nessuna pratica selezionata!!
				throw new Exception("Non esiste la pratica con id: "+idPratica+".");
			}
		
			result.close();
			ps.close();
			connessione.close();
			
		} catch(SQLException erroreSQL) {
			DataBaseHelper.manageError(erroreSQL);
		}
		
		return pratica;
	}
	
	
	/**
	 * [CRUD: update] Aggiorna sul database TUTTI gli attributi della Pratica passata come parametro.
	 * @param pratica Pratica da aggiornare
	 */
	public static void update(Pratica pratica) {
		
		Connection connessione = null;
		PreparedStatement ps = null;
		connessione = DataBaseHelper.getConnection();
		
		try {
			
			ps = connessione.prepareStatement("UPDATE pratica SET cliente=?, imponibile=?, spese=?, descrizione=?, pagata=?, dataPagamento=? WHERE id=?");
			ps.setInt(1, pratica.getCliente().getId());
			ps.setDouble(2, pratica.getImponibile().getValore().doubleValue());
			ps.setDouble(3, pratica.getSpese().getValore().doubleValue());
			ps.setString(4, pratica.getDescrizione());
			ps.setBoolean(5, pratica.isPagata());
			ps.setDate(6, new java.sql.Date(pratica.getDataPagamento().getTime().getTime()));
			ps.setInt(7, pratica.getId());
			
			ps.executeUpdate();
		
			ps.close();
			connessione.close();
			
		} catch (SQLException e) {
			System.err.println("Errore durante l'aggiornamento della pratica "+pratica.getDescrizione());
			DataBaseHelper.manageError(e);
		}
		
	}
	
	
	/**
	 * [CRUD: create] Salva una pratica sul database. 
	 * @param pratica	pratica da salvare nel db
	 */
	public static void create(Pratica pratica) {
		// TODO: implementare
		
	}
	
	
	/**
	 * <p>Restituisce un ArrayList con tutte le pratiche del Cliente passato come parametro.</p>
	 * </p>Se il Cliente non ha pratiche, restituisce un ArrayList vuoto.</p>
	 * @param cliente
	 * @return
	 */
	public static ArrayList<Pratica> selectPraticheNonPagateByCliente(Cliente cliente) {
		
		Connection connessione = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		connessione = DataBaseHelper.getConnection();
		
		ArrayList<Pratica> pratiche = new ArrayList<Pratica>();
		try {
			ps = connessione.prepareStatement("SELECT * FROM Pratica WHERE cliente = ? AND pagata = 0");
			ps.setInt(1, cliente.getId());
			result = ps.executeQuery();
			
			
			if (result.isBeforeFirst()) {
				// qualcosa c'è
				while(result.next()) {
					Pratica pratica = new Pratica(result.getInt("id"));
					pratica.setCliente(cliente);
					pratica.setImponibile(new Importo(result.getDouble("imponibile"), new Aliquota(22), false));
					pratica.setSpese(new Euro(result.getDouble("spese")));
					pratica.setDescrizione(result.getString("descrizione"));
					
					pratica.setDataPagamento(new Data(result.getString("dataPagamento")));
					
					pratiche.add(pratica);
					
				}
				
			}
			
			result.close();
			ps.close();
			connessione.close();
			
		} catch(SQLException erroreSQL) {
			DataBaseHelper.manageError(erroreSQL);
		}
			
		return pratiche;
		
	}
}
