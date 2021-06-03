/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;
import producciones.CharLiteral;
import producciones.CreateArray;
import producciones.CreateVar;
import producciones.FloatLiteral;
import producciones.Function;
import producciones.InitProgram;
import producciones.IntLiteral;
import producciones.Operation;
import producciones.OperationSentence;
import producciones.Parameters;
import producciones.Plus;
import producciones.Sentence;
import producciones.Sentences;
import producciones.StringLiteral;

/**
 *
 * @author snerd
 */
public class Codigo3Direcciones {
    private InitProgram program; //Objeto que guarda todas las sentencias del código fuente
    private String codigo3d = ""; //String que va a guardar el código
    private int temp; //Contador de los temporales que se van a usar
    private Vector<Vector<String>> tablaSimbolos = new Vector<Vector<String>>();
    
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
               cont_param += 1;
            }
            codigo3d += "end params\n";
            generarBloque(tempFuncion.getBlock().getSentences());
            codigo3d += "func end " + nombreF + "\n\n";
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
        temp+=1;
        return dato;
    }
}
