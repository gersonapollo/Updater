package br.com.chaos.updater.repository;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.merge.MergeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chaos.updater.view.UpdaterProgressMonitor;

public class ClientRepository {

	private final static Logger LOG = LoggerFactory.getLogger(ClientRepository.class);

	public  Git getRepository(Repositories repository) {
		File workDirectory = new File(repository.getPath());
		Git git = null;
		try {
			LOG.info("loading work directory: "+ workDirectory);
			git = Git.open(workDirectory);
		} catch (IOException e) {
			LOG.error("Unable to load the work directory " + workDirectory);
			//e.printStackTrace();
			return null;
		}
		return git;
	}

	public Boolean cloneServerRepository(Repositories repository) {
		Git git = getRepository(repository);
		try {
			LOG.info("Clonning server Repository");
			StringWriter s = new StringWriter();
			git.cloneRepository()
				.setURI(repository.getPath())
				.setDirectory(new File(Repositories.LOCAL.getPath()))
				.setBranch("master")
				.setBare(false)
				.setRemote("origin")
				.setNoCheckout(false)
				.setProgressMonitor(new UpdaterProgressMonitor(s))
				.call();
		} catch (Exception e) {
			LOG.error("Error during Clonnig server repository", e);
		}

		return true;
	}

	public Boolean pull() {
		Git git = getRepository(Repositories.LOCAL);
		try {
			StringWriter s = new StringWriter();
			PullResult result = git.pull().setStrategy(MergeStrategy.THEIRS).setRebase(true).setProgressMonitor(new UpdaterProgressMonitor(s)).call();
			System.out.println("Pull Progress: "+s.toString());
			LOG.debug("Pull Command Successful: "+String.valueOf(result.isSuccessful()));
			LOG.debug("Pull Command Return: "+ result.toString());
			//git.log().call();
		} catch (GitAPIException e) {
			LOG.error("Error during Pull Operation", e);
		}


		return true;
	}

	public Boolean discardLocalChanges() {
		Git git = getRepository(Repositories.LOCAL);
		try {
			LOG.info("reseting work directory: "+ git.getRepository());
			//when the changes are not staged, the command to exclude them is git Clean -fdx
			git.clean().call();
			git.reset().setMode(ResetType.HARD).call();
			git.checkout().setName("master").call();
		} catch (Exception e) {
			LOG.error("Error discarding changes", e);
		}

		return true;
	}

	@Deprecated
	public Status getStatus() {
		Git git = getRepository(Repositories.LOCAL);
		Status status = null;
		try {
			StringWriter s = new StringWriter();
			status = git.status().setProgressMonitor(new UpdaterProgressMonitor(s)).call();
			System.out.println("Status Progress: "+s.toString());
			LOG.info("work directory has changed: "+ !status.isClean());
		} catch (GitAPIException e) {
			LOG.error("Error during Status operation", e);
		}

		return status;
	}

}
