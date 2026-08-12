package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ComandosDeIaAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {

    }

    protected String[] token() {
        return tokens.getCOMANDOS_DE_IA();
    }
}
