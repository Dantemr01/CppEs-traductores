package act1_analisis_lexico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManejadorErrores {

    /** Categorias de error pedidas por la practica. */
    public enum Tipo {
        CARACTER_NO_RECONOCIDO   ("CARACTER NO RECONOCIDO"),
        IDENTIFICADOR_MAL_FORMADO("IDENTIFICADOR MAL FORMADO"),
        LITERAL_NUMERICO         ("LITERAL NUMERICO INCORRECTO"),
        CADENA_MAL_FORMADA       ("CADENA MAL FORMADA"),
        CARACTER_MAL_FORMADO     ("LITERAL DE CARACTER MAL FORMADO"),
        DIRECTIVA_MAL_FORMADA    ("DIRECTIVA MAL FORMADA"),
        COMENTARIO_MAL_FORMADO   ("COMENTARIO MAL FORMADO");

        private final String etiqueta;

        Tipo(String etiqueta) { this.etiqueta = etiqueta; }

        public String getEtiqueta() { return etiqueta; }
    }

    public static class ErrorLexico {
        public final Tipo   tipo;
        public final int    linea;
        public final int    columna;
        public final String lexema;
        public final String descripcion;
        public final String sugerencia;

        public ErrorLexico(Tipo tipo, int linea, int columna,
                           String lexema, String descripcion, String sugerencia) {
            this.tipo        = tipo;
            this.linea       = linea;
            this.columna     = columna;
            this.lexema      = lexema;
            this.descripcion = descripcion;
            this.sugerencia  = sugerencia;
        }
    }

    private final List<ErrorLexico> errores = new ArrayList<>();

     /* Registra un error y lo imprime en el momento*/
    public void reportar(Tipo tipo, int linea, int columna,
                         String lexema, String descripcion, String sugerencia) {

        errores.add(new ErrorLexico(tipo, linea, columna, lexema, descripcion, sugerencia));

        System.out.println("[" + linea + "-" + columna + "] "
                + "ERROR LEXICO (" + tipo.getEtiqueta() + "): "
                + "\"" + visible(lexema) + "\" -> " + descripcion);
        System.out.println("          Sugerencia: " + sugerencia);
    }

    private static String visible(String texto) {
        String limpio = texto.replace("\r", "\\r")
                             .replace("\n", "\\n")
                             .replace("\t", "\\t");
        /* Los lexemas muy largos (por ejemplo un comentario sin cerrar que
           consume el resto del archivo) se recortan para no inundar la consola. */
        if (limpio.length() > 40) {
            limpio = limpio.substring(0, 40) + "...";
        }
        return limpio;
    }

    public int total() {
        return errores.size();
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }

    public List<ErrorLexico> getErrores() {
        return errores;
    }

    /* Cuantos errores hubo de cada categoria. */
    public Map<Tipo, Integer> conteoPorTipo() {
        Map<Tipo, Integer> conteo = new LinkedHashMap<>();
        for (Tipo t : Tipo.values()) {
            conteo.put(t, 0);
        }
        for (ErrorLexico e : errores) {
            conteo.put(e.tipo, conteo.get(e.tipo) + 1);
        }
        return conteo;
    }

    /* Tabla final agrupada por categoria. */
    public void imprimirResumen() {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println(" RESUMEN DE ERRORES LEXICOS");
        System.out.println("--------------------------------------------------");

        if (!hayErrores()) {
            System.out.println(" No se encontraron errores lexicos.");
            System.out.println("--------------------------------------------------");
            return;
        }

        for (Map.Entry<Tipo, Integer> entrada : conteoPorTipo().entrySet()) {
            if (entrada.getValue() == 0) {
                continue;
            }
            System.out.println();
            System.out.println(" " + entrada.getKey().getEtiqueta()
                             + "  (" + entrada.getValue() + ")");

            for (ErrorLexico e : errores) {
                if (e.tipo == entrada.getKey()) {
                    System.out.println("   linea " + e.linea + ", columna " + e.columna
                                     + " -> " + e.descripcion);
                }
            }
        }

        System.out.println();
        System.out.println(" TOTAL DE ERRORES: " + total());
        System.out.println("--------------------------------------------------");
    }
}
