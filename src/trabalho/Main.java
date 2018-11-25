/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Link:    https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3000
        /*Os Minicipios de coimbra começam em
        https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3060
            e acabam em
        https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3230

            3060 - 3230    Os numeros sobem de 10 em 10
            Alguns nao teem informação 
         */
        String link = "https://www.anmp.pt/anmp/pro/mun1/mun101w3.php?cod=M3230";

        int num_start = 3060;
        int num_end = 3230;

        ArrayList<ThreadCamaras> threadCamaras = new ArrayList<>();
        ArrayList<CamaraMunicipal> camaras = new ArrayList<>();
        
        for (int i = num_start; i <= num_end; i += 10) {

            ThreadCamaras thread = new ThreadCamaras(link, i,camaras);
            thread.start();

            threadCamaras.add(thread);

            //Backup :     <div class="sel3">(.*)<\/div><br><div class="f3">(.*)\n.*\n.*\n.*\n.*<div class="f1" align="left"><a href="(.*")\sclass="f2"
            /*
            Identificador único (gerado pelo aluno), Nome do Município, Nome do Presidente, E-mail, Site
            Institucional, Telefone, Número de Freguesias, Área, Número Habitantes, Brasão
             */
        }

        for (int i = 0; i <= threadCamaras.size(); i++) {
            try {
                threadCamaras.get(i).join();
            } catch (Exception ex) {
                System.out.println("\nErro na threa nº"+i);
                ex.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

}
