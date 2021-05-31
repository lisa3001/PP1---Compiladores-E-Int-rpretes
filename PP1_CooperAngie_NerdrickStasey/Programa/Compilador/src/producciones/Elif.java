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
public class Elif extends Sentence {
    private Operation operation;
    private Sentences elifSentences;
    private int position[];
    
    public Elif(Operation pOperation, Sentences sentences, int pos[]){
        operation = pOperation;
        elifSentences = sentences;
        position = pos;
        
    }
    
    public Operation getOperation(){ return operation;}
    
    public int[] getPosition(){return position;}
    
    public Sentences getSentences(){return elifSentences;}
}
