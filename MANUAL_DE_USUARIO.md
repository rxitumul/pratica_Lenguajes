# Manual de Usuario - Analizador PromptZal

Este manual explica cómo utilizar el software del analizador léxico de **PromptZal** para verificar la validez de los scripts de prompts estructurados.

---

## 1. Introducción a PromptZal

**PromptZal** es un lenguaje diseñado para estructurar prompts complejos destinados a Modelos de Inteligencia Artificial (LLM). Permite definir metadatos iniciales (directivas), declarar agentes con contextos especializados, ejecutar comandos de IA sobre datos específicos y exportar los resultados generados de forma limpia.

---

## 2. Requisitos de Ejecución

Para utilizar esta aplicación, asegúrese de contar con:
*   **Java Development Kit (JDK)** versión 17 o superior instalado y configurado en sus variables de entorno.
*   El archivo compilado del analizador (`.jar` o clases compiladas en la carpeta de ejecución).

---

## 3. Guía de Uso del Software

Al iniciar el programa, se desplegará el menú interactivo:

1.  **Cargar Archivo `.zal`**: Permite escribir la ruta del archivo de texto que contiene el código PromptZal que desea validar (ej: `casos_de_prueba/valido1.zal`).
2.  **Iniciar Análisis**: El analizador procesará el archivo línea por línea en busca de errores.
3.  **Visualizar Resultados**:
    *   Si el archivo es correcto, la aplicación confirmará que el script es totalmente válido.
    *   Si el archivo contiene caracteres o estructuras incorrectas, el programa generará un reporte de errores interactivo en formato HTML llamado **`ReporteErrores.html`** en el directorio raíz.
4.  **Abrir Reporte**: Abra el archivo `ReporteErrores.html` con cualquier navegador web para ver una tabla detallada con:
    *   **Lexema**: El carácter o palabra inválido.
    *   **Fila y Columna**: Posición exacta del error en el archivo de texto.
    *   **Descripción del Error**: Razón por la cual el lexema no es válido.

---

## 4. Ejemplos de Programas Válidos

### Ejemplo 1: Análisis de Ventas
```zal
@modelo "claude-sonnet-4-6"
@rol "analista de datos"
@formato "markdown"

AGENTE analista {
  contexto = "Eres un analista de datos experto"
  variable ventas = CARGAR("ventas.csv")
  PREGUNTAR "Cuales son las 3 tendencias principales?" SOBRE ventas -> tendencias
}

EJECUTAR analista
EXPORTAR tendencias
```

---

## 5. Ejemplos de Programas con Errores Léxicos

### Ejemplo 2: Falta Cierre de Comillas
```zal
@modelo "claude-sonnet-4-6
```
*   **Error reportado**: "Falta el cierre de comillas en la directiva" en la fila 1.

### Ejemplo 3: Carácter Inválido y Variable no Declarada
```zal
@modelo "gpt-4o"
AGENTE analista {
  variable datos = $ventas
}
```
*   **Errores reportados**:
    *   Carácter no reconocido o fuera de contexto (`$`).
    *   La variable `ventas` no ha sido declarada.
