package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    private File fileLocal;

    public int analizador(int columna, int fila, String linea, ReporteDeError reportesError, File file,boolean verdadError)
            throws IOException {
        this.reportesError = reportesError;
        int inicio = columna;
        fileLocal = file;

        columna = concatenadorDePalabaras(columna, linea);
        String palabra = linea.substring(inicio, columna);

        if (esDirectivaValida(palabra)) {
            tabla.tablaDeTokens();
            columna = condicion(columna, fila, linea, file);
        } else if (verdadError) {
            error = new ErrorLexico(palabra, "Token no válida o no encontrada", fila, inicio);
            reportesError.registrarError(error);
        }else{
            return -1;
        }
        return columna;
    }



    protected boolean esDirectivaValida(String palabra) {
        contadorDeIndices = 0; // Reiniciar antes de cada búsqueda
        String[] palabrasDirectivas = token();

        for (String d : palabrasDirectivas) {
            if (d.equals(palabra)) {
                return true;
            }
            contadorDeIndices++;
        }
        return false;
    }

    protected int lectorDeComillas(int columna, int fila, String linea) {
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

    protected int verificadorDeCierre(int columna, int fila, char analizarCiereDe) throws IOException {
        int filaFile2 = 0;
        int contadorColumna2 = 0;
        columna++;
        try (BufferedReader lector = new BufferedReader(new FileReader(fileLocal))) {

            while (true) {
                if (filaFile2 != fila) {
                    lector.readLine();
                    filaFile2++;
                } else {
                    break;
                }
            }
            String lineaLeida = "";
            while ((lineaLeida = lector.readLine()) != null) {
                contadorColumna2 = 0;
                while (contadorColumna2 < lineaLeida.length()) {
                    if (lineaLeida.charAt(contadorColumna2) == analizarCiereDe) {
                        return columna;
                    }
                    contadorColumna2++;
                }
            }
            reportesError.registrarError(
                    new ErrorLexico("", "Se esperaba " + analizarCiereDe + " después de abrir un bloque del agente",
                            fila,
                            columna));
            return columna;
        }
    }

    protected int saltadorDeespacios(int columna, String linea) {
        while (columna < linea.length() && linea.charAt(columna) == ' ') {
            columna++;
        }
        return columna;
    }

    protected int concatenadorDePalabaras(int columna, String linea) {
        while (columna < linea.length() && linea.charAt(columna) != ' ') {
            columna++;
        }
        return columna;
    }

    protected abstract String[] token();

    protected abstract int condicion(int columna, int fila, String linea, File file) throws IOException;

}
