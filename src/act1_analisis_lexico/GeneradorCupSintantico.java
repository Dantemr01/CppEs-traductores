package act1_analisis_lexico;

import java.io.IOException;

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
