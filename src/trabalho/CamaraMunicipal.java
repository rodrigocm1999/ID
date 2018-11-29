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
    protected static int counter = 0;
    
    protected Integer id;
    protected Element MainElement;

    public CamaraMunicipal(HashMap<String,Object> hashMap) {
    
        id=++counter;
        
        MainElement = new Element("municipio");
        MainElement.setAttribute("id",id.toString());        
        
        for (int i = 0; i < Camaras.camaraStrings.length; i++) {
            
            String str = Camaras.camaraStrings[i];
            Element temp = new Element(str);
            temp.setText(hashMap.get(str).toString());
            MainElement.addContent(temp);
        }
    }
    
    public Element getElement(){
        return MainElement;
    }
    
    
}
