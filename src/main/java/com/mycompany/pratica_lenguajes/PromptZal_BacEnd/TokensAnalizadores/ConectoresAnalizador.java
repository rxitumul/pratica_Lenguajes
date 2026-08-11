package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ConectoresAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        // Saltar espacios entre el conector y su complemento
        while (columna < linea.length() && linea.charAt(columna) == ' ') {
            columna++;
        }

        int inicio = columna;
        // Leer el complemento del conector
        while (columna < linea.length() && linea.charAt(columna) != ' ') {
            columna++;
        }

        if (columna == inicio) {
            error = new ErrorLexico("", "Se esperaba un complemento después del conector", fila, columna);
            reportesError.registrarError(error);
        }

        return columna;
    }

    protected String[] token() {
        return tokens.getCONECTORES();
    }
}
