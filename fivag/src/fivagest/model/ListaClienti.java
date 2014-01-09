package fivagest.model;

import java.util.ArrayList;

public class ListaClienti {

	private String descrizione;
	private ArrayList<Cliente> clienti;
	//private ArrayList<String> colonne;
	
	
	
	public ListaClienti(String descrizione, ArrayList<Cliente> clienti, ArrayList<String> colonne) {
		this.descrizione = descrizione;
		this.clienti = clienti;


	}
	
	
	public void stampa() {
		System.out.println("LISTA: " + this.descrizione);
		for(Cliente c:this.clienti) {
			System.out.println(c.getNome());
		}
		
	}
}
