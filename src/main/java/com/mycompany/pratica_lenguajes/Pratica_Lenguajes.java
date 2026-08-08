/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pratica_lenguajes;

import com.mycompany.pratica_lenguajes.PromptZal_FrontEnd.CreadorDePz;
import com.mycompany.pratica_lenguajes.PromptZal_FrontEnd.MenuDeSelecion;

/**
 *
 * @author ricardocastillo
 */

public class Pratica_Lenguajes {

    public static void main(String[] args) {

        CreadorDePz crea = new CreadorDePz();
        MenuDeSelecion menuDeSelecion = new MenuDeSelecion();

        try {

            crea.main();
            menuDeSelecion.menuPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
