package br.com.chaos.updater.repository;

public enum Repositories {

	SERVER_CLIENT("/\\a6505s001/dados/CTAC/AtualizacoesTF/TF_SERVER/AGENCIA/CLIENT"),
	SERVER_LOCAL("/\\a6505s001/dados/CTAC/AtualizacoesTF/TF_SERVER/LOCAL/CLIENT"),
//	SERVER_LOCAL("/\\D4253E8870/EAGE/Updater/TF_SERVER/LOCAL/CLIENT"),
	LOCAL("C:/CLIENT");

	private String path;

	Repositories(String path){
		this.path = path;
	}

	public String getPath(){
		return this.path;
	}
}
