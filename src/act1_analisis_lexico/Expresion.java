package act1_analisis_lexico;

/**
 * Resultado de evaluar una expresion durante el analisis.
 *
 * Guarda dos cosas a la vez:
 *
 *   texto -> la reconstruccion de la expresion tal como se escribio, que es
 *            lo que se imprime en los mensajes "Expresion reconocida: ..."
 *   tipo  -> el tipo de dato que la expresion produce, que es lo que usa el
 *            analizador semantico para verificar compatibilidades
 *
 * IMPORTANTE: toString() devuelve el texto. Gracias a eso, todas las acciones
 * de la gramatica que ya concatenaban la expresion en un mensaje
 * ("Asignacion reconocida: " + e) siguen funcionando sin cambios, aunque el
 * no terminal ya no devuelva un String sino un objeto Expresion.
 */
public class Expresion {

    private final String texto;
    private final String tipo;

    public Expresion(String texto, String tipo) {
        this.texto = texto;
        this.tipo  = tipo;
    }

    public String getTexto() { return texto; }
    public String getTipo()  { return tipo;  }

    /** true si el tipo ya quedo invalidado por un error previo. */
    public boolean esError() {
        return ManejadorTipos.ERROR.equals(tipo);
    }

    @Override
    public String toString() { return texto; }
}
