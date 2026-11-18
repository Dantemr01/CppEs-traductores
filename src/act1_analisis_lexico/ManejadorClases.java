package act1_analisis_lexico;

import java.util.ArrayList;

/**
 * MANEJADOR DE CLASES Y MIEMBROS
 *
 * Verifica los accesos de la forma  objeto.miembro  y
 * objeto.metodo(argumentos). Comprueba cuatro cosas en cadena:
 *
 *   1. que el objeto este declarado
 *   2. que su tipo sea una clase conocida
 *   3. que el miembro exista en esa clase
 *   4. que su visibilidad permita el acceso desde el contexto actual
 *
 * VISIBILIDAD EN C++Es
 *
 * Se siguen las reglas de C++, simplificadas al alcance del proyecto:
 *
 *   publico     accesible desde cualquier contexto
 *   privado     accesible solo desde dentro de la propia clase
 *   protegido   igual que privado mientras no haya herencia
 *
 * Un miembro sin modificador explicito se considera PUBLICO. C++ usaria
 * 'private' por omision en una 'class', pero en C++Es la mayoria de los
 * ejemplos de practicas anteriores declaran miembros sin modificador y
 * los usan entre metodos de la misma clase; tomar 'privado' por omision
 * habria invalidado codigo que ya era correcto.
 */
public class ManejadorClases {

    private final ManejadorTipos manejador;
    private final TablaSimbolos  tabla;

    public ManejadorClases(ManejadorTipos manejador, TablaSimbolos tabla) {
        this.manejador = manejador;
        this.tabla     = tabla;
    }

    /**
     * Acceso a un atributo:  objeto.atributo
     *
     * @return el tipo del atributo, o el tipo invalido si el acceso falla
     */
    public String validarAccesoAAtributo(String objeto, String miembro,
                                         int linea, int columna) {

        Simbolo simboloDelMiembro = resolver(objeto, miembro, linea, columna);
        if (simboloDelMiembro == null) { return ManejadorTipos.ERROR; }

        if (Simbolo.METODO.equals(simboloDelMiembro.getTipoSimbolo())) {
            manejador.reportar(
                "'" + miembro + "' es un metodo de la clase '"
                + simboloDelMiembro.getAmbito() + "'; faltan los parentesis"
                + " de la llamada", linea, columna);
            return ManejadorTipos.ERROR;
        }

        return simboloDelMiembro.getTipoDato();
    }

    /**
     * Llamada a un metodo de un objeto:  objeto.metodo(argumentos)
     *
     * @return el tipo de retorno del metodo, o el tipo invalido
     */
    public String validarLlamadaAMiembro(String objeto, String miembro,
                                         ArrayList argumentos,
                                         ManejadorMetodos metodos,
                                         int linea, int columna) {

        Simbolo simboloDelMiembro = resolver(objeto, miembro, linea, columna);
        if (simboloDelMiembro == null) { return ManejadorTipos.ERROR; }

        if (!Simbolo.METODO.equals(simboloDelMiembro.getTipoSimbolo())) {
            manejador.reportar(
                "'" + miembro + "' no es un metodo de la clase '"
                + simboloDelMiembro.getAmbito() + "': esta declarado como "
                + simboloDelMiembro.getTipoSimbolo().toLowerCase(),
                linea, columna);
            return ManejadorTipos.ERROR;
        }

        metodos.validarArgumentos(simboloDelMiembro, argumentos, linea, columna);
        return simboloDelMiembro.getTipoDato();
    }

    /* ==============================================================
       RESOLUCION DEL MIEMBRO
       ============================================================== */

    /**
     * Las cuatro comprobaciones en cadena. Devuelve el simbolo del
     * miembro si el acceso es valido, o null si ya reporto un error.
     */
    private Simbolo resolver(String objeto, String miembro,
                             int linea, int columna) {

        /* ---- 1. El objeto esta declarado ---- */
        Simbolo simboloDelObjeto = tabla.buscar(objeto);
        if (simboloDelObjeto == null) {
            manejador.reportar(
                "el identificador '" + objeto + "' no ha sido declarado en el"
                + " ambito '" + tabla.ambitoActual() + "'", linea, columna);
            return null;
        }

        /* ---- 2. Su tipo es una clase conocida ---- */
        String nombreDeLaClase = simboloDelObjeto.getTipoDato();
        if (!tabla.esUnaClase(nombreDeLaClase)) {
            manejador.reportar(
                "'" + objeto + "' es de tipo '" + nombreDeLaClase + "', que no"
                + " es una clase; no admite acceso a miembros con '.'",
                linea, columna);
            return null;
        }

        /* ---- 3. El miembro existe en esa clase ---- */
        Simbolo simboloDelMiembro = tabla.buscarMiembro(nombreDeLaClase, miembro);
        if (simboloDelMiembro == null) {
            manejador.reportar(
                "el atributo o metodo '" + miembro + "' no esta declarado en la"
                + " clase '" + nombreDeLaClase + "'", linea, columna);
            return null;
        }

        /* ---- 4. Su visibilidad permite el acceso ---- */
        if (!simboloDelMiembro.esAccesibleDesdeFuera()
            && !tabla.estamosDentroDeLaClase(nombreDeLaClase)) {

            manejador.reportar(
                "no se permite acceder al miembro '" + miembro + "' desde el"
                + " contexto actual: esta declarado como "
                + simboloDelMiembro.getVisibilidad().toLowerCase()
                + " en la clase '" + nombreDeLaClase + "'",
                linea, columna);
            return null;
        }

        return simboloDelMiembro;
    }
}
