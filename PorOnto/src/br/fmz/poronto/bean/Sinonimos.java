package br.fmz.poronto.bean;

import java.util.ArrayList;

public class Sinonimos {
	private ArrayList<Sinonimo> sinonimos;
	
	public Sinonimos(){
		sinonimos = new ArrayList<Sinonimo>();
	}

	public void add(Sinonimo sinonimo) {
		sinonimos.add(sinonimo);
	}

	public ArrayList<String>  getSinonimos(String sinonimoCompare) {
		for (Sinonimo sinonimo : sinonimos) {
			if(sinonimo.isSinonimo(sinonimoCompare)){
				return sinonimo.getSinonimo();
			}
		}
		return new ArrayList<String>();
	}
}
