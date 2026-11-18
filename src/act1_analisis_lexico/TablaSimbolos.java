package act1_analisis_lexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TABLA DE SIMBOLOS
 *
 * Guarda todos los identificadores declarados y permite consultarlos desde
 * las acciones semanticas de la gramatica.
 *
 * ESTRUCTURA DE DATOS
 *
 * Son dos estructuras dinamicas que trabajan juntas, porque resuelven dos
 * problemas distintos:
 *
 *   ambitos   ArrayList usado como PILA de ambitos. Cada ambito es un
 *             LinkedHashMap de nombre a Simbolo. Sirve para la CONSULTA:
 *             buscar del ambito mas interno al mas externo, y detectar
 *             duplicados dentro de un mismo ambito.
 *
 *   todos     ArrayList con TODOS los simbolos registrados, en el orden en
 *             que se dieron de alta. Sirve para el REPORTE: cuando un
 *             ambito se cierra, sus simbolos salen de la pila pero siguen
 *             en esta lista, de modo que la tabla final los muestra.
 *
 * Ninguna de las dos tiene tamanio fijo: crecen conforme avanza el analisis.
 *
 * JERARQUIA DE AMBITOS
 *
 *   GLOBAL          variables y constantes globales, y la clase misma
 *     clase         atributos, constantes de clase y metodos
 *       metodo      parametros y variables locales
 *
 * El ambito de clase se abre al reconocer el encabezado de la clase, y el
 * de metodo al declararse su primer parametro o variable local. Como el
 * analizador es ascendente, esas reducciones ocurren ANTES que el cuerpo
 * que las usa, de modo que los identificadores ya estan en la tabla cuando
 * el cuerpo los consulta.
 *
 * LIMITACION ASUMIDA: el ambito de metodo es plano. Una variable declarada
 * dentro de un bloque 'si' o 'mientras' sigue siendo visible despues de ese
 * bloque, porque no se abre un ambito por bloque.
 */
public class TablaSimbolos {

    /* Clases de ambito */
    private static final String GLOBAL = "GLOBAL";
    private static final String CLASE  = "CLASE";
    private static final String METODO = "METODO";

    /** Un ambito: su nombre, su clase y los simbolos declarados en el. */
    private static class Ambito {
        String nombre;
        final String clase;
        final Map simbolos = new LinkedHashMap();
        Ambito(String nombre, String clase) {
            this.nombre = nombre;
            this.clase  = clase;
        }
    }

    private final ArrayList ambitos = new ArrayList();   // pila de ambitos
    private final ArrayList todos   = new ArrayList();   // historial completo
    private final ManejadorTipos manejador;
    private final ValidadorNomenclatura nomenclatura;

    /** Nombre con el que se abre un ambito cuyo nombre aun no se conoce. */
    private static final String PENDIENTE = "(pendiente)";

    public TablaSimbolos(ManejadorTipos manejador) {
        this.manejador    = manejador;
        this.nomenclatura = new ValidadorNomenclatura(manejador);
        ambitos.add(new Ambito(GLOBAL, GLOBAL));
    }

    public ValidadorNomenclatura getNomenclatura() { return nomenclatura; }

    /* ==============================================================
       APERTURA Y CIERRE DE AMBITOS
       ============================================================== */

    public void abrirAmbitoClase(String nombreDeLaClase) {
        ambitos.add(new Ambito(nombreDeLaClase, CLASE));
    }

    public void cerrarAmbitoClase() {
        if (ambitos.size() > 1 && CLASE.equals(cima().clase)) {
            ambitos.remove(ambitos.size() - 1);
        }
    }

    /** Abre el ambito del metodo en curso, si no estaba abierto. */
    public void abrirAmbitoMetodo() {
        if (!METODO.equals(cima().clase)) {
            ambitos.add(new Ambito(PENDIENTE, METODO));
        }
    }

    /**
     * Le pone nombre al ambito del metodo en cuanto se reconoce su
     * encabezado. Hasta ese momento el ambito pudo abrirse con un nombre
     * provisional, porque los parametros se reducen antes que el encabezado
     * completo. A partir de aqui los mensajes de error ya pueden nombrar el
     * ambito correcto.
     */
    public void nombrarAmbitoMetodo(String nombreDelMetodo) {
        abrirAmbitoMetodo();
        Ambito a = cima();
        a.nombre = nombreDelMetodo;
        for (Object v : a.simbolos.values()) {
            ((Simbolo) v).setAmbito(nombreDelMetodo);
        }
    }

    /**
     * Cierra el ambito del metodo y le pone su nombre definitivo, que no se
     * conocia al abrirlo porque la regla 'metodo' se reduce al final.
     */
    public void cerrarAmbitoMetodo(String nombreDelMetodo) {
        if (!METODO.equals(cima().clase)) { return; }

        Ambito a = cima();
        a.nombre = nombreDelMetodo;
        for (Object v : a.simbolos.values()) {
            ((Simbolo) v).setAmbito(nombreDelMetodo);
        }
        ambitos.remove(ambitos.size() - 1);
    }

    private Ambito cima() {
        return (Ambito) ambitos.get(ambitos.size() - 1);
    }

    /** Ambito donde viven los metodos: el de la clase, o el global. */
    private Ambito ambitoDeClase() {
        for (int i = ambitos.size() - 1; i >= 0; i--) {
            Ambito a = (Ambito) ambitos.get(i);
            if (CLASE.equals(a.clase) || GLOBAL.equals(a.clase)) { return a; }
        }
        return (Ambito) ambitos.get(0);
    }

    /** Nombre del ambito actual, para los mensajes de error. */
    public String ambitoActual() {
        return cima().nombre;
    }

    /* ==============================================================
       DECLARACION
       ============================================================== */

    /**
     * Da de alta un identificador en el ambito actual.
     *
     * Hace tres cosas en este orden: comprueba que el nombre cumpla la
     * convencion de su categoria, comprueba que no este ya declarado en ESE
     * mismo ambito, y lo inserta.
     *
     * Un nombre mal escrito se registra de todos modos: el error es de
     * estilo, no impide saber que el identificador existe, y omitirlo
     * provocaria despues un falso "no ha sido declarado".
     *
     * @return el simbolo dado de alta, o el que ya existia si estaba repetido
     */
    public Simbolo declarar(String id, String tiposimbolo, String tipodato,
                            int linea, int columna) {
        return declararEn(cima(), id, tiposimbolo, tipodato, linea, columna);
    }

    /** Alta de un metodo: va al ambito de la clase, no al local. */
    public Simbolo declararMetodo(String id, String tipoDeRetorno,
                                  ArrayList parametros,
                                  int linea, int columna) {
        Simbolo s = declararEn(ambitoDeClase(), id, Simbolo.METODO,
                               tipoDeRetorno, linea, columna);
        s.setParametros(parametros);
        return s;
    }

    /* ==============================================================
       CLASES Y MIEMBROS
       ============================================================== */

    /** true si el nombre corresponde a una clase declarada. */
    public boolean esUnaClase(String nombre) {
        if (nombre == null) { return false; }
        for (int i = 0; i < todos.size(); i++) {
            Simbolo s = (Simbolo) todos.get(i);
            if (Simbolo.CLASE.equals(s.getTipoSimbolo())
                && nombre.equals(s.getId())) { return true; }
        }
        return false;
    }

    /**
     * Busca un miembro dentro de una clase. Recorre el historial y no la
     * pila, porque el ambito de la clase puede estar ya cerrado: un
     * acceso de la forma  objeto.miembro  suele ocurrir despues.
     */
    public Simbolo buscarMiembro(String clase, String miembro) {
        for (int i = 0; i < todos.size(); i++) {
            Simbolo s = (Simbolo) todos.get(i);
            if (clase.equals(s.getAmbito()) && miembro.equals(s.getId())) {
                return s;
            }
        }
        return null;
    }

    /** true si el punto que se analiza esta dentro de esa clase. */
    public boolean estamosDentroDeLaClase(String nombre) {
        for (int i = 0; i < ambitos.size(); i++) {
            Ambito a = (Ambito) ambitos.get(i);
            if (CLASE.equals(a.clase) && a.nombre.equals(nombre)) { return true; }
        }
        return false;
    }

    /** Alta de la clase: va al ambito global. */
    public Simbolo declararClase(String id, int linea, int columna) {
        return declararEn((Ambito) ambitos.get(0), id, Simbolo.CLASE,
                          ManejadorTipos.VACIO, linea, columna);
    }

    private Simbolo declararEn(Ambito destino, String id, String tiposimbolo,
                               String tipodato, int linea, int columna) {

        nomenclatura.validar(id, tiposimbolo, linea, columna);

        if (destino.simbolos.containsKey(id)) {
            Simbolo previo = (Simbolo) destino.simbolos.get(id);
            manejador.reportar(
                "el identificador '" + id + "' ya ha sido declarado en el"
                + " ambito '" + destino.nombre + "' (linea " + previo.getLinea()
                + ")", linea, columna);
            return previo;
        }

        Simbolo s = new Simbolo(id, tiposimbolo, tipodato, destino.nombre,
                                linea, columna, "IDENTIFICADOR");
        destino.simbolos.put(id, s);
        todos.add(s);
        return s;
    }

    /* ==============================================================
       REGLAS DE INICIALIZACION
       ============================================================== */

    /**
     * Comprueba la regla de inicializacion que corresponde a la categoria:
     *
     *   Variable local    obligatoria
     *   Constante         obligatoria
     *   Atributo          opcional
     *   Parametro         recibe su valor en la llamada
     */
    public void verificarInicializacion(Simbolo s, boolean traeValor,
                                        int linea, int columna) {
        if (traeValor) { return; }

        /* Una variable GLOBAL queda exenta: C++ las inicializa a cero
           automaticamente, mientras que una local contiene basura. Esa
           diferencia del lenguaje es justo la razon de la regla. */
        if (GLOBAL.equals(s.getAmbito())) { return; }

        if (Simbolo.VARIABLE.equals(s.getTipoSimbolo())) {
            manejador.reportar(
                "la variable local '" + s.getId() + "' debe ser inicializada"
                + " en su declaracion", linea, columna);

        } else if (Simbolo.CONSTANTE.equals(s.getTipoSimbolo())) {
            manejador.reportar(
                "la constante '" + s.getId() + "' requiere una asignacion"
                + " obligatoria de valor inicial", linea, columna);
        }
        /* Un atributo sin valor es legal: su casilla queda en "null". */
    }

    /* ==============================================================
       CONSULTA
       ============================================================== */

    /** Busca del ambito mas interno al mas externo. null si no existe. */
    public Simbolo buscar(String id) {
        for (int i = ambitos.size() - 1; i >= 0; i--) {
            Ambito a = (Ambito) ambitos.get(i);
            Simbolo s = (Simbolo) a.simbolos.get(id);
            if (s != null) { return s; }
        }
        return null;
    }

    /**
     * Consulta usada cuando un identificador aparece como OPERANDO de una
     * expresion. Si no esta declarado reporta el error y devuelve el tipo
     * invalido, que se propaga sin generar errores en cascada.
     */
    public String tipoDelOperando(String id, int linea, int columna) {
        Simbolo s = buscar(id);
        if (s == null) {
            manejador.reportar(
                "el identificador '" + id + "' NO ha sido declarado en el"
                + " ambito '" + ambitoActual() + "'", linea, columna);
            return ManejadorTipos.ERROR;
        }
        if (Simbolo.METODO.equals(s.getTipoSimbolo())) {
            manejador.reportar(
                "'" + id + "' es un metodo y no puede usarse como operando;"
                + " faltan los parentesis de la llamada", linea, columna);
            return ManejadorTipos.ERROR;
        }
        return s.getTipoDato();
    }

    /**
     * Comprueba que el destino de una asignacion exista y sea modificable.
     * Devuelve el simbolo si la asignacion puede realizarse, null si no.
     */
    public Simbolo destinoDeAsignacion(String id, int linea, int columna) {
        Simbolo s = buscar(id);

        if (s == null) {
            manejador.reportar(
                "el identificador '" + id + "' NO ha sido declarado en el"
                + " ambito '" + ambitoActual() + "'", linea, columna);
            return null;
        }

        if (s.esInmutable()) {
            manejador.reportar(
                "no se puede reasignar un valor a la constante '" + id + "'",
                linea, columna);
            return null;
        }

        if (Simbolo.METODO.equals(s.getTipoSimbolo())) {
            manejador.reportar(
                "'" + id + "' es un metodo y no admite asignacion",
                linea, columna);
            return null;
        }

        return s;
    }

    /* ==============================================================
       REPORTE
       ============================================================== */

    public int totalDeSimbolos() { return todos.size(); }

    private static final String REGLA =
        "===================================================================================================================================";
    private static final String GUIONES =
        "-----------------------------------------------------------------------------------------------------------------------------------";

    /** Imprime la tabla completa al terminar el analisis. */
    public void imprimirTabla() {

        ArrayList ordenados = new ArrayList(todos);
        Collections.sort(ordenados, new Comparator() {
            public int compare(Object a, Object b) {
                Simbolo x = (Simbolo) a;
                Simbolo y = (Simbolo) b;
                if (x.getLinea() != y.getLinea()) {
                    return x.getLinea() - y.getLinea();
                }
                return x.getColumna() - y.getColumna();
            }
        });

        System.out.println();
        System.out.println(REGLA);
        System.out.println("                                             TABLA DE SIMBOLOS");
        System.out.println(REGLA);
        System.out.println(String.format("%-18s %-13s %-11s %-16s %-17s %-10s %-7s %-9s %s",
                                         "ID", "Categoria", "Tipo Dato", "Valor",
                                         "Ambito", "Visibilidad", "Linea", "Columna",
                                         "Token Originador"));
        System.out.println(GUIONES);

        if (ordenados.isEmpty()) {
            System.out.println("  (no se registro ningun identificador)");
        }
        for (int i = 0; i < ordenados.size(); i++) {
            System.out.println(((Simbolo) ordenados.get(i)).comoFila());
        }

        System.out.println(REGLA);
        System.out.println("Total de simbolos registrados: " + ordenados.size());

        imprimirFirmas(ordenados);
    }

    /**
     * Las firmas completas de los metodos. Van en un bloque aparte porque
     * la lista de parametros no cabe como una columna mas de la tabla, y
     * el enunciado pide que la tabla guarde los parametros de cada metodo.
     */
    private void imprimirFirmas(ArrayList ordenados) {
        ArrayList metodos = new ArrayList();
        for (int i = 0; i < ordenados.size(); i++) {
            Simbolo s = (Simbolo) ordenados.get(i);
            if (Simbolo.METODO.equals(s.getTipoSimbolo())) { metodos.add(s); }
        }
        if (metodos.isEmpty()) { return; }

        System.out.println();
        System.out.println(" FIRMAS DE LOS METODOS DECLARADOS");
        System.out.println(" " + GUIONES.substring(0, 90));
        System.out.println(String.format(" %-18s %-12s %s",
                                         "AMBITO", "VISIBILIDAD", "FIRMA"));
        for (int i = 0; i < metodos.size(); i++) {
            Simbolo m = (Simbolo) metodos.get(i);
            System.out.println(String.format(" %-18s %-12s %s",
                                             m.getAmbito(),
                                             m.getVisibilidad().toLowerCase(),
                                             m.getFirma()));
        }
    }

    /** Mantiene el nombre anterior para no romper el codigo existente. */
    public void imprimirResumen() { imprimirTabla(); }
}
