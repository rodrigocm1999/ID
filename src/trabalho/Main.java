/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.List;
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

        MainWindow main = new MainWindow();
        main.show();

        //juntar2ficheiros();
        //Camaras.Run();
        //Contratos.Run();
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

            for (int j = 0; j < munList.size(); j++) {
                Element contElement = munList.get(j);
                String str = contElement.getAttributeValue("nomeMun");

                if (nomeMun.equals(nomeMun)) {
                    contElement.detach();
                    munElement.addContent(contElement);
                    break;
                }
            }
        }

        Util.escreverDocumentoParaFicheiro(docTudo, "tudo.xml");
    }

}
