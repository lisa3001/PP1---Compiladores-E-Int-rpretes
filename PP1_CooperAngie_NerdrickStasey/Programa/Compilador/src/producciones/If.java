/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producciones;

import java.util.Vector;

/**
 *
 * @author snerd
 */
public class If extends Sentence{
    private Operation operation;
    private Sentences ifSentences;
    private Vector<Elif> elifStatement;
    private Sentences elseSentences;
    private int position[];
    
    public If(Operation pOperation, Sentences ifS, Vector<Elif> elifS, Sentences elseS, int pos[]){
        operation = pOperation;
        ifSentences = ifS;
        elifStatement = elifS;
        elseSentences = elseS;
        position = pos;
    }
    
    public Operation getOperation(){return operation;}
    
    public Sentences getIfSentences(){ return ifSentences;}
    
    public Vector<Elif> getElifSentences(){ return elifStatement;}
    
    public Sentences getElseSentences(){ return elseSentences;}
    
    public int[] getPosition(){return position;}
}
