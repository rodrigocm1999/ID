/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        MainWindow main = new MainWindow();
        main.show();
        
        Camaras.Run();
        //Contratos.Run();
    }

    
    public static void juntar2ficheiros(){
        
        Document docTudo = Util.lerDocumentoXML("camaras.xml");
        Document docContratos = Util.lerDocumentoXML("contratos.xml");
        
        
        Element mun = docTudo.getRootElement();
        List<Element> list =  mun.getChildren();
        
        /*Element mun = docContratos.getRootElement();
        List<Element> list =  mun.getChildren();*/
        
        
        
        for (int i = 0; i < list.size(); i++) {
            Element el = list.get(i);
            
            
            
        }
        
        
        
        //municipios.getChild()
        
        
        
        
    }
    
}
