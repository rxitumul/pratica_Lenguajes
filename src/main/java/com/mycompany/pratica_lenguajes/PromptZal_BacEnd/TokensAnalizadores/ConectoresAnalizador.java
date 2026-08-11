package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;



public class ConectoresAnalizador extends Analizador{


    @Override
    protected int condicion(int columna, int fila, String linea) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'condicion'");
    }

    protected String[] token() {
        return tokens.getCONECTORES();
    }
}
