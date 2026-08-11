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
    private ReporteDeError reportesError = new ReporteDeError();


    
    public void lector(String paht) throws IOException {

        int contadorDeFilas = 1;
        lector = new BufferedReader(new FileReader(paht));
        String lineaLeida;
        while ((lineaLeida = lector.readLine()) != null) {
            int contadorDeColumnas = 0;
            while (contadorDeColumnas < lineaLeida.length()) {
                char letra = lineaLeida.charAt(contadorDeColumnas);
                if (letra == ' ') {
                    contadorDeColumnas++;
                } else if (letra == '@') {
                    contadorDeColumnas = directivas.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError);
                } else if (letra >= 65 && letra <= 90) {

                    contadorDeColumnas = analizadorComandosIaPalabrasReservadasConectores(contadorDeColumnas,
                            contadorDeFilas, lineaLeida);

                } else if (letra == 99 || letra == 118) {
                    contadorDeColumnas = reservadasA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError);

                } else {

                    contadorDeColumnas++;
                }
            }
            contadorDeFilas++;
        }
        reportesError.generarHTMLDeError("ReporteErrores.html");
    }

    private int analizadorComandosIaPalabrasReservadasConectores(int columna, int fila, String linea) {
        int inicio = columna;
        String[] palabrasReservadasDeEstructura = tokens.getPALABRAS_RESERVADAS();
        String[] comandosDeIA = tokens.getCOMANDOS_DE_IA();
        String[] conectores = tokens.getCONECTORES();
        while (true) {
            if (columna < linea.length() && linea.charAt(columna) != ' ') {
                columna++;
            } else {
                break;
            }
        }

        String palabra = linea.substring(inicio, columna);

        for (String conector : conectores) {
            if (conector.equals(palabra)) {
                conectoresA.analizador(inicio, fila, linea, reportesError);
                return columna;
            }
        }

        for (String comando : comandosDeIA) {
            if (comando.equals(palabra)) {
                iaA.analizador(inicio, fila, linea, reportesError);
                return columna;
            }
        }

        for (String resrvada : palabrasReservadasDeEstructura) {
            if (resrvada.equals(palabra)) {
                reservadasA.analizador(inicio, fila, linea, reportesError);
                return columna;
            }
        }
        return columna;
    }

    public BufferedReader getArchivoLectura(){
        return lector;
    }
}
