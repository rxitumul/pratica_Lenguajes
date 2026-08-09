package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class DirectivasAnalaizador extends Analizador {

    public int analizador(int columna, int fila, String linea, ReporteDeError reportesError) {
        this.reportesError = reportesError;
        int inicio = columna;
        while (columna < linea.length() && linea.charAt(columna) != ' ') {
            columna++;
        }
        String palabra = linea.substring(inicio, columna);
        if (esDirectivaValida(palabra)) {
            tabla.tablaDeDirectivas();
            columna = condicion(columna, fila, linea);
        } else {
            error = new ErrorLexico(palabra, "Directiva no válida o no encontrada", fila, inicio);
            reportesError.registrarError(error);
        }
        return columna;
    }

    @Override
    protected int condicion(int columna, int fila, String linea) {
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

    private boolean esDirectivaValida(String palabra) {
        String[] palabrasDirectivas = tokens.getDIRECTIVAS();
        for (String d : palabrasDirectivas) {
            if (d.equals(palabra)) {
                return true;
            }
        }
        return false;
    }
}