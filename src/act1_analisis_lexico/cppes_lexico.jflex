/*
  NOMBRES COMPLETOS DEL EQUIPO:
  1) Larios Hernandez Carlos Alberto
  2) Macias Renteria Dante Yael
  3) Salcedo Ramos Luis Gael

  Materia:   Traductores de Lenguaje
  Profesor:  Jose Navarro Rios
  Practica:  Analizador Lexico
  Lenguaje:  C++Es  (C++ con palabras reservadas en espanol)
*/

package act1_analisis_lexico;

import java_cup.runtime.Symbol;

%%

%class CppLexer
%public
%unicode
%cup
%line
%column


%xstate INCLUSION

%{
  private int totalTokens = 0;
  private final ManejadorErrores errores = new ManejadorErrores();

  private boolean mostrarTokens = true;
  public void setMostrarTokens(boolean valor) { mostrarTokens = valor; }

  public int getTotalTokens() { return totalTokens; }
  public int getTotalErrores() { return errores.total(); }
  public ManejadorErrores getManejadorErrores() { return errores; }

  private Symbol token(int type) {
    totalTokens++;
    if (mostrarTokens) {
      System.out.println("[" + (yyline + 1) + "-" + (yycolumn + 1) + "] "
                         + TablaTokens.nombre(type) + ": " + yytext());
    }
    return new Symbol(type, yyline + 1, yycolumn + 1, yytext());
  }

  private Symbol token(int type, Object value) {
    totalTokens++;
    if (mostrarTokens) {
      System.out.println("[" + (yyline + 1) + "-" + (yycolumn + 1) + "] "
                         + TablaTokens.nombre(type) + ": " + value);
    }
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }

  /* Reporta un error lexico y CONTINUA el analisis (recuperacion de errores). */
  private void error(ManejadorErrores.Tipo tipo, String descripcion, String sugerencia) {
    errores.reportar(tipo, yyline + 1, yycolumn + 1, yytext(), descripcion, sugerencia);
  }
%}

/* ---------- Espacios en blanco y fin de linea ---------- */
FinDeLinea      = \r|\n|\r\n
CaracterEntrada = [^\r\n]
Espacios        = {FinDeLinea} | [ \t\f]

/* ---------- Comentarios ---------- */
Comentario           = {ComentarioBloque} | {ComentarioLinea} | {ComentarioDoc}
ComentarioBloque     = "/*" [^*] ~"*/" | "/*" "*"+ "/"
ComentarioLinea      = "//" {CaracterEntrada}* {FinDeLinea}?
ComentarioDoc        = "/**" {ContenidoDoc} "*"+ "/"
ContenidoDoc         = ( [^*] | \*+ [^/*] )*
ComentarioNoCerrado  = "/*" !([^]* "*/" [^]*)

/* ---------- Identificadores ----------
   [:letter:] admite letras Unicode (incluye ñ y vocales acentuadas),
   lo cual es deseable en un lenguaje en espanol.                         */
Letra          = [:letter:] | "_"
Digito         = [0-9]
Identificador  = {Letra} ({Letra} | {Digito})*

/* ---------- Identificadores mal formados ---------- */
IdentIniciaDigito = {Digito}+ {Letra} ({Letra} | {Digito})*
IdentConSimbolo   = {Letra} ({Letra} | {Digito})* "$" ({Letra} | {Digito} | "$")*
                  | "$" {Letra} ({Letra} | {Digito} | "$")*

/* ---------- Literales numericos ---------- */
Digitos        = {Digito}+
/* Sufijos enteros: se listan del mas largo al mas corto (convencion segura) */
SufijoEntero   = ([uU][lL][lL]) | ([lL][lL][uU]) | ([uU][lL]) | ([lL][uU]) | ([lL][lL]) | [uU] | [lL]
SufijoFlotante = [fFlL]
Exponente      = [eE] [+-]? {Digitos}

LitEnteroDec   = ( 0 | [1-9] {Digito}* ) {SufijoEntero}?
LitEnteroHex   = 0 [xX] [0-9a-fA-F]+ {SufijoEntero}?
LitEnteroBin   = 0 [bB] [01]+ {SufijoEntero}?
LitEnteroOct   = 0 [0-7]+ {SufijoEntero}?

LitFlotante    = {Digitos} "." {Digitos}? {Exponente}? {SufijoFlotante}?
               | "." {Digitos} {Exponente}? {SufijoFlotante}?
               | {Digitos} {Exponente} {SufijoFlotante}?

/* ---------- Cadenas y caracteres ---------- */
Escape         = \\ [abfnrtv0\\\'\"?] | \\ x [0-9a-fA-F]+ | \\ [0-7] [0-7]? [0-7]?
EscapeInvalido = \\ [^abfnrtvx0-7\\\'\"?\r\n]

LitCadena      = \" ( [^\"\\\r\n] | {Escape} )* \"
CadenaAbierta  = \" ( [^\"\\\r\n] | {Escape} )*
CadenaEscapeMal = \" ( [^\"\\\r\n] | {Escape} | {EscapeInvalido} )* \"

LitCaracter    = \' ( [^\'\\\r\n] | {Escape} ) \'
CaracterAbierto = \' ( [^\'\\\r\n] | {Escape} )?
CaracterMultiple = \' ( [^\'\\\r\n] | {Escape} ) ( [^\'\\\r\n] | {Escape} )+ \'
CaracterEscapeMal = \' {EscapeInvalido} \'

/* ---------- Directivas del preprocesador ---------- */
RutaSistema    = "<" [^<>\r\n]* ">"
RutaLocal      = \" [^\"\r\n]* \"
MarcaIncluir   = "#" [ \t]* "incluir"
Definir        = "#" [ \t]* "definir"
OtraDirectiva  = "#" [ \t]* {Identificador}

%%

/* ------------------------------------------------------------------
   REGLAS LEXICAS
   ------------------------------------------------------------------ */

/* ===== 1. Comentarios y espacios (se ignoran) ===== */
{Comentario}          { /* Ignorar */ }
{Espacios}            { /* Ignorar */ }

/* ===== 2. Directivas del preprocesador ===== */
{MarcaIncluir}        { yybegin(INCLUSION); return token(sym.INCLUIR); }
{Definir}             { return token(sym.DIRECTIVA_DEFINIR); }
{OtraDirectiva}       { return token(sym.DIRECTIVA);         }

<INCLUSION> {
  [ \t]+                        { /* Ignorar los espacios previos a la ruta */ }

  {RutaSistema} | {RutaLocal}    { yybegin(YYINITIAL);
                                   return token(sym.RUTA_INCLUSION); }

  {FinDeLinea}                   { yybegin(YYINITIAL);
                                   error(ManejadorErrores.Tipo.DIRECTIVA_MAL_FORMADA,
                                         "falta la ruta despues de #incluir",
                                         "escriba #incluir <archivo> o #incluir \"archivo\""); }

  [^]                            { yybegin(YYINITIAL);
                                   error(ManejadorErrores.Tipo.DIRECTIVA_MAL_FORMADA,
                                         "la ruta del #incluir esta mal delimitada",
                                         "use <angulos> para librerias del sistema o \"comillas\" para archivos propios"); }
}

/* ===== 3. Palabras reservadas ===== */

/* --- Tipos de dato --- */
"entero"              { return token(sym.ENTERO);      }
"flotante"            { return token(sym.FLOTANTE);    }
"doble"               { return token(sym.DOBLE);       }
"caracter"            { return token(sym.CARACTER);    }
"booleano"            { return token(sym.BOOLEANO);    }
"vacio"               { return token(sym.VACIO);       }
"cadena"              { return token(sym.CADENA);      }
"largo"               { return token(sym.LARGO);       }
"corto"               { return token(sym.CORTO);       }
"sin_signo"           { return token(sym.SIN_SIGNO);   }
"con_signo"           { return token(sym.CON_SIGNO);   }
"auto"                { return token(sym.AUTO);        }
"constante"           { return token(sym.CONSTANTE);   }
"estatico"            { return token(sym.ESTATICO);    }
"tipo_def"            { return token(sym.TIPO_DEF);    }
"tamanio_de"          { return token(sym.TAMANIO_DE);  }

/* --- Estructuras de control --- */
"si"                  { return token(sym.SI);          }
"sino"                { return token(sym.SINO);        }
"mientras"            { return token(sym.MIENTRAS);    }
"para"                { return token(sym.PARA);        }
"hacer"               { return token(sym.HACER);       }
"segun"               { return token(sym.SEGUN);       }
"caso"                { return token(sym.CASO);        }
"defecto"             { return token(sym.DEFECTO);     }
"romper"              { return token(sym.ROMPER);      }
"continuar"           { return token(sym.CONTINUAR);   }
"retornar"            { return token(sym.RETORNAR);    }
"ir_a"                { return token(sym.IR_A);        }

/* --- Programacion orientada a objetos --- */
"clase"               { return token(sym.CLASE);       }
"estructura"          { return token(sym.ESTRUCTURA);  }
"union"               { return token(sym.UNION);       }
"enumeracion"         { return token(sym.ENUMERACION); }
"publico"             { return token(sym.PUBLICO);     }
"privado"             { return token(sym.PRIVADO);     }
"protegido"           { return token(sym.PROTEGIDO);   }
"virtual"             { return token(sym.VIRTUAL);     }
"amigo"               { return token(sym.AMIGO);       }
"este"                { return token(sym.ESTE);        }
"nuevo"               { return token(sym.NUEVO);       }
"borrar"              { return token(sym.BORRAR);      }
"plantilla"           { return token(sym.PLANTILLA);   }
"operador"            { return token(sym.OPERADOR);    }
"en_linea"            { return token(sym.EN_LINEA);    }
"espacio_nombres"     { return token(sym.ESPACIO_NOMBRES); }
"usando"              { return token(sym.USANDO);      }

/* --- Manejo de excepciones --- */
"intentar"            { return token(sym.INTENTAR);    }
"capturar"            { return token(sym.CAPTURAR);    }
"lanzar"              { return token(sym.LANZAR);      }

/* --- Literales reservados --- */
"verdadero"           { return token(sym.VERDADERO);   }
"falso"               { return token(sym.FALSO);       }
"nulo"                { return token(sym.NULO);        }

/* --- Entrada / salida y punto de entrada --- */
"imprimir"            { return token(sym.IMPRIMIR);    }
"leer"                { return token(sym.LEER);        }
"fin_linea"           { return token(sym.FIN_LINEA);   }
"principal"           { return token(sym.PRINCIPAL);   }

/* ===== 4. Literales numericos ===== */
{LitEnteroHex}        { return token(sym.LIT_ENTERO_HEX); }
{LitEnteroBin}        { return token(sym.LIT_ENTERO_BIN); }
{LitEnteroOct}        { return token(sym.LIT_ENTERO_OCT); }
{LitFlotante}         { return token(sym.LIT_FLOTANTE);   }
{LitEnteroDec}        { return token(sym.LIT_ENTERO);     }

/* ===== 5. Cadenas y caracteres ===== */
{LitCadena}           { return token(sym.LIT_CADENA);   }
{LitCaracter}         { return token(sym.LIT_CARACTER); }

/* ===== 6. Identificadores ===== */
{Identificador}       { return token(sym.IDENTIFICADOR); }

/* ===== 7. Operadores y simbolos ===== */

/* Asignaciones compuestas */
"<<="                 { return token(sym.DESP_IZQ_ASIGNA); }
">>="                 { return token(sym.DESP_DER_ASIGNA); }
"+="                  { return token(sym.MAS_ASIGNA);      }
"-="                  { return token(sym.MENOS_ASIGNA);    }
"*="                  { return token(sym.POR_ASIGNA);      }
"/="                  { return token(sym.ENTRE_ASIGNA);    }
"%="                  { return token(sym.MODULO_ASIGNA);   }
"&="                  { return token(sym.AND_ASIGNA);      }
"|="                  { return token(sym.OR_ASIGNA);       }
"^="                  { return token(sym.XOR_ASIGNA);      }

/* Relacionales */
"=="                  { return token(sym.IGUAL);        }
"!="                  { return token(sym.DIFERENTE);    }
"<="                  { return token(sym.MENOR_IGUAL);  }
">="                  { return token(sym.MAYOR_IGUAL);  }

/* Logicos */
"&&"                  { return token(sym.AND_LOGICO);   }
"||"                  { return token(sym.OR_LOGICO);    }
"!"                   { return token(sym.NOT_LOGICO);   }

/* Desplazamiento de bits */
"<<"                  { return token(sym.DESP_IZQ);     }
">>"                  { return token(sym.DESP_DER);     }

/* Incremento / decremento y acceso por puntero */
"++"                  { return token(sym.INCREMENTO);   }
"--"                  { return token(sym.DECREMENTO);   }
"->"                  { return token(sym.FLECHA);       }
"::"                  { return token(sym.RESOLUCION);   }

/* Aritmeticos */
"+"                   { return token(sym.MAS);          }
"-"                   { return token(sym.MENOS);        }
"*"                   { return token(sym.POR);          }
"/"                   { return token(sym.ENTRE);        }
"%"                   { return token(sym.MODULO);       }

/* Bit a bit */
"&"                   { return token(sym.AND_BIT);      }
"|"                   { return token(sym.OR_BIT);       }
"^"                   { return token(sym.XOR_BIT);      }
"~"                   { return token(sym.NOT_BIT);      }

/* Comparacion simple y asignacion */
"<"                   { return token(sym.MENOR);        }
">"                   { return token(sym.MAYOR);        }
"="                   { return token(sym.ASIGNA);       }

/* Agrupacion */
"("                   { return token(sym.PAR_IZQ);      }
")"                   { return token(sym.PAR_DER);      }
"["                   { return token(sym.COR_IZQ);      }
"]"                   { return token(sym.COR_DER);      }
"{"                   { return token(sym.LLAVE_IZQ);    }
"}"                   { return token(sym.LLAVE_DER);    }

/* Puntuacion */
","                   { return token(sym.COMA);         }
";"                   { return token(sym.PUNTO_COMA);   }
"."                   { return token(sym.PUNTO);        }
":"                   { return token(sym.DOS_PUNTOS);   }
"?"                   { return token(sym.INTERROGACION);}

/* ===== 8. Reglas de error ===== */

/* ---------- 8.1 Literales numericos incorrectos ---------- */

0 [xX] ({Letra} | {Digito})*
    { error(ManejadorErrores.Tipo.LITERAL_NUMERICO,
            "literal hexadecimal mal formado",
            "despues de 0x debe haber al menos un digito 0-9, a-f o A-F"); }

0 [bB] ({Letra} | {Digito})*
    { error(ManejadorErrores.Tipo.LITERAL_NUMERICO,
            "literal binario mal formado",
            "despues de 0b solo se admiten los digitos 0 y 1"); }

0 [0-9] ({Letra} | {Digito})*
    { error(ManejadorErrores.Tipo.LITERAL_NUMERICO,
            "literal octal con digito invalido",
            "un 0 inicial indica base octal: solo se admiten digitos 0-7"); }

{LitFlotante} ({Letra} | {Digito})+
    { error(ManejadorErrores.Tipo.LITERAL_NUMERICO,
            "literal flotante seguido de caracteres invalidos",
            "los unicos sufijos validos para flotantes son f, F, l y L"); }

{Digitos} "." {Digitos}? ("." {Digitos}?)+
    { error(ManejadorErrores.Tipo.LITERAL_NUMERICO,
            "literal flotante con mas de un punto decimal",
            "un literal flotante admite un solo punto decimal"); }

/* ---------- 8.2 Identificadores mal formados ---------- */

{IdentIniciaDigito}
    { error(ManejadorErrores.Tipo.IDENTIFICADOR_MAL_FORMADO,
            "un identificador no puede comenzar con un digito",
            "inicie el identificador con una letra o con guion bajo"); }

{Digitos} ({Letra} | {Digito})*
    { error(ManejadorErrores.Tipo.IDENTIFICADOR_MAL_FORMADO,
            "secuencia que no es ni numero valido ni identificador valido",
            "separe el numero del identificador, o inicie el nombre con una letra"); }

{IdentConSimbolo}
    { error(ManejadorErrores.Tipo.IDENTIFICADOR_MAL_FORMADO,
            "el simbolo $ no esta permitido en identificadores de C++Es",
            "use solo letras, digitos y guion bajo"); }

/* ---------- 8.3 Cadenas mal formadas ---------- */

{CadenaEscapeMal}
    { error(ManejadorErrores.Tipo.CADENA_MAL_FORMADA,
            "la cadena contiene una secuencia de escape invalida",
            "escapes validos: \\n \\t \\r \\\\ \\\" \\' \\0 \\a \\b \\f \\v \\xHH \\OOO"); }

{CadenaAbierta}
    { error(ManejadorErrores.Tipo.CADENA_MAL_FORMADA,
            "cadena sin comilla doble de cierre antes del fin de linea",
            "cierre la cadena con \" o use \\ al final para continuarla"); }

/* ---------- 8.4 Literales de caracter mal formados ---------- */

{CaracterMultiple}
    { error(ManejadorErrores.Tipo.CARACTER_MAL_FORMADO,
            "un literal de caracter debe contener exactamente un caracter",
            "si queria una cadena, use comillas dobles en lugar de simples"); }

{CaracterEscapeMal}
    { error(ManejadorErrores.Tipo.CARACTER_MAL_FORMADO,
            "el literal de caracter contiene una secuencia de escape invalida",
            "escapes validos: \\n \\t \\r \\\\ \\\" \\' \\0 \\a \\b \\f \\v \\xHH \\OOO"); }

{CaracterAbierto}
    { error(ManejadorErrores.Tipo.CARACTER_MAL_FORMADO,
            "literal de caracter sin comilla simple de cierre",
            "cierre el literal con ' en la misma linea"); }

/* ---------- 8.5 Comentarios mal formados ---------- */

{ComentarioNoCerrado}
    { error(ManejadorErrores.Tipo.COMENTARIO_MAL_FORMADO,
            "comentario de bloque abierto que nunca se cierra",
            "agregue la marca de cierre de comentario de bloque"); }

/* ===== 9. Caracteres no reconocidos ===== */

.   { error(ManejadorErrores.Tipo.CARACTER_NO_RECONOCIDO,
            "el caracter no pertenece al alfabeto de C++Es",
            "elimine el caracter o reemplacelo por un operador valido"); }
