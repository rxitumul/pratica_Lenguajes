package com.mycompany.pratica_lenguajes.PromptZal_BacEnd.TokensAnalizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.BibliotecaDeTokens;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.ReporteHTMLTabla;
import com.mycompany.pratica_lenguajes.PromptZal_BacEnd.Errores.ErrorLexico;

public abstract class Analizador {
    protected BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    protected ReporteDeError reportesError;
    protected ReporteHTMLTabla tabla = new ReporteHTMLTabla();
    protected ErrorLexico error;
    protected int contadorDeIndices = 0;
    protected String[] palabrasResultadoLocal;
    private File fileLocal;

    // Listas estáticas compartidas para validación semántica
    protected static List<String> variablesDeclaradas = new ArrayList<>();
    protected static List<String> agentesDeclarados = new ArrayList<>();

    public static void limpiarDeclaraciones() {
        variablesDeclaradas.clear();
        agentesDeclarados.clear();
    }

    public int analizador(int columna, int fila, String linea, ReporteDeError reportesError, File file,
            boolean verdadError)
            throws IOException {
        this.reportesError = reportesError;
        columna = saltarEspacios(linea, columna);
        if (columna >= linea.length()) {
            return columna;
        }
        this.fileLocal = file;

        int inicio = columna;
        String palabra = "";

        // Si empieza con @ o es el conector ->, leemos adecuadamente
        if (linea.charAt(columna) == '@') {
            palabra = leerDirectivaOConector(linea, columna);
        } else if (linea.charAt(columna) == '-' && columna + 1 < linea.length() && linea.charAt(columna + 1) == '>') {
            palabra = "->";
        } else {
            palabra = leerPalabra(linea, columna);
        }

        columna = inicio + palabra.length();

        if (esDirectivaValida(palabra)) {
            // tabla.tablaDeTokens();
            columna = condicion(columna, fila, linea, file);
        } else if (verdadError) {
            error = new ErrorLexico(palabra, "Token no valido o no encontrado", fila, inicio);
            reportesError.registrarError(error);
        } else {
            return -1;
        }
        return columna;
    }

    protected boolean esDirectivaValida(String palabra) {
        contadorDeIndices = 0;
        String[] palabrasDirectivas = token();

        for (String d : palabrasDirectivas) {
            if (d.equals(palabra)) {
                return true;
            }
            contadorDeIndices++;
        }
        return false;
    }

    // Recorre Caracter por Caracter saltando los espacios en blanco
    public int saltarEspacios(String linea, int columna) {
        while (columna < linea.length() && (linea.charAt(columna) == ' ' || linea.charAt(columna) == '\t')) {
            columna++;
        }
        return columna;
    }

    // Lee un identificador, número o comando Caracter por Caracter
    public String leerPalabra(String linea, int columna) {
        int inicio = columna;
        while (columna < linea.length()) {
            char columnasLocales = linea.charAt(columna);
            if (Character.isLetterOrDigit(columnasLocales) || columnasLocales == '_' || columnasLocales == '-') {
                columna++;
            } else {
                break;
            }
        }
        return linea.substring(inicio, columna);
    }

    // Lee una directiva que inicia con @ o el conector -> Caracter por Caracter
    public String leerDirectivaOConector(String linea, int columna) {
        int inicio = columna;
        if (columna < linea.length() && linea.charAt(columna) == '@') {
            columna++;
            while (columna < linea.length()
                    && (Character.isLetterOrDigit(linea.charAt(columna)) || linea.charAt(columna) == '_')) {
                columna++;
            }
        }
        return linea.substring(inicio, columna);
    }

    // Recorre Caracter por Caracter buscando un texto entre comillas dobles
    public String leerTextoEntreComillas(String linea, int columna) {
        if (columna < linea.length() && linea.charAt(columna) == '"') {
            int inicio = columna;
            columna++;
            while (columna < linea.length() && linea.charAt(columna) != '"') {
                columna++;
            }
            if (columna < linea.length()) {
                columna++; // Incluir la comilla de cierre
            }
            return linea.substring(inicio, columna);
        }
        return "";
    }

    // Consume una expresión completa (cadenas, funciones, sumas, etc.) Caracter por
    // Caracter
    public int consumirExpresionCompleta(String linea, int columna) {
        columna = saltarEspacios(linea, columna);
        if (columna >= linea.length()) {
            return columna;
        }

        char c = linea.charAt(columna);

        // 1. Si es un literal entre comillas
        if (c == '"') {
            String texto = leerTextoEntreComillas(linea, columna);
            columna += texto.length();
        }
        // 2. Si es una palabra (identificador o número)
        else if (Character.isLetterOrDigit(c) || c == '_') {
            String palabra = leerPalabra(linea, columna);
            columna += palabra.length();

            // Verificamos si es una función (ej: CARGAR(...))
            int columnaTemp = saltarEspacios(linea, columna);
            if (columnaTemp < linea.length() && linea.charAt(columnaTemp) == '(') {
                columna = columnaTemp + 1; // Consumimos el '('
                int parentesisAbiertos = 1;
                while (columna < linea.length() && parentesisAbiertos > 0) {
                    char ch = linea.charAt(columna);
                    if (ch == '(') {
                        parentesisAbiertos++;
                    } else if (ch == ')') {
                        parentesisAbiertos--;
                    }
                    columna++;
                }
            }
        }
        // 3. Cualquier otro Caracter simple
        else {
            columna++;
        }

        // Verificamos si le sigue otra palabra sin palabras claves (ej: 100 palabras)
        int columnaSig = saltarEspacios(linea, columna);
        if (columnaSig < linea.length()) {
            char sigC = linea.charAt(columnaSig);
            if (Character.isLetterOrDigit(sigC) || sigC == '_') {
                String sigPalabra = leerPalabra(linea, columnaSig);
                if (!esPalabraClave(sigPalabra)) {
                    columna = columnaSig;
                    columna = consumirExpresionCompleta(linea, columna);
                }
            }
        }

        // Verificamos si hay concatenación/suma con '+'
        int columnaMas = saltarEspacios(linea, columna);
        if (columnaMas < linea.length() && linea.charAt(columnaMas) == '+') {
            columna = columnaMas + 1; // Consumimos el '+'
            columna = consumirExpresionCompleta(linea, columna);
        }

        return columna;
    }

    // Verifica si una palabra es parte del vocabulario reservado de ZAL
    protected boolean esPalabraClave(String token) {
        for (String directivaAnalizar : tokens.getDIRECTIVAS()) {
            if (directivaAnalizar.equals(token))

                return true;
        }
        for (String palabraReservadaAnalizar : tokens.getPALABRAS_RESERVADAS()) {
            if (palabraReservadaAnalizar.equals(token))
                return true;
        }
        for (String comandoDeIaAnalizar : tokens.getCOMANDOS_DE_IA()) {
            if (comandoDeIaAnalizar.equals(token))
                return true;
        }
        for (String conectorAnalizar : tokens.getCONECTORES()) {
            if (conectorAnalizar.equals(token))
                return true;
        }
        return false;
    }

    // Busca de forma manual en el archivo la llave de cierre '}'
    protected int verificadorDeCierre(int columna, int fila, char analizarCierreDe) throws IOException {
        int filaArchivo = 0;
        int columnaArchivo = 0;
        columna++;
        try (BufferedReader lector = new BufferedReader(new FileReader(fileLocal))) {
            while (true) {
                if (filaArchivo != fila) {
                    lector.readLine();
                    filaArchivo++;
                } else {
                    break;
                }
            }
            String lineaLeida;
            while ((lineaLeida = lector.readLine()) != null) {
                columnaArchivo = 0;
                while (columnaArchivo < lineaLeida.length()) {
                    if (lineaLeida.charAt(columnaArchivo) == analizarCierreDe) {
                        return columna;
                    }
                    columnaArchivo++;
                }
            }
            reportesError.registrarError(
                    new ErrorLexico("", "Se esperaba " + analizarCierreDe + " después de abrir un bloque",
                            fila, columna));
            return columna;
        }
    }

    protected int lectorDeComillas(int columna, int fila, String linea) {
        int contadorComillas = 0;
        while (columna < linea.length()) {
            if (linea.charAt(columna) == '"') {
                contadorComillas++;
                if (contadorComillas == 2) {
                    columna++;
                    break;
                }
            }
            columna++;
        }
        if (contadorComillas < 2) {
            error = new ErrorLexico("\"...\"", "Falta el cierre de comillas en la directiva", fila, columna);
            reportesError.registrarError(error);
        }
        return columna;
    }

    public void setPalabrasResultado(String[] resultado) {
        palabrasResultadoLocal = resultado;
    }

    protected abstract String[] token();

    protected abstract int condicion(int columna, int fila, String linea, File file) throws IOException;
}
