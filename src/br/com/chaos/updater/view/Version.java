package br.com.chaos.updater.view;

public enum Version {
	
	UPDATER_VERSION("01.00.06");

	
	String versionNumber;
	
	Version(String versionNumber){
		this.versionNumber = versionNumber;
	}
	
	public String getNumber(){
		return this.versionNumber;
	}
	
}
