package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class PalabrasReservadasDeEstructuraAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea) {

        switch (contadorDeIndices) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            default:

                break;
        }

        while (columna < linea.length()) {
            if (linea.charAt(columna) == ' ') {
                do {

                } while (linea.charAt(columna) == ' ');
            }
            columna++;

        }

        return columna;
    }

    protected String[] token() {
        return tokens.getPALABRAS_RESERVADAS();
    }

}
