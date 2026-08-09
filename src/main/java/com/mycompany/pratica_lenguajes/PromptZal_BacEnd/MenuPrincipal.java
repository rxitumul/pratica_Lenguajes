package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.util.Scanner;

import com.mycompany.pratica_lenguajes.PromptZal_FrontEnd.MenuDeSelecion;

public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in);

    public void menuPrincipalInicio() {
        String phat;
        MenuDeSelecion front = new MenuDeSelecion();
        AnailizadorDeTexto analizador = new AnailizadorDeTexto();
        front.menuPrincipal();
        phat = scanner.nextLine();
        analizador.lector(phat);

    }
}
