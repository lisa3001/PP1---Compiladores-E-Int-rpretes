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
    private int hayErrores = false;
    
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
            validarBloque(tempFuncion.getBlock().getSentences(), variablesLocales, parametros);
        }
        
        return true;
    }
    
    public boolean validarBloque(Sentences bloque, Vector<CreateVar> variablesLocales, Vector<Parameters> parametros){
        for(Sentence tempSentence: bloque.getSentences()){
            //Acá se empezarían a validar una a una las sentencias de la función
        }
        return true;
    }
    
    //Entrada: No tiene
    //Salida: Retorna un booleano indicando si el main tiene un return
    //Objetivo: Le envía las sentencias del main a una función que verifica si hay una sentencia return
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
