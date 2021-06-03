/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;
import producciones.*;

/**
 *
 * @author snerd
 */
public class Codigo3Direcciones {
    private InitProgram program; //Objeto que guarda todas las sentencias del código fuente
    private String codigo3d = ""; //String que va a guardar el código
    private int temp; //Contador de los temporales que se van a usar
    private Vector<Vector<String>> tablaSimbolos = new Vector<Vector<String>>();
    private int ifcont = 0;
    private int forcont = 0;
    private int varcont = 0;
    private int assingcont = 0;
    private int arraycont = 0;
    private int marraycont = 0;
    
    
    //Entrada: Recibe un objeto de tipo InitProgram
    //Salida: Retorna un objeto de la clase Codigo3Direcciones
    //Objetivo: Ser el constructor de la clase
    public Codigo3Direcciones(InitProgram pProgram){
        program = pProgram;
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de limpiar el archivo en el que se va a escribir el código en 3 direcciones
    public void limpiarArchivo(){
        try {     
            FileWriter fichero = new FileWriter("Codigo3D.txt", false); 
            BufferedWriter writer = new BufferedWriter (fichero);
            writer.write("");
            writer.close();

          } catch (Exception ew) {
            ew.printStackTrace();
          }
    }

    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de escribir en un archivo de texto el código en 3 direcciones generado
    public void guardarCodigo3D()
    {
      try {    
        limpiarArchivo();
        FileWriter fichero = new FileWriter("Codigo3D.txt", true); 
        BufferedWriter writer = new BufferedWriter (fichero);
        writer.write(codigo3d);
        writer.close();

      } catch (Exception ew) {
        ew.printStackTrace();
      }
    }
 
    //Entrada: Identificador de la fila que se desea obtener
    //Salida: variable en la que está almacenada el identificador
    //Objetivo: Retornas la variable del identificador que se encuentra en la tabla de símbolos
    public String getTablaDeSimbolosData(String identificador){
        for(Vector<String> i: tablaSimbolos){
            if(i.elementAt(1).equals(identificador)) return i.elementAt(0);
        }
        return "";
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones del programa fuente y escribirlo en un archivo de texto  
    public void generarCodigo(){
       generarFuncionesScope(program.getFunctions().getFunctions());
       guardarCodigo3D();
    }
    
    
    //Entrada: Recibe un vector con objetos Function que son las funciones del código fuente
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones del scope de cada función 
    public void generarFuncionesScope(Vector<Function> funciones){
        for(Function tempFuncion: funciones){
            String nombreF = tempFuncion.getIdentifier().getName();
            codigo3d += "func begin " + nombreF + "\n";
            Vector<Parameters> parametros = tempFuncion.getParameterList().getParameters(); //Guarda los parámetros de la función en un vector
            codigo3d += "begin params\n";
            int cont_param = 0;
            for(Parameters temppam: parametros){
               String nombreP = nombreF + "_p" + cont_param;
               codigo3d += nombreP + " = " + temppam.getIdentifier().getName() + "\n";
               Vector<String> v1 = new Vector<String>();
               v1.add(nombreP);
               v1.add(temppam.getIdentifier().getName());
               tablaSimbolos.add(v1);
               cont_param += 1;
            }
            codigo3d += "end params\n";
            generarBloque(tempFuncion.getBlock().getSentences());
            codigo3d += "func end " + nombreF + "\n\n";
            ifcont = 0;
            forcont = 0;
            varcont = 0;
            assingcont = 0;
            arraycont = 0;
            marraycont = 0;
        }
    }
 
 
    //Entrada: Un objeto Sentence con las sentencias de código de un bloque de código
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones d¿e las sentencias de código de un bloque
    public void generarBloque(Sentences bloque){
        for(Sentence tempSentence: bloque.getSentences()){
            //Operacion
            if(tempSentence instanceof OperationSentence){
                OperationSentence declaracion = (OperationSentence)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                generarOperacion(op, "op");
            }
        }
    }
    
    //Entrada: Un objeto Operation
    //Salida: String con el resultado de la operacion
    //Objetivo: Generar el código de 3 direcciones de la operacion
    public String generarOperacion(Operation op, String identificador){
        String dato = "";
        if((op instanceof Plus)){
            Plus sentencia = (Plus)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " + " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Minus)){
            Minus sentencia = (Minus)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " - " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Multi)){
            Multi sentencia = (Multi)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " * " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Divide)){
            Divide sentencia = (Divide)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " / " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Module)){
            Module sentencia = (Module)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " % " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Power)){
            Power sentencia = (Power)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " ** " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Minor)){
            Minor sentencia = (Minor)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " < " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof MinorEqual)){
            MinorEqual sentencia = (MinorEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " =< " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Greater)){
            Greater sentencia = (Greater)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " > " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof GreaterEqual)){
            GreaterEqual sentencia = (GreaterEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " >= " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof EqualEqual)){
            EqualEqual sentencia = (EqualEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " == " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Different)){
            Different sentencia = (Different)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " != " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof MinusUnary)){
            MinusUnary sentencia = (MinusUnary)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + "-" + leftType + "\n";
            dato = var;
        }
        if((op instanceof And)){
            And sentencia = (And)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " & " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Or)){
            Or sentencia = (Or)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + leftType + " | " + rigthType + "\n";
            dato = var;
        }
        else if(op instanceof IntLiteral){
            IntLiteral sentencia = (IntLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
        }
        else if(op instanceof FloatLiteral){
            FloatLiteral sentencia = (FloatLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
        }
        else if(op instanceof StringLiteral){
            StringLiteral sentencia = (StringLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
        }
        else if(op instanceof CharLiteral){
            CharLiteral sentencia = (CharLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
        }
        else if(op instanceof BoolLiteral){
            BoolLiteral sentencia = (BoolLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
        }
        else if(op instanceof Identifier){
           Identifier identifier = (Identifier)op;
           String var = getTablaDeSimbolosData(identifier.getName());
           dato = var;
        }
        else if(op instanceof ArrayPositionOperation){
            ArrayPositionOperation sentencia = (ArrayPositionOperation)op;
            String valor = String.valueOf(sentencia.getIdentifier());
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + valor + "[" + String.valueOf(sentencia.getArrayPos()) + "]" + "\n";
            dato = var;
        }
        else if(op instanceof ArrayListAssigment){
            ArrayListAssigment sentencia = (ArrayListAssigment)op;
            Vector<Operation> arr = sentencia.getArrayList().getParameterList();
            dato = "err";
        }
        else if(op instanceof CallFunction){
           CallFunction sentencia = (CallFunction)op;
           String name = sentencia.getName();
           Vector<Operation> param = sentencia.getParameterList().getParameterList();
           int contador = 0;
           for(Operation op1: param){
               generarOperacion(op1, "p" + contador);
           }
           codigo3d += "call " + name + ", " + String.valueOf((contador + 1));
           String var = identificador + "_t" + temp;
           codigo3d += var + " = " + "return ";
           dato = var;
        }
        else if(op instanceof NullLiteral){
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + "null" + "\n";
            dato = var;
        }
        temp+=1;
        return dato;
    }
}
