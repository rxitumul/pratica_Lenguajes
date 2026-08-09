package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.BibliotecaDeTokens;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteHTMLTabla;

public abstract class Analizador {
    protected BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    protected ReporteDeError error = new ReporteDeError();
    protected ReporteHTMLTabla tabla = new ReporteHTMLTabla();

    public abstract int analizador(int columna, int fila, String linea);

}
