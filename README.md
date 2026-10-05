# CppEs — Analizador sintáctico y gestión de errores

**Práctica:** Análisis sintáctico y gestión de errores en la estructura de un programa
**Materia:** Traductores de Lenguaje · **Profesor:** José Navarro Ríos

**Integrantes** (orden alfabético):

1. Larios Hernandez Carlos Alberto
2. Macias Renteria Dante Yael
3. Salcedo Ramos Luis Gael

**Lenguaje asignado:** **C++Es** — C++ con las palabras reservadas traducidas al español.

---

## 1. Reglas sintácticas implementadas

```
archivo          -> importacion* paquete? clase_principal
importacion      -> '#incluir' ruta
paquete          -> 'usando' 'espacio_nombres' ubicacion ';'
ubicacion        -> identificador ( '::' identificador )*
clase_principal  -> 'clase' identificador cuerpo_clase ';'
cuerpo_clase     -> '{' '}'
```

### Correspondencia con el enunciado, que usa Java como lenguaje base

| Java | C++Es | Por qué |
|---|---|---|
| `package ubicacion ;` | `usando espacio_nombres ubicacion ;` | C++ no tiene `package`; el espacio de nombres es su equivalente conceptual. |
| `import ubicacion ;` | `#incluir <ruta>` | La inclusión de cabeceras es el mecanismo de C++ para traer código externo. No lleva punto y coma. |
| `a.b.c` | `a::b::c` | El operador de resolución de ámbito en C++ es `::`. |
| `class Id { }` | `clase Id { } ;` | En C++ la declaración de una clase cierra con punto y coma. |

**Diferencia de orden:** en Java el `package` va antes de los `import`. En C++ las
inclusiones van primero, porque `usando espacio_nombres` necesita que alguna
cabecera ya haya declarado ese espacio. Por eso la producción es
`importacion* paquete?` y no al revés.

### Validaciones cubiertas

| Validación del enunciado | Cómo se cumple |
|---|---|
| El archivo puede iniciar con paquete, importación o clase | `lista_importaciones` y `paquete_opcional` admiten la producción vacía |
| Las importaciones deben aparecer antes de la clase | El orden está fijado en la producción `archivo` |
| Debe existir exactamente una clase principal | `archivo` referencia `clase_principal` una sola vez y después exige EOF |
| La clase debe tener nombre válido | Exige el token `IDENTIFICADOR`; hay producción de error para cuando falta |
| Las llaves deben estar correctamente definidas | `cuerpo_clase -> '{' '}'`, con producción de error para la llave sin cerrar |

---

## 2. Tipos de errores detectados

### 2.1 Errores recuperables — `report_error`

El analizador reporta el problema, descarta la parte inválida y continúa.

| Estructura | Error | Archivo de prueba |
|---|---|---|
| Importación | Punto y coma sobrante (`#incluir <x>;`) | `error_importacion.txt` |
| Importación | Ruta ausente o mal delimitada (`#incluir <x`) | `error_importacion.txt` |
| Paquete | Falta el punto y coma final | `error_paquete.txt` |
| Paquete | Ubicación mal formada | producción `USANDO ESPACIO_NOMBRES error PUNTO_COMA` |
| Clase | Falta el nombre de la clase | `error_clase.txt` |
| Clase | Falta la llave de cierre | `error_llaves.txt` |
| Clase | Falta el punto y coma de cierre | producción `CLASE IDENTIFICADOR cuerpo_clase` |

Formato del mensaje:

```
Error sintactico en linea 8, columna 20: se encontro ';'
     Token       : PUNTO_COMA
     Descripcion : la directiva #incluir no lleva punto y coma en C++Es
     >> Recuperacion: se descarto el punto y coma sobrante y el analisis continua.
```

### 2.2 Error fatal — `report_fatal_error`

Cuando la estructura general es inválida y no existe ninguna producción que
permita resincronizar, el análisis se detiene.

```
Error sintactico en linea 6, columna 1: se encontro 'publico'
     Token       : PUBLICO
     Descripcion : este token no puede aparecer en este punto de la estructura del archivo
     >> No existe ninguna regla que permita resincronizar el analisis.
Error fatal en linea 6, columna 1: se encontro 'publico'
     Token       : PUBLICO
     Descripcion : la estructura general del programa es invalida; el analisis termina aqui
```

Archivo de prueba: `error_fatal.txt`.

**Por qué aparecen dos líneas:** CUP llama a `report_error` en cuanto detecta un
token que no encaja, *antes* de saber si podrá recuperarse. Solo después, si la
recuperación falla, deriva en `report_fatal_error`. La salida refleja esa
secuencia: primero se detecta un error sintáctico y luego se escala a fatal. El
contador de recuperables se decrementa al reclasificarlo, de modo que las cifras
finales son correctas.

---

## 3. Mecanismos de recuperación

Se emplean dos técnicas complementarias.

### 3.1 Símbolo especial `error`

Se coloca dentro de la producción para marcar dónde puede resincronizar el
analizador:

```
importacion ::= INCLUIR error ;
cuerpo_clase ::= LLAVE_IZQ error ;
paquete ::= USANDO ESPACIO_NOMBRES error PUNTO_COMA ;
clase_principal ::= CLASE error PUNTO_COMA ;
```

CUP descarta tokens hasta poder continuar el análisis, y llama a `report_error`
automáticamente.

### 3.2 Producciones alternativas que aceptan la forma incorrecta

Para errores muy concretos, aceptar deliberadamente la forma errónea permite dar
un diagnóstico mucho más preciso que el genérico de `error`:

```
importacion ::= INCLUIR RUTA_INCLUSION PUNTO_COMA ;      // ';' sobrante
paquete     ::= USANDO ESPACIO_NOMBRES ubicacion ;       // falta ';'
clase_principal ::= CLASE cuerpo_clase PUNTO_COMA ;      // falta el nombre
```

La acción de estas producciones llama a `reportarError(...)`, que construye un
`Symbol` y lo pasa por `report_error`, de modo que **todos** los errores salgan
por el mismo camino y con el mismo formato.

### 3.3 Por qué el error fatal es posible

**No hay ninguna producción con `error` en la regla inicial `archivo`.** Si la
hubiera, cualquier entrada sería recuperable y nunca se produciría un error
fatal. Al no existir, CUP no encuentra ningún estado donde desplazar `error`,
llama a `unrecovered_syntax_error` y de ahí a `report_fatal_error`.

---

## 4. Uso de `Symbol` para línea y columna

El analizador léxico construye cada token así:

```java
new Symbol(tipo, yyline + 1, yycolumn + 1, yytext())
```

Es decir, aprovecha los campos posicionales de `Symbol`:

| Campo de `Symbol` | Qué guardamos |
|---|---|
| `sym` | Identificador numérico del token |
| `left` | **Número de línea** |
| `right` | **Número de columna** |
| `value` | Lexema (el texto reconocido) |

En las acciones de la gramática, CUP genera automáticamente las variables
`xleft` y `xright` para cada símbolo etiquetado, y de ahí salen la línea y la
columna de los mensajes de retroalimentación positiva.

---

## 5. Retroalimentación positiva

Cada producción imprime un mensaje al reducirse:

```
OK   Importacion reconocida correctamente: #incluir <flujo_es>   [linea 7, columna 1]
OK   Ubicacion reconocida correctamente: estd::sistema   [linea 11, columna 24]
OK   Paquete reconocido correctamente: espacio de nombres estd::sistema   [linea 11, columna 1]
OK   Estructura de llaves valida   [linea 13, columna 18]
OK   Clase principal reconocida correctamente: Estudiante   [linea 13, columna 1]
OK   Analisis sintactico finalizado correctamente
```

---

## 6. Archivos de prueba

| Archivo | Tipo | Reglas | Recuperables | Fatales |
|---|---|---|---|---|
| `programa_valido.txt` | Válido | 8 | 0 | 0 |
| `programa_con_errores.txt` | Inválido | 6 | 4 | 0 |
| `error_paquete.txt` | Inválido | 6 | 1 | 0 |
| `error_importacion.txt` | Inválido | 4 | 2 | 0 |
| `error_clase.txt` | Inválido | 2 | 1 | 0 |
| `error_llaves.txt` | Inválido | 2 | 1 | 0 |
| `error_fatal.txt` | Inválido | 0 | 0 | 1 |

El archivo con errores **no** incluye el caso fatal a propósito: como el fatal
detiene el análisis, ocultaría los errores posteriores y no se podría demostrar
la continuación. Se prueba por separado.

---

## 7. Compilar y ejecutar

### Requisitos

- NetBeans (proyecto Java con Ant) o JDK 11+
- `java-cup-11b.jar` y `java-cup-11b-runtime.jar`
- `jflex-full-1.9.1.jar` (descarga aparte desde <https://jflex.de/download.html>)

Los tres JAR deben estar agregados en **Libraries** del proyecto.

### Orden de ejecución

El orden **no** es negociable: cada paso produce lo que necesita el siguiente.

| # | Ejecutar | Genera | Indicador de éxito |
|---|---|---|---|
| 1 | `GeneradorCupSintantico.java` | `sym.java`, `ParserCppEs.java` | `Code written to "ParserCppEs.java", and "sym.java"` |
| 2 | `GeneradorJFlexLexico.java` | `CppLexer.java` | `Writing code to "...CppLexer.java"` |
| 3 | **Clean and Build** (`Shift`+`F11`) | los `.class` | `BUILD SUCCESSFUL` |
| 4 | `AnalizadorSintactico.java` | la salida del análisis | — |

En NetBeans, para ejecutar una clase suelta: clic derecho sobre el archivo →
**Run File** (`Shift`+`F6`).

**Repetir los pasos 1 a 3 cada vez que se modifique el `.cup` o el `.jflex`.**

> **Aviso:** los pasos 1 y 2 reportan `BUILD SUCCESSFUL` incluso cuando CUP o
> JFlex fallan, porque las clases generadoras capturan la excepción
> internamente. El único indicador confiable es la línea de la columna
> "Indicador de éxito" de la tabla.

### Elegir el archivo a analizar

En `AnalizadorSintactico.java`:

```java
private static final String ARCHIVO_POR_OMISION =
        "src/act1_analisis_lexico/programa_valido.txt";
```

La ruta es relativa a la raíz del proyecto, así que debe empezar con `src/`.
También puede pasarse como argumento en *Project Properties → Run → Arguments*.

### Ver también los tokens

```java
private static final boolean MOSTRAR_TOKENS = false;
```

Ponlo en `true` para intercalar la salida del analizador léxico.

---

## 8. Estructura del repositorio

```
CppEs/
├── README.md
└── src/act1_analisis_lexico/
    ├── cppes_sintactico.cup        Gramática, report_error, report_fatal_error
    ├── cppes_lexico.jflex          Analizador léxico
    ├── AnalizadorSintactico.java   Clase principal
    ├── AnalizadorLexico.java       Clase principal de la práctica anterior
    ├── GeneradorCupSintantico.java Ejecuta CUP
    ├── GeneradorJFlexLexico.java   Ejecuta JFlex
    ├── ManejadorErrores.java       Errores léxicos
    ├── TablaTokens.java            Nombre legible de cada token
    ├── programa_valido.txt         Archivo 1
    ├── programa_con_errores.txt    Archivo 2
    ├── error_paquete.txt
    ├── error_importacion.txt
    ├── error_clase.txt
    ├── error_llaves.txt
    └── error_fatal.txt
```

### Archivos generados

`sym.java` y `ParserCppEs.java` los produce CUP a partir de
`cppes_sintactico.cup`; `CppLexer.java` lo produce JFlex a partir de
`cppes_lexico.jflex`.

Por convención el código generado no se versiona, pero aquí **sí se suben a
propósito**: así cualquiera que clone el repositorio puede compilar y ejecutar
de inmediato, sin instalar JFlex ni correr los generadores. El código fuente
real de esas tres clases son el `.cup` y el `.jflex`, que también están en el
repositorio.

Lo que **no** se versiona está en `.gitignore`: `build/`, `dist/`,
`nbproject/private/`, los `.class` y los respaldos `*.java~` que deja JFlex.

---

## 9. Verificación de la gramática

```
  0 errors and 103 warnings
  115 terminals, 8 non-terminals, and 20 productions declared,
  producing 32 unique parse states.
  0 conflicts detected (0 expected).
  0 productions never reduced.
```

- **0 conflictos:** no hay ambigüedad; con un solo token de anticipación el
  analizador siempre sabe qué hacer, incluso con las producciones de `error`.
- **0 producciones sin reducir:** toda regla escrita es alcanzable.
- Los 103 *warnings* son terminales declarados pero no usados: el `.cup` declara
  los 115 tokens del lenguaje completo, pero esta práctica solo usa los de la
  estructura del archivo.
