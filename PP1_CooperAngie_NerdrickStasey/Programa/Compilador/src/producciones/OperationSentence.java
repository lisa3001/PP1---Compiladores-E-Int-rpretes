/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producciones;

/**
 *
 * @author snerd
 */
public class OperationSentence extends Sentence {
    private Operation operation;
    private int position[];
    
    public OperationSentence(Operation op, int pos[]){
        operation = op;
    }
    
    public Operation getOperation(){return operation;}
}
