package trabalho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThreadContratos extends Thread {

    private String link;
    private String num;
    private String nomeMunicipio;
    private ArrayList<Contrato> contratos;

    public ThreadContratos(String link, String num,String nomeMunicipio) {

        this.link = link;
        this.num = num;
        this.nomeMunicipio = nomeMunicipio;
        contratos = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        String siteString = null;
        while (siteString == null) {
            siteString = Util.httpRequestToString(link + num, "UTF-8");
        }

        //pego o codigo da camara no site
        Pattern pat = Pattern.compile("class=\"plusSign\".*href=\".*=(\\d+)\" t");
        Matcher mat = pat.matcher(siteString);

        if (mat.find()) {
            //link pronto a colocar o range dos contratos
            String[] superLink = {"http://www.base.gov.pt/Base/pt/ResultadosPesquisa?range=", 
                "&type=contratos&query=adjudicanteid%3D" + mat.group(1) + "&ordering=sort%28-publicationDate%29"};
            
            ThreadGetContratos thread;
            ArrayList<ThreadGetContratos> threads = new ArrayList<>();
            //neste loop pego o range com 1200 contratos e fasso 2 requests para pegar todos os contratos
            for (int last = 0, i = 1200, counter = 1; true; last = ++i, i += 1199, counter++) {
                try {
                    //crio uma thread para fazer o request e nao parar a execução desta
                    thread = new ThreadGetContratos(superLink[0] + last + '-' + i + superLink[1]);
                    thread.start();
                    threads.add(thread);

                    //apos 2 espero pela resposta e se ja nao houverem mais paro
                    if ((counter % 2) == 0) {
                        thread.join();
                        if (thread.NothingMore()) {
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // neste loop juntamos os resultados de todas as threads
            for (int i = 0; i < threads.size(); i++) {
                try {
                    thread = threads.get(i);
                    thread.join();
                    ArrayList<Contrato> contratosThread = thread.getContratos();

                    contratos.addAll(contratosThread);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Contrato> getContratos() {
        return contratos;
    }

    public class ThreadGetContratos extends Thread {

        private String link;
        private ArrayList<Contrato> contratos;
        private boolean found = false;

        public ThreadGetContratos(String link) {
            this.link = link;
            contratos = new ArrayList<>();
        }

        public void run() {
            super.run();
            String siteString = null;
            //esta verificação ja nao é necessária pois o timeout do request foi retirado
            while (siteString == null) {
                //Fasso o request
                siteString = Util.httpRequestToString(link, "UTF-8");
            }
            //Uso o RegEx para pegar todas as informações
            Pattern pattern = Pattern.compile("<tr>\\r\\n.*<td title=\"(?<objetoContrato>.*)\">.*<\\/td>"
                    + "\\r\\n.*<td.*>(?<preco>.*) €<\\/td>\\r\\n.*<td.*>(?<publicacao>\\d{2}-\\d{2}-\\d{4})"
                    + "<\\/td>\\r\\n.*\\r\\n.*<td>(?<adjudicatario>.*)<\\/td>", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(siteString);
                
            while (matcher.find()) {
                found = true;
                //Uso um hashmap para guardar todas as informações
                HashMap<String, String> hashMap = new HashMap<>(25);
                // uso um array com os nomes do grupos do RegEx para pegar os varios valores
                for (String elemento : Contratos.strings) {
                    hashMap.put(elemento, matcher.group(elemento));
                }
                //Guardo um objeto do tipo Contrato com o Elemento contruido
                Contrato contrato = new Contrato(hashMap);
                this.contratos.add(contrato);
            }
        }

        public ArrayList<Contrato> getContratos() {
            return this.contratos;
        }

        public boolean NothingMore() {
            return !found;
        }
    }

}
