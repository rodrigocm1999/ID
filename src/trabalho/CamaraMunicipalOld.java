/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import org.jdom2.Element;

/**
 *
 * @author Rodrigo
 */
public class CamaraMunicipalOld {
    protected static int counter = 0;
    
    protected Integer id;
    protected String nome;
    protected String presidente;
    protected String email;
    protected String site;
    protected Integer telefone;
    protected Integer nFreguesias;
    protected Integer area;
    protected Integer nHabitantes;
    protected String feriado;
    protected String brasao;
    protected Element element;

    public CamaraMunicipalOld(String nome_, String presidente_, String email_, String site_, int telefone_, int nFreguesias_, int area_, int nHabitantes_,String feriado_, String brasao_) {
        counter++;
        this.id = counter;
        this.nome = nome_;
        this.presidente = presidente_;
        this.email = email_;
        this.site = site_;
        this.telefone = telefone_;
        this.nFreguesias = nFreguesias_;
        this.area = area_;
        this.nHabitantes = nHabitantes_;
        this.feriado = feriado_;
        this.brasao = brasao_;
        
        
        element = new Element("municipio");
        
        Element elNome = new Element("nome");
        elNome.setText(nome);
        element.addContent(elNome);
        
        Element elPresidente = new Element("presidente");
        elPresidente.setText(presidente);
        element.addContent(elPresidente);
        
        Element elEmail = new Element("email");
        elEmail.setText(email);
        element.addContent(elEmail);
        
        Element elSite = new Element("site");
        elSite.setText(site);
        element.addContent(elSite);
        
        Element elTelefone = new Element("telefone");
        elTelefone.setText(telefone.toString());
        element.addContent(elTelefone);
        
        Element elFreguesias = new Element("freguesias");
        elFreguesias.setText(nFreguesias.toString());
        element.addContent(elFreguesias);
        
        Element elArea = new Element("area");
        elArea.setText(area.toString());
        element.addContent(elArea);
        
        Element elHabitantes = new Element("habitantes");
        elHabitantes.setText(nHabitantes.toString());
        element.addContent(elHabitantes);
        
        Element elBrasao = new Element("brasao");
        elBrasao.setText(brasao.toString());
        element.addContent(elBrasao);            
    }

    public Element getElement() {
        return element;
    }

}
