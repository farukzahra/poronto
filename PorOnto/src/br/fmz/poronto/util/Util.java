package br.fmz.poronto.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import br.fmz.poronto.bean.Sinonimo;
import br.fmz.poronto.bean.Sinonimos;

public class Util {

	public static ArrayList<String> getStopWordsList() {
		return loadList("StopWords.txt");
	}

	public static ArrayList<String> getDecsList() {
		return loadList("Decs.txt");
	}
	
	public static Sinonimos getSinonimosList() {
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					Util.class.getResourceAsStream("Thesaurus.txt")));
			//ArrayList<Sinonimos> lista = new ArrayList<Sinonimos>();
			Sinonimos sinonimos = new Sinonimos();
			while (buf.ready()){
				StringTokenizer toks = new StringTokenizer(buf.readLine().toLowerCase(),";");
				//lista.add();
				sinonimos.add(new Sinonimo(toks));
			}
			return sinonimos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<String> loadList(String arq) {
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					Util.class.getResourceAsStream(arq)));
			ArrayList<String> lista = new ArrayList<String>();
			while (buf.ready())
				lista.add(buf.readLine().toLowerCase());
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
