package com.mycompany.pratica_lenguajes.PromptZal_BacEnd;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public class ReporteDeError {

    private List<ErrorLexico> listaDeErrores = new ArrayList<>();
    // Método para agregar errores durante el análisis
    public void registrarError(ErrorLexico errorLexico) {
        listaDeErrores.add(errorLexico);
    }

    // Generar el archivo HTML al terminar la lectura del documento
    public void generarHTMLDeError(String rutaDestino) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>Reporte de Errores Léxicos</title>\n");
        html.append(
                "<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>Reporte de Errores Léxicos</h1>\n");

        if (listaDeErrores.isEmpty()) {
            html.append(
                    "<p style='color: green;'><strong>No se encontraron errores léxicos durante el análisis.</strong></p>\n");
        } else {
            html.append("<table>\n");
            html.append(
                    "<tr><th>#</th><th>Lexema / Carácter</th><th>Descripción del Error</th><th>Fila</th><th>Columna</th></tr>\n");
            int contador = 1;
            for (ErrorLexico err : listaDeErrores) {
                html.append("<tr>")
                        .append("<td>").append(contador++).append("</td>")
                        .append("<td>").append(err.getLexema()).append("</td>")
                        .append("<td>").append(err.getDescripcion()).append("</td>")
                        .append("<td>").append(err.getFila()).append("</td>")
                        .append("<td>").append(err.getColumna()).append("</td>")
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

}
