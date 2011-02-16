package br.fmz.poronto.core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;


public class ConceitosBaseline {
	
	public static TreeMap<String,String> conceitosBase;
	
	static{
		conceitosBase = new TreeMap<String,String>();
		try {
			BufferedReader buf = new BufferedReader(new FileReader("conceitos_baseline.txt"));
			while(buf.ready()){
				String aux = buf.readLine();
				conceitosBase.put(aux.toLowerCase(),aux);
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
