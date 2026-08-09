package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

public class DirectivasAnalaizador extends Analizador{

    public int analizador(int columna, int fila, String linea) {
        while (true) {
            if (comprobadorDirectivas(columna, linea.charAt(columna))) {
                columna++;
            } else if (linea.charAt(columna) != ' ') {
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
