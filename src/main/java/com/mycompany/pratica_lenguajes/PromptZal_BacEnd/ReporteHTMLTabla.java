
package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensRegistrados.RegistroDeTokens;

public class ReporteHTMLTabla {

    private List<RegistroDeTokens> listaDeTokens = new ArrayList<>();

    // Método para agregar errores durante el análisis
    public void registroDeTokens(RegistroDeTokens registroDeTokens) {
        listaDeTokens.add(registroDeTokens);
    }

    // Generar el archivo HTML al terminar la lectura del documento
    public void generarHTMLDeTokens(String rutaDestino) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>Reporte de Tokens</title>\n");
        html.append(
                "<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>Reporte de Tokens</h1>\n");

        if (listaDeTokens.isEmpty()) {
            html.append(
                    "<p style='color: green;'><strong>No se encontraron tokens durante el análisis.</strong></p>\n");
        } else {
            html.append("<table>\n");
            html.append(
                    "<tr><th>#</th><th>Lexema / Caracter</th><th>Token</th><th>Fila</th><th>Columna</th></tr>\n");
            int contador = 1;
            for (RegistroDeTokens token : listaDeTokens) {
                html.append("<tr>")
                        .append("<td>").append(contador++).append("</td>")
                        .append("<td>").append(token.getLexema()).append("</td>")
                        .append("<td>").append(token.getToken()).append("</td>")
                        .append("<td>").append(token.getFila()).append("</td>")
                        .append("<td>").append(token.getColumna()).append("</td>")
                        .append("</tr>\n");
            }
            html.append("</table>\n");
        }

        html.append("</body>\n</html>");

        try (FileWriter writer = new FileWriter(rutaDestino)) {
            writer.write(html.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir el reporte HTML: " + e.getMessage());
        }
    }

    public void tablaDeTokensConsola() {
        System.out.println("Tabla de Tokens");
        System.out.println("Lexema\t\tDescripción\tFila\tColumna");
        for (RegistroDeTokens token : listaDeTokens) {
            System.out.println(token.getLexema() + "\t\t" + token.getDescripcion() + "\t" + token.getFila() + "\t"
                    + token.getColumna());
        }
    }

}
