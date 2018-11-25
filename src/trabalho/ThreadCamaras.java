/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;
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

        String result = Requests.httpRequestToString(link + num);

        Pattern pat = Pattern.compile("class=\"sel3\"");
        Matcher mat = pat.matcher(result);

        if (mat.find()) {
            
            Pattern pattern = Pattern.compile("");
            Matcher matcher = pat.matcher(result);
                
            
            
            
            
            
            

        }
    }
    
    public Element getElement(){
        return camara.getElement();
    }
}
