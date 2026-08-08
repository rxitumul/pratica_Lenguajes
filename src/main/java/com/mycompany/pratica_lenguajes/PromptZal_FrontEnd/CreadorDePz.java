package com.mycompany.pratica_lenguajes.PromptZal_FrontEnd;

import java.io.FileWriter;
import java.io.IOException;

public class CreadorDePz {
    public void main() throws IOException {
        FileWriter archivo = new FileWriter("/Users/ricardocastillo/Documents/Prueba.pz");

        archivo.write("Hola mundo");

        archivo.close();
    }
}
