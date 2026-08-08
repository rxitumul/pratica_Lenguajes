package com.mycompany.pratica_lenguajes.PromptZal_FrontEnd;

public class MenuDeSelecion {
    private Configuraciones confi = new Configuraciones();

    public void menuPrincipal() {
        confi.delayThread();
        confi.limpiadorDeLineas();
        confi.separadorDeLineas();
        System.out.println(confi.formatear("PromptZal"));
        confi.separadorDeLineas();
        System.out.println(confi.formatear("Porfavor ingrese la ruta del archivo a analizar"));
        System.out.println(confi.formatear("Ejemplo; '/Users/ricardocastillo/Documents/Archivo de prueba.pz .html' "));
    }

}
