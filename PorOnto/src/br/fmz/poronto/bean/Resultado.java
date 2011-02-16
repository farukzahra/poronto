package br.fmz.poronto.bean;

import java.util.ArrayList;

public class Resultado{

	private String lemma;
	private int total;
	private double tfidf;
	private double entropy;
	private boolean decs;
	private boolean selected;
	private String tipoComposto;
	private ArrayList<String>  sinonimos;
	private boolean sinonimosB;
	private String sinonimoToolTip; 

	public Resultado(String lemma, int total, double tfidf, boolean decs,
			String tipoComposto, double entropy, ArrayList<String>  sinonimos) {
		super();
		this.lemma = lemma;
		this.tfidf = tfidf;
		this.total = total;
		this.decs = decs;
		this.tipoComposto = tipoComposto;
		this.entropy = entropy;
		this.sinonimos = sinonimos;
	}

	public Resultado() {
		// TODO Auto-generated constructor stub
	}
	
	

	public String getSinonimoToolTip() {
		return sinonimos != null && sinonimos.size() > 0 ? sinonimos.get(0)+"..." : "";
	}

	public void setSinonimoToolTip(String sinonimoToolTip) {
		this.sinonimoToolTip = sinonimoToolTip;
	}

	public boolean isSinonimosB() {
		return sinonimos != null && sinonimos.size() > 0 ? true : false;
	}

	public void setSinonimosB(boolean sinonimosB) {
		this.sinonimosB = sinonimosB;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public boolean isDecs() {
		return decs;
	}

	public void setDecs(boolean decs) {
		this.decs = decs;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getTipoComposto() {
		return tipoComposto;
	}

	public void setTipoComposto(String tipoComposto) {
		this.tipoComposto = tipoComposto;
	}

	public ArrayList<String>  getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(ArrayList<String>  sinonimos) {
		this.sinonimos = sinonimos;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((Resultado)obj).getLemma().equals(getLemma());
	}
}
