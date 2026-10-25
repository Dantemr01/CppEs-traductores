package act1_analisis_lexico;

/**
 * MANEJADOR DE TIPOS  -  nucleo del analisis semantico
 *
 * Concentra las reglas de compatibilidad del lenguaje C++Es y el reporte de
 * los errores semanticos. Las acciones de la gramatica no deciden nada por su
 * cuenta: calculan el tipo de cada subexpresion llamando a esta clase, que es
 * quien aplica la regla y, si no se cumple, emite el diagnostico.
 *
 * ---------------------------------------------------------------------------
 * POSTURA DEL LENGUAJE: C++Es ES ESTATICAMENTE TIPADO
 * ---------------------------------------------------------------------------
 * C++Es hereda de C++ el tipado estatico y fuerte en la declaracion:
 *
 *   - Declarar exige indicar el tipo:  entero edad;   no existe  var edad;
 *   - El tipo queda ligado al IDENTIFICADOR, no al valor, y no cambia durante
 *     toda la vida del programa.
 *   - Toda asignacion se verifica en tiempo de compilacion contra el tipo
 *     declarado, no en tiempo de ejecucion.
 *
 * En un lenguaje dinamicamente tipado (Python, JavaScript) el tipo viaja con
 * el valor: la misma variable puede guardar un entero y despues una cadena, y
 * la comprobacion se hace al ejecutar. Esa diferencia es la que obliga a que
 * nuestra Tabla de Simbolos guarde un campo 'tipo' por identificador: en un
 * lenguaje no tipado ese campo cambiaria en cada asignacion, o directamente
 * no existiria en la tabla.
 *
 * ---------------------------------------------------------------------------
 * JERARQUIA DE PROMOCION NUMERICA
 * ---------------------------------------------------------------------------
 *       caracter  <  entero  <  flotante  <  doble
 *
 * El resultado de una operacion aritmetica es el mas amplio de los dos
 * operandos, con un minimo de 'entero': igual que en C++, 'caracter' se
 * promueve a 'entero' antes de operar.
 */
public class ManejadorTipos {

    /* ==============================================================
       NOMBRES DE TIPO
       ============================================================== */
    public static final String ENTERO   = "entero";
    public static final String FLOTANTE = "flotante";
    public static final String DOBLE    = "doble";
    public static final String CARACTER = "caracter";
    public static final String BOOLEANO = "booleano";
    public static final String CADENA   = "cadena";
    public static final String VACIO    = "vacio";
    public static final String NULO     = "nulo";

    /** Tipo invalido. Se propaga en silencio para no encadenar errores. */
    public static final String ERROR    = "<error>";

    /** Tipo de un metodo todavia no declarado. No genera diagnosticos. */
    public static final String INDETERMINADO = "<indeterminado>";

    private int erroresSemanticos = 0;

    public int getErroresSemanticos() { return erroresSemanticos; }
    public boolean huboErrores()      { return erroresSemanticos > 0; }

    /* ==============================================================
       REPORTE DE ERRORES SEMANTICOS
       ============================================================== */

    public void reportar(String descripcion, int linea, int columna) {
        erroresSemanticos++;
        System.out.println("Error semantico en linea " + linea
                         + ", columna " + columna + ":");
        System.out.println("     " + descripcion + ".");
    }

    /* ==============================================================
       CLASIFICACION DE TIPOS
       ============================================================== */

    /** true si el tipo no debe producir mas diagnosticos. */
    private boolean silencioso(String t) {
        return ERROR.equals(t) || INDETERMINADO.equals(t) || t == null;
    }

    /** Tipos sobre los que la aritmetica tiene sentido. */
    public boolean esNumerico(String t) {
        return ENTERO.equals(t) || FLOTANTE.equals(t)
            || DOBLE.equals(t)  || CARACTER.equals(t);
    }

    /** Tipos enteros: los unicos validos para el operador modulo. */
    public boolean esIntegral(String t) {
        return ENTERO.equals(t) || CARACTER.equals(t);
    }

    /** Posicion en la jerarquia de promocion. */
    private int rango(String t) {
        if (CARACTER.equals(t)) { return 1; }
        if (ENTERO.equals(t))   { return 2; }
        if (FLOTANTE.equals(t)) { return 3; }
        if (DOBLE.equals(t))    { return 4; }
        return 0;
    }

    private String porRango(int r) {
        switch (r) {
            case 1:  return CARACTER;
            case 2:  return ENTERO;
            case 3:  return FLOTANTE;
            case 4:  return DOBLE;
            default: return ERROR;
        }
    }

    /**
     * Tipo resultante de operar dos numericos: el mas amplio de los dos, con
     * un minimo de 'entero' porque 'caracter' se promueve antes de operar.
     */
    private String promocionar(String a, String b) {
        int r = Math.max(rango(a), rango(b));
        return porRango(Math.max(r, 2));
    }

    /* ==============================================================
       A. OPERACIONES ARITMETICAS   +  -  *  /  %
       ============================================================== */

    public String aritmetica(String operador, Expresion a, Expresion b,
                             int linea, int columna) {

        String ta = a.getTipo();
        String tb = b.getTipo();

        /* Un operando ya invalido no vuelve a diagnosticarse. */
        if (silencioso(ta) || silencioso(tb)) { return ERROR; }

        /* Concatenacion de cadenas: unica excepcion admitida, y solo con '+'.
           C++ la permite entre objetos cadena, asi que C++Es la conserva. */
        if (CADENA.equals(ta) && CADENA.equals(tb)) {
            if ("+".equals(operador)) { return CADENA; }
            reportar("el operador aritmetico '" + operador
                   + "' no es aplicable a operandos de tipo 'cadena'"
                   + "; solo se admite '+' para concatenar",
                     linea, columna);
            return ERROR;
        }

        if (!esNumerico(ta) || !esNumerico(tb)) {
            reportar("el operador aritmetico '" + operador
                   + "' no es aplicable a los operandos de tipo '" + ta
                   + "' y '" + tb + "'",
                     linea, columna);
            return ERROR;
        }

        /* El modulo exige operandos enteros, igual que en C++. */
        if ("%".equals(operador) && (!esIntegral(ta) || !esIntegral(tb))) {
            reportar("el operador '%' requiere operandos enteros"
                   + " y se recibieron '" + ta + "' y '" + tb + "'",
                     linea, columna);
            return ERROR;
        }

        return promocionar(ta, tb);
    }

    /** Menos unario: -x */
    public String menosUnario(Expresion a, int linea, int columna) {
        String t = a.getTipo();
        if (silencioso(t)) { return ERROR; }
        if (!esNumerico(t)) {
            reportar("el operador '-' unario requiere un operando numerico"
                   + " y se recibio '" + t + "'", linea, columna);
            return ERROR;
        }
        return porRango(Math.max(rango(t), 2));
    }

    /* ==============================================================
       B. OPERACIONES RELACIONALES   <  >  <=  >=  ==  !=
       ============================================================== */

    /**
     * Los operadores de orden exigen operandos numericos.
     * El resultado de toda expresion relacional es siempre booleano.
     */
    public String relacionalDeOrden(String operador, Expresion a, Expresion b,
                                    int linea, int columna) {
        String ta = a.getTipo();
        String tb = b.getTipo();

        if (silencioso(ta) || silencioso(tb)) { return ERROR; }

        if (!esNumerico(ta) || !esNumerico(tb)) {
            reportar("el operador relacional '" + operador
                   + "' requiere operandos numericos"
                   + " y se recibieron '" + ta + "' y '" + tb + "'",
                     linea, columna);
            return ERROR;
        }
        return BOOLEANO;
    }

    /**
     * La igualdad admite ademas booleanos y cadenas, siempre que ambos
     * operandos sean del mismo grupo.
     */
    public String igualdad(String operador, Expresion a, Expresion b,
                           int linea, int columna) {
        String ta = a.getTipo();
        String tb = b.getTipo();

        if (silencioso(ta) || silencioso(tb)) { return ERROR; }

        boolean comparables =
               (esNumerico(ta) && esNumerico(tb))
            || (BOOLEANO.equals(ta) && BOOLEANO.equals(tb))
            || (CADENA.equals(ta)   && CADENA.equals(tb))
            || (NULO.equals(ta)     || NULO.equals(tb));

        if (!comparables) {
            reportar("el operador '" + operador
                   + "' no puede comparar un operando de tipo '" + ta
                   + "' con uno de tipo '" + tb + "'",
                     linea, columna);
            return ERROR;
        }
        return BOOLEANO;
    }

    /* ==============================================================
       C. OPERACIONES LOGICAS   &&  ||  !
       ============================================================== */

    public String logica(String operador, Expresion a, Expresion b,
                         int linea, int columna) {
        String ta = a.getTipo();
        String tb = b.getTipo();

        if (silencioso(ta) || silencioso(tb)) { return ERROR; }

        if (!BOOLEANO.equals(ta) || !BOOLEANO.equals(tb)) {
            reportar("el operador logico '" + operador
                   + "' requiere operandos de tipo 'booleano'"
                   + " y se recibieron '" + ta + "' y '" + tb + "'",
                     linea, columna);
            return ERROR;
        }
        return BOOLEANO;
    }

    public String negacion(Expresion a, int linea, int columna) {
        String t = a.getTipo();
        if (silencioso(t)) { return ERROR; }
        if (!BOOLEANO.equals(t)) {
            reportar("el operador logico '!' requiere un operando de tipo"
                   + " 'booleano' y se recibio '" + t + "'", linea, columna);
            return ERROR;
        }
        return BOOLEANO;
    }

    /* ==============================================================
       D. COMPATIBILIDAD EN ASIGNACIONES
       ============================================================== */

    /**
     * Regla de asignabilidad de C++Es.
     *
     * Se admite la conversion ampliadora (caracter -> entero -> flotante ->
     * doble), porque no pierde informacion. Se RECHAZA la estrechadora
     * (doble -> entero), que en C++ compila con advertencia pero que aqui se
     * trata como error: en un compilador didactico es preferible exigir una
     * conversion explicita a permitir una perdida silenciosa de precision.
     */
    public boolean esAsignable(String destino, String origen) {

        if (destino == null || origen == null)      { return true; }
        if (destino.equals(origen))                 { return true; }

        /* Un tipo ya invalidado no vuelve a diagnosticarse. */
        if (silencioso(origen) || silencioso(destino)) { return true; }

        /* nulo se admite donde hay una referencia, no en los numericos. */
        if (NULO.equals(origen)) {
            return CADENA.equals(destino) || !esNumerico(destino);
        }

        /* Conversion ampliadora entre numericos. */
        if (esNumerico(destino) && esNumerico(origen)) {
            return rango(origen) <= rango(destino);
        }

        return false;
    }

    /**
     * Verificacion de una asignacion o de una inicializacion.
     * 'que' describe al destino para el mensaje: "la variable", "el atributo",
     * "la constante".
     */
    public void verificarAsignacion(String que, String nombre, String tipoDestino,
                                    Expresion valor, int linea, int columna) {

        String tipoOrigen = valor.getTipo();

        if (esAsignable(tipoDestino, tipoOrigen)) { return; }

        reportar("incompatibilidad de tipos: no se puede asignar un valor de"
               + " tipo '" + tipoOrigen + "' a " + que + " '" + nombre
               + "' de tipo '" + tipoDestino + "'",
                 linea, columna);
    }

    /* ==============================================================
       E. CONDICIONES DE LAS ESTRUCTURAS DE CONTROL
       ============================================================== */

    /**
     * La condicion de 'si', 'mientras', 'hacer-mientras' y 'para' debe ser
     * booleana. C++ aceptaria un entero por conversion implicita a bool;
     * C++Es lo rechaza para que el diagnostico sea util, que es justo el caso
     * que el enunciado pide detectar.
     */
    public void verificarCondicion(String estructura, Expresion condicion,
                                   int linea, int columna) {
        String t = condicion.getTipo();
        if (silencioso(t)) { return; }

        if (!BOOLEANO.equals(t)) {
            reportar("la expresion dentro de la condicion '" + estructura
                   + "' debe ser de tipo 'booleano', se encontro '" + t + "'",
                     linea, columna);
        }
    }

    /* ==============================================================
       F. TIPO DE LOS LITERALES
       ============================================================== */

    /**
     * Un literal decimal sin sufijo se considera 'flotante' y no 'doble',
     * de modo que  flotante factor = 2.5;  sea valido sin exigir el sufijo
     * 'f' que pediria C++. Como flotante se amplia a doble, la declaracion
     * doble d = 2.5;  tambien sigue siendo valida.
     */
    public String tipoDeLiteral(int codigoDeToken) {
        if (codigoDeToken == sym.LIT_ENTERO
         || codigoDeToken == sym.LIT_ENTERO_HEX
         || codigoDeToken == sym.LIT_ENTERO_OCT
         || codigoDeToken == sym.LIT_ENTERO_BIN) { return ENTERO;   }
        if (codigoDeToken == sym.LIT_FLOTANTE)   { return FLOTANTE; }
        if (codigoDeToken == sym.LIT_CARACTER)   { return CARACTER; }
        if (codigoDeToken == sym.LIT_CADENA)     { return CADENA;   }
        if (codigoDeToken == sym.VERDADERO
         || codigoDeToken == sym.FALSO)          { return BOOLEANO; }
        if (codigoDeToken == sym.NULO)           { return NULO;     }
        return ERROR;
    }

    /* ==============================================================
       REPORTE FINAL
       ============================================================== */

    public void imprimirResumen() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" RESULTADO DEL ANALISIS SEMANTICO");
        System.out.println("==================================================");
        if (erroresSemanticos == 0) {
            System.out.println(" No se detectaron errores de tipos.");
            System.out.println(" Todas las expresiones, asignaciones y condiciones");
            System.out.println(" son compatibles con los tipos declarados.");
        } else {
            System.out.println(" Errores semanticos detectados: " + erroresSemanticos);
            System.out.println(" El programa es sintacticamente correcto en las partes");
            System.out.println(" senaladas, pero viola las reglas de tipos del lenguaje.");
        }
    }
}
