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
    private InitProgram program; //Objeto que guarda todas las sentencias del código fuente
    private boolean hayErrores = false; //Bandera para saber si se encuentra algún error en el código fuente
    
    
    //Entrada: Recibe un objeto de tipo InitProgram
    //Salida: Retorna un objeto de la clase AnalizadorSemántico
    //Objetivo: Ser el constructor de la clase
    public AnalizadorSemantico(InitProgram pProgram){
        program = pProgram;
    }
    
    //Entrada: No tiene entradas
    //Salida: Retorna un booleano indicando si el código fuente cumple con las reglas semánticas
    //Objetivo: Realizar el análisis semántico del código fuente
    public boolean verficar(){
        hayErrores = false;
        if(!mainExisteReturn()) return false;
        if(!FuncionExisteReturn()) return false;
        if(!verificacionNombreFunciones()) return false;
        validarScopeFuncion(program.getFunctions().getFunctions());
        validarScopeMain(program.getMain());
        if(hayErrores) return false;
        return true;
    }
    
    //Entrada: Recibe un vector con las funciones del programa
    //Salida: Retorna un booleano indicando si el scope de las funciones está bien
    //Objetivo: Realizar el análisis semántico en cada una de las sentencias del scope de cada función
    public void validarScopeFuncion(Vector<Function> funciones){
        for(Function tempFuncion: funciones){
            Vector<Parameters> parametros = tempFuncion.getParameterList().getParameters(); //Guarda los parámetros de la función en un vector
            Vector<CreateVar> variablesLocales = new Vector<CreateVar>(); //Se van a ir guardando las variables que se crean en la función
            Vector<CreateArray> arraysLocales = new Vector<CreateArray>();
            validarBloque(tempFuncion.getBlock().getSentences(), variablesLocales, parametros, arraysLocales, tempFuncion);
        }
    }
    
    //Entrada: Recibe el main del programa
    //Salida: Retorna un booleano indicando si el scope del main está bien
    //Objetivo: Realizar el análisis semántico en cada una de las sentencias del scope de cada sentencia main
    public boolean validarScopeMain(Main main){
        Vector<Parameters> parametros = new Vector<Parameters>();  //Guarda los parámetros de la función en un vector
        Vector<CreateVar> variablesLocales = new Vector<CreateVar>(); //Se van a ir guardando las variables que se crean en la función
        Vector<CreateArray> arraysLocales = new Vector<CreateArray>();
        
        Function functMain = new Function(new IntType(), null, null, main.getBlock(), null);
        validarBloque(main.getBlock().getSentences(), variablesLocales, parametros, arraysLocales, functMain);

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
    
    //Entrada: Recibe un vector con las variables, los parámetros y los arreglos de una función y el nombre del identificador
    //del que se quiere obtener el tipo
    //Salida: Retorna un string con el tipo del identificador recibido
    //Objetivo: Obtener el tipo de un identificador dado
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
    
    //Entrada: Recibe un arreglo de los arrays creados en una función y el identificador del array que se desea obtener
    //Salida: Retorna un objeto de tipo CreateArray
    //Objetivo: Buscar en la lista de arrays el array solicitado y retornarlo
    public CreateArray getArray(Vector<CreateArray> arraysLocales, String nombreArray){
        for(CreateArray tempArray: arraysLocales){
            if(nombreArray.equals(tempArray.getIdentifier().getName())) return tempArray;
        }
        return null;
    }
    
    //Entrada: Recibe un vector con las variables locales, otro con los parámetros y otro con los array de una función y el array del que se desea obtener el tipo
    //Salida: Retorna un string con el tipo del arreglo
    //Objetivo: Obtener el tipo de un arreglo
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
    
    //Entrada: Recibe un objeto Sentence que son las sentencias de un bloque de código, así como un un array con las variables, otro con los parámetros y otro con los array de la función y un objeto tipo function
    //Salida: Retorna un booleano indicando si el bloque de código no tiene errores
    //Objetivo: Validar un bloque de código
    public boolean validarBloque(Sentences bloque, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales, Function tempFuncion){
        boolean error = false;
        for(Sentence tempSentence: bloque.getSentences()){
            //Acá se empezarían a validar una a una las sentencias de la función
            //Creación de una variable
            if(tempSentence instanceof CreateVar){
                CreateVar declaracion = (CreateVar)tempSentence;
                if(existeVariable(variablesLocales, parametros, arraysLocales,declaracion.getIdentifier().getName())){
                    imprimirError("la variable " + declaracion.getIdentifier().getName() + " ya ha sido declarada", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                    error = true;
                    hayErrores = true;
                }else{
                    if(declaracion.getOperation() != null){
                        String tipoop = validarOperation(declaracion.getOperation(), variablesLocales, parametros, arraysLocales);
                       if(!declaracion.getType().getTipo().equals(tipoop)){
                           if((!declaracion.getType().getTipo().equals("Integer") && !tipoop.equals("Float")) && (!tipoop.equals("Integer") && !declaracion.getType().getTipo().equals("Float"))){
                              imprimirError("El tipo de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                              error = true;
                              hayErrores = true;
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
                    hayErrores = true;
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
                                    hayErrores = true;
                               }else if (arrL.size() <= arr.getLength()){
                                   imprimirError("El tamaño de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                                   hayErrores = true;
                               }
                               else{
                                   arr.setArrayList(arrL);
                               }
                            }else if((!tipoVar.equals("Integer") && !tipoAsig.equals("Float")) && (!tipoAsig.equals("Integer") && !tipoVar.equals("Float"))){
                                imprimirError("El tipo de la variable y el de su asignación no coinciden", declaracion.getIdentifier().getPosition()[0], declaracion.getIdentifier().getPosition()[1]);
                                hayErrores = true;
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
                   hayErrores = true;
                }
            }
            // Create Array
            // Objetivo: Verifica cuando se crea un arreglo, valida que los datos ingresados coincidan con el arreglo.
            else if(tempSentence instanceof CreateArray){
                CreateArray array = (CreateArray) tempSentence;
                //Verifica que sea un tipo de arreglo válido
                if(array.getType() instanceof IntType || array.getType() instanceof CharType){
                    //Verifica que el arreglo no exista
                    if(getArray(arraysLocales, array.getIdentifier().getName()) == null ){
                        //Verifica el tipo de creación que se está realizando
                        if(array.getArrayList() == null){
                            arraysLocales.add(array);
                        }
                        else{
                            String tipoArrayLis = tipoArrayList(array.getArrayList(), variablesLocales, parametros, arraysLocales);
                            //Verifica que el tipo de la asignación coincida con el tipo del arreglo
                            if( tipoArrayLis != "" ){
                                //Verifica que el tipo de la asignación sea entero o char
                                if(tipoArrayLis.equals("Char") || tipoArrayLis.equals("Integer")){
                                    array.setLength(array.getArrayList().size());
                                    arraysLocales.add(array);
                                }
                                else{
                                    imprimirError("el valor del tipo del arreglo asignado " + array.getIdentifier().getName() + " es inválido.", array.getPosition()[0], array.getPosition()[1]);
                                    hayErrores = true;
                                }
                            }
                            else{
                                imprimirError("los valores del arreglo " + array.getIdentifier().getName() + " no son del mismo tipo.", array.getPosition()[0], array.getPosition()[1]);
                                hayErrores = true;
                            }
                        }
                        
                    }
                    else{
                        imprimirError("el nombre del arreglo " + array.getIdentifier().getName() + " ya existe.", array.getPosition()[0], array.getPosition()[1]);
                        hayErrores = true;
                    }
                }
                else{
                    imprimirError("el valor del tipo del arreglo " + array.getIdentifier().getName() + " es inválido.", array.getPosition()[0], array.getPosition()[1]);
                    hayErrores = true;
                }
            }
            //ModifyArray
            //Objetivo: Verifica cuando se modifica una arreglo previamente creado. El tipo de datos asignados
            //debe coincider con los del tipo de arreglo.
            else if(tempSentence instanceof ModifyArray){
                ModifyArray modifyArray = (ModifyArray) tempSentence;
                
                //Verifica que el arreglo exista
                if( (getArray(arraysLocales, modifyArray.getIdentifier().getName())) == null ){
                    imprimirError("El arreglo " + modifyArray.getIdentifier().getName() + " no existe.", modifyArray.getPosition()[0], modifyArray.getPosition()[1]);
                    hayErrores = true;
                }
                else{
                    CreateArray createArray = getArray(arraysLocales, modifyArray.getIdentifier().getName());
                    //Verifica que la posición del arreglo que se indicó sea válida
                    if(modifyArray.getArrayPos() < createArray.getLength()){
                        //Verifica que el tipo del valor asignado coincida con el el tipo del arreglo.
                        if(!createArray.getType().getTipo().equals(validarOperation(modifyArray.getOperation(),variablesLocales, parametros, arraysLocales))){
                            imprimirError("el valor del tipo en la asignación " + modifyArray.getIdentifier().getName() + " no coincide con el del arreglo.", modifyArray.getPosition()[0], modifyArray.getPosition()[1]);
                            hayErrores = true; 
                        }
                    }
                    else{
                        imprimirError("La posición del arreglo " + createArray.getIdentifier().getName() + " es inválida.", createArray.getPosition()[0], createArray.getPosition()[1]);
                        hayErrores = true;
                    }
                }
                
            //For    
            //Objetivo: Valida que se pueda realizar un for con los datos ingresados. Valida si el tipo
            // que debe tener la variable, condición y manejo coinciden con lo que acepta For.
            }
            else if(tempSentence instanceof For){
                For forSentence = ((For) tempSentence);
                Sentences sentences = new Sentences();
                sentences.addSentence(forSentence.getStructure().getVar());
                
                CreateVar forCreateVar = ((CreateVar)forSentence.getStructure().getVar());
                Operation forCondition = forSentence.getStructure().getCondition();
                Operation forManager = forSentence.getStructure().getManageVar();
                
                if(existeVariable(variablesLocales, parametros, arraysLocales, forCreateVar.getIdentifier().getName())){
                    imprimirError("la variable ya existe. ", forSentence.getPosition()[0], forSentence.getPosition()[1]);
                    hayErrores = true;
                }
                else {
                    //Verifica que se pueda crear la variable
                    if(forCreateVar.getType().getTipo().equals(validarOperation(forCreateVar.getOperation(),variablesLocales, parametros, arraysLocales))) {
                        Vector<CreateVar> variablesTemp = variablesLocales;
                        variablesTemp.add(forCreateVar);
                        //Verifica que la condición retorne un valor booleano.
                        if( validarOperation(forCondition,variablesTemp, parametros, arraysLocales).equals("Boolean")) {
                            //verifica que en el manejo se utilice una operación unaria
                            if (forManager instanceof MinusMinus || forManager instanceof PlusPlus) {
                                validarBloque(forSentence.getSentences().getSentences(),variablesTemp, parametros, arraysLocales, tempFuncion);
                            }
                            else{
                                imprimirError("la operación es inválida. ", forCreateVar.getPosition()[0], forCreateVar.getPosition()[1]);
                                hayErrores = true;
                            }
                        }
                        else {
                            imprimirError("la condición es inválida. ", forSentence.getPosition()[0], forSentence.getPosition()[1]);
                            hayErrores = true;
                        }
                    }
                    else {
                        imprimirError("el valor del tipo cuando se asigna la variable es inválido.", forCreateVar.getPosition()[0], forCreateVar.getPosition()[1]);
                        hayErrores = true;
                    }
                }
            }
            //Return
            //Se valida que el tipo del return coincida con el de la función
            else if(tempSentence instanceof Return){
               Return declaracion = (Return)tempSentence; 
               String returnF = tempFuncion.getType().getTipo();
               String returnr = validarOperation(declaracion.getReturnOp(), variablesLocales, parametros, arraysLocales);
               if(!returnF.equals(returnr)){
                 imprimirError("El tipo del valor de retorno no coincide con tipo de la función", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 hayErrores = true;  
               }
            }
            //Print
            else if(tempSentence instanceof Print){
                Print declaracion = (Print)tempSentence;
                String tipop = validarOperation(declaracion.getOperation(), variablesLocales, parametros, arraysLocales);
                if(!tipop.equals("String") && !tipop.equals("Integer") && !tipop.equals("Float")){
                 imprimirError("La función print no admite el tipo de valor enviado", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 hayErrores = true;
                }
            }
            //Read
            else if(tempSentence instanceof Read){
               Read declaracion = (Read)tempSentence; 
               String tipop = getIdentifierType(variablesLocales, parametros, arraysLocales,declaracion.getVarName().getName());
               if(!tipop.equals("String") && !tipop.equals("Integer") && !tipop.equals("Float")){
                 imprimirError("La función read no admite el tipo de valor enviado", declaracion.getPosition()[0], declaracion.getPosition()[1]);
                 hayErrores = true;
                }
            }
        }
        return error;
    }
    
    //Entrada: Recibe el nombre del array y un vector con los arrays de la función
    //Salida: Retorna un string con el tipo y el tamaño del array
    //Objetivo: Obtener el tipo y el tamaño del array
    public String[] obtenerTipoyLengthArray(String nombreArray, Vector<CreateArray> arraysLocales){
        CreateArray arr = null;
        for(CreateArray tempArray: arraysLocales){
            if(nombreArray.equals(tempArray.getIdentifier().getName())) arr = tempArray;    
        }
        int length = arr.getArrayListSize();
        return new String[] {arr.getType().getTipo(), String.valueOf(length)};
    } 
    
    //Entrada: Recibe un vector con las variables de la función y el nombre de la variable que se desea validar
    //Salida: Retorna un booleano indicando si la variable ya fue asignada
    //Objetivo: Verificar que una variable ya haya sido asignada
    public boolean isVariableAsignada(Vector<CreateVar> variablesLocales, Vector<Parameters> parametros,String varName){
        for(CreateVar tempVar: variablesLocales){
            if(varName.equals(tempVar.getIdentifier().getName())){
                if(tempVar.getOperation() != null) return true;
                break;
            }
        }
        for(Parameters tempPar: parametros){
            if(varName.equals(tempPar.getIdentifier().getName())){ return true;}
        }
        return false;
    }
    
    //Entrada: Recibe unn objeto de tipo Operation y un vector con las variables, otro con los parámetros y otro con los arrays de la función
    //Salida: Retorna un string con el tipo de la operación
    //Objetivo: Obtener el tipo de una operación
    public String validarOperation(Operation op, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros, Vector<CreateArray> arraysLocales){
        String tipo = "";
        //Se valida la suma
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida la resta
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida la división
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida la multiplicación
        if((op instanceof Multi)){
            System.out.println("Mult");
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida pow
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el módulo
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el auto incremento
        if((op instanceof PlusPlus)){
            PlusPlus sentencia = (PlusPlus)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer")){
                 tipo = operationType;
             }else{
                 imprimirError("el autoincremento solo se puede aplicar a números enteros", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 hayErrores = true;
             }   
            }
        }
        //Se valida el decremento
        if((op instanceof MinusMinus)){
            MinusMinus sentencia = (MinusMinus)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer")){
                 tipo = operationType;
             }else{
                 imprimirError("el autodecremento solo se puede aplicar a números enteros", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 hayErrores = true;
             }   
            }
        }
        //Se valida el negativo
        if((op instanceof MinusUnary)){
            MinusUnary sentencia = (MinusUnary)op;
            String operationType = validarOperation(sentencia.getIdent(), variablesLocales, parametros, arraysLocales);
            if(!operationType.equals("")){
             if (operationType.equals("Integer") || operationType.equals("Float")){
                 tipo = operationType;
             }else{
                 imprimirError("el negativo solo se puede aplicar a números enteros y reales", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 hayErrores = true;
             }   
            }
        }
        //Se valida el menor
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el menor igual
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el mayor
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el mayor igual
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el igual
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el diferente
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
                 hayErrores = true;
             }   
            }
        }
        //Se valida el and
        if((op instanceof And)){
            And sentencia = (And)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if (leftType.equals("Boolean") && rigthType.equals("Boolean")){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones lógicas solo se pueden aplicar a valores booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 hayErrores = true;
             }   
            }
        }
        //Se valida el or
        if((op instanceof Or)){
            Or sentencia = (Or)op;
            String leftType = validarOperation(sentencia.getLeftOperation(), variablesLocales, parametros, arraysLocales);
            String rigthType = validarOperation(sentencia.getRightOperation(), variablesLocales, parametros, arraysLocales);
            if(!leftType.equals("") && !rigthType.equals("")){
             if (leftType.equals("Boolean") && rigthType.equals("Boolean")){
                 tipo = "Boolean";
             }else{
                 imprimirError("Las expresiones lógicas solo se pueden aplicar a valores booleanos", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                 hayErrores = true;
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
        //Se valida que los identificadores existan y que hayan sido asignados
        else if(op instanceof Identifier){
            Identifier identifier = (Identifier)op;
            String varName = identifier.getName();
            if(existeVariable(variablesLocales, parametros, arraysLocales, varName)){
                tipo = getIdentifierType(variablesLocales, parametros, arraysLocales, varName);
                if(!tipo.equals("Array") && !isVariableAsignada(variablesLocales, parametros, varName)){
                    tipo = "";
                    imprimirError("la variable " + varName + " aun no ha sido asignada", identifier.getPosition()[0], identifier.getPosition()[1]);
                    hayErrores = true;
                }
            }   
            else imprimirError("la variable " + varName + " no existe", identifier.getPosition()[0], identifier.getPosition()[1]);
            hayErrores = true;
        }
        else if(op instanceof BoolLiteral){
            tipo = "Boolean";
        }
        //Se valida la posicion de una array
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
                    hayErrores = true;
                }
            }else if(tp.equals("")){
                imprimirError("la variable " + sentencia.getIdentifier() + " no existe", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                hayErrores = true;
            }
            else{
                imprimirError("la variable " + sentencia.getIdentifier() + " no puede ser accedida por posiciones", sentencia.getPosition()[0], sentencia.getPosition()[1]);
                hayErrores = true;
            }
            //Que exista el array
            //Que la posicion sea valida
        }
        else if(op instanceof ArrayListAssigment){
            tipo = "ArrayList";
        }
     
        //CallFuntion
        //Salida: Retorna el tipo que tiene la función que se llama
        //Objetivo: Verifica que la función exista, y los parámetros y tipos ue se envían sean correctos
        else if(op instanceof CallFunction){
            CallFunction functionActual = (CallFunction)op;
            boolean isFunction = false;
            for(Function function : program.getFunctions().getFunctions()){
                if(function.getIdentifier().getName().equals(functionActual.getName())){ //verifica que la función exista
                    isFunction = true;
                    //Verifica que la cantidad de parametros que se agregan al llamar la función coincida con los que tiene la función
                    if(function.getParameterList().size() == 0 && functionActual.getParameterList() == null){
                        tipo = function.getType().getTipo();
                    } else if(function.getParameterList().size() == 0 && functionActual.getParameterList() != null){
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        hayErrores = true;
                        break;
                    } else if(function.getParameterList().size() != 0 && functionActual.getParameterList() == null){
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        hayErrores = true;
                        break;
                    } else if(function.getParameterList().size() == functionActual.getParameterList().size()){
                        Vector<Operation> paramsCall = functionActual.getParameterList().getParameterList();
                        Vector<Parameters> paramsFunc = function.getParameterList().getParameters();
                        for(int i=0; i < paramsCall.size(); i++){
                            //Verifica que los parámetros sean del mismo tipo
                            if(!paramsFunc.get(i).getType().getTipo().equals(validarOperation(paramsCall.get(i), variablesLocales, parametros, arraysLocales)) ){
                                imprimirError("El tipo de los parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                                tipo = "";
                                hayErrores = true;
                                break;
                               
                            }
                        }
                        tipo = function.getType().getTipo();
                    }
                    else{
                        imprimirError("La cantidad de parámetros de la función " + functionActual.getName() + " no coincide.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                        tipo = "";
                        hayErrores = true;
                        break;
                    }
                }
            }
            if(!isFunction){
                imprimirError("La función " + functionActual.getName() + " no existe.", functionActual.getPosition()[0], functionActual.getPosition()[1]);
                tipo = "";
                hayErrores = true;
            }
        }
        else if(op instanceof NullLiteral){
            tipo = "Null";
        }
        return tipo;
    }
    
    //Entrada: Tipo de error, fila y columna donde está el error
    //Salida: Mensaje de error en consola
    //Objetivo: Forma el mensaje de error con los parámetros ingresados
    public void imprimirError(String tipoError, int fila, int columna){
        System.err.println("Error semántico, " + tipoError  +" Ver fila " + fila + " columna " + columna);
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si las funciones tienen un return
    //Objetivo: Envía a la función tieneSentenciaReturn las sentencias de código del main
    public boolean mainExisteReturn(){
        return tieneSentenciaReturn(program.getMain().getBlock().getSentences());
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si las funciones tienen un return
    //Objetivo: Envía a la función tieneSentenciaReturn las sentencias de código del main
    public boolean FuncionExisteReturn(){
        for(Function function : program.getFunctions().getFunctions()){
            return tieneSentenciaReturn(function.getBlock().getSentences());
        }
        return false;
    }
    
    //Entrada: Sentencia de código
    //Salida: Retorna un booleano indicando si las funciones tienen un return
    //Objetivo: Recorre las funciones y envía las sentencias de una función a una función que verifica si hay una sentencia return
    public boolean tieneSentenciaReturn(Sentences sentencesList) {
        for(Sentence sentence : sentencesList.getSentences()) {
            if(sentence instanceof Return) return true;
        }
        System.err.println("La función no tiene valor de retorno.");
        hayErrores = true;
        return false;
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si hay funciones con el mismo nombre
    //Objetivo: Recorre las funciones y guarda el nombre en una lista para verificar si los nombres se repiten
    public boolean verificacionNombreFunciones(){
        Vector nombreFunciones = new Vector();
        for(Function function : program.getFunctions().getFunctions()){
            String nombre = function.getIdentifier().getName();
            if(nombreFunciones.contains(nombre)) {
                System.err.println("El nombre de la función " + nombre + " está repetido.");
                hayErrores = true;
                return false;
            }
            else nombreFunciones.add(nombre);
        }
        return true;
    }
}
