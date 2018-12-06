/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Rodrigo
 */
public class Util {

    public static String lerFicheiroTexto(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException ex) {
        }
        return null;
    }

    /*public static void httpRequest(String link, String pesquisa, String outFile) {
        URL url;
        try {
            if (!pesquisa.isEmpty()) {
                //System.out.println(link + URLEncoder.encode(pesquisa,"UTF-8"));
                url = new URL(link + URLEncoder.encode(pesquisa, "UTF-8").replace('+', '_')); //alterar replace 
            } else {
                //Criar URL simples
                // System.out.println(link);
                url = new URL(link);
            }
            //System.out.println(url);
            URLConnection ligacao = url.openConnection();

            //Ver User-Agent actual de um determinado browser : http://whatsmyuseragent.com
            ligacao.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");

            BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String linha;

            while ((linha = in.readLine()) != null) {
                sb.append(linha)
                        .append(System.getProperty("line.separator"));
            }
            //Escrever num ficheiro
            BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            out.write(sb.toString());

            out.close();
            in.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    public static String httpRequestToString(String link, String encoding) {
        URL url;
        try {
            url = new URL(link);

            //URLConnection
            URLConnection ligacao = url.openConnection();
            ligacao.setConnectTimeout(0);
            ligacao.setReadTimeout(0);
            //System.out.println("connectTimeout : "+ligacao.getConnectTimeout() + "\treadTimeout : "+ligacao.getReadTimeout());
            ligacao.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");

            /*
            //HttpURLConnection 
            HttpURLConnection ligacao = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(false);
            ligacao.setConnectTimeout(1000 * 1000);
            ligacao.setReadTimeout(1000 * 1000);
            ligacao.setRequestMethod("GET");
            ligacao.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            ligacao.connect();
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(ligacao.getInputStream(), encoding));
            StringBuilder sb = new StringBuilder();
            String linha;

            while ((linha = in.readLine()) != null) {
                sb.append(linha).append(System.getProperty("line.separator"));
            }
            in.close();
            return sb.toString();

        } catch (SocketTimeoutException ex) {
            System.out.println(LocalDate.now() + "Connection Timeout");
            //ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Connection Error");
            ex.printStackTrace();
        }
        return null;
    }

    //Executa validação do documento XML usando  DTD 
    public static Document validarDTD(String caminhoFicheiro) throws IOException {
        try {
            SAXBuilder builder = new SAXBuilder(true);  // true ativa a validação
            Document doc = builder.build(new File(caminhoFicheiro));
            //System.out.println("Documento XML " + caminhoFicheiro + " é válido (DTD)");
            return doc;
        } catch (JDOMException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " apresenta erros e não é válido (DTD)");
            //Logger.getLogger(JDOMFunctions_Validar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " nao foi encontrado");
            //Logger.getLogger(JDOMFunctions_Validar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Executa validação do documento XML usando XSD
    public static Document validarXSD(String caminhoFicheiro) {
        try {
            SAXBuilder builder = new SAXBuilder(true); // true ativa a validação

            // esta linha ativa a validação com XSD
            builder.setFeature("http://apache.org/xml/features/validation/schema", true);

            Document doc = builder.build(new File(caminhoFicheiro));
            //System.out.println("Documento XML " + caminhoFicheiro + " é válido (XSD)");
            return doc;
        } catch (JDOMException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " apresenta erros e não é válido (XSD)");
            //Logger.getLogger(JDOMFunctions_Validar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " nao foi encontrado");
            //Logger.getLogger(JDOMFunctions_Validar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*Le um ficheiro XML do disco*/
    public static Document lerDocumentoXML(String caminhoFicheiro) {
        try {

            File file = new File(caminhoFicheiro);
            InputStreamReader stream = new InputStreamReader(new FileInputStream(file), "UTF-8");
            Reader reader = new BufferedReader(stream);

            SAXBuilder builder = new SAXBuilder();
            Document anotherDocument = builder.build(reader);
            return anotherDocument;

        } catch (JDOMException ex) {
            System.out.println("Ficheiro XML nao existe");
        } catch (IOException ex) {
            System.out.println("Ficheiro XML nao existe");
        }
        return null;
    }

    public static void escreverDocumentoParaFicheiro(Document doc, String caminhoFicheiro) {
        OutputStreamWriter writer = null;
        try {
            //Define o formato de saida
            //O caracter de indentacao pode ser diferente (e.g. \t)
            Format outputFormat = Format.getPrettyFormat();
            outputFormat.setIndent("\t");
            outputFormat.setEncoding("utf-8"); // 
            //Prepara o XMLOutputter
            XMLOutputter outputter = new XMLOutputter(outputFormat);
            writer = new OutputStreamWriter(new FileOutputStream(caminhoFicheiro), "UTF-8");
            outputter.output(doc, writer);
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /*Coloca o conteudo de um documento numa String*/
    public static String escreverDocumentoString(Document doc) {
        //Define o formato de saida

        Format outputFormat = Format.getPrettyFormat();
        outputFormat.setIndent("     ");

        //Escreve o XML para o ecra, ou seja, System.out
        XMLOutputter outputter = new XMLOutputter(outputFormat);
        String txt = outputter.outputString(doc);
        return txt;

    }
}
