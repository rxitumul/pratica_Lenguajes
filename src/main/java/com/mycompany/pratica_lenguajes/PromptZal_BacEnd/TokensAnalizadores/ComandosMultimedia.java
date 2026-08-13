package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

public class ComandosMultimedia {
    
    // Recorre Caracter por Caracter saltando los espacios en blanco
    public int saltarEspacios(String linea, int columna) {
        while (columna < linea.length() && (linea.charAt(columna) == ' ' || linea.charAt(columna) == '\t')) {
            columna++;
        }
        return columna;
    }

    // Lee un identificador, número o comando Caracter por Caracter
    public String leerPalabra(String linea, int columna) {
        int inicio = columna;
        while (columna < linea.length()) {
            char columnasLocales = linea.charAt(columna);
            if (Character.isLetterOrDigit(columnasLocales) || columnasLocales == '_' || columnasLocales == '-') {
                columna++;
            } else {
                break;
            }
        }
        return linea.substring(inicio, columna);
    }

    // Lee una directiva que inicia con @ o el conector -> Caracter por Caracter
    public String leerDirectivaOConector(String linea, int columna) {
        int inicio = columna;
        if (columna < linea.length() && linea.charAt(columna) == '@') {
            columna++;
            while (columna < linea.length()
                    && (Character.isLetterOrDigit(linea.charAt(columna)) || linea.charAt(columna) == '_')) {
                columna++;
            }
        }
        return linea.substring(inicio, columna);
    }

}
