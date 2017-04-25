package br.com.chaos.updater.view;

import java.io.StringWriter;

import org.eclipse.jgit.lib.TextProgressMonitor;

import br.com.chaos.updater.business.CommandLine;

public class UpdaterProgressMonitor extends TextProgressMonitor {
	
	public UpdaterProgressMonitor(StringWriter writer) {
		super(writer);
	}

	@Override
	protected void onUpdate(String taskName, int cmp, int totalWork, int pcnt) {
		try {
			changeScreenStatus(taskName, pcnt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.onUpdate(taskName, cmp, totalWork, pcnt);
	}
	
	private void changeScreenStatus(String taskName, int pcnt) throws InterruptedException {
		System.out.println("UpdateProgressMonitor: " + taskName +"Progress %: "+ pcnt);
		UpdaterScreen.setTaskName(taskName);
		UpdaterScreen.setProgress(new Double(pcnt*0.01));

		if("Updating references".equals(taskName)) {
			UpdaterScreen.setProgress(new Double(-1));
			CommandLine.execFinancialTerminal();
		}
		//Thread.sleep(500);
	}
}
