package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.BibliotecaDeTokens;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteHTMLTabla;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public abstract class Analizador {
    protected BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    protected ReporteDeError reportesError;
    protected ReporteHTMLTabla tabla = new ReporteHTMLTabla();
    protected ErrorLexico error;
    protected int contadorDeIndices = 0;

    public int analizador(int columna, int fila, String linea, ReporteDeError reportesError) {
        this.reportesError = reportesError;
        int inicio = columna;
        while (columna < linea.length() && linea.charAt(columna) != ' ') {
            columna++;
        }
        String palabra = linea.substring(inicio, columna);

        if (esDirectivaValida(palabra)) {
            tabla.tablaDeDirectivas();
            columna = condicion(columna, fila, linea, palabra);
        } else {
            error = new ErrorLexico(palabra, "Directiva no válida o no encontrada", fila, inicio);
            reportesError.registrarError(error);
        }
        return columna;
    }

    protected boolean esDirectivaValida(String palabra) {
        String[] palabrasDirectivas = token();

        for (String d : palabrasDirectivas) {

            if (d.equals(palabra)) {
                return true;
            }
            contadorDeIndices++;
        }
        return false;
    }

    protected abstract String[] token();

    protected abstract int condicion(int columna, int fila, String linea);

}
