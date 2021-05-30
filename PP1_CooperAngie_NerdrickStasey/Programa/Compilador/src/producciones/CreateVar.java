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
public class CreateVar extends Sentence {
    private Type type;
    private Identifier name;
    private Operation operation;
    private int position[];
    
    public CreateVar(Type pType, Identifier pName, Operation pOperation, int pPosition[]){
        type = pType;
        name = pName;
        operation = pOperation;
        position = pPosition;
    }
    
    public Type getType(){return type;}
    
    public Identifier getIdentifier(){return name;}
    
    public Operation getOperation(){return operation;}
    
    public int[] getPosition(){return position;}
}
