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
    private int hayErrores = 0;
    
    public AnalizadorSemantico(InitProgram pProgram){
        program = pProgram;
    }
    
    public boolean verficar(){
        if(!mainExisteReturn()) return false;
        if(!FuncionExisteReturn()) return false;
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
            validarBloque(tempFuncion.getBlock().getSentences());
        }
        
        return true;
    }
    
    public boolean validarBloque(Sentences bloque){
        for(Sentence tempSentence: bloque.getSentences()){
            //Acá se empezarían a validar una a una las sentencias de la función
        }
        return true;
    }
    
    public boolean mainExisteReturn(){
        return tieneSentenciaReturn(program.getMain().getBlock().getSentences());
    }
    
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
    
}
