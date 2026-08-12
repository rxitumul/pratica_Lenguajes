# Manual Técnico - Analizador Léxico PromptZal

Este documento describe la arquitectura técnica, la especificación de tokens y el funcionamiento del analizador léxico implementado manualmente en Java para el lenguaje **PromptZal**.

---

## 1. Especificación de los Tokens del Lenguaje

El analizador clasifica las secuencias de caracteres (lexemas) en las siguientes categorías de tokens:

### A. Directivas (Empiezan con `@`)
Describen metadatos del prompt a generar al inicio del archivo:
*   `@modelo`: Indica el modelo de IA destino (ej: `"claude-sonnet-4-6"`).
*   `@rol`: Define el rol que asumirá la IA (ej: `"analista de datos"`).
*   `@formato`: Indica el formato de salida deseado (ej: `"markdown"`).

### B. Palabras Reservadas de Estructura
Definen la estructura de agentes, variables y flujos de ejecución:
*   `AGENTE`: Define el inicio del bloque de un agente.
*   `contexto`: Clave para asignar la descripción del agente.
*   `variable`: Permite declarar variables internas de un agente.
*   `EJECUTAR`: Indica la ejecución del bloque de un agente.
*   `EXPORTAR`: Exporta una o más variables declaradas al final del script.

### C. Comandos de IA
Operaciones específicas que interactúan con los modelos de lenguaje:
*   `PREGUNTAR`: Envía una consulta o pregunta.
*   `GENERAR`: Genera código u otro contenido a partir de instrucciones.
*   `RESUMIR`: Sintetiza información a una longitud específica.
*   `ANALIZAR`: Analiza datos bajo un contexto determinado.
*   `TRADUCIR`: Traduce texto de un idioma a otro.
*   `CLASIFICAR`: Clasifica contenido en categorías.
*   `EXTRAER`: Extrae entidades o fragmentos de datos.
*   `CARGAR`: Comando especial que lee el contenido de un archivo (ej: `CARGAR("ventas.csv")`).

### D. Conectores
Enlazan parámetros con los comandos de IA:
*   `SOBRE`: Vincula una variable de datos a un comando.
*   `DESDE`: Especifica la fuente de generación.
*   `EN`: Especifica el límite o restricción.
*   `COMO`: Especifica el formato o estilo de traducción/análisis.
*   `->`: Asigna el resultado del comando de IA a una variable destino.

### E. Operadores y Símbolos Estructurales
*   `=`: Operador de asignación.
*   `+`: Operador de concatenación o suma de expresiones.
*   `{` y `}`: Delimitadores del bloque de código del agente.
*   `(` y `)`: Delimitadores de argumentos de funciones.
*   `,`: Separador de variables en la exportación.
*   `"`: Delimitador de literales de cadena (Strings).

---

## 2. Recorrido de la Entrada (Carácter por Carácter)

El análisis léxico se realiza de forma **100% manual** sin el uso de librerías de expresiones regulares (`java.util.regex`) ni generadores de analizadores. El recorrido se realiza utilizando un puntero de columna (`contadorDeColumnas`) en cada línea del archivo:

1.  **Lectura del Archivo**: Se procesa línea por línea usando `BufferedReader.readLine()`.
2.  **Ignorar Espacios**: El método `saltarEspacios(linea, col)` avanza el puntero de columna saltando caracteres de espacio en blanco `' '` y tabulación `'\t'`.
3.  **Descarte de Comentarios**:
    *   Si detecta `//`, rompe el bucle de la línea actual ignorando el resto.
    *   Si detecta `/*`, busca secuencialmente la secuencia `*/` para reanudar el análisis.
4.  **Detección de Directivas y Símbolos**:
    *   Si el carácter en la columna actual es `@`, llama a `leerDirectivaOConector()` que lee caracteres alfanuméricos hasta encontrar un delimitador.
    *   Si detecta `-` seguido de `>`, lo extrae como el token de asignación `->`.
    *   Cualquier palabra regular es leída carácter por carácter por `leerPalabra()` hasta chocar con un espacio, operador o delimitador.
5.  **Análisis de Expresiones**: El método `consumirExpresionCompleta()` evalúa y avanza el cursor a través de literales de texto entre comillas (usando `leerTextoEntreComillas()`), llamadas a funciones con paréntesis anidados y concatenaciones con `+`, asegurando que no se reporten palabras normales internas de los prompts como errores léxicos.

---

## 3. Gestión y Manejo de Errores

El analizador está diseñado bajo la premisa de **recuperación de errores**: si encuentra un token no válido, no detiene la ejecución, sino que:

1.  Crea un objeto `ErrorLexico` con la información del lexema problemático, la descripción del fallo, la fila y la columna exactas.
2.  Registra el error en la instancia global de `ReporteDeError`.
3.  Avanza el puntero de columna saltando el lexema erróneo, y continúa analizando el resto del archivo.
4.  Al finalizar el análisis, el método `generarHTMLDeError()` crea un archivo dinámico `ReporteErrores.html` con una tabla estructurada y estilizada que muestra todos los errores encontrados detalladamente.

---

## 4. Estructura de Clases

*   **`AnailizadorDeTexto.java`**: El motor del análisis. Contiene el bucle principal de lectura, descarta comentarios y distribuye los tokens a los analizadores específicos.
*   **`Analizador.java`**: Clase abstracta base que implementa las funciones comunes de lectura carácter por carácter (salto de espacios, comillas, expresiones, llaves de cierre).
*   **`DirectivasAnalaizador.java`**: Analiza las directivas `@modelo`, `@rol` y `@formato`, asegurando que vayan acompañadas de sus respectivos literales.
*   **`PalabrasReservadasDeEstructuraAnalizador.java`**: Valida las palabras reservadas de estructura (`AGENTE`, `contexto`, `variable`, `EJECUTAR`, `EXPORTAR`). Lleva el control semántico de los agentes y variables declaradas.
*   **`ComandosDeIaAnalizador.java`**: Analiza y consume las instrucciones de comandos de IA.
*   **`ConectoresAnalizador.java`**: Valida y procesa los conectores y las asignaciones de variables (`->`).
*   **`BibliotecaDeTokens.java`**: Define el vocabulario y arreglos constantes de palabras clave del lenguaje.
