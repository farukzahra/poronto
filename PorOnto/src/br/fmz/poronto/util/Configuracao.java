package br.fmz.poronto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracao {

	private Properties propriedade;
	private static Configuracao theInstance;
	
	public Configuracao(){
		propriedade = new Properties();
	}

	public static Configuracao getInstance() {
		if (theInstance == null)
			theInstance = new Configuracao();
		return theInstance;
	}

	public void loadConfig(String arqConf){
		FileInputStream fis = null;
		File arquuivoConf = new File(arqConf);
		try {
			fis = new FileInputStream(arquuivoConf);
			propriedade.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(Properties propriedade) {
		this.propriedade = propriedade;
	}

}
