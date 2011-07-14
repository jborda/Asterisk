package br.com.jborda.asterisk.negocio;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.jborda.asterisk.model.Extension;

public class ExtensionHarness extends TesteHarness {
	
	ExtensionNegocio negocio = new ExtensionNegocio();
	
	@Before
	public void executaEnvironment() {
		System.out.println("executaEnvironment");
	}
	
	@After
	public void limpaCamada() {
		System.out.println("limpaCamada");
	}
	
	@Test
	public void adicionaHappyDay() {
		System.out.println("inicio...");
		Assert.assertEquals(0, negocio.list(Extension.class).size());
		Extension extension = new Extension();
		extension.setApp("app");
		extension.setAppdata("data app");
		extension.setContext("context");
		extension.setExten("exe");
		extension.setPriority(Short.valueOf("10"));
		negocio.add(extension);
		Assert.assertEquals(1, negocio.list(Extension.class).size());
		System.out.println("fim");
	}
}
