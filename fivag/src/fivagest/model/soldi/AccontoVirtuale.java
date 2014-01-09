package fivagest.model.soldi;

import java.math.BigDecimal;

public class AccontoVirtuale extends Euro {
	
	private boolean pagaIVA;
	
	public AccontoVirtuale() {
		super();
		this.pagaIVA = false;
	}
	
	public AccontoVirtuale(BigDecimal acconto, boolean pagaIVA) {
		super(acconto);
		this.pagaIVA = pagaIVA;
	}
	
	public AccontoVirtuale(double acconto, boolean pagaIVA) {
		super(acconto);
		this.pagaIVA = pagaIVA;
	}
	
	public AccontoVirtuale(String acconto, boolean pagaIVA) {
		super(acconto);
		this.pagaIVA = pagaIVA;
	}
	
	
	public boolean getPagaIVA() {
		return this.pagaIVA;
	}
	
	
	public void setPagaIVA(boolean pagaIVA) {
		this.pagaIVA = pagaIVA;
	}

}
