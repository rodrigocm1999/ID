package trabalho;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


public class Camaras {


    //Array que tem o nome dos grupos usados na regularExpression
    public static String[] strings = new String[]{"nome", "area", "nHabitantes", "nFreguesias", "feriado", "presidente",
        "assembleia", "telefone", "email", "site", "brasao", "morada", "codPostal"};

    //Regular expression que pega todos os valores a partir da pagina da camara
    private static String regExp
            = "<div class=\"sel3\">(?<nome>.*)<\\/div><br><div class=\"f3\">.*área de (?<area>\\d*),\\d km2, (?<nHabitantes>\\d+ \\d+).* "
            + "(?<nFreguesias>\\d+) freguesias.*f3>(?<distrito>.*)<\\/a>.*feriado municipal, (?<feriado>\\d{4}-\\d\\d-\\d\\d).*\"f3\">(?<presidente>.*),.*\\r\\n.*>"
            + "(?<assembleia>.*)\\s?,.*\\r\\n.*sel2\">(?<morada>.*)<br> (?<codPostal>\\d+-\\d+).*<\\/.*Telefone:.(?<telefone>.*) <br>.*\\r\\n.*mailto:(?<email>.*)\" "
            + ".*\\r\\n.*<div class=\"f1\" align=\"left\"><a href=\"(?<site>.*\")\\sclass=\"f2\".*\\r\\n.*SRC=\"(?<brasao>.*)\" A";
    private static final String camarasPath = "camaras";
    public static final String ficheiroAlterado = camarasPath + ".xml";

    private Camaras() {
    }

    public static Document Run() {

        String getAllMunString = Util.httpRequestToString("https://www.anmp.pt/anmp/pro/mun1/mun101w2.php?dis=06", "windows-1252");

        Pattern pattern = Pattern.compile("href=\"mun101w3\\.php\\?cod=M(3\\d+)\" c", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(getAllMunString);

        ArrayList<String> numsMun = new ArrayList<>();

        while (matcher.find()) {
            numsMun.add(matcher.group(1));
        }

        ArrayList<ThreadCamaras> threadCamaras = new ArrayList<>();

        ThreadCamaras thread;
        //elemento agregador de todos os municipios
        Element municipios = new Element("municipios");
        //Objecto Document que tem todos os objetos Element ligados
        Document doc = new Document(municipios);

        //loop para iniciar threads para fazer requests ao servidos
        for (int i = 0; i < numsMun.size(); i++) {

            //crio o elemento da thread e envio o link e o codigo do municipio
            thread = new ThreadCamaras("https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M", numsMun.get(i));
            // inicio a thread
            thread.start();

            //adiciono a thread a um arrayList para depois espera que a thread acabe
            // e ir buscar o resultado das threads
            threadCamaras.add(thread);
        }
        for (int i = 0; i < threadCamaras.size(); i++) {
            try {
                thread = threadCamaras.get(i);
                //esperar que a thread acabe de executar
                thread.join();
                //pegar o elemento que criou
                Element element = thread.getElement();
                if (element != null) {
                    //adicionar elemento ao elemento raiz do documento
                    municipios.addContent(element);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Validação
        try {
            File f = new File("camaras.xsd");
            if (f.exists()) {

                Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                //adicionar Namespace
                municipios.addNamespaceDeclaration(xsi);
                municipios.setAttribute("noNamespaceSchemaLocation", camarasPath + ".xsd", xsi);

                if (Util.validarXSD(camarasPath + ".xml") == null) {
                    //Se não for válido
                    throw new Exception("Ficheiro camaras.xml não é válido");
                }
            } else {
                System.out.println("O ficheiro camaras.xsd não existe");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        //Guardar ficheiro
        Util.escreverDocumentoParaFicheiro(doc, getFilePath(true));
        return doc;
    }

    //Getters
    public static String getRegExp() {
        return regExp.substring(0);
    }

    public static String getFilePath(boolean original) {
        if (original) {
            return camarasPath + "_original.xml";
        }

        File editedFile = new File(camarasPath + ".xml");
        if (editedFile.exists()) {
            return editedFile.getPath();
        }else{
            return getFilePath(true);
        }
    }

    public static String getRawFileString(boolean original) {
        return Util.lerFicheiroTexto(getFilePath(original));
    }

    public static Document getDocument(boolean original) {
        return Util.lerDocumentoXML(getFilePath(original));
    }
}
