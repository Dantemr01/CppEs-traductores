package act1_analisis_lexico;

/**
 * Traduce el valor entero de un token a su nombre legible, para poder
 * imprimir en pantalla:
 *
 *      [linea-columna] NOMBRE_TOKEN: lexema
 */
public class TablaTokens {

    public static String nombre(int tipo) {
        if (tipo >= 0 && tipo < sym.terminalNames.length) {
            return sym.terminalNames[tipo];
        }
        return "TOKEN_" + tipo;
    }

    public static int cantidadDeTokens() {
        return sym.terminalNames.length;
    }
}
