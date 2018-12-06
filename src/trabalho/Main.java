/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.File;
import java.util.List;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        //MainWindow main = new MainWindow();
        //main.show();

        System.out.println(Util.lerFicheiroTexto("municipios.txt"));
        
        
        juntar2ficheiros();
        //paginaHTMLBrasoes();
        //Camaras.Run();
        Contratos.Run();
    }

    public static void juntar2ficheiros() {

        Document docTudo = Util.lerDocumentoXML("camaras.xml");
        Document docContratos = Util.lerDocumentoXML("contratos.xml");

        Element mun = docTudo.getRootElement();
        List<Element> municipioslist = mun.getChildren();

        Element cont = docContratos.getRootElement();
        List<Element> munList = cont.getChildren();

        for (int i = 0; i < municipioslist.size(); i++) {
            Element munElement = municipioslist.get(i);
            String nomeMun = munElement.getChildText("nome");
            System.out.println("Nome Mun: " + nomeMun);
            for (int j = 0; j < munList.size(); j++) {
                Element contElement = munList.get(j);
                String str = contElement.getAttributeValue("nomeMun");
                System.out.println("nome municipio contratos: " + str);
                if (nomeMun.equals(str)) {
                    contElement.detach();
                    munElement.addContent(contElement);
                    break;
                }
            }
            System.out.println("Fin");
        }

        Util.escreverDocumentoParaFicheiro(docTudo, "tudo.xml");
    }

    public static void paginaHTMLBrasoes() {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("brasoes.xslt"));
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(new File("camaras.xml"));
            transformer.transform(text, new StreamResult(new File("brasoes.html")));

        } catch (TransformerException ex) {
            System.out.println("Erro de Transformação");
        }
    }

}
