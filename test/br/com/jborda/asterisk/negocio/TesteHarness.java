package br.com.jborda.asterisk.negocio;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TesteHarness {
	
	@BeforeClass
	public static void criaBase() {
		System.out.println("criaBase");
	}

	@AfterClass
	public static void apagaBase() {
		System.out.println("apagaBase");
	}
}
