/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abs
 */
public class Requests {

    public static void httpRequest(String link, String pesquisa, String outFile) {
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
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

}
