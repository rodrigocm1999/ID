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
public class CamaraMunicipal {
    private static int counter = 0;
    
    private Integer id;
    private Element MainElement;

    public CamaraMunicipal(HashMap<String,String> hashMap) {
        getId();
        
        MainElement = new Element("municipio");
        MainElement.setAttribute("id","id"+id.toString());        
        
        for (int i = 0; i < Camaras.strings.length; i++) {
         
            String str = Camaras.strings[i];
            Element temp = new Element(str);
            
            String strValue = hashMap.get(str);
            String tempString = strValue.replaceAll(" ", "");
            
            if(Util.TryParse(tempString)){
                temp.setText(tempString);
            }else{
                temp.setText(strValue);
            }
            MainElement.addContent(temp);
        }
    }
    
    public Element getElement(){
        return MainElement;
    }
    
       
    private synchronized void getId(){
        this.id=++counter;
    }
}
