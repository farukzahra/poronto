package br.fmz.poronto.texto;

public class TelaVO {
	
	public String palavra;
	public double tfidf;
	public double total;
	
	
	public TelaVO(String palavra, double tfidf, double total) {
		super();
		this.palavra = palavra;
		this.tfidf = tfidf;
		this.total = total;
	}
	public String getPalavra() {
		return palavra;
	}
	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}
	public double getTfidf() {
		return tfidf;
	}
	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	

}
