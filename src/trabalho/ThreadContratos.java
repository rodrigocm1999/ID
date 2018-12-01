/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rodrigo
 */
public class ThreadContratos extends Thread {

    private String link;
    private String num;
    private ArrayList<Contrato> contratos;

    public ThreadContratos(String link, String num) {

        this.link = link;
        this.num = num;
        contratos = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        System.out.println("thread qualquer cena " + num);
        String siteString = Requests.httpRequestToString(link + num);

        Pattern pat = Pattern.compile("class=\"plusSign\".*href=\".*(\\d{4})\" t");
        Matcher mat = pat.matcher(siteString);

        if (mat.find()) {
            //String otherSiteString = Requests.httpRequestToString(mat.group(1));
            /*Pattern pattern = Pattern.compile("<a href=\"(.*)\">Lista de contratos onde esta entidade foi adjudicante");
            Matcher matcher = pattern.matcher(otherSiteString);*/
            String[] superLink = {"http://www.base.gov.pt/Base/pt/ResultadosPesquisa?range=", "&type=contratos&query=adjudicanteid%3D" + mat.group(1) + "&ordering=sort%28-publicationDate%29"};

            //if (matcher.find()) {
            ThreadGetContratos thread;
            ArrayList<ThreadGetContratos> threads = new ArrayList<>();
            for (int last = 1, i = 24, counter = 1; i < 100000; last = ++i, i += 23, counter++) {
                // este loop serve para entrar na lista dos contratos 
                //a partir do numero que recolhi da pagina que tem o Mais depois da procura
                thread = new ThreadGetContratos(superLink[0] + last + '-' + i + superLink[1]);
                thread.start();

                threads.add(thread);

                if ((counter % 10) == 0) {

                }
            }

            for (int i = 0; i < threads.size(); i++) {
                try {
                    thread = threads.get(i);
                    thread.join();
                    ArrayList<Contrato> contratosThread = thread.getContratos();
                    
                    contratos.addAll(contratosThread);
                } catch (Exception ex) {
                }
            }
            //}
        }

    }

    public class ThreadGetContratos extends Thread {

        private String link;
        private ArrayList<Contrato> contratos;

        public ThreadGetContratos(String link) {
            this.link = link;
            contratos = new ArrayList<>();
        }

        public void run() {
            super.run();
            String siteString = Requests.httpRequestToString(link);

            Pattern pattern = Pattern.compile("<tr>\\r\\n.*<td title=\"(?<objetoContrato>.*)\">.*<\\/td>\\r\\n.*<td.*>(?<preco>.*) €<\\/td>\\r\\n.*<td.*>(?<publicacao>\\d{2}-\\d{2}-\\d{4})<\\/td>\\r\\n.*\\r\\n.*<td>(?<adjudicatario>.*)<\\/td>", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(siteString);

            while (matcher.find()) {

                HashMap<String, String> hashMap = new HashMap<>(25);

                for (String elemento : Camaras.strings) {

                    hashMap.put(elemento, matcher.group(elemento));
                }

                Contrato contrato = new Contrato(hashMap);
                contratos.add(contrato);
            }
        }

        public ArrayList<Contrato> getContratos() {
            return contratos;
        }
    }

}
