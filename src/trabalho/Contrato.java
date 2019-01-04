package trabalho;

import java.util.HashMap;
import org.jdom2.Element;


public class Contrato {

    private static Integer counter = 0;

    private Element MainElement;

    public Contrato(HashMap<String, String> hashMap) {

        MainElement = new Element("contrato");
        MainElement.setAttribute("id", "id" + getId());

        for (int i = 0; i < Contratos.strings.length; i++) {

            String str = Contratos.strings[i];
            Element temp = new Element(str);
            if (str.equals("preco")) {
                
                String precoString = hashMap.get(str);
                precoString = precoString.replace(".", "").replace(",", ".");
                temp.setText(precoString);
            } else {
                temp.setText(hashMap.get(str));
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
