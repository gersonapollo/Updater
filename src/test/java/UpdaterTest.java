package test.java;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.chaos.updater.business.Updater;

public class UpdaterTest {

	@Test
	public void evaluatesUpdate() {
		Updater updater = new Updater();
		String[] args = {"-x"};
		assertTrue(updater.update(args));
	}
}
