package br.fmz.poronto.texto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Textos {

	private ArrayList<Texto> textos;
	private int totalDeTermos;
	private static ArrayList<ArrayList<String>> arquivos;
	private static ArrayList<String> nomeArquivos;
	
	public Textos(List<String> itens, boolean substantivos,boolean termosCompostos) {
		textos = new ArrayList<Texto>();		
		carregaTextos(itens, substantivos,termosCompostos);
	}

	
	private void carregaTextos(List<String> itens, boolean substantivos, boolean termosCompostos) {		
		for (String item : itens) {
			Texto texto = new Texto(new File(item), substantivos,termosCompostos);
			textos.add(texto);
			this.totalDeTermos += texto.getPalavras().size();
		}
		
	}
	
	public TreeMap<String, String> getTipoComposto(){
		TreeMap<String, String> tipoComposto = new TreeMap<String, String>();
		for (Texto texto : textos) {
			ArrayList<Palavra> palavras = texto.getPalavras();
			for (Palavra palavra : palavras) {
				tipoComposto.put(palavra.getLemma(),palavra.getTipoComposto());
			}			
		}
		return tipoComposto;
	}
	
	public TreeMap<String, Integer> getTotalPorLemma(){
		TreeMap<String, Integer> totaisPorLemma = new TreeMap<String, Integer>();
		for (Texto texto : textos) {
			TreeMap<String, Integer> totaisPorLemmaPorTexto = texto.getTotalPorLemma();
			Set<String> lemmas = totaisPorLemmaPorTexto.keySet();
			for (String lemma : lemmas) {
				if(totaisPorLemma.get(lemma) != null)
					totaisPorLemma.put(lemma,new Integer(totaisPorLemmaPorTexto.get(lemma)).intValue() + 
							new Integer(totaisPorLemma.get(lemma)).intValue());
				else
					totaisPorLemma.put(lemma,new Integer(totaisPorLemmaPorTexto.get(lemma)));
			}
		}
		return totaisPorLemma;
	}

	//Baseado no Entropy implementado no TextToOnto
	/**
	 * entropy = 1 - sum_j=1,..,n ((p_ij * log (p_ij)) / log N) 
     * where :
     * p_ij = tf_ij / gf_i, 
     * tf_ij = frequency of term i in document j, 
     * gf_i = total number of times term i occurs in the entire corpus, 
     * N = number of documents in the corpus
     **/
	private TreeMap<String, BigDecimal> getEntropy(int minimo,boolean apenasTermosCompostos){
		TreeMap<String, BigDecimal> entropy = new TreeMap<String, BigDecimal>();
		TreeMap<String, Integer> totaisPorLemma = getTotalPorLemma(); 
		Set<String> lemmas = totaisPorLemma.keySet();
		System.out.println("[getEntropy]totaisPorLemma " + totaisPorLemma.size());		
		for (String lemma : lemmas) {
			int gfi = totaisPorLemma.get(lemma).intValue();
			double sum = 0;
			boolean ehUmTermoComposto = lemma.contains(" ");
			if(apenasTermosCompostos && !ehUmTermoComposto)
				continue;
			
			if(gfi >= minimo)
			{
				for (Texto texto : textos) {
					if(texto.getTotalPorLemma().containsKey(lemma)){
						int tfij = texto.getTotalPorLemma().get(lemma);
		                double quotient=(double)tfij/gfi;
		                sum+=quotient*Math.log(quotient);
					}
				}			
				double aux = 1-sum/Math.log(lemmas.size());
				BigDecimal tot = new BigDecimal(aux).setScale(2, BigDecimal.ROUND_UP);
				System.out.println(lemma + " " + tot.doubleValue());
				entropy.put(lemma,tot);
			}						
		}
		System.out.println("Entropy " + entropy.size());
		return entropy;
	}
	
	public TreeMap<String, BigDecimal[]> getMedidas(int minimo,boolean apenasTermosCompostos){
		TreeMap<String, BigDecimal[]> resultado = new TreeMap<String, BigDecimal[]>();
		TreeMap<String, Integer> totaisPorLemma = getTotalPorLemma(); 
		Set<String> lemmas = totaisPorLemma.keySet();
		System.out.println("[getMedidas]totaisPorLemma " + totaisPorLemma.size());
		StringBuilder arqout = new StringBuilder();
		for (String lemma : lemmas) {
			boolean ehUmTermoComposto = lemma.contains(" ");
			int gfi = totaisPorLemma.get(lemma).intValue();
			if(apenasTermosCompostos && !ehUmTermoComposto)
				continue;
			if(gfi >= minimo)
			{
				int totalLemmaTodosTextos = 0;
				double sum = 0;
				for (Texto texto : textos) {
					if(texto.getTotalPorLemma().containsKey(lemma)){
						int tfij = texto.getTotalPorLemma().get(lemma);
						totalLemmaTodosTextos++;
		                double quotient=(double)tfij/gfi;
		                sum+=quotient*Math.log(quotient);
					}
						
				}
				BigDecimal[] arrResult = new BigDecimal[2];
				BigDecimal totTfidf = new BigDecimal( Math.log(lemmas.size()/totalLemmaTodosTextos)).setScale(2, BigDecimal.ROUND_UP);
				arrResult[0] = totTfidf;
				
				
				double aux = 1-sum/Math.log(lemmas.size());
				BigDecimal totEntropy = new BigDecimal(aux).setScale(2, BigDecimal.ROUND_UP);
				arrResult[1] = totEntropy;
				arqout.append(lemma + ";" + totTfidf.doubleValue() + ";" + totEntropy.doubleValue()+"\n");
				
				resultado.put(lemma,arrResult);
			}
		}
//		try {
//			FileOutputStream fileout = new FileOutputStream("c:\\temp\\"+Calendar.getInstance().getTimeInMillis()+".txt");
//			fileout.write(arqout.toString().getBytes());
//			fileout.close();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("Resultado Medidas: " + resultado.size());
		return resultado;
	}
	
	//Baseado no tfidf implementado no TextToOnto
	private TreeMap<String, BigDecimal> getTfidf(int minimo,boolean apenasTermosCompostos){
		TreeMap<String, BigDecimal> tfidf = new TreeMap<String, BigDecimal>();
		TreeMap<String, Integer> totaisPorLemma = getTotalPorLemma(); 
		Set<String> lemmas = totaisPorLemma.keySet();
		System.out.println("[getTfidf]totaisPorLemma " + totaisPorLemma.size());
		for (String lemma : lemmas) {
			boolean ehUmTermoComposto = lemma.contains(" ");
			if(apenasTermosCompostos && !ehUmTermoComposto)
				continue;
			if(totaisPorLemma.get(lemma).intValue() >= minimo)
			{
				int totalLemmaTodosTextos = 0;
				for (Texto texto : textos) {
					if(texto.getTotalPorLemma().containsKey(lemma))
						totalLemmaTodosTextos++;
				}
				BigDecimal tot = new BigDecimal( Math.log(lemmas.size()/totalLemmaTodosTextos)).setScale(2, BigDecimal.ROUND_UP);
				System.out.println(lemma + " " + tot.doubleValue());
				tfidf.put(lemma,tot);
			}
		}
		System.out.println("TFIDF " + tfidf.size());
		return tfidf;
	}

	public TreeMap<String, BigDecimal> getTfidfTermosCompostos(int minimo){
		return getTfidf(minimo, true);
	}
	
	public TreeMap<String, BigDecimal> getTfidfTodos(int minimo){
		return getTfidf(minimo, false);
	}
	
	public TreeMap<String, BigDecimal> getEntropyTermosCompostos(int minimo){
		return getEntropy(minimo, true);
	}
	
	public TreeMap<String, BigDecimal> getEntropyTodos(int minimo){
		return getEntropy(minimo, false);
	}
	
	//Baseado em : http://www.cs.helsinki.fi/group/dime/lado/s01/exerc/samples.txt	
	public TreeMap<String, BigDecimal> getTfidf2(int minimo){
		TreeMap<String, BigDecimal> tfidf = new TreeMap<String, BigDecimal>();
		TreeMap<String, Integer> totaisPorLemma = getTotalPorLemma(); 
		Set<String> lemmas = totaisPorLemma.keySet();
		for (String lemma : lemmas) {
			if(totaisPorLemma.get(lemma).intValue() >= minimo)
			{
				int totalLemmaTodosTextos = 0;
				double tfidfD = 0;
				for (Texto texto : textos) {
					if(texto.getTotalPorLemma().containsKey(lemma))
						totalLemmaTodosTextos++;
				}
				for (Texto texto : textos) {
					if(texto.getTotalPorLemma().containsKey(lemma)){
						tfidfD += texto.getTotalPorLemma().get(lemma).doubleValue() * Math.log(textos.size()/totalLemmaTodosTextos);
					}						
				}
				tfidf.put(lemma,new BigDecimal(tfidfD));
			}
		}
		return tfidf;
	}
	
	public int getTotalPalavras(){
		int totalDePalavras = 0;
		for (Texto texto : textos) {
			totalDePalavras += texto.getPalavras().size();
		}
		return totalDePalavras;
	}
	
	public static ArrayList<String> getFrases(String lemma, ArrayList<String> arquivosFrase){	
		ArrayList<String> retorno = new ArrayList<String>();
		int TOTAL = 10;
		int i = 0;
		try {
			loadArquivos(arquivosFrase);
			
			for (ArrayList<String> arq : arquivos) {
				if(retorno.size() >= TOTAL)
					return retorno;
				for (String linha : arq) {									
					if(linha.toLowerCase().contains(" "+lemma.toLowerCase()+" ")){
						String aux = "";
						String aux2[] = linha.toLowerCase().split(" "+lemma.toLowerCase()+" ");
						if(aux2.length >= 2) {
							aux += " \"..."+ aux2[0].substring(aux2[0].length()-(aux2[0].length()>100?100:aux2[0].length()), aux2[0].length()) + " [" +
								lemma.toLowerCase() + "] " +aux2[1].substring(0, aux2[1].length()>100?100:aux2[1].length()) + "...\"";
						}
						else{
							if(linha.toLowerCase().length() >= 200)
								aux = linha.toLowerCase().substring(0, 200);
							else
								aux = linha.toLowerCase();
						}
						retorno.add(nomeArquivos.get(i).split("\\.")[0]+ " - "+ aux);
						break;
					}
				}
				i++;
			}
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	private static void loadArquivos(ArrayList<String> arquivosFrase){
		if(arquivos == null){
			try {				
				arquivos = new ArrayList<ArrayList<String>>();
				nomeArquivos = new ArrayList<String>();
				for (String arq : arquivosFrase) {
					ArrayList<String> arquivo = new ArrayList<String>();
					BufferedReader buReader = new BufferedReader(new InputStreamReader(
							new FileInputStream(arq)));
					while (buReader.ready()){
						arquivo.add(buReader.readLine());
					}
					arquivos.add(arquivo);
					nomeArquivos.add(arq.split("FRASE_")[1]);
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

