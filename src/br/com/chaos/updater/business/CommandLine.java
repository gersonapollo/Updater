package br.com.chaos.updater.business;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chaos.updater.view.UpdaterScreen;

public class CommandLine {
	
	private final static Logger LOG = LoggerFactory.getLogger(CommandLine.class);

	public static void execFinancialTerminal() {
		String command = "C:/CLIENT/open.bat";
		UpdaterScreen.setTaskName("Opening Application - TF");
	//	try {
			LOG.info("Opening Application - TF");
			execCommand(command);
//		} catch (IOException e) {
//			LOG.error("Error opening Application - TF", e);
//		}
	}
	
	public static void mapServerFolder() {
//		UpdaterScreen.setTaskName("Mapping server folder");
		String command = "c:\\windows\\system32\\net.exe use y: \\\\a6505s001/dados /user:root root /PERSISTENT:YES";
		LOG.info("Executing command Net Use to map server: " + command);
		execCommand(command);
	}

//	private synchronized static String execCommand(final String commandLine) throws IOException {
//		boolean success = false;
//		String result;
//		Process p;
//		BufferedReader input;
//		StringBuffer cmdOut = new StringBuffer();
//		String lineOut = null;
//		int numberOfOutline = 0;
//		try {
//			p = Runtime.getRuntime().exec(commandLine);
//			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			while ((lineOut = input.readLine()) != null) {
//				if (numberOfOutline > 0) {
//					cmdOut.append("\n");
//				}
//				cmdOut.append(lineOut);
//				numberOfOutline++;
//			}
//			result = cmdOut.toString();
//			success = true;
//			input.close();
//		} catch (IOException e) {
//			result = String.format("Error trying to execute command %s. Erro: %s", commandLine, e.toString());
//			LOG.error("Error trying to execute command %s. Erro: %s", commandLine, e.toString(), e);
//		}
//		if (!success) {
//			throw new IOException(result);
//		}
//		return result;
//	}
	
	public synchronized static void execCommand(final String commandLine) {
		Process process;
		try {
			process = Runtime.getRuntime().exec(commandLine);
			Scanner leitor = new Scanner(process.getInputStream());
			while (leitor.hasNextLine()) {
				LOG.info(leitor.nextLine());
//				System.out.println(leitor.nextLine());
			}
			if(process.waitFor() == 0) {
				LOG.debug("Result of Net Use: Sucess");
			}
		} catch (IOException e) {
			LOG.error("I/O Error executing command Net Use", e);
		} catch (InterruptedException e) {
			LOG.error("Error executing command Net Use", e);
		}
		
		
	}

}
