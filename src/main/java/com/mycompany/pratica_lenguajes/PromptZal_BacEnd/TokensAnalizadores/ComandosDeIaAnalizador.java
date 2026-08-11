package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ComandosDeIaAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        // Saltar espacios entre el comando y su argumento
        while (columna < linea.length() && linea.charAt(columna) == ' ') {
            columna++;
        }

        int inicio = columna;
        // Leer el argumento del comando de IA
        while (columna < linea.length() && linea.charAt(columna) != ' ') {
            columna++;
        }

        if (columna == inicio) {
            error = new ErrorLexico("", "Se esperaba un argumento después del comando de IA", fila, columna);
            reportesError.registrarError(error);
        }

        return columna;
    }

    protected String[] token() {
        return tokens.getCOMANDOS_DE_IA();
    }
}
