package trabalho;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jdom2.Element;

public class ThreadCamaras extends Thread {
    private final String link;
    private final String num;
    private CamaraMunicipal camara;

    public ThreadCamaras(String link, String num) {
        this.link = link;
        this.num = num;// Codigo da camara
    }

    @Override
    public void run() {
        super.run();
        // Faz-se o request ao link da camara em causa
        String siteString = Util.httpRequestToString(link + num, "windows-1252");
        //Regex que verifica se a página não é errada
        Pattern pat = Pattern.compile("class=\"sel3\"");
        Matcher mat = pat.matcher(siteString);
        //Se for uma página válida
        if (mat.find()) {
            //Uso o RegEx para pegar todas as informações
            Pattern pattern = Pattern.compile(Camaras.getRegExp(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(siteString);
            if (matcher.find()) {
                //Uso um hashmap para guardar todas as informações
                HashMap<String, String> hashMap = new HashMap<>(11);
                // uso um array com os nomes do grupos do RegEx para pegar os varios valores
                for (String elemento : Camaras.strings) {
                    if (elemento.equals("brasao")) {
                        hashMap.put(elemento, "https://www.anmp.pt/" + matcher.group(elemento));
                    } else {
                        hashMap.put(elemento, matcher.group(elemento));
                    }
                }
                //Guardo um objeto camara municipal com o Elemento contruido
                camara = new CamaraMunicipal(hashMap);
                return;
            }
        }
        camara = null;
    }

    public Element getElement() {
        return camara == null ? null : camara.getElement();
    }
}
