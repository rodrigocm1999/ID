/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.File;
import java.util.ArrayList;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 *
 * @author Rodrigo
 */
public class Camaras {

    public static String[] camaraStrings = new String[]{"nome", "area", "nHabitantes", "nFreguesias", "feriado", "presidente",
        "assembleia", "telefone", "email", "site", "brasao", "morada", "codPostal"};
    private static String regExp = "<div class=\"sel3\">(?<nome>.*)<\\/div><br><div class=\"f3\">.*área de (?<area>\\d*),\\d km2, (?<nHabitantes>\\d+ \\d+).* (?<nFreguesias>\\d+) freguesias.*f3>(?<distrito>.*)<\\/a>.*feriado municipal, (?<feriado>\\d{4}-\\d\\d-\\d\\d).*\"f3\">(?<presidente>.*),.*\\r\\n.*>(?<assembleia>.*)\\s?,.*\\r\\n.*sel2\">(?<morada>.*)<br> (?<codPostal>\\d+-\\d+).*<\\/.*Telefone:.(?<telefone>.*) <br>.*\\r\\n.*mailto:(?<email>.*)\" .*\\r\\n.*<div class=\"f1\" align=\"left\"><a href=\"(?<site>.*\")\\sclass=\"f2\".*\\r\\n.*SRC=\"(?<brasao>.*)\" A";

    public static void Run() {
        // Link:    https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3000
        /*Minicipios de coimbra
            3060 - 3230    Os numeros sobem de 10 em 10
            Alguns nao teem informação 
         */
        String link = "https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M";

        int num_start = 3000;
        int num_end = 3420;//3420;

        ArrayList<ThreadCamaras> threadCamaras = new ArrayList<>();

        ThreadCamaras thread;
        Element municipios = new Element("municipios");
        Document doc = new Document(municipios);

        for (int i = num_start; i <= num_end; i += 10) {
            thread = new ThreadCamaras(link, i);
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
        XMLfunc.escreverDocumentoParaFicheiro(doc, "camaras.xml");
        try {
            File f = new File("camaras.xsd");
            if (f.exists()) {
                Namespace XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchemainstance");
                municipios.addNamespaceDeclaration(XSI);
                municipios.setAttribute(new Attribute("noNamespaceSchemaLocation", "camaras.xsd",
                        Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")));
                JDOMFunc_Validar.validarXSD("camaras.xml");
            } else {
                System.out.println("O ficheiro camaras.xsd não existe");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getRegExp() {
        return regExp;
    }
}
