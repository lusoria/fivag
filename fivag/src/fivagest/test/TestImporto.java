package fivagest.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fivagest.model.soldi.Aliquota;
import fivagest.model.soldi.Euro;
import fivagest.model.soldi.Importo;

public class TestImporto {

	@Test
	public void testIsIvato() throws Exception {
		
		Importo a = new Importo("12.20", new Aliquota(22), false);
		assertEquals("a non deve essere ivato", false, a.isIvato());
		
		a.applicaIva();
		assertEquals("a deve essere ivato", true, a.isIvato());
		
		a.scorporaIVA();
		assertEquals("a non deve essere ivato", false, a.isIvato());
		
		Importo b = new Importo("12.20", new Aliquota(22), true);
		assertEquals("b deve essere ivato", true, b.isIvato());
		
	}
	
	
	@Test
	public void testAliquota() throws Exception {
		Aliquota a = new Aliquota(22);
		Importo b = new Importo("42.00", a, true);
		assertEquals("aliquota di b è 22 %", "22 %", b.getAliquota().toString());
		assertEquals("aliquota di b è 22 %", a, b.getAliquota());
	}
	
	
	@Test
	public void testTotale() throws Exception {
		
		Importo a = new Importo("12.20", new Aliquota(22), false);
		Euro vecchioImponibile = a.getImponibile();
		assertEquals("imponibile di a è a", true, a.uguale(a.getImponibile()));
		Euro vecchiaImposta = a.getImposta();
		assertEquals("imposta di a è 2.68 €", true, a.getImposta().uguale(new Euro("2.68")));
		
		a.applicaIva();
		assertEquals("a con iva è 14.88 €", true, a.uguale(new Euro("14.88")));
		vecchiaImposta = a.getImposta();
		assertEquals("imposta sempre la stessa", true, a.getImposta().uguale(vecchiaImposta));
		vecchioImponibile = a.getImponibile();
		assertEquals("imponibile è il valore di partenza", true, a.getImponibile().uguale(vecchioImponibile));
		
		a.scorporaIVA();
		assertEquals("a scorporato torna a 12.20 €", true, a.uguale(new Euro("12.20")));
		assertEquals("imposta sempre uguale", true, a.getImposta().uguale(new Euro("2.68")));
		assertEquals("imponibile tornato a 12.20", true, a.getImponibile().uguale(new Euro("12.20")));
		
		
	}

}
