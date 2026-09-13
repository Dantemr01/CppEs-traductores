package act1_analisis_lexico;

import jflex.Main;

public class GeneradorJFlexLexico {

    public static void main(String[] args) {

        try {
            String rutaJflex = "src/act1_analisis_lexico/cppes_lexico.jflex";

            String datos[] = { rutaJflex };

            Main.generate(datos);

            System.out.println(">> JFlex termino: se genero CppLexer.java");

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.getLogger(GeneradorJFlexLexico.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
