package act1_analisis_lexico;

/**
 * Un parametro de la declaracion de un metodo.
 *
 * Hasta la practica anterior la lista de parametros era una lista de
 * CADENAS ("entero a"), que servia para componer el mensaje de la firma
 * pero no para nada mas. Para verificar que los argumentos de una llamada
 * sean compatibles hace falta el TIPO por separado, no el texto, asi que
 * cada parametro pasa a ser un objeto.
 *
 * Es la recomendacion explicita del enunciado: representar la informacion
 * semantica con clases auxiliares en lugar de depender de cadenas.
 */
public class Parametro {

    private final String tipo;
    private final String nombre;
    private final int    linea;
    private final int    columna;

    public Parametro(String tipo, String nombre, int linea, int columna) {
        this.tipo    = tipo;
        this.nombre  = nombre;
        this.linea   = linea;
        this.columna = columna;
    }

    public String getTipo()    { return tipo;    }
    public String getNombre()  { return nombre;  }
    public int    getLinea()   { return linea;   }
    public int    getColumna() { return columna; }

    /** "entero a", que es como aparece en la firma del metodo. */
    @Override
    public String toString() {
        return (nombre == null) ? tipo : (tipo + " " + nombre);
    }
}
