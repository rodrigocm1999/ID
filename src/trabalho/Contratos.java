/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 *
 * @author Joao
 */
public final class Contratos {

    public static String[] strings = new String[]{"objetoContrato", "preco", "publicacao", "adjudicatario"};

    private Document contratos;
    
    public Contratos(boolean fromFiles) {
        if(fromFiles){
            contratos = Util.lerDocumentoXML("contratos.xml");
        }else{
            contratos = this.Run();
        }
    }
    public Document getDocument(){return contratos;}

    private Contratos(){ 
    }
    
    
    
    
    
    
    
    
    public static Document Run() {
        ArrayList<String> codEntidade = new ArrayList<>();
        ArrayList<String> nomeMunicipios = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("municipios.txt"), "UTF-8"));

            String line, numPart, nomeMunicipio;
            while ((line = br.readLine()) != null) {
                String[] splited = line.split(";");
                numPart = splited[1];
                nomeMunicipio = splited[0];
                nomeMunicipios.add(nomeMunicipio);
                codEntidade.add(numPart);

                System.out.println(nomeMunicipio);
            }
        } catch (Exception ex) {
        }

        String link = "http://www.base.gov.pt/Base/pt/ResultadosPesquisa?type=entidades&query=texto%3D";

        ArrayList<ThreadContratos> threadsContratos = new ArrayList<>();
        ThreadContratos thread;

        Element contratos = new Element("contratos");
        Document doc = new Document(contratos);

        for (int i = 0; i < codEntidade.size(); i++) {
            thread = new ThreadContratos(link, codEntidade.get(i), nomeMunicipios.get(i));
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

                Element municipio = new Element("municipio");
                municipio.setAttribute("nomeMun", nomeMunicipios.get(i));
                contratos.addContent(municipio);

                for (int j = 0; j < arrayThread.size(); j++) {
                    municipio.addContent(arrayThread.get(j).getElement());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            File f = new File("contratos.xsd");
            if (f.exists()) {

                Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                contratos.addNamespaceDeclaration(xsi);
                contratos.setAttribute("noNamespaceSchemaLocation", "contratos.xsd", xsi);

                if (Util.validarXSD("contratos.xml") == null) {
                    throw new Exception("Ficheiro contratos.xml não é válido");
                }
            } else {
                System.out.println("O ficheiro contratos.xml não existe");
            }
        } catch (Exception ex) {
        }

        Util.escreverDocumentoParaFicheiro(doc, "contratos.xml");
        return doc;
    }
    
    
    
    
    public Element ContratoDeMaiorValorCamara(String nomeCamara){
        
        
        
        
        
        
        
        return null;
    }
    
    
    
    
}
