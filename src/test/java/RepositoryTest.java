package test.java;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.chaos.updater.repository.ClientRepository;
import br.com.chaos.updater.repository.Repositories;

public class RepositoryTest {
	
	@Test
	public void evaluatesRepositoryNotNull() {
		ClientRepository origin = new ClientRepository();
		origin.getRepository(Repositories.LOCAL);
		assertNotNull(origin);
	}
	
	@Test
	public void evaluatesServerNotNull() {
		assertNotNull(Repositories.SERVER_CLIENT);
	}
	@Test
	public void evaluatesLocalNotNull() {
		assertNotNull(Repositories.LOCAL);
	}

}
