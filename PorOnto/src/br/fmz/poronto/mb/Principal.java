package br.fmz.poronto.mb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.richfaces.model.UploadItem;

import br.fmz.poronto.bean.Resultado;
import br.fmz.poronto.bean.Sinonimos;
import br.fmz.poronto.export.Excel;
import br.fmz.poronto.texto.Textos;
import br.fmz.poronto.util.ExtractText;
import br.fmz.poronto.util.Util;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.ibm.icu.util.Calendar;

public class Principal {

	private List<UploadItem> itens;
	private ArrayList<String> arquivos;
	private ArrayList<String> arquivosFrase;
	private ArrayList<Resultado> valores;
	private ArrayList<Resultado> valoresCompostos;
	private ArrayList<String> decs;
	private Sinonimos sinonimos;
	//private ArrayList<String> stopWords;
	private int minimo;
	private int minimotc;
	private boolean termosCompostos = true;
	private boolean apenasSubstantivos = true;
	private int incValue = 0;
	private String selectedTab = "config";
	private boolean blSelectTodos;
	private boolean blSelectTC;
	private TreeNode<String> rootNode;
	private TreeNode<String> rootNode_;
	private int totalDePalavrasComRepetidas;
	private int totalDePalavrasSemRepetidas;
	private String totalDePalavrasSelecionadas;
	private boolean tfidf = true;
	private boolean entropy = true;
	private boolean gerar = false;
	private Textos textos;

	private void doFake() {
		try {
			BufferedReader buReader = new BufferedReader(new InputStreamReader(
					Principal.class.getResourceAsStream("resultado(2).txt")));
			while (buReader.ready()) {
				StringTokenizer tok = new StringTokenizer(buReader.readLine(),
						"\t");
				String chave = tok.nextToken();
				int tot = Integer.parseInt(tok.nextToken());
				double tfidf = Double.parseDouble(tok.nextToken().replace(",", "."));
				boolean decs = tok.nextToken().equals("VERDADEIRO") ? true : false;
				Resultado r = new Resultado(chave,tot , tfidf,decs , null,0,new ArrayList<String>());
				valores.add(r);
				if (chave.contains(" ")) {
					Resultado r2 = new Resultado(chave,tot , tfidf,decs , null,0,new ArrayList<String>());
					valoresCompostos.add(r2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Principal() {
		itens = new ArrayList<UploadItem>();		
		arquivos = new ArrayList<String>();
		arquivosFrase = new ArrayList<String>();
		valores = new ArrayList<Resultado>();
		valoresCompostos = new ArrayList<Resultado>();
		decs = Util.getDecsList();
		sinonimos = Util.getSinonimosList();
		//stopWords = Util.getStopWordsList();
		rootNode = new TreeNodeImpl<String>();		
		rootNode_ = new TreeNodeImpl<String>();
		rootNode_.setData("Thing");
		rootNode.addChild(1, rootNode_);
	}

	public void setCheckValor(ValueChangeEvent event){
		System.out.println((Boolean)event.getNewValue());
	}
	
	private ArrayList<String> mergeValores(){
		ArrayList<String> strValores = new ArrayList<String>();
		for(Resultado resultado : valores)	{
			if(resultado.isSelected())
				strValores.add(resultado.getLemma());
		}
		for(Resultado resultado : valoresCompostos)	{
			if(resultado.isSelected())
			{
				boolean a = strValores.remove(resultado.getLemma().split(" ")[0]);
				boolean b = strValores.remove(resultado.getLemma());
				if(!a || !b)
				{
					strValores.add(resultado.getLemma());
				}
			}
		}
		return strValores;
	}
	
	public void doOnto(){		
		int count = 1;
//		for (Resultado resultado : valores) {
//			if(resultado.isSelected()){
//				TreeNode<String> t = new TreeNodeImpl<String>();
//				t.setData(resultado.getLemma());
//				rootNode_.addChild(count, t);
//				count++;
//			}
//		}
		ArrayList<String> strValores = mergeValores();
		String resultadoAnterior = "";
		TreeNode<String> folha = new TreeNodeImpl<String>();
		int countInterno = 1;
		Collections.sort(strValores);
		for (String resultado : strValores) {
				if(resultadoAnterior.equals(resultado.split(" ")[0]))
				{
					if(countInterno == 1)
					{
						if(folha.getData().contains(" "))
						{
							TreeNodeImpl<String> folha_ = new TreeNodeImpl<String>();
							folha_.setData(folha.getData());
							folha.addChild(countInterno, folha_);
							System.out.println(folha.getData());
							countInterno++;
						}
					}
					resultadoAnterior = resultado.split(" ")[0];
					TreeNode<String> t = new TreeNodeImpl<String>();
					t.setData(resultado);
					folha.addChild(countInterno, t);
					countInterno++;
				}
				else
				{
					countInterno = 1;
					resultadoAnterior = resultado.split(" ")[0];
					folha = new TreeNodeImpl<String>();
					folha.setData(resultado);
					rootNode_.addChild(count, folha);
					count++;
				}
			}
		Iterator<Entry<Object, TreeNode<String>>> it =  rootNode_.getChildren();
		while(it.hasNext()) {
			TreeNode<String> t = it.next().getValue();
			if(!t.isLeaf())
				t.setData(t.getData().split(" ")[0]);				
		}
		setSelectedTab("ontoTab");
	}
	
	public void doNovo() {
		itens = new ArrayList<UploadItem>();
		arquivos = new ArrayList<String>();
		valores = new ArrayList<Resultado>();
		valoresCompostos = new ArrayList<Resultado>();
		rootNode = new TreeNodeImpl<String>();
	}

	public void clickTodos() {
//		System.out.println(FacesContext.getCurrentInstance()
//				.getExternalContext().getRequestParameterMap().get("valor"));
		//((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect("");
	}

	public void selectTodos() {
		for (Resultado valor : valores) {
			valor.setSelected(blSelectTodos);
		}
	}

	public void selectTC() {
		for (Resultado valor : valoresCompostos) {
			valor.setSelected(blSelectTC);
		}
	}

	public String doExcel() {
		try {
			String contentType = "application/ms-excel";
			HttpServletResponse r = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			r.setHeader("Content-disposition",
					"attachment; filename=resultado.xls");
			r.setContentType(contentType);
			Excel.doExcel(valores, valoresCompostos, r.getOutputStream(),textos,arquivosFrase);
			r.getOutputStream().flush();
			r.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @link http://www.w3.org/TR/owl-ref/#MIMEType
	 * The Web Ontology Working Group has not requested a separate MIME type for OWL documents. 
	 * Instead, we recommend to use the MIME type requested by the RDF Core Working Group, namely 
	 * application/rdf+xml [RDF Concepts], or alternatively the XML MIME type application/xml.
	 * */
	public String doOwl() {
		try {
			//String contentType = "application/xml";
			String contentType = "application/rdf+xml";
			HttpServletResponse r = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			r.setHeader("Content-disposition",
					"attachment; filename=resultado.owl");
			r.setContentType(contentType);
			
			String url = "http://www.pucpr.br#";
			OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);
			RDFWriter rdfw = m.getWriter("RDF/XML-ABBREV");
			rdfw.setProperty("xmlbase", url);
			rdfw.setProperty("relativeURIs", "");
			
			Iterator<Entry<Object, TreeNode<String>>> it =  rootNode_.getChildren();
			while(it.hasNext()) {
				TreeNode<String> t = it.next().getValue();
				OntClass pai = m.createClass(url+t.getData());
				if(!t.isLeaf())
				{
					Iterator<Entry<Object, TreeNode<String>>> it2 =  t.getChildren();
					while(it2.hasNext()) {
						OntClass filho = m.createClass(url+it2.next().getValue().getData());
						pai.addSubClass(filho);
					}
				}
			}
			
			m.write(r.getOutputStream(), "RDF/XML-ABBREV", url);
			
			r.getOutputStream().flush();
			r.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void upload(UploadEvent event) {
		boolean contains = false;
		for (UploadItem uploadItem : itens) {
			if (uploadItem.getFileName().equals(
					event.getUploadItem().getFileName()))
				contains = true;
		}
		if (!contains) {
			try {
				ExtractText.doFile(event.getUploadItem().getFileName(),event.getUploadItem().getFile());
				itens.add(event.getUploadItem());
				System.out.println("Arquivo incluido = "
						+ event.getUploadItem().getFile().getPath() + " " 
						+ event.getUploadItem().getFileName());
				arquivos.add(event.getUploadItem().getFile().getPath().substring(0,
						event.getUploadItem().getFile().getPath().length() - 7)
						+ "_OUT.txt");
				arquivosFrase.add(event.getUploadItem().getFile().getPath().substring(0,
						event.getUploadItem().getFile().getPath().length() - 7)
						+ ".txt_FRASE_"+event.getUploadItem().getFileName()+".txt");
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: Mostrar erro para o usuario
			}						
		}
	}

	private void loadDiretorio(){
		//String strDir = "C:\\PauloZ\\Mulher\\";
		String strDir = "C:\\tmp\\corpus_Pediatria_txt\\TXTS\\";
		String strDirProcessados = strDir+"\\processados\\";
		//String strDir = "C:\\Processados\\teste\\";
		File diretorio = new File(strDir);
		String[] files = diretorio.list();
		for (String string : files) {
			// TODO enxugar estes if else !!!
			if(string.contains(".pdf")){
				String aux = strDir+string.substring(0,string.length() - 4);
				arquivos.add(aux + "_OUT.txt");
				try {
					ExtractText.doFile(string,new File(strDir+string));					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}else if(string.contains(".txt")){
				String origem = strDir+string;
				String destino = strDirProcessados+string;
				copiaArquivoTXT(origem, destino);
				String aux = strDirProcessados+string.substring(0,string.length() - 4);
				arquivos.add(aux + "_OUT.txt");
				try {
					ExtractText.doFile(string,new File(destino));	
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public void copiaArquivoTXT(String origem, String destino){
		try {
			FileChannel oriChannel = new FileInputStream(origem).getChannel();
			FileChannel destChannel = new FileOutputStream(destino).getChannel();
			destChannel.transferFrom(oriChannel, 0, oriChannel.size());
			oriChannel.close();
			destChannel.close();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gerarLista() {
		long i1 = Calendar.getInstance().getTimeInMillis();
		//doFake();
		//loadDiretorio();
		valores = new ArrayList<Resultado>();
		valoresCompostos = new ArrayList<Resultado>();
		setIncValue(100);
		textos = new Textos(arquivos, apenasSubstantivos,
				termosCompostos);
		TreeMap<String, Integer> totais = textos.getTotalPorLemma();
		TreeMap<String, BigDecimal[]> medidasTodos = textos.getMedidas(minimo, false);
		TreeMap<String, BigDecimal[]> medidasTermosCompostos = textos.getMedidas(minimotc, true);

		// TODO INSERIR E ARRUMAR O FILTRO DAS MEDIDAS
//		if(tfidf){
//			tfidfTodos = textos.getTfidfTodos(minimo);
//			tfidfTermosCompostos = textos.getTfidfTermosCompostos(minimotc);
//		}
//		if(entropy){
//			entropyTodos = textos.getEntropyTodos(minimo);
//			entropyTermosCompostos = textos.getEntropyTermosCompostos(minimotc);
//		}
		TreeMap<String, String> tipoComposto =  textos.getTipoComposto();
		Set<String> chaves = totais.keySet();

		for (String chave : chaves) {		
				if (totais.get(chave).intValue() >= minimo) {
//					System.out.println(chave + " "
//							+ totais.get(chave).intValue());
					Resultado resultado = new Resultado(chave, totais
							.get(chave).intValue(), 
							medidasTodos.get(chave)[0]!=null?medidasTodos.get(chave)[0].doubleValue():0, 
							decs.contains(chave.toLowerCase()),"",
							medidasTodos.get(chave)[1]!=null?medidasTodos.get(chave)[1].doubleValue():0,sinonimos.getSinonimos(chave));
					valores.add(resultado);
				}
				if (chave.contains(" ")) {
					if (totais.get(chave).intValue() >= minimotc) {
						Resultado resultado = new Resultado(chave, totais.get(
								chave).intValue(), 
								medidasTermosCompostos.get(chave)[0]!=null?medidasTermosCompostos.get(chave)[0].doubleValue():0, 
								decs.contains(chave
								.toLowerCase()),tipoComposto.get(chave),
								medidasTermosCompostos.get(chave)[1]!=null?medidasTermosCompostos.get(chave)[1].doubleValue():0,sinonimos.getSinonimos(chave));
//						System.out.println("VALOR COMPOSTO "
//								+ resultado.getLemma());
						valoresCompostos.add(resultado);
					}
				}
			}
		//}
		setTotalDePalavrasComRepetidas(textos.getTotalPalavras());
		setTotalDePalavrasSemRepetidas(textos.getTotalPorLemma().keySet().size());
		
		int totalDePalavrasSelecionadasAux = valores.size();
		for (Resultado re : valoresCompostos) {
			if(!valores.contains(re))
				totalDePalavrasSelecionadasAux++;
		}
		
		//double porcentagemSelecionadas = ;
		double auxPorcentagem = (double)(totalDePalavrasSelecionadasAux * 100)/textos.getTotalPorLemma().keySet().size();
		BigDecimal porcentagemSelecionadas = new BigDecimal(auxPorcentagem);
		porcentagemSelecionadas = porcentagemSelecionadas.setScale(2, BigDecimal.ROUND_UP);
		
		
		
		setTotalDePalavrasSelecionadas(totalDePalavrasSelecionadasAux +" ("+porcentagemSelecionadas.doubleValue()+"%)");
		setIncValue(0);
		setSelectedTab("result");
		long i2 = Calendar.getInstance().getTimeInMillis();
		System.out.println((i2-i1) + " milisegundos");
		System.out.println((((i2-i1)/1000)) + " segundos");
		System.out.println((((i2-i1)/1000))/60 + " minutos");
	}

	public ArrayList<Resultado> getValores() {
		return valores;
	}

	public void setValores(ArrayList<Resultado> valores) {
		this.valores = valores;
	}

	public List<UploadItem> getItens() {
		return itens;
	}

	public void setItens(List<UploadItem> itens) {
		this.itens = itens;
	}

	public int getMinimo() {
		return minimo;
	}

	public void setMinimo(int minimo) {
		this.minimo = minimo;
	}

	public boolean isTermosCompostos() {
		return termosCompostos;
	}

	public void setTermosCompostos(boolean termosCompostos) {
		this.termosCompostos = termosCompostos;
	}

	public boolean isApenasSubstantivos() {
		return apenasSubstantivos;
	}

	public void setApenasSubstantivos(boolean apenasSubstantivos) {
		this.apenasSubstantivos = apenasSubstantivos;
	}

	public int getIncValue() {
		return incValue;
	}

	public void setIncValue(int incValue) {
		this.incValue = incValue;
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public ArrayList<Resultado> getValoresCompostos() {
		return valoresCompostos;
	}

	public void setValoresCompostos(ArrayList<Resultado> valoresCompostos) {
		this.valoresCompostos = valoresCompostos;
	}

	public int getMinimotc() {
		return minimotc;
	}

	public void setMinimotc(int minimotc) {
		this.minimotc = minimotc;
	}

	public boolean isBlSelectTodos() {
		return blSelectTodos;
	}

	public void setBlSelectTodos(boolean blSelectTodos) {
		this.blSelectTodos = blSelectTodos;
	}

	public boolean isBlSelectTC() {
		return blSelectTC;
	}

	public void setBlSelectTC(boolean blSelectTC) {
		this.blSelectTC = blSelectTC;
	}

	public TreeNode<String> getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode<String> rootNode) {
		this.rootNode = rootNode;
	}

	public int getTotalDePalavrasComRepetidas() {
		return totalDePalavrasComRepetidas;
	}

	public void setTotalDePalavrasComRepetidas(int totalDePalavrasComRepetidas) {
		this.totalDePalavrasComRepetidas = totalDePalavrasComRepetidas;
	}

	public int getTotalDePalavrasSemRepetidas() {
		return totalDePalavrasSemRepetidas;
	}

	public void setTotalDePalavrasSemRepetidas(int totalDePalavrasSemRepetidas) {
		this.totalDePalavrasSemRepetidas = totalDePalavrasSemRepetidas;
	}

	public String getTotalDePalavrasSelecionadas() {
		return totalDePalavrasSelecionadas;
	}

	public void setTotalDePalavrasSelecionadas(String totalDePalavrasSelecionadas) {
		this.totalDePalavrasSelecionadas = totalDePalavrasSelecionadas;
	}
	
	public static void main(String[] args) {
		Principal p = new Principal();
		p.loadDiretorio();
	}

	public boolean isTfidf() {
		return tfidf;
	}

	public void setTfidf(boolean tfidf) {
		this.tfidf = tfidf;
	}

	public boolean isEntropy() {
		return entropy;
	}

	public void setEntropy(boolean entropy) {
		this.entropy = entropy;
	}

	public boolean isGerar() {
//		this.gerar = itens.size() <= 0;
		System.out.println(gerar + " " + itens.size());
		return gerar;
	}

	public void setGerar(boolean gerar) {
		this.gerar = gerar;
	}	
	
	

}
