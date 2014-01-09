package fivagest.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fivagest.model.Cliente;
import fivagest.model.soldi.AccontoVirtuale;
import fivagest.util.DataBaseHelper;


/**
 * Cliente Data Access Object
 * Questa classe si occupa della lettura e scrittura nel DB di dati che riguardano un Cliente
 * CRUD: Create, Read, Update, Delete
 * @author nico
 *
 */
public class ClienteDao {


	/**
	 * Costruttore nascosto
	 */
	private ClienteDao() {}
	
	
	/**
	 * [CRUD: read] Interroga il database e cerca di riempire un oggetto Cliente con più dati possibili.<br>
	 * Alcuni campi saranno obbligatori e quindi sicuramente presenti nel database, mentre altri facoltativi.<br>
	 * In quest'ultimo caso, i relativi attributi verranno settati sul loro valore di default o lasciati vuoti.
	 * @param id identificativo del cliente sul DB
	 * @throws Exception se viene passato un id che non esiste nel database
	 */
	public static Cliente getCliente(int idCliente) throws Exception {

		
		Connection connessione = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		connessione = DataBaseHelper.getConnection();
		
		Cliente cliente = null;
		
		try {
		
			ps = connessione.prepareStatement("SELECT * FROM Cliente WHERE id = ?");
			ps.setInt(1, idCliente);
			result = ps.executeQuery();
		
			// recupero i dati
			if (result.isBeforeFirst()) {
				result.first();
				

				
				// nome e cognome
				cliente = new Cliente(result.getString("nome"), result.getString("cognome"));
				
				// id
				cliente.setId(idCliente);

				// acconto virtuale
				// acconto virtuale mai null ma sempre con un valore Euro positivo (>= 0.00 )
				AccontoVirtuale acconto = new AccontoVirtuale();
				if(result.getDouble("accontoVirtuale") != 0.0) {
					acconto = new AccontoVirtuale(result.getDouble("accontoVirtuale"), result.getBoolean("accontoVirtualeIVA"));

				}
				cliente.setAccontoVirtuale(acconto);
				
			} else {
				// nessun cliente selezionato!!
				throw new Exception("nessun cliente con id "+idCliente+" è presente nel database");
			}
			
			result.close();
			ps.close();
			connessione.close();
			
		} catch (SQLException e) {
			DataBaseHelper.manageError(e);
		}
		
		return cliente;
		
	}


	/**
	 * [CRUD: update] Aggiorna sul database TUTTI gli attributi del Cliente passato come parametro.
	 * @param cliente Cliente da aggiornare
	 */
	public static void update(Cliente cliente) {
		
		Connection connessione = null;
		PreparedStatement ps = null;
		connessione = DataBaseHelper.getConnection();
		
		try {
			
			ps = connessione.prepareStatement("UPDATE cliente SET cognome=?, nome=?, accontoVirtuale=?, accontoVirtualeIVA=? WHERE id=?");
			ps.setString(1, cliente.getCognome());
			ps.setString(2, cliente.getNome());
			// TODO: codice fiscale!!
			ps.setDouble(3, cliente.getAccontoVirtuale().getValore().doubleValue());
			ps.setBoolean(4, cliente.getAccontoVirtuale().getPagaIVA());
			ps.setInt(5, cliente.getId());
			
			ps.executeUpdate();
		
			ps.close();
			connessione.close();
			
		} catch (SQLException e) {
			System.err.println("Errore durante l'aggiornamento del cliente "+cliente);
			DataBaseHelper.manageError(e);
		}
		
	}
	
	
	/**
	 * Restituisce la lista di tutti i clienti presenti nel Database, ordinati alfabeticamente
	 * per cognome.
	 * @return
	 */
	public static ArrayList<Cliente> getTuttiClienti() {

		ArrayList<Cliente> clienti = new ArrayList<Cliente>();
		
		Connection connessione = null;
		Statement state = null;
		ResultSet result = null;
		String query = "SELECT * FROM cliente ORDER BY cognome";
		
		connessione = DataBaseHelper.getConnection();
		
		try {
		
			state = connessione.createStatement();
			result = state.executeQuery(query);
		
			// recupero i dati
			if (result.isBeforeFirst()) {
				while(result.next()) {
				
					// nome e cognome
					Cliente cliente = new Cliente(result.getString("nome"), result.getString("cognome"));
					
					// acconto virtuale
					// acconto virtuale mai null ma sempre con un valore Euro positivo (>= 0.00 )
					AccontoVirtuale acconto = new AccontoVirtuale();
					if(result.getDouble("accontoVirtuale") != 0.0) {
						acconto = new AccontoVirtuale(result.getDouble("accontoVirtuale"), result.getBoolean("accontoVirtualeIVA"));

					}
					cliente.setAccontoVirtuale(acconto);
					
					clienti.add(cliente);
				}
				
			}
			
			result.close();
			state.close();
			connessione.close();
			
		} catch (SQLException e) {
			DataBaseHelper.manageError(e);
		}
		
		return clienti;
	}
	
	
	

}
