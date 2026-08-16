package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.Analizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.ComandosDeIaAnalizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.ComandosMultimedia;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.ConectoresAnalizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.DirectivasAnalaizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores.PalabrasReservadasDeEstructuraAnalizador;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensRegistrados.RegistroDeTokens;

public class AnailizadorDeTexto {
    private BufferedReader lector;
    private BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    private DirectivasAnalaizador directivas = new DirectivasAnalaizador();
    private PalabrasReservadasDeEstructuraAnalizador reservadasA = new PalabrasReservadasDeEstructuraAnalizador();
    private ConectoresAnalizador conectoresA = new ConectoresAnalizador();
    private ComandosDeIaAnalizador iaA = new ComandosDeIaAnalizador();
    private ReporteDeError reportesError = new ReporteDeError();
    private ReporteHTMLTabla reporteHTMLTabla = new ReporteHTMLTabla();
    private ComandosMultimedia comando = new ComandosMultimedia();
    private File file;
    private File file2;

    public void lector(String paht, int contadorDeAarchivosAnalizados) throws IOException {
        Analizador.limpiarDeclaraciones();
        int contadorDeFilas = 1;
        file = new File(paht);
        lector = new BufferedReader(new FileReader(file));
        String lineaLeida;
        int contadorDeCierreFila = 0;

        while ((lineaLeida = lector.readLine()) != null) {

            // cambio de linea

            int contadorDeColumnas = 0;
            while (contadorDeColumnas < lineaLeida.length() && contadorDeCierreFila < contadorDeFilas) {

                // Saltar espacios/tabs iniciales

                contadorDeColumnas = comando.saltarEspacios(lineaLeida, contadorDeColumnas);

                if (contadorDeColumnas >= lineaLeida.length()) {
                    break;
                }

                char letra = lineaLeida.charAt(contadorDeColumnas);

                // Descartar comentarios y bloques de comentarios
                if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '/') {
                    reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("//", "Reconocido", contadorDeFilas,
                            contadorDeColumnas, "Comentarios"));
                    break; // Ignorar el resto de la línea
                }

                if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                        && lineaLeida.charAt(contadorDeColumnas + 1) == '*') {

                    file2 = file;

                    try (BufferedReader lector2 = new BufferedReader(new FileReader(file2))) {

                        int lineasAvanzadas = 0;
                        while (lineasAvanzadas < contadorDeFilas && lector2.readLine() != null) {
                            lineasAvanzadas++;
                        }

                        lineaLeida = lector2.readLine();

                        int contadorDeColumnasBloques = contadorDeColumnas + 2;
                        boolean comentarioCerrado = false;

                        while (lineaLeida != null && !comentarioCerrado) {

                            while (contadorDeColumnasBloques < lineaLeida.length()) {
                                if (lineaLeida.charAt(contadorDeColumnasBloques) == '*'
                                        && contadorDeColumnasBloques + 1 < lineaLeida.length()
                                        && lineaLeida.charAt(contadorDeColumnasBloques + 1) == '/') {
                                    reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("/*..*/", "Reconocido",
                                            contadorDeFilas, contadorDeColumnas, "Comentarios"));

                                    comentarioCerrado = true;
                                    contadorDeColumnas = contadorDeColumnasBloques + 2;
                                    break;
                                }
                                contadorDeColumnasBloques++;
                            }

                            if (!comentarioCerrado) {
                                contadorDeColumnasBloques = 0;
                                contadorDeCierreFila++;
                                lineaLeida = lector2.readLine();
                            }
                        }

                        if (contadorDeCierreFila > contadorDeFilas) {
                            break;
                        }

                    } catch (Exception e) {
                        reportesError.registrarError(new ErrorLexico(" /*..*/",
                                "se espera que se ciere el bloque", contadorDeFilas,
                                contadorDeColumnas));
                    }
                }

                if (letra == '{' || letra == '}') {
                    contadorDeColumnas++;
                    continue;
                }

                // separador de palabras con caracteres especiales como -> y @
                String token = comando.leerSiguienteToken(lineaLeida, contadorDeColumnas);

                if (token.isEmpty()) {
                    // Si no es una palabra, podría ser un operador suelto no reconocido en este
                    // nivel
                    reportesError.registrarError(new ErrorLexico(String.valueOf(letra),
                            "Caracter no reconocido o fuera de contexto", contadorDeFilas, contadorDeColumnas));
                    contadorDeColumnas++;
                    continue;
                }

                // 4. Distribuir el token de acuerdo a su tipo y genera su reconocimiento
                if (esDirectiva(token)) {

                    reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(token, "Reconocido", contadorDeFilas,
                            contadorDeColumnas, "Directivas"));
                    contadorDeColumnas = directivas.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, reporteHTMLTabla, file, true);
                } else if (esConector(token)) {

                    reporteHTMLTabla.registroDeTokens(
                            new RegistroDeTokens(token, "Reconocido", contadorDeFilas, contadorDeColumnas, "Conector"));
                    contadorDeColumnas = conectoresA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, reporteHTMLTabla, file, true);
                } else if (esComandoIA(token)) {

                    reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(token, "Reconocido", contadorDeFilas,
                            contadorDeColumnas, "ComandoIA"));
                    contadorDeColumnas = iaA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, reporteHTMLTabla, file, true);
                } else if (esPalabraReservada(token)) {

                    reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(token, "Reconocido", contadorDeFilas,
                            contadorDeColumnas, "Palabra Reservada"));
                    contadorDeColumnas = reservadasA.analizador(contadorDeColumnas, contadorDeFilas, lineaLeida,
                            reportesError, reporteHTMLTabla, file, true);
                } else {
                    reportesError.registrarError(new ErrorLexico(token,
                            "Instruccion o token no reconocido en este contexto", contadorDeFilas, contadorDeColumnas));
                    contadorDeColumnas += token.length();
                }
            }
            contadorDeFilas++;
        }
        reporteHTMLTabla.generarHTMLDeTokens("ReportesHTML/ReporteTokens" + contadorDeAarchivosAnalizados + ".html");
        reporteHTMLTabla.tablaDeTokensConsola();
        reportesError.generarHTMLDeError("ReportesHTML/ReporteErrores" + contadorDeAarchivosAnalizados + ".html");
    }

    // verificadores de tokens
    private boolean esDirectiva(String token) {
        for (String d : tokens.getDIRECTIVAS()) {
            if (d.equals(token))
                return true;
        }
        return false;
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

}
