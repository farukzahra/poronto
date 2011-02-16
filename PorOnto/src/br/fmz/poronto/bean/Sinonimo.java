package br.fmz.poronto.bean;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Sinonimo {
	private ArrayList<String> sinonimo;
	public Sinonimo(StringTokenizer toks) {
		sinonimo = new ArrayList<String>();
			
		while(toks.hasMoreElements())
			sinonimo.add(toks.nextToken());
	}

	public boolean isSinonimo(String sinonimoCompare){
		return sinonimo.contains(sinonimoCompare);	
	}

	public ArrayList<String> getSinonimo() {
		return sinonimo;
	}

	public static String toString(ArrayList<String> sinonimo) {
		String retorno = "";
		//int i = 1;
		for (String sin : sinonimo) {
			//retorno += sin+";"+((i%4==0)?"\n":"");
			retorno += sin+";";
			//i++;
		}
		return retorno;
	}	
}
