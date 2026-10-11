package act1_analisis_lexico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java_cup.runtime.Symbol;

/**
 * Clase principal de la practica: lee un archivo fuente escrito en C++Es y
 * muestra en consola la lista de componentes lexicos reconocidos.
 *
 * ORDEN DE EJECUCION EN NETBEANS
 *   1) GeneradorCupSintantico  -> genera sym.java y ParserCppEs.java
 *   2) GeneradorJFlexLexico    -> genera CppLexer.java
 *   3) Clean and Build
 *   4) AnalizadorLexico        (esta clase)
 */
public class AnalizadorLexico {

    /* Archivo que se analiza por omision. Se puede cambiar aqui o pasar
       la ruta como argumento al ejecutar la clase. */
    private static final String ARCHIVO_POR_OMISION =
            "src/act1_analisis_lexico/prueba1_programa_valido.txt";

    public static void main(String[] args) {

        String ruta = (args.length > 0) ? args[0] : ARCHIVO_POR_OMISION;

        System.out.println("==================================================");
        System.out.println(" ANALIZADOR LEXICO - LENGUAJE C++Es");
        System.out.println(" Archivo analizado: " + ruta);
        System.out.println("==================================================");

        /* Se fuerza UTF-8 para que las letras acentuadas y la ñ dentro de
           identificadores y cadenas se lean igual en cualquier sistema. */
        try (Reader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            CppLexer lexer = new CppLexer(lector);

            while (true) {
                Symbol token = lexer.next_token();
                if (token.sym == sym.EOF) {
                    break;
                }
            }

            System.out.println();
            System.out.println("==================================================");
            System.out.println(" FIN DEL ANALISIS");
            System.out.println(" Tokens reconocidos : " + lexer.getTotalTokens());
            System.out.println(" Errores lexicos    : " + lexer.getTotalErrores());
            System.out.println("==================================================");

            /* Tabla final agrupada por categoria de error */
            lexer.getManejadorErrores().imprimirResumen();

        } catch (IOException ex) {
            System.err.println("No se pudo leer el archivo: " + ruta);
            System.err.println("Detalle: " + ex.getMessage());
        }
    }
}
