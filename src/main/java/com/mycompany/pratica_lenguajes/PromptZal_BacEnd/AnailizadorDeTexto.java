package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AnailizadorDeTexto {
    private BufferedReader lector;

    public void lector(String paht) {

        int contadorDeFilas = 0;
        int contadorDeColumnas = 0;
        String palabraParaAnalizar = "";
        try {
            lector = new BufferedReader(new FileReader(paht));
            while (true) {
                String lineaLeida = lector.readLine();
                if (lineaLeida != null) {
                    while (true) {
                        char letra = lineaLeida.charAt(contadorDeColumnas);
                        if (letra != ' ') {
                            palabraParaAnalizar = palabraParaAnalizar + letra;

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

    public void comprobador(String letra) {
        BibliotecaDeTokens tokens = new BibliotecaDeTokens();
        char primerLetra = letra.charAt(0);
        String[] comparador;
        if (primerLetra == '@') {
            comparador = tokens.getDIRECTIVAS();
            for (int i = 0; i < comparador.length; i++) {

            }
        }

    }
}
