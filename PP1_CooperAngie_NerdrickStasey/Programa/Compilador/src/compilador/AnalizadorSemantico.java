/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Vector;
import producciones.*;

/**
 *
 * @author snerd
 */
public class AnalizadorSemantico {
    private InitProgram program;
    private boolean hayErrores = false;
    
    public AnalizadorSemantico(InitProgram pProgram){
        program = pProgram;
    }
    
    public boolean verficar(){
        if(!mainExisteReturn()) return false;
        if(!FuncionExisteReturn()) return false;
        if(!verificacionNombreFunciones()) return false;
        if(!validarScopeFuncion(program.getFunctions().getFunctions())) return false;
        return true;
    }
    
    //Entrada: Recibe un vector con las funciones del programa
    //Salida: Retorna un booleano indicando si el scope de las funciones está bien
    //Objetivo: Realizar el análisis semántico en cada una de las sentencias del scope de cada función
    public boolean validarScopeFuncion(Vector<Function> funciones){
        
        for(Function tempFuncion: funciones){
            Vector<Parameters> parametros = tempFuncion.getParameterList().getParameters(); //Guarda los parámetros de la función en un vector
            Vector<CreateVar> variablesLocales = new Vector<CreateVar>(); //Se van a ir guardando las variables que se crean en la función
            Vector<CreateArray> arraysLocales = new Vector<CreateArray>();
            validarBloque(tempFuncion.getBlock().getSentences(), variablesLocales, parametros, arraysLocales);
        }
        
        return true;
    }
    
    //Entrada: Recibe un vector con las variables creadas y los parametros de una función, y la variable que se desea buscar
    //Salida: Retorna un booleano indicando si la variable ya fue creada o no
    //Objetivo: validar si en la lista de variables de una función o en los parámetros ya existe la variable que se desea crear
    public boolean existeVariable(Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales,String varName){
        for(CreateVar tempVar: variablesLocales){
            if(varName.equals(tempVar.getIdentifier().getName())) return true;
        }
        for(CreateArray tempArray: arraysLocales){
            if(varName.equals(tempArray.getIdentifier().getName())) return true;
        }
        for(Parameters tempParam: parametros){
            if(varName.equals(tempParam.getIdentifier().getName())) return true;
        }
        return false;
    }
    
    public String getIdentifierType(Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales,String varName){
        for(CreateVar tempVar: variablesLocales){
            if(varName.equals(tempVar.getIdentifier().getName())){
                return tempVar.getType().getTipo();
            }
        }
        for(CreateArray tempArray: arraysLocales){
            if(varName.equals(tempArray.getIdentifier().getName())){
                return "Array";
            }
        }
        for(Parameters tempParam: parametros){
            if(varName.equals(tempParam.getIdentifier().getName())){
                return tempParam.getType().getTipo();
            }
        }
        return "";
    }
    
    public boolean validarBloque(Sentences bloque, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales){
        for(Sentence tempSentence: bloque.getSentences()){
            //Acá se empezarían a validar una a una las sentencias de la función
            //Creación de una variable
            if(tempSentence instanceof CreateVar){
                CreateVar declaracion = (CreateVar)tempSentence;
                if(existeVariable(variablesLocales, parametros, arraysLocales,declaracion.getIdentifier().getName())){
                    imprimirError("la variable " + declaracion.getIdentifier().getName() + " ya ha sido declarada", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                    //Cambiar bandera de error acá
                }else{
                    variablesLocales.add(declaracion);
                } //Hay que validar la operacion del lado izquierdo
            }
            //Assignación de una variable
            else if(tempSentence instanceof AssignVar){
                AssignVar declaracion = (AssignVar)tempSentence;
                if(!existeVariable(variablesLocales, parametros, arraysLocales,declaracion.getIdentifier().getName())){
                    imprimirError("la variable " + declaracion.getIdentifier().getName() + " no existe", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                    //Cambiar bandera de error acá
                }//Hay que validar la operacion del lado izquierdo
            } 
            else if(tempSentence instanceof OperationSentence){
                OperationSentence declaracion = (OperationSentence)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                System.out.println("La operacion es tipo");
                System.out.println(validarOperation(op, variablesLocales, parametros, arraysLocales));
                
            }
        }
        return true;
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si el main tiene un return
    //Objetivo: Le envía las sentencias del main a una función que verifica si hay una sentencia return
    public String[] obtenerTipoyLengthArray(String nombreArray, Vector<CreateArray> arraysLocales){
        CreateArray arr = null;
        for(CreateArray tempArray: arraysLocales){
            if(nombreArray.equals(tempArray.getIdentifier().getName())) arr = tempArray;    
        }
        int length = arr.getArrayListSize();
        return new String[] {arr.getType().getTipo(), String.valueOf(length)};
    } 
    
    public String validarOperation(Operation op, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales){
        String tipo = "";
        if((op instanceof Plus)){
            Plus sentencia = (Plus)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de suma", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Minus)){
            Minus sentencia = (Minus)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de resta", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Divide)){
            Divide sentencia = (Divide)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de división", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Multi)){
            Multi sentencia = (Multi)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de multiplicación", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Power)){
            Power sentencia = (Power)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de potencia", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Module)){
            Module sentencia = (Module)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 if(rigthType.equals("Float") || leftType.equals("Float")) tipo = "Float";
                 else tipo = "Integer";
             }else{
                 imprimirError("el tipo de uno de los operandos no es aceptado para la operación de módulo", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof PlusPlus)){
            PlusPlus sentencia = (PlusPlus)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer")){
                 tipo = operationType;
             }else{
                 imprimirError("el autoincremento solo se puede aplicar a números enteros", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof MinusMinus)){
            MinusMinus sentencia = (MinusMinus)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer")){
                 tipo = operationType;
             }else{
                 imprimirError("el autodecremento solo se puede aplicar a números enteros", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof MinusUnary)){
            MinusUnary sentencia = (MinusUnary)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer") || operationType.equals("Float")){
                 tipo = operationType;
             }else{
                 imprimirError("el negativo solo se puede aplicar a números enteros y reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Minor)){
            Minor sentencia = (Minor)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros o reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof MinorEqual)){
            MinorEqual sentencia = (MinorEqual)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros o reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Greater)){
            Greater sentencia = (Greater)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros o reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof GreaterEqual)){
            GreaterEqual sentencia = (GreaterEqual)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros o reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof EqualEqual)){
            EqualEqual sentencia = (EqualEqual)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float") || leftType.equals("Boolean"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float") || rigthType.equals("Boolean"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros, reales o booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Different)){
            Different sentencia = (Different)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if ((leftType.equals("Integer") || leftType.equals("Float") || leftType.equals("Boolean"))
                     && (rigthType.equals("Integer") || rigthType.equals("Float") || rigthType.equals("Boolean"))){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones relacionales solo se puede aplicar a enteros, reales o booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof And)){
            And sentencia = (And)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if (leftType.equals("Boolean") && rigthType.equals("Boolean")){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones lógicas solo se pueden aplicar a valores booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        if((op instanceof Or)){
            Or sentencia = (Or)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if (leftType.equals("Boolean") && rigthType.equals("Boolean")){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones lógicas solo se pueden aplicar a valores booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 //Bander de error, activarla
             }   
            }
        }
        else if(op instanceof IntLiteral){
            tipo = "Integer";
        }
        else if(op instanceof FloatLiteral){
            tipo = "Float";
        }
        else if(op instanceof StringLiteral){
            tipo = "String";
        }
        else if(op instanceof CharLiteral){
            tipo = "Char";
        }
        else if(op instanceof Identifier){
            Identifier identifier = (Identifier)op;
            String varName = identifier.getName();
            if(existeVariable(variablesLocales, parametros, arraysLocales, varName))
                tipo = getIdentifierType(variablesLocales, parametros, arraysLocales, varName);
            else imprimirError("la variable " + varName + " no existe", identifier.getPosition()[0], identifier.getPosition()[1]);
            //Agregar bandera de error
        }
        else if(op instanceof BoolLiteral){
            tipo = "Boolean";
        }
        else if(op instanceof ArrayPositionOperation){
            ArrayPositionOperation sentencia = (ArrayPositionOperation)op;
            String tp = getIdentifierType(variablesLocales, parametros, arraysLocales, sentencia.getIdentifier());
            if (tp.equals("Array")){
                String info[] = obtenerTipoyLengthArray(sentencia.getIdentifier(), arraysLocales);
                int largoArray = Integer.parseInt(info[1]);
                if(sentencia.getArrayPos() < largoArray){
                    tipo = info[0];
                }else{
                    imprimirError("Posición inválida", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                    //agregar bandera error
                }
            }else if(tp.equals("")){
                imprimirError("la variable " + sentencia.getIdentifier() + " no existe", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                //agregar bandera error
            }
            else{
                imprimirError("la variable " + sentencia.getIdentifier() + " no puede ser accedida por posiciones", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                //agregar bandera error
            }
            //Que exista el array
            //Que la posicion sea valida
        }
        else if(op instanceof ArrayListAssigment){
            tipo = "ArrayList";
        }
     
        //Salida: Retorna el tipo que tiene la función que se llama
        //Objetivo: Verifica que la función exista, y los parámetros y tipos ue se envían sean correctos
        else if(op instanceof CallFunction){
            CallFunction functionActual = (CallFunction)op;
            for(Function function : program.getFunctions().getFunctions()){
                if(function.getIdentifier().getName().equals(functionActual.getName())){ //verifica que la función exista
                    if(function.getParameterList().size() == functionActual.getParameterList().size()){
                        Vector<Operation> paramsCall = functionActual.getParameterList().getParameterList();
                        Vector<Parameters> paramsFunc = function.getParameterList().getParameters();
                        for(int i=0; i < paramsCall.size(); i++){
                            if(!paramsFunc.get(i).getType().getTipo().equals(validarOperation(paramsCall.get(i), variablesLocales, parametros, arraysLocales)) ){
                                imprimirError("El tipo de los parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                                return  function.getType().getTipo();
                            }
                        }
                        return function.getType().getTipo();
                    }
                    else{
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        return function.getType().getTipo();
                    }
                }
            }
            imprimirError("La función " + functionActual.getName() + " no existe.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
            return "Null";
        }
        else if(op instanceof NullLiteral){
            tipo = "Null";
        }
        return tipo;
    }
    
    public void imprimirError(String tipoError, int fila, int columna){
        System.err.println("Error semántico, " + tipoError  +" ver fila " + fila + " columna " + columna);
    }
    
    public boolean mainExisteReturn(){
        return tieneSentenciaReturn(program.getMain().getBlock().getSentences());
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si las funciones tienen un return
    //Objetivo: Recorre las funciones y envía las sentencias de una función a una función que verifica si hay una sentencia return
    public boolean FuncionExisteReturn(){
        for(Function function : program.getFunctions().getFunctions()){
            return tieneSentenciaReturn(function.getBlock().getSentences());
        }
        return false;
    }
    
    public boolean tieneSentenciaReturn(Sentences sentencesList) {
        for(Sentence sentence : sentencesList.getSentences()) {
            if(sentence instanceof Return) return true;
        }
        return false;
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si hay funciones con el mismo nombre
    //Objetivo: Recorre las funciones y guarda el nombre en una lista para verificar si los nombres se repiten
    public boolean verificacionNombreFunciones(){
        Vector nombreFunciones = new Vector();
        for(Function function : program.getFunctions().getFunctions()){
            String nombre = function.getIdentifier().getName();
            if(nombreFunciones.contains(nombre)) return false;
            else nombreFunciones.add(nombre);
        }
        return true;
    }
}
