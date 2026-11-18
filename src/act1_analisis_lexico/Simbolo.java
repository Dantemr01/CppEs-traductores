package act1_analisis_lexico;

/**
 * Una entrada de la TABLA DE SIMBOLOS.
 *
 * Representa un identificador declarado en el programa fuente junto con todo
 * lo que el compilador necesita saber de el. Los campos son los que exige el
 * enunciado de la practica:
 *
 *   id          nombre o lexema del identificador
 *   tiposimbolo categoria: CLASE, METODO, VARIABLE, ATRIBUTO, CONSTANTE
 *   tipodato    tipo asociado: entero, flotante, booleano, cadena, vacio...
 *   valor       valor asignado, o "null" / "N/A" cuando no aplica
 *   ambito      alcance donde se declaro: GLOBAL, nombre de clase o de metodo
 *   linea       linea del archivo fuente donde se detecto
 *   columna     columna donde inicia el token
 *   token       token que origino la insercion (IDENTIFICADOR)
 */
public class Simbolo {

    /* ---- Categorias de identificador (tiposimbolo) ---- */
    public static final String CLASE     = "CLASE";
    public static final String METODO    = "METODO";
    public static final String VARIABLE  = "VARIABLE";
    public static final String ATRIBUTO  = "ATRIBUTO";
    public static final String CONSTANTE = "CONSTANTE";

    /* Categoria interna. El enunciado no la lista entre las cinco, de modo
       que en la tabla se muestra como VARIABLE con la marca "(Parametro)".
       Se conserva aparte porque permite dar mensajes de error precisos:
       "no se puede reasignar el parametro x" en lugar de "la variable x". */
    public static final String PARAMETRO = "PARAMETRO";

    /* ---- Valores convencionales del campo valor ---- */
    public static final String SIN_VALOR       = "null";
    public static final String NO_APLICA       = "N/A";
    public static final String ES_PARAMETRO    = "N/A (Parametro)";

    /* ---- Modificadores de acceso ---- */
    public static final String PUBLICO   = "PUBLICO";
    public static final String PRIVADO   = "PRIVADO";
    public static final String PROTEGIDO = "PROTEGIDO";

    private final String id;
    private final String tiposimbolo;
    private final String tipodato;
    private final int    linea;
    private final int    columna;
    private final String token;

    private String  ambito;
    private String  valor;
    private boolean inicializado = false;

    /* Solo para los metodos: la lista de Parametro que declara. Guardar
       los tipos y no solo la cantidad es lo que permite verificar la
       compatibilidad de cada argumento de una llamada. */
    private java.util.ArrayList parametros = new java.util.ArrayList();

    /* Modificador de acceso. Sin modificador explicito se asume PUBLICO. */
    private String visibilidad = PUBLICO;

    public Simbolo(String id, String tiposimbolo, String tipodato,
                   String ambito, int linea, int columna, String token) {
        this.id          = id;
        this.tiposimbolo = tiposimbolo;
        this.tipodato    = tipodato;
        this.ambito      = ambito;
        this.linea       = linea;
        this.columna     = columna;
        this.token       = token;
        this.valor       = valorPorOmision(tiposimbolo);
    }

    /**
     * Que se muestra en la columna 'valor' cuando no hay un valor asignado.
     * Una clase o un metodo no almacenan nada, de modo que para ellos la
     * casilla no aplica; un atributo sin inicializar si tiene una casilla,
     * y esta vacia.
     */
    private static String valorPorOmision(String categoria) {
        if (CLASE.equals(categoria) || METODO.equals(categoria)) {
            return NO_APLICA;
        }
        if (PARAMETRO.equals(categoria)) {
            return ES_PARAMETRO;
        }
        return SIN_VALOR;
    }

    /* ==============================================================
       ACCESO
       ============================================================== */

    public String getId()          { return id;          }
    public String getTipoSimbolo() { return tiposimbolo; }
    public String getTipoDato()    { return tipodato;    }
    public String getValor()       { return valor;       }
    public String getAmbito()      { return ambito;      }
    public int    getLinea()       { return linea;       }
    public int    getColumna()     { return columna;     }
    public String getToken()       { return token;       }

    /** Nombre del identificador. Alias de getId() para el codigo anterior. */
    public String getNombre()      { return id;          }
    /** Categoria. Alias de getTipoSimbolo() para el codigo anterior. */
    public String getCategoria()   { return tiposimbolo; }
    /** Tipo de dato. Alias de getTipoDato() para el codigo anterior. */
    public String getTipo()        { return tipodato;    }

    public void setAmbito(String a) { ambito = a; }

    public void setValor(String v) {
        valor = (v == null || v.length() == 0) ? SIN_VALOR : v;
        inicializado = true;
    }

    public boolean estaInicializado()         { return inicializado; }
    public void    setInicializado(boolean b) { inicializado = b;    }

    public java.util.ArrayList getParametros() { return parametros; }

    public void setParametros(java.util.ArrayList ps) {
        parametros = (ps == null) ? new java.util.ArrayList() : ps;
    }

    public int getNumeroDeParametros() { return parametros.size(); }

    public String getVisibilidad()          { return visibilidad; }
    public void   setVisibilidad(String v)  {
        if (v != null && v.length() > 0) { visibilidad = v; }
    }

    /** true si el miembro puede usarse desde fuera de su clase. */
    public boolean esAccesibleDesdeFuera() {
        return PUBLICO.equals(visibilidad);
    }

    /**
     * La restriccion de inmutabilidad: una constante puede leerse dentro de
     * una expresion, pero no admite reasignacion.
     */
    public boolean esInmutable() {
        return CONSTANTE.equals(tiposimbolo);
    }

    public boolean esParametro() {
        return PARAMETRO.equals(tiposimbolo);
    }

    /**
     * Categoria tal como debe aparecer en la tabla. El enunciado define cinco
     * categorias y el parametro no es una de ellas: se reporta como VARIABLE,
     * y la marca "(Parametro)" viaja en la columna del valor.
     */
    public String categoriaParaLaTabla() {
        return esParametro() ? VARIABLE : tiposimbolo;
    }

    /** Fila de la tabla, con las columnas alineadas. */
    public String comoFila() {
        return String.format("%-18s %-13s %-11s %-16s %-17s %-10s %-7d %-9d %s",
                             recortar(id, 18),
                             categoriaParaLaTabla(),
                             recortar(tipodato, 11),
                             recortar(valor, 16),
                             recortar(ambito, 17),
                             visibilidadParaLaTabla(),
                             linea,
                             columna,
                             token);
    }

    /**
     * La visibilidad solo tiene sentido en los miembros de una clase. En
     * una variable local o un parametro no aplica, y mostrar PUBLICO ahi
     * sugeriria una propiedad que el lenguaje no les da.
     */
    private String visibilidadParaLaTabla() {
        if (ATRIBUTO.equals(tiposimbolo)
         || METODO.equals(tiposimbolo)
         || (CONSTANTE.equals(tiposimbolo) && !GLOBAL.equals(ambito))) {
            return visibilidad.toLowerCase();
        }
        return NO_APLICA;
    }

    private static final String GLOBAL = "GLOBAL";

    /** "entero sumar(entero a, entero b)", para el reporte de firmas. */
    public String getFirma() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipodato).append(' ').append(id).append('(');
        for (int i = 0; i < parametros.size(); i++) {
            if (i > 0) { sb.append(", "); }
            sb.append(parametros.get(i));
        }
        return sb.append(')').toString();
    }

    private static String recortar(String s, int n) {
        if (s == null)       { return "";                      }
        if (s.length() <= n) { return s;                       }
        return s.substring(0, n - 1) + ".";
    }
}
