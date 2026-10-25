package act1_analisis_lexico;

/**
 * Una entrada de la Tabla de Simbolos.
 *
 * Representa un identificador declarado en el programa fuente junto con todo
 * lo que el analizador semantico necesita saber de el: que clase de
 * identificador es, de que tipo, donde se declaro y si puede reasignarse.
 */
public class Simbolo {

    /* ---- Categorias de identificador ---- */
    public static final String VARIABLE  = "variable";
    public static final String CONSTANTE = "constante";
    public static final String ATRIBUTO  = "atributo";
    public static final String PARAMETRO = "parametro";
    public static final String METODO    = "metodo";

    private final String nombre;
    private final String categoria;
    private final String tipo;
    private final int    linea;
    private final int    columna;
    private final String ambito;

    /* Solo para los metodos: cuantos parametros declara. */
    private int numeroDeParametros = 0;

    /* true cuando la declaracion incluyo un valor inicial. */
    private boolean inicializado = false;

    public Simbolo(String nombre, String categoria, String tipo,
                   int linea, int columna, String ambito) {
        this.nombre    = nombre;
        this.categoria = categoria;
        this.tipo      = tipo;
        this.linea     = linea;
        this.columna   = columna;
        this.ambito    = ambito;
    }

    public String getNombre()    { return nombre;    }
    public String getCategoria() { return categoria; }
    public String getTipo()      { return tipo;      }
    public int    getLinea()     { return linea;     }
    public int    getColumna()   { return columna;   }
    public String getAmbito()    { return ambito;    }

    public int  getNumeroDeParametros()         { return numeroDeParametros; }
    public void setNumeroDeParametros(int n)    { numeroDeParametros = n;    }

    public boolean estaInicializado()           { return inicializado;       }
    public void    setInicializado(boolean b)   { inicializado = b;          }

    /**
     * La restriccion de inmutabilidad del enunciado: una constante puede
     * leerse dentro de una expresion, pero no admite reasignacion.
     */
    public boolean esInmutable() {
        return CONSTANTE.equals(categoria);
    }

    /** Fila de la tabla, con las columnas alineadas para el reporte. */
    public String comoFila() {
        return String.format("  %-18s %-11s %-10s %-9s %-8s %s",
                             nombre,
                             categoria,
                             tipo,
                             "linea " + linea,
                             "col " + columna,
                             METODO.equals(categoria)
                                 ? numeroDeParametros + " parametro(s)"
                                 : (inicializado ? "inicializado" : "sin valor"));
    }
}
