package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnailizadorDeTexto {
    private BufferedReader lector;
    private BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    private ReporteDeError error = new ReporteDeError();
    private ReporteHTMLTabla tabla = new ReporteHTMLTabla();

    public void lector(String paht) {

        int contadorDeFilas = 0;
        int contadorDeColumnas = 0;
        boolean palabraVariada = false;
        try {
            lector = new BufferedReader(new FileReader(paht));
            while (true) {
                String lineaLeida = lector.readLine();
                if (lineaLeida != null) {
                    while (true) {
                        char letra = lineaLeida.charAt(contadorDeColumnas);
                        if (letra != ' ') {

                        } else if (letra == 64) {
                            contadorDeColumnas = analizador(contadorDeColumnas, contadorDeFilas, lineaLeida);
                        } else if (letra >= 65 && letra <= 90) {

                        } else if (letra == 97) {

                        } else if (letra == 99 || letra == 118) {

                        } else if (palabraVariada) {

                        } else {

                        }
                        contadorDeColumnas++;
                    }
                }
                contadorDeFilas++;
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    private int analizador(int columna, int fila, String letra) {
        while (true) {
            if (comprobadorDirectivas(columna, letra.charAt(columna))) {
                columna++;
            } else if (letra.charAt(columna) != ' ') {
                tabla.tablaDeDirectivas();
                break;
            } else {
                error.archivoHTMLDeError(fila, columna);
                columna++;
            }

        }

        return columna;
    }

    private boolean comprobadorDirectivas(int indice, char letra) {
        String[] palbrasDeDirctivas = tokens.getDIRECTIVAS();
        for (int i = 0; i < palbrasDeDirctivas.length; i++) {
            if (palbrasDeDirctivas[i].charAt(indice) == letra) {
                return true;
            }
        }
        return false;

    }
}
