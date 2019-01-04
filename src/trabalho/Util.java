package trabalho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Util {

    public static String lerFicheiroTexto(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null)
                stringBuilder.append(line).append("\n");
            return stringBuilder.toString();
        } catch (IOException ex) {
        }
        return null;
    }

    public static void escreverFicheiroTexto(String str, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(str);
            bw.close();
        } catch (IOException ex) {
        }
    }

    public static String httpRequestToString(String link, String encoding) {
        URL url;
        try {
            url = new URL(link);
            URLConnection ligacao = url.openConnection();
            ligacao.setConnectTimeout(0);
            ligacao.setReadTimeout(0);
            ligacao.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16");
            
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
        } catch (IOException ex) {
            System.out.println("Erro");
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
        } catch (IOException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " nao foi encontrado");
        }
        return null;
    }

    //Executa validação do documento XML usando XSD
    public static Document validarXSD(String caminhoFicheiro) {
        try {
            SAXBuilder builder = new SAXBuilder(true); // true ativa a validação
            builder.setFeature("http://apache.org/xml/features/validation/schema", true);

            Document doc = builder.build(new File(caminhoFicheiro));
            return doc;
        } catch (JDOMException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " apresenta erros e não é válido (XSD)");
        } catch (IOException ex) {
            System.out.println("Documento XML " + caminhoFicheiro + " nao foi encontrado");
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
            System.out.println("JDOM Exception Ficheiro contem erros : " + caminhoFicheiro);
        } catch (IOException ex) {
            System.out.println("Ficheiro XML nao existe");
        }
        return null;
    }

    public static void escreverDocumentoParaFicheiro(Document doc, String caminhoFicheiro) {
        OutputStreamWriter writer = null;
        try {
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
        Format outputFormat = Format.getPrettyFormat();
        outputFormat.setIndent("    ");
        XMLOutputter outputter = new XMLOutputter(outputFormat);
        return outputter.outputString(doc);
    }

    public static boolean TryParse(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
