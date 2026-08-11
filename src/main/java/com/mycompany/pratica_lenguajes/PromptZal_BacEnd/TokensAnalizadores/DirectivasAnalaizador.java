package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class DirectivasAnalaizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        int contadorComillas = 0;

        while (columna < linea.length()) {
            if (linea.charAt(columna) == '"') {
                contadorComillas++;
                if (contadorComillas == 2) {
                    columna++;
                    break;
                }
            }
            columna++;
        }
        if (contadorComillas < 2) {
            error = new ErrorLexico("\"...\"", "Falta el cierre de comillas en la directiva", fila, columna);
            reportesError.registrarError(error);
        }
        return columna;
    }

    protected String[] token() {
        return tokens.getDIRECTIVAS();
    }

}