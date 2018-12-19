/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.HashMap;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class Contrato {

    private static Integer counter = 0;

    private Element MainElement;

    public Contrato(HashMap<String, String> hashMap) {

        MainElement = new Element("contrato");
        MainElement.setAttribute("id", "id" + getId());

        for (int i = 0; i < Contratos.strings.length; i++) {

            String str = Contratos.strings[i];
            Element temp = new Element(str);
            
            String strValue = hashMap.get(str);
            String tempString = strValue.substring(0);
            tempString.replaceAll(" ", "");
            
            if(Util.TryParse(tempString)){
                temp.setText(tempString);
            }else{
                temp.setText(strValue);
            }
            MainElement.addContent(temp);
        }
    }

    public Element getElement() {
        return MainElement;
    }

    private synchronized String getId() {
        return (++counter).toString();
    }
}
