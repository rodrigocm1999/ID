package trabalho;

import java.util.HashMap;
import org.jdom2.Element;

//classe usada para criar o elemento para colocar no Document
public class CamaraMunicipal {
    private static int counter = 0;
    private int id;
    private Element MainElement;

    public CamaraMunicipal(HashMap<String, String> hashMap) {
        //o hashmap recebido tem todos os valores dessa camara

        //a função getId() é um método synchronized para impedir que duas 
        //classes o corram ao mesmo tempo e haverem erros nos IDs
        getId();

        //elemento que agrega todas as folhas do municipio
        MainElement = new Element("municipio");
        MainElement.setAttribute("id", "id" + id);


        for (int i = 0; i < Camaras.strings.length; i++) {

            String str = Camaras.strings[i];
            Element temp = new Element(str);

            //pego o valor do hashmap a partir do array de strings
            String strValue = hashMap.get(str);
            //crio uma cópia do valor e retiro os espaços
            String tempString = strValue.replaceAll(" ", "");
            //se for um número guardo o valor sem os espaços
            if (Util.TryParse(tempString)) {
                temp.setText(tempString);
            } else {
                temp.setText(strValue);
            }
            //adicionar ao elemento agregador
            MainElement.addContent(temp);
        }
    }

    //Getter para o elemento construido
    public Element getElement() {
        return MainElement;
    }

    //usado para pegar o id de forma a não haverem erros nos IDs devido ao acesso assíncrono da variavel
    private synchronized void getId() {
        this.id = ++counter;
    }
}
