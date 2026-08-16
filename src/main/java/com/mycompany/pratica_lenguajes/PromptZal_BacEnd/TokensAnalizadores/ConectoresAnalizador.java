package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ConectoresAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        int inicio = columna;
        columna = movedorDeColumnasHastaFinDeEsprecion(linea, columna, fila);
        if (columna == inicio) {
            error = new ErrorLexico("", "Se esperaba un complemento después del conector", fila, columna);
            reportesError.registrarError(error);
            return columna;
        }

        // Si el conector es "->", registramos la variable asignada
        if (contadorDeIndices == 4) {
            String varName = linea.substring(inicio, columna).trim();
            // verificar nombre de variable
            if (!varName.isEmpty() && Character.isLetter(varName.charAt(0))) {
                // verificar que exista la variable
                variablesDeclaradas.agregarAlFinal(varName);
            }
        }

        return columna;
    }

    protected String[] token() {
        return tokens.getCONECTORES();
    }
}
