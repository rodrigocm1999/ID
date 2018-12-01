/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class ThreadCamaras extends Thread {

    private final String link;
    private final String num;
    private CamaraMunicipal camara;

    public ThreadCamaras(String link, String num) {
        this.link = link;
        this.num = num;
    }

    @Override
    public void run() {
        super.run();

        String siteString = Requests.httpRequestToString(link + num);

        Pattern pat = Pattern.compile("class=\"sel3\"");
        Matcher mat = pat.matcher(siteString);

        if (mat.find()) {

            Pattern pattern = Pattern.compile(Camaras.getRegExp(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(siteString);

            if (matcher.find()) {

                HashMap<String, Object> hashMap = new HashMap<>(11);

                if (matcher.group("distrito").equals("COIMBRA")) {
                    for (String elemento : Camaras.camaraStrings) {
                        if (elemento.equals("brasao")) {
                            hashMap.put(elemento, "https://www.anmp.pt/" + matcher.group(elemento));
                        } else {
                            hashMap.put(elemento, matcher.group(elemento));
                        }
                    }
                } else {
                    camara = null;
                    return;
                }
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
