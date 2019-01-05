package trabalho;

import java.io.File;
import java.util.List;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jdom2.Document;
import org.jdom2.Element;

public class Main {

    public static void main(String[] args) throws Exception {

        MainWindow main = new MainWindow();
        main.setVisible(true);
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

                if (nomeMun.equals(str)) {
                    contElement.detach();
                    munElement.addContent(contElement);
                    break;
                }
            }
        }
        Util.escreverDocumentoParaFicheiro(docTudo, "tudo.xml");
    }

    public static <T extends List<T>> void printList(T list) {
        for (Object item : list) {
            System.out.println(item.toString());
        }
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
