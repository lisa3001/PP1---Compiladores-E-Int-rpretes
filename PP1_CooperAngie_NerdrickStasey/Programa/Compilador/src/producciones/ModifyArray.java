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
public class ModifyArray extends Sentence {
    private Identifier identifier;
    private int arrayPosition;
    private Operation operation;
    private int position[];
    
    public ModifyArray(Identifier pIdentifier, int pArrayPos, Operation pOperation, int pPosition[]){
        identifier = pIdentifier;
        arrayPosition = pArrayPos;
        operation = pOperation;
        position = pPosition;
       
    }
    
    public Identifier getIdentifier(){return identifier;}
    
    public int getArrayPos(){return arrayPosition;}
    
    public Operation getOperation(){return operation;}
    
    public int[] getPosition(){return position;}
}
