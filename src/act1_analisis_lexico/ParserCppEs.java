package act1_analisis_lexico;

import java_cup.runtime.*;
import java_cup.runtime.XMLElement;

@SuppressWarnings({"rawtypes"})
public class ParserCppEs extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return sym.class;
}

  @Deprecated
  public ParserCppEs() {super();}

  @Deprecated
  public ParserCppEs(java_cup.runtime.Scanner s) {super(s);}

  public ParserCppEs(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\024\000\002\002\004\000\002\002\005\000\002\003" +
    "\004\000\002\003\002\000\002\004\004\000\002\004\005" +
    "\000\002\004\004\000\002\005\003\000\002\005\002\000" +
    "\002\006\006\000\002\006\005\000\002\006\006\000\002" +
    "\011\003\000\002\011\005\000\002\007\006\000\002\007" +
    "\005\000\002\007\005\000\002\007\005\000\002\010\004" +
    "\000\002\010\004" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\040\000\010\004\ufffe\044\ufffe\064\ufffe\001\002\000" +
    "\004\002\042\001\002\000\010\004\007\044\ufff9\064\010" +
    "\001\002\000\004\044\027\001\002\000\006\003\024\005" +
    "\023\001\002\000\004\063\013\001\002\000\010\004\uffff" +
    "\044\uffff\064\uffff\001\002\000\004\044\ufffa\001\002\000" +
    "\006\003\014\077\016\001\002\000\004\157\022\001\002" +
    "\000\010\044\ufff7\157\020\164\017\001\002\000\010\044" +
    "\ufff5\157\ufff5\164\ufff5\001\002\000\004\077\021\001\002" +
    "\000\004\044\ufff8\001\002\000\010\044\ufff4\157\ufff4\164" +
    "\ufff4\001\002\000\004\044\ufff6\001\002\000\012\004\ufffd" +
    "\044\ufffd\064\ufffd\157\025\001\002\000\010\004\ufffb\044" +
    "\ufffb\064\ufffb\001\002\000\010\004\ufffc\044\ufffc\064\ufffc" +
    "\001\002\000\004\002\000\001\002\000\010\003\030\077" +
    "\031\154\032\001\002\000\004\157\041\001\002\000\004" +
    "\154\032\001\002\000\006\003\035\155\036\001\002\000" +
    "\004\157\034\001\002\000\004\002\ufff1\001\002\000\006" +
    "\002\uffee\157\uffee\001\002\000\006\002\uffef\157\uffef\001" +
    "\002\000\006\002\ufff2\157\040\001\002\000\004\002\ufff3" +
    "\001\002\000\004\002\ufff0\001\002\000\004\002\001\001" +
    "\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\040\000\006\002\003\003\004\001\001\000\002\001" +
    "\001\000\010\004\010\005\005\006\011\001\001\000\004" +
    "\007\025\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\004\011\014\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\010\032\001\001\000\002" +
    "\001\001\000\004\010\036\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$ParserCppEs$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$ParserCppEs$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$ParserCppEs$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}




    /* ---------- Contadores ---------- */
    private int reglasReconocidas   = 0;
    private int erroresRecuperables = 0;
    private int erroresFatales      = 0;
    private int recuperaciones      = 0;

    public int getReglasReconocidas()   { return reglasReconocidas;   }
    public int getErroresRecuperables() { return erroresRecuperables; }
    public int getErroresFatales()      { return erroresFatales;      }
    public int getRecuperaciones()      { return recuperaciones;      }
    public boolean huboErrores() { return erroresRecuperables + erroresFatales > 0; }

    /* Descripcion especifica para el proximo report_error. Si es null, se
       usa una descripcion generica deducida del token encontrado.        */
    private String descripcionPendiente = null;

    /* Ultimo token que provoco un error. Se guarda porque, cuando la
       recuperacion falla, CUP ya avanzo el flujo y el token que recibe
       report_fatal_error suele ser el fin de archivo en vez del token que
       realmente rompio la estructura.                                    */
    private Symbol ultimoTokenConError = null;

    /* ==============================================================
       1. RETROALIMENTACION POSITIVA
       ============================================================== */

    /* Se invoca desde la accion de cada produccion cuando esta se reduce,
       es decir, en el momento exacto en que la regla queda reconocida.   */
    public void ok(String mensaje, int linea, int columna) {
        reglasReconocidas++;
        System.out.println("OK   " + mensaje
                         + "   [linea " + linea + ", columna " + columna + "]");
    }

    public void finalizado() {
        System.out.println("OK   Analisis sintactico finalizado correctamente");
    }

    /* ==============================================================
       2. ERROR RECUPERABLE  ->  report_error
       ============================================================== */

    /*
       CUP llama a report_error (a traves de syntax_error) en cuanto detecta
       un token que no encaja, ANTES de saber si podra recuperarse.
       El objeto info es el Symbol del token problematico, y de el se
       obtiene la linea (campo left), la columna (campo right), el lexema
       (campo value) y el tipo de token (campo sym).
    */
    /*
       Se sobrescribe syntax_error para que CUP no imprima su volcado en
       ingles ("instead expected token classes are ...") y todo el reporte
       salga por report_error con el formato pedido.
    */
    @Override
    public void syntax_error(Symbol cur_token) {
        report_error("", cur_token);
    }

    @Override
    public void report_error(String message, Object info) {

        if (!(info instanceof Symbol)) {
            System.out.println("Error sintactico: " + message);
            return;
        }

        Symbol s = (Symbol) info;
        erroresRecuperables++;
        ultimoTokenConError = s;

        String descripcion = (descripcionPendiente != null)
                           ? descripcionPendiente
                           : descripcionGenerica(s);
        descripcionPendiente = null;

        imprimir("Error sintactico", s, descripcion);
    }

    /*
       Permite reportar un error desde la accion de una produccion, cuando la
       gramatica acepto deliberadamente una forma incorrecta para poder dar un
       diagnostico preciso. Construye el Symbol y lo pasa por report_error,
       de modo que todos los errores salgan por el mismo camino.
    */
    public void reportarError(String descripcion, int linea, int columna,
                              int tipoToken, String lexema) {
        descripcionPendiente = descripcion;
        report_error("", new Symbol(tipoToken, linea, columna, lexema));
    }

    /* ==============================================================
       3. ERROR FATAL  ->  report_fatal_error
       ============================================================== */

    /*
       CUP llama a unrecovered_syntax_error cuando la recuperacion fallo,
       y esa rutina deriva en report_fatal_error. El error ya se habia
       contado como recuperable en report_error, asi que aqui se
       reclasifica antes de detener el analisis.
    */
    @Override
    public void report_fatal_error(String message, Object info) {

        if (erroresRecuperables > 0) {
            erroresRecuperables--;   /* se reclasifica: no era recuperable */
        }
        erroresFatales++;

        System.out.println("     >> No existe ninguna regla que permita resincronizar el analisis.");

        /* Se reporta sobre el token que realmente rompio la estructura, no
           sobre el punto al que CUP llego intentando recuperarse.         */
        Symbol culpable = (ultimoTokenConError != null)
                        ? ultimoTokenConError
                        : (info instanceof Symbol ? (Symbol) info : null);

        if (culpable != null) {
            imprimir("Error fatal", culpable,
                     "la estructura general del programa es invalida; el analisis termina aqui");
        } else {
            System.out.println("Error fatal: " + message);
        }

        done_parsing();
    }

    /* ==============================================================
       4. RECUPERACION
       ============================================================== */

    /* Se invoca desde las producciones que contienen el simbolo 'error',
       una vez que el analizador logro resincronizar.                     */
    public void recuperado(String queSeDescarto) {
        recuperaciones++;
        System.out.println("     >> Recuperacion: se descarto " + queSeDescarto
                         + " y el analisis continua.");
    }

    /* ==============================================================
       Utilidades de impresion
       ============================================================== */

    private void imprimir(String encabezado, Symbol s, String descripcion) {
        System.out.println(encabezado + " en linea " + s.left
                         + ", columna " + s.right
                         + ": se encontro '" + lexema(s) + "'");
        System.out.println("     Token       : " + TablaTokens.nombre(s.sym));
        System.out.println("     Descripcion : " + descripcion);
    }

    private String lexema(Symbol s) {
        if (s.sym == sym.EOF) { return "<fin de archivo>"; }
        return (s.value == null) ? TablaTokens.nombre(s.sym) : s.value.toString();
    }

    private String descripcionGenerica(Symbol s) {
        if (s.sym == sym.EOF) {
            return "el archivo termino antes de completar la estructura esperada";
        }
        if (s.sym == sym.INCLUIR) {
            return "las inclusiones deben aparecer antes de la clase principal";
        }
        if (s.sym == sym.USANDO) {
            return "el espacio de nombres debe declararse despues de las inclusiones y antes de la clase";
        }
        if (s.sym == sym.CLASE) {
            return "solo se admite una clase principal por archivo";
        }
        if (s.sym == sym.PUNTO_COMA) {
            return "falta la llave de cierre, o hay un punto y coma fuera de lugar";
        }
        if (s.sym == sym.IDENTIFICADOR) {
            return "se esperaba otra estructura: revise si falta un delimitador o una palabra reservada";
        }
        return "este token no puede aparecer en este punto de la estructura del archivo";
    }


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$ParserCppEs$actions {
  private final ParserCppEs parser;

  /** Constructor */
  CUP$ParserCppEs$actions(ParserCppEs parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$ParserCppEs$do_action_part00000000(
    int                        CUP$ParserCppEs$act_num,
    java_cup.runtime.lr_parser CUP$ParserCppEs$parser,
    java.util.Stack            CUP$ParserCppEs$stack,
    int                        CUP$ParserCppEs$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$ParserCppEs$result;

      /* select the action based on the action number */
      switch (CUP$ParserCppEs$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= archivo EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		RESULT = start_val;
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$ParserCppEs$parser.done_parsing();
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // archivo ::= lista_importaciones paquete_opcional clase_principal 
            {
              Object RESULT =null;
		 parser.finalizado(); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("archivo",0, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // lista_importaciones ::= lista_importaciones importacion 
            {
              Object RESULT =null;

              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("lista_importaciones",1, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // lista_importaciones ::= 
            {
              Object RESULT =null;

              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("lista_importaciones",1, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // importacion ::= INCLUIR RUTA_INCLUSION 
            {
              Object RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).left;
		int rright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).right;
		String r = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.peek()).value;
		 parser.ok("Importacion reconocida correctamente: #incluir " + r,
                             ileft, iright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("importacion",2, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // importacion ::= INCLUIR RUTA_INCLUSION PUNTO_COMA 
            {
              Object RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String r = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		int pleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).left;
		int pright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).right;
		String p = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.peek()).value;
		 parser.reportarError(
                       "la directiva #incluir no lleva punto y coma en C++Es",
                       pleft, pright, sym.PUNTO_COMA, ";");
                   parser.recuperado("el punto y coma sobrante");
                   parser.ok("Importacion reconocida correctamente: #incluir " + r,
                             ileft, iright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("importacion",2, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // importacion ::= INCLUIR error 
            {
              Object RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		 parser.recuperado("la inclusion sin ruta valida"); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("importacion",2, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // paquete_opcional ::= paquete 
            {
              Object RESULT =null;

              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("paquete_opcional",3, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // paquete_opcional ::= 
            {
              Object RESULT =null;

              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("paquete_opcional",3, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // paquete ::= USANDO ESPACIO_NOMBRES ubicacion PUNTO_COMA 
            {
              Object RESULT =null;
		int uleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).left;
		int uright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).right;
		String u = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).value;
		int ubleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int ubright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String ub = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		 parser.ok("Paquete reconocido correctamente: espacio de nombres " + ub,
                         uleft, uright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("paquete",4, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // paquete ::= USANDO ESPACIO_NOMBRES ubicacion 
            {
              Object RESULT =null;
		int uleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int uright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String u = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		int ubleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).left;
		int ubright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).right;
		String ub = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.peek()).value;
		 parser.reportarError(
                   "falta el punto y coma al final de la declaracion del espacio de nombres",
                   ubleft, ubright, sym.IDENTIFICADOR, ub);
               parser.recuperado("la falta del punto y coma");
               parser.ok("Paquete reconocido correctamente: espacio de nombres " + ub,
                         uleft, uright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("paquete",4, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // paquete ::= USANDO ESPACIO_NOMBRES error PUNTO_COMA 
            {
              Object RESULT =null;
		int uleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).left;
		int uright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).right;
		String u = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).value;
		 parser.recuperado("la ubicacion mal formada del espacio de nombres"); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("paquete",4, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // ubicacion ::= IDENTIFICADOR 
            {
              String RESULT =null;
		int idleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).left;
		int idright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.peek()).value;
		 RESULT = id;
                 parser.ok("Ubicacion reconocida correctamente: " + id, idleft, idright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("ubicacion",7, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // ubicacion ::= ubicacion RESOLUCION IDENTIFICADOR 
            {
              String RESULT =null;
		int uleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int uright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String u = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).left;
		int idright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.peek()).value;
		 RESULT = u + "::" + id;
                 parser.ok("Ubicacion reconocida correctamente: " + RESULT, uleft, uright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("ubicacion",7, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // clase_principal ::= CLASE IDENTIFICADOR cuerpo_clase PUNTO_COMA 
            {
              Object RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		 parser.ok("Clase principal reconocida correctamente: " + id,
                                 cleft, cright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("clase_principal",5, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-3)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // clase_principal ::= CLASE IDENTIFICADOR cuerpo_clase 
            {
              Object RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		 parser.reportarError(
                           "falta el punto y coma despues de cerrar la clase",
                           cleft, cright, sym.CLASE, "clase");
                       parser.recuperado("la falta del punto y coma");
                       parser.ok("Clase principal reconocida correctamente: " + id,
                                 cleft, cright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("clase_principal",5, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // clase_principal ::= CLASE cuerpo_clase PUNTO_COMA 
            {
              Object RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		 parser.reportarError(
                           "falta el nombre de la clase despues de la palabra reservada clase",
                           cleft, cright, sym.CLASE, "clase");
                       parser.recuperado("la declaracion de clase sin nombre"); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("clase_principal",5, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // clase_principal ::= CLASE error PUNTO_COMA 
            {
              Object RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).right;
		String c = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)).value;
		 parser.recuperado("la declaracion de clase mal formada"); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("clase_principal",5, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-2)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // cuerpo_clase ::= LLAVE_IZQ LLAVE_DER 
            {
              Object RESULT =null;
		int aleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int aright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String a = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		 parser.ok("Estructura de llaves valida", aleft, aright); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("cuerpo_clase",6, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // cuerpo_clase ::= LLAVE_IZQ error 
            {
              Object RESULT =null;
		int aleft = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).left;
		int aright = ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).right;
		String a = (String)((java_cup.runtime.Symbol) CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)).value;
		 parser.recuperado("el cuerpo de clase sin llave de cierre"); 
              CUP$ParserCppEs$result = parser.getSymbolFactory().newSymbol("cuerpo_clase",6, ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.elementAt(CUP$ParserCppEs$top-1)), ((java_cup.runtime.Symbol)CUP$ParserCppEs$stack.peek()), RESULT);
            }
          return CUP$ParserCppEs$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$ParserCppEs$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$ParserCppEs$do_action(
    int                        CUP$ParserCppEs$act_num,
    java_cup.runtime.lr_parser CUP$ParserCppEs$parser,
    java.util.Stack            CUP$ParserCppEs$stack,
    int                        CUP$ParserCppEs$top)
    throws java.lang.Exception
    {
              return CUP$ParserCppEs$do_action_part00000000(
                               CUP$ParserCppEs$act_num,
                               CUP$ParserCppEs$parser,
                               CUP$ParserCppEs$stack,
                               CUP$ParserCppEs$top);
    }
}

}
