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
public class AssignVar extends Sentence {
    private Identifier name;
    private Operation operation;
    private int position[];
    
    public AssignVar(Identifier pName, Operation pOperation, int pPosition[]){
        name = pName;
        operation = pOperation;
        position = pPosition;
    }
        
    public Identifier getIdentifier(){return name;}
    
    public Operation getOperation(){return operation;}
    
    public int[] getPosition(){return position;}
}
