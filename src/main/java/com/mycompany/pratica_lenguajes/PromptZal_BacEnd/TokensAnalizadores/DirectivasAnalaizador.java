package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

public class DirectivasAnalaizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
         lectorDeComillas(columna, fila, linea);
         return verificadorDeCierre(columna, fila, '"');
    }

    protected String[] token() {
        return tokens.getDIRECTIVAS();
    }

}