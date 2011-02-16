package br.fmz.poronto.export;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import br.fmz.poronto.bean.Resultado;
import br.fmz.poronto.texto.Textos;

public class Excel {
	
	private static void setSheet(ArrayList<Resultado> valores,String nome,HSSFWorkbook wb, 
			Textos textos,ArrayList<String> arquivosFrase, boolean tc){
		Collections.sort(valores,new Comparator<Resultado>(){@Override
			public int compare(Resultado o1, Resultado o2) {
//				if(o1.getTotal() < o2.getTotal())
//					return 1;
//				else
//					return -1;
				return o1.getLemma().compareTo(o2.getLemma());
			}});
			
		    
		    HSSFSheet sheet = wb.createSheet(nome);
		    
		    HSSFCellStyle blackStyle = wb.createCellStyle();
		    HSSFCellStyle alternateWhiteStyle = wb.createCellStyle();
		    HSSFCellStyle alternateGrayStyle = wb.createCellStyle();
			HSSFFont fontHeader = wb.createFont();

			fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontHeader.setFontName(HSSFFont.FONT_ARIAL);
			fontHeader.setFontHeightInPoints((short)10);
			fontHeader.setColor(HSSFColor.WHITE.index);
		    
		    blackStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			blackStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			blackStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			blackStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			blackStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			blackStyle.setRightBorderColor(HSSFColor.BLACK.index);
			blackStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			blackStyle.setTopBorderColor(HSSFColor.BLACK.index);
			blackStyle.setFillForegroundColor(HSSFColor.BLACK.index);
			blackStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			blackStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			blackStyle.setFont(fontHeader);
			
			alternateGrayStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			alternateGrayStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			alternateGrayStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			alternateGrayStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			alternateGrayStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			alternateGrayStyle.setRightBorderColor(HSSFColor.BLACK.index);
			alternateGrayStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			alternateGrayStyle.setTopBorderColor(HSSFColor.BLACK.index);
			alternateGrayStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			alternateGrayStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			alternateWhiteStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			alternateWhiteStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			alternateWhiteStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			alternateWhiteStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			alternateWhiteStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			alternateWhiteStyle.setRightBorderColor(HSSFColor.BLACK.index);
			alternateWhiteStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			alternateWhiteStyle.setTopBorderColor(HSSFColor.BLACK.index);
			alternateWhiteStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			alternateWhiteStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    
		    HSSFRow row = sheet.createRow((short)0);
		    HSSFCell cellCabec1 = row.createCell((short)0);
		    cellCabec1.setCellValue(new HSSFRichTextString("Palavra"));
		    cellCabec1.setCellStyle(blackStyle);
		    
		    HSSFCell cellCabec2 = row.createCell((short)1);
		    cellCabec2.setCellValue(new HSSFRichTextString("Total"));
		    cellCabec2.setCellStyle(blackStyle);
		    
		    HSSFCell cellCabec3 = row.createCell((short)2);
		    cellCabec3.setCellValue(new HSSFRichTextString("Tf-Idf"));
		    cellCabec3.setCellStyle(blackStyle);
		    
		    HSSFCell cellCabec4 = row.createCell((short)3);
		    cellCabec4.setCellValue(new HSSFRichTextString("Entropy"));
		    cellCabec4.setCellStyle(blackStyle);
		    
		    HSSFCell cellCabec5 = row.createCell((short)4);
		    cellCabec5.setCellValue(new HSSFRichTextString("Decs"));
		    cellCabec5.setCellStyle(blackStyle);		    		   
		    
		    if(tc){
		    	HSSFCell cellCabec8 = row.createCell((short)5);
			    cellCabec8.setCellValue(new HSSFRichTextString("Tipo Composto"));
			    cellCabec8.setCellStyle(blackStyle);

			    HSSFCell cellCabec6 = row.createCell((short)6);
			    cellCabec6.setCellValue(new HSSFRichTextString("Sinônimos"));
			    cellCabec6.setCellStyle(blackStyle);
			    
			    HSSFCell cellCabec7 = row.createCell((short)7);
			    cellCabec7.setCellValue(new HSSFRichTextString("Frases"));
			    cellCabec7.setCellStyle(blackStyle);
		    }else{
			    HSSFCell cellCabec6 = row.createCell((short)5);
			    cellCabec6.setCellValue(new HSSFRichTextString("Sinônimos"));
			    cellCabec6.setCellStyle(blackStyle);
			    
			    HSSFCell cellCabec7 = row.createCell((short)6);
			    cellCabec7.setCellValue(new HSSFRichTextString("Frases"));
			    cellCabec7.setCellStyle(blackStyle);
		    }
		    
		    int i = 1;
		    int j , k = 0;
		    boolean quebra = true;
		    HSSFCellStyle styleDaVez = alternateWhiteStyle;
		    for (Resultado resultado : valores) {		    	
		    	if(resultado.isSelected()){
		    		quebra = true;
		    		/**
			    	if((i % 2) == 0)
			    		styleDaVez = alternateWhiteStyle;
			    	else
			    		styleDaVez = alternateGrayStyle;
			    		**/
			    	row = sheet.createRow((short)i);
			    	
			    	HSSFCell cell1 = row.createCell((short)0);
			    	HSSFCell cell2 = row.createCell((short)1);
			    	HSSFCell cell3 = row.createCell((short)2);
			    	HSSFCell cell4 = row.createCell((short)3);
			    	HSSFCell cell5 = row.createCell((short)4);
			    	HSSFCell cell6 = row.createCell((short)6);
			    	HSSFCell cell7 = row.createCell((short)7);
			    	HSSFCell cell8 = null;
			    	if(tc){
			    		cell8 = row.createCell((short)5);
			    		cell8.setCellValue(new HSSFRichTextString(resultado.getTipoComposto()));
			    		cell8.setCellStyle(styleDaVez);
			    	}
				    
				    cell1.setCellValue(new HSSFRichTextString(resultado.getLemma()));
				    cell2.setCellValue(resultado.getTotal());
				    cell3.setCellValue(resultado.getTfidf());
				    cell4.setCellValue(resultado.getEntropy());
				    cell5.setCellValue(resultado.isDecs());
				    
				    cell1.setCellStyle(styleDaVez);
				    cell2.setCellStyle(styleDaVez);
				    cell3.setCellStyle(styleDaVez);
		   		    cell4.setCellStyle(styleDaVez);
		   		    cell5.setCellStyle(styleDaVez);
		   		    cell6.setCellStyle(styleDaVez);
		   		    cell7.setCellStyle(styleDaVez);

				    
				    j = i;
//				    ArrayList<String> sinonimos = resultado.getSinonimos();
				    ArrayList<String> sinonimos = new ArrayList<String>();
				    int sincont = 0;
				    for (String sin : sinonimos) {
				    	sincont++;
				    	if(sincont > 10)
				    		break;
				    	quebra = false;
				    	row = sheet.getRow((short)j) == null ? sheet.createRow((short)j):sheet.getRow((short)j);
				    	if(tc)
				    		cell6 = row.createCell((short)6);
				    	else
				    		cell6 = row.createCell((short)5);
				   		cell6.setCellStyle(styleDaVez);				   
				    	cell6.setCellValue(new HSSFRichTextString(sin));
				    	for(int z=0;z<7;z++){
				    		if(row.getCell((short)z) == null)
				    			row.createCell((short)z).setCellStyle(styleDaVez);
				    		else
				    			row.getCell((short)z).setCellStyle(styleDaVez);
				    	}
				    	j++;
					}	
				    
				    k = i;
//				    ArrayList<String> frases = textos.getFrases(resultado.getLemma(),arquivosFrase);
				    ArrayList<String> frases = new ArrayList<String>();
				    for (String frase : frases) {	
				    	quebra = false;
				    	row = sheet.getRow((short)k) == null ? sheet.createRow((short)k):sheet.getRow((short)k);
				    	if(tc)
				    		cell7 = row.createCell((short)7);
				    	else
				    		cell7 = row.createCell((short)6);
				    	
				   		cell7.setCellStyle(styleDaVez);							   		
				    	cell7.setCellValue(new HSSFRichTextString(frase));
				    	for(int z=0;z<6;z++){
				    		if(row.getCell((short)z) == null)
				    			row.createCell((short)z).setCellStyle(styleDaVez);
				    		else
				    			row.getCell((short)z).setCellStyle(styleDaVez);
				    	}
				    	k++;
					}	
				    
				    //i  = Math.max(j, k);

				    /**if(quebra)
				    	i=i+2;
				    else
				    	i++;**/
				    i++;
		    	}
			}
	}
	
	public static String doExcel(ArrayList<Resultado> valoresTodos, ArrayList<Resultado> valoresTC, 
			ServletOutputStream servletOutputStream, Textos textos, ArrayList<String> arquivosFrase){
		
		HSSFWorkbook wb = new HSSFWorkbook();
		//valoresTodos.addAll(valoresTC);
		for (Resultado re : valoresTC) {
			if(!valoresTodos.contains(re)){
				System.out.println(re.getLemma() + " " + re.getTotal());
				valoresTodos.add(re);
			}
		}
//		
//		HashMap<String, ArrayList<String>> arvore = new HashMap<String, ArrayList<String>>();
//		
//		for (Resultado re : valoresTodos) {
//			if(re.getLemma().contains(" ")){
//				String p1 = re.getLemma().split(" ")[0];
//				ArrayList<String> arrayAux;
//				if(arvore.get(p1) != null)
//					arvore.get(p1).add(re.getLemma());
//				else{
//					arrayAux = new ArrayList<String>();
//					arrayAux.add(re.getLemma());
//					arvore.put(p1, arrayAux);
//				}
//			}
//		}
//		System.out.println(arvore);
		
		setSheet(valoresTodos, "Todos os Termos", wb,textos, arquivosFrase, false);
		setSheet(valoresTC, "Apenas Termos Compostos", wb,textos, arquivosFrase, true);
		String ARQUIVO = Calendar.getInstance().getTimeInMillis()+"resultados.xls";

	    try {
	        wb.write(servletOutputStream);		   
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ARQUIVO;
	}
	
}
