package act1_analisis_lexico;

/**
 * VALIDADOR DE CONVENCIONES DE NOMENCLATURA
 *
 * Comprueba que cada identificador siga la convencion de estilo que le
 * corresponde segun su categoria:
 *
 *   Clase                        PascalCase    ProcesadorDatos
 *   Metodo                       camelCase     calcularPromedio
 *   Variable, atributo           camelCase     resultadoBase
 *   Constante                    SNAKE_CASE    MAX_CALIFICACION
 *                                o camelCase   maxCalificacion
 *
 * Es una comprobacion de ESTILO, no de sintaxis: 'VariableLocal' es un
 * identificador perfectamente valido para el analizador lexico y para la
 * gramatica. Lo que viola es una convencion del lenguaje, y por eso se
 * reporta en la fase semantica y no antes.
 */
public class ValidadorNomenclatura {

    private final ManejadorTipos manejador;

    public ValidadorNomenclatura(ManejadorTipos manejador) {
        this.manejador = manejador;
    }

    /* ==============================================================
       RECONOCEDORES DE ESTILO
       ============================================================== */

    /** PascalCase: empieza en mayuscula, sin guiones bajos. */
    public static boolean esPascalCase(String s) {
        return s != null && s.matches("[A-Z][a-zA-Z0-9]*");
    }

    /** camelCase: empieza en minuscula, sin guiones bajos. */
    public static boolean esCamelCase(String s) {
        return s != null && s.matches("[a-z][a-zA-Z0-9]*");
    }

    /** SNAKE_CASE en mayusculas: MAX_CALIFICACION, PI, LIMITE_2. */
    public static boolean esSnakeCaseMayusculas(String s) {
        return s != null && s.matches("[A-Z][A-Z0-9]*(_[A-Z0-9]+)*");
    }

    /* ==============================================================
       VALIDACION POR CATEGORIA
       ============================================================== */

    /**
     * Verifica el nombre contra la convencion de su categoria y reporta el
     * error si no la cumple.
     *
     * @return true si el nombre cumple la convencion
     */
    public boolean validar(String nombre, String categoria,
                           int linea, int columna) {

        if (Simbolo.CLASE.equals(categoria)) {
            if (esPascalCase(nombre)) { return true; }
            reportar("de clase", nombre, "PascalCase", categoria, linea, columna);
            return false;
        }

        if (Simbolo.CONSTANTE.equals(categoria)) {
            /* El enunciado admite las dos convenciones para constantes. */
            if (esSnakeCaseMayusculas(nombre) || esCamelCase(nombre)) {
                return true;
            }
            reportar("de constante", nombre, "SNAKE_CASE o camelCase",
                     categoria, linea, columna);
            return false;
        }

        /* Metodos, variables, atributos y parametros: camelCase */
        if (esCamelCase(nombre)) { return true; }

        reportar(descripcion(categoria), nombre, "camelCase",
                 categoria, linea, columna);
        return false;
    }

    private String descripcion(String categoria) {
        if (Simbolo.METODO.equals(categoria))    { return "de metodo";    }
        if (Simbolo.ATRIBUTO.equals(categoria))  { return "de atributo";  }
        if (Simbolo.PARAMETRO.equals(categoria)) { return "de parametro"; }
        return "de variable";
    }

    private void reportar(String que, String nombre, String convencion,
                          String categoria, int linea, int columna) {
        String sugerido = sugerir(nombre, categoria);
        String ayuda = (sugerido == null || sugerido.equals(nombre))
                     ? ""
                     : "; se sugiere '" + sugerido + "'";
        manejador.reportar(
            "el identificador " + que + " '" + nombre + "' debe utilizar la"
            + " convencion " + convencion + ayuda,
            linea, columna);
    }

    /* ==============================================================
       SUGERENCIA DE CORRECCION
       ============================================================== */

    /**
     * Propone como se escribiria el nombre con la convencion correcta.
     * Sirve para que el mensaje de error sea accionable y no solo un
     * reproche: el programador ve exactamente que debe escribir.
     */
    public static String sugerir(String nombre, String categoria) {
        if (nombre == null || nombre.length() == 0) { return nombre; }

        /* Separa el nombre en palabras: por guion bajo o por mayuscula. */
        String[] palabras = nombre.replace('_', ' ')
                                  .replaceAll("([a-z0-9])([A-Z])", "$1 $2")
                                  .toLowerCase()
                                  .trim()
                                  .split("\\s+");

        if (Simbolo.CONSTANTE.equals(categoria)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < palabras.length; i++) {
                if (i > 0) { sb.append('_'); }
                sb.append(palabras[i].toUpperCase());
            }
            return sb.toString();
        }

        boolean pascal = Simbolo.CLASE.equals(categoria);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            String p = palabras[i];
            if (p.length() == 0) { continue; }
            if (i == 0 && !pascal) {
                sb.append(p);
            } else {
                sb.append(Character.toUpperCase(p.charAt(0)));
                sb.append(p.substring(1));
            }
        }
        return sb.toString();
    }
}
