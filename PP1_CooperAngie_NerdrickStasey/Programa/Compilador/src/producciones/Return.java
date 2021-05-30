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
public class Return extends Sentence {
    private Operation returnOp;
    private int position[];
    
    public Return(Operation op, int pos[]){
        returnOp = op;
        position = pos;
    }
    
    public Operation getReturnOp(){return returnOp;}
    
    public int[] getPosition(){return position;}
}
