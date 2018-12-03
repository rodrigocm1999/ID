/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author Joao
 */
public class Contratos {

    public static String[] strings = new String[]{"objetoContrato", "preco", "publicacao", "adjudicatario"};

    public static void Run() {
        ArrayList<String> codEntidade = new ArrayList<>();
        ArrayList<String> nomeMunicipios = new ArrayList<>();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("municipios.txt"));
            String line, numPart,nomeMunicipio;
            while ((line = dis.readLine()) != null) {
                String[] splited = line.split(";");
                numPart = splited[1];
                nomeMunicipio = splited[0];
                nomeMunicipios.add(nomeMunicipio);
                codEntidade.add(numPart);
            }
        } catch (Exception ex) {
        }

        String link = "http://www.base.gov.pt/Base/pt/ResultadosPesquisa?type=entidades&query=texto%3D";

        ArrayList<ThreadContratos> threadsContratos = new ArrayList<>();
        ThreadContratos thread;

        Element contratos = new Element("contratos");
        Document doc = new Document(contratos);

        for (int i = 0; i < codEntidade.size(); i++) {
            thread = new ThreadContratos(link, codEntidade.get(i),nomeMunicipios.get(i));
            thread.start();
            /*
            try {
                thread.join();

                ArrayList<Contrato> arrayThread = thread.getContratos();

                for (int j = 0; j < arrayThread.size(); j++) {
                    contratos.addContent(arrayThread.get(j).getElement());
                }
                XMLfunc.escreverDocumentoParaFicheiro(doc, "contratos.xml");
                //Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println("erro");
                ex.printStackTrace();
            }*/

            threadsContratos.add(thread);
        }
        for (int i = 0; i < threadsContratos.size(); i++) {
            try {
                thread = threadsContratos.get(i);
                thread.join();

                ArrayList<Contrato> arrayThread = thread.getContratos();

                Element municipio = new Element(nomeMunicipios.get(i));
                contratos.addContent(municipio);
                
                for (int j = 0; j < arrayThread.size(); j++) {
                    municipio.addContent(arrayThread.get(j).getElement());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        XMLfunc.escreverDocumentoParaFicheiro(doc, "contratos.xml");

    }
}
