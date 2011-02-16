package br.fmz.poronto.texto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class Texto {
	
	private ArrayList<Palavra> palavras;
	private boolean substantivos;
	private TreeMap<String, Integer> totaisPorLemma;
	
	public Texto(File nomeArq, boolean substantivos, boolean termosCompostos){
		this.palavras = new ArrayList<Palavra>();
		this.substantivos = substantivos;
		leArquivo(nomeArq);
		if(termosCompostos)
			selecionaTermosCompostos2();
	}

	/**
	private void selecionaTermosCompostos() {
		Palavra palAnterior = new Palavra();		
		Palavra palAtual = new Palavra();
		ArrayList<Palavra> palavrasAux = new ArrayList<Palavra>();
		for (Palavra palavra : palavras) {
			palAtual = palavra;
			if(palAnterior.getTag().equals("NOM") && palAtual.getTag().equals("ADJ"))
			{
				String composto = palAnterior.getPalavra() + " " + palAtual.getPalavra();
				palavrasAux.add(new Palavra(composto.toLowerCase(),Palavra.COMPOSTO,composto.toLowerCase()));
			}
			palAnterior = palavra;
		}
		palavras.addAll(palavrasAux);
	}
	**/
	
	private void selecionaTermosCompostos2() {
		ArrayList<Integer> substantivos = new ArrayList<Integer>();
		ArrayList<Palavra> palavrasAux = new ArrayList<Palavra>();
		for (Palavra palavra : palavras) {
			if(palavra.getTag().equals(Palavra.SUBSTANTIVO))
				substantivos.add(palavras.indexOf(palavra));
		}
		for (Integer subIndex : substantivos) {
			Palavra p1 = palavras.get(subIndex);
			Palavra p2 = (subIndex+1) < palavras.size() ? palavras.get(subIndex+1) : null;
			Palavra p3 = (subIndex+2) < palavras.size() ? palavras.get(subIndex+2) : null;
			Palavra p4 = (subIndex+3) < palavras.size() ? palavras.get(subIndex+3) : null;
			Palavra p5 = (subIndex+4) < palavras.size() ? palavras.get(subIndex+4) : null;
			
			//SU AJ
			if(p2 != null && p2.getTag().equals(Palavra.ADJETIVO)){
				String composto = p1.getPalavra() + " " + p2.getPalavra();
				palavrasAux.add(new Palavra(composto.toLowerCase(),Palavra.COMPOSTO,composto.toLowerCase(),"SU AJ"));
			}
			//SU PR SU
			if( (p2 != null && (p2.getTag().equals(Palavra.PREPOSICAO) ||  
					            p2.getTag().equals(Palavra.DETERMINANTE) || 
					            p2.getTag().equals(Palavra.PREPOSICAO_DET)) ) &&
				(p3 != null && p3.getTag().equals(Palavra.SUBSTANTIVO))){
				String composto = p1.getPalavra() + " " + p2.getPalavra()+ " " + p3.getPalavra();
				palavrasAux.add(new Palavra(composto.toLowerCase(),Palavra.COMPOSTO,composto.toLowerCase(),"SU PR SU"));
			}
			//SU PR AD SU
			if( (p2 != null && (p2.getTag().equals(Palavra.PREPOSICAO) ||  
		            			p2.getTag().equals(Palavra.DETERMINANTE) || 
		            			p2.getTag().equals(Palavra.PREPOSICAO_DET))) &&
				(p3 != null && p3.getTag().equals(Palavra.ADJETIVO)) &&
				(p4 != null && p4.getTag().equals(Palavra.SUBSTANTIVO))){
				String composto = p1.getPalavra() + " " + p2.getPalavra()+ " " + 
						p3.getPalavra()+ " " + p4.getPalavra();
				palavrasAux.add(new Palavra(composto.toLowerCase(),Palavra.COMPOSTO,composto.toLowerCase(),"SU PR AD SU"));
			}
			//p1 p2 p3 p4 p5
			//SU PR SU PR SU
			if( (p2 != null && (p2.getTag().equals(Palavra.PREPOSICAO) ||  
		            			p2.getTag().equals(Palavra.DETERMINANTE) || 
		            			p2.getTag().equals(Palavra.PREPOSICAO_DET))) &&
				(p3 != null && p3.getTag().equals(Palavra.SUBSTANTIVO)) &&
				(p4 != null && (p4.getTag().equals(Palavra.PREPOSICAO) ||  
            					p4.getTag().equals(Palavra.DETERMINANTE) || 
            					p4.getTag().equals(Palavra.PREPOSICAO_DET))) &&
				(p5 != null && p5.getTag().equals(Palavra.SUBSTANTIVO))){
				String composto = p1.getPalavra() + " " + p2.getPalavra()+ " " + 
						p3.getPalavra()+ " " + p4.getPalavra()+ " " + p5.getPalavra();
				palavrasAux.add(new Palavra(composto.toLowerCase(),Palavra.COMPOSTO,composto.toLowerCase(),"SU PR SU PR SU"));
			}
			
				
		}
		palavras.addAll(palavrasAux);
	}


	private ArrayList<Palavra> leArquivo(File nomeArq){
		try {
			BufferedReader buf = new BufferedReader(new FileReader(nomeArq));
			while(buf.ready()){
				StringTokenizer tok = new StringTokenizer(buf.readLine(),"\t");
				Palavra palavra = new Palavra(tok.nextToken(),tok.nextToken(),tok.nextToken(),"");
				palavras.add(palavra);
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return palavras;
	}

	public ArrayList<Palavra> getPalavras() {
		return palavras;
	}
	
	public ArrayList<Palavra> getPalavrasByTag(String tag) {
		ArrayList<Palavra> palavrasAux = new ArrayList<Palavra>();
		for (Palavra palavra : palavras) {
			if(palavra.getTag().equals(tag))
				palavrasAux.add(palavra);
		}
		return palavrasAux;
	}
	
	public TreeMap<String, Integer> getTotalPorLemma(){
		if(totaisPorLemma == null){
			totaisPorLemma = new TreeMap<String, Integer>();
			ArrayList<Palavra> palavrasArray;
			if(substantivos)
			{
				palavrasArray = getPalavrasByTag(Palavra.SUBSTANTIVO);
				palavrasArray.addAll(getPalavrasByTag(Palavra.COMPOSTO));
			}
			else
				palavrasArray = palavras;
			for (Palavra palavra : palavrasArray) {
				if(totaisPorLemma.get(palavra.getLemma()) != null)
					totaisPorLemma.put(palavra.getLemma(),new Integer(totaisPorLemma.get(palavra.getLemma()).intValue() + 1));
				else
					totaisPorLemma.put(palavra.getLemma(),new Integer(1));
			}
		}
		return totaisPorLemma;
	}
	
	public boolean hasPalavra(String lemma){
		for (Palavra palavra : palavras) {
			if(palavra.getLemma().equals(lemma))
				return true;
		}
		return false;
	}
	
	
}
