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
public class Negation extends Operation {
    private Operation rigthOperation;
    private int position[];
    
    public Negation(Operation pRigthOperation, int pPosition[]){
    rigthOperation = pRigthOperation;
    position = pPosition;
    }
    
    public Operation getOperation(){ return rigthOperation;}
    
    public int[] getPosition() {return position;}
}
