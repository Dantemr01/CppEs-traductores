package act1_analisis_lexico;

import java.util.ArrayList;

/**
 * MANEJADOR DE METODOS
 *
 * Verifica que una llamada sea coherente con la declaracion del metodo
 * invocado. Comprueba, en este orden:
 *
 *   1. que el metodo exista y pueda resolverse en este contexto
 *   2. que la cantidad de argumentos coincida con la de parametros
 *   3. que el tipo de cada argumento sea compatible con su parametro
 *
 * El orden importa: si el metodo no existe no tiene sentido contar
 * argumentos, y si la cantidad no coincide no tiene sentido emparejar
 * tipos uno a uno. Cada comprobacion corta la cadena, de modo que un
 * solo defecto produce un solo mensaje.
 */
public class ManejadorMetodos {

    private final ManejadorTipos manejador;
    private final TablaSimbolos  tabla;

    public ManejadorMetodos(ManejadorTipos manejador, TablaSimbolos tabla) {
        this.manejador = manejador;
        this.tabla     = tabla;
    }

    /**
     * Verifica una llamada completa y devuelve el TIPO que produce, para
     * que pueda usarse dentro de una expresion.
     *
     * @param argumentos lista de Expresion, cada una con su texto y su tipo
     * @return el tipo de retorno del metodo, o el tipo invalido si falla
     */
    public String validarLlamada(String nombre, ArrayList argumentos,
                                 int linea, int columna) {

        Simbolo metodo = tabla.buscar(nombre);

        /* ---- 1. El metodo existe y es un metodo ---- */
        if (metodo == null) {
            manejador.reportar(
                "el metodo '" + nombre + "' no esta declarado o no puede"
                + " resolverse en este contexto", linea, columna);
            return ManejadorTipos.ERROR;
        }

        if (!Simbolo.METODO.equals(metodo.getTipoSimbolo())) {
            manejador.reportar(
                "'" + nombre + "' no es un metodo: esta declarado como "
                + metodo.getTipoSimbolo().toLowerCase(), linea, columna);
            return ManejadorTipos.ERROR;
        }

        /* ---- 2 y 3. Cantidad y tipos de los argumentos ---- */
        validarArgumentos(metodo, argumentos, linea, columna);

        return metodo.getTipoDato();
    }

    /**
     * Comprueba la correspondencia entre los argumentos de la llamada y
     * los parametros declarados.
     */
    public void validarArgumentos(Simbolo metodo, ArrayList argumentos,
                                  int linea, int columna) {

        ArrayList parametros = metodo.getParametros();
        int esperados = parametros.size();
        int recibidos = (argumentos == null) ? 0 : argumentos.size();

        /* ---- Cantidad ---- */
        if (esperados != recibidos) {
            manejador.reportar(
                "el metodo '" + metodo.getId() + "' requiere " + esperados
                + " argumento(s), pero se proporciono " + recibidos,
                linea, columna);
            return;   /* sin la misma cantidad, emparejar tipos no tiene sentido */
        }

        /* ---- Tipos, uno a uno ---- */
        for (int i = 0; i < esperados; i++) {
            Parametro p = (Parametro) parametros.get(i);
            Expresion a = (Expresion) argumentos.get(i);

            if (manejador.esAsignable(p.getTipo(), a.getTipo())) { continue; }

            manejador.reportar(
                "el argumento " + (i + 1) + " del metodo '" + metodo.getId()
                + "' es de tipo '" + a.getTipo() + "'; se esperaba un tipo"
                + " compatible con '" + p.getTipo() + "'",
                linea, columna);
        }
    }
}
