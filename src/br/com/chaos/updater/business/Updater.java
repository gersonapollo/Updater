package br.com.chaos.updater.business;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chaos.updater.repository.ClientRepository;
import br.com.chaos.updater.repository.Repositories;
import br.com.chaos.updater.view.UpdaterScreen;

public class Updater {
	
	private final static Logger LOG = LoggerFactory.getLogger(Updater.class);

	public Boolean update(String[] args) {
		boolean statusReturn = false;
		
		Repository localRepository = getLocalReposytory();
		
		List<String> arguments = Arrays.asList(args);
		if(arguments.contains("-x")){
			LOG.info("Argument x received, Client will not be Updated");
		}else {
			if(localRepository == null) {
				Repositories serverRepository = arguments.contains("-ag") ? Repositories.SERVER_CLIENT : Repositories.SERVER_LOCAL;
				cloneServerRepository(serverRepository);
				statusReturn = true;
			}else {
				
				pull();
				statusReturn = true;
			}
			
			
			
		}
		openTF();
		UpdaterScreen.closeApplication();
		return statusReturn;
	}
	
	public boolean cloneServerRepository(Repositories serverRepository) {
		ClientRepository repo = new ClientRepository();
		LOG.info("Cloning the Client from server: " + serverRepository.getPath());
		mapSeverFolder();
		return repo.cloneServerRepository(serverRepository);
		
	}
	
	public Boolean pull() {
		ClientRepository repo = new ClientRepository();
		//repo.getStatus();
		repo.discardLocalChanges();
		LOG.info("Updating the existing Client " + Repositories.LOCAL.getPath());
		mapSeverFolder();
		return repo.pull();
	}
	
//	private Repository getServerReposytory(){
//		ClientRepository repo = new ClientRepository();
//		return repo.getRepository(Repositories.SERVER);
//	}
	
	private Repository getLocalReposytory(){
		ClientRepository repo = new ClientRepository();
		Git localRepository = repo.getRepository(Repositories.LOCAL);
		if(localRepository != null) {
			return localRepository.getRepository();
		}
		return null;
	}
	
	private void openTF() {
		CommandLine.execFinancialTerminal();
	}
	
	private void mapSeverFolder() {
		CommandLine.mapServerFolder();
	}
}
