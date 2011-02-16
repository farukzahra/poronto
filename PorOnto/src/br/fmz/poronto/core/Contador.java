package br.fmz.poronto.core;
import java.util.ArrayList;
import java.util.TreeMap;

import br.fmz.poronto.texto.Palavra;


public class Contador {
	
	
	public Contador(){};
	
	public void contaPorTag(ArrayList<Palavra> palavras){
		TreeMap<String, Integer> totais = new TreeMap<String, Integer>();
		for (Palavra palavra : palavras) {
			if(totais.get(palavra.getTag()) != null)
				totais.put(palavra.getTag(),new Integer(totais.get(palavra.getTag()).intValue() + 1));
			else
				totais.put(palavra.getTag(),new Integer(1));
		}
//		Set<String> chaves = totais.keySet();
//		for (String chave : chaves) {
//			System.out.println(chave + " " + Palavra.getTagDescription(chave) + " : " + totais.get(chave));	
//		}		
	}
	
	public TreeMap<String, Integer>  contaPorLemma(ArrayList<Palavra> palavras){
		TreeMap<String, Integer> totais = new TreeMap<String, Integer>();
		for (Palavra palavra : palavras) {
			if(totais.get(palavra.getLemma()) != null)
				totais.put(palavra.getLemma(),new Integer(totais.get(palavra.getLemma()).intValue() + 1));
			else
				totais.put(palavra.getLemma(),new Integer(1));
		}
		return totais;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		}
}
