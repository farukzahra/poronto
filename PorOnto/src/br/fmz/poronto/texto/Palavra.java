package br.fmz.poronto.texto;

import java.util.HashMap;

public class Palavra {

	private String palavra;
	private String tag;
	private String lemma;
	private String tipoComposto;

	public static final String SUBSTANTIVO = "NOM";
	public static final String PREPOSICAO = "PRP";
	public static final String PREPOSICAO_DET = "PRP+DET";
	public static final String DETERMINANTE = "DET";
	public static final String ADJETIVO = "ADJ";
	public static final String COMPOSTO = "COMPOSTO";

	// TODO USAR enum
	public static final HashMap<String, String> tags = new HashMap<String, String>();
	static {
		tags.put("ADJ", "Adjetivo");
		tags.put("ADV", "Advérbio");
		tags.put("DET", "Determinante");
		tags.put("CARD", "Número Cardinal / Ordinal");
		tags.put("NOM", "Nome Comum / Próprio");
		tags.put("P", "Pronome");
		tags.put("PRP", "Preposição");
		tags.put("V", "Verbo");
		tags.put("I", "Interjeição");
		tags.put("VIRG", "Separadores dentro da oraçao");
		tags.put("SENT", "Separadores de oraçoes");
	}

	public Palavra() {
		setTag("");
	}

	public Palavra(String palavra, String tag, String lemma, String tipoComposto) {
		super();
		this.palavra = palavra.toLowerCase();
		this.tag = tag;
		this.lemma = lemma.toLowerCase();
		this.tipoComposto = tipoComposto;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLemma() {
		if ("<unknown>".equals(lemma))
			return palavra;
		else
			return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	@Override
	public String toString() {
		return this.palavra + "-" + this.tag + "-" + this.lemma;
	}

	public String getTagDescription() {
		return tags.get(tag);
	}

	public static String getTagDescription(String tag_) {
		return tags.get(tag_);
	}

	public String getTipoComposto() {
		return tipoComposto;
	}

	public void setTipoComposto(String tipoComposto) {
		this.tipoComposto = tipoComposto;
	}

}
