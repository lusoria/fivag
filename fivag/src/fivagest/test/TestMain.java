package fivagest.test;

import fivagest.model.Cliente;
import fivagest.model.PartitaIva;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Cliente titolare = new Cliente("Paolo", "Rossi");
		try {
			titolare.setPartitaIva(new PartitaIva("05542310965"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("piva: "+titolare.getPartitaIva());
		
		
	}

}
