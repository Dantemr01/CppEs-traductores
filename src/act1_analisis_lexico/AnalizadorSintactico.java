package act1_analisis_lexico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Clase principal de la practica de ANALISIS SINTACTICO.
 *
 * Encadena las dos fases del compilador construidas hasta ahora:
 *
 *   archivo fuente  ->  CppLexer (analizador lexico)  ->  flujo de tokens
 *                   ->  ParserCppEs (analizador sintactico)  ->  validacion
 *
 * El parser no lee el archivo: le pide tokens al lexer conforme los va
 * necesitando. Por eso al constructor de ParserCppEs se le pasa el lexer.
 *
 * ORDEN DE EJECUCION EN NETBEANS
 *   1) GeneradorCupSintantico  -> genera sym.java y ParserCppEs.java
 *   2) GeneradorJFlexLexico    -> genera CppLexer.java
 *   3) Clean and Build
 *   4) AnalizadorSintactico    (esta clase)
 */
public class AnalizadorSintactico {

    private static final String ARCHIVO_POR_OMISION =
            "src/act1_analisis_lexico/sem_valido.txt";

    /* Poner en true para ver tambien la lista de tokens del analizador lexico */
    private static final boolean MOSTRAR_TOKENS = false;

    public static void main(String[] args) {

        String ruta = (args.length > 0) ? args[0] : ARCHIVO_POR_OMISION;

        System.out.println("==================================================");
        System.out.println(" ANALIZADOR SINTACTICO Y SEMANTICO - LENGUAJE C++Es");
        System.out.println(" Verificacion de tipos de datos en expresiones");
        System.out.println(" Archivo analizado: " + ruta);
        System.out.println("==================================================");
        System.out.println();

        try (Reader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            CppLexer lexer = new CppLexer(lector);
            lexer.setMostrarTokens(MOSTRAR_TOKENS);

            ParserCppEs parser = new ParserCppEs(lexer);

            /* parse() consume el flujo de tokens completo. Cada vez que una
               produccion se reduce, su accion imprime "Regla reconocida". */
            parser.parse();

            System.out.println();
            System.out.println("==================================================");
            System.out.println(" FIN DEL ANALISIS SINTACTICO");
            System.out.println(" Tokens leidos        : " + lexer.getTotalTokens());
            System.out.println(" Errores lexicos      : " + lexer.getTotalErrores());
            System.out.println(" Reglas reconocidas   : " + parser.getReglasReconocidas());
            System.out.println(" Errores recuperables : " + parser.getErroresRecuperables());
            System.out.println(" Errores fatales      : " + parser.getErroresFatales());
            System.out.println(" Recuperaciones       : " + parser.getRecuperaciones());
            System.out.println(" Errores semanticos   : " + parser.getErroresSemanticos()
                             + "   (tipos, ambito e inmutabilidad)");
            System.out.println(" Advertencias semant. : " + parser.getAdvertenciasSemanticas()
                             + "   (cantidad de argumentos; no son errores sintacticos)");
            System.out.println("==================================================");

            boolean sintaxisValida = !parser.huboErrores() && lexer.getTotalErrores() == 0;
            boolean semanticaValida = parser.getErroresSemanticos() == 0;

            if (sintaxisValida && semanticaValida) {
                System.out.println(" RESULTADO: el archivo es VALIDO sintactica y semanticamente.");
            } else if (sintaxisValida) {
                System.out.println(" RESULTADO: la estructura es VALIDA, pero el archivo");
                System.out.println("            viola las reglas de tipos del lenguaje.");
            } else {
                System.out.println(" RESULTADO: la estructura del archivo NO es valida.");
            }
            System.out.println("==================================================");

            /* Detalle de los errores lexicos, si los hubo */
            if (lexer.getTotalErrores() > 0) {
                lexer.getManejadorErrores().imprimirResumen();
            }

            /* Reporte del analisis semantico y de la Tabla de Simbolos */
            parser.getTipos().imprimirResumen();
            parser.getTabla().imprimirResumen();

        } catch (IOException ex) {
            System.err.println("No se pudo leer el archivo: " + ruta);
            System.err.println("Detalle: " + ex.getMessage());

        } catch (Exception ex) {
            System.err.println("El analisis sintactico se interrumpio: " + ex.getMessage());
        }
    }
}
