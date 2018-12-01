/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho;

import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 *
 * @author Rodrigo
 */
public class Util {

    public static String lerFicheiroTexto(String path) {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(path));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = dis.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
        }
        return null;
    }
}
