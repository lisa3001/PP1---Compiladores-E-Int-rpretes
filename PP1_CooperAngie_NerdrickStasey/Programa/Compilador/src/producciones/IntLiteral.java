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
public class IntLiteral extends Operation {
    private int number;
    private int position[];
    
    
    public IntLiteral(int pNumber, int pPosition[]){
        number = pNumber;
        position = pPosition;
    }
    
    public int getValue(){ return number;}
    
    public int[] getPosition(){ return position;}
}
