package com.mycompany.pratica_lenguajes.PromptZal_FrontEnd;

import java.util.Scanner;

public class MenuDeSelecion {
    private Configuraciones confi = new Configuraciones();

    public void menuPrincipal() {
        confi.delayThread();
        confi.limpiadorDeLineas();
        confi.separadorDeLineas();
        System.out.println(confi.formatear("PromptZal"));
        confi.separadorDeLineas();
        System.out.println(confi.formatear("Porfavor de ingresasr la ruta del archivo a analizar"));
        System.out.println(confi.formatear("Ejemplo: /Users/ricardocastillo/Documents/Prueba.pz"));
        confi.separadorDeLineas();
    }

    public void analizando() {
        confi.delayThread();
        confi.limpiadorDeLineas();
        confi.separadorDeLineas();
        System.out.println(confi.formatear("analizando en archivo"));
        confi.separadorDeLineas();
    }
}
