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
            validarBloque(tempFuncion.getBlock().getSentences(), variablesLocales, parametros, arraysLocales, tempFuncion);
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
    
    public CreateArray getArray(Vector<CreateArray> arraysLocales, String nombreArray){
        for(CreateArray tempArray: arraysLocales){
            if(nombreArray.equals(tempArray.getIdentifier().getName())) return tempArray;
        }
        return null;
    }
    
    public String tipoArrayList(ArrayList arrl, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales){
        String tipo = "";
        for (Operation op: arrl.getParameterList()){
            String tp = validarOperation(op, variablesLocales, parametros, arraysLocales);
            if (!tipo.equals("")){
                if (!tipo.equals(tp)){
                    tipo = "";
                    break;
                }
            }else{
                tipo = tp;
            }
        }
        return tipo;
    }
    
    public boolean validarBloque(Sentences bloque, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales, Function tempFuncion){
        for(Sentence tempSentence: bloque.getSentences()){
            //Acá se empezarían a validar una a una las sentencias de la función
            //Creación de una variable
            if(tempSentence instanceof CreateVar){
                CreateVar declaracion = (CreateVar)tempSentence;
                if(existeVariable(variablesLocales, parametros, arraysLocales,declaracion.getIdentifier().getName())){
                    imprimirError("la variable " + declaracion.getIdentifier().getName() + " ya ha sido declarada", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                    //Cambiar bandera de error acá
                }else{
                    if(declaracion.getOperation() != null){
                        String tipoop = validarOperation(declaracion.getOperation(), variablesLocales, parametros, arraysLocales);
                       if(!declaracion.getType().getTipo().equals(tipoop)){
                           if((!declaracion.getType().getTipo().equals("Integer") && !tipoop.equals("Float")) && (!tipoop.equals("Integer") && !declaracion.getType().getTipo().equals("Float"))){
                              imprimirError("El tipo de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                               //Cambiar bandera de error acá 
                           }else{
                             variablesLocales.add(declaracion);  
                           }
                       }
                       else{
                           variablesLocales.add(declaracion);
                       }   
                    }else variablesLocales.add(declaracion);   
               }     
            }
            //Assignación de una variable
            else if(tempSentence instanceof AssignVar){
                AssignVar declaracion = (AssignVar)tempSentence;
                if(!existeVariable(variablesLocales, parametros, arraysLocales,declaracion.getIdentifier().getName())){
                    imprimirError("la variable " + declaracion.getIdentifier().getName() + " no existe", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                    //Cambiar bandera de error acá
                }else{
                  String tipoVar = getIdentifierType(variablesLocales, parametros, arraysLocales, declaracion.getIdentifier().getName());
                  String tipoAsig = validarOperation(declaracion.getOperation(), variablesLocales, parametros, arraysLocales);
                    if(!tipoAsig.equals("")){
                        if (!tipoVar.equals(tipoAsig)){
                            if(tipoVar.equals("Array") && tipoAsig.equals("ArrayList")){
                               CreateArray arr = getArray(arraysLocales, declaracion.getIdentifier().getName());
                               ArrayListAssigment arrLS = (ArrayListAssigment)declaracion.getOperation();
                               ArrayList arrL = (ArrayList)arrLS.getArrayList();
                               String tipoArrl = tipoArrayList(arrL, variablesLocales, parametros, arraysLocales);
                               if(!tipoArrl.equals(arr.getType().getTipo())){
                                   imprimirError("El tipo de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                                    //Cambiar bandera de error acá
                               }else if (arrL.size() <= arr.getLength()){
                                   imprimirError("El tamaño de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                                    //Cambiar bandera de error acá
                               }
                               else{
                                   arr.setArrayList(arrL);
                               }
                            }else if((!tipoVar.equals("Integer") && !tipoAsig.equals("Float")) && (!tipoAsig.equals("Integer") && !tipoVar.equals("Float"))){
                                imprimirError("El tipo de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                                //Cambiar bandera de error acá
                            }
                        }
                    }  
                }   
            } 
            //Operacion
            else if(tempSentence instanceof OperationSentence){
                OperationSentence declaracion = (OperationSentence)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                validarOperation(op, variablesLocales, parametros, arraysLocales);
            }
            //If
            else if(tempSentence instanceof If){
                If declaracion = (If)tempSentence;
                Operation op = (Operation)declaracion.getOperation();
                String opS = validarOperation(op, variablesLocales, parametros, arraysLocales);
                if(opS.equals("Boolean")){
                    validarBloque(declaracion.getIfSentences(), variablesLocales, parametros, arraysLocales, tempFuncion);
                    for (Elif elifTemp: declaracion.getElifSentences()){
                      Operation opElf = (Operation)elifTemp.getOperation();
                      String opSe = validarOperation(opElf, variablesLocales, parametros, arraysLocales);
                      if(opSe.equals("Boolean")){
                        validarBloque(elifTemp.getSentences(), variablesLocales, parametros, arraysLocales, tempFuncion);  
                      }
                    }
                    if(declaracion.getElseSentences() != null) validarBloque(declaracion.getElseSentences(), variablesLocales, parametros, arraysLocales, tempFuncion);
                }else{
                   imprimirError("La condición del if debe retornar un booleano", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                   //Cambiar bandera de error acá 
                }
            }
            // Create Array
            else if(tempSentence instanceof CreateArray){
                CreateArray array = (CreateArray) tempSentence;
                if(array.getArrayList() == null){
                    if(array.getType() instanceof IntType || array.getType() instanceof CharType) System.out.println("Atrapadaaa");
                }
                else{
                
            else if(tempSentence instanceof For){
                
            }
            else if(tempSentence instanceof Return){
               Return declaracion = (Return)tempSentence; 
               String returnF = tempFuncion.getType().getTipo();
               String returnr = validarOperation(declaracion.getReturnOp(), variablesLocales, parametros, arraysLocales);
               if(!returnF.equals(returnr)){
                 imprimirError("El tipo del valor de retorno no coincide con tipo de la función", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 //Cambiar bandera de error acá   
               }
            }
            else if(tempSentence instanceof Print){
                Print declaracion = (Print)tempSentence;
                String tipop = validarOperation(declaracion.getOperation(), variablesLocales, parametros, arraysLocales);
                if(!tipop.equals("String") && !tipop.equals("Integer") && !tipop.equals("Float")){
                 imprimirError("La función print no admite el tipo de valor enviado", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 //Cambiar bandera de error acá 
                }
            }
            else if(tempSentence instanceof Read){
               Read declaracion = (Read)tempSentence; 
               String tipop = getIdentifierType(variablesLocales, parametros, arraysLocales,declaracion.getVarName().getName());
               if(!tipop.equals("String") && !tipop.equals("Integer") && !tipop.equals("Float")){
                 imprimirError("La función read no admite el tipo de valor enviado", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 //Cambiar bandera de error acá 
                }
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
    
    public boolean isVariableAsignada(Vector<CreateVar> variablesLocales, String varName){
        for(CreateVar tempVar: variablesLocales){
            if(varName.equals(tempVar.getIdentifier().getName())){
                if(tempVar.getOperation() != null) return true;
            }
        }
        return false;
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
            if(existeVariable(variablesLocales, parametros, arraysLocales, varName)){
                tipo = getIdentifierType(variablesLocales, parametros, arraysLocales, varName);
                if(!tipo.equals("Array") && !isVariableAsignada(variablesLocales, varName)){
                    tipo = "";
                    imprimirError("la variable " + varName + " aun no ha sido asignada", identifier.getPosition()[0], identifier.getPosition()[1]);
                    //Agregar bandera de error
                }
            }   
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
            boolean isFunction = false;
            for(Function function : program.getFunctions().getFunctions()){
                if(function.getIdentifier().getName().equals(functionActual.getName())){ //verifica que la función exista
                    isFunction = true;
                    if(function.getParameterList().size() == 0 && functionActual.getParameterList() == null){
                        tipo = function.getType().getTipo();
                    } else if(function.getParameterList().size() == 0 && functionActual.getParameterList() != null){
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        break;
                    } else if(function.getParameterList().size() != 0 && functionActual.getParameterList() == null){
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        break;
                    } else if(function.getParameterList().size() == functionActual.getParameterList().size()){
                        Vector<Operation> paramsCall = functionActual.getParameterList().getParameterList();
                        Vector<Parameters> paramsFunc = function.getParameterList().getParameters();
                        for(int i=0; i < paramsCall.size(); i++){
                            if(!paramsFunc.get(i).getType().getTipo().equals(validarOperation(paramsCall.get(i), variablesLocales, parametros, arraysLocales)) ){
                                imprimirError("El tipo de los parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                                tipo = "";
                                break;
                                //Bander de error, activarla
                            }
                        }
                        tipo = function.getType().getTipo();
                    }
                    else{
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        break;
                        //Bander de error, activarla
                    }
                }
            }
            if(!isFunction)imprimirError("La función " + functionActual.getName() + " no existe.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
            tipo = "";
            //Bander de error, activarla
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
