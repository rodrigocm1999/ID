package trabalho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import static trabalho.Camaras.getFilePath;


public final class Contratos {

    //Array que tem o nome dos grupos usados na regularExpression
    public static final String[] strings = new String[]{"objetoContrato", "preco", "publicacao", "adjudicatario"};
    private static final String contratosPath = "contratos";
    public static final String ficheiroAlterado = contratosPath + ".xml";

    private Contratos() {
    }


    public static Document Run() {
        ArrayList<String> codEntidade = new ArrayList<>();
        ArrayList<String> nomeMunicipios = new ArrayList<>();
        try {
            //abro o ficheiro com o nome dos municipios e e com os códigos dos mesmos
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("municipios.txt"), "UTF-8"));

            String line, numPart, nomeMunicipio;
            while ((line = br.readLine()) != null) {
                //para cada linha divido o codigo e o nome
                String[] splited = line.split(";");
                numPart = splited[1];
                nomeMunicipio = splited[0];
                //e guardo nos devidos arrays
                nomeMunicipios.add(nomeMunicipio);
                codEntidade.add(numPart);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        String link = "http://www.base.gov.pt/Base/pt/ResultadosPesquisa?type=entidades&query=texto%3D";

        ArrayList<ThreadContratos> threadsContratos = new ArrayList<>();
        ThreadContratos thread;

        //Elemento agregador de todos os contratos do Documento
        Element contratos = new Element("contratos");
        Document doc = new Document(contratos);

        //iniciar todas as threads
        for (int i = 0; i < codEntidade.size(); i++) {
            //crio o elemento da thread e envio o link e o codigo do municipio
            thread = new ThreadContratos(link, codEntidade.get(i), nomeMunicipios.get(i));
            // inicio a thread
            thread.start();

            //adiciono a thread a um arrayList para depois espera que a thread acabe
            // e ir buscar o resultado das threads
            threadsContratos.add(thread);
        }
        //para cada thread pegar os resultados
        for (int i = 0; i < threadsContratos.size(); i++) {
            try {
                thread = threadsContratos.get(i);
                //esperar que a thread acabe de executar
                thread.join();

                ArrayList<Contrato> arrayThread = thread.getContratos();

                // colocar todos os contratos de cada municipio dentro de um elemento para esse municipio
                Element municipio = new Element("municipio");
                //colocar o nome do municipio
                municipio.setAttribute("nomeMun", nomeMunicipios.get(i));
                //adicionar elemento ao elemento raiz do documento
                contratos.addContent(municipio);

                for (int j = 0; j < arrayThread.size(); j++) {
                    //adicionar elemento do contrato ao elemento desse municipio
                    municipio.addContent(arrayThread.get(j).getElement());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Validação
        try {
            File f = new File(contratosPath + ".xsd");
            if (f.exists()) {

                Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                //adicionar Namespace
                contratos.addNamespaceDeclaration(xsi);
                contratos.setAttribute("noNamespaceSchemaLocation", contratosPath + ".xsd", xsi);

                if (Util.validarXSD(contratosPath + ".xml") == null) {
                    //Se não for válido
                    throw new Exception("Ficheiro " + contratosPath + ".xml não é válido");
                }
            } else {
                System.out.println("O ficheiro " + contratosPath + ".xsd não existe");
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
    public static String getFilePath(boolean original) {
        if (original) {
            return contratosPath + "_original.xml";
        }

        File editedFile = new File(contratosPath + ".xml");
        if (editedFile.exists()) {
            return editedFile.getPath();
        } else {
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
