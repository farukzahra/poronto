package br.fmz.poronto.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.encryption.AccessPermission;
import org.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.pdfbox.util.PDFText2HTML;
import org.pdfbox.util.PDFTextStripper;

public class ExtractText {
    public static final String DEFAULT_ENCODING = null;

    public static final String TREE_TAGGER = "C:\\TreeTagger\\bin\\tree-tagger.exe";

    public static final String PT_PAR = "C:\\TreeTagger\\bin\\pt.par";

    private static ArrayList<String> stopWords = Util.getStopWordsList();

    public ExtractText() {
    }

    private static int treeTagger(String txtFile) throws Exception {
        String txtFileOut = txtFile.contains(".txt") ? txtFile.replace(".txt", "_OUT.txt") : txtFile + "_OUT.txt";
        // String cmd =
        // +
        // + + " \"" + txtFile + "\" \"" + txtFileOut + "\"";
        String cmd[] = new String[] { Configuracao.getInstance().getPropriedade().getProperty("TREE_TAGGER"), "-token", "-lemma", Configuracao.getInstance().getPropriedade().getProperty(
                "PT_PAR"), txtFile, txtFileOut };
        for (String string : cmd) {
            System.out.print(string + " ");
        }
        // System.out.println(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        return p.waitFor();
    }

    public static void main(String[] args) {
        try {
            tokenizer("D:\\tmp\\texto.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tokenizerFrase(String txtFile, String fileFrase) throws Exception {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile)));
        StringBuilder texto = new StringBuilder();
        StringBuilder texto2 = new StringBuilder();
        String texto3 = "";
        while (buReader.ready())
            texto.append(buReader.readLine() + " ");
        texto3 = texto.toString();
        StringTokenizer toks = new StringTokenizer(texto3);
        String frase = "";
        while (toks.hasMoreElements()) {
            String tok = toks.nextToken().trim();
            if (tok.matches("\\D{5,}?\\.")) {
                frase += " " + tok;
                texto2.append(frase + "\n");
                frase = "";
            } else
                frase += " " + tok;
        }
        buReader.close();
        FileOutputStream fileOutputStream = new FileOutputStream(txtFile + "_FRASE_" + fileFrase + ".txt");
        fileOutputStream.write(texto2.toString().getBytes());
        fileOutputStream.close();
    }

    private static void tokenizer(String txtFile) throws Exception {
        // try {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile)));
        StringBuilder texto = new StringBuilder();
        StringBuilder texto2 = new StringBuilder();
        String texto3 = "";
        while (buReader.ready())
            texto.append(buReader.readLine() + "\n");
        texto3 = texto.toString();
        texto3 = texto3.replaceAll(",", " ");
        texto3 = texto3.replaceAll("\\.", " ");
        texto3 = texto3.replaceAll(";", " ");
        texto3 = texto3.replaceAll("\\–", " ");
        texto3 = texto3.replaceAll("\"", "");
        texto3 = texto3.replaceAll("'", "");
        texto3 = texto3.replaceAll("”", "");
        texto3 = texto3.replaceAll("“", "");
        texto3 = texto3.replaceAll("[0-9]", "");
        texto3 = texto3.replaceAll("\\(", "");
        texto3 = texto3.replaceAll("\\)", "");
        texto3 = texto3.replaceAll(":", "");
        texto3 = texto3.replaceAll("-", "");
        texto3 = texto3.replaceAll("•", "");
        texto3 = texto3.replaceAll("%", "");
        texto3 = texto3.replaceAll("/", "");
        texto3 = texto3.replaceAll("\\[", "");
        texto3 = texto3.replaceAll("\\]", "");
        texto3 = texto3.replaceAll("", "");
        texto3 = texto3.replaceAll("\\*", "");
        texto3 = texto3.replaceAll("!", "");
        texto3 = texto3.replaceAll("&", "");
        texto3 = texto3.replaceAll("_", "");
        texto3 = texto3.replaceAll("<", "");
        texto3 = texto3.replaceAll(">", "");
        texto3 = texto3.replaceAll("\n", " ");
        StringTokenizer toks = new StringTokenizer(texto3, " ");
        while (toks.hasMoreElements()) {
            String tok = toks.nextToken().trim();
            if (tok.length() >= 2 && !stopWords.contains(tok.toLowerCase())) {
                texto2.append(tok + "\n");
            }
        }
        buReader.close();
        FileOutputStream fileOutputStream = new FileOutputStream(txtFile);
        fileOutputStream.write(texto2.toString().getBytes());
        fileOutputStream.close();
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

    public static String pdf2txt(String pdfFile) throws Exception {
        boolean toConsole = false;
        boolean toHTML = false;
        boolean sort = false;
        String password = "";
        String encoding = DEFAULT_ENCODING;
        String textFile = null;
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;
        Writer output = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFile);
            if (textFile == null && pdfFile.length() > 4) {
                textFile = pdfFile.substring(0, pdfFile.length() - 7)
                // .replaceAll(" ", "_")
                + ".txt";
            }
            // document.print();
            if (document.isEncrypted()) {
                StandardDecryptionMaterial sdm = new StandardDecryptionMaterial(password);
                document.openProtection(sdm);
                AccessPermission ap = document.getCurrentAccessPermission();
                if (ap == null || !ap.canExtractContent()) {
                    System.out.println("You do not have permission to extract text: " + pdfFile);
                    throw new IOException("You do not have permission to extract text");
                }
            }
            if (toConsole) {
                output = new OutputStreamWriter(System.out);
            } else {
                if (encoding != null) {
                    output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
                } else {
                    // use default encoding
                    output = new OutputStreamWriter(new FileOutputStream(textFile));
                }
            }
            PDFTextStripper stripper = null;
            if (toHTML) {
                stripper = new PDFText2HTML();
            } else {
                stripper = new PDFTextStripper();
            }
            stripper.setSortByPosition(sort);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            stripper.writeText(document, output);
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                document.close();
            }
        }
        return textFile;
    }

    public static int doFile(String fileName, File file) throws Exception {
        String txtFile = "";
        if (fileName.contains(".pdf"))
            txtFile = ExtractText.pdf2txt(file.getAbsolutePath());
        else {
            // String aux = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7).replaceAll(" ", "_") + ".txt";
            // System.out.println(aux);
            // f.renameTo(new File(aux));
            // txtFile = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7).replaceAll(" ", "_") + ".txt";
            // file.
            txtFile = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7) + ".txt";
            file.renameTo(new File(txtFile));
        }
        // ExtractText.tokenizerFrase(txtFile,fileFrase);
        ExtractText.tokenizer(txtFile);
        return ExtractText.treeTagger(txtFile);
    }
}
