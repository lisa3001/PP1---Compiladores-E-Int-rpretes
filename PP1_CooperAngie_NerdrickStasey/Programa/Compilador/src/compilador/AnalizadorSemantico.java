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
        if(!verificacionNombreFunciones()) return false;
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
