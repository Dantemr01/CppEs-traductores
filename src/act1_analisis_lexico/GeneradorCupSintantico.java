package act1_analisis_lexico;

import java.io.IOException;

/**
 * Ejecuta CUP sobre cppes_sintactico.cup.
 *
 * Genera dentro del mismo paquete:
 *   - sym.java          (constantes enteras de cada terminal)
 *   - ParserCppEs.java  (analizador sintactico de la estructura del archivo)
 *
 * IMPORTANTE: esta clase debe ejecutarse ANTES que GeneradorJFlexLexico,
 * porque el archivo .jflex hace referencia a sym.X.
 */
public class GeneradorCupSintantico {

    public static void main(String[] args) throws IOException, Exception {

        String[] parametros = {
            "-destdir", "src/act1_analisis_lexico",
            "-parser",  "ParserCppEs",
            "-progress",
            "src/act1_analisis_lexico/cppes_sintactico.cup"
        };

        java_cup.Main.main(parametros);

        System.out.println(">> CUP termino: se generaron sym.java y ParserCppEs.java");
    }
}
