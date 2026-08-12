package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ComandosDeIaAnalizador extends Analizador {

    // Los comandos de IA (PREGUNTAR, GENERAR, RESUMIR, etc.) siempre van
    // seguidos de una expresión: un texto, un identificador o una función.
    // Ejemplo:
    // PREGUNTAR "¿Cuáles son las tendencias?" → argumento es un texto
    // GENERAR codigo → argumento es un identificador
    // CARGAR("ventas.csv") → argumento es una función
    @Override
    protected int condicion(int contadorDeColumnas, int contadorDeFilas, String lineaLeida, File archivo)
            throws IOException {
        // Saltamos el espacio entre el comando y su argumento
        contadorDeColumnas = saltarEspacios(lineaLeida, contadorDeColumnas);

        int posicionAntes = contadorDeColumnas;

        // Consumimos la expresión completa que sigue al comando
        contadorDeColumnas = consumirExpresionCompleta(lineaLeida, contadorDeColumnas);

        // Si no avanzamos nada es porque no había argumento → error
        if (contadorDeColumnas == posicionAntes) {
            error = new ErrorLexico("", "Se esperaba un argumento despues del comando de IA",
                    contadorDeFilas, contadorDeColumnas);
            reportesError.registrarError(error);
        }

        return contadorDeColumnas;
    }

    // Los comandos de IA válidos están definidos en BibliotecaDeTokens
    @Override
    protected String[] token() {
        return tokens.getCOMANDOS_DE_IA();
    }
}
