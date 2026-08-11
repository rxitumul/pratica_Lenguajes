package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.IOException;
import java.util.Scanner;

import com.mycompany.pratica_lenguajes.PromptZal_FrontEnd.MenuDeSelecion;

public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in);

    public void menuPrincipalInicio() {
        while (true) {
            try {
                String phat;
                MenuDeSelecion front = new MenuDeSelecion();
                AnailizadorDeTexto analizador = new AnailizadorDeTexto();
                front.menuPrincipal();
                phat = scanner.nextLine();
                analizador.lector(phat);
                System.out.println("fin del analisis corectamente ");
                System.out.println("Desea continuar con otro archivo escriba 's' sino presione enter");
                if (!scanner.nextLine().equals("s")) {
                    break;
                }
                
            } catch (IOException e) {
                System.out.println("No se pudo encontrar el archivo vuelva a intentar ");
                e.printStackTrace();
            }
        }

    }
}
