package fivagest.test;


import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import fivagest.model.soldi.Euro;


class TestEuro {

	
	@Test
	public void testToStringZeroEuro() {
		Euro zero = new Euro();
		assertEquals("ZERO deve essere 0.00 €", "0.00 €", zero.toString());
	}
	
	
	@Test
	public void testToStringEuroCostruttori() {
		// costruttore con parametro in String
		assertEquals("0.42 stringa deve essere 0.42 €", "0.42 €", new Euro("0.42").toString());
		// costruttore con parametro in String senza decimali
		assertEquals("42 stringa deve essere 42.00 €", "42.00 €", new Euro("42").toString());
		// costruttore con parametro in BigDecimal
		assertEquals("0.42 BigDecimal essere 0.42 €", "0.42 €", new Euro(new BigDecimal("0.42")).toString());
	}
	
	
	@Test
	public void testToStringEuroArrotondamento() {
		// creo un array di coppie (test, valore atteso)
		String[][] test = {
				{"-42.00", "-42.00 €"},
				{"-0.42", "-0.42 €"},
				{"0.420", "0.42 €"},
				{"0.4201", "0.42 €"},
				{"0.424", "0.42 €"},
				{"0.425", "0.43 €"},
				{"0.42501", "0.43 €"},
				{"0.42554", "0.43 €"},
				{"0.42555", "0.43 €"},
				{"0.42556", "0.43 €"},
				{"0.426", "0.43 €"},
				{"0.4299", "0.43 €"}
		};
		
		// testo tutti i valori non arrotondati e li confronto col valore atteso
		// costrutto foreach in java
		for(String[] coppia:test){
			assertEquals(coppia[0]+" deve essere "+coppia[1], coppia[1], new Euro(coppia[0]).toString());
		}
		
	}
	
	
	@Test
	public void testGetValore() {
		Euro soldi = new Euro("42.42");
		assertEquals("42.42 deve essere 42.42", new BigDecimal("42.42"), soldi.getValore());
	}
	
	
	@Test
	public void testPiu() {
		Euro a = new Euro("500");
		Euro b = new Euro("2000");
		Euro valoreAspettato = new Euro("2500.00");
		a.piu(b);
		assertEquals("500 + 2000 deve essere 2500 €", valoreAspettato.getValore(), a.getValore());
		b.piu(new Euro("500"));
		assertEquals("2000 + 500 deve essere 2500 €", valoreAspettato.getValore(), b.getValore());	// commutativa
	}
	
	
	@Test
	public void testMeno() {
		Euro a = new Euro("6543.21");
		Euro b = new Euro("1234.56");
		a.meno(b);
		Euro valoreAspettato = new Euro("5308.65");
		assertEquals("6543.21 € - 1234.56 € deve fare 5308.65 €", valoreAspettato.getValore(), a.getValore());
	}
	
	
	@Test
	public void testDiviso() {
		Euro a = new Euro("42.50");
		Euro b = new Euro("2");
		a.diviso(b);
		assertEquals("42.50 € / 2 €", new Euro("21.25").getValore(), a.getValore());		// argomento Euro
		a.diviso(3);
		assertEquals("21.25 € / 3 (num)", new Euro("7.08").getValore(), a.getValore());		// argomento int
		a.diviso(3.55);
		assertEquals("7.08 € / 3.55 (num)", new Euro("1.99").getValore(), a.getValore());	// argomento double
		
	}
	
	
	@Test
	public void testPer() {
		Euro a = new Euro("25");
		Euro b = new Euro("4");
		a.per(b);
		assertEquals("25 € * 4 €", new Euro("100").getValore(), a.getValore());		// argomento Euro
		a = new Euro("25");
		b.per(a);
		assertEquals("4 € * 25 €", new Euro("100").getValore(), b.getValore());		// commutativa
		b.per(3);
		assertEquals("100 € * 3 (num)", new Euro("300").getValore(), b.getValore()); 	// argomento int
		b.per(3.33);
		assertEquals("300 € * 3.33 (num)", new Euro("999").getValore(), b.getValore()); 	// argomento double
	}
	
	
	@Test
	public void testSomma() {
		Euro[] addendi = {new Euro("10"), new Euro("20.5"), new Euro("10.45"), new Euro("1"), new Euro("0.05")};
		assertEquals("10+20.5+10.45+1+0.05", new Euro("42").getValore(), Euro.somma(addendi).getValore());		// argomento Euro
	}
	
	
	@Test
	public void testDifferenza() {
		Euro a = new Euro("42.42");
		Euro b = new Euro("2.02");
		Euro c = Euro.sottrazione(a, b);
		assertEquals("42.42€ - 2.02€ = 40.40€", true, c.uguale(new Euro("40.40")));
		
		Euro d = Euro.sottrazione(b, a);
		assertEquals("2.02€ - 42.42€ = -40.40€", true, d.uguale(new Euro("-40.40")));
	}
	
	
	@Test
	public void testDivisione() {
		Euro a = new Euro("42.42");
		int b = 2;
		Euro c = Euro.divisione(a, b);
		assertEquals("42.42€ / 2 = 21.21€", true, c.uguale(new Euro("21.21")));
		Euro d = Euro.divisione(a, 21.21);
		assertEquals("42.42€ / 21.21 = 2€", true, d.uguale(new Euro("2.00")));
	}
	
	
	
	@Test
	public void testMoltiplicazione() {
		Euro a = new Euro("21.21");
		int b = 2;
		Euro c = Euro.moltiplicazione(a, b);
		assertEquals("21.21€ * 2 = 42.42€", true, c.uguale(new Euro("42.42")));
		Euro d = Euro.moltiplicazione(c, 0.5);
		assertEquals("42.42€ * 0.5 = 21.21€", true, d.uguale(a));
	}
		
	
	@Test
	public void testCompara() {
		Euro a = new Euro("1.99");
		Euro b = new Euro("2.00");
		assertEquals("1.99 < 2", true, (a.compareTo(b) < 0));
		assertEquals("1.99 <= 2", true, (a.compareTo(b) <= 0));
		assertEquals("1.99 == 2", false, (a.compareTo(b) == 0));
		assertEquals("1.99 >= 2", false, (a.compareTo(b) >= 0));
		assertEquals("1.99 > 2", false, (a.compareTo(b) > 0));
		assertEquals("1.99 != 2", true, (a.compareTo(b) != 0));
		
		Euro c = new Euro("1.99");
		assertEquals("1.99 < 1.99", false, (a.compareTo(c) < 0));
		assertEquals("1.99 <= 1.99", true, (a.compareTo(c) <= 0));
		assertEquals("1.99 == 1.99", true, (a.compareTo(c) == 0));
		assertEquals("1.991 >= 1.99", true, (a.compareTo(c) >= 0));
		assertEquals("1.99 > 1.99", false, (a.compareTo(c) > 0));
		assertEquals("1.99 != 1.99", false, (a.compareTo(c) != 0));
	}
	
	
	@Test
	public void testUguale() {
		Euro a = new Euro("1.99");
		Euro b = new Euro("1.99");
		assertEquals("1.99 == 1.99", true, a.uguale(b));
		assertEquals("0 == 0", true, new Euro().uguale(new Euro()));
		assertEquals("42 == 0", false, new Euro("42").uguale(new Euro()));
	}
	
	
	/**
	 * Test del metodo per verificare se l'oggetto è 0.00 €
	 */
	@Test
	public void testZero() {
		Euro a = new Euro();
		assertEquals("numero 0 è zero", true, a.isZero());
		Euro b = new Euro("0");
		assertEquals("stringa 0 è zero", true, b.isZero());
		Euro c = new Euro("0.00");
		assertEquals("stringa 0.00 è zero", true, c.isZero());
		Euro d = new Euro("42");
		d.meno(new Euro("42"));
		assertEquals("42 € - 42 € è zero", true, d.isZero());
	}
	
	
	/**
	 * Test dei metodi per verificare se l'oggetto è positivo o negativo
	 */
	@Test
	public void testPositivoNegativo() {
		Euro a = new Euro("42");
		assertEquals("42 € è positivo", true, a.isPositivo());
		Euro b = new Euro("0.01");
		assertEquals("0.01 € è positivo", true, b.isPositivo());
		Euro c = new Euro();
		assertEquals("0 € è positivo", true, c.isPositivo());
		Euro d = new Euro("-0.01");
		assertEquals("-0.01 € è negativo", true, d.isNegativo());
		Euro e = new Euro("-42");
		assertEquals("-42 € è negativo", true, e.isNegativo());
	}
	
	
	/**
	 * Test che riassume un po' tutti i metodi della classe Euro,
	 * compresi valori negativi, finora non ancora testati in pieno.
	 */
	@Test
	public void testRiattuntivo() {
		Euro a = new Euro();
		Euro b = new Euro("42");
		
		a.meno(b);
		assertEquals("0 - 42 = -42", "-42.00 €", a.toString());
		
		a.piu(new Euro("20"));
		assertEquals("-42 + 20 = -22", "-22.00 €", a.toString());
		
		a.diviso(2);
		assertEquals("-22 / 2 = -11", "-11.00 €", a.toString());
		
		a = Euro.somma(new Euro[]{a,b});
		assertEquals("-11 + 42 = 31", "31.00 €", a.toString());
		
		a.per(-2);
		assertEquals("31 * -2 = -62", "-62.00 €", a.toString());
		
		a.piu(new Euro(new BigDecimal(20)));
		assertEquals("-62 + 20 = -42 = -42", true, a.compareTo(new Euro("-42")) == 0);
		
		
	}
	
	
	
	
}
