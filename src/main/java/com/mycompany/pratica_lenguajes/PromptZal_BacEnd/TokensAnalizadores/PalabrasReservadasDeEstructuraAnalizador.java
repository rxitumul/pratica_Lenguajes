package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class PalabrasReservadasDeEstructuraAnalizador extends Analizador {
    private String palabraDeAgente;
    private String variable;

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        // Saltar espacios previos
        columna = saltadorDeespacios(columna, linea);

        switch (contadorDeIndices) {
            case 0: // AGENTE - espera un identificador a continuación
                columna = leerPalabraAgente(columna, fila, linea, "identificador de agente");
                break;
            case 1: // contexto - espera un bloque de contenido
                columna = leerPalabraContexto(columna, fila, linea, "valor de contexto");
                break;
            case 2: // variable - espera nombre y valor
                columna = leerPalabraVariable(columna, fila, linea, "nombre de variable", file);
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

        columna = saltadorDeespacios(columna, linea);

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
                columna = verificadorDeCierre(columna, fila, '}');
            }

        }

        return columna;
    }

    private int leerPalabraContexto(int columna, int fila, String linea, String contexto) throws IOException {
        // Saltar espacios
        columna = saltadorDeespacios(columna, linea);

        if (columna < linea.length() && linea.charAt(columna) == '=') {
            columna++;
            columna = lectorDeComillas(columna, fila, linea);
            columna = verificadorDeCierre(columna, fila, '"');
        } else {
            reportesError.registrarError(
                    new ErrorLexico("", "Se esperaba '=' después de 'contexto'", fila, columna));
        }
        return columna;
    }

    private int leerPalabraVariable(int columna, int fila, String linea, String contexto, File file)
            throws IOException {
        int columnaTemporal;
        columna = saltadorDeespacios(columna, linea);
        while (true) {

            if (columna < linea.length() && linea.charAt(columna) == '=') {
                columnaTemporal = analizador(columna, fila, linea, reportesError, file, false);
                if (columnaTemporal != -1) {
                    return columnaTemporal;
                } else {
                    columna = lectorDeComillas(columna, fila, linea);
                    columna = verificadorDeCierre(columna, fila, '"');
                    return columna;
                }
            } else if (columna == linea.length()) {

                return columna;

            } else {
                int inicio = columna;
                columna = concatenadorDePalabaras(columna, linea);
                variable = linea.substring(inicio, columna);
            }

        }
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
