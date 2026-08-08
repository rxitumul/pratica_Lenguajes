package com.mycompany.pratica_lenguajes.PromptZal_FrontEnd;

import java.io.FileWriter;
import java.io.IOException;

public class CreadorDePz {
    public void main() throws IOException {
        String ruta = "'/Users/ricardocastillo/Documents/Prueba.pz";

        FileWriter archivo = new FileWriter(ruta);

        archivo.write("Hola mundo");

        archivo.close();
    }
}
