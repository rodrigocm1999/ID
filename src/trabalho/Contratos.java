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

/**
 *
 * @author Joao
 */
public class Contratos {

    public static String[] strings = new String[]{"objetoContrato", "preco", "publicacao", "adjudicatario"};
    
    
    public static void Run() {
        ArrayList<String> codEntidade = new ArrayList<>();
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("municipios.txt"));
            String line, numPart;
            while ((line = dis.readLine()) != null) {
                numPart = line.split(";")[1];
                codEntidade.add(numPart);
            }
        } catch (Exception ex) {
        }

        String link = "http://www.base.gov.pt/Base/pt/ResultadosPesquisa?type=entidades&query=texto%3D";

        ArrayList<ThreadContratos> threadsContratos = new ArrayList<>();
        ThreadContratos thread;

        Document doc = new Document();

        for (int i = 0; i < codEntidade.size(); i++) {
            thread = new ThreadContratos(link, codEntidade.get(i));
            thread.start();

            threadsContratos.add(thread);
        }
        for (int i = 0; i < threadsContratos.size(); i++) {
            try {
                thread = threadsContratos.get(i);
                thread.join();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        XMLfunc.escreverDocumentoParaFicheiro(doc, "camaras.xml");

    }
}
