package act1_analisis_lexico;

/**
 * Un identificador recogido por la regla 'lista_identificadores', junto con
 * la posicion exacta en que aparecio.
 *
 * Hace falta porque una sola declaracion puede dar de alta varios nombres
 *
 *     entero contador, total, indice;
 *
 * y cada uno debe registrarse en la Tabla de Simbolos con SU linea y SU
 * columna, no con las de la declaracion completa. Sin esto, un error sobre
 * 'indice' se reportaria en la columna de 'entero'.
 */
public class Identificador {

    private final String nombre;
    private final int    linea;
    private final int    columna;

    public Identificador(String nombre, int linea, int columna) {
        this.nombre  = nombre;
        this.linea   = linea;
        this.columna = columna;
    }

    public String getNombre()  { return nombre;  }
    public int    getLinea()   { return linea;   }
    public int    getColumna() { return columna; }

    @Override
    public String toString() { return nombre; }
}
