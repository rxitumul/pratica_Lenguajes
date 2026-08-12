package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;
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
    private File file;

    public void lector(String paht) throws IOException {

        int contadorDeFilas = 1;
        file = new File(paht);
        lector = new BufferedReader(new FileReader(file));
        String lineaLeida;
        while ((lineaLeida = lector.readLine()) != null) {
            int contadorDeColumnas = 0;
            while (contadorDeColumnas < lineaLeida.length()) {
                char letra = lineaLeida.charAt(contadorDeColumnas);
                if (letra == ' ') {
                    contadorDeColumnas++;
                } else if (letra == '@') {
                    contadorDeColumnas = directivas.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, file, true);
                } else if ((letra >= 65 && letra <= 90) || letra == 'c' || letra == 'v') {
                    // Leer la palabra completa para decidir el analizador correcto
                    contadorDeColumnas = analizadorComandosIaPalabrasReservadasConectores(contadorDeColumnas,
                            contadorDeFilas, lineaLeida);
                } else if (letra == '-' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '>') {
                    // Bug fix #1: detectar el conector '->' que empieza con '-'
                    contadorDeColumnas = analizadorComandosIaPalabrasReservadasConectores(contadorDeColumnas,
                            contadorDeFilas, lineaLeida);
                } else {
                    // Carácter no reconocido como inicio de token: saltar la palabra completa
                    while (contadorDeColumnas < lineaLeida.length() && lineaLeida.charAt(contadorDeColumnas) != ' ') {
                        contadorDeColumnas++;
                    }
                }
            }
            contadorDeFilas++;
        }
        reportesError.generarHTMLDeError("ReporteErrores.html");
    }

    private int analizadorComandosIaPalabrasReservadasConectores(int columna, int fila, String linea)
            throws IOException {
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
                return conectoresA.analizador(inicio, fila, linea, reportesError, file, true);
            }
        }

        for (String comando : comandosDeIA) {
            if (comando.equals(palabra)) {
                return iaA.analizador(inicio, fila, linea, reportesError, file, true);
            }
        }

        for (String resrvada : palabrasReservadasDeEstructura) {
            if (resrvada.equals(palabra)) {
                return reservadasA.analizador(inicio, fila, linea, reportesError, file, true);
            }
        }

        // La palabra no coincide con ningún token conocido: error léxico
        reportesError.registrarError(new ErrorLexico(palabra, "Token no reconocido", fila, inicio));
        return columna;
    }

    public BufferedReader getArchivoLectura() {
        return lector;
    }
}
