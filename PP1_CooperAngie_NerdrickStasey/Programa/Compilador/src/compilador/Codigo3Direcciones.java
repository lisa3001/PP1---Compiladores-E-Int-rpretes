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
    private int elifcont = 0;
    private int forcont = 0;
    private int varcont = 0;
    private int assingcont = 0;
    private int arraycont = 0;
    private int marraycont = 0;
    private int returncont = 0;
    
    
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
       generarMain(program.getMain());
       guardarCodigo3D();
    }
    
   
    //Entrada: Recibe el main del código fuente
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones del scope del main 
    public void generarMain(Main main){
            codigo3d += "func begin main \n";
            generarBloque(main.getBlock().getSentences());
            codigo3d += "func end main \n\n";
            ifcont = 0;
            forcont = 0;
            varcont = 0;
            assingcont = 0;
            arraycont = 0;
            marraycont = 0;
            returncont = 0;
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
            elifcont = 0;
            forcont = 0;
            varcont = 0;
            assingcont = 0;
            arraycont = 0;
            marraycont = 0;
            returncont = 0;
        }
    }
 
 
    //Entrada: Un objeto Sentence con las sentencias de código de un bloque de código
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones d¿e las sentencias de código de un bloque
    public void generarBloque(Sentences bloque){
        for(Sentence tempSentence: bloque.getSentences()){
            //Crear variables
            if(tempSentence instanceof CreateVar){
               CreateVar declaracion = (CreateVar)tempSentence;
               String valor = generarOperacion(declaracion.getOperation(), "var" + String.valueOf(varcont));
               String name = declaracion.getIdentifier().getName();
               String var = name + "_var";
               if(valor == "")codigo3d += var + " = " + "null" + "\n";
               else codigo3d += var + " = " + valor + "\n";
               Vector<String> v1 = new Vector<String>();
               v1.add(var);
               v1.add(name);
               tablaSimbolos.add(v1);
               varcont+=1;
            }
            //Asignacion
            else if(tempSentence instanceof AssignVar){
                AssignVar declaracion = (AssignVar)tempSentence;
                String valor = generarOperacion(declaracion.getOperation(), "assing" + String.valueOf(assingcont));
                String var = getTablaDeSimbolosData(declaracion.getIdentifier().getName());
                codigo3d += var + " = " + valor + "\n";
                assingcont+=1;
            }
            //Operacion
            if(tempSentence instanceof OperationSentence){
                OperationSentence declaracion = (OperationSentence)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                generarOperacion(op, "op");
            }
            else if(tempSentence instanceof For){
                For forSentence = (For) tempSentence;
                Sentences sentences = new Sentences();
                sentences.addSentence(forSentence.getStructure().getVar());
                CreateVar forCreateVar = ((CreateVar)forSentence.getStructure().getVar());
                Operation forCondition = forSentence.getStructure().getCondition();
                Operation forManager = forSentence.getStructure().getManageVar();
                
                int forNumber = forcont;
                forcont += 1;
                
                codigo3d += "begin for_" + forNumber + "\n";
                String nombre = "for_t" + forcont;
                forcont += 1;
                
                Vector<String> v1 = new Vector<String>();
                v1.add(nombre);
                v1.add(forCreateVar.getIdentifier().getName());
                tablaSimbolos.add(v1);
                String tempCreateVar = generarOperacion(forCreateVar.getOperation(), "for_op");
                
                codigo3d += nombre + " = " + tempCreateVar + "\n";  
                
                String nombre2 = "for_t" + forcont;
                forcont += 1; 
                String tempConditional = generarOperacion(forCondition, "for_op");
                codigo3d += nombre2 + " = " + tempConditional + "\n"; 
                codigo3d += "if !(" + nombre2 + ") goto (end for)" + "\n";
                
                generarBloque(forSentence.getSentences().getSentences());
                
                String tempUnary = generarOperacion(forManager, "for_op");
                codigo3d += nombre + " = " + tempUnary + "\n"; 
                
                codigo3d += "goto (for_" + forNumber + ")\n";
                codigo3d += "end for_" + forNumber + "\n";   
            }
            //Return
            else if(tempSentence instanceof Return){
                Return declaracion = (Return)tempSentence;
                String valor = generarOperacion(declaracion.getReturnOp(), "return" + String.valueOf(returncont));
                codigo3d += "return " + valor + "\n";
                returncont+=1;
            }
            //Print
            else if(tempSentence instanceof Print){
                Print declaracion = (Print)tempSentence;
                String valor = generarOperacion(declaracion.getOperation(), "print");
                codigo3d += "print_param" + " = " + valor + "\n";
                codigo3d += "call " + "print, 1" + "\n";
            }
            //Read
            else if(tempSentence instanceof Read){
                Read declaracion = (Read)tempSentence;
                String var = getTablaDeSimbolosData(declaracion.getVarName().getName());
                codigo3d += "read_param" + " = " + var + "\n";
                codigo3d += "call " + "read, 1" + "\n";
                String var2 =  "read_t" + temp;
                codigo3d += var2 + " = " + "return \n";
                temp+=1;
            }
            else if(tempSentence instanceof If){
                If ifSentence = ((If) tempSentence);
                int ifNumber = ifcont;
                ifcont++;
                int elseNumber = ifcont;
                ifcont++;
                Vector<String> elifNumbers = new Vector<String>();
                String condition = generarOperacion(ifSentence.getOperation(), "if_op");
                codigo3d += "if (" + condition + ") goto (IF_" + ifNumber + ")\n";
                if (ifSentence.getElifSentences().size() > 0){
                    for(Elif elifSentence: ifSentence.getElifSentences()){
                        String elifNumber = "ELIF_" + elifcont;
                        elifNumbers.add(elifNumber);
                        String elifCondition = generarOperacion(elifSentence.getOperation(), "elif_op");
                        elifcont++;
                        codigo3d += "if (" + elifCondition + ") goto (" + elifNumber + ")\n";
                    }
                }
                if (!(ifSentence.getElseSentences() == null )){
                    codigo3d += "goto (ELSE_" + elseNumber + ")\n";
                }
                codigo3d += "IF_" + ifNumber + ":\n";
                generarBloque(ifSentence.getIfSentences());
                
                if (ifSentence.getElifSentences().size() > 0){
                    int i = 0;
                    for(Elif elifSentence: ifSentence.getElifSentences()){
                        codigo3d += elifNumbers.get(i) + ":\n";
                        generarBloque(elifSentence.getSentences());
                        i++;
                    }
                }
                if (!(ifSentence.getElseSentences() == null )){
                    codigo3d += "ELSE_" + elseNumber + ":\n";
                    generarBloque(ifSentence.getElseSentences());
                }
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
        if((op instanceof PlusPlus)){
            PlusPlus sentencia = (PlusPlus)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + "++" + leftType + "\n";
            dato = var;
        }
        if((op instanceof Minus)){
            PlusPlus sentencia = (PlusPlus)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = identificador + "_t" + temp;
            codigo3d += var + " = " + "--" + leftType + "\n";
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
               contador+=1;
           }
           codigo3d += "call " + name + ", " + String.valueOf((contador)) + "\n";
           String var = identificador + "_t" + temp;
           codigo3d += var + " = " + "return \n";
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
