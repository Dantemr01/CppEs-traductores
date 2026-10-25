package act1_analisis_lexico;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TABLA DE SIMBOLOS
 *
 * Guarda todos los identificadores declarados y permite consultarlos desde
 * las acciones semanticas de la gramatica.
 *
 * MODELO DE AMBITOS
 *
 *   Ambito 0 (global)  -> variables y constantes globales, atributos de la
 *                         clase, constantes de clase y metodos
 *   Ambito 1 (local)   -> parametros y variables locales del metodo que se
 *                         esta analizando en este momento
 *
 * El ambito local se abre solo si el metodo declara algo, y se cierra cuando
 * la regla 'metodo' se reduce. Como el analizador es ascendente, los
 * parametros y las declaraciones locales se reducen ANTES que el cuerpo del
 * metodo, de modo que ya estan en la tabla cuando el cuerpo las consulta.
 *
 * LIMITACION ASUMIDA: el ambito local es plano. Una variable declarada
 * dentro de un bloque 'si' o 'mientras' sigue siendo visible despues de ese
 * bloque, porque el analizador es de una sola pasada y no se abre un ambito
 * por bloque. Para el alcance de esta practica basta con distinguir lo
 * global de lo local.
 */
public class TablaSimbolos {

    /** Un ambito: su nombre y los simbolos declarados en el. */
    private static class Ambito {
        final String nombre;
        /* LinkedHashMap para que el reporte conserve el orden de declaracion */
        final Map simbolos = new LinkedHashMap();
        Ambito(String nombre) { this.nombre = nombre; }
    }

    private final ArrayList ambitos = new ArrayList();
    private final ManejadorTipos manejador;

    /** Historial de los ambitos locales ya cerrados, para el reporte final. */
    private final ArrayList historial = new ArrayList();

    public TablaSimbolos(ManejadorTipos manejador) {
        this.manejador = manejador;
        ambitos.add(new Ambito("global"));
    }

    /* ==============================================================
       APERTURA Y CIERRE DE AMBITOS
       ============================================================== */

    /** Abre el ambito local del metodo indicado, si no estaba abierto. */
    public void abrirAmbitoLocal(String nombreDelMetodo) {
        if (ambitos.size() == 1) {
            ambitos.add(new Ambito(nombreDelMetodo));
        }
    }

    /**
     * Cierra el ambito local. Se llama al reducir la regla 'metodo'.
     * Si el metodo no declaro nada no hay ambito local que cerrar.
     */
    public void cerrarAmbitoLocal(String nombreDelMetodo) {
        if (ambitos.size() > 1) {
            Ambito a = (Ambito) ambitos.remove(ambitos.size() - 1);
            historial.add(renombrar(a, nombreDelMetodo));
        }
    }

    private Ambito renombrar(Ambito original, String nombre) {
        Ambito copia = new Ambito(nombre);
        copia.simbolos.putAll(original.simbolos);
        return copia;
    }

    private Ambito ambitoActual() {
        return (Ambito) ambitos.get(ambitos.size() - 1);
    }

    private boolean hayAmbitoLocal() {
        return ambitos.size() > 1;
    }

    /* ==============================================================
       DECLARACION
       ============================================================== */

    /**
     * Da de alta un identificador en el ambito actual.
     * Reporta un error semantico si ya existia en ESE mismo ambito; volver a
     * declararlo en un ambito mas interno es legal y lo oculta.
     *
     * @return el simbolo dado de alta, o el que ya existia si estaba repetido
     */
    public Simbolo declarar(String nombre, String categoria, String tipo,
                            int linea, int columna) {

        Ambito destino = ambitoActual();

        if (destino.simbolos.containsKey(nombre)) {
            Simbolo previo = (Simbolo) destino.simbolos.get(nombre);
            manejador.reportar(
                "el identificador '" + nombre + "' ya fue declarado en este ambito"
                + " (linea " + previo.getLinea() + ")",
                linea, columna);
            return previo;
        }

        Simbolo s = new Simbolo(nombre, categoria, tipo, linea, columna,
                                destino.nombre);
        destino.simbolos.put(nombre, s);
        return s;
    }

    /** Alta de un metodo: siempre va al ambito global, nunca al local. */
    public Simbolo declararMetodo(String nombre, String tipoDeRetorno,
                                  int numeroDeParametros, int linea, int columna) {

        Ambito global = (Ambito) ambitos.get(0);

        if (global.simbolos.containsKey(nombre)) {
            Simbolo previo = (Simbolo) global.simbolos.get(nombre);
            manejador.reportar(
                "el identificador '" + nombre + "' ya fue declarado en este ambito"
                + " (linea " + previo.getLinea() + ")",
                linea, columna);
            return previo;
        }

        Simbolo s = new Simbolo(nombre, Simbolo.METODO, tipoDeRetorno,
                                linea, columna, "global");
        s.setNumeroDeParametros(numeroDeParametros);
        global.simbolos.put(nombre, s);
        return s;
    }

    /* ==============================================================
       CONSULTA
       ============================================================== */

    /** Busca del ambito mas interno al mas externo. null si no existe. */
    public Simbolo buscar(String nombre) {
        for (int i = ambitos.size() - 1; i >= 0; i--) {
            Ambito a = (Ambito) ambitos.get(i);
            Simbolo s = (Simbolo) a.simbolos.get(nombre);
            if (s != null) { return s; }
        }
        return null;
    }

    /**
     * Consulta usada cuando un identificador aparece como OPERANDO de una
     * expresion. Si no esta declarado reporta el error y devuelve el tipo
     * invalido, que se propaga sin generar errores en cascada.
     */
    public String tipoDelOperando(String nombre, int linea, int columna) {
        Simbolo s = buscar(nombre);
        if (s == null) {
            manejador.reportar(
                "el identificador '" + nombre + "' no ha sido declarado en este ambito",
                linea, columna);
            return ManejadorTipos.ERROR;
        }
        if (Simbolo.METODO.equals(s.getCategoria())) {
            manejador.reportar(
                "'" + nombre + "' es un metodo y no puede usarse como operando"
                + "; faltan los parentesis de la llamada",
                linea, columna);
            return ManejadorTipos.ERROR;
        }
        return s.getTipo();
    }

    /**
     * Comprueba que el destino de una asignacion exista y sea modificable.
     * Devuelve el simbolo si la asignacion puede realizarse, null si no.
     */
    public Simbolo destinoDeAsignacion(String nombre, int linea, int columna) {
        Simbolo s = buscar(nombre);

        if (s == null) {
            manejador.reportar(
                "el identificador '" + nombre + "' no ha sido declarado en este ambito",
                linea, columna);
            return null;
        }

        if (s.esInmutable()) {
            manejador.reportar(
                "no se puede reasignar un valor a la constante '" + nombre + "'",
                linea, columna);
            return null;
        }

        if (Simbolo.METODO.equals(s.getCategoria())) {
            manejador.reportar(
                "'" + nombre + "' es un metodo y no admite asignacion",
                linea, columna);
            return null;
        }

        return s;
    }

    /* ==============================================================
       REPORTE
       ============================================================== */

    public int totalDeSimbolos() {
        int n = 0;
        for (int i = 0; i < ambitos.size(); i++) {
            n += ((Ambito) ambitos.get(i)).simbolos.size();
        }
        for (int i = 0; i < historial.size(); i++) {
            n += ((Ambito) historial.get(i)).simbolos.size();
        }
        return n;
    }

    /** Imprime el ambito local de un metodo justo antes de cerrarlo. */
    public void imprimirAmbitoLocal(String nombreDelMetodo) {
        if (!hayAmbitoLocal()) { return; }
        imprimirAmbito(ambitoActual(), "Ambito local del metodo " + nombreDelMetodo);
    }

    /** Imprime la tabla completa al terminar el analisis. */
    public void imprimirResumen() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" TABLA DE SIMBOLOS");
        System.out.println("==================================================");

        imprimirAmbito((Ambito) ambitos.get(0), "Ambito global");

        for (int i = 0; i < historial.size(); i++) {
            Ambito a = (Ambito) historial.get(i);
            imprimirAmbito(a, "Ambito local del metodo " + a.nombre);
        }

        System.out.println();
        System.out.println(" Total de identificadores registrados: " + totalDeSimbolos());
    }

    private void imprimirAmbito(Ambito a, String encabezado) {
        System.out.println();
        System.out.println(" " + encabezado + "  (" + a.simbolos.size() + ")");
        if (a.simbolos.isEmpty()) {
            System.out.println("    (sin identificadores)");
            return;
        }
        System.out.println(String.format("  %-18s %-11s %-10s %-9s %-8s %s",
                                         "NOMBRE", "CATEGORIA", "TIPO",
                                         "LINEA", "COLUMNA", "DETALLE"));
        for (Object valor : a.simbolos.values()) {
            System.out.println(((Simbolo) valor).comoFila());
        }
    }
}
