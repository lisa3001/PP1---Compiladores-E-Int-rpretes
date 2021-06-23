/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.ArrayList;
import producciones.*;

/**
 *
 * @author snerd
 */
public class Codigo3Direcciones {
    private InitProgram program; //Objeto que guarda todas las sentencias del código fuente
    private String codigo3d = ""; //String que va a guardar el código
    private List<String> temp; //Contador de los temporales que se van a usar
    private List<String> tempRenov = new ArrayList<String>(); //Contador de los temporales que se van a usar
    private List<String> tempAnidado = new ArrayList<String>(); //Contador de los temporales que se van a usar
    private Vector<Vector<String>> tablaSimbolos = new Vector<Vector<String>>();
    private String printOp = "";
    private boolean anidado = false;
    private int corteTabla = 0;
    private int extras = 0;
    private int ifcont = 0;
    private int elifcont = 0;
    private int elsecont = 0;
    private int forcont = 0;
    private int varcont = 0;
    private int assingcont = 0;
    private int arraycont = 0;
    private int returncont = 0;
    
    
    //Entrada: Recibe un objeto de tipo InitProgram
    //Salida: Retorna un objeto de la clase Codigo3Direcciones
    //Objetivo: Ser el constructor de la clase
    public Codigo3Direcciones(InitProgram pProgram){
        program = pProgram;
    }
    
    
    public String getCodigo3D(){
        return codigo3d;
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
    
    
    public String getTablaDeSimbolosDataType(String identificador){
        for(Vector<String> i: tablaSimbolos){
            if(i.elementAt(1).equals(identificador)) return i.elementAt(2);
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
            temp = new ArrayList<String>(Arrays.asList("t0","t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9"));
            tablaSimbolos = new Vector<Vector<String>>();
            extras = 0;
            codigo3d += "func begin main \n";
            generarBloque(main.getBlock().getSentences());
            codigo3d += "func end main \n\n";
            ifcont = 0;
            elifcont = 0;
            elsecont = 0;
            forcont = 0;
            varcont = 0;
            assingcont = 0;
            arraycont = 0;
            returncont = 0;
    }
    
    
    //Entrada: Recibe un vector con objetos Function que son las funciones del código fuente
    //Salida: No tiene
    //Objetivo: Generar el código de 3 direcciones del scope de cada función 
    public void generarFuncionesScope(Vector<Function> funciones){
        for(Function tempFuncion: funciones){
            temp = new ArrayList<String>(Arrays.asList("t0","t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9"));
            tablaSimbolos = new Vector<Vector<String>>();
            extras = 0;
            String nombreF = tempFuncion.getIdentifier().getName();
            codigo3d += "func begin " + nombreF + "\n";
            Vector<Parameters> parametros = tempFuncion.getParameterList().getParameters(); //Guarda los parámetros de la función en un vector
            codigo3d += "begin params\n";
            int cont_param = 0;
            for(Parameters temppam: parametros){
               String nombreP = nombreF + "_" + temp.get(0) + "_fpa";
               codigo3d += nombreP + " = " + temppam.getIdentifier().getName() + "\n";
               Vector<String> v1 = new Vector<String>();
               v1.add(nombreP);
               v1.add(temppam.getIdentifier().getName());
               v1.add(temppam.getType().getTipo());
               tablaSimbolos.add(v1);
               cont_param += 1;
               temp = cutHead(temp);
            }
            codigo3d += "end params\n";
            generarBloque(tempFuncion.getBlock().getSentences());
            codigo3d += "func end " + nombreF + "\n\n";
            ifcont = 0;
            elifcont = 0;
            elsecont = 0;
            forcont = 0;
            varcont = 0;
            assingcont = 0;
            arraycont = 0;
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
               union(temp, tempRenov);//temp.addAll(tempRenov);
               tempRenov = new ArrayList<String>();
                if (temp.size() == 0){
                    temp.add("s" + String.valueOf(extras));
                    extras += 1;
                }
               String var = name + "_" + temp.get(0);
               if(valor == ""){
                   if (declaracion.getType().getTipo().equals("Integer") || declaracion.getType().getTipo().equals("Float") || declaracion.getType().getTipo().equals("Boolean")){
                      codigo3d += var + " = " + "null_int" + "\n";
                   }else{
                      codigo3d += var + " = " + "null_str" + "\n"; 
                   }
               }
               else codigo3d += var + " = " + valor + "\n";
               Vector<String> v1 = new Vector<String>();
               v1.add(var);
               v1.add(name);
               v1.add(declaracion.getType().getTipo());
               tablaSimbolos.add(v1);
               varcont+=1;
               if (anidado){
                   tempAnidado.add(temp.get(0));
               }
               if (temp.size() > 0){
                temp = cutHead(temp);   
               }
            }
            //Asignacion
            else if(tempSentence instanceof AssignVar){
                AssignVar declaracion = (AssignVar)tempSentence;
                String valor = generarOperacion(declaracion.getOperation(), "as");
                String var = getTablaDeSimbolosData(declaracion.getIdentifier().getName());
                if (!var.equals(valor))codigo3d += var + " = " + valor + "\n";
                assingcont+=1;
                union(temp, tempRenov);//temp.addAll(tempRenov);
                tempRenov = new ArrayList<String>();
            }
            //Operacion
            if(tempSentence instanceof OperationSentence){
                OperationSentence declaracion = (OperationSentence)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                generarOperacion(op, "as");
                union(temp, tempRenov);//temp.addAll(tempRenov);
                tempRenov = new ArrayList<String>();
            }
            else if(tempSentence instanceof For){
                anidado = true;
                corteTabla = tablaSimbolos.size();
                For forSentence = (For) tempSentence;
                Sentences sentences = new Sentences();
                sentences.addSentence(forSentence.getStructure().getVar());
                CreateVar forCreateVar = ((CreateVar)forSentence.getStructure().getVar());
                Operation forCondition = forSentence.getStructure().getCondition();
                Operation forManager = forSentence.getStructure().getManageVar();
                
                int forNumber = forcont;
                forcont += 1;
                
                codigo3d += "begin_for_" + forNumber + "\n";
                String tempCreateVar = generarOperacion(forCreateVar.getOperation(), "as");
                String nombre = "as_" + temp.get(0);
                
                if (temp.size() > 0){
                temp = cutHead(temp);   
                }
                
                Vector<String> v1 = new Vector<String>();
                v1.add(nombre);
                v1.add(forCreateVar.getIdentifier().getName());
                v1.add(forCreateVar.getType().getTipo());
                tablaSimbolos.add(v1);
                
                codigo3d += nombre + " = " + tempCreateVar + "\n";  
                
                codigo3d += "FOR_" + forNumber + ":\n";
                String tempConditional = generarOperacion(forCondition, "as");
                String nombre2 = "as_" + temp.get(0);
                
                if (temp.size() > 0){
                temp = cutHead(temp);   
               }
                
                codigo3d += nombre2 + " = " + tempConditional + "\n"; 
                codigo3d += "if_go !(" + nombre2 + ") goto (end_for_" +forNumber+ ")\n";
                
                generarBloque(forSentence.getSentences().getSentences());
                
                String tempUnary = generarOperacion(forManager, "as");
                codigo3d += nombre + " = " + tempUnary + "\n";                 
                codigo3d += "goto (FOR_" + forNumber + ")\n";


                anidado = false;
                union(temp, tempAnidado);//temp.addAll(tempAnidado);
                tempAnidado = new ArrayList<String>();
                reiniciarTabla();
                corteTabla = 0;
                codigo3d += "goto (For_" + forNumber + ")\n";
                codigo3d += "end_for_" + forNumber + ":\n";
                
            }
            //Return
            else if(tempSentence instanceof Return){
                Return declaracion = (Return)tempSentence;
                String valor = generarOperacion(declaracion.getReturnOp(), "return" + String.valueOf(returncont));
                codigo3d += "return " + valor + "\n";
                returncont+=1;
                union(temp, tempRenov);//temp.addAll(tempRenov);
                tempRenov = new ArrayList<String>();
            }
            //Break
            else if(tempSentence instanceof Break){
                Break declaracion = (Break)tempSentence;
                codigo3d += "break"+ "\n";
            }
            //Print
            else if(tempSentence instanceof Print){
                Print declaracion = (Print)tempSentence;
                String valor = generarOperacion(declaracion.getOperation(), "print");
                codigo3d += "print_param" + " = " + valor + "\n";
                codigo3d += "call " + "print" + "_" + printOp + "\n";
                union(temp, tempRenov);//temp.addAll(tempRenov);
                tempRenov = new ArrayList<String>();
            }
            //Read
            else if(tempSentence instanceof Read){
                Read declaracion = (Read)tempSentence;
                String var = getTablaDeSimbolosData(declaracion.getVarName().getName());
                printOp = getTablaDeSimbolosDataType(declaracion.getVarName().getName());
                if (printOp.equals("Integer") || printOp.equals("Boolean")){
                    printOp="int";
                }else{
                    printOp="str";
                }
                codigo3d += "read_param" + " = " + var + "\n";
                codigo3d += "call " + "read" + "_" + printOp + "\n";
                if (printOp.equals("int"))codigo3d += var + " = " + "return \n";
            }
            //CreateArray
            else if(tempSentence instanceof CreateArray){
                CreateArray array = (CreateArray) tempSentence;
                String tipo = array.getType().getTipo();
                codigo3d += "createarray_" + array.getIdentifier().getName() + "_" + array.getLength() + "\n";
                arraycont++;
            }
            else if(tempSentence instanceof If){
                anidado = true;
                corteTabla = tablaSimbolos.size();
                If ifSentence = ((If) tempSentence);
                int ifNumber = ifcont;
                ifcont++;
                int elseNumber = elsecont;
                Vector<String> elifNumbers = new Vector<String>();
                String condition = generarOperacion(ifSentence.getOperation(), "as");
                union(temp, tempRenov);//temp.addAll(tempRenov);
                tempRenov = new ArrayList<String>();
                codigo3d += "if_go (" + condition + ") goto (IF_" + ifNumber + ")\n";
                if (ifSentence.getElifSentences().size() > 0){
                    for(Elif elifSentence: ifSentence.getElifSentences()){
                        String elifNumber = "ELIF_" + elifcont;
                        elifNumbers.add(elifNumber);
                        String elifCondition = generarOperacion(elifSentence.getOperation(), "as");
                        union(temp, tempRenov);//temp.addAll(tempRenov);
                        tempRenov = new ArrayList<String>();
                        elifcont++;
                        codigo3d += "if_go (" + elifCondition + ") goto (" + elifNumber + ")\n";
                    }
                }
                if (!(ifSentence.getElseSentences() == null )){
                    codigo3d += "goto (ELSE_" + elseNumber + ")\n";
                    elsecont++;
                    
                }
                codigo3d += "IF_" + ifNumber + ":\n";
                generarBloque(ifSentence.getIfSentences());
                union(temp, tempAnidado);//temp.addAll(tempAnidado);
                tempAnidado = new ArrayList<String>();
                reiniciarTabla();
                // salto final
                codigo3d += "goto (IF_"+ifNumber+"_END)\n";
                
                if (ifSentence.getElifSentences().size() > 0){
                    int i = 0;
                    for(Elif elifSentence: ifSentence.getElifSentences()){
                        codigo3d += elifNumbers.get(i) + ":\n";
                        generarBloque(elifSentence.getSentences());
                        union(temp, tempAnidado);//temp.addAll(tempAnidado);
                        tempAnidado = new ArrayList<String>();
                        reiniciarTabla();
                        // salto final
                        codigo3d += "goto (IF_"+ifNumber+"_END)\n";
                        i++;
                    }
                    
                }
                if (!(ifSentence.getElseSentences() == null )){
                    codigo3d += "ELSE_" + elseNumber + ":\n";
                    generarBloque(ifSentence.getElseSentences());
                    union(temp, tempAnidado);//temp.addAll(tempAnidado);
                    tempAnidado = new ArrayList<String>();
                    reiniciarTabla();
                }
                // final
                codigo3d += "IF_"+ifNumber+"_END:\n";
                anidado = false;
                corteTabla = 0;
            }
        }
    }
    
    public void union(List<String> lista1, List<String> lista2){
        for(String i:lista2){
            if(!lista1.contains(i)){
                lista1.add(i);
            }
        }
    }
    
    public void reiniciarTabla(){
       int contador = 0;
       Vector<Vector<String>> nueva = new Vector<Vector<String>>();
       for(Vector<String> i: tablaSimbolos){
           if (contador == corteTabla) break;
           else{
               nueva.add(i);
               contador +=1;
           }
       }
       tablaSimbolos = nueva;
    }
    
    //Entrada: Un objeto Operation
    //Salida: String con el resultado de la operacion
    //Objetivo: Generar el código de 3 direcciones de la operacion
    public String generarOperacion(Operation op, String identificador){
        String dato = "";
        if (temp.size() == 0){
            temp.add("s" + String.valueOf(extras));
            extras += 1;
        }
        String temporal = temp.get(0);
        int uso = 0;
        if((op instanceof Plus)){
            Plus sentencia = (Plus)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " + " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Minus)){
            Minus sentencia = (Minus)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " - " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Multi)){
            Multi sentencia = (Multi)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " * " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Divide)){
            Divide sentencia = (Divide)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " / " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Module)){
            Module sentencia = (Module)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " ~ " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Power)){
            Power sentencia = (Power)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " ** " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Minor)){
            Minor sentencia = (Minor)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " < " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof MinorEqual)){
            MinorEqual sentencia = (MinorEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            // << es <=, se hace así para facilitar la creación de mips
            codigo3d += var + " = " + leftType + " << " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Greater)){
            Greater sentencia = (Greater)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " > " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof GreaterEqual)){
            GreaterEqual sentencia = (GreaterEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            // << es <=, se hace así para facilitar la creación de mips
            codigo3d += var + " = " + leftType + " >> " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof EqualEqual)){
            EqualEqual sentencia = (EqualEqual)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            // @@ es ==, se hace así para facilitar la creación de mips
            codigo3d += var + " = " + leftType + " @@ " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Different)){
            Different sentencia = (Different)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            // @! es !=, se hace así para facilitar la creación de mips
            codigo3d += var + " = " + leftType + " !@ " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof MinusUnary)){
            MinusUnary sentencia = (MinusUnary)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + "-" + leftType + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        if((op instanceof PlusPlus)){
            PlusPlus sentencia = (PlusPlus)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = leftType;
            codigo3d += var + " = " + "++" + leftType + "\n";
            dato = var;
        }
        if((op instanceof MinusMinus)){
            MinusMinus sentencia = (MinusMinus)op;
            String leftType = generarOperacion(sentencia.getIdent(), identificador);
            String var = leftType;
            codigo3d += var + " = " + "--" + leftType + "\n";
            dato = var;
        }
        if((op instanceof And)){
            And sentencia = (And)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " & " + rigthType + "\n";
            dato = var;
        }
        if((op instanceof Or)){
            Or sentencia = (Or)op;
            String leftType = generarOperacion(sentencia.getLeftOperation(), identificador);
            String rigthType = generarOperacion(sentencia.getRightOperation(), identificador);
            String var = "";
            if (!isInTabla(leftType))var = leftType;
            else if (!isInTabla(rigthType))var = rigthType;
            else{
                var = temporal;
                uso = 1;
                tempRenov.add(temporal);
            }
            codigo3d += var + " = " + leftType + " | " + rigthType + "\n";
            dato = var;
        }
        else if(op instanceof IntLiteral){
            printOp = "int";
            IntLiteral sentencia = (IntLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        else if(op instanceof FloatLiteral){
            printOp = "int";
            FloatLiteral sentencia = (FloatLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String nuevoValor = "";
            for(int i = 0; i< valor.length(); i++){
                char letra = valor.charAt(i);
                if(letra == '.') break;
                else nuevoValor+= String.valueOf(letra);
                
            }            
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + nuevoValor + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        else if(op instanceof StringLiteral){
            printOp = "str";
            StringLiteral sentencia = (StringLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + valor + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        else if(op instanceof CharLiteral){
            printOp = "str";
            CharLiteral sentencia = (CharLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            String var = identificador + "_" + temporal;
            codigo3d += var + " = '" + valor + "'\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        else if(op instanceof BoolLiteral){
            BoolLiteral sentencia = (BoolLiteral)op;
            String valor = String.valueOf(sentencia.getValue());
            int mipsValor;
            if(valor == "true") mipsValor = 1;
            else mipsValor = 0;
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + mipsValor + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        else if(op instanceof Identifier){
           Identifier identifier = (Identifier)op;
           String var = getTablaDeSimbolosData(identifier.getName());
           dato = var;
           if(identificador.contains("param")){
               String var1 = identificador + "_" + temporal;
               codigo3d += var1 + " = " + var + "\n";
               uso = 1;
               tempRenov.add(temporal);
           }
           printOp = getTablaDeSimbolosDataType(identifier.getName());
           if (printOp.equals("Integer") || printOp.equals("Boolean") || printOp.equals("Float")){
               printOp="int";
           }else{
               printOp="str";
           }
        }
        else if(op instanceof ArrayPositionOperation){
            ArrayPositionOperation sentencia = (ArrayPositionOperation)op;
            String valor = String.valueOf(sentencia.getIdentifier());
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + valor + "[" + String.valueOf(sentencia.getArrayPos()) + "]" + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
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
               generarOperacion(op1, "param" + contador);
               contador+=1;
           }
           codigo3d += "call " + name + "\n";
           String var = identificador + "_" + temporal;
           codigo3d += var + " = " + "return \n";
           dato = var;
           uso = 1;
           tempRenov.add(temporal);
        }
        else if(op instanceof NullLiteral){
            String var = identificador + "_" + temporal;
            codigo3d += var + " = " + "null" + "\n";
            dato = var;
            uso = 1;
            tempRenov.add(temporal);
        }
        //temp+=1;
        if (temp.size() > 0 && uso == 1){
         temp = cutHead(temp);   
        }
        return dato;
    }
    
    public boolean isInTabla(String ident){
       for(Vector<String> i: tablaSimbolos){
            if(i.elementAt(1).equals(ident) || i.elementAt(0).equals(ident)) return true;
       }
       return false; 
    }
    
    public List<String> cutHead(List<String> lista){
        List<String> nueva = new ArrayList<String>();
        int index = 1;
        while(index < lista.size()){
            nueva.add(lista.get(index));
            index += 1;
        }
        return nueva;
    }
}
