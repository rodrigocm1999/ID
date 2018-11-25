/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class Main {

    public static String[] camaraStrings = new String[]{"nome", "area", "nHabitantes","nFreguesias", "feriado", "presidente"
            , "assembleia", "telefone", "email", "site", "brasao","morada","codPostal"};
    private static String regExp = "<div class=\"sel3\">(?<nome>.*)<\\/div><br><div class=\"f3\">.*área de (?<area>\\d*),\\d km2, (?<nHabitantes>\\d+ \\d+).* (?<nFreguesias>\\d+) freguesias.*f3>(?<distrito>.*)<\\/a>.*feriado municipal, (?<feriado>\\d{4}-\\d\\d-\\d\\d).*\"f3\">(?<presidente>.*),.*\\r\\n.*>(?<assembleia>.*)\\s?,.*\\r\\n.*sel2\">(?<morada>.*)<br> (?<codPostal>\\d+-\\d+).*<\\/.*Telefone:.(?<telefone>.*) <br>.*\\r\\n.*mailto:(?<email>.*)\" .*\\r\\n.*<div class=\"f1\" align=\"left\"><a href=\"(?<site>.*\")\\sclass=\"f2\".*\\r\\n.*SRC=\"(?<brasao>.*)\" A";

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        Camaras();
    }
    
    
    public static void Camaras(){
        // Link:    https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3000
        /*Minicipios de coimbra
            3060 - 3230    Os numeros sobem de 10 em 10
            Alguns nao teem informação 
         */
        String link = "https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M";

        int num_start = 3000;
        int num_end = 3420;//3420;

        ArrayList<ThreadCamaras> threadCamaras = new ArrayList<>();
        ArrayList<CamaraMunicipalOld> camaras = new ArrayList<>();

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
    }

    public static String getRegExp() {
        return regExp;
    }

   
}
