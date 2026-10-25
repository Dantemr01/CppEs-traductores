package act1_analisis_lexico;

/**
 * Traduce el valor entero de un token a su nombre legible, para poder
 * imprimir en pantalla:
 *
 *      [linea-columna] NOMBRE_TOKEN: lexema
 *
 * CUP genera dentro de sym.java, ademas de las constantes enteras, un
 * arreglo publico llamado terminalNames en el que la posicion i contiene
 * el nombre del terminal cuyo valor es i. Aprovecharlo evita mantener una
 * lista paralela de nombres: al agregar un terminal nuevo en el archivo
 * .cup, su nombre aparece aqui automaticamente.
 */
public class TablaTokens {

    /** Devuelve el nombre del token, o TOKEN_<n> si el indice no existe. */
    public static String nombre(int tipo) {
        if (tipo >= 0 && tipo < sym.terminalNames.length) {
            return sym.terminalNames[tipo];
        }
        return "TOKEN_" + tipo;
    }

    /** Cantidad de terminales declarados en el archivo .cup. */
    public static int cantidadDeTokens() {
        return sym.terminalNames.length;
    }
}
