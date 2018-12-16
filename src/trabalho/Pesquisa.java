/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.jdom2.Document;

/**
 *
 * @author Rodrigo
 */
public class Pesquisa {

    public Document doc;
    public ArrayList<Element> elements;
    public Search[] search;

    private class Search {

        public String elName;
        public String elValue;
        public boolean found;

        public Search(String elName, String elValue) {
            this.elName = elName;
            this.elValue = elValue;
            this.found = false;
        }
    }

    public Pesquisa(Document doc, String searchString) throws Exception {

        this.doc = doc;
        this.elements = new ArrayList<>();

        String[] splitted = searchString.split("&");
        this.search = new Search[splitted.length];

        for (int i = 0; i < splitted.length; i++) {
            String string = splitted[i];
            String[] splittedAgain = string.split("=");

            search[i] = new Search(splittedAgain[0], splittedAgain[1]);
        }
    }

    public void Pesquisa() {
        Search(doc.getRootElement());

    }

    private boolean Search(Element el) {
        boolean hitEnd = false;
        List<Element> children = el.getChildren();

        if (children.isEmpty() == true) {
            return true;
        }
        for (Element child : children) {
            boolean hasSearchedTerm = false;
            boolean isNotTheOne = false;

            for (int i = 0; i < search.length; i++) {

                if (child.getName().equals(search[i].elName)) {
                    hasSearchedTerm = true;
                    if (!child.getValue().contains(search[i].elValue)) {
                        isNotTheOne = true;
                    } else {
                        search[i].found = true;
                        
                        boolean addNow = true;
                        for (int k = 0; k < search.length; k++) {
                            if (search[k].found == false) {
                                addNow = false;
                            }
                        }
                        if (addNow) {
                            elements.add(el);
                        }
                    }
                }
            }
            if (hasSearchedTerm == false || (hasSearchedTerm == true && isNotTheOne == false)) {
                Element e = child.clone();
                e.detach();
                hitEnd = Search(e);
            }
        }
        if (hitEnd) {
            return true;
        }
        return false;
    }
}
