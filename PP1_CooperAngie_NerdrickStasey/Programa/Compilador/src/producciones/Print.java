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
public class Print extends Sentence {
    private Operation op;
    private int position[];
    
    public Print(Operation pOp, int pos[]){
        op = pOp;
        position = pos;
    }
    
    public Operation getOperation(){return op;}
    
    public int[] getPosition(){return position;}
}
