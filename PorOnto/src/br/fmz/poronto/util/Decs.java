package br.fmz.poronto.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Decs {
	
	public static ArrayList<String> getDecsList(){
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(Decs.class.getResourceAsStream("Decs.txt")));
			ArrayList<String> lista = new ArrayList<String>();
			while (buf.ready())
				lista.add(buf.readLine());
			return lista;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try {			
			BufferedReader buf = new BufferedReader(new FileReader("Decs.txt"));
			while (buf.ready()) {
				String aux = buf.readLine();
				if(aux.length() > 1)
					System.out.println(aux.split("use")[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
