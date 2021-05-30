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
public class ArrayPositionOperation extends Operation {
    private String identifier;
    private int arrayPos;
    private int position[];
    
    public ArrayPositionOperation(String pIdentifier, int pArrayPos, int pPosition[]){
        identifier = pIdentifier;
        arrayPos = pArrayPos;
        position = pPosition;
    }
    
    public String getIdentifier(){return identifier;}
    
    public int getArrayPos(){return arrayPos;}
    
    public int[] getPosition(){return position;}
    
    
}
