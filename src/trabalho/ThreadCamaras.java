/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class ThreadCamaras extends Thread {

    private String link;
    private int num;
    private CamaraMunicipal camara;

    public ThreadCamaras(String link, int num) {
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

            Pattern pattern = Pattern.compile(Main.getRegExp(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(siteString);

            if (matcher.find()) {

                HashMap<String, Object> hashMap = new HashMap<>();

                if (matcher.group("distrito").equals("COIMBRA")) {
                    for (int i = 0; i < Main.camaraStrings.length; i++) {
                        String elemento = Main.camaraStrings[i];
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
