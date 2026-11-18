package act1_analisis_lexico;

import java.util.ArrayList;

/**
 * El encabezado de un metodo, ya reconocido y registrado.
 *
 * Existe porque la regla 'metodo' se reduce DESPUES de su cuerpo, y para
 * entonces ya es tarde para dos cosas: dar de alta el metodo en la tabla
 * de simbolos antes de que el cuerpo lo use, y ponerle nombre al ambito
 * local donde viven sus parametros y variables.
 *
 * Separando el encabezado en su propia regla, esa parte se reduce antes
 * del cuerpo y este objeto lleva al final lo que la regla 'metodo'
 * necesita para componer sus mensajes.
 */
public class EncabezadoMetodo {

    private final String    tipoDeRetorno;
    private final String    nombre;
    private final ArrayList parametros;
    private final int       linea;
    private final int       columna;

    public EncabezadoMetodo(String tipoDeRetorno, String nombre,
                            ArrayList parametros, int linea, int columna) {
        this.tipoDeRetorno = tipoDeRetorno;
        this.nombre        = nombre;
        this.parametros    = (parametros == null) ? new ArrayList() : parametros;
        this.linea         = linea;
        this.columna       = columna;
    }

    public String    getTipoDeRetorno() { return tipoDeRetorno; }
    public String    getNombre()        { return nombre;        }
    public ArrayList getParametros()    { return parametros;    }
    public int       getLinea()         { return linea;         }
    public int       getColumna()       { return columna;       }

    public int getNumeroDeParametros()  { return parametros.size(); }

    /**
     * true cuando el encabezado se reconocio a traves de una produccion de
     * error. La regla 'metodo' lo consulta para NO anunciar el metodo como
     * reconocido: su firma no es la que el programador escribio, y darla
     * por buena confundiria el reporte.
     */
    private boolean conError = false;

    public boolean tieneError()     { return conError; }
    public void    marcarConError() { conError = true; }

    /** "entero calcularPromedio(entero nota1, entero nota2)" */
    public String getFirma() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoDeRetorno).append(' ').append(nombre).append('(');
        for (int i = 0; i < parametros.size(); i++) {
            if (i > 0) { sb.append(", "); }
            sb.append(parametros.get(i));
        }
        return sb.append(')').toString();
    }

    public boolean devuelveVacio() {
        return ManejadorTipos.VACIO.equals(tipoDeRetorno);
    }
}
