package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.ComandosDeIaAnalizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.ConectoresAnalizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.DirectivasAnalaizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.PalabrasReservadasDeEstructuraAnalizador;

public class AnailizadorDeTexto {
    private BufferedReader lector;
    private BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    private DirectivasAnalaizador directivas = new DirectivasAnalaizador();
    private PalabrasReservadasDeEstructuraAnalizador reservadasA = new PalabrasReservadasDeEstructuraAnalizador();
    private ConectoresAnalizador conectoresA = new ConectoresAnalizador();
    private ComandosDeIaAnalizador iaA = new ComandosDeIaAnalizador();

    public void lector(String paht) {

        int contadorDeFilas = 0;
        int contadorDeColumnas = 0;
        try {
            lector = new BufferedReader(new FileReader(paht));
            while (true) {
                String lineaLeida = lector.readLine();
                if (lineaLeida != null) {
                    while (true) {
                        char letra = lineaLeida.charAt(contadorDeColumnas);
                        if (letra != ' ') {

                        } else if (letra == 64) {
                            contadorDeColumnas = directivas.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida);

                        } else if (letra >= 65 && letra <= 90) {
                            contadorDeColumnas = analizadorComandosIaPalabrasReservadasConectores(contadorDeColumnas,
                                    contadorDeFilas, lineaLeida);

                        } else if (letra == 99 || letra == 118) {
                            reservadasA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida);

                        } else if (contadorDeColumnas == lineaLeida.length()) {
                            break;
                        } else {

                        }
                        contadorDeColumnas++;
                    }
                    contadorDeFilas++;
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    private int analizadorComandosIaPalabrasReservadasConectores(int columna, int fila, String linea) {
        int inicio = columna;
        String[] palabrasReservadasDeEstructura = tokens.getPALABRAS_RESERVADAS();
        String[] comandosDeIA = tokens.getCOMANDOS_DE_IA();
        String[] conectores = tokens.getCONECTORES();
        while (true) {
            if (linea.charAt(columna) != ' ') {
                columna++;
            } else {
                break;
            }
        }

        String palabra = linea.substring(inicio, columna);

        for (String conector : conectores) {
            if (conector.equals(palabra)) {
                conectoresA.analizador(inicio, fila, linea);
                return columna;
            }
        }

        for (String comando : comandosDeIA) {
            if (comando.equals(palabra)) {
                iaA.analizador(inicio, fila, linea);
                return columna;
            }
        }

        for (String resrvada : palabrasReservadasDeEstructura) {
            if (resrvada.equals(palabra)) {
                reservadasA.analizador(inicio, fila, linea);
                return columna;
            }
        }
        return columna;
    }

}
