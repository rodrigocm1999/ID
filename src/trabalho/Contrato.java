package trabalho;

import java.util.HashMap;
import org.jdom2.Element;


public class Contrato {

    private static Integer counter = 0;

    private Element MainElement;

    public Contrato(HashMap<String, String> hashMap) {
        //o hashmap recebido tem todos os valores dessa camara

        //elemento que agrega todas as folhas
        MainElement = new Element("contrato");
        MainElement.setAttribute("id", "id" + getId());
        //a função getId() é um método synchronized para impedir que duas 
        //classes o corram ao mesmo tempo e haverem erros nos IDs        

        for (int i = 0; i < Contratos.strings.length; i++) {

            String str = Contratos.strings[i];
            Element temp = new Element(str);

            //se for o preço trato a String de maneira diferente
            if (str.equals("preco")) {
                //pois o preço tem espaços pontos e virgulas
                // e é necessário formatar corretamente
                String precoString = hashMap.get(str);
                precoString = precoString.replace(".", "").replace(",", ".");
                temp.setText(precoString);
            } else {
                temp.setText(hashMap.get(str));
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
    private synchronized String getId() {
        return (++counter).toString();
    }
}
