package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.BibliotecaDeTokens;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteHTMLTabla;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public abstract class Analizador {
    protected BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    protected ReporteDeError reportesError;
    protected ReporteHTMLTabla tabla = new ReporteHTMLTabla();
    protected ErrorLexico error ;

    public abstract int analizador(int columna, int fila, String linea,ReporteDeError reportesError);
    protected abstract int condicion(int columna, int fila, String linea);

}
