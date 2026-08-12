package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.Analizador;
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
    private File file2;
    private ReporteHTMLTabla reporteHTMLTabla = new ReporteHTMLTabla();

    public void lector(String paht) throws IOException {
        Analizador.limpiarDeclaraciones();
        int contadorDeFilas = 1;
        file = new File(paht);
        lector = new BufferedReader(new FileReader(file));
        String lineaLeida;
        int contadorDeCierreFila = 0;
        while ((lineaLeida = lector.readLine()) != null) {
            int contadorDeColumnas = 0;
            while (contadorDeColumnas < lineaLeida.length() && contadorDeCierreFila < contadorDeFilas) {
                // Saltar espacios/tabs iniciales
                contadorDeColumnas = directivas.saltarEspacios(lineaLeida, contadorDeColumnas);
                if (contadorDeColumnas >= lineaLeida.length()) {
                    break;
                }

                char letra = lineaLeida.charAt(contadorDeColumnas);

                // 1. Descartar comentarios
                if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '/') {
                    break; // Ignorar el resto de la línea
                }
                if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '*') {
                    file2 = file;

                    int contadorDeColumnasBloques = contadorDeColumnas;
                    while ((lineaLeida = lector.readLine()) != null) {
                        while (contadorDeColumnasBloques < lineaLeida.length()) {
                            if (lineaLeida.charAt(contadorDeColumnasBloques) == '*'
                                    && contadorDeColumnasBloques + 1 < lineaLeida.length()
                                    && lineaLeida.charAt(contadorDeColumnasBloques + 1) == '/') {
                                break;
                            }
                            contadorDeColumnasBloques++;
                        }
                        contadorDeColumnasBloques = 0;
                        contadorDeCierreFila++;
                    }
                    if (contadorDeCierreFila > contadorDeFilas) {
                        break;
                    }
                }

                // 2. Delimitadores estructurales
                if (letra == '{' || letra == '}') {
                    contadorDeColumnas++;
                    continue;
                }

                // 3. Leer la palabra o token actual
                String token = "";
                if (letra == '@' || (letra == '-' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '>')) {
                    if (letra == '@') {
                        token = directivas.leerDirectivaOConector(lineaLeida, contadorDeColumnas);
                    } else {
                        token = "->";
                    }
                } else {
                    token = directivas.leerPalabra(lineaLeida, contadorDeColumnas);
                }

                if (token.isEmpty()) {
                    // Si no es una palabra, podría ser un operador suelto no reconocido en este
                    // nivel
                    reportesError.registrarError(new ErrorLexico(String.valueOf(letra),
                            "Caracter no reconocido o fuera de contexto", contadorDeFilas, contadorDeColumnas));
                    contadorDeColumnas++;
                    continue;
                }

                // 4. Distribuir el token de acuerdo a su tipo
                if (token.startsWith("@")) {
                    contadorDeColumnas = directivas.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, file, true);
                } else if (esConector(token)) {
                    contadorDeColumnas = conectoresA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, file, true);
                } else if (esComandoIA(token)) {
                    contadorDeColumnas = iaA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, file, true);
                } else if (esPalabraReservada(token)) {
                    contadorDeColumnas = reservadasA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, file, true);
                } else {
                    reportesError.registrarError(new ErrorLexico(token,
                            "Instruccion o token no reconocido en este contexto", contadorDeFilas, contadorDeColumnas));
                    contadorDeColumnas += token.length();
                }
            }
            contadorDeFilas++;
        }
        reportesError.generarHTMLDeError("ReporteErrores.html");
    }

    private boolean esConector(String token) {
        for (String c : tokens.getCONECTORES()) {
            if (c.equals(token))
                return true;
        }
        return false;
    }

    private boolean esComandoIA(String token) {
        for (String c : tokens.getCOMANDOS_DE_IA()) {
            if (c.equals(token))
                return true;
        }
        return false;
    }

    private boolean esPalabraReservada(String token) {
        for (String p : tokens.getPALABRAS_RESERVADAS()) {
            if (p.equals(token))
                return true;
        }
        return false;
    }

    public BufferedReader getArchivoLectura() {
        return lector;
    }
}
