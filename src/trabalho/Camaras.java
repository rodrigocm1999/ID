/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 *
 * @author Rodrigo
 */
public class Camaras {

    public static String[] strings = new String[]{"nome", "area", "nHabitantes", "nFreguesias", "feriado", "presidente",
        "assembleia", "telefone", "email", "site", "brasao", "morada", "codPostal"};
    private static String regExp = "<div class=\"sel3\">(?<nome>.*)<\\/div><br><div class=\"f3\">.*área de (?<area>\\d*),\\d km2, (?<nHabitantes>\\d+ \\d+).* (?<nFreguesias>\\d+) freguesias.*f3>(?<distrito>.*)<\\/a>.*feriado municipal, (?<feriado>\\d{4}-\\d\\d-\\d\\d).*\"f3\">(?<presidente>.*),.*\\r\\n.*>(?<assembleia>.*)\\s?,.*\\r\\n.*sel2\">(?<morada>.*)<br> (?<codPostal>\\d+-\\d+).*<\\/.*Telefone:.(?<telefone>.*) <br>.*\\r\\n.*mailto:(?<email>.*)\" .*\\r\\n.*<div class=\"f1\" align=\"left\"><a href=\"(?<site>.*\")\\sclass=\"f2\".*\\r\\n.*SRC=\"(?<brasao>.*)\" A";
    private static final String camarasPath = "camaras";
    
    public static String getRegExp() {
        return regExp.substring(0);
    }
    public static String getPath(){
        return "camaras.xml";
    }

    private Document camaras;

    public Document getDocument() {
        return camaras;
    }

     public String getRawFileString() {
        return Util.lerFicheiroTexto(camarasPath + ".xml");
    }

    private Camaras() {
    }

    public Camaras(boolean fromFiles) {
        if (fromFiles) {
            camaras = Util.lerDocumentoXML(camarasPath+".xml");
        } else {
            camaras = this.Run();
        }
    }

    public static Document Run() {
        // Link:    https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3000
        /*Minicipios de coimbra
            3060 - 3230    Os numeros sobem de 10 em 10
            Alguns nao teem informação 
         */
        String link = "https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M";

        /*int num_start = 3000;
        int num_end = 3420;//3420;*/
        String getAllCods = "href=\"mun101w3\\.php\\?cod=M(3\\d+)\" c";
        String getAllMunString = Util.httpRequestToString("https://www.anmp.pt/anmp/pro/mun1/mun101w2.php?dis=06", "windows-1252");

        Pattern pattern = Pattern.compile(getAllCods, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(getAllMunString);

        ArrayList<String> numsMun = new ArrayList<>();

        while (matcher.find()) {
            numsMun.add(matcher.group(1));
        }

        ArrayList<ThreadCamaras> threadCamaras = new ArrayList<>();

        ThreadCamaras thread;
        Element municipios = new Element("municipios");
        Document doc = new Document(municipios);

        for (int i = 0; i < numsMun.size(); i++) {
            thread = new ThreadCamaras(link, numsMun.get(i));
            thread.start();

            threadCamaras.add(thread);
        }
        for (int i = 0; i < threadCamaras.size(); i++) {
            try {
                thread = threadCamaras.get(i);
                thread.join();
                Element element = thread.getElement();
                if (element != null) {
                    municipios.addContent(element);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            File f = new File("camaras.xsd");
            if (f.exists()) {

                Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                municipios.addNamespaceDeclaration(xsi);
                municipios.setAttribute("noNamespaceSchemaLocation", camarasPath+".xsd", xsi);

                if (Util.validarXSD(camarasPath+".xml") == null) {
                    throw new Exception("Ficheiro camaras.xml não é válido");
                }
            } else {
                System.out.println("O ficheiro camaras.xsd não existe");
            }
        } catch (Exception ex) {
        }

        Util.escreverDocumentoParaFicheiro(doc, camarasPath+".xml");
        return doc;
    }

    public void saveFile(){
        Util.escreverDocumentoParaFicheiro(camaras, camarasPath);
    }
}
