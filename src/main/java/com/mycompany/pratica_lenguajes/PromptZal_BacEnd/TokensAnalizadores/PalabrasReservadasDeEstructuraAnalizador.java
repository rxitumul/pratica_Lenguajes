package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class PalabrasReservadasDeEstructuraAnalizador extends Analizador {
    private String palabraDeAgente;
    private File fileLocal;

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        fileLocal = file;
        // Saltar espacios previos
        while (columna < linea.length() && linea.charAt(columna) == ' ') {
            columna++;
        }

        switch (contadorDeIndices) {
            case 0: // AGENTE - espera un identificador a continuación
                columna = leerPalabraAgente(columna, fila, linea, "identificador de agente");
                break;
            case 1: // contexto - espera un bloque de contenido
                columna = leerPalabraContexto(columna, fila, linea, "valor de contexto");
                break;
            case 2: // variable - espera nombre y valor
                columna = leerPalabraVariable(columna, fila, linea, "nombre de variable");
                break;
            case 3: // EJECUTAR - espera un comando o bloque
                columna = leerPalabraEjecucion(columna, fila, linea, "bloque de ejecución");
                break;
            case 4: // EXPORTAR - espera un formato o destino
                columna = leerPalabraExportacion(columna, fila, linea, "destino de exportación");
                break;
            default:
                break;
        }

        return columna;
    }

    private int leerPalabraAgente(int columna, int fila, String linea, String contexto) throws IOException {
        int inicio = columna;
        int filaFile2 = 0;
        int contadorColumna2 = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(fileLocal))) {
            while (columna < linea.length() && linea.charAt(columna) != ' ') {
                columna++;
            }
            if (columna == inicio) {
                reportesError.registrarError(
                        new ErrorLexico("", "Se esperaba " + contexto + " después de la palabra reservada", fila,
                                columna));
                return columna;
            } else {
                palabraDeAgente = linea.substring(inicio, columna);
                if (linea.charAt(columna) != '{') {
                    reportesError.registrarError(
                            new ErrorLexico("", "Se esperaba '{' después del identificador de agente", fila, columna));
                    return columna;
                } else {
                    columna++;
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
                            if (lineaLeida.charAt(contadorColumna2) == '}') {
                                return columna;
                            }
                            contadorColumna2++;
                        }
                    }
                    reportesError.registrarError(
                            new ErrorLexico("", "Se esperaba '}' después de abrir un bloque del agente", fila,
                                    columna));
                }
            }
        }
        return columna;
    }

    private int leerPalabraContexto(int columna, int fila, String linea, String contexto) throws IOException {
        // Saltar espacios
        while (columna < linea.length() && linea.charAt(columna) == ' ') {
            columna++;
        }
        if (columna < linea.length() && linea.charAt(columna) == '=') {
            columna++;
        } else {
            reportesError.registrarError(
                    new ErrorLexico("", "Se esperaba '=' después de 'contexto'", fila, columna));
        }
        return columna;
    }

    private int leerPalabraVariable(int columna, int fila, String linea, String contexto) throws IOException {

        return columna;
    }

    private int leerPalabraEjecucion(int columna, int fila, String linea, String contexto) throws IOException {

        return columna;
    }

    private int leerPalabraExportacion(int columna, int fila, String linea, String contexto) throws IOException {

        return columna;
    }

    protected String[] token() {
        return tokens.getPALABRAS_RESERVADAS();
    }

}
